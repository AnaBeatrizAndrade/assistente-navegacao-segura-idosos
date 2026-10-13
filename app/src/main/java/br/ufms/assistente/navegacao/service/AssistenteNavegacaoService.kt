package br.ufms.assistente.navegacao.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import br.ufms.assistente.navegacao.ui.MainActivity
import br.ufms.assistente.navegacao.util.UrlHashUtil

class AssistenteNavegacaoService : AccessibilityService() {
    companion object {
        private const val TAG = "AssistenteService"
        private const val CHANNEL_ID = "assistente_foreground_channel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Assistente de Navegação conectado e ativo.")
        criarCanalNotificacao()
        startForeground(NOTIFICATION_ID, construirNotificacaoForeground())
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType != AccessibilityEvent.TYPE_VIEW_CLICKED) return

        val urlCandidata = extrairUrlDoEvento(event) ?: return

        if (!urlCandidata.startsWith("http://") && !urlCandidata.startsWith("https://")) return

        Log.d(TAG, "URL interceptada para análise (apenas em log de debug).")
        enfileirarAnalise(urlCandidata)
    }

    private fun extrairUrlDoEvento(event: AccessibilityEvent): String? {
        event.text?.forEach { texto ->
            val s = texto?.toString()?.trim() ?: return@forEach
            if (s.startsWith("http://") || s.startsWith("https://")) {
                return limparUrl(s)
            }
        }

        val descricao = event.contentDescription?.toString()?.trim()
        if (descricao != null &&
            (descricao.startsWith("http://") || descricao.startsWith("https://"))) {
            return limparUrl(descricao)
        }

        return null
    }

    private fun limparUrl(urlBruta: String): String? {
        return try {
            val uri = Uri.parse(urlBruta)
            Uri.Builder()
                .scheme(uri.scheme)
                .authority(uri.authority)
                .path(uri.path)
                .query(uri.query)
                .build()
                .toString()
        } catch (e: Exception) {
            Log.w(TAG, "URL inválida ignorada.")
            null
        }
    }

    private fun enfileirarAnalise(url: String) {
        val urlHash = UrlHashUtil.gerarHash(url) ?: return

        val inputData = Data.Builder()
            .putString(UrlAnalisadorWorker.KEY_URL, url)
            .putString(UrlAnalisadorWorker.KEY_URL_HASH, urlHash)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(UrlAnalisadorWorker::class.java)
            .setInputData(inputData)
            .addTag("analise_url")
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        Log.d(TAG, "Análise enfileirada no WorkManager.")
    }

    private fun construirNotificacaoForeground(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Proteção ativa")
            .setContentText("Seu assistente de segurança está cuidando de você.")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun criarCanalNotificacao() {
        val canal = NotificationChannel(
            CHANNEL_ID,
            "Assistente em segundo plano",
            NotificationManager.IMPORTANCE_MIN
        ).apply {
            description = "Indica que a proteção de links está ativa."
            setShowBadge(false)
            enableVibration(false)
            enableLights(false)
        }
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(canal)
    }

    override fun onInterrupt() {
        Log.w(TAG, "Assistente de Navegação interrompido pelo sistema.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Assistente de Navegação encerrado.")
    }
}

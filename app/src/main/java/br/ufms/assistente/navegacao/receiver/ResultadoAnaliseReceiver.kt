package br.ufms.assistente.navegacao.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.ui.AlertaActivity

class ResultadoAnaliseReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ResultadoReceiver"

        const val ACTION_RESULTADO = "br.ufms.assistente.navegacao.RESULTADO_ANALISE"
        const val EXTRA_STATUS = "extra_status"
        const val EXTRA_DOMINIO = "extra_dominio"
        const val EXTRA_URGENCIA = "extra_urgencia"
        const val EXTRA_ID_URL = "extra_id_url"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action != ACTION_RESULTADO) return

        val status = intent.getStringExtra(EXTRA_STATUS)
        val dominio = intent.getStringExtra(EXTRA_DOMINIO)
        val urgencia = intent.getStringExtra(EXTRA_URGENCIA)
        val idUrl = intent.getIntExtra(EXTRA_ID_URL, -1)

        Log.d(TAG, "Resultado recebido: status=$status")

        if (urgencia == Alerta.URGENCIA_ALTA || urgencia == Alerta.URGENCIA_MEDIA) {
            val alertaIntent = Intent(context, AlertaActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
                putExtra(AlertaActivity.EXTRA_URGENCIA, urgencia)
                putExtra(AlertaActivity.EXTRA_DOMINIO,  dominio)
                putExtra(AlertaActivity.EXTRA_ID_URL,   idUrl)
            }
            context.startActivity(alertaIntent)
            Log.i(TAG, "AlertaActivity iniciada para domínio: $dominio")
        } else {
            Log.d(TAG, "URL considerada segura — sem interrupção ao usuário")
        }
    }
}
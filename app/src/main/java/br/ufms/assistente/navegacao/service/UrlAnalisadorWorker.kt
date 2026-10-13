package br.ufms.assistente.navegacao.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.ufms.assistente.navegacao.db.DatabaseHelper
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.model.UrlAnalisada
import br.ufms.assistente.navegacao.receiver.ResultadoAnaliseReceiver
import br.ufms.assistente.navegacao.util.SecurePrefsHelper
import br.ufms.assistente.navegacao.util.UrlHashUtil

class UrlAnalisadorWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    companion object {
        const val KEY_URL = "key_url"
        const val KEY_URL_HASH = "key_url_hash"
        private const val TAG = "UrlAnalisadorWorker"
    }

    private val db = DatabaseHelper(context)
    //private val verificadorVuln = VerificadorVulnerabilidades(context)
    //private val verificadorGolpe = VerificadorPadraoGolpe(context)

    private val verificadorVuln = null
    private val verificadorGolpe = null

    override fun doWork(): Result {
        val url     = inputData.getString(KEY_URL)
        val urlHash = inputData.getString(KEY_URL_HASH)

        if (url == null || urlHash == null) {
            Log.w(TAG, "Dados de entrada inválidos")
            return Result.failure()
        }

        Log.d(TAG, "Iniciando análise")

        return try {
            val cache = db.buscarUrlPorHash(urlHash)
            if (cache != null && cache.isCacheValido) {
                Log.d(TAG, "Cache válido encontrado. Status: ${cache.statusSeguranca}")
                dispararResultado(cache, url)
                return Result.success()
            }

            //val isVulneravel = verificadorVuln.verificar(url)

            val statusCalculado = when {
                //isVulneravel -> {
                //    Log.i(TAG, "URL classificada como PERIGOSA pela base de vulnerabilidades")
                //    UrlAnalisada.STATUS_PERIGOSO
                //}
                //verificadorGolpe.verificar(url) -> {
                //    Log.i(TAG, "URL classificada como SUSPEITA por padrão textual")
                //    UrlAnalisada.STATUS_SUSPEITO
                //}
                else -> {
                    Log.d(TAG, "URL considerada SEGURA")
                    UrlAnalisada.STATUS_SEGURO
                }
            }

            val urlAnalisada = UrlAnalisada(urlHash = urlHash, url = url, statusSeguranca = statusCalculado)
            val idUrl = db.inserirOuAtualizarUrl(urlAnalisada)
            val urlComId = urlAnalisada.copy(idUrl = idUrl)

            if (urlComId.requerAlerta) {
                val idUsuario = SecurePrefsHelper.lerInt(
                    applicationContext, SecurePrefsHelper.KEY_USUARIO_ID, 1
                )
                val dominio = UrlHashUtil.extrairDominio(url)
                val alerta = if (urlComId.isPerigoso)
                    Alerta.criarAlertaPerigo(idUsuario, idUrl, dominio)
                else
                    Alerta.criarAlertaSuspeito(idUsuario, idUrl, dominio)

                db.inserirAlerta(alerta)
                Log.i(TAG, "Alerta persistido. Urgência: ${alerta.nivelUrgencia}")
            }

            dispararResultado(urlComId, url)

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Erro inesperado durante análise", e)
            Result.retry()
        } finally {
            db.close()
        }
    }

    private fun dispararResultado(urlAnalisada: UrlAnalisada, urlOriginal: String) {
        val dominio  = UrlHashUtil.extrairDominio(urlOriginal)
        val urgencia = when (urlAnalisada.statusSeguranca) {
            UrlAnalisada.STATUS_PERIGOSO -> Alerta.URGENCIA_ALTA
            UrlAnalisada.STATUS_SUSPEITO -> Alerta.URGENCIA_MEDIA
            else -> ""
        }

        val intent = Intent(ResultadoAnaliseReceiver.ACTION_RESULTADO).apply {
            setPackage(applicationContext.packageName)
            putExtra(ResultadoAnaliseReceiver.EXTRA_STATUS, urlAnalisada.statusSeguranca)
            putExtra(ResultadoAnaliseReceiver.EXTRA_DOMINIO, dominio)
            putExtra(ResultadoAnaliseReceiver.EXTRA_URGENCIA, urgencia)
            putExtra(ResultadoAnaliseReceiver.EXTRA_ID_URL, urlAnalisada.idUrl)
        }
        applicationContext.sendBroadcast(intent)
    }
}
package br.ufms.assistente.navegacao.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

object FamiliarNotificacaoHelper {
    private const val TAG = "FamiliarNotificacao"

    fun notificarFamiliar(
        context: Context,
        telefoneFamiliar: String,
        nomeFamiliar: String,
        nomeIdoso: String
    ) {
        if (telefoneFamiliar.isBlank()) {
            Log.w(TAG, "Telefone do familiar não cadastrado — notificação cancelada")
            return
        }

        val mensagem = montarMensagem(nomeIdoso)

        if (!abrirWhatsApp(context, telefoneFamiliar, mensagem)) {
            abrirSmsNativo(context, telefoneFamiliar, mensagem)
        }
    }

    private fun abrirWhatsApp(context: Context, telefone: String, mensagem: String): Boolean {
        return try {
            var telefoneLimpo = telefone.replace(Regex("[^0-9]"), "")

            if (!telefoneLimpo.startsWith("55") && telefoneLimpo.length <= 11) {
                telefoneLimpo = "55$telefoneLimpo"
            }

            val url =
                "https://api.whatsapp.com/send?phone=$telefoneLimpo&text=${Uri.encode(mensagem)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Log.i(TAG, "WhatsApp aberto para notificar familiar")
            true
        } catch (e: Exception) {
            Log.w(TAG, "WhatsApp não disponível: ${e.message}")
            false
        }
    }

    private fun abrirSmsNativo(context: Context, telefone: String, mensagem: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$telefone")
                putExtra("sms_body", mensagem)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Log.i(TAG, "SMS nativo aberto para notificar familiar")
        } catch (e: Exception) {
            Log.e(TAG, "Não foi possível abrir nenhum app de mensagens", e)
        }
    }

    private fun montarMensagem(nomeIdoso: String): String =
        "Alerta de Segurança\n\n" +
        "$nomeIdoso recebeu um aviso sobre um link possivelmente perigoso no celular.\n\n" +
        "Por favor, entre em contato para ajudá-lo(a) a decidir o que fazer.\n\n" +
        "— Assistente de Navegação Segura"

}
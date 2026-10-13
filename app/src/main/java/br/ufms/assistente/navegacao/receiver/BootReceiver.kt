package br.ufms.assistente.navegacao.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import br.ufms.assistente.navegacao.util.SecurePrefsHelper

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED &&
            action != "android.intent.action.QUICKBOOT_POWERON") return

        Log.i(TAG, "Dispositivo iniciado. Verificando estado do assistente.")

        val ativo = SecurePrefsHelper.lerBoolean(
            context, SecurePrefsHelper.KEY_ASSISTENTE_ATIVO, false
        )

        if (ativo) {
            Log.i(
                TAG, "Assistente estava ativo antes do reboot. " +
                        "O Android Accessibility Service retomará automaticamente."
            )
        }
    }
}
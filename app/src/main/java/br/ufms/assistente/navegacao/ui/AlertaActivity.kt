package br.ufms.assistente.navegacao.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import br.ufms.assistente.navegacao.db.DatabaseHelper
import br.ufms.assistente.navegacao.model.Alerta

class AlertaActivity : ComponentActivity() {

    companion object {
        const val  EXTRA_STATUS = "extra_status"
        const val EXTRA_DOMINIO = "extra_dominio"
        const val EXTRA_URGENCIA = "extra_urgencia"
        const val EXTRA_ID_URL = "extra_id_url"
    }

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        db = DatabaseHelper(this)

        val urgencia = intent.getStringExtra(EXTRA_URGENCIA) ?: Alerta.URGENCIA_MEDIA
        val dominio = intent.getStringExtra(EXTRA_DOMINIO) ?: "link desconhecido"
        val idUrl = intent.getIntExtra(EXTRA_ID_URL, -1)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                TODO("Não implementado ainda")
            }
        })
    }
}


package br.ufms.assistente.navegacao.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Vibrator
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import br.ufms.assistente.navegacao.db.DatabaseHelper
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme
import br.ufms.assistente.navegacao.util.FamiliarNotificacaoHelper
import br.ufms.assistente.navegacao.util.SecurePrefsHelper

class AlertaActivity : ComponentActivity() {

    companion object {
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
                registrarDecisao(idUrl, Alerta.DECISAO_SAIU)
                finish()
            }
        })

        vibrarDiscretamente()

        setContent {
            AssistenteNavegacaoTheme {
                AlertaScreen(
                    urgencia = urgencia,
                    dominio = dominio,
                    nomeFamiliar = SecurePrefsHelper.lerString(
                        this,
                        SecurePrefsHelper.KEY_FAMILIAR_NOME,
                        ""
                    ),
                    nomeIdoso = SecurePrefsHelper.lerString(
                        this,
                        SecurePrefsHelper.KEY_USUARIO_NOME,
                        ""
                    )
                        .trim().split(" ").firstOrNull() ?: "você",
                    onSairSeguro = { registrarDecisao(idUrl, Alerta.DECISAO_SAIU); finish() },
                    onEntrarMesmoAssim = {
                        registrarDecisao(
                            idUrl,
                            Alerta.DECISAO_ENTROU
                        ); finish()
                    },
                    onPedirAjuda = { nomeFam, nomeIdoso ->
                        registrarDecisao(idUrl, Alerta.DECISAO_FAMILIA)
                        val tel = SecurePrefsHelper.lerString(
                            this,
                            SecurePrefsHelper.KEY_FAMILIAR_TELEFONE,
                            ""
                        )
                        FamiliarNotificacaoHelper.notificarFamiliar(this, tel, nomeFam, nomeIdoso)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    private fun registrarDecisao(idUrl: Int, decisao: String) {
        if (idUrl > 0) db.atualizarDecisaoAlerta(idUrl, decisao)
    }

    @Suppress("DEPRECATION")
    private fun vibrarDiscretamente() {
        try {
            val v = getSystemService(VIBRATOR_SERVICE) as? Vibrator
            if (v?.hasVibrator() == true) v.vibrate(longArrayOf(0, 200, 150, 200), -1)
        } catch (_: Exception) {}
    }
}


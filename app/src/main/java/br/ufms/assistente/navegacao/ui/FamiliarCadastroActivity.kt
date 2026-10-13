package br.ufms.assistente.navegacao.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme
import br.ufms.assistente.navegacao.util.SecurePrefsHelper

class FamiliarCadastroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nomeSalvo   = SecurePrefsHelper.lerString(this, SecurePrefsHelper.KEY_FAMILIAR_NOME, "")
        val temCadastro = nomeSalvo.isNotBlank()

        setContent {
            AssistenteNavegacaoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FamiliarCadastroScreen(
                        nomeSalvo   = nomeSalvo,
                        temCadastro = temCadastro,
                        onSalvar    = { nome, telefone ->
                            SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_NOME, nome)
                            if (telefone.isNotBlank()) {
                                SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, telefone)
                            }
                            finish()
                        },
                        onRemover   = {
                            SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_NOME, "")
                            SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, "")
                            SecurePrefsHelper.salvarInt(this, SecurePrefsHelper.KEY_FAMILIAR_ID, -1)
                            finish()
                        },
                        onVoltar    = { finish() }
                    )
                }
            }
        }
    }
}
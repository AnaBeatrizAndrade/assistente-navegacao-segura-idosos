package br.ufms.assistente.navegacao.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme
import br.ufms.assistente.navegacao.util.SecurePrefsHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TelaMain() }
    }

    override fun onResume() {
        super.onResume()
        setContent { TelaMain() }
    }

    @Composable
    private fun TelaMain() {
        AssistenteNavegacaoTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScreen(
                    cadastroConcluido = SecurePrefsHelper.cadastroConcluido(this),
                    servicoAtivo      = isAccessibilityServiceAtivo(),
                    nomeUsuario       = SecurePrefsHelper.lerString(this, SecurePrefsHelper.KEY_USUARIO_NOME, ""),
                    nomeFamiliarSalvo = SecurePrefsHelper.lerString(this, SecurePrefsHelper.KEY_FAMILIAR_NOME, ""),
                    onSalvarCadastro  = { nome, nomeFamiliar, tel ->
                        SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_USUARIO_NOME, nome)
                        SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_NOME, nomeFamiliar)
                        SecurePrefsHelper.salvarString(this, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, tel)
                        SecurePrefsHelper.salvarBoolean(this, SecurePrefsHelper.KEY_PRIMEIRA_EXECUCAO, false)
                    },
                    onAtivarProtecao  = { abrirConfiguracaoAcessibilidade() },
                    onEditarFamiliar  = {
                        startActivity(Intent(this, FamiliarCadastroActivity::class.java))
                    }
                )
            }
        }
    }

    private fun isAccessibilityServiceAtivo(): Boolean {
        val esperado = "$packageName/br.ufms.assistente.navegacao.service.AssistenteNavegacaoService"
        val ativos   = Settings.Secure.getString(
            contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val splitter = TextUtils.SimpleStringSplitter(':').also { it.setString(ativos) }
        return splitter.any { it.equals(esperado, ignoreCase = true) }
    }

    private fun abrirConfiguracaoAcessibilidade() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}
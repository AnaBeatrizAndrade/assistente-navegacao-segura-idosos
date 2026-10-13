package br.ufms.assistente.navegacao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.ufms.assistente.navegacao.util.SecurePrefsHelper
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecurePrefsHelperTest {

    private val context by lazy {
        InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Before
    fun limparAntes() = SecurePrefsHelper.limparTudo(context)

    @After
    fun limparDepois() = SecurePrefsHelper.limparTudo(context)

    @Test
    fun salvarELerNomeUsuario_retornaValorSalvo() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_USUARIO_NOME, "Maria")
        assertEquals("Maria", SecurePrefsHelper.lerString(context, SecurePrefsHelper.KEY_USUARIO_NOME, ""))
    }

    @Test
    fun lerNomeUsuario_semSalvar_retornaPadrao() {
        assertEquals("", SecurePrefsHelper.lerString(context, SecurePrefsHelper.KEY_USUARIO_NOME, ""))
    }

    @Test
    fun salvarELerNomeFamiliar_retornaValorSalvo() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_FAMILIAR_NOME, "Carlos")
        assertEquals("Carlos", SecurePrefsHelper.lerString(context, SecurePrefsHelper.KEY_FAMILIAR_NOME, ""))
    }

    @Test
    fun salvarELerTelefoneFamiliar_retornaValorSalvo() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, "67912345678")
        assertEquals("67912345678", SecurePrefsHelper.lerString(context, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, ""))
    }

    @Test
    fun lerTelefoneFamiliar_semSalvar_retornaPadrao() {
        assertEquals("", SecurePrefsHelper.lerString(context, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, ""))
    }

    @Test
    fun cadastroConcluido_semDados_retornaFalse() {
        assertFalse(SecurePrefsHelper.cadastroConcluido(context))
    }

    @Test
    fun cadastroConcluido_somenteNome_retornaFalse() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_USUARIO_NOME, "Maria")
        assertFalse(SecurePrefsHelper.cadastroConcluido(context))
    }

    @Test
    fun cadastroConcluido_nomEtelefone_retornaTrue() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_USUARIO_NOME, "Maria")
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, "67912345678")
        assertTrue(SecurePrefsHelper.cadastroConcluido(context))
    }

    @Test
    fun limparTudo_removeTodosOsDados() {
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_USUARIO_NOME, "Maria")
        SecurePrefsHelper.salvarString(context, SecurePrefsHelper.KEY_FAMILIAR_TELEFONE, "67912345678")
        SecurePrefsHelper.limparTudo(context)
        assertFalse(SecurePrefsHelper.cadastroConcluido(context))
    }
}
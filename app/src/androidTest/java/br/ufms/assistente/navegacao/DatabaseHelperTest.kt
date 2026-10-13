package br.ufms.assistente.navegacao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.ufms.assistente.navegacao.db.DatabaseHelper
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.model.UrlAnalisada
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseHelperTest {
    private lateinit var db: DatabaseHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase("assistente_navegacao.db")
        db = DatabaseHelper(context)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun buscarUrlInexistente_retornaNull() {
        assertNull(db.buscarUrlPorHash("hash_inexistente"))
    }

    @Test
    fun inserirAlerta_retornaIdPositivo() {
        val url = UrlAnalisada(urlHash = "xyz789", statusSeguranca = UrlAnalisada.STATUS_SUSPEITO)
        val idUrl = db.inserirOuAtualizarUrl(url)

        val alerta = Alerta(
            mensagemAlerta = "Site suspeito",
            urlInterceptadaDominio = "malware.com",
            nivelUrgencia = Alerta.URGENCIA_ALTA,
            decisaoUsuario = Alerta.DECISAO_AGUARDA,
            idUrl = idUrl
        )
        val idAlerta = db.inserirAlerta(alerta)
        assertTrue(idAlerta > 0)
    }

    @Test
    fun atualizarDecisaoAlerta_persisteNovaDecisao() {
        val url = UrlAnalisada(urlHash = "def456", statusSeguranca = UrlAnalisada.STATUS_SUSPEITO)
        val idUrl = db.inserirOuAtualizarUrl(url)

        val alerta = Alerta(
            mensagemAlerta = "Teste",
            urlInterceptadaDominio = "phish.com",
            nivelUrgencia = Alerta.URGENCIA_MEDIA,
            decisaoUsuario = Alerta.DECISAO_AGUARDA,
            idUrl = idUrl
        )
        val idAlerta = db.inserirAlerta(alerta)
        db.atualizarDecisaoAlerta(idAlerta, Alerta.DECISAO_SAIU)
        assertTrue(idAlerta > 0)
    }
}

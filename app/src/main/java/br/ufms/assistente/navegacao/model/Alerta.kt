package br.ufms.assistente.navegacao.model

import java.util.Date

data class Alerta (
    val idAlerta: Int = 0,
    val mensagemAlerta: String = "",
    val urlInterceptadaDominio: String = "",
    val nivelUrgencia: String = URGENCIA_MEDIA,
    val decisaoUsuario: String = DECISAO_AGUARDA,
    val criadoEm: Date = Date(),
    val idUsuario: Int = 0,
    val idUrl: Int = 0
) {

    val isUrgenciaAlta: Boolean
        get() = URGENCIA_ALTA == nivelUrgencia

    val familiarFoiAcionado: Boolean
        get() =  DECISAO_FAMILIA == decisaoUsuario

    fun comDecisao(decisao: String): Alerta = copy(decisaoUsuario = decisao)

    companion object {
        const val URGENCIA_ALTA: String = "ALTA"
        const val URGENCIA_MEDIA: String = "MEDIA"
        const val DECISAO_SAIU: String = "SAIU_COM_SEGURANCA"
        const val DECISAO_ENTROU: String = "ENTROU_MESMO_ASSIM"
        const val DECISAO_FAMILIA: String = "ACIONOU_FAMILIAR"
        const val DECISAO_AGUARDA: String = "AGUARDANDO"

        fun criarAlertaPerigo(idUsuario: Int, idUrl: Int, dominio: String) = Alerta (
            idUsuario = idUsuario,
            idUrl = idUrl,
            urlInterceptadaDominio = dominio,
            nivelUrgencia = URGENCIA_ALTA,
            mensagemAlerta = ("Cuidado! Este link pode ser uma tentativa de golpe.\n\n"
                    + "Ele pode tentar roubar seu dinheiro ou suas informações.\n\n"
                    + "O mais seguro é sair daqui.")
            )

        fun criarAlertaSuspeito(idUsuario: Int, idUrl: Int, dominio: String) = Alerta (
            idUsuario = idUsuario,
            idUrl = idUrl,
            urlInterceptadaDominio = dominio,
            nivelUrgencia = URGENCIA_MEDIA,
            mensagemAlerta = ("Atenção! Não reconhecemos este link.\n\n"
                    + "Se você não esperava receber isso, é melhor não continuar.\n\n"
                    + "Em caso de dúvida, peça ajuda a um familiar.")
        )
    }
}

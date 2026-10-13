package br.ufms.assistente.navegacao.model

data class FamiliarResponsavel (
    val idFamiliar: Int = 0,
    val nome: String = "",
    @Transient val telefone: String = "",
    val email: String = "",
    val idUsuario: Int = 0
) {

    fun montarMensagemAlerta(nomeIdoso: String): String =
        "Alerta de Segurança\n\n" +
        "$nomeIdoso acabou de receber um aviso de possível golpe no celular.\n" +
        "Por favor, entre em contato com $nomeIdoso para ajudá-lo(a) a tomar uma decisão segura.\n\n" +
        "- Assistente de Navegação Segura"
}

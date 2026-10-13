package br.ufms.assistente.navegacao.model

class Usuario (
    val idUsuario: Int = 0,
    val nome: String = ""
){

    val primeiroNome: String
        get() = nome.trim().split("\\s+".toRegex()).firstOrNull()
            ?.takeIf { it.isNotEmpty() } ?: "Usuário"
}

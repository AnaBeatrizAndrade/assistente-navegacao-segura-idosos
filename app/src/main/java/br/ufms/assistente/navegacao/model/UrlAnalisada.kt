package br.ufms.assistente.navegacao.model

import java.nio.file.Files.copy
import java.util.Date

data class UrlAnalisada (
    val idUrl: Int = 0,
    val urlHash: String = "",
    @Transient val url: String = "",
    val statusSeguranca: String = STATUS_PENDENTE,
    val dataVerificacao: Date = Date()
) {

    val isSeguro: Boolean
        get() = STATUS_SEGURO == statusSeguranca

    val isPerigoso: Boolean
        get() = STATUS_PERIGOSO == statusSeguranca

    val isSuspeito: Boolean
        get() = STATUS_SUSPEITO == statusSeguranca

    val requerAlerta: Boolean
        get() = isPerigoso || isSuspeito

    val isCacheValido: Boolean
        get() {
            val diff = Date().getTime() - dataVerificacao.time
            return diff < 24 * 60 * 60 * 1000L
        }

    fun comStatus(status: String): UrlAnalisada = copy(statusSeguranca = status)

    companion object {
        const val STATUS_SEGURO: String = "SEGURO"
        const val STATUS_PERIGOSO: String = "PERIGOSO"
        const val STATUS_SUSPEITO: String = "SUSPEITO"
        const val STATUS_PENDENTE: String = "PENDENTE"
    }
}

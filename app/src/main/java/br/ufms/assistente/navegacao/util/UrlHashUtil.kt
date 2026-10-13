package br.ufms.assistente.navegacao.util

import android.net.Uri
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object UrlHashUtil {
    private const val TAG = "UrlHashUtil"
    private const val ALGORITMO = "SHA-256"

    fun gerarHash(url: String?): String? {
        if (url.isNullOrEmpty()) return null

        return try {
            val digest = MessageDigest.getInstance(ALGORITMO)
            val hashBytes = digest.digest(url.toByteArray(StandardCharsets.UTF_8))
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Algoritmo SHA-256 não disponível" + e)
            null
        }
    }

    fun extrairDominio(url: String?): String {
        if (url.isNullOrEmpty()) return "Link desconhecido"

        return try {
            Uri.parse(url).host?.takeIf { it.isNotEmpty() } ?: "Link desconhecido"
        } catch (e: Exception) {
            "Link desconhecido"
        }
    }
}

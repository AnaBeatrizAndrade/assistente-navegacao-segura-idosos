package br.ufms.assistente.navegacao.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

object SecurePrefsHelper {
    private const val TAG = "SecurePrefsHelper"
    private const val PREFS_FILE = "assistente_secure_prefs"

    const val KEY_USUARIO_NOME = "usuario_nome"
    const val KEY_USUARIO_ID = "usuario_id"
    const val KEY_FAMILIAR_NOME = "familiar_nome"
    const val KEY_FAMILIAR_TELEFONE = "familiar_telefone"
    const val KEY_FAMILIAR_ID = "familiar_id"
    const val KEY_ASSISTENTE_ATIVO = "assistente_ativo"
    const val KEY_PRIMEIRA_EXECUCAO = "primeira_execucao"

    fun getPrefs(context: Context): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao criar EncryptedSharedPreferences: " + e.message)

            context.getSharedPreferences("${PREFS_FILE}_fallback", Context.MODE_PRIVATE)
        }
    }

    fun salvarString(context: Context, chave: String, valor: String) {
        getPrefs(context).edit { putString(chave, valor) }
    }

    fun lerString(context: Context, chave: String, padrao: String): String =
        getPrefs(context).getString(chave, padrao) ?: padrao

    fun salvarInt(context: Context, chave: String, valor: Int) {
        getPrefs(context).edit { putInt(chave, valor) }
    }

    fun lerInt(context: Context, chave: String, padrao: Int): Int =
        getPrefs(context).getInt(chave, padrao)

    fun salvarBoolean(context: Context, chave: String, valor: Boolean) {
        getPrefs(context).edit { putBoolean(chave, valor) }
    }

    fun lerBoolean(context: Context, chave: String, padrao: Boolean): Boolean =
        getPrefs(context).getBoolean(chave, padrao)

    fun limparTudo(context: Context) {
        getPrefs(context).edit { clear() }
        Log.i(TAG, "Dados removidos pelo usuário")
    }

    fun cadastroConcluido(context: Context): Boolean {
        val nome = lerString(context, KEY_USUARIO_NOME, "")
        val telFamiliar = lerString(context, KEY_FAMILIAR_TELEFONE, "")
        return nome.isNotBlank() && telFamiliar.isNotBlank()
    }
}

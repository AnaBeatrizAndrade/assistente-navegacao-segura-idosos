package br.ufms.assistente.navegacao.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.model.UrlAnalisada
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_URL)
        sqLiteDatabase.execSQL(SQL_CREATE_ALERTA)
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_url_hash ON $TABLE_URL($COL_URL_HASH);")
        Log.i(TAG, "Banco de dados criado com sucesso")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_ALERTA")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_URL")
        onCreate(sqLiteDatabase)
    }

    override fun onConfigure(sqLiteDatabase: SQLiteDatabase) {
        super.onConfigure(sqLiteDatabase)
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true)
    }

    fun buscarUrlPorHash(hash: String?): UrlAnalisada? {
        return readableDatabase.use { db ->
            db.query(
                TABLE_URL,
                null,
                "$COL_URL_HASH = ?",
                arrayOf(hash),
                null, null, null, "1"
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    UrlAnalisada(
                        idUrl = cursor.getInt(cursor.getColumnIndexOrThrow(COL_URL_ID)),
                        urlHash = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_HASH)),
                        statusSeguranca = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_STATUS)),
                        dataVerificacao = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_DATA_VERIF))
                            .parseData()
                    )
                } else null
            }
        }
    }

    fun inserirOuAtualizarUrl(urlAnalisada: UrlAnalisada): Int {
        val values = ContentValues().apply {
            put(COL_URL_HASH, urlAnalisada.urlHash)
            put(COL_URL_STATUS, urlAnalisada.statusSeguranca)
            put(COL_URL_DATA_VERIF, Date().format())
        }

        return try {
           writableDatabase.use { db ->
               db.insertWithOnConflict(TABLE_URL, null, values, SQLiteDatabase.CONFLICT_REPLACE).toInt()
           }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inserir/atualizar URL", e)
            -1
        }
    }

    fun inserirAlerta(alerta: Alerta): Int {
        val values = ContentValues().apply {
            put(COL_ALERTA_MENSAGEM, alerta.mensagemAlerta)
            put(COL_ALERTA_DOMINIO, alerta.urlInterceptadaDominio)
            put(COL_ALERTA_URGENCIA, alerta.nivelUrgencia)
            put(COL_ALERTA_DECISAO, alerta.decisaoUsuario)
            put(COL_ALERTA_CRIADO, Date().format())
            put(COL_ALERTA_ID_URL, alerta.idUrl)
        }

        return try {
            writableDatabase.use { db ->
                db.insert(TABLE_ALERTA, null, values).toInt()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inserir alerta", e)
            -1
        }
    }

    fun atualizarDecisaoAlerta(idAlerta: Int, decisao: String?) {
        val values = ContentValues().apply { put(COL_ALERTA_DECISAO, decisao) }
        writableDatabase.use { db ->
            db.update(TABLE_ALERTA, values, "$COL_ALERTA_ID = ?", arrayOf(idAlerta.toString()))
        }
    }

    fun limparCacheAntigo() {
        try {
            writableDatabase.use { db ->
                db.execSQL(
                    "DELETE FROM $TABLE_URL WHERE " +
                            "julianday('now') - julianday($COL_URL_DATA_VERIF) > 30"
                )
            }
            Log.i(TAG, "Cache antigo removido")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao limpar cache", e)
        }
    }

    private fun Date.format(): String = SimpleDateFormat(FORMAT_DATA, Locale.ROOT).format(this)

    private fun String.parseData(): Date =
        try {
            SimpleDateFormat(FORMAT_DATA, Locale.ROOT).parse(this) ?: Date()
        }
        catch (e: Exception) {
            Date()
        }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val DB_NAME = "assistente_navegacao.db"
        private const val DB_VERSION = 1
        private const val FORMAT_DATA = "yyyy-MM-dd HH:mm:ss"

        const val TABLE_URL = "url_analisada"
        const val COL_URL_ID = "id_url"
        const val COL_URL_HASH = "url_hash"
        const val COL_URL_STATUS = "status_seguranca"
        const val COL_URL_DATA_VERIF = "data_verificacao"

        const val TABLE_ALERTA = "alerta"
        const val COL_ALERTA_ID = "id_alerta"
        const val COL_ALERTA_MENSAGEM = "mensagem_alerta"
        const val COL_ALERTA_DOMINIO = "url_dominio"
        const val COL_ALERTA_URGENCIA = "nivel_urgencia"
        const val COL_ALERTA_DECISAO = "decisao_usuario"
        const val COL_ALERTA_CRIADO = "criado_em"
        const val COL_ALERTA_ID_URL = "id_url"

        private val SQL_CREATE_URL = """
            CREATE TABLE IF NOT EXISTS $TABLE_URL (
                $COL_URL_ID         INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_URL_HASH       TEXT NOT NULL UNIQUE,
                $COL_URL_STATUS     TEXT NOT NULL,
                $COL_URL_DATA_VERIF TEXT NOT NULL
            );
        """.trimIndent()

        private val SQL_CREATE_ALERTA = """
            CREATE TABLE IF NOT EXISTS $TABLE_ALERTA (
                $COL_ALERTA_ID       INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ALERTA_MENSAGEM TEXT,
                $COL_ALERTA_DOMINIO  TEXT,
                $COL_ALERTA_URGENCIA TEXT NOT NULL,
                $COL_ALERTA_DECISAO  TEXT,
                $COL_ALERTA_CRIADO   TEXT NOT NULL,
                $COL_ALERTA_ID_URL   INTEGER,
                FOREIGN KEY ($COL_ALERTA_ID_URL) REFERENCES $TABLE_URL($COL_URL_ID)
            );
        """.trimIndent()
    }
}

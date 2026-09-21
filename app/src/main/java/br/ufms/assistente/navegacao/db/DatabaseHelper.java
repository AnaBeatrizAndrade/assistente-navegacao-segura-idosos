package br.ufms.assistente.navegacao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.ufms.assistente.navegacao.model.Alerta;
import br.ufms.assistente.navegacao.model.UrlAnalisada;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "assistente_navegacao.db";
    private static final Integer DB_VERSION = 1;
    
    public static final String TABLE_URL = "url_analisada";
    public static final String COL_URL_ID = "id_url";
    public static final String COL_URL_HASH = "url_hash";
    public static final String COL_URL_STATUS = "status_seguranca";
    public static final String COL_URL_DATA_VERIF = "data_verificacao";

    public static final String TABLE_ALERTA = "alerta";
    public static final String COL_ALERTA_ID = "id_alerta";
    public static final String COL_ALERTA_MENSAGEM = "mensagem_alerta";
    public static final String COL_ALERTA_DOMINIO = "url_dominio";
    public static final String COL_ALERTA_URGENCIA = "nivel_urgencia";
    public static final String COL_ALERTA_DECISAO = "decisao_usuario";
    public static final String COL_ALERTA_CRIADO = "criado_em";
    public static final String COL_ALERTA_ID_URL = "id_url";

    private static final String FORMAT_DATA = "yyyy-MM-dd HH:mm:ss";

    private static final String SQL_CREATE_URL =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_URL + " (" +
            COL_URL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_URL_HASH + " TEXT NOT NULL UNIQUE, " +
            COL_URL_STATUS + " TEXT NOT NULL, " +
            COL_URL_DATA_VERIF + " TEXT NOT NULL" +
            ");";

    private static final String SQL_CREATE_ALERTA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ALERTA + " (" +
            COL_ALERTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ALERTA_MENSAGEM + " TEXT, " +
            COL_ALERTA_DOMINIO + " TEXT, " +
            COL_ALERTA_URGENCIA + " TEXT NOT NULL, " +
            COL_ALERTA_DECISAO + " TEXT, " +
            COL_ALERTA_CRIADO + " TEXT NOT NULL, " +
            COL_ALERTA_ID_URL + " INTEGER, " +
            "FOREIGN KEY (" + COL_ALERTA_ID_URL + ") REFERENCES " + TABLE_URL + " (" + COL_URL_ID + ")" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_URL);
        sqLiteDatabase.execSQL(SQL_CREATE_ALERTA);
        sqLiteDatabase.execSQL("CREATE INDEX idx_url_hash ON " + TABLE_URL + "(" + COL_URL_HASH +");");
        Log.i(TAG, "Banco de dados criado com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_URL);
        onCreate(sqLiteDatabase);
    }

    @Override
    public  void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    public UrlAnalisada buscarUrlPorHash(String hash) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    TABLE_URL,
                    null,
                    COL_URL_HASH + " = ?",
                    new String[]{hash},
                    null, null, null, "1"
            );

            if (cursor != null && cursor.moveToFirst()) {
                UrlAnalisada url = new UrlAnalisada();
                url.setIdUrl(cursor.getInt(cursor.getColumnIndexOrThrow(COL_URL_ID)));
                url.setUrlHash(cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_HASH)));
                url.setStatusSeguranca(cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_STATUS)));
                url.setDataVerificacao(parseData(cursor.getString(cursor.getColumnIndexOrThrow(COL_URL_DATA_VERIF))));
                return url;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar URL por hash", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public int inserirOuAtualizarUrl(UrlAnalisada urlAnalisada) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_URL_HASH, urlAnalisada.getUrlHash());
        values.put(COL_URL_STATUS, urlAnalisada.getStatusSeguranca());
        values.put(COL_URL_DATA_VERIF, formatarData(new Date()));

        try {
            long id = db.insertWithOnConflict(
                    TABLE_URL, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return (int) id;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir/atualizar URL", e);
            return -1;
        }
    }

    public int inserirAlerta(Alerta alerta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ALERTA_MENSAGEM, alerta.getMensagemAlerta());
        values.put(COL_ALERTA_DOMINIO, alerta.getUrlInterceptadaDominio());
        values.put(COL_ALERTA_URGENCIA, alerta.getNivelUrgencia());
        values.put(COL_ALERTA_DECISAO, alerta.getDecisaoUsuario());
        values.put(COL_ALERTA_CRIADO, formatarData(new Date()));
        values.put(COL_ALERTA_ID_URL, alerta.getIdUrl());

        try {
            long id = db.insert(TABLE_ALERTA, null, values);
            alerta.setIdAlerta((int) id);
            return (int) id;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir alerta", e);
            return -1;
        }
    }

    public void atualizarDecisaoAlerta(int idAlerta, String decisao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ALERTA_DECISAO, decisao);
        db.update(TABLE_ALERTA, values,  COL_ALERTA_ID + " = ?", new String[]{String.valueOf(idAlerta)});
    }

    public void limparCacheAntigo() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(
                "DELETE FROM " + TABLE_URL + " WHERE JULIANDAY('now') - julianday(" + COL_URL_DATA_VERIF + ") > 30");
            Log.i(TAG, "Cache antigo removido");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao limpar cache", e);
        }
    }
    
    private String formatarData(Date date) {
        return new SimpleDateFormat(FORMAT_DATA, Locale.ROOT).format(date);
    }
    
    private Date parseData(String dataStr) {
        try {
            return new SimpleDateFormat(FORMAT_DATA, Locale.ROOT).parse(dataStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}

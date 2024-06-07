package com.example.bancodip.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ModelBancoDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CyberBank";
    private static final int DATABASE_VERSION = 1;
    public static final String NOME_TABELA = "CyberBank";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_CPF = "CPF";
    public static final String COLUNA_SENHA = "senha";
    public static final String COLUNA_SALDO= "Saldo";
    public static final String COLUNA_CHEQUEESPECIAL = "chequeEspecial";
    public static final String COLUNA_CHEQUEI = "chequeI";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + NOME_TABELA + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_NOME + " VARCHAR(80), " +
                    COLUNA_CPF + " VARCHAR(80), " +
                    COLUNA_SENHA + " VARCHAR(80), " +
                    COLUNA_CHEQUEESPECIAL + " DECIMAL(10,2), " +
                    COLUNA_CHEQUEI + " DECIMAL(10,2), " +
                    COLUNA_SALDO + " DECIMAL(10,2)" +
                    ");";



    public ModelBancoDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOME_TABELA);
        onCreate(sqLiteDatabase);
    }

}

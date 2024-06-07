package com.example.bancodip.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bancodip.model.ModelBancoDados;

public class ControllerBancoDados {

    private final Context context;
    private SQLiteDatabase database;
    private final ModelBancoDados dbHelper;

    public ControllerBancoDados(Context context) {
        this.context = context;
        dbHelper = new ModelBancoDados(context);
    }

    public ControllerBancoDados open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertData(String nome, String CPF, String senha, double Saldo, double chequeEspecial, double chequeI) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_NOME, nome);
        contentValues.put(ModelBancoDados.COLUNA_CPF, CPF);
        contentValues.put(ModelBancoDados.COLUNA_SENHA, senha);
        contentValues.put(ModelBancoDados.COLUNA_SALDO, Saldo);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUEI, chequeI);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUEESPECIAL, chequeEspecial);

        long result = -1;

        try {
            result = database.insertOrThrow(ModelBancoDados.NOME_TABELA, null, contentValues);
            Log.d("INSERT_DATA", "Inserção bem-sucedida. ID do novo registro: " + result);
        } catch (SQLException e) {
            Log.e("INSERT_DATA", "Erro ao inserir dados: " + e.getMessage());
        }

        return result;
    }

    public void updateSaldo(String CPF, double newSaldo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_SALDO, newSaldo);
        String whereClause = ModelBancoDados.COLUNA_CPF + " = ?";
        String[] whereArgs = {CPF};
        database.update(ModelBancoDados.NOME_TABELA, contentValues, whereClause, whereArgs);
    }

    public void updateCheque(String CPF, double newCheque) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_CHEQUEESPECIAL, newCheque);
        String whereClause = ModelBancoDados.COLUNA_CPF + " = ?";
        String[] whereArgs = {CPF};
        database.update(ModelBancoDados.NOME_TABELA, contentValues, whereClause, whereArgs);
    }

    public Cursor getAllData() {
        return database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_ID, ModelBancoDados.COLUNA_NOME, ModelBancoDados.COLUNA_SALDO, ModelBancoDados.COLUNA_CPF},
                null, null, null, null, null);
    }

    public Double getSaldoByTitular(String CPF) {
        Double saldo = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_SALDO},
                ModelBancoDados.COLUNA_CPF + " = ?",
                new String[]{CPF},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int saldoIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_SALDO);
                saldo = cursor.getDouble(saldoIndex);
            }
        } catch (Exception e) {
            Log.e("GET_SALDO", "Erro ao obter saldo: " + e.getMessage());
        }
        return saldo;
    }

    public Double getChequeByTitular(String CPF) {
        Double cheque = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_CHEQUEESPECIAL},
                ModelBancoDados.COLUNA_CPF + " = ?",
                new String[]{CPF},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int chequeIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_CHEQUEESPECIAL);
                if (!cursor.isNull(chequeIndex)) {
                    cheque = cursor.getDouble(chequeIndex);
                }
            }
        } catch (Exception e) {
            Log.e("GET_CHEQUE", "Erro ao obter cheque especial: " + e.getMessage());
        }
        return cheque;
    }

    public Double getChequeDEFIByTitular(String CPF) {
        Double cheque = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_CHEQUEI},
                ModelBancoDados.COLUNA_CPF + " = ?",
                new String[]{CPF},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int chequeIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_CHEQUEI);
                if (!cursor.isNull(chequeIndex)) {
                    cheque = cursor.getDouble(chequeIndex);
                }
            }
        } catch (Exception e) {
            Log.e("GET_CHEQUE", "Erro ao obter cheque especial: " + e.getMessage());
        }
        return cheque;
    }

    public boolean isSenhaInDatabase(String emailToCheck) {
        try (Cursor cursor = database.query(
                ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_SENHA},
                null, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String Senha = cursor.getString(cursor.getColumnIndex(ModelBancoDados.COLUNA_SENHA));
                    if (emailToCheck.equals(Senha)) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("EMAIL_IN_DB", "Erro ao verificar email na base de dados: " + e.getMessage());
        }
        return false;
    }

    public boolean isCPFInDatabase(String CPFToCheck) {
        try (Cursor cursor = database.query(
                ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_CPF},
                null, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String CPF = cursor.getString(cursor.getColumnIndex(ModelBancoDados.COLUNA_CPF));
                    if (CPFToCheck.equals(CPF)) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CPF_IN_DB", "Erro ao verificar CPF na base de dados: " + e.getMessage());
        }
        return false;
    }

}

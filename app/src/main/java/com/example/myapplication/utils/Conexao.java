package com.example.myapplication.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Conexao extends SQLiteOpenHelper {
    private static final String DB_NAME = "banco.db";
    private static final int version = 2;

    public Conexao(Context context) { super(context, DB_NAME, null, version); }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Aluno(id integer primary key autoincrement, nome varchar(50), cpf varchar(50), telefone varchar(50), fotoBytes blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (version < 2) {
            db.execSQL("ALTER TABLE aluno ADD COLUMN fotoBytes BLOB");
        }
    }
}

package com.example.myapplication.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.models.dao.AlunoDAO;
import com.example.myapplication.models.entity.Aluno;

@Database(entities = {Aluno.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlunoDAO alunoDAO();
    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "banco"
            )
                    .allowMainThreadQueries() //remover em prod
                    .build();
        }
        return INSTANCE;
    }
}

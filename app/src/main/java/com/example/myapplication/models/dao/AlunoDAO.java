package com.example.myapplication.models.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.entity.Aluno;
import com.example.myapplication.models.entity.AlunoDeprecated;

import java.util.List;

@Dao
public interface AlunoDAO {
    @Insert
    long insertAluno(Aluno aluno);

    @Query("SELECT * FROM aluno")
    List<Aluno> obterTodos();

    @Update
    void updateAluno(Aluno aluno);

    @Delete
    void deleteAluno(Aluno aluno);
}

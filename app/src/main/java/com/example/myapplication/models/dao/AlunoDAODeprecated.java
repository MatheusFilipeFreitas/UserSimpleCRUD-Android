package com.example.myapplication.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.entity.AlunoDeprecated;
import com.example.myapplication.utils.Conexao;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoDAODeprecated {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public AlunoDAODeprecated(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public int inserir(AlunoDeprecated aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("fotoBytes", aluno.getFotosBytes());
        return (int) banco.insert("aluno", null, values);
    }

    public void excluir(AlunoDeprecated aluno) {
        banco.delete("aluno", "id = ?", new String[]{String.valueOf(aluno.getId())});
    }

    public void atualizar(AlunoDeprecated aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("fotoBytes", aluno.getFotosBytes());
        banco.update("aluno", values, "id = ?", new String[] {String.valueOf(aluno.getId())});
    }
    public List<AlunoDeprecated> obterTodos() {
        List<AlunoDeprecated> alunos = new ArrayList<AlunoDeprecated>();

        Cursor cursor = banco.query("aluno",  new String[]{"id", "nome", "cpf", "telefone", "fotoBytes"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            AlunoDeprecated a = new AlunoDeprecated();
            a.setId(cursor.getInt(0));
            a.setNome(cursor.getString(1));
            a.setCpf(cursor.getString(2));
            a.setTelefone(cursor.getString(3));
            a.setFotosBytes(cursor.getBlob(4));
            alunos.add(a);
        }
        return alunos;
    }
}

package com.example.myapplication.models.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "aluno")
public class Aluno implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "nome")
    private String nome;
    @ColumnInfo(name = "cpf")
    private String cpf;
    @ColumnInfo(name = "telefone")
    private String telefone;
    @ColumnInfo(name = "fotosBytes")
    private byte[] fotosBytes;

    @Override
    public String toString() {
        return "Nome: " + this.nome + "\nCPF: " + this.cpf + "\nTelefone: " + this.telefone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getFotosBytes() {
        return fotosBytes;
    }

    public void setFotosBytes(byte[] fotosBytes) {
        this.fotosBytes = fotosBytes;
    }
}

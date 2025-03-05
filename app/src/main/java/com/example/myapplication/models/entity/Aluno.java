package com.example.myapplication.models.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aluno implements Serializable {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;

    @Override
    public String toString() {
        return "Nome: " + this.nome + "\nCPF: " + this.cpf + "\nTelefone: " + this.telefone;
    }
}

package com.example.myapplication.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Endereco {
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer numeroRua;
    private String complemento;
}

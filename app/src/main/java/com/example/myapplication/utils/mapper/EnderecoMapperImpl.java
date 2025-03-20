package com.example.myapplication.utils.mapper;

import com.example.myapplication.models.entity.Endereco;

import org.json.JSONObject;

public class EnderecoMapperImpl implements EnderecoMapper {
    @Override
    public Endereco convertJsonToEndereco(JSONObject object) {
        String logradouro = object.optString("logradouro", "");
        String bairro = object.optString("bairro", "");
        String cidade = object.optString("localidade", "");
        String estado = object.optString("uf", "");
        return new Endereco(logradouro, bairro, cidade, estado, 0, "");
    }
}

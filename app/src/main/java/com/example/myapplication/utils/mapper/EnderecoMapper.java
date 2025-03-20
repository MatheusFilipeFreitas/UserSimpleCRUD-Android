package com.example.myapplication.utils.mapper;

import com.example.myapplication.models.entity.Endereco;

import org.json.JSONObject;

public interface EnderecoMapper {
    Endereco convertJsonToEndereco(JSONObject object);
}

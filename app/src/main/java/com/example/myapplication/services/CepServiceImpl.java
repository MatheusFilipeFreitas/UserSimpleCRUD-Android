package com.example.myapplication.services;

import android.os.AsyncTask;
import com.example.myapplication.models.entity.Endereco;
import com.example.myapplication.utils.mapper.EnderecoMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CepServiceImpl implements CepService {
    private static final String BASE_URL = "https://viacep.com.br/ws/";

    private final EnderecoMapper enderecoMapper;
    private CepCallback cepCallback;

    public CepServiceImpl(EnderecoMapper enderecoMapper, CepCallback callback) {
        this.enderecoMapper = enderecoMapper;
        this.cepCallback = callback;
    }

    @Override
    public void getEnderecoAsync(String cep) {
        new CepTask().execute(cep);
    }

    private class CepTask extends AsyncTask<String, Void, Endereco> {

        @Override
        protected Endereco doInBackground(String... params) {
            String cep = params[0];
            JSONObject json = buscarCEP(cep);

            if (json == null) {
                cepCallback.onError("Erro ao buscar o endereço. CEP inválido ou erro na API.");
                return null;
            }

            try {
                return enderecoMapper.convertJsonToEndereco(json);
            } catch (Exception e) {
                cepCallback.onError("Erro ao converter JSON para Endereco: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Endereco endereco) {
            super.onPostExecute(endereco);
            if (endereco != null) {
                cepCallback.onSuccess(endereco);
            }
        }
    }

    private static JSONObject buscarCEP(String cep) {
        String urlString = BASE_URL + cep + "/json";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONObject json = new JSONObject(response.toString());

            if (json.has("erro") && json.getBoolean("erro")) {
                System.out.println("CEP não encontrado: " + cep);
                return null;
            }

            return json;
        } catch (IOException | JSONException e) {
            System.out.println("Erro ao buscar o CEP: " + e.getMessage());
        }
        return null;
    }

    public interface CepCallback {
        void onSuccess(Endereco endereco);
        void onError(String errorMessage);
    }
}
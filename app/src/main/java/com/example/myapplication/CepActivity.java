package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.models.entity.Endereco;
import com.example.myapplication.services.CepService;
import com.example.myapplication.services.CepServiceImpl;
import com.example.myapplication.utils.mapper.EnderecoMapper;
import com.example.myapplication.utils.mapper.EnderecoMapperImpl;

public class CepActivity extends AppCompatActivity {
    private Endereco endereco = null;
    private CepService cepService;
    private EditText cepEditText;
    private EditText logradouroEditText;
    private EditText nRuaEditText;
    private EditText complementoEditText;
    private EditText bairroEditText;
    private EditText cidadeEditText;
    private EditText estadoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CepServiceImpl.CepCallback callback = new CepServiceImpl.CepCallback() {
            @Override
            public void onSuccess(Endereco endereco) {
                logradouroEditText.setText(endereco.getLogradouro());
                bairroEditText.setText(endereco.getBairro());
                cidadeEditText.setText(endereco.getCidade());
                estadoEditText.setText(endereco.getEstado());
            }

            @Override
            public void onError(String errorMessage) {
                createErrorToast();
            }
        };
        this.cepService = new CepServiceImpl(new EnderecoMapperImpl(), callback);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cep);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cepEditText = findViewById(R.id.cepEditText);
        logradouroEditText = findViewById(R.id.logradouroEditText);
        nRuaEditText = findViewById(R.id.nRuaEditText);
        complementoEditText = findViewById(R.id.complementoEditText);
        bairroEditText = findViewById(R.id.bairroEditText);
        cidadeEditText = findViewById(R.id.cidadeEditText);
        estadoEditText = findViewById(R.id.estadoEditText);
    }

    public void buscarCEP(View view) {
        String cep = cepEditText.getText().toString();
        if (!cep.isEmpty()) {
            this.cepService.getEnderecoAsync(cep);
        }
    }

    public void createErrorToast() {
        Toast.makeText(this, "Erro ao consultar a api de CEP", Toast.LENGTH_SHORT).show();
    }

    public void salvarCEP(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String message = transformEnderecoIntoResumo();
        intent.putExtra("resumoEndereco", message);
        startActivity(intent);
    }

    private String transformEnderecoIntoResumo() {
        return logradouroEditText.getText().toString()
                + ", " + nRuaEditText.getText().toString()
                + " - " + bairroEditText.getText().toString()
                + ", " + complementoEditText.getText().toString()
                + ", " + cidadeEditText.getText().toString() + " - "
                + estadoEditText.getText().toString();
    }
}
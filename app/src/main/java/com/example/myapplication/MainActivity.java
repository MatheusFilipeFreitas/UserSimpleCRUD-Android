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

import com.example.myapplication.models.dao.AlunoDAO;
import com.example.myapplication.models.entity.Aluno;
import com.example.myapplication.utils.validator.DocumentValidator;
import com.example.myapplication.utils.validator.DocumentValidatorImpl;

public class MainActivity extends AppCompatActivity {
    private Aluno aluno = null;
    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private AlunoDAO dao;
    private DocumentValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent.hasExtra("aluno")) {
            aluno = (Aluno) intent.getSerializableExtra("aluno");
        }

        nome = findViewById(R.id.nameEditText);
        cpf = findViewById(R.id.cpfEditText);
        telefone = findViewById(R.id.telefoneEditText);

        if (!this.isCreate()) {
            nome.setText(aluno.getNome());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
        }

        dao = new AlunoDAO(this);
    }

    public void onSave(View view) {
        if (isCreate()) {
            createAction();
        } else {
            updateAction();
            finish();
        }

    }

    public boolean validateInputs() {
        boolean result = true;
        validator = new DocumentValidatorImpl();
        if(!validator.validateCPF(cpf.getText().toString())) {
            cpf.setError("CPF inválido!");
            result = false;
        } else {
            cpf.setError(null);
        }

        if (!validator.validateTelefone(telefone.getText().toString())) {
            telefone.setError("Telefone inválido!");
            result = false;
        } else {
            telefone.setError(null);
        }
        return result;
    }

    public void resetFields() {
        nome.setText("");
        cpf.setText("");
        telefone.setText("");
    }

    public void navegarParaListagem(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void createAction() {
        if (!validateInputs()) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
            return;
        }
        aluno = new Aluno();
        aluno.setNome(nome.getText().toString());
        aluno.setCpf(cpf.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        int id = dao.inserir(aluno);
        Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
        resetFields();
    }

    private void updateAction() {
        if (!validateInputs()) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
            return;
        }
        aluno.setNome(nome.getText().toString());
        aluno.setCpf(cpf.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        dao.atualizar(aluno);
        Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        resetFields();
        aluno = null;
    }

    private boolean isCreate() {
        return aluno == null;
    }

}
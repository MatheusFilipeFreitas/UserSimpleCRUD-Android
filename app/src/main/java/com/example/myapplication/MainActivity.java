package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.models.dao.AlunoDAO;
import com.example.myapplication.models.dao.AlunoDAODeprecated;
import com.example.myapplication.models.entity.Aluno;
import com.example.myapplication.models.entity.AlunoDeprecated;
import com.example.myapplication.utils.AppDatabase;
import com.example.myapplication.utils.validator.DocumentValidator;
import com.example.myapplication.utils.validator.DocumentValidatorImpl;

import java.io.ByteArrayOutputStream;

import lombok.NonNull;

public class MainActivity extends AppCompatActivity {
    private Aluno aluno = null;
    private ImageView profileImageView;
    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private AlunoDAO dao;
    private DocumentValidator validator;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

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
        profileImageView = findViewById(R.id.profileImageView);

        dao = AppDatabase.getInstance(this).alunoDAO();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null && extras.containsKey("data")) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            if (imageBitmap != null) {
                                profileImageView.setImageBitmap(corrigirOrientacao(imageBitmap));
                            }
                        } else {
                            Toast.makeText(this, "Erro ao capturar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Button btnSelfie = findViewById(R.id.btnSelfie);
        btnSelfie.setOnClickListener(v -> tirarFoto());

        if (!this.isCreate()) {
            nome.setText(aluno.getNome());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
            byte[] fotoBytes = aluno.getFotosBytes();
            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                profileImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "A permissão da câmera é necessária para tirar fotos.", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void tirarFoto() {
        checkCameraPermissionAndStart();
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

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "Câmera não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
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

        BitmapDrawable drawable = (BitmapDrawable) profileImageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] fotoBytes = stream.toByteArray();
            aluno.setFotosBytes(fotoBytes);
        }

        long id = dao.insertAluno(aluno);
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

        BitmapDrawable drawable = (BitmapDrawable) profileImageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] fotoBytes = stream.toByteArray();
            aluno.setFotosBytes(fotoBytes);
        }

        dao.updateAluno(aluno);
        Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        resetFields();
        aluno = null;
    }

    private boolean isCreate() {
        return aluno == null;
    }

    private Bitmap corrigirOrientacao(Bitmap bitmap) {
        if (bitmap == null) return null;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
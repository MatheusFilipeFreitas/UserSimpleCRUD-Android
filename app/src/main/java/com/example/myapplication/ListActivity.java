package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.models.dao.AlunoDAO;
import com.example.myapplication.models.dao.AlunoDAODeprecated;
import com.example.myapplication.models.entity.Aluno;
import com.example.myapplication.models.entity.AlunoDeprecated;
import com.example.myapplication.utils.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private AlunoDAO dao;
    private List<Aluno> alunos;
    private EditText buscar;
    private List<Aluno> alunosFiltrados = new ArrayList<Aluno>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buscar = findViewById(R.id.buscarEditText);
        listView = findViewById(R.id.alunoListView);
        registerForContextMenu(listView);

        dao = AppDatabase.getInstance(this).alunoDAO();

        getAllAlunos();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
    }

    public void navegarParaCriacao(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void excluir(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        final Aluno aluno = alunosFiltrados.get(menuInfo.position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o aluno?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alunosFiltrados.remove(aluno);
                        alunos.remove(aluno);
                        dao.deleteAluno(aluno);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void atualizar(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        final Aluno aluno = alunosFiltrados.get(menuInfo.position);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("aluno", aluno);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAlunos();
    }

    public void filtrar(View view) {
        List<Aluno> alunosFiltradosPorCampo = new ArrayList<>();
        String valorCampoBuscar = buscar.getText().toString();
        if (!valorCampoBuscar.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                alunosFiltradosPorCampo = this.alunos.stream().filter(aluno -> aluno.getNome().contains(valorCampoBuscar)).toList();
            }
        } else {
            alunosFiltradosPorCampo = this.alunos;
        }
        this.handleWithAlunosList(alunosFiltradosPorCampo);
        this.setListInListView(alunosFiltradosPorCampo);
    }

    public void cancelFiltrar(View view) {
        this.handleWithAlunosList(alunos);
        this.setListInListView(alunos);
    }

    private void getAllAlunos() {
        alunos = dao.obterTodos();
        this.handleWithAlunosList(alunos);
        this.setListInListView(alunos);
    }

    private void handleWithAlunosList(List<Aluno> listAlunos) {
        alunosFiltrados.clear();
        alunosFiltrados.addAll(listAlunos);
    }

    private void setListInListView(List<Aluno> listAlunos) {
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, listAlunos);
        listView.setAdapter(adaptador);
    }
}
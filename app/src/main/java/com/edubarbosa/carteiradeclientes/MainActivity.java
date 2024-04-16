package com.edubarbosa.carteiradeclientes;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.edubarbosa.carteiradeclientes.database.DadosOpenHelper;
import com.edubarbosa.carteiradeclientes.databinding.ActivityMainBinding;
import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;
import com.edubarbosa.carteiradeclientes.dominio.repositorio.ClienteRepositorio;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteAdapter adapter;
    private ClienteRepositorio clienteRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.include.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CadClienteActivity.class);
            startActivityForResult(intent, 0);
        });

        criarConexao();

        clienteRepositorio = new ClienteRepositorio(conexao);
        List<Cliente> listaClientes = clienteRepositorio.buscarTodos();

        adapter = new ClienteAdapter(listaClientes);
        binding.include.rvListDados.setLayoutManager(new LinearLayoutManager(this));
        binding.include.rvListDados.setHasFixedSize(true);
        binding.include.rvListDados.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            List<Cliente> listaClientes = clienteRepositorio.buscarTodos();
            adapter = new ClienteAdapter(listaClientes);
            binding.include.rvListDados.setAdapter(adapter);
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(binding.include.layoutContentMain, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_ok, null).show();

        } catch (SQLException exception){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(exception.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }
}
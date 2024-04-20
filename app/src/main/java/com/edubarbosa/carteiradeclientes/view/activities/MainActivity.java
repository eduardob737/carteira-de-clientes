package com.edubarbosa.carteiradeclientes.view.activities;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.edubarbosa.carteiradeclientes.R;
import com.edubarbosa.carteiradeclientes.database.DadosOpenHelper;
import com.edubarbosa.carteiradeclientes.databinding.ActivityMainBinding;
import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;
import com.edubarbosa.carteiradeclientes.dominio.repositorio.ClienteRepositorio;
import com.edubarbosa.carteiradeclientes.view.ClienteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteAdapter adapter;
    private ClienteRepositorio clienteRepositorio;
    public ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), ar -> {
                    List<Cliente> listaClientes = clienteRepositorio.buscarTodos();
                    setupRecyclerView(listaClientes, R.string.lbl_msg_carteira_vazia);
                });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CadClienteActivity.class);
            activityLauncher.launch(intent);
        });

        criarConexao();

        clienteRepositorio = new ClienteRepositorio(conexao);
        List<Cliente> listaClientes = clienteRepositorio.buscarTodos();

        setupRecyclerView(listaClientes, R.string.lbl_msg_carteira_vazia);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
        searchView.setQueryHint(getString(R.string.hint_buscar_nome));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cliente> clientesEncontrados = clienteRepositorio.pesquisarClientesPorNome(newText);
                setupRecyclerView(clientesEncontrados, R.string.lbl_msg_busca_vazia);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setupRecyclerView(List<Cliente> listaClientes, @StringRes int mensagemListaVazia) {
        adapter = new ClienteAdapter(listaClientes);
        binding.rvListDados.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListDados.setHasFixedSize(true);
        binding.rvListDados.setAdapter(adapter);

        if (listaClientes.isEmpty()){
            binding.tvListaVazia.setText(mensagemListaVazia);
            binding.tvListaVazia.setVisibility(View.VISIBLE);
        } else {
            binding.tvListaVazia.setVisibility(View.GONE);
        }
    }

    private void criarConexao() {
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(binding.constraintMain, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_SHORT).show();
        } catch (SQLException exception) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(exception.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }
}
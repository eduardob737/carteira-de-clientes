package com.edubarbosa.carteiradeclientes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.edubarbosa.carteiradeclientes.database.DadosOpenHelper;
import com.edubarbosa.carteiradeclientes.databinding.ActivityMainBinding;
import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;
import com.edubarbosa.carteiradeclientes.dominio.repositorio.ClienteRepositorio;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteAdapter adapter;
    private ClienteRepositorio clienteRepositorio;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CadClienteActivity.class);
            startActivityForResult(intent, 0);
        });

        criarConexao();

        clienteRepositorio = new ClienteRepositorio(conexao);
        List<Cliente> listaClientes = clienteRepositorio.buscarTodos();

        setupRecyclerView(listaClientes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setQueryHint("Procure por um nome");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setupRecyclerView(List<Cliente> listaClientes) {
        adapter = new ClienteAdapter(listaClientes);
        binding.rvListDados.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListDados.setHasFixedSize(true);
        binding.rvListDados.setAdapter(adapter);

        ArrayList<String> listaNomesClientes = new ArrayList<>();

        for (int i=0; i < listaClientes.size(); i++) {
            listaNomesClientes.add(listaClientes.get(i).nome);
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.linha_clientes, listaNomesClientes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            List<Cliente> listaClientes = clienteRepositorio.buscarTodos();
            adapter = new ClienteAdapter(listaClientes);
            binding.rvListDados.setAdapter(adapter);
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(binding.constraintMain, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_LONG)
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
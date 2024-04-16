package com.edubarbosa.carteiradeclientes;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.edubarbosa.carteiradeclientes.database.DadosOpenHelper;
import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;
import com.edubarbosa.carteiradeclientes.dominio.repositorio.ClienteRepositorio;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edubarbosa.carteiradeclientes.databinding.ActivityCadClienteBinding;

public class CadClienteActivity extends AppCompatActivity {

    private ActivityCadClienteBinding binding;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteRepositorio clienteRepositorio;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        criarConexao();
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(binding.include.layoutContentCliente, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_ok, null).show();

            clienteRepositorio = new ClienteRepositorio(conexao);

        } catch (SQLException exception){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(exception.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    private void confirmar() {
        cliente = new Cliente();

        if (!validaCampos()) {
            try {
                clienteRepositorio.inserir(cliente);
                finish();

            } catch (SQLException exception) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_erro);
                dlg.setMessage(exception.getMessage());
                dlg.setNeutralButton(R.string.action_ok, null);
                dlg.show();
            }
        }
    }

    private boolean validaCampos() {
        String nome = binding.include.edtNome.getText().toString();
        String endereco = binding.include.edtEndereco.getText().toString();
        String email = binding.include.edtEmail.getText().toString();
        String telefone = binding.include.edtTelefone.getText().toString();

        cliente.nome        = nome;
        cliente.endereco    = endereco;
        cliente.email       = email;
        cliente.telefone    = telefone;

        boolean camposIncorretos = true;

        if (isCampoVazio(nome)) {
            binding.include.edtNome.requestFocus();
        } else if (isCampoVazio(endereco)) {
            binding.include.edtEndereco.requestFocus();
        } else if (!isEmailValido(email)) {
            binding.include.edtEmail.requestFocus();
        } else if (isCampoVazio(telefone)) {
            binding.include.edtTelefone.requestFocus();
        } else {
            camposIncorretos = false;
        }

        if (camposIncorretos){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_aviso);
            dlg.setMessage(R.string.message_campos_invalidos_brancos);
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();
        }
        return camposIncorretos;
    }

    private boolean isCampoVazio(String valor) {
        return (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
    }

    private boolean isEmailValido(String email) {
        return (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_cad_cliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ok) {
            confirmar();
        } else if (id == R.id.action_cancelar) {
            //Toast.makeText(this, "Botão Cancelar selecionado", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
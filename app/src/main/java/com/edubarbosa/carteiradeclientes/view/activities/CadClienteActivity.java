package com.edubarbosa.carteiradeclientes.view.activities;

import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.edubarbosa.carteiradeclientes.R;
import com.edubarbosa.carteiradeclientes.database.DadosOpenHelper;
import com.edubarbosa.carteiradeclientes.databinding.ActivityCadClienteBinding;
import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;
import com.edubarbosa.carteiradeclientes.dominio.repositorio.ClienteRepositorio;

import java.util.Objects;

public class CadClienteActivity extends AppCompatActivity {

    private ActivityCadClienteBinding binding;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteRepositorio clienteRepositorio;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        criarConexao();
        verificaParametro();
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            SQLiteDatabase conexao = dadosOpenHelper.getWritableDatabase();
            clienteRepositorio = new ClienteRepositorio(conexao);

        } catch (SQLException exception){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(exception.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    private void verificaParametro(){
        Bundle bundle = getIntent().getExtras();
        cliente = new Cliente();

        if ((bundle != null) && (bundle.containsKey("CLIENTE"))){
            cliente = (Cliente) bundle.getSerializable("CLIENTE");
            assert cliente != null;
            binding.edtNome.setText(cliente.nome);
            binding.edtEndereco.setText(cliente.endereco);
            binding.edtEmail.setText(cliente.email);
            binding.edtTelefone.setText(cliente.telefone);
        }
    }

    private void confirmar() {
        if (!validaCampos()) {
            try {
                if (cliente.codigo == 0) {
                    clienteRepositorio.inserir(cliente);
                } else {
                    clienteRepositorio.alterar(cliente);
                }
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
        String nome = binding.edtNome.getText().toString();
        String endereco = binding.edtEndereco.getText().toString();
        String email = binding.edtEmail.getText().toString();
        String telefone = binding.edtTelefone.getText().toString();

        cliente.nome        = nome;
        cliente.endereco    = endereco;
        cliente.email       = email;
        cliente.telefone    = telefone;

        boolean camposIncorretos = true;

        if (isCampoVazio(nome)) {
            binding.edtNome.requestFocus();
        } else if (isCampoVazio(endereco)) {
            binding.edtEndereco.requestFocus();
        } else if (!isEmailValido(email)) {
            binding.edtEmail.requestFocus();
        } else if (isCampoVazio(telefone)) {
            binding.edtTelefone.requestFocus();
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
        } else if (id == R.id.action_excluir) {
            AlertDialog.Builder dlg = setupAlertDialog();
            dlg.show();
        } else if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private AlertDialog.Builder setupAlertDialog() {
        DialogInterface.OnClickListener positiveListener = (dialogInterface, i) -> {
            clienteRepositorio.excluir(cliente.codigo);
            Toast.makeText(this, getString(R.string.dlg_cliente) + cliente.nome + getString(R.string.dlg_excluido_sucesso), Toast.LENGTH_SHORT).show();
            finish();
        };

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(R.string.dlg_atencao);
        dlg.setMessage(getString(R.string.dlg_msg_confirmacao_exclusao) + cliente.nome + getString(R.string.dlg_ponto_interrogacao));
        dlg.setPositiveButton(R.string.dlg_action_sim, positiveListener);
        dlg.setNegativeButton(R.string.dlg_action_cancelar, null);
        return dlg;
    }
}
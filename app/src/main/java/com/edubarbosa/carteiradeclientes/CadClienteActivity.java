package com.edubarbosa.carteiradeclientes;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    private void validaCampos() {
        String nome = binding.include.edtNome.getText().toString();
        String endereco = binding.include.edtEndereco.getText().toString();
        String email = binding.include.edtEmail.getText().toString();
        String telefone = binding.include.edtTelefone.getText().toString();

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
            validaCampos();
            //Toast.makeText(this, "Botão OK selecionado", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_cancelar) {
            //Toast.makeText(this, "Botão Cancelar selecionado", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
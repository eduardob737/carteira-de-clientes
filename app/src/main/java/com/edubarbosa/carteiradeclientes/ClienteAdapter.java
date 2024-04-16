package com.edubarbosa.carteiradeclientes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edubarbosa.carteiradeclientes.dominio.entidades.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolderCliente> {

    //private AdapterClienteBinding binding;
    private List<Cliente> dados;

    public ClienteAdapter(List<Cliente> listaClientes) {
        dados = listaClientes;
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolderCliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolderCliente holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderCliente extends RecyclerView.ViewHolder {
        public TextView tvNome;
        public TextView tvTelefone;

        public ViewHolderCliente(@NonNull View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.tv_nome);
            tvTelefone = itemView.findViewById(R.id.tv_telefone);

        }
    }
}
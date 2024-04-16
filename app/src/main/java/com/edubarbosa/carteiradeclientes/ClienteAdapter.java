package com.edubarbosa.carteiradeclientes;

import android.view.LayoutInflater;
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_clientes, parent, false);
        ViewHolderCliente holderCliente = new ViewHolderCliente(view);
        return holderCliente;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolderCliente holder, int position) {
        if ((dados != null) && (!dados.isEmpty())) {

            Cliente cliente = dados.get(position);

            holder.tvNome.setText(cliente.nome);
            holder.tvTelefone.setText(cliente.telefone);
        }
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
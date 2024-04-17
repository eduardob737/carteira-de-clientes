package com.edubarbosa.carteiradeclientes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.edubarbosa.carteiradeclientes.databinding.ActivityCadClienteBinding;
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
        return new ViewHolderCliente(view, parent.getContext());
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

        public ViewHolderCliente(@NonNull View itemView, Context context) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.tv_nome);
            tvTelefone = itemView.findViewById(R.id.tv_telefone);

            itemView.setOnClickListener(view -> {
                if (!dados.isEmpty()) {
                    Cliente cliente = dados.get(getLayoutPosition());

                    Intent intent = new Intent(context, CadClienteActivity.class);
                    intent.putExtra("CLIENTE", cliente);

                    ((AppCompatActivity) context).startActivityForResult(intent, 0);

                }
            });

        }
    }
}
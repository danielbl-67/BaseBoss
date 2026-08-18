package com.example.baseboss.ui.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseboss.databinding.ItemClienteBinding;
import com.example.baseboss.datos.entidades.Cliente;

/**
 * Adaptador para RecyclerView utilizando ListAdapter y DiffUtil para actualizaciones óptimas.
 */
public class ClienteAdaptador extends ListAdapter<Cliente, ClienteAdaptador.ClienteViewHolder> {

    private final OnClienteClickListener listener;

    public interface OnClienteClickListener {
        void onClienteClick(Cliente cliente);
    }

    public ClienteAdaptador(OnClienteClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Cliente> DIFF_CALLBACK = new DiffUtil.ItemCallback<Cliente>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cliente oldItem, @NonNull Cliente newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cliente oldItem, @NonNull Cliente newItem) {
            return oldItem.getNombreRazonSocial().equals(newItem.getNombreRazonSocial())
                    && oldItem.getNifCif().equals(newItem.getNifCif())
                    && oldItem.getEmail().equals(newItem.getEmail());
        }
    };

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClienteBinding binding = ItemClienteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ClienteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private final ItemClienteBinding binding;

        public ClienteViewHolder(ItemClienteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Cliente cliente, OnClienteClickListener listener) {
            binding.textoNombreCliente.setText(cliente.getNombreRazonSocial());
            binding.textoCifCliente.setText(cliente.getNifCif());

            if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
                binding.textoEmailCliente.setVisibility(View.VISIBLE);
                binding.textoEmailCliente.setText(cliente.getEmail());
            } else {
                binding.textoEmailCliente.setVisibility(View.GONE);
            }

            if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
                binding.textoTelefonoCliente.setVisibility(View.VISIBLE);
                binding.textoTelefonoCliente.setText(cliente.getTelefono());
            } else {
                binding.textoTelefonoCliente.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClienteClick(cliente);
                }
            });
        }
    }
}
package com.example.baseboss.ui.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseboss.databinding.ItemGastoBinding;
import com.example.baseboss.datos.entidades.Gasto;
import com.example.baseboss.utilidades.FormateadorMoneda;

public class GastoAdaptador extends ListAdapter<Gasto, GastoAdaptador.GastoViewHolder> {

    private final OnGastoClickListener listener;

    public interface OnGastoClickListener {
        void onGastoClick(Gasto gasto);
    }

    public GastoAdaptador(OnGastoClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Gasto> DIFF_CALLBACK = new DiffUtil.ItemCallback<Gasto>() {
        @Override
        public boolean areItemsTheSame(@NonNull Gasto oldItem, @NonNull Gasto newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Gasto oldItem, @NonNull Gasto newItem) {
            return oldItem.getImporte() == newItem.getImporte()
                    && oldItem.getDescripcion().equals(newItem.getDescripcion())
                    && oldItem.getCategoria().equals(newItem.getCategoria());
        }
    };

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGastoBinding binding = ItemGastoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new GastoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {
        private final ItemGastoBinding binding;

        public GastoViewHolder(ItemGastoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Gasto gasto, OnGastoClickListener listener) {
            binding.textoGastoDescripcion.setText(gasto.getDescripcion());
            binding.textoGastoImporte.setText("-" + FormateadorMoneda.formatear(gasto.getImporte()));
            binding.etiquetaGastoCategoria.setText(gasto.getCategoria());
            binding.textoGastoFecha.setText(FormateadorMoneda.formatearFecha(gasto.getFecha()));

            if (gasto.getProveedor() != null && !gasto.getProveedor().trim().isEmpty()) {
                binding.textoGastoProveedor.setVisibility(View.VISIBLE);
                binding.textoGastoProveedor.setText("Proveedor: " + gasto.getProveedor());
            } else {
                binding.textoGastoProveedor.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onGastoClick(gasto);
            });
        }
    }
}
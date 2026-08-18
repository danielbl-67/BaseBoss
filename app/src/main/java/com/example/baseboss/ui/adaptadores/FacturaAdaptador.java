package com.example.baseboss.ui.adaptadores;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseboss.R;
import com.example.baseboss.databinding.ItemFacturaBinding;
import com.example.baseboss.datos.entidades.FacturaConDetalles;
import com.example.baseboss.utilidades.FormateadorMoneda;

public class FacturaAdaptador extends ListAdapter<FacturaConDetalles, FacturaAdaptador.FacturaViewHolder> {

    private final OnFacturaClickListener listener;

    public interface OnFacturaClickListener {
        void onFacturaClick(FacturaConDetalles facturaConDetalles);
    }

    public FacturaAdaptador(OnFacturaClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<FacturaConDetalles> DIFF_CALLBACK = new DiffUtil.ItemCallback<FacturaConDetalles>() {
        @Override
        public boolean areItemsTheSame(@NonNull FacturaConDetalles oldItem, @NonNull FacturaConDetalles newItem) {
            return oldItem.factura.getId() == newItem.factura.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FacturaConDetalles oldItem, @NonNull FacturaConDetalles newItem) {
            return oldItem.factura.getEstado().equals(newItem.factura.getEstado())
                    && oldItem.factura.getTotal() == newItem.factura.getTotal()
                    && oldItem.factura.getNumeroFactura().equals(newItem.factura.getNumeroFactura());
        }
    };

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFacturaBinding binding = ItemFacturaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FacturaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        private final ItemFacturaBinding binding;

        public FacturaViewHolder(ItemFacturaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FacturaConDetalles item, OnFacturaClickListener listener) {
            binding.textoNumeroFactura.setText(item.factura.getNumeroFactura());
            binding.textoClienteFactura.setText(item.cliente != null ? item.cliente.getNombreRazonSocial() : "Cliente sin asignar");
            binding.textoFechaEmision.setText("Emisión: " + FormateadorMoneda.formatearFecha(item.factura.getFechaEmision()));
            binding.textoImporteTotal.setText(FormateadorMoneda.formatear(item.factura.getTotal()));

            String estado = item.factura.getEstado();
            binding.etiquetaEstadoFactura.setText(estado);

            int colorEstado;
            switch (estado) {
                case "PAGADA":
                    colorEstado = ContextCompat.getColor(itemView.getContext(), R.color.estado_pagada);
                    break;
                case "PENDIENTE":
                    colorEstado = ContextCompat.getColor(itemView.getContext(), R.color.estado_pendiente);
                    break;
                case "VENCIDA":
                    colorEstado = ContextCompat.getColor(itemView.getContext(), R.color.estado_vencida);
                    break;
                case "ANULADA":
                    colorEstado = ContextCompat.getColor(itemView.getContext(), R.color.estado_anulada);
                    break;
                default:
                    colorEstado = ContextCompat.getColor(itemView.getContext(), R.color.estado_borrador);
                    break;
            }

            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(16f);
            background.setColor(colorEstado);
            binding.etiquetaEstadoFactura.setBackground(background);
            binding.etiquetaEstadoFactura.setTextColor(Color.WHITE);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onFacturaClick(item);
            });
        }
    }
}
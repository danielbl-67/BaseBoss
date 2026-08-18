package com.example.baseboss.ui.adaptadores;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseboss.databinding.ItemLineaFacturaEdicionBinding;
import com.example.baseboss.datos.entidades.LineaFactura;
import com.example.baseboss.utilidades.FormateadorMoneda;

import java.util.ArrayList;
import java.util.List;

public class LineaFacturaFormularioAdaptador extends RecyclerView.Adapter<LineaFacturaFormularioAdaptador.LineaViewHolder> {

    private List<LineaFactura> listaLineas = new ArrayList<>();
    private final OnEliminarLineaListener listener;

    public interface OnEliminarLineaListener {
        void onEliminar(int posicion);
    }

    public LineaFacturaFormularioAdaptador(OnEliminarLineaListener listener) {
        this.listener = listener;
    }

    public void setLineas(List<LineaFactura> lineas) {
        this.listaLineas = lineas != null ? lineas : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LineaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLineaFacturaEdicionBinding binding = ItemLineaFacturaEdicionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new LineaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LineaViewHolder holder, int position) {
        holder.bind(listaLineas.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    static class LineaViewHolder extends RecyclerView.ViewHolder {
        private final ItemLineaFacturaEdicionBinding binding;

        public LineaViewHolder(ItemLineaFacturaEdicionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LineaFactura linea, int posicion, OnEliminarLineaListener listener) {
            binding.textoLineaDescripcion.setText(linea.getDescripcion());
            String detalleCalculo = String.format(java.util.Locale.getDefault(),
                    "%.2f uds x %s (IVA %.0f%%)",
                    linea.getCantidad(),
                    FormateadorMoneda.formatear(linea.getPrecioUnitario()),
                    linea.getPorcentajeIva());
            binding.textoLineaCalculo.setText(detalleCalculo);
            binding.textoLineaSubtotal.setText(FormateadorMoneda.formatear(linea.getSubtotal()));

            binding.botonEliminarLinea.setOnClickListener(v -> {
                if (listener != null) listener.onEliminar(posicion);
            });
        }
    }
}
package com.example.baseboss.ui.facturas;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentDetalleFacturaBinding;
import com.example.baseboss.databinding.ItemLineaFacturaDetalleBinding;
import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.entidades.Configuracion;
import com.example.baseboss.datos.entidades.FacturaConDetalles;
import com.example.baseboss.datos.entidades.LineaFactura;
import com.example.baseboss.repositorio.ConfiguracionRepositorio;
import com.example.baseboss.utilidades.FormateadorMoneda;
import com.example.baseboss.utilidades.GeneradorPdfFactura;

import java.io.File;

public class DetalleFacturaFragment extends Fragment {

    private FragmentDetalleFacturaBinding binding;
    private FacturaViewModel facturaViewModel;
    private ConfiguracionRepositorio configRepositorio;
    private long facturaId;
    private FacturaConDetalles facturaActual;
    private File ultimoPdfGenerado;

    private final String[] ESTADOS = {"BORRADOR", "PENDIENTE", "PAGADA", "VENCIDA", "ANULADA"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleFacturaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        facturaViewModel = new ViewModelProvider(this).get(FacturaViewModel.class);
        configRepositorio = new ConfiguracionRepositorio(requireActivity().getApplication());

        if (getArguments() != null) {
            facturaId = getArguments().getLong("facturaId", 0L);
        }

        facturaViewModel.obtenerFacturaPorId(facturaId).observe(getViewLifecycleOwner(), facturaConDetalles -> {
            if (facturaConDetalles != null) {
                facturaActual = facturaConDetalles;
                cargarDatosFactura(facturaConDetalles);
            }
        });

        binding.botonCambiarEstado.setOnClickListener(v -> mostrarDialogoCambiarEstado());
        binding.botonEliminarFactura.setOnClickListener(v -> mostrarDialogoEliminar());
        binding.botonGenerarPdf.setOnClickListener(v -> generarYCompartirPdf());
    }

    private void cargarDatosFactura(FacturaConDetalles f) {
        binding.detalleNumeroFactura.setText(f.factura.getNumeroFactura());
        binding.detalleClienteNombre.setText(f.cliente != null ? f.cliente.getNombreRazonSocial() : "Sin Cliente");
        binding.detalleClienteNif.setText(f.cliente != null ? "NIF/CIF: " + f.cliente.getNifCif() : "");

        String fechas = "Emisión: " + FormateadorMoneda.formatearFecha(f.factura.getFechaEmision()) +
                " | Vencimiento: " + FormateadorMoneda.formatearFecha(f.factura.getFechaVencimiento());
        binding.detalleFechas.setText(fechas);

        // Estado y Badge
        String estado = f.factura.getEstado();
        binding.detalleEstadoFactura.setText(estado);
        int color = ContextCompat.getColor(requireContext(),
                estado.equals("PAGADA") ? R.color.estado_pagada :
                        estado.equals("PENDIENTE") ? R.color.estado_pendiente :
                        estado.equals("VENCIDA") ? R.color.estado_vencida :
                        estado.equals("ANULADA") ? R.color.estado_anulada : R.color.estado_borrador);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(16f);
        bg.setColor(color);
        binding.detalleEstadoFactura.setBackground(bg);
        binding.detalleEstadoFactura.setTextColor(Color.WHITE);

        // Inflar líneas de concepto
        binding.contenedorLineasDetalle.removeAllViews();
        if (f.lineas != null) {
            for (LineaFactura linea : f.lineas) {
                ItemLineaFacturaDetalleBinding itemBinding = ItemLineaFacturaDetalleBinding.inflate(getLayoutInflater());
                itemBinding.detalleLineaDesc.setText(linea.getDescripcion());
                String cantPrecio = String.format(java.util.Locale.getDefault(), "%.2f uds x %s (IVA %.0f%%)",
                        linea.getCantidad(), FormateadorMoneda.formatear(linea.getPrecioUnitario()), linea.getPorcentajeIva());
                itemBinding.detalleLineaCantPrecio.setText(cantPrecio);
                itemBinding.detalleLineaTotal.setText(FormateadorMoneda.formatear(linea.getSubtotal()));
                binding.contenedorLineasDetalle.addView(itemBinding.getRoot());
            }
        }

        binding.detalleTotalBase.setText(FormateadorMoneda.formatear(f.factura.getBaseImponible()));
        binding.detalleTotalIva.setText(FormateadorMoneda.formatear(f.factura.getTotalIva()));
        binding.detalleTotalFinal.setText(FormateadorMoneda.formatear(f.factura.getTotal()));
    }

    private void generarYCompartirPdf() {
        if (facturaActual == null) return;

        Toast.makeText(requireContext(), "Generando factura PDF...", Toast.LENGTH_SHORT).show();

        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            try {
                Configuracion config = configRepositorio.obtenerConfiguracionSincrona();
                ultimoPdfGenerado = GeneradorPdfFactura.generarPdf(requireContext(), facturaActual, config);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "PDF generado con éxito", Toast.LENGTH_SHORT).show();
                    GeneradorPdfFactura.compartirPdf(requireContext(), ultimoPdfGenerado);
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void mostrarDialogoCambiarEstado() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cambiar estado de factura")
                .setItems(ESTADOS, (dialog, which) -> {
                    String nuevoEstado = ESTADOS[which];
                    if (facturaActual != null) {
                        facturaViewModel.cambiarEstadoFactura(facturaActual.factura, nuevoEstado);
                        Toast.makeText(requireContext(), "Estado actualizado a: " + nuevoEstado, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.accion_eliminar)
                .setMessage(R.string.confirmar_eliminacion)
                .setPositiveButton(R.string.accion_confirmar, (dialog, which) -> {
                    if (facturaActual != null) {
                        facturaViewModel.eliminarFactura(facturaActual.factura);
                        Toast.makeText(requireContext(), "Factura eliminada", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    }
                })
                .setNegativeButton(R.string.accion_cancelar, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
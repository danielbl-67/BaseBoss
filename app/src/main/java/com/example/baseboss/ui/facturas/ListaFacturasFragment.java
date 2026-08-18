package com.example.baseboss.ui.facturas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentListaFacturasBinding;
import com.example.baseboss.ui.adaptadores.FacturaAdaptador;

public class ListaFacturasFragment extends Fragment {

    private FragmentListaFacturasBinding binding;
    private FacturaViewModel facturaViewModel;
    private FacturaAdaptador adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListaFacturasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        facturaViewModel = new ViewModelProvider(this).get(FacturaViewModel.class);

        configurarRecyclerView();
        configurarFiltrosChips();
        observarDatos();

        binding.fabNuevaFactura.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("facturaId", 0L);
            Navigation.findNavController(v).navigate(R.id.accion_listaFacturas_a_crearFactura, bundle);
        });
    }

    private void configurarRecyclerView() {
        adaptador = new FacturaAdaptador(facturaConDetalles -> {
            Bundle bundle = new Bundle();
            bundle.putLong("facturaId", facturaConDetalles.factura.getId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.accion_listaFacturas_a_detalleFactura, bundle);
        });
        binding.recyclerFacturas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFacturas.setAdapter(adaptador);
    }

    private void configurarFiltrosChips() {
        binding.grupoChipsEstados.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int idSeleccionado = checkedIds.get(0);
            if (idSeleccionado == R.id.chip_pendientes) {
                facturaViewModel.establecerFiltroEstado("PENDIENTE");
            } else if (idSeleccionado == R.id.chip_pagadas) {
                facturaViewModel.establecerFiltroEstado("PAGADA");
            } else if (idSeleccionado == R.id.chip_vencidas) {
                facturaViewModel.establecerFiltroEstado("VENCIDA");
            } else if (idSeleccionado == R.id.chip_borradores) {
                facturaViewModel.establecerFiltroEstado("BORRADOR");
            } else {
                facturaViewModel.establecerFiltroEstado("TODOS");
            }
        });
    }

    private void observarDatos() {
        facturaViewModel.getFacturas().observe(getViewLifecycleOwner(), facturas -> {
            adaptador.submitList(facturas);
            binding.textoFacturasVacio.setVisibility(facturas == null || facturas.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
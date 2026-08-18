package com.example.baseboss.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baseboss.databinding.FragmentDashboardBinding;
import com.example.baseboss.utilidades.FormateadorMoneda;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        observarMetricas();
        dashboardViewModel.cargarDatosGrafico();
    }

    private void observarMetricas() {
        dashboardViewModel.getIngresosMes().observe(getViewLifecycleOwner(), ing ->
                binding.textoDashboardIngresos.setText(FormateadorMoneda.formatear(ing != null ? ing : 0.0))
        );

        dashboardViewModel.getGastosMes().observe(getViewLifecycleOwner(), gas ->
                binding.textoDashboardGastos.setText(FormateadorMoneda.formatear(gas != null ? gas : 0.0))
        );

        dashboardViewModel.getBeneficioMes().observe(getViewLifecycleOwner(), ben ->
                binding.textoDashboardBeneficio.setText(FormateadorMoneda.formatear(ben != null ? ben : 0.0))
        );

        dashboardViewModel.getFacturasPendientes().observe(getViewLifecycleOwner(), count ->
                binding.textoKpiPendientes.setText(String.valueOf(count != null ? count : 0))
        );

        dashboardViewModel.getFacturasVencidas().observe(getViewLifecycleOwner(), count ->
                binding.textoKpiVencidas.setText(String.valueOf(count != null ? count : 0))
        );

        dashboardViewModel.getTotalClientes().observe(getViewLifecycleOwner(), count ->
                binding.textoKpiClientes.setText(String.valueOf(count != null ? count : 0))
        );

        dashboardViewModel.getDatosGraficoHistorico().observe(getViewLifecycleOwner(), matriz -> {
            if (matriz != null) {
                binding.graficoBarrasDashboard.setDatos(matriz);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
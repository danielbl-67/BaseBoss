package com.example.baseboss.ui.gastos;

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
import com.example.baseboss.databinding.FragmentListaGastosBinding;
import com.example.baseboss.ui.adaptadores.GastoAdaptador;

public class ListaGastosFragment extends Fragment {

    private FragmentListaGastosBinding binding;
    private GastoViewModel gastoViewModel;
    private GastoAdaptador adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListaGastosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gastoViewModel = new ViewModelProvider(this).get(GastoViewModel.class);

        configurarRecyclerView();
        configurarFiltrosChips();
        observarDatos();

        binding.fabNuevoGasto.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("gastoId", 0L);
            Navigation.findNavController(v).navigate(R.id.accion_listaGastos_a_crearEditarGasto, bundle);
        });
    }

    private void configurarRecyclerView() {
        adaptador = new GastoAdaptador(gasto -> {
            Bundle bundle = new Bundle();
            bundle.putLong("gastoId", gasto.getId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.accion_listaGastos_a_crearEditarGasto, bundle);
        });
        binding.recyclerGastos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerGastos.setAdapter(adaptador);
    }

    private void configurarFiltrosChips() {
        binding.grupoChipsCategorias.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int idSeleccionado = checkedIds.get(0);
            if (idSeleccionado == R.id.chip_cat_material) {
                gastoViewModel.establecerFiltroCategoria("Material");
            } else if (idSeleccionado == R.id.chip_cat_transporte) {
                gastoViewModel.establecerFiltroCategoria("Transporte");
            } else if (idSeleccionado == R.id.chip_cat_software) {
                gastoViewModel.establecerFiltroCategoria("Software");
            } else if (idSeleccionado == R.id.chip_cat_telefonia) {
                gastoViewModel.establecerFiltroCategoria("Telefonía");
            } else if (idSeleccionado == R.id.chip_cat_marketing) {
                gastoViewModel.establecerFiltroCategoria("Marketing");
            } else if (idSeleccionado == R.id.chip_cat_otros) {
                gastoViewModel.establecerFiltroCategoria("Otros");
            } else {
                gastoViewModel.establecerFiltroCategoria("TODAS");
            }
        });
    }

    private void observarDatos() {
        gastoViewModel.getGastos().observe(getViewLifecycleOwner(), gastos -> {
            adaptador.submitList(gastos);
            binding.textoGastosVacio.setVisibility(gastos == null || gastos.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
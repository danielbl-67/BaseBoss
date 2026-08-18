package com.example.baseboss.ui.clientes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.baseboss.databinding.FragmentListaClientesBinding;
import com.example.baseboss.ui.adaptadores.ClienteAdaptador;

public class ListaClientesFragment extends Fragment {

    private FragmentListaClientesBinding binding;
    private ClienteViewModel clienteViewModel;
    private ClienteAdaptador adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListaClientesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        configurarRecyclerView();
        configurarBuscador();
        observarDatos();

        binding.fabAgregarCliente.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("clienteId", 0L); // 0 indica nuevo cliente
            Navigation.findNavController(v).navigate(R.id.accion_listaClientes_a_crearEditarCliente, bundle);
        });
    }

    private void configurarRecyclerView() {
        adaptador = new ClienteAdaptador(cliente -> {
            Bundle bundle = new Bundle();
            bundle.putLong("clienteId", cliente.getId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.accion_listaClientes_a_detalleCliente, bundle);
        });
        binding.recyclerClientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerClientes.setAdapter(adaptador);
    }

    private void configurarBuscador() {
        binding.campoBuscador.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clienteViewModel.establecerFiltroBusqueda(s != null ? s.toString() : "");
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void observarDatos() {
        clienteViewModel.getClientes().observe(getViewLifecycleOwner(), clientes -> {
            adaptador.submitList(clientes);
            binding.textoVacio.setVisibility(clientes == null || clientes.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
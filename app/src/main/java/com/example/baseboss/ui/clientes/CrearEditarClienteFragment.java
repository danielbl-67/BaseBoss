package com.example.baseboss.ui.clientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentCrearEditarClienteBinding;
import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.utilidades.Validador;

public class CrearEditarClienteFragment extends Fragment {

    private FragmentCrearEditarClienteBinding binding;
    private ClienteViewModel clienteViewModel;
    private long clienteId = 0L;
    private Cliente clienteActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearEditarClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        if (getArguments() != null) {
            clienteId = getArguments().getLong("clienteId", 0L);
        }

        if (clienteId != 0L) {
            binding.tituloFormularioCliente.setText("Editar Cliente");
            clienteViewModel.obtenerClientePorId(clienteId).observe(getViewLifecycleOwner(), cliente -> {
                if (cliente != null && clienteActual == null) {
                    clienteActual = cliente;
                    cargarDatosEnFormulario(cliente);
                }
            });
        }

        binding.botonGuardarCliente.setOnClickListener(v -> procesarGuardado());
    }

    private void cargarDatosEnFormulario(Cliente cliente) {
        binding.campoNombre.setText(cliente.getNombreRazonSocial());
        binding.campoNif.setText(cliente.getNifCif());
        binding.campoEmail.setText(cliente.getEmail());
        binding.campoTelefono.setText(cliente.getTelefono());
        binding.campoDireccion.setText(cliente.getDireccion());
        binding.campoCp.setText(cliente.getCodigoPostal());
        binding.campoCiudad.setText(cliente.getCiudad());
        binding.campoNotas.setText(cliente.getNotas());
    }

    private void procesarGuardado() {
        String nombre = binding.campoNombre.getText() != null ? binding.campoNombre.getText().toString().trim() : "";
        String nif = binding.campoNif.getText() != null ? binding.campoNif.getText().toString().trim() : "";
        String email = binding.campoEmail.getText() != null ? binding.campoEmail.getText().toString().trim() : "";
        String telefono = binding.campoTelefono.getText() != null ? binding.campoTelefono.getText().toString().trim() : "";
        String direccion = binding.campoDireccion.getText() != null ? binding.campoDireccion.getText().toString().trim() : "";
        String cp = binding.campoCp.getText() != null ? binding.campoCp.getText().toString().trim() : "";
        String ciudad = binding.campoCiudad.getText() != null ? binding.campoCiudad.getText().toString().trim() : "";
        String notas = binding.campoNotas.getText() != null ? binding.campoNotas.getText().toString().trim() : "";

        // Limpiar errores previos
        binding.layoutNombre.setError(null);
        binding.layoutNif.setError(null);
        binding.layoutEmail.setError(null);

        boolean esValido = true;

        if (!Validador.esTextoValido(nombre)) {
            binding.layoutNombre.setError(getString(R.string.error_campo_obligatorio));
            esValido = false;
        }

        if (!Validador.esTextoValido(nif)) {
            binding.layoutNif.setError(getString(R.string.error_campo_obligatorio));
            esValido = false;
        }

        if (!Validador.esEmailValido(email)) {
            binding.layoutEmail.setError(getString(R.string.error_email_invalido));
            esValido = false;
        }

        if (!esValido) return;

        if (clienteActual == null) {
            clienteActual = new Cliente(nombre, nif, email, telefono, direccion, cp, ciudad, notas);
        } else {
            clienteActual.setNombreRazonSocial(nombre);
            clienteActual.setNifCif(nif);
            clienteActual.setEmail(email);
            clienteActual.setTelefono(telefono);
            clienteActual.setDireccion(direccion);
            clienteActual.setCodigoPostal(cp);
            clienteActual.setCiudad(ciudad);
            clienteActual.setNotas(notas);
        }

        clienteViewModel.guardarCliente(clienteActual, id -> {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Cliente guardado con éxito", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigateUp();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
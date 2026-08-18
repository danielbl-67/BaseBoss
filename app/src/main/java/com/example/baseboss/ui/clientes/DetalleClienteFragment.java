package com.example.baseboss.ui.clientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentDetalleClienteBinding;
import com.example.baseboss.datos.entidades.Cliente;

public class DetalleClienteFragment extends Fragment {

    private FragmentDetalleClienteBinding binding;
    private ClienteViewModel clienteViewModel;
    private long clienteId;
    private Cliente clienteActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        if (getArguments() != null) {
            clienteId = getArguments().getLong("clienteId", 0L);
        }

        observarDetalleCliente();
        configurarBotones();
    }

    private void observarDetalleCliente() {
        clienteViewModel.obtenerClienteConFacturas(clienteId).observe(getViewLifecycleOwner(), clienteConFacturas -> {
            if (clienteConFacturas != null && clienteConFacturas.cliente != null) {
                clienteActual = clienteConFacturas.cliente;
                binding.detalleNombre.setText(clienteActual.getNombreRazonSocial());
                binding.detalleNif.setText(clienteActual.getNifCif());
                binding.detalleEmail.setText("Email: " + (clienteActual.getEmail().isEmpty() ? "No especificado" : clienteActual.getEmail()));
                binding.detalleTelefono.setText("Teléfono: " + (clienteActual.getTelefono().isEmpty() ? "No especificado" : clienteActual.getTelefono()));

                String ubicacion = clienteActual.getDireccion() + " " + clienteActual.getCodigoPostal() + " " + clienteActual.getCiudad();
                binding.detalleDireccion.setText("Dirección: " + (ubicacion.trim().isEmpty() ? "No especificada" : ubicacion.trim()));

                if (clienteActual.getNotas() != null && !clienteActual.getNotas().isEmpty()) {
                    binding.detalleNotas.setVisibility(View.VISIBLE);
                    binding.detalleNotas.setText("Notas: " + clienteActual.getNotas());
                } else {
                    binding.detalleNotas.setVisibility(View.GONE);
                }

                if (clienteConFacturas.facturas != null && !clienteConFacturas.facturas.isEmpty()) {
                    binding.textoSinFacturas.setText("Total de facturas emitidas: " + clienteConFacturas.facturas.size());
                } else {
                    binding.textoSinFacturas.setText("Este cliente aún no tiene facturas registradas.");
                }
            }
        });
    }

    private void configurarBotones() {
        binding.botonEditarCliente.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("clienteId", clienteId);
            Navigation.findNavController(v).navigate(R.id.accion_detalleCliente_a_crearEditarCliente, bundle);
        });

        binding.botonEliminarCliente.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());
    }

    private void mostrarDialogoConfirmacionEliminar() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.accion_eliminar)
                .setMessage(R.string.confirmar_eliminacion)
                .setPositiveButton(R.string.accion_confirmar, (dialog, which) -> {
                    if (clienteActual != null) {
                        try {
                            clienteViewModel.eliminarCliente(clienteActual);
                            Toast.makeText(requireContext(), "Cliente eliminado", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "No se puede eliminar un cliente con facturas asociadas", Toast.LENGTH_LONG).show();
                        }
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
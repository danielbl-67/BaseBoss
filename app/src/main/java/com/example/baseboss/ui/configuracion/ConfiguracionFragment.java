package com.example.baseboss.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentConfiguracionBinding;
import com.example.baseboss.datos.entidades.Configuracion;
import com.example.baseboss.utilidades.Validador;

public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;
    private ConfiguracionViewModel configuracionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configuracionViewModel = new ViewModelProvider(this).get(ConfiguracionViewModel.class);

        observarConfiguracion();

        binding.botonGuardarConfiguracion.setOnClickListener(v -> procesarGuardado());
    }

    private void observarConfiguracion() {
        configuracionViewModel.getConfiguracion().observe(getViewLifecycleOwner(), conf -> {
            if (conf != null) {
                binding.campoConfNombre.setText(conf.getNombreFiscal());
                binding.campoConfNif.setText(conf.getNifCif());
                binding.campoConfEmail.setText(conf.getEmail());
                binding.campoConfTelefono.setText(conf.getTelefono());
                binding.campoConfDireccion.setText(conf.getDireccion());
                binding.campoConfCp.setText(conf.getCodigoPostal());
                binding.campoConfCiudad.setText(conf.getCiudad());
                binding.campoConfIban.setText(conf.getIban());
            }
        });
    }

    private void procesarGuardado() {
        String nombre = binding.campoConfNombre.getText() != null ? binding.campoConfNombre.getText().toString().trim() : "";
        String nif = binding.campoConfNif.getText() != null ? binding.campoConfNif.getText().toString().trim() : "";
        String email = binding.campoConfEmail.getText() != null ? binding.campoConfEmail.getText().toString().trim() : "";
        String telefono = binding.campoConfTelefono.getText() != null ? binding.campoConfTelefono.getText().toString().trim() : "";
        String direccion = binding.campoConfDireccion.getText() != null ? binding.campoConfDireccion.getText().toString().trim() : "";
        String cp = binding.campoConfCp.getText() != null ? binding.campoConfCp.getText().toString().trim() : "";
        String ciudad = binding.campoConfCiudad.getText() != null ? binding.campoConfCiudad.getText().toString().trim() : "";
        String iban = binding.campoConfIban.getText() != null ? binding.campoConfIban.getText().toString().trim() : "";

        binding.layoutConfNombre.setError(null);
        binding.layoutConfNif.setError(null);
        binding.layoutConfEmail.setError(null);

        boolean esValido = true;

        if (!Validador.esTextoValido(nombre)) {
            binding.layoutConfNombre.setError(getString(R.string.error_campo_obligatorio));
            esValido = false;
        }

        if (!Validador.esTextoValido(nif)) {
            binding.layoutConfNif.setError(getString(R.string.error_campo_obligatorio));
            esValido = false;
        }

        if (!Validador.esEmailValido(email) || !Validador.esTextoValido(email)) {
            binding.layoutConfEmail.setError(getString(R.string.error_email_invalido));
            esValido = false;
        }

        if (!esValido) return;

        Configuracion nuevaConfiguracion = new Configuracion(nombre, nif, email, telefono, direccion, cp, ciudad, iban, null);
        nuevaConfiguracion.setId(1);

        configuracionViewModel.guardarConfiguracion(nuevaConfiguracion);
        Toast.makeText(requireContext(), "Configuración guardada correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.baseboss.ui.gastos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.baseboss.R;
import com.example.baseboss.databinding.FragmentCrearEditarGastoBinding;
import com.example.baseboss.datos.entidades.Gasto;
import com.example.baseboss.utilidades.FormateadorMoneda;
import com.example.baseboss.utilidades.Validador;

import java.util.Calendar;
import java.util.Date;

public class CrearEditarGastoFragment extends Fragment {

    private FragmentCrearEditarGastoBinding binding;
    private GastoViewModel gastoViewModel;
    private long gastoId = 0L;
    private Gasto gastoActual;
    private Date fechaGasto = new Date();

    private final String[] CATEGORIAS = {
            "Material", "Transporte", "Software", "Telefonía",
            "Marketing", "Equipamiento", "Formación", "Servicios", "Otros"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearEditarGastoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gastoViewModel = new ViewModelProvider(this).get(GastoViewModel.class);

        if (getArguments() != null) {
            gastoId = getArguments().getLong("gastoId", 0L);
        }

        configurarSelectorCategorias();
        configurarSelectorFecha();

        if (gastoId != 0L) {
            binding.tituloFormularioGasto.setText("Editar Gasto");
            binding.botonEliminarGasto.setVisibility(View.VISIBLE);
            gastoViewModel.obtenerGastoPorId(gastoId).observe(getViewLifecycleOwner(), gasto -> {
                if (gasto != null && gastoActual == null) {
                    gastoActual = gasto;
                    cargarDatosEnFormulario(gasto);
                }
            });
        }

        binding.botonGuardarGasto.setOnClickListener(v -> procesarGuardado());
        binding.botonEliminarGasto.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    private void configurarSelectorCategorias() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, CATEGORIAS);
        binding.spinnerCategoriaGasto.setAdapter(adapter);
        binding.spinnerCategoriaGasto.setText(CATEGORIAS[0], false);
    }

    private void configurarSelectorFecha() {
        binding.campoFechaGasto.setText(FormateadorMoneda.formatearFecha(fechaGasto));
        binding.campoFechaGasto.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaGasto);
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                Calendar seleccion = Calendar.getInstance();
                seleccion.set(year, month, dayOfMonth);
                fechaGasto = seleccion.getTime();
                binding.campoFechaGasto.setText(FormateadorMoneda.formatearFecha(fechaGasto));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void cargarDatosEnFormulario(Gasto gasto) {
        binding.spinnerCategoriaGasto.setText(gasto.getCategoria(), false);
        binding.campoDescripcionGasto.setText(gasto.getDescripcion());
        binding.campoImporteGasto.setText(String.valueOf(gasto.getImporte()));
        binding.campoIvaGasto.setText(String.valueOf((int) gasto.getPorcentajeIva()));
        binding.campoProveedorGasto.setText(gasto.getProveedor());
        binding.campoNotasGasto.setText(gasto.getNotas());
        fechaGasto = gasto.getFecha();
        binding.campoFechaGasto.setText(FormateadorMoneda.formatearFecha(fechaGasto));
    }

    private void procesarGuardado() {
        String categoria = binding.spinnerCategoriaGasto.getText().toString().trim();
        String descripcion = binding.campoDescripcionGasto.getText() != null ? binding.campoDescripcionGasto.getText().toString().trim() : "";
        String importeStr = binding.campoImporteGasto.getText() != null ? binding.campoImporteGasto.getText().toString().trim() : "";
        String ivaStr = binding.campoIvaGasto.getText() != null ? binding.campoIvaGasto.getText().toString().trim() : "21";
        String proveedor = binding.campoProveedorGasto.getText() != null ? binding.campoProveedorGasto.getText().toString().trim() : "";
        String notas = binding.campoNotasGasto.getText() != null ? binding.campoNotasGasto.getText().toString().trim() : "";

        binding.layoutDescripcionGasto.setError(null);
        binding.layoutImporteGasto.setError(null);

        boolean esValido = true;

        if (!Validador.esTextoValido(descripcion)) {
            binding.layoutDescripcionGasto.setError(getString(R.string.error_campo_obligatorio));
            esValido = false;
        }

        double importe = 0.0;
        try {
            importe = Double.parseDouble(importeStr);
            if (importe <= 0) {
                binding.layoutImporteGasto.setError(getString(R.string.error_precio_invalido));
                esValido = false;
            }
        } catch (NumberFormatException e) {
            binding.layoutImporteGasto.setError(getString(R.string.error_precio_invalido));
            esValido = false;
        }

        double iva = 21.0;
        try {
            iva = Double.parseDouble(ivaStr);
        } catch (NumberFormatException ignored) {}

        if (!esValido) return;

        if (gastoActual == null) {
            gastoActual = new Gasto(fechaGasto, categoria, descripcion, importe, iva, proveedor, notas, null);
        } else {
            gastoActual.setFecha(fechaGasto);
            gastoActual.setCategoria(categoria);
            gastoActual.setDescripcion(descripcion);
            gastoActual.setImporte(importe);
            gastoActual.setPorcentajeIva(iva);
            gastoActual.setProveedor(proveedor);
            gastoActual.setNotas(notas);
        }

        gastoViewModel.guardarGasto(gastoActual, id -> requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), "Gasto guardado correctamente", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }));
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.accion_eliminar)
                .setMessage(R.string.confirmar_eliminacion)
                .setPositiveButton(R.string.accion_confirmar, (dialog, which) -> {
                    if (gastoActual != null) {
                        gastoViewModel.eliminarGasto(gastoActual);
                        Toast.makeText(requireContext(), "Gasto eliminado", Toast.LENGTH_SHORT).show();
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
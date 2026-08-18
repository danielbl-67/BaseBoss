package com.example.baseboss.ui.facturas;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseboss.R;
import com.example.baseboss.databinding.DialogoAgregarLineaBinding;
import com.example.baseboss.databinding.FragmentCrearFacturaBinding;
import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.Factura;
import com.example.baseboss.datos.entidades.LineaFactura;
import com.example.baseboss.ui.adaptadores.LineaFacturaFormularioAdaptador;
import com.example.baseboss.utilidades.FormateadorMoneda;
import com.example.baseboss.utilidades.Validador;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearFacturaFragment extends Fragment {

    private FragmentCrearFacturaBinding binding;
    private FacturaViewModel facturaViewModel;
    private LineaFacturaFormularioAdaptador adaptadorLineas;

    private List<Cliente> listaClientes = new ArrayList<>();
    private Cliente clienteSeleccionado;
    private Date fechaEmision = new Date();
    private Date fechaVencimiento = new Date();

    private final String[] ESTADOS = {"BORRADOR", "PENDIENTE", "PAGADA", "VENCIDA", "ANULADA"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearFacturaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        facturaViewModel = new ViewModelProvider(this).get(FacturaViewModel.class);
        facturaViewModel.limpiarLineas();

        configurarSpinners();
        configurarFechas();
        configurarRecyclerLineas();
        observarTotales();

        facturaViewModel.generarSiguienteNumero(numero ->
                requireActivity().runOnUiThread(() -> binding.campoNumeroFactura.setText(numero))
        );

        binding.botonAgregarLinea.setOnClickListener(v -> mostrarDialogoAgregarLinea());
        binding.botonGuardarFactura.setOnClickListener(v -> guardarFactura());
    }

    private void configurarSpinners() {
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, ESTADOS);
        binding.spinnerEstadoFactura.setAdapter(adapterEstados);
        binding.spinnerEstadoFactura.setText(ESTADOS[1], false); // Por defecto PENDIENTE

        facturaViewModel.obtenerListaClientes().observe(getViewLifecycleOwner(), clientes -> {
            this.listaClientes = clientes != null ? clientes : new ArrayList<>();
            List<String> nombres = new ArrayList<>();
            for (Cliente c : listaClientes) {
                nombres.add(c.getNombreRazonSocial() + " (" + c.getNifCif() + ")");
            }
            ArrayAdapter<String> adapterClientes = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_dropdown_item_1line, nombres);
            binding.spinnerCliente.setAdapter(adapterClientes);
        });

        binding.spinnerCliente.setOnItemClickListener((parent, view, position, id) -> {
            if (position < listaClientes.size()) {
                clienteSeleccionado = listaClientes.get(position);
            }
        });
    }

    private void configurarFechas() {
        // Inicializar vencimiento por defecto a +30 días
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        fechaVencimiento = cal.getTime();

        actualizarCamposFecha();

        binding.campoFechaEmision.setOnClickListener(v -> mostrarDatePicker(true));
        binding.campoFechaVencimiento.setOnClickListener(v -> mostrarDatePicker(false));
    }

    private void actualizarCamposFecha() {
        binding.campoFechaEmision.setText(FormateadorMoneda.formatearFecha(fechaEmision));
        binding.campoFechaVencimiento.setText(FormateadorMoneda.formatearFecha(fechaVencimiento));
    }

    private void mostrarDatePicker(boolean esEmision) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(esEmision ? fechaEmision : fechaVencimiento);

        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            Calendar seleccion = Calendar.getInstance();
            seleccion.set(year, month, dayOfMonth);
            if (esEmision) {
                fechaEmision = seleccion.getTime();
            } else {
                fechaVencimiento = seleccion.getTime();
            }
            actualizarCamposFecha();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void configurarRecyclerLineas() {
        adaptadorLineas = new LineaFacturaFormularioAdaptador(posicion -> facturaViewModel.eliminarLinea(posicion));
        binding.recyclerLineasFormulario.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerLineasFormulario.setAdapter(adaptadorLineas);

        facturaViewModel.getLineasFactura().observe(getViewLifecycleOwner(), lineas -> adaptadorLineas.setLineas(lineas));
    }

    private void observarTotales() {
        facturaViewModel.getBaseImponibleTotal().observe(getViewLifecycleOwner(), base ->
                binding.textoResumenBase.setText(FormateadorMoneda.formatear(base)));

        facturaViewModel.getIvaTotal().observe(getViewLifecycleOwner(), iva ->
                binding.textoResumenIva.setText(FormateadorMoneda.formatear(iva)));

        facturaViewModel.getTotalFactura().observe(getViewLifecycleOwner(), total ->
                binding.textoResumenTotal.setText(FormateadorMoneda.formatear(total)));
    }

    private void mostrarDialogoAgregarLinea() {
        DialogoAgregarLineaBinding dialogBinding = DialogoAgregarLineaBinding.inflate(getLayoutInflater());
        new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Añadir", (dialog, which) -> {
                    String desc = dialogBinding.dialogoCampoDescripcion.getText() != null ? dialogBinding.dialogoCampoDescripcion.getText().toString().trim() : "";
                    String cantStr = dialogBinding.dialogoCampoCantidad.getText() != null ? dialogBinding.dialogoCampoCantidad.getText().toString().trim() : "1";
                    String precioStr = dialogBinding.dialogoCampoPrecio.getText() != null ? dialogBinding.dialogoCampoPrecio.getText().toString().trim() : "0";
                    String ivaStr = dialogBinding.dialogoCampoIva.getText() != null ? dialogBinding.dialogoCampoIva.getText().toString().trim() : "21";

                    if (!Validador.esTextoValido(desc) || precioStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Descripción y precio son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double cant = Double.parseDouble(cantStr);
                        double precio = Double.parseDouble(precioStr);
                        double iva = Double.parseDouble(ivaStr);

                        if (cant <= 0 || precio < 0) {
                            Toast.makeText(requireContext(), "Valores numéricos no válidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double subtotal = cant * precio;
                        LineaFactura linea = new LineaFactura(0, desc, cant, precio, iva, subtotal);
                        facturaViewModel.agregarLinea(linea);
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Error en el formato de números", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarFactura() {
        String numero = binding.campoNumeroFactura.getText() != null ? binding.campoNumeroFactura.getText().toString().trim() : "";
        String estado = binding.spinnerEstadoFactura.getText().toString().trim();
        String notas = binding.campoNotasFactura.getText() != null ? binding.campoNotasFactura.getText().toString().trim() : "";

        if (clienteSeleccionado == null) {
            binding.layoutSelectorCliente.setError("Selecciona un cliente válido");
            return;
        } else {
            binding.layoutSelectorCliente.setError(null);
        }

        if (!Validador.esTextoValido(numero)) {
            binding.layoutNumeroFactura.setError("Indica el número de factura");
            return;
        }

        List<LineaFactura> lineas = facturaViewModel.getLineasFactura().getValue();
        if (lineas == null || lineas.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.error_sin_lineas), Toast.LENGTH_LONG).show();
            return;
        }

        double base = facturaViewModel.getBaseImponibleTotal().getValue() != null ? facturaViewModel.getBaseImponibleTotal().getValue() : 0.0;
        double iva = facturaViewModel.getIvaTotal().getValue() != null ? facturaViewModel.getIvaTotal().getValue() : 0.0;
        double total = facturaViewModel.getTotalFactura().getValue() != null ? facturaViewModel.getTotalFactura().getValue() : 0.0;

        Factura factura = new Factura(numero, clienteSeleccionado.getId(), fechaEmision, fechaVencimiento, estado, base, iva, total, notas);

        facturaViewModel.guardarFactura(factura, id -> requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), "Factura creada correctamente", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
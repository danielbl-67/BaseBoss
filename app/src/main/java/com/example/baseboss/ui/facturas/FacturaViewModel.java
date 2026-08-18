package com.example.baseboss.ui.facturas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.Factura;
import com.example.baseboss.datos.entidades.FacturaConDetalles;
import com.example.baseboss.datos.entidades.LineaFactura;
import com.example.baseboss.repositorio.ClienteRepositorio;
import com.example.baseboss.repositorio.FacturaRepositorio;

import java.util.ArrayList;
import java.util.List;

public class FacturaViewModel extends AndroidViewModel {

    private final FacturaRepositorio facturaRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    private final MutableLiveData<String> filtroEstado = new MutableLiveData<>("TODOS");
    private final LiveData<List<FacturaConDetalles>> facturasFiltradas;

    // Estado del formulario de creación/edición
    private final MutableLiveData<List<LineaFactura>> lineasFactura = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Double> baseImponibleTotal = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> ivaTotal = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalFactura = new MutableLiveData<>(0.0);

    public FacturaViewModel(@NonNull Application application) {
        super(application);
        this.facturaRepositorio = new FacturaRepositorio(application);
        this.clienteRepositorio = new ClienteRepositorio(application);

        this.facturasFiltradas = Transformations.switchMap(filtroEstado, estado -> {
            if (estado == null || estado.equals("TODOS")) {
                return facturaRepositorio.obtenerTodasLasFacturas();
            } else {
                return facturaRepositorio.obtenerFacturasPorEstado(estado);
            }
        });
    }

    public LiveData<List<FacturaConDetalles>> getFacturas() {
        return facturasFiltradas;
    }

    public void establecerFiltroEstado(String estado) {
        filtroEstado.setValue(estado);
    }

    public LiveData<FacturaConDetalles> obtenerFacturaPorId(long id) {
        return facturaRepositorio.obtenerFacturaPorId(id);
    }

    public LiveData<List<Cliente>> obtenerListaClientes() {
        return clienteRepositorio.obtenerTodosLosClientes();
    }

    public LiveData<List<LineaFactura>> getLineasFactura() {
        return lineasFactura;
    }

    public LiveData<Double> getBaseImponibleTotal() { return baseImponibleTotal; }
    public LiveData<Double> getIvaTotal() { return ivaTotal; }
    public LiveData<Double> getTotalFactura() { return totalFactura; }

    public void generarSiguienteNumero(FacturaRepositorio.OnNumeroGenerado callback) {
        facturaRepositorio.obtenerSiguienteNumeroFactura("F", callback);
    }

    public void agregarLinea(LineaFactura linea) {
        List<LineaFactura> actual = lineasFactura.getValue() != null ? new ArrayList<>(lineasFactura.getValue()) : new ArrayList<>();
        actual.add(linea);
        lineasFactura.setValue(actual);
        recalcularTotales(actual);
    }

    public void eliminarLinea(int posicion) {
        List<LineaFactura> actual = lineasFactura.getValue() != null ? new ArrayList<>(lineasFactura.getValue()) : new ArrayList<>();
        if (posicion >= 0 && posicion < actual.size()) {
            actual.remove(posicion);
            lineasFactura.setValue(actual);
            recalcularTotales(actual);
        }
    }

    public void limpiarLineas() {
        lineasFactura.setValue(new ArrayList<>());
        recalcularTotales(new ArrayList<>());
    }

    public void cargarLineasExistentes(List<LineaFactura> lineas) {
        lineasFactura.setValue(new ArrayList<>(lineas));
        recalcularTotales(lineas);
    }

    private void recalcularTotales(List<LineaFactura> lineas) {
        double subtotal = 0.0;
        double iva = 0.0;

        for (LineaFactura l : lineas) {
            double baseLinea = l.getCantidad() * l.getPrecioUnitario();
            double ivaLinea = baseLinea * (l.getPorcentajeIva() / 100.0);
            subtotal += baseLinea;
            iva += ivaLinea;
        }

        baseImponibleTotal.setValue(subtotal);
        ivaTotal.setValue(iva);
        totalFactura.setValue(subtotal + iva);
    }

    public void guardarFactura(Factura factura, FacturaRepositorio.OnFacturaGuardada callback) {
        List<LineaFactura> lineas = lineasFactura.getValue() != null ? lineasFactura.getValue() : new ArrayList<>();
        facturaRepositorio.guardarFacturaConLineas(factura, lineas, callback);
    }

    public void cambiarEstadoFactura(Factura factura, String nuevoEstado) {
        facturaRepositorio.actualizarEstadoFactura(factura, nuevoEstado);
    }

    public void eliminarFactura(Factura factura) {
        facturaRepositorio.eliminarFactura(factura);
    }
}
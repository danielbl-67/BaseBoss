package com.example.baseboss.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.dao.FacturaDao;
import com.example.baseboss.datos.dao.LineaFacturaDao;
import com.example.baseboss.datos.entidades.Factura;
import com.example.baseboss.datos.entidades.FacturaConDetalles;
import com.example.baseboss.datos.entidades.LineaFactura;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Repositorio para la gestión de transacciones de facturación y numeración automática.
 */
public class FacturaRepositorio {

    private final FacturaDao facturaDao;
    private final LineaFacturaDao lineaFacturaDao;
    private final LiveData<List<FacturaConDetalles>> todasLasFacturas;

    public FacturaRepositorio(Application aplicacion) {
        BaseBossDatabase db = BaseBossDatabase.obtenerInstancia(aplicacion);
        this.facturaDao = db.facturaDao();
        this.lineaFacturaDao = db.lineaFacturaDao();
        this.todasLasFacturas = facturaDao.obtenerTodasLasFacturasConDetalles();
    }

    public LiveData<List<FacturaConDetalles>> obtenerTodasLasFacturas() {
        return todasLasFacturas;
    }

    public LiveData<FacturaConDetalles> obtenerFacturaPorId(long id) {
        return facturaDao.obtenerFacturaPorId(id);
    }

    public LiveData<List<FacturaConDetalles>> obtenerFacturasPorEstado(String estado) {
        return facturaDao.obtenerFacturasPorEstado(estado);
    }

    /**
     * Guarda la factura y sus líneas de detalle de forma atómica.
     */
    public void guardarFacturaConLineas(Factura factura, List<LineaFactura> lineas, OnFacturaGuardada callback) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            long facturaId;
            if (factura.getId() == 0) {
                facturaId = facturaDao.insertar(factura);
            } else {
                facturaDao.actualizar(factura);
                facturaId = factura.getId();
                lineaFacturaDao.eliminarPorFacturaId(facturaId);
            }

            for (LineaFactura linea : lineas) {
                linea.setFacturaId(facturaId);
            }
            lineaFacturaDao.insertarLineas(lineas);

            if (callback != null) {
                callback.alGuardar(facturaId);
            }
        });
    }

    public void actualizarEstadoFactura(Factura factura, String nuevoEstado) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            factura.setEstado(nuevoEstado);
            facturaDao.actualizar(factura);
        });
    }

    public void eliminarFactura(Factura factura) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> facturaDao.eliminar(factura));
    }

    /**
     * Genera el siguiente número de factura correlativo basándose en la serie y el año actual.
     * Ejemplo: F-2026-001
     */
    public void obtenerSiguienteNumeroFactura(String serie, OnNumeroGenerado callback) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            int anioActual = Calendar.getInstance().get(Calendar.YEAR);
            String prefijo = String.format(Locale.getDefault(), "%s-%d-", serie, anioActual);
            String ultimoNumero = facturaDao.obtenerUltimoNumeroFactura(prefijo);

            int siguienteSecuencial = 1;
            if (ultimoNumero != null && ultimoNumero.startsWith(prefijo)) {
                try {
                    String numeroStr = ultimoNumero.substring(prefijo.length());
                    siguienteSecuencial = Integer.parseInt(numeroStr) + 1;
                } catch (NumberFormatException ignored) {}
            }

            String nuevoNumero = String.format(Locale.getDefault(), "%s%03d", prefijo, siguienteSecuencial);
            if (callback != null) {
                callback.alGenerar(nuevoNumero);
            }
        });
    }

    public interface OnFacturaGuardada {
        void alGuardar(long idFactura);
    }

    public interface OnNumeroGenerado {
        void alGenerar(String numeroFactura);
    }
}
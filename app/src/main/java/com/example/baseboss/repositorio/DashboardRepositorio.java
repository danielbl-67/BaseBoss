package com.example.baseboss.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.dao.ClienteDao;
import com.example.baseboss.datos.dao.FacturaDao;
import com.example.baseboss.datos.dao.GastoDao;

import java.util.Calendar;
import java.util.Date;

public class DashboardRepositorio {

    private final FacturaDao facturaDao;
    private final GastoDao gastoDao;
    private final ClienteDao clienteDao;

    public DashboardRepositorio(Application aplicacion) {
        BaseBossDatabase db = BaseBossDatabase.obtenerInstancia(aplicacion);
        this.facturaDao = db.facturaDao();
        this.gastoDao = db.gastoDao();
        this.clienteDao = db.clienteDao();
    }

    public LiveData<Integer> obtenerNumeroClientes() {
        return clienteDao.contarClientes();
    }

    public LiveData<Integer> obtenerFacturasPendientes() {
        return facturaDao.contarFacturasPendientes();
    }

    public LiveData<Integer> obtenerFacturasVencidas() {
        return facturaDao.contarFacturasVencidas();
    }

    public LiveData<Double> obtenerIngresosMesActual() {
        Calendar calInicio = Calendar.getInstance();
        calInicio.set(Calendar.DAY_OF_MONTH, 1);
        calInicio.set(Calendar.HOUR_OF_DAY, 0);
        calInicio.set(Calendar.MINUTE, 0);
        calInicio.set(Calendar.SECOND, 0);

        Calendar calFin = Calendar.getInstance();
        calFin.set(Calendar.DAY_OF_MONTH, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
        calFin.set(Calendar.HOUR_OF_DAY, 23);
        calFin.set(Calendar.MINUTE, 59);
        calFin.set(Calendar.SECOND, 59);

        return facturaDao.obtenerIngresosEnPeriodo(calInicio.getTime(), calFin.getTime());
    }

    public LiveData<Double> obtenerGastosMesActual() {
        Calendar calInicio = Calendar.getInstance();
        calInicio.set(Calendar.DAY_OF_MONTH, 1);
        calInicio.set(Calendar.HOUR_OF_DAY, 0);
        calInicio.set(Calendar.MINUTE, 0);
        calInicio.set(Calendar.SECOND, 0);

        Calendar calFin = Calendar.getInstance();
        calFin.set(Calendar.DAY_OF_MONTH, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
        calFin.set(Calendar.HOUR_OF_DAY, 23);
        calFin.set(Calendar.MINUTE, 59);
        calFin.set(Calendar.SECOND, 59);

        return gastoDao.obtenerTotalGastosEnPeriodo(calInicio.getTime(), calFin.getTime());
    }

    public double[] obtenerBalanceMensualSincrono(int mesOffset) {
        Calendar calInicio = Calendar.getInstance();
        calInicio.add(Calendar.MONTH, -mesOffset);
        calInicio.set(Calendar.DAY_OF_MONTH, 1);
        calInicio.set(Calendar.HOUR_OF_DAY, 0);
        calInicio.set(Calendar.MINUTE, 0);
        calInicio.set(Calendar.SECOND, 0);

        Calendar calFin = Calendar.getInstance();
        calFin.add(Calendar.MONTH, -mesOffset);
        calFin.set(Calendar.DAY_OF_MONTH, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
        calFin.set(Calendar.HOUR_OF_DAY, 23);
        calFin.set(Calendar.MINUTE, 59);
        calFin.set(Calendar.SECOND, 59);

        Double ingresos = facturaDao.obtenerIngresosEnPeriodoSincrono(calInicio.getTime(), calFin.getTime());
        Double gastos = gastoDao.obtenerTotalGastosEnPeriodoSincrono(calInicio.getTime(), calFin.getTime());

        return new double[]{ingresos != null ? ingresos : 0.0, gastos != null ? gastos : 0.0};
    }
}
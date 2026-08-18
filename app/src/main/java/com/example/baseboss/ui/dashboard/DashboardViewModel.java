package com.example.baseboss.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.repositorio.DashboardRepositorio;

public class DashboardViewModel extends AndroidViewModel {

    private final DashboardRepositorio repositorio;

    private final LiveData<Double> ingresosMes;
    private final LiveData<Double> gastosMes;
    private final MediatorLiveData<Double> beneficioMes = new MediatorLiveData<>();

    private final LiveData<Integer> totalClientes;
    private final LiveData<Integer> facturasPendientes;
    private final LiveData<Integer> facturasVencidas;

    private final MutableLiveData<float[][]> datosGraficoHistorico = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = new DashboardRepositorio(application);

        this.ingresosMes = repositorio.obtenerIngresosMesActual();
        this.gastosMes = repositorio.obtenerGastosMesActual();
        this.totalClientes = repositorio.obtenerNumeroClientes();
        this.facturasPendientes = repositorio.obtenerFacturasPendientes();
        this.facturasVencidas = repositorio.obtenerFacturasVencidas();

        beneficioMes.addSource(ingresosMes, ing -> calcularBeneficio());
        beneficioMes.addSource(gastosMes, gas -> calcularBeneficio());
    }

    private void calcularBeneficio() {
        double ing = ingresosMes.getValue() != null ? ingresosMes.getValue() : 0.0;
        double gas = gastosMes.getValue() != null ? gastosMes.getValue() : 0.0;
        beneficioMes.setValue(ing - gas);
    }

    public LiveData<Double> getIngresosMes() { return ingresosMes; }
    public LiveData<Double> getGastosMes() { return gastosMes; }
    public LiveData<Double> getBeneficioMes() { return beneficioMes; }
    public LiveData<Integer> getTotalClientes() { return totalClientes; }
    public LiveData<Integer> getFacturasPendientes() { return facturasPendientes; }
    public LiveData<Integer> getFacturasVencidas() { return facturasVencidas; }
    public LiveData<float[][]> getDatosGraficoHistorico() { return datosGraficoHistorico; }

    public void cargarDatosGrafico() {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            float[][] matriz = new float[3][2]; // Últimos 3 meses: [Ingresos, Gastos]
            for (int i = 0; i < 3; i++) {
                double[] balance = repositorio.obtenerBalanceMensualSincrono(2 - i);
                matriz[i][0] = (float) balance[0];
                matriz[i][1] = (float) balance[1];
            }
            datosGraficoHistorico.postValue(matriz);
        });
    }
}
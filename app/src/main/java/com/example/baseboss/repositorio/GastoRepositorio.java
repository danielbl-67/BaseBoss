package com.example.baseboss.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.dao.GastoDao;
import com.example.baseboss.datos.entidades.Gasto;

import java.util.List;

public class GastoRepositorio {

    private final GastoDao gastoDao;
    private final LiveData<List<Gasto>> todosLosGastos;

    public GastoRepositorio(Application aplicacion) {
        BaseBossDatabase db = BaseBossDatabase.obtenerInstancia(aplicacion);
        this.gastoDao = db.gastoDao();
        this.todosLosGastos = gastoDao.obtenerTodosLosGastos();
    }

    public LiveData<List<Gasto>> obtenerTodosLosGastos() {
        return todosLosGastos;
    }

    public LiveData<Gasto> obtenerGastoPorId(long id) {
        return gastoDao.obtenerGastoPorId(id);
    }

    public LiveData<List<Gasto>> obtenerGastosPorCategoria(String categoria) {
        return gastoDao.obtenerGastosPorCategoria(categoria);
    }

    public LiveData<List<Gasto>> buscarGastos(String busqueda) {
        return gastoDao.buscarGastos(busqueda);
    }

    public void insertar(Gasto gasto, OnGastoOperacionCompletada callback) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            long idGenerado = gastoDao.insertar(gasto);
            if (callback != null) {
                callback.alCompletar(idGenerado);
            }
        });
    }

    public void actualizar(Gasto gasto) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> gastoDao.actualizar(gasto));
    }

    public void eliminar(Gasto gasto) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> gastoDao.eliminar(gasto));
    }

    public interface OnGastoOperacionCompletada {
        void alCompletar(long id);
    }
}
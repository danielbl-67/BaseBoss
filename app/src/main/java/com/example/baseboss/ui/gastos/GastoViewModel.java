package com.example.baseboss.ui.gastos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.baseboss.datos.entidades.Gasto;
import com.example.baseboss.repositorio.GastoRepositorio;

import java.util.List;

public class GastoViewModel extends AndroidViewModel {

    private final GastoRepositorio repositorio;
    private final MutableLiveData<String> filtroCategoria = new MutableLiveData<>("TODAS");
    private final LiveData<List<Gasto>> gastosFiltrados;

    public GastoViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = new GastoRepositorio(application);

        this.gastosFiltrados = Transformations.switchMap(filtroCategoria, cat -> {
            if (cat == null || cat.equals("TODAS")) {
                return repositorio.obtenerTodosLosGastos();
            } else {
                return repositorio.obtenerGastosPorCategoria(cat);
            }
        });
    }

    public LiveData<List<Gasto>> getGastos() {
        return gastosFiltrados;
    }

    public void establecerFiltroCategoria(String categoria) {
        filtroCategoria.setValue(categoria);
    }

    public LiveData<Gasto> obtenerGastoPorId(long id) {
        return repositorio.obtenerGastoPorId(id);
    }

    public void guardarGasto(Gasto gasto, GastoRepositorio.OnGastoOperacionCompletada callback) {
        if (gasto.getId() == 0) {
            repositorio.insertar(gasto, callback);
        } else {
            repositorio.actualizar(gasto);
            if (callback != null) callback.alCompletar(gasto.getId());
        }
    }

    public void eliminarGasto(Gasto gasto) {
        repositorio.eliminar(gasto);
    }
}
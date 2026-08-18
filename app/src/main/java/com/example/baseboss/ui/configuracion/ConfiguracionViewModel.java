package com.example.baseboss.ui.configuracion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.entidades.Configuracion;
import com.example.baseboss.repositorio.ConfiguracionRepositorio;

public class ConfiguracionViewModel extends AndroidViewModel {

    private final ConfiguracionRepositorio repositorio;
    private final LiveData<Configuracion> configuracion;

    public ConfiguracionViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = new ConfiguracionRepositorio(application);
        this.configuracion = repositorio.obtenerConfiguracion();
    }

    public LiveData<Configuracion> getConfiguracion() {
        return configuracion;
    }

    public void guardarConfiguracion(Configuracion conf) {
        repositorio.guardarConfiguracion(conf);
    }
}
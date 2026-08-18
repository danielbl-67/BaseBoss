package com.example.baseboss.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.dao.ConfiguracionDao;
import com.example.baseboss.datos.entidades.Configuracion;

public class ConfiguracionRepositorio {

    private final ConfiguracionDao configuracionDao;
    private final LiveData<Configuracion> configuracion;

    public ConfiguracionRepositorio(Application aplicacion) {
        BaseBossDatabase db = BaseBossDatabase.obtenerInstancia(aplicacion);
        this.configuracionDao = db.configuracionDao();
        this.configuracion = configuracionDao.obtenerConfiguracion();
    }

    public LiveData<Configuracion> obtenerConfiguracion() {
        return configuracion;
    }

    public Configuracion obtenerConfiguracionSincrona() {
        return configuracionDao.obtenerConfiguracionSincrona();
    }

    public void guardarConfiguracion(Configuracion conf) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> configuracionDao.guardarConfiguracion(conf));
    }
}
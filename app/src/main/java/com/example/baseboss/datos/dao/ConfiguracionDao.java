package com.example.baseboss.datos.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baseboss.datos.entidades.Configuracion;

@Dao
public interface ConfiguracionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void guardarConfiguracion(Configuracion configuracion);

    @Update
    void actualizarConfiguracion(Configuracion configuracion);

    @Query("SELECT * FROM configuracion WHERE id = 1 LIMIT 1")
    LiveData<Configuracion> obtenerConfiguracion();

    @Query("SELECT * FROM configuracion WHERE id = 1 LIMIT 1")
    Configuracion obtenerConfiguracionSincrona();
}
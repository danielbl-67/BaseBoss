package com.example.baseboss.datos.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baseboss.datos.entidades.Gasto;

import java.util.Date;
import java.util.List;

@Dao
public interface GastoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Gasto gasto);

    @Update
    void actualizar(Gasto gasto);

    @Delete
    void eliminar(Gasto gasto);

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    LiveData<List<Gasto>> obtenerTodosLosGastos();

    @Query("SELECT * FROM gastos WHERE id = :id LIMIT 1")
    LiveData<Gasto> obtenerGastoPorId(long id);

    @Query("SELECT * FROM gastos WHERE categoria = :categoria ORDER BY fecha DESC")
    LiveData<List<Gasto>> obtenerGastosPorCategoria(String categoria);

    @Query("SELECT * FROM gastos WHERE descripcion LIKE '%' || :busqueda || '%' OR proveedor LIKE '%' || :busqueda || '%' ORDER BY fecha DESC")
    LiveData<List<Gasto>> buscarGastos(String busqueda);

    @Query("SELECT SUM(importe) FROM gastos WHERE fecha BETWEEN :inicio AND :fin")
    LiveData<Double> obtenerTotalGastosEnPeriodo(Date inicio, Date fin);

    @Query("SELECT SUM(importe) FROM gastos WHERE fecha BETWEEN :inicio AND :fin")
    Double obtenerTotalGastosEnPeriodoSincrono(Date inicio, Date fin);
}
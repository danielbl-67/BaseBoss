package com.example.baseboss.datos.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baseboss.datos.entidades.LineaFactura;

import java.util.List;

@Dao
public interface LineaFacturaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(LineaFactura linea);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarLineas(List<LineaFactura> lineas);

    @Update
    void actualizar(LineaFactura linea);

    @Delete
    void eliminar(LineaFactura linea);

    @Query("DELETE FROM lineas_factura WHERE factura_id = :facturaId")
    void eliminarPorFacturaId(long facturaId);

    @Query("SELECT * FROM lineas_factura WHERE factura_id = :facturaId")
    LiveData<List<LineaFactura>> obtenerLineasPorFactura(long facturaId);

    @Query("SELECT * FROM lineas_factura WHERE factura_id = :facturaId")
    List<LineaFactura> obtenerLineasPorFacturaSincrono(long facturaId);
}
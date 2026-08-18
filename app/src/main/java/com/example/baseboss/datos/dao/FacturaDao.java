package com.example.baseboss.datos.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.baseboss.datos.entidades.Factura;
import com.example.baseboss.datos.entidades.FacturaConDetalles;

import java.util.Date;
import java.util.List;

@Dao
public interface FacturaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertar(Factura factura);

    @Update
    void actualizar(Factura factura);

    @Delete
    void eliminar(Factura factura);

    @Transaction
    @Query("SELECT * FROM facturas ORDER BY fecha_emision DESC")
    LiveData<List<FacturaConDetalles>> obtenerTodasLasFacturasConDetalles();

    @Transaction
    @Query("SELECT * FROM facturas WHERE id = :id LIMIT 1")
    LiveData<FacturaConDetalles> obtenerFacturaPorId(long id);

    @Transaction
    @Query("SELECT * FROM facturas WHERE id = :id LIMIT 1")
    FacturaConDetalles obtenerFacturaPorIdSincrono(long id);

    @Transaction
    @Query("SELECT * FROM facturas WHERE estado = :estado ORDER BY fecha_emision DESC")
    LiveData<List<FacturaConDetalles>> obtenerFacturasPorEstado(String estado);

    @Transaction
    @Query("SELECT * FROM facturas WHERE cliente_id = :clienteId ORDER BY fecha_emision DESC")
    LiveData<List<FacturaConDetalles>> obtenerFacturasPorCliente(long clienteId);

    @Query("SELECT numero_factura FROM facturas WHERE numero_factura LIKE :prefijo || '%' ORDER BY id DESC LIMIT 1")
    String obtenerUltimoNumeroFactura(String prefijo);

    @Query("SELECT COUNT(*) FROM facturas WHERE estado = 'PENDIENTE'")
    LiveData<Integer> contarFacturasPendientes();

    @Query("SELECT COUNT(*) FROM facturas WHERE estado = 'VENCIDA'")
    LiveData<Integer> contarFacturasVencidas();

    @Query("SELECT SUM(total) FROM facturas WHERE estado = 'PAGADA' AND fecha_emision BETWEEN :inicio AND :fin")
    LiveData<Double> obtenerIngresosEnPeriodo(Date inicio, Date fin);

    @Query("SELECT SUM(total) FROM facturas WHERE estado = 'PAGADA' AND fecha_emision BETWEEN :inicio AND :fin")
    Double obtenerIngresosEnPeriodoSincrono(Date inicio, Date fin);
}
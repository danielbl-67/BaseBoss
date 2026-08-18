package com.example.baseboss.datos.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.ClienteConFacturas;

import java.util.List;

@Dao
public interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Cliente cliente);

    @Update
    void actualizar(Cliente cliente);

    @Delete
    void eliminar(Cliente cliente);

    @Query("SELECT * FROM clientes ORDER BY nombre_razon_social ASC")
    LiveData<List<Cliente>> obtenerTodosLosClientes();

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    LiveData<Cliente> obtenerClientePorId(long id);

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    Cliente obtenerClientePorIdSincrono(long id);

    @Query("SELECT * FROM clientes WHERE nombre_razon_social LIKE '%' || :busqueda || '%' OR nif_cif LIKE '%' || :busqueda || '%' ORDER BY nombre_razon_social ASC")
    LiveData<List<Cliente>> buscarClientes(String busqueda);

    @Query("SELECT COUNT(*) FROM clientes")
    LiveData<Integer> contarClientes();

    @Transaction
    @Query("SELECT * FROM clientes WHERE id = :clienteId")
    LiveData<ClienteConFacturas> obtenerClienteConFacturas(long clienteId);
}
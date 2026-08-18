package com.example.baseboss.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.baseboss.datos.basedatos.BaseBossDatabase;
import com.example.baseboss.datos.dao.ClienteDao;
import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.ClienteConFacturas;

import java.util.List;

/**
 * Repositorio que gestiona el acceso a los datos de Clientes abstrayendo la fuente de datos.
 */
public class ClienteRepositorio {

    private final ClienteDao clienteDao;
    private final LiveData<List<Cliente>> todosLosClientes;

    public ClienteRepositorio(Application aplicacion) {
        BaseBossDatabase db = BaseBossDatabase.obtenerInstancia(aplicacion);
        this.clienteDao = db.clienteDao();
        this.todosLosClientes = clienteDao.obtenerTodosLosClientes();
    }

    public LiveData<List<Cliente>> obtenerTodosLosClientes() {
        return todosLosClientes;
    }

    public LiveData<Cliente> obtenerClientePorId(long id) {
        return clienteDao.obtenerClientePorId(id);
    }

    public LiveData<List<Cliente>> buscarClientes(String busqueda) {
        return clienteDao.buscarClientes(busqueda);
    }

    public LiveData<ClienteConFacturas> obtenerClienteConFacturas(long clienteId) {
        return clienteDao.obtenerClienteConFacturas(clienteId);
    }

    public void insertar(Cliente cliente, OnOperacionCompletada callback) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> {
            long idGenerado = clienteDao.insertar(cliente);
            if (callback != null) {
                callback.alCompletar(idGenerado);
            }
        });
    }

    public void actualizar(Cliente cliente) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> clienteDao.actualizar(cliente));
    }

    public void eliminar(Cliente cliente) {
        BaseBossDatabase.ejecutorEscritura.execute(() -> clienteDao.eliminar(cliente));
    }

    public interface OnOperacionCompletada {
        void alCompletar(long id);
    }
}
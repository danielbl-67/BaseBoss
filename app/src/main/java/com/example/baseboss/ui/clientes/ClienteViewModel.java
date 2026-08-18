package com.example.baseboss.ui.clientes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.ClienteConFacturas;
import com.example.baseboss.repositorio.ClienteRepositorio;

import java.util.List;

/**
 * ViewModel que expone el estado de los clientes y gestiona las operaciones de UI.
 */
public class ClienteViewModel extends AndroidViewModel {

    private final ClienteRepositorio repositorio;
    private final MutableLiveData<String> filtroBusqueda = new MutableLiveData<>("");
    private final LiveData<List<Cliente>> clientesFiltrados;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = new ClienteRepositorio(application);

        // Búsqueda reactiva mediante transformaciones de LiveData
        this.clientesFiltrados = Transformations.switchMap(filtroBusqueda, busqueda -> {
            if (busqueda == null || busqueda.trim().isEmpty()) {
                return repositorio.obtenerTodosLosClientes();
            } else {
                return repositorio.buscarClientes(busqueda.trim());
            }
        });
    }

    public LiveData<List<Cliente>> getClientes() {
        return clientesFiltrados;
    }

    public void establecerFiltroBusqueda(String texto) {
        filtroBusqueda.setValue(texto);
    }

    public LiveData<Cliente> obtenerClientePorId(long id) {
        return repositorio.obtenerClientePorId(id);
    }

    public LiveData<ClienteConFacturas> obtenerClienteConFacturas(long clienteId) {
        return repositorio.obtenerClienteConFacturas(clienteId);
    }

    public void guardarCliente(Cliente cliente, ClienteRepositorio.OnOperacionCompletada callback) {
        if (cliente.getId() == 0) {
            repositorio.insertar(cliente, callback);
        } else {
            repositorio.actualizar(cliente);
            if (callback != null) callback.alCompletar(cliente.getId());
        }
    }

    public void eliminarCliente(Cliente cliente) {
        repositorio.eliminar(cliente);
    }
}
package com.example.baseboss.datos.basedatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.baseboss.datos.dao.ClienteDao;
import com.example.baseboss.datos.dao.ConfiguracionDao;
import com.example.baseboss.datos.dao.FacturaDao;
import com.example.baseboss.datos.dao.GastoDao;
import com.example.baseboss.datos.dao.LineaFacturaDao;
import com.example.baseboss.datos.entidades.Cliente;
import com.example.baseboss.datos.entidades.Configuracion;
import com.example.baseboss.datos.entidades.Factura;
import com.example.baseboss.datos.entidades.Gasto;
import com.example.baseboss.datos.entidades.LineaFactura;
import com.example.baseboss.utilidades.ConversorFecha;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base de datos principal de Room con patrón Singleton y Executor para operaciones en segundo plano.
 */
@Database(
        entities = {
                Cliente.class,
                Factura.class,
                LineaFactura.class,
                Gasto.class,
                Configuracion.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({ConversorFecha.class})
public abstract class BaseBossDatabase extends RoomDatabase {

    public abstract ClienteDao clienteDao();
    public abstract FacturaDao facturaDao();
    public abstract LineaFacturaDao lineaFacturaDao();
    public abstract GastoDao gastoDao();
    public abstract ConfiguracionDao configuracionDao();

    private static volatile BaseBossDatabase INSTANCIA;
    private static final int NUMERO_DE_HILOS = 4;
    public static final ExecutorService ejecutorEscritura = Executors.newFixedThreadPool(NUMERO_DE_HILOS);

    public static BaseBossDatabase obtenerInstancia(final Context contexto) {
        if (INSTANCIA == null) {
            synchronized (BaseBossDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(
                                    contexto.getApplicationContext(),
                                    BaseBossDatabase.class,
                                    "baseboss_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}
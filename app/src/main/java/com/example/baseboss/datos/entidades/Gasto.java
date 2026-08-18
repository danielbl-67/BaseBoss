package com.example.baseboss.datos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entidad que registra los egresos y compras del profesional.
 */
@Entity(tableName = "gastos")
public class Gasto {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "fecha")
    private Date fecha;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "importe")
    private double importe;

    @ColumnInfo(name = "porcentaje_iva")
    private double porcentajeIva;

    @ColumnInfo(name = "proveedor")
    private String proveedor;

    @ColumnInfo(name = "notas")
    private String notas;

    @ColumnInfo(name = "ruta_documento")
    private String rutaDocumento;

    public Gasto(Date fecha, String categoria, String descripcion, double importe,
                 double porcentajeIva, String proveedor, String notas, String rutaDocumento) {
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.importe = importe;
        this.porcentajeIva = porcentajeIva;
        this.proveedor = proveedor;
        this.notas = notas;
        this.rutaDocumento = rutaDocumento;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public double getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(double porcentajeIva) { this.porcentajeIva = porcentajeIva; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getRutaDocumento() { return rutaDocumento; }
    public void setRutaDocumento(String rutaDocumento) { this.rutaDocumento = rutaDocumento; }
}
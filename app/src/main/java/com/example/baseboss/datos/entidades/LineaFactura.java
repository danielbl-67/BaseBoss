package com.example.baseboss.datos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad que almacena cada elemento o servicio individual dentro de una factura.
 */
@Entity(
        tableName = "lineas_factura",
        foreignKeys = @ForeignKey(
                entity = Factura.class,
                parentColumns = "id",
                childColumns = "factura_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("factura_id")}
)
public class LineaFactura {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "factura_id")
    private long facturaId;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "cantidad")
    private double cantidad;

    @ColumnInfo(name = "precio_unitario")
    private double precioUnitario;

    @ColumnInfo(name = "porcentaje_iva")
    private double porcentajeIva;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    public LineaFactura(long facturaId, String descripcion, double cantidad,
                        double precioUnitario, double porcentajeIva, double subtotal) {
        this.facturaId = facturaId;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.porcentajeIva = porcentajeIva;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getFacturaId() { return facturaId; }
    public void setFacturaId(long facturaId) { this.facturaId = facturaId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(double porcentajeIva) { this.porcentajeIva = porcentajeIva; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
package com.example.baseboss.datos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entidad principal de facturas vinculada al cliente emisor.
 */
@Entity(
        tableName = "facturas",
        foreignKeys = @ForeignKey(
                entity = Cliente.class,
                parentColumns = "id",
                childColumns = "cliente_id",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = {@Index(value = "cliente_id"), @Index(value = "numero_factura", unique = true)}
)
public class Factura {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "numero_factura")
    private String numeroFactura;

    @ColumnInfo(name = "cliente_id")
    private long clienteId;

    @ColumnInfo(name = "fecha_emision")
    private Date fechaEmision;

    @ColumnInfo(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    @ColumnInfo(name = "estado")
    private String estado; // BORRADOR, PENDIENTE, PAGADA, VENCIDA, ANULADA

    @ColumnInfo(name = "base_imponible")
    private double baseImponible;

    @ColumnInfo(name = "total_iva")
    private double totalIva;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "notas")
    private String notas;

    public Factura(String numeroFactura, long clienteId, Date fechaEmision, Date fechaVencimiento,
                   String estado, double baseImponible, double totalIva, double total, String notas) {
        this.numeroFactura = numeroFactura;
        this.clienteId = clienteId;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.baseImponible = baseImponible;
        this.totalIva = totalIva;
        this.total = total;
        this.notas = notas;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public long getClienteId() { return clienteId; }
    public void setClienteId(long clienteId) { this.clienteId = clienteId; }

    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getBaseImponible() { return baseImponible; }
    public void setBaseImponible(double baseImponible) { this.baseImponible = baseImponible; }

    public double getTotalIva() { return totalIva; }
    public void setTotalIva(double totalIva) { this.totalIva = totalIva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
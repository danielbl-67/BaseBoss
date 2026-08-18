package com.example.baseboss.datos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa a un cliente en la base de datos.
 */
@Entity(tableName = "clientes")
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nombre_razon_social")
    private String nombreRazonSocial;

    @ColumnInfo(name = "nif_cif")
    private String nifCif;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "direccion")
    private String direccion;

    @ColumnInfo(name = "codigo_postal")
    private String codigoPostal;

    @ColumnInfo(name = "ciudad")
    private String ciudad;

    @ColumnInfo(name = "notas")
    private String notas;

    public Cliente(String nombreRazonSocial, String nifCif, String email, String telefono,
                   String direccion, String codigoPostal, String ciudad, String notas) {
        this.nombreRazonSocial = nombreRazonSocial;
        this.nifCif = nifCif;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.notas = notas;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombreRazonSocial() { return nombreRazonSocial; }
    public void setNombreRazonSocial(String nombreRazonSocial) { this.nombreRazonSocial = nombreRazonSocial; }

    public String getNifCif() { return nifCif; }
    public void setNifCif(String nifCif) { this.nifCif = nifCif; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
package com.example.baseboss.datos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que almacena los datos de perfil y facturación del usuario autónomo.
 */
@Entity(tableName = "configuracion")
public class Configuracion {

    @PrimaryKey
    private int id = 1; // Registro único para la configuración global

    @ColumnInfo(name = "nombre_fiscal")
    private String nombreFiscal;

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

    @ColumnInfo(name = "iban")
    private String iban;

    @ColumnInfo(name = "ruta_logo")
    private String rutaLogo;

    public Configuracion(String nombreFiscal, String nifCif, String email, String telefono,
                         String direccion, String codigoPostal, String ciudad, String iban, String rutaLogo) {
        this.nombreFiscal = nombreFiscal;
        this.nifCif = nifCif;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.iban = iban;
        this.rutaLogo = rutaLogo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreFiscal() { return nombreFiscal; }
    public void setNombreFiscal(String nombreFiscal) { this.nombreFiscal = nombreFiscal; }

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

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getRutaLogo() { return rutaLogo; }
    public void setRutaLogo(String rutaLogo) { this.rutaLogo = rutaLogo; }
}
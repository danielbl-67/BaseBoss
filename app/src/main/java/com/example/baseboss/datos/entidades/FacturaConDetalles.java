package com.example.baseboss.datos.entidades;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

/**
 * Estructura relacional que contiene una factura junto a su cliente y sus líneas asociadas.
 */
public class FacturaConDetalles {

    @Embedded
    public Factura factura;

    @Relation(
            parentColumn = "cliente_id",
            entityColumn = "id"
    )
    public Cliente cliente;

    @Relation(
            parentColumn = "id",
            entityColumn = "factura_id"
    )
    public List<LineaFactura> lineas;
}
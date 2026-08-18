package com.example.baseboss.datos.entidades;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

/**
 * Estructura relacional que asocia un cliente con todo su histórico de facturas.
 */
public class ClienteConFacturas {

    @Embedded
    public Cliente cliente;

    @Relation(
            parentColumn = "id",
            entityColumn = "cliente_id"
    )
    public List<Factura> facturas;
}
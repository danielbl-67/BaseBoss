package com.example.baseboss.utilidades;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase utilitaria para estandarizar formatos de moneda y fecha en toda la app.
 */
public class FormateadorMoneda {

    private static final Locale LOCALE_ES = new Locale("es", "ES");
    private static final NumberFormat FORMATO_MONEDA = NumberFormat.getCurrencyInstance(LOCALE_ES);
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy", LOCALE_ES);

    public static String formatear(double importe) {
        return FORMATO_MONEDA.format(importe);
    }

    public static String formatearFecha(Date fecha) {
        return fecha != null ? FORMATO_FECHA.format(fecha) : "-";
    }
}
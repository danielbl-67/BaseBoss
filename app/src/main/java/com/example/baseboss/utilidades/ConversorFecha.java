package com.example.baseboss.utilidades;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Conversor de tipos para permitir almacenar objetos Date en Room como Long (milisegundos).
 */
public class ConversorFecha {

    @TypeConverter
    public static Date aFecha(Long valorMilisegundos) {
        return valorMilisegundos == null ? null : new Date(valorMilisegundos);
    }

    @TypeConverter
    public static Long aMilisegundos(Date fecha) {
        return fecha == null ? null : fecha.getTime();
    }
}
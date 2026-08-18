package com.example.baseboss.utilidades;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para validaciones de formularios en toda la aplicación.
 */
public class Validador {

    private static final Pattern PATRON_NIF_NIE_CIF = Pattern.compile("^[A-HJ-NP-SU-WXYZ0-9][0-9]{7}[A-Z0-9]$", Pattern.CASE_INSENSITIVE);

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esEmailValido(String email) {
        if (TextUtils.isEmpty(email)) {
            return true; // El email puede ser opcional según el campo
        }
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean esDocumentoFiscalValido(String nifCif) {
        if (TextUtils.isEmpty(nifCif)) {
            return false;
        }
        return PATRON_NIF_NIE_CIF.matcher(nifCif.trim()).matches();
    }
}
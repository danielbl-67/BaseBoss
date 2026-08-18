package com.example.baseboss.utilidades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.baseboss.datos.entidades.Configuracion;
import com.example.baseboss.datos.entidades.FacturaConDetalles;
import com.example.baseboss.datos.entidades.LineaFactura;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Generador nativo de facturas en PDF utilizando android.graphics.pdf.PdfDocument.
 */
public class GeneradorPdfFactura {

    private static final int ANCHO_PAGINA = 595; // A4 estándar a 72 DPI
    private static final int ALTO_PAGINA = 842;

    public static File generarPdf(Context contexto, FacturaConDetalles facturaDetalle, Configuracion config) throws Exception {
        File directorioFacturas = new File(contexto.getCacheDir(), "facturas");
        if (!directorioFacturas.exists()) {
            directorioFacturas.mkdirs();
        }

        String nombreArchivo = "Factura_" + facturaDetalle.factura.getNumeroFactura() + ".pdf";
        File archivoPdf = new File(directorioFacturas, nombreArchivo);

        PdfDocument documentoPdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(ANCHO_PAGINA, ALTO_PAGINA, 1).create();
        PdfDocument.Page pagina = documentoPdf.startPage(pageInfo);
        Canvas canvas = pagina.getCanvas();

        Paint pincel = new Paint();
        pincel.setAntiAlias(true);

        int margenIzquierdo = 40;
        int margenDerecho = ANCHO_PAGINA - 40;
        int y = 50;

        // 1. Cabecera Emisor
        String nombreEmisor = config != null && config.getNombreFiscal() != null ? config.getNombreFiscal() : "BaseBoss Autónomo";
        String nifEmisor = config != null && config.getNifCif() != null ? "NIF/CIF: " + config.getNifCif() : "NIF/CIF: -";

        pincel.setColor(Color.rgb(30, 41, 59));
        pincel.setTextSize(14f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(nombreEmisor, margenIzquierdo, y, pincel);

        // Título FACTURA a la derecha
        pincel.setColor(Color.rgb(37, 99, 235));
        pincel.setTextSize(20f);
        pincel.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("FACTURA", margenDerecho, y, pincel);

        y += 18;
        pincel.setTextAlign(Paint.Align.LEFT);
        pincel.setColor(Color.rgb(100, 116, 139));
        pincel.setTextSize(9f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(nifEmisor, margenIzquierdo, y, pincel);

        // Número de Factura
        pincel.setTextAlign(Paint.Align.RIGHT);
        pincel.setColor(Color.rgb(30, 41, 59));
        pincel.setTextSize(10f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Nº: " + facturaDetalle.factura.getNumeroFactura(), margenDerecho, y, pincel);

        y += 14;
        pincel.setTextAlign(Paint.Align.LEFT);
        pincel.setColor(Color.rgb(100, 116, 139));
        pincel.setTextSize(9f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        if (config != null && config.getEmail() != null) {
            canvas.drawText(config.getEmail(), margenIzquierdo, y, pincel);
        }

        pincel.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Fecha: " + FormateadorMoneda.formatearFecha(facturaDetalle.factura.getFechaEmision()), margenDerecho, y, pincel);

        y += 14;
        canvas.drawText("Vencimiento: " + FormateadorMoneda.formatearFecha(facturaDetalle.factura.getFechaVencimiento()), margenDerecho, y, pincel);

        // 2. Bloque Cliente Receptor
        y += 30;
        pincel.setColor(Color.rgb(241, 245, 249));
        canvas.drawRoundRect(margenIzquierdo, y, margenDerecho, y + 60, 8, 8, pincel);

        pincel.setTextAlign(Paint.Align.LEFT);
        pincel.setColor(Color.rgb(37, 99, 235));
        pincel.setTextSize(9f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("FACTURAR A:", margenIzquierdo + 12, y + 18, pincel);

        if (facturaDetalle.cliente != null) {
            pincel.setColor(Color.rgb(30, 41, 59));
            pincel.setTextSize(10f);
            canvas.drawText(facturaDetalle.cliente.getNombreRazonSocial(), margenIzquierdo + 12, y + 34, pincel);

            pincel.setColor(Color.rgb(100, 116, 139));
            pincel.setTextSize(9f);
            pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("NIF/CIF: " + facturaDetalle.cliente.getNifCif(), margenIzquierdo + 12, y + 48, pincel);
        }

        // 3. Tabla de Líneas
        y += 85;
        // Cabecera tabla
        pincel.setColor(Color.rgb(30, 41, 59));
        canvas.drawRect(margenIzquierdo, y, margenDerecho, y + 24, pincel);

        pincel.setColor(Color.WHITE);
        pincel.setTextSize(9f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Descripción", margenIzquierdo + 8, y + 16, pincel);

        pincel.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Cant.", margenDerecho - 200, y + 16, pincel);
        canvas.drawText("Precio", margenDerecho - 130, y + 16, pincel);
        canvas.drawText("IVA", margenDerecho - 80, y + 16, pincel);
        canvas.drawText("Total", margenDerecho - 8, y + 16, pincel);

        y += 24;
        pincel.setColor(Color.rgb(30, 41, 59));
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        if (facturaDetalle.lineas != null) {
            for (LineaFactura linea : facturaDetalle.lineas) {
                y += 20;
                pincel.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(linea.getDescripcion(), margenIzquierdo + 8, y, pincel);

                pincel.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.format(java.util.Locale.getDefault(), "%.1f", linea.getCantidad()), margenDerecho - 200, y, pincel);
                canvas.drawText(FormateadorMoneda.formatear(linea.getPrecioUnitario()), margenDerecho - 130, y, pincel);
                canvas.drawText(String.format(java.util.Locale.getDefault(), "%.0f%%", linea.getPorcentajeIva()), margenDerecho - 80, y, pincel);
                canvas.drawText(FormateadorMoneda.formatear(linea.getSubtotal()), margenDerecho - 8, y, pincel);

                // Línea separadora
                Paint lineaBorde = new Paint();
                lineaBorde.setColor(Color.rgb(226, 232, 240));
                lineaBorde.setStrokeWidth(1f);
                canvas.drawLine(margenIzquierdo, y + 6, margenDerecho, y + 6, lineaBorde);
            }
        }

        // 4. Totales
        y += 40;
        pincel.setTextAlign(Paint.Align.RIGHT);
        pincel.setColor(Color.rgb(100, 116, 139));
        canvas.drawText("Base Imponible: " + FormateadorMoneda.formatear(facturaDetalle.factura.getBaseImponible()), margenDerecho - 8, y, pincel);

        y += 16;
        canvas.drawText("Total IVA: " + FormateadorMoneda.formatear(facturaDetalle.factura.getTotalIva()), margenDerecho - 8, y, pincel);

        y += 20;
        pincel.setColor(Color.rgb(37, 99, 235));
        pincel.setTextSize(13f);
        pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("TOTAL: " + FormateadorMoneda.formatear(facturaDetalle.factura.getTotal()), margenDerecho - 8, y, pincel);

        // 5. Pie de página con IBAN
        if (config != null && config.getIban() != null && !config.getIban().trim().isEmpty()) {
            pincel.setTextAlign(Paint.Align.LEFT);
            pincel.setColor(Color.rgb(100, 116, 139));
            pincel.setTextSize(9f);
            pincel.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("IBAN para transferencias: " + config.getIban(), margenIzquierdo, ALTO_PAGINA - 40, pincel);
        }

        documentoPdf.finishPage(pagina);

        FileOutputStream fos = new FileOutputStream(archivoPdf);
        documentoPdf.writeTo(fos);
        documentoPdf.close();
        fos.close();

        return archivoPdf;
    }

    public static void compartirPdf(Context contexto, File archivoPdf) {
        Uri uri = FileProvider.getUriForFile(
                contexto,
                contexto.getPackageName() + ".fileprovider",
                archivoPdf
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        contexto.startActivity(Intent.createChooser(intent, "Compartir Factura"));
    }
}
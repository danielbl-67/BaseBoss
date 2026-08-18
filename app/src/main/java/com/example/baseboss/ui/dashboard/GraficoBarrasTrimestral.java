package com.example.baseboss.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.baseboss.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Vista personalizada para renderizar un gráfico de barras comparativo trimestral 100% nativo.
 */
public class GraficoBarrasTrimestral extends View {

    private final Paint pincelIngresos = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelGastos = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelTexto = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelEjes = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float[][] datos = new float[3][2]; // 3 meses: [Ingresos, Gastos]
    private final String[] nombresMeses = new String[3];

    public GraficoBarrasTrimestral(Context context) {
        super(context);
        inicializar();
    }

    public GraficoBarrasTrimestral(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    private void inicializar() {
        pincelIngresos.setColor(ContextCompat.getColor(getContext(), R.color.estado_pagada));
        pincelGastos.setColor(ContextCompat.getColor(getContext(), R.color.estado_vencida));

        pincelTexto.setColor(Color.rgb(100, 116, 139));
        pincelTexto.setTextSize(32f);
        pincelTexto.setTextAlign(Paint.Align.CENTER);

        pincelEjes.setColor(Color.rgb(226, 232, 240));
        pincelEjes.setStrokeWidth(3f);

        Calendar cal = Calendar.getInstance();
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));
        for (int i = 2; i >= 0; i--) {
            Calendar temp = (Calendar) cal.clone();
            temp.add(Calendar.MONTH, -(2 - i));
            nombresMeses[i] = dfs.getShortMonths()[temp.get(Calendar.MONTH)].toUpperCase();
        }
    }

    public void setDatos(float[][] datos) {
        if (datos != null) {
            this.datos = datos;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ancho = getWidth();
        int alto = getHeight();

        float margenInferior = 60f;
        float margenSuperior = 40f;
        float altoUtil = alto - margenInferior - margenSuperior;

        // Calcular valor máximo para escalar las barras
        float maxValor = 100f;
        for (int i = 0; i < 3; i++) {
            if (datos[i][0] > maxValor) maxValor = datos[i][0];
            if (datos[i][1] > maxValor) maxValor = datos[i][1];
        }

        // Línea base horizontal
        canvas.drawLine(40f, alto - margenInferior, ancho - 40f, alto - margenInferior, pincelEjes);

        float anchoGrupo = (ancho - 80f) / 3f;
        float anchoBarra = anchoGrupo * 0.28f;

        for (int i = 0; i < 3; i++) {
            float centroX = 40f + (i * anchoGrupo) + (anchoGrupo / 2f);

            // Barra Ingresos
            float altoIngreso = (datos[i][0] / maxValor) * altoUtil;
            RectF rectIngreso = new RectF(
                    centroX - anchoBarra - 4f,
                    (alto - margenInferior) - altoIngreso,
                    centroX - 4f,
                    alto - margenInferior
            );
            canvas.drawRoundRect(rectIngreso, 8f, 8f, pincelIngresos);

            // Barra Gastos
            float altoGasto = (datos[i][1] / maxValor) * altoUtil;
            RectF rectGasto = new RectF(
                    centroX + 4f,
                    (alto - margenInferior) - altoGasto,
                    centroX + anchoBarra + 4f,
                    alto - margenInferior
            );
            canvas.drawRoundRect(rectGasto, 8f, 8f, pincelGastos);

            // Etiqueta del Mes
            canvas.drawText(nombresMeses[i], centroX, alto - 15f, pincelTexto);
        }
    }
}
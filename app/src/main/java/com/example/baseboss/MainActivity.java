package com.example.baseboss;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.baseboss.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_contenedor_principal);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.barraNavegacionInferior, navController);

            // Ocultar barra inferior en pantallas de edición y detalle
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.destino_crear_editar_cliente ||
                        id == R.id.destino_detalle_cliente ||
                        id == R.id.destino_crear_factura ||
                        id == R.id.destino_detalle_factura ||
                        id == R.id.destino_crear_editar_gasto) {
                    binding.barraNavegacionInferior.setVisibility(View.GONE);
                } else {
                    binding.barraNavegacionInferior.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
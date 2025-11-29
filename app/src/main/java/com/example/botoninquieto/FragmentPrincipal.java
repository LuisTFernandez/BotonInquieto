package com.example.botoninquieto;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentPrincipal extends Fragment {

    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        Button botonRandom = view.findViewById(R.id.buttonColorRandom);
        botonRandom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int r = obtenerAleatorio(0, 255);
               int g = obtenerAleatorio(0,255);
               int b = obtenerAleatorio(0,255);
               botonRandom.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(r,g,b)));
               EditText etHex = view.findViewById(R.id.editTextHex);
               etHex.setText(String.format("#%02x%02x%02x", r, g, b));
           }
       });


        Button botonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        botonCerrarSesion.setOnClickListener(v -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentHome));

        Button botonAjustes = view.findViewById(R.id.buttonAjustes);
        botonAjustes.setOnClickListener(v -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentAjustes));
    }

    protected static int obtenerAleatorio(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
package com.example.botoninquieto;

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

public class FragmentLogin extends Fragment {

    public FragmentLogin() {}

    NavController navController;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button botonLogin = view.findViewById(R.id.buttonLogin);
        EditText correo = view.findViewById(R.id.editTextTextEmailAddress);
        EditText contrasena = view.findViewById(R.id.editTextTextPassword);
        navController = Navigation.findNavController(view);
        botonLogin.setOnClickListener(v -> {
            if (correo.getText().toString().equals("alumno") && contrasena.getText().toString().equals("alumno")){
                navController.navigate(R.id.action_fragmentLogin_to_fragmentPrincipal);
            } else {
                new DialogErrorLogin().show(requireActivity().getSupportFragmentManager(),"Error");
                correo.setText("");
                contrasena.setText("");
            }
        });


    }
}
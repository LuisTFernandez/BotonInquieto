package com.example.botoninquieto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentCrearContacto extends Fragment {
    public FragmentCrearContacto() {
    }
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_contacto, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        Button botonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        botonCerrarSesion.setOnClickListener(v -> navController.navigate(R.id.action_fragmentCrearContacto_to_fragmentHome));

        Button botonGuardar = view.findViewById(R.id.buttonModificar);
        botonGuardar.setOnClickListener(v -> guardar(view));
    }

    public void guardar(View view) {
        EditText etNombre = (EditText) view.findViewById(R.id.editTextNombre);
        EditText etCorreo = (EditText) view.findViewById(R.id.editTextCorreo);
        EditText etTlf = (EditText) view.findViewById(R.id.editTextTlf);

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, etNombre.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.EMAIL, etCorreo.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, etTlf.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

        startActivity(intent);
    }
}
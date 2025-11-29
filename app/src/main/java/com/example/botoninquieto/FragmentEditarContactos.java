package com.example.botoninquieto;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentEditarContactos extends Fragment {
    NavController navController;
    private String selectedContactId = null;
    private EditText etNombre;
    private EditText etCorreo;
    private EditText etTlf;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    ActivityResultLauncher<Intent> contactPickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_contactos, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        Button botonBuscar = view.findViewById(R.id.buttonBuscarContacto);
        Button botonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        Button botonModificar = view.findViewById(R.id.buttonModificar);

        etNombre = (EditText) view.findViewById(R.id.editTextNombre);
        etCorreo = (EditText) view.findViewById(R.id.editTextCorreo);
        etTlf = (EditText) view.findViewById(R.id.editTextTlf);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
            if (granted) {
                Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Permiso DENEGADO. No se pueden leer contactos", Toast.LENGTH_LONG).show();
            }
        });

        //Declaro el selector de contactos para poder obtener los datos de uno.
        contactPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri contactUri = result.getData().getData();
                getContactData(contactUri);
            } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        //Al tocar el botón se muestra el selector
        botonBuscar.setOnClickListener(v ->abrirSelectorContactos());
        botonCerrarSesion.setOnClickListener(v -> navController.navigate(R.id.action_fragmentEditarContactos_to_fragmentHome));
        botonModificar.setOnClickListener(v -> modificar(view));
    }

    private void pedirPermisoContactos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS);
        }
    }
    private void abrirSelectorContactos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {

            Intent selector = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            contactPickerLauncher.launch(selector);

        } else {
            pedirPermisoContactos();
        }
    }

    private void getContactData(Uri contactUri) {
        /*ContentResolver es la plataforma para escribir datos entre aplicaciones usando el ContentProvider.
        Lo utilizo junto con un cursor , similar al prepared statement para poder modificar datos.*/


        ContentResolver resolver = requireContext().getContentResolver();
        String idContacto = null;
        String nombre = null;

        try (Cursor cursor = resolver.query(contactUri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                idContacto = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                selectedContactId = idContacto;
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            }
        }

        String tlf = null;
        try (Cursor tlfCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{idContacto},
                null)) {
            if (tlfCursor != null && tlfCursor.moveToFirst()) {
                tlf = tlfCursor.getString(tlfCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }

        String correo = null;
        try (Cursor correoCursor = resolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{idContacto},
                null)) {

                if (correoCursor != null && correoCursor.moveToFirst()) {
                    correo = correoCursor.getString(correoCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS));
                }
        }
        //Ifs ternarios para determinar si rellenar o no los EditText.
        etNombre.setText(nombre != null ? nombre : "");
        etTlf.setText(tlf != null ? tlf : "");
        etCorreo.setText(correo != null ? correo : "");

        Toast.makeText(requireContext(), "Contacto cargado", Toast.LENGTH_SHORT).show();

    }
    /*La edición del contacto la realizará manualmente el usuario dentro del selector de contactos de Android
    * Facilitándonos a nosotros los problemas de permisos*/
    public void modificar(View view) {
        if (selectedContactId == null) {
            Toast.makeText(requireContext(), "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            return;
        }
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(selectedContactId));

            etNombre = (EditText) view.findViewById(R.id.editTextNombre);
            etCorreo = (EditText) view.findViewById(R.id.editTextCorreo);
            etTlf = (EditText) view.findViewById(R.id.editTextTlf);

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(contactUri);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, etNombre.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.EMAIL, etCorreo.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, etTlf.getText().toString())
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            intent.putExtra("finishActivityOnSaveCompleted", true);

            startActivity(intent);

    }
}
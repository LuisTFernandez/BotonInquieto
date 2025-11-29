package com.example.botoninquieto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DialogErrorLogin extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder buildAlert = new AlertDialog.Builder(getActivity());

        buildAlert.setMessage("Usuario o contraseña incorrectos. Compruebe sus creedenciales.").setTitle("Inicio de sesión fallido").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Denegar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return buildAlert.create();
    }

}
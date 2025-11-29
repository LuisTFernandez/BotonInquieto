package com.example.botoninquieto;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.security.Principal;

public class FragmentAjustes extends Fragment {

    public FragmentAjustes(){}
    private ActivityResultLauncher<String> requestPermissionLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController= Navigation.findNavController(view);
        //Lógica inversión.
        SharedPreferences prefs = requireContext().getSharedPreferences("app_settings", MODE_PRIVATE);
        Switch invertirColores = view.findViewById(R.id.switchInvertirColores);
        boolean dark = prefs.getBoolean("dark_mode", false);
        invertirColores.setChecked(dark);
        invertirColores.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                prefs.edit().putBoolean("dark_mode", true).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                prefs.edit().putBoolean("dark_mode", false).apply();
            }
        });

        //Botones de navegación
        Button botonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        botonCerrarSesion.setOnClickListener(v -> navController.navigate(R.id.action_fragmentAjustes_to_fragmentHome));

        Button botonContactos = view.findViewById(R.id.buttonContactos);
        botonContactos.setOnClickListener(v -> navController.navigate(R.id.action_fragmentAjustes_to_fragmentEditarContactos));

        Button botonNuevoContacto = view.findViewById(R.id.buttonNuevoContacto);
        botonNuevoContacto.setOnClickListener(v -> navController.navigate(R.id.action_fragmentAjustes_to_fragmentCrearContacto));
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
            if (o) {
                Toast.makeText(requireContext(),"Permiso concedido \uD83D\uDC4D",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),"Permiso denegado \uD83D\uDC4E",Toast.LENGTH_SHORT).show();
            }
        });

        //Lógica notificaciones
        Switch botonNotificaciones = view.findViewById(R.id.switchActivarNotificaciones);

        botonNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notificacionEjemplo(view);
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        });
    }

    public void notificacionEjemplo(View view){
        Context context = requireContext();
        Intent i = new Intent(context, Principal.class);
        PendingIntent pi = PendingIntent.getActivity(context,0, i,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "notify_001", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(),"default");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Notificaciones activadas");
        mBuilder.setContentText("A partir de ahora recibirá notificaciones de la aplicación.");
        mBuilder.setContentIntent(pi);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(10, mBuilder.build());
    }
}
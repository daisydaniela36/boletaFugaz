package com.example.boletafugaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.boletafugaz.CambiarContrasenaActivity;
import com.example.boletafugaz.ListaGiroActivity;
import com.example.boletafugaz.MostrarGiroActivity;
import com.example.boletafugaz.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CuentaFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    EditText edt_Nombre,edt_Direccion,edt_Telefono,edt_Correo;
    Button btn_Editar,btn_cambiarContraseña;
    String id,nombre,direccion,telefono,correo,contraseña;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cuenta, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        edt_Nombre = root.findViewById(R.id.edt_Nombre);
        edt_Direccion = root.findViewById(R.id.edt_Direccion);
        edt_Telefono = root.findViewById(R.id.edt_Telefono);
        edt_Correo = root.findViewById(R.id.edt_Correo);

        btn_Editar = root.findViewById(R.id.btn_editar);
        btn_cambiarContraseña = root.findViewById(R.id.btn_cambiarContraseña);

        id = firebaseAuth.getCurrentUser().getUid();


        getUser();

        btn_Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edt_Nombre.getText().toString();
                String direccion = edt_Direccion.getText().toString();
                String telefono = edt_Telefono.getText().toString();
                String correo = edt_Correo.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("nombre", nombre);
                map.put("direccion", direccion);
                map.put("telefono", telefono);
                map.put("correo", correo);

                mDataBase.child("usuario").child(id).updateChildren(map);

                Toast.makeText(getActivity(), "Se ah editado correctamente", Toast.LENGTH_SHORT).show();

            }
        });

        btn_cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CambiarContrasenaActivity.class);

                i.putExtra("id", id);
                i.putExtra("nombre", nombre);
                i.putExtra("direccion", direccion);
                i.putExtra("telefono", telefono);
                i.putExtra("contraseña", contraseña);

                startActivity(i);


            }
        });

        return root;
    }

    private void getUser() {

        mDataBase.child("usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    nombre = snapshot.child("nombre").getValue().toString();
                    direccion = snapshot.child("direccion").getValue().toString();
                    telefono = snapshot.child("telefono").getValue().toString();
                    correo = snapshot.child("correo").getValue().toString();
                    contraseña = snapshot.child("contraseña").getValue().toString();

                    edt_Nombre.setText(nombre);
                    edt_Direccion.setText(direccion);
                    edt_Telefono.setText(telefono);
                    edt_Correo.setText(correo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
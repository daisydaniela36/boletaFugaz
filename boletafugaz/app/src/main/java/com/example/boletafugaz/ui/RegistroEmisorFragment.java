package com.example.boletafugaz.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroEmisorFragment extends Fragment {

    private EditText edt_rut, edt_nombre,edt_comuna, edt_direccion, edt_telefono;
    private Button btn_registrar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference bdEmpresa;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registro_emisor, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        String id = firebaseAuth.getCurrentUser().getUid();

        bdEmpresa = FirebaseDatabase.getInstance().getReference("usuario").child(id).child("empresa");


        edt_rut = root.findViewById(R.id.edt_rut);
        edt_nombre = root.findViewById(R.id.edt_nombre);
        edt_comuna = root.findViewById(R.id.edt_comuna);
        edt_direccion = root.findViewById(R.id.edt_direccion);
        edt_telefono = root.findViewById(R.id.edt_telefono);
        btn_registrar = root.findViewById(R.id.btn_registrar);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }

            private void registrar() {

                String rut = edt_rut.getText().toString();
                String nombre = edt_nombre.getText().toString();
                String comuna = edt_comuna.getText().toString();
                String direccion = edt_direccion.getText().toString();
                String telefono = edt_telefono.getText().toString();

                if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(comuna) && !TextUtils.isEmpty(direccion)  && !TextUtils.isEmpty(telefono) ) {
                    String id = bdEmpresa.push().getKey();

                    Empresa mascota = new Empresa(id,rut, nombre,comuna, direccion, telefono);
                    bdEmpresa.child(id).setValue(mascota);

                    Toast.makeText(getActivity(), "Se registro correctamente", Toast.LENGTH_SHORT).show();
                    Limpiar();

                } else {
                    Toast.makeText(getActivity(), "Debe completar los campos", Toast.LENGTH_SHORT).show();

                }
            }

            private void Limpiar() {

                edt_rut.setText("");
                edt_nombre.setText("");
                edt_comuna.setText("");
                edt_direccion.setText("");
                edt_telefono.setText("");
            }
        });

        return root;
    }
}
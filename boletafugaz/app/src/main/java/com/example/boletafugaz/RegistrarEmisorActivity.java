package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boletafugaz.Model.Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarEmisorActivity extends AppCompatActivity {

    private EditText edt_rut, edt_nombre, edt_direccion, edt_telefono;
    private Button btn_registrar, btn_volver;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference bdEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_emisor);

        firebaseAuth = FirebaseAuth.getInstance();

        String id = firebaseAuth.getCurrentUser().getUid();

        bdEmpresa = FirebaseDatabase.getInstance().getReference("usuario").child(id).child("empresa");


        edt_rut = findViewById(R.id.edt_rut);
        edt_nombre = findViewById(R.id.edt_nombre);
        edt_direccion = findViewById(R.id.edt_direccion);
        edt_telefono = findViewById(R.id.edt_telefono);
        btn_registrar = findViewById(R.id.btn_registrar);
        btn_volver = findViewById(R.id.btn_volver);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarEmisorActivity.this,ProfileActivity.class));
                finish();
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }

            private void registrar() {

                String rut = edt_rut.getText().toString();
                String nombre = edt_nombre.getText().toString();
                String direccion = edt_direccion.getText().toString();
                String telefono = edt_telefono.getText().toString();

                if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(nombre)  && !TextUtils.isEmpty(direccion)  && !TextUtils.isEmpty(telefono) ) {
                    String id = bdEmpresa.push().getKey();

                    Empresa mascota = new Empresa(id,rut, nombre, direccion, telefono);
                    bdEmpresa.child(id).setValue(mascota);

                    Toast.makeText(RegistrarEmisorActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                    Limpiar();

                } else {
                    Toast.makeText(RegistrarEmisorActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                }
            }

            private void Limpiar() {

                edt_rut.setText("");
                edt_nombre.setText("");
                edt_direccion.setText("");
                edt_telefono.setText("");
            }
        });

    }
}
package com.example.boletafugaz;

import android.content.Intent;
import android.os.Bundle;

import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.Model.Giro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarGiroActivity extends AppCompatActivity {
    String id2;
    private EditText edt_nombre;
    private Button btn_registrar,btn_volver;
    private DatabaseReference bdGiro;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_giro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        firebaseAuth = FirebaseAuth.getInstance();

        id2 = getIntent().getStringExtra("id2");



        String id = firebaseAuth.getCurrentUser().getUid();
        bdGiro = FirebaseDatabase.getInstance().getReference("usuario").child(id).child("empresa").child(id2).child("giro");

        edt_nombre = findViewById(R.id.edt_nombre);
        btn_registrar = findViewById(R.id.btn_registrar);
        btn_volver = findViewById(R.id.btn_volver);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }

            private void registrar() {


                String nombre = edt_nombre.getText().toString();

                if (!TextUtils.isEmpty(nombre)) {
                    String id = bdGiro.push().getKey();

                    Giro giro = new Giro(id, nombre);
                    bdGiro.child(id).setValue(giro);

                    Toast.makeText(RegistrarGiroActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                    Limpiar();

                } else {
                    Toast.makeText(RegistrarGiroActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                }
            }
            private void Limpiar() {

                edt_nombre.setText("");
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarGiroActivity.this, ProfileActivity.class));
            }
        });

    }

}

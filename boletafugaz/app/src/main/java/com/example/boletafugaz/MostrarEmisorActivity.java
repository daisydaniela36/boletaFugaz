package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MostrarEmisorActivity extends AppCompatActivity {
    private EditText edt_rut;
    private EditText edt_nombre;
    private EditText edt_comuna;
    private EditText edt_direccion;
    private EditText edt_telefono;
    private FirebaseAuth firebaseAuth;
    private Button btn_editar;
    private Button btn_registrar_giro;
    private Button btn_volver;
    private DatabaseReference bdEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_emisor);

        firebaseAuth = FirebaseAuth.getInstance();

        String id = firebaseAuth.getCurrentUser().getUid();

        bdEmpresa = FirebaseDatabase.getInstance().getReference("usuario").child(id).child("empresa");

        edt_rut = findViewById(R.id.edt_rut);
        edt_nombre = findViewById(R.id.edt_nombre);
        edt_comuna = findViewById(R.id.edt_comuna);
        edt_direccion = findViewById(R.id.edt_direccion);
        edt_telefono = findViewById(R.id.edt_telefono);
        btn_editar = findViewById(R.id.btn_editar);
        btn_registrar_giro = findViewById(R.id.btn_registrar_giro);
        btn_volver = findViewById(R.id.btn_volver);

        id = getIntent().getStringExtra("id");
        String rutE = getIntent().getStringExtra("rut");
        String nombreE = getIntent().getStringExtra("nombre");
        String comunaE = getIntent().getStringExtra("comuna");
        String direccionE = getIntent().getStringExtra("direccion");
        String telefonoE = getIntent().getStringExtra("telefono");

        edt_rut.setText(rutE);
        edt_nombre.setText(nombreE);
        edt_comuna.setText(comunaE);
        edt_direccion.setText(direccionE);
        edt_telefono.setText(telefonoE);

    }
}
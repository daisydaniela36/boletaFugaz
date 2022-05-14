package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
    private DatabaseReference mDataBase;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_emisor);

        firebaseAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        id = firebaseAuth.getCurrentUser().getUid();

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

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = firebaseAuth.getCurrentUser().getUid();
                String rutE = edt_rut.getText().toString();
                String nombreE = edt_nombre.getText().toString();
                String comunaE = edt_comuna.getText().toString();
                String direccionE = edt_direccion.getText().toString();
                String telefonoE = edt_telefono.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("rut", rutE);
                map.put("nombre", nombreE);
                map.put("comuna", comunaE);
                map.put("direccion", direccionE);
                map.put("telefono", telefonoE);


                mDataBase.child("usuario").child(id2).child("empresa").child(id).updateChildren(map);

            }
        });

        btn_registrar_giro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MostrarEmisorActivity.this, RegistrarGiroActivity.class));

                Intent i = new Intent(getApplicationContext(), RegistrarGiroActivity.class);
                i.putExtra("id2", id);
                startActivity(i);
                finish();

            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MostrarEmisorActivity.this, ProfileActivity.class));
            }
        });

    }


}
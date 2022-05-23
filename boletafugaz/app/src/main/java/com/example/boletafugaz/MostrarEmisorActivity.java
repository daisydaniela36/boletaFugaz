package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private DatabaseReference bdEmpresa;
    private DatabaseReference mDataBase;
    String id,rut,nombre,comuna,direccion,telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_emisor);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        id = getIntent().getStringExtra("id");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        comuna = getIntent().getStringExtra("comuna");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");

        edt_rut.setText(rut);
        edt_nombre.setText(nombre);
        edt_comuna.setText(comuna);
        edt_direccion.setText(direccion);
        edt_telefono.setText(telefono);

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
                i.putExtra("id", id);
                i.putExtra("rut", rut);
                i.putExtra("nombre", nombre);
                i.putExtra("comuna", comuna);
                i.putExtra("direccion", direccion);
                i.putExtra("telefono", telefono);
                startActivity(i);
                finish();

            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


}
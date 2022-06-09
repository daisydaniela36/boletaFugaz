package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MostrarGiroActivity extends AppCompatActivity {

    String id1,nombre1;
    String id,rut,nombre,comuna,direccion,telefono;
    EditText edt_nombre;
    Button btn_Editar,btn_Eliminar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    String id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_giro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_nombre = findViewById(R.id.edt_nombre);
        btn_Editar = findViewById(R.id.btn_Editar);
        btn_Eliminar = findViewById(R.id.btn_Eliminar);

        id = getIntent().getStringExtra("id");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        comuna = getIntent().getStringExtra("comuna");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");

        firebaseAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        id2 = firebaseAuth.getCurrentUser().getUid();



        id1 = getIntent().getStringExtra("id1");
        nombre1 = getIntent().getStringExtra("nombre1");

        edt_nombre.setText(nombre1);

        btn_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDataBase.child("usuario").child(id2).child("empresa").child(id).child("giro").child(id1).removeValue();

                Intent i = new Intent(MostrarGiroActivity.this, ListaGiroActivity.class);

                i.putExtra("id", id);
                i.putExtra("rut", rut);
                i.putExtra("nombre", nombre);
                i.putExtra("comuna", comuna);
                i.putExtra("direccion", direccion);
                i.putExtra("telefono", telefono);

                startActivity(i);

            }
        });

        btn_Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edt_nombre.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("nombre", nombre);

                mDataBase.child("usuario").child(id2).child("empresa").child(id).child("giro").child(id1).updateChildren(map);

                Toast.makeText(MostrarGiroActivity.this, "Se ah editado correctamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ListaGiroActivity.class);

        i.putExtra("id", id);
        i.putExtra("rut", rut);
        i.putExtra("nombre", nombre);
        i.putExtra("comuna", comuna);
        i.putExtra("direccion", direccion);
        i.putExtra("telefono", telefono);

        startActivity(i);
        finish();
        return true;
    }
}
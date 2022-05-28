package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

public class MostrarGiroActivity extends AppCompatActivity {

    String id1,nombre1;
    String id,rut,nombre,comuna,direccion,telefono;
    EditText edt_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_giro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_nombre = findViewById(R.id.edt_nombre);

        id = getIntent().getStringExtra("id");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        comuna = getIntent().getStringExtra("comuna");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");


        id1 = getIntent().getStringExtra("id1");
        nombre1 = getIntent().getStringExtra("nombre1");

        edt_nombre.setText(nombre1);
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
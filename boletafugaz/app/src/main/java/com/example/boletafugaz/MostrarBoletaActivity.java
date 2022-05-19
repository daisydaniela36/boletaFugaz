package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;

public class MostrarBoletaActivity extends AppCompatActivity {

    String id,rut, nombre,comuna, direccion, telefono,fecha,total;
    int iva;
    EditText edt_Rut, edt_Nombre, edt_Comuna,edt_Direccion, edt_Telefono,edt_Fecha,edt_Total,edt_Iva;
    Button btn_Volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_boleta);

        edt_Rut = findViewById(R.id.edt_Rut);
        edt_Nombre = findViewById(R.id.edt_Nombre);
        edt_Comuna = findViewById(R.id.edt_Comuna);
        edt_Direccion = findViewById(R.id.edt_Direccion);
        edt_Telefono = findViewById(R.id.edt_Telefono);
        edt_Fecha = findViewById(R.id.edt_Fecha);
        edt_Total = findViewById(R.id.edt_Total);
        edt_Iva = findViewById(R.id.edt_Iva);
        btn_Volver = findViewById(R.id.btn_volver);


        id = getIntent().getStringExtra("id");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        comuna = getIntent().getStringExtra("comuna");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");
        fecha = getIntent().getStringExtra("fecha");
        total = getIntent().getStringExtra("total");

        DecimalFormat df = new DecimalFormat("#");

        int total1 = Integer.parseInt(total);

        double calc1 = total1 / 1.19;
        double calc2 = calc1 * 1.19;
        double resultIva = calc2-calc1;
        String resultIva2 = String.valueOf(df.format(resultIva));


        edt_Rut.setText(rut);
        edt_Nombre.setText(nombre);
        edt_Comuna.setText(comuna);
        edt_Direccion.setText(direccion);
        edt_Telefono.setText(telefono);
        edt_Fecha.setText(fecha);
        edt_Total.setText(total);
        edt_Iva.setText(resultIva2);

        btn_Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MostrarBoletaActivity.this, HistorialBoletasActivity.class));
            }
        });

    }
}
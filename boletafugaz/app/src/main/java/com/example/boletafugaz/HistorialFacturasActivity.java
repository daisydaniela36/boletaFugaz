package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistorialFacturasActivity extends AppCompatActivity {

    private Button btn_VolverF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_facturas);

        btn_VolverF = findViewById(R.id.btn_VolverF);

        btn_VolverF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistorialFacturasActivity.this, ProfileActivity.class));
            }
        });
    }
}
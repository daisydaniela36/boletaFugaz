package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.Model.Factura;
import com.example.boletafugaz.Model.Giro;
import com.example.boletafugaz.Model.Productos;
import com.example.boletafugaz.Model.Total;
import com.example.boletafugaz.utilidades.PrintBitmap;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MostrarFacturaActivity extends AppCompatActivity{

    String id1,rut, nombre,giro_empresa,comuna1, direccion1, telefono;
    String id2,fecha, rut_cliente,razon_Social, giro, direccion2,region,provincia,comuna2,total;
    EditText edt_Rut, edt_Nombre, edt_Comuna,edt_Direccion, edt_Telefono;
    Button btnVerProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_factura);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnVerProductos =  findViewById(R.id.btnVerProductos);

        edt_Rut = findViewById(R.id.edt_Rut);
        edt_Nombre = findViewById(R.id.edt_Nombre);
        edt_Comuna = findViewById(R.id.edt_Comuna);
        edt_Direccion = findViewById(R.id.edt_Direccion);
        edt_Telefono = findViewById(R.id.edt_Telefono);

        id1 = getIntent().getStringExtra("id1");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        giro_empresa = getIntent().getStringExtra("giro_empresa");
        comuna1 = getIntent().getStringExtra("comuna1");
        direccion1 = getIntent().getStringExtra("direccion1");
        telefono = getIntent().getStringExtra("telefono");

        id2 = getIntent().getStringExtra("id2");
        fecha = getIntent().getStringExtra("fecha");
        rut_cliente = getIntent().getStringExtra("rut_cliente");
        razon_Social = getIntent().getStringExtra("razon_Social");
        giro = getIntent().getStringExtra("giro");
        direccion2 = getIntent().getStringExtra("direccion2");
        region = getIntent().getStringExtra("region");
        provincia = getIntent().getStringExtra("provincia");
        comuna2 = getIntent().getStringExtra("comuna2");
        total = getIntent().getStringExtra("total");


        edt_Rut.setText(rut);
        edt_Nombre.setText(nombre);
        edt_Comuna.setText(comuna1);
        edt_Direccion.setText(direccion1);
        edt_Telefono.setText(telefono);


        btnVerProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MostrarProductosActivity.class);


                i.putExtra("id1", id1);
                i.putExtra("rut", rut);
                i.putExtra("nombre", nombre);
                i.putExtra("giro_empresa", giro_empresa);
                i.putExtra("comuna1", comuna1);
                i.putExtra("direccion1", direccion1);
                i.putExtra("telefono", telefono);


                i.putExtra("id2", id2);
                i.putExtra("fecha", fecha);
                i.putExtra("rut_cliente", rut_cliente);
                i.putExtra("giro_empresa", giro_empresa);
                i.putExtra("razon_Social", razon_Social);
                i.putExtra("giro", giro);
                i.putExtra("direccion2", direccion2);
                i.putExtra("region", region);
                i.putExtra("provincia", provincia);
                i.putExtra("comuna2", comuna2);
                i.putExtra("total", total);

                startActivity(i);
                finish();

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), HistorialFacturasActivity.class);
        startActivity(i);
        finish();
        return true;
    }




}
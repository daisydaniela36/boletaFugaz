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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

    TextView txt_Rut, txt_Nombre,txt_Giro_Empresa, txt_Comuna,txt_Direccion, txt_Telefono;
    TextView txt_Fecha,txt_Rut_Cliente, txt_Razon_Social, txt_Giro,txt_Direccion2, txt_Region,txt_Provincia,txt_Comuna2;

    Button btnVerProductos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_factura);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnVerProductos =  findViewById(R.id.btnVerProductos);

        txt_Rut = findViewById(R.id.txt_Rut);
        txt_Nombre = findViewById(R.id.txt_Nombre);
        txt_Giro_Empresa = findViewById(R.id.txt_Giro_Empresa);
        txt_Comuna = findViewById(R.id.txt_Comuna);
        txt_Direccion = findViewById(R.id.txt_Direccion);
        txt_Telefono = findViewById(R.id.txt_Telefono);

        txt_Fecha = findViewById(R.id.txt_Fecha);
        txt_Rut_Cliente = findViewById(R.id.txt_Rut_Cliente);
        txt_Razon_Social = findViewById(R.id.txt_Razon_Social);
        txt_Giro = findViewById(R.id.txt_Giro);
        txt_Direccion2 = findViewById(R.id.txt_Direccion2);
        txt_Region = findViewById(R.id.txt_Region);
        txt_Provincia = findViewById(R.id.txt_Provincia);
        txt_Comuna2 = findViewById(R.id.txt_Comuna2);

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

        txt_Rut.setText(rut);
        txt_Nombre.setText("Nombre: "+nombre);
        txt_Giro_Empresa.setText("Giro: "+giro_empresa);
        txt_Comuna.setText("Comuna: "+comuna1);
        txt_Direccion.setText("Direccion: "+direccion1);
        txt_Telefono.setText("Telefono: "+telefono);

        txt_Fecha.setText("Fecha: "+fecha);
        txt_Rut_Cliente.setText("Rut: "+rut_cliente);
        txt_Razon_Social.setText("Razon Social: "+razon_Social);
        txt_Giro.setText("Giro: "+giro);
        txt_Direccion2.setText("Direccion: "+direccion2);
        txt_Region.setText("Region: "+region);
        txt_Provincia.setText("Provincia: "+provincia);
        txt_Comuna2.setText("Comuna: "+comuna2);



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
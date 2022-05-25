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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boletafugaz.Model.Productos;
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
import java.util.List;
import java.util.UUID;

public class MostrarProductosActivity extends AppCompatActivity {
    private static final int REQUEST_DISPOSITIVO = 425;
    private static final String TAG_DEBUG = "tag_debug";
    private final int ANCHO_IMG_58_MM = 384;
    private static final int MODE_PRINT_IMG = 0;
    private TextView txtLabel;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothSocket bluetoothSocket;
    private UUID aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private OutputStream outputStream;
    private InputStream inputStream;
    private volatile boolean pararLectura;

    private BarcodeEncoder barcodeEncoder;
    private String valueOfEditText;
    private RelativeLayout relativeLayout;
    private MultiFormatWriter writer;
    private BitMatrix bitMatrix;
    private Bitmap bitmap = null;
    private ImageView ivCodeContainer;

    List<Productos> productos;
    private DatabaseReference bdProductos;
    private FirebaseAuth firebaseAuth;

    String id_usuario;
    String id1,rut, nombre,giro_empresa,comuna1, direccion1, telefono;
    String id2,fecha, rut_cliente,razon_Social, giro, direccion2,region,provincia,comuna2,total;
    Button btnImprimirTexto, btnCerrarConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtLabel = findViewById(R.id.txt_label);
        ivCodeContainer = findViewById(R.id.ivg_imagen);
        btnCerrarConexion = findViewById(R.id.btn_cerrar_conexion);
        btnImprimirTexto =  findViewById(R.id.btnImprimir);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        id_usuario = firebaseAuth.getCurrentUser().getUid();

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

        loadProductos();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImprimir:
                if (bluetoothSocket != null) {
                    try {

                        int fuente2 = 0;
                        int negrita2 = 1;
                        int ancho2 = 0;
                        int alto2 = 0;

                        String st1 = " ==============================" + "\n";
                        String st2 = "     "+rut+"\n";
                        String st3 = "      FACTURA ELECTRONICA" + "\n";
                        String st4 = "             N° 10" + "\n";
                        String st5 = " ==============================" + "\n";
                        String st6 = "Vendedor: " +nombre+ "\n";
                        String st7 = "Giro: " +giro_empresa+ "\n";
                        String st8 = "Fecha emision: " +fecha+ "\n";
                        String st9 = "direccion: " +direccion1+" "+comuna1+"\n";
                        String st10 =  " ==============================" + "\n";
                        String st11 = "         DATOS CLIENTE" + "\n";
                        String st12 = " ==============================" + "\n";
                        String st13 = "Rut: " +rut_cliente+ "\n";
                        String st14 = "Razon Social: " +razon_Social+"\n";
                        String st15 = "Giro: " +giro+"\n";
                        String st16 = "Direccion: " +direccion2+"\n";
                        String st17 = "Region: " +region+"\n";
                        String st18 = "Provincia: " +provincia+"\n";
                        String st19 = "Comuna: " +comuna2+"\n";
                        String st20 = " =============================="+"\n";
                        String st21 = "        DATOS PRODUCTOS"+"\n";
                        String st22 = " =============================="+"\n";




                        valueOfEditText = "B"+"O"+"L"+"E"+"T"+"A"+ "ELECTRONICA"+"N° 541";


                        if(valueOfEditText.equals("")|| valueOfEditText == null){
                            showSnackbar(getResources().getString(R.string.etWithoutContent));
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.etWithContent), Toast.LENGTH_SHORT).show();
                            generateQrCode(valueOfEditText);
                        }


                        outputStream.write(0x1C); outputStream.write(0x2E); // Cancelamos el modo de caracteres chino (FS .)
                        outputStream.write(0x1B); outputStream.write(0x74); outputStream.write(0x10); // Seleccionamos los caracteres escape (ESC t n) - n = 16(0x10) para WPC1252

                        outputStream.write( getByteString(st1,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st2,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st3,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st4,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st5,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st6,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st7,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st8,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st9,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st10,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st11,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st12,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st13,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st14,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st15,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st16,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st17,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st18,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st19,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st20,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st21,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st22,negrita2, fuente2, ancho2, alto2));



                        for(Productos p : productos){

                            String st23 ="Nombre: "+p.getNombre()+"\n"+"Cantidad: "+p.getCantidad()+"\n"+"Precio: "+p.getPrecio()+"\n"+"Total: "+p.getTotal()+"\n";
                            String st24 =" =============================="+"\n";




                            outputStream.write( getByteString(st23,negrita2, fuente2, ancho2, alto2));
                            outputStream.write( getByteString(st24,negrita2, fuente2, ancho2, alto2));

                        }


                        String st25 ="Total: "+total;
                        outputStream.write( getByteString(st25,negrita2, fuente2, ancho2, alto2));


                        byte[] center = new byte[]{ 0x1b, 0x61, 0x01 };
                        outputStream.write( center );

                        PrintHelper photoPrinter = new PrintHelper(MostrarProductosActivity.this);
                        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                        Bitmap bitmap = ((BitmapDrawable) ivCodeContainer.getDrawable()).getBitmap();

                        outputStream.write(PrintBitmap.POS_PrintBMP(bitmap, ANCHO_IMG_58_MM,MODE_PRINT_IMG));

                        outputStream.write("\n".getBytes());

                        byte[] center1 = new byte[]{ 0x1b, 0x61, 0x01 };
                        outputStream.write( center1 );


                    } catch (IOException e) {
                        Log.e(TAG_DEBUG, "Error al escribir en el socket");

                        Toast.makeText(this, "Error al interntar imprimir texto", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {

                    Log.e(TAG_DEBUG, "Socket nulo");

                    txtLabel.setText("Impresora no conectada");
                }

                break;

            case R.id.btn_cerrar_conexion:
                cerrarConexion();

                break;

        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), MostrarFacturaActivity.class);

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
        return true;
    }

    public void loadProductos(){
        productos = new ArrayList<>();
        bdProductos = FirebaseDatabase.getInstance().getReference();
        bdProductos.child("usuario").child(id_usuario).child("empresa").child(id1).child("Factura").child(id2).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String cantidad = ds.child("cantidad").getValue().toString();
                        String precio = ds.child("precio").getValue().toString();
                        String total = ds.child("total").getValue().toString();

                        int total2 = Integer.parseInt(total);
                        productos.add(new Productos(nombre,cantidad, precio, total2));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showSnackbar(String message){
        final Snackbar snackBar = Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG);
        snackBar.setAction(getResources().getString(R.string.accept), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    private void generateQrCode(String qrContent){
        writer = new MultiFormatWriter();
        try {
            bitMatrix = writer.encode(qrContent, BarcodeFormat.PDF_417, 700, 700);
            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivCodeContainer.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void clickBuscarDispositivosSync(View btn) {
        cerrarConexion();

        Intent intentLista = new Intent(this, ListaBluetoothActivity.class);
        startActivityForResult(intentLista, REQUEST_DISPOSITIVO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DISPOSITIVO:
                    txtLabel.setText("Cargando...");

                    final String direccionDispositivo = data.getExtras().getString("DireccionDispositivo");
                    final String nombreDispositivo = data.getExtras().getString("NombreDispositivo");

                    // Obtenemos el dispositivo con la direccion seleccionada en la lista
                    dispositivoBluetooth = bluetoothAdapter.getRemoteDevice(direccionDispositivo);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Conectamos los dispositivos

                                // Creamos un socket
                                bluetoothSocket = dispositivoBluetooth.createRfcommSocketToServiceRecord(aplicacionUUID);
                                bluetoothSocket.connect();// conectamos el socket
                                outputStream = bluetoothSocket.getOutputStream();
                                inputStream = bluetoothSocket.getInputStream();

                                //empezarEscucharDatos();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText(nombreDispositivo + " conectada");
                                        Toast.makeText(MostrarProductosActivity.this, "Dispositivo Conectado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText("");
                                        Toast.makeText(MostrarProductosActivity.this, "No se pudo conectar el dispositivo", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.e(TAG_DEBUG, "Error al conectar el dispositivo bluetooth");

                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
            }

        }
    }

    private void cerrarConexion() {
        try {
            if (bluetoothSocket != null) {
                if (outputStream != null) outputStream.close();
                pararLectura = true;
                if (inputStream != null) inputStream.close();
                bluetoothSocket.close();
                txtLabel.setText("Conexion terminada");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] getByteString(String str, int bold, int font, int widthsize, int heigthsize) {

        if (str.length() == 0 | widthsize < 0 | widthsize > 3 | heigthsize < 0 | heigthsize > 3
                | font < 0 | font > 1)
            return null;

        byte[] strData = null;
        try {
            strData = str.getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        byte[] command = new byte[strData.length + 9];

        byte[] intToWidth = {0x00, 0x10, 0x20, 0x30};//
        byte[] intToHeight = {0x00, 0x01, 0x02, 0x03};//

        command[0] = 27;// caracter ESC para darle comandos a la impresora
        command[1] = 69;
        command[2] = ((byte) bold);
        command[3] = 27;
        command[4] = 77;
        command[5] = ((byte) font);
        command[6] = 29;
        command[7] = 33;
        command[8] = (byte) (intToWidth[widthsize] + intToHeight[heigthsize]);

        System.arraycopy(strData, 0, command, 9, strData.length);
        return command;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cerrarConexion();
    }

}
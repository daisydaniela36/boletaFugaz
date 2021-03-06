package com.example.boletafugaz;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;
import com.example.boletafugaz.Model.Boleta;
import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.db.dbValor;
import com.example.boletafugaz.utilidades.PrintBitmap;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CalculadoraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_DISPOSITIVO = 425;
    private static final String TAG_DEBUG = "tag_debug";
    private final int ANCHO_IMG_58_MM = 384;
    private static final int MODE_PRINT_IMG = 0;

    private TextView txtLabel;
    private EditText edtTexto, etconcatenar;
    private Button btnImprimirTexto, btnCerrarConexion, btnVolver;
    private Button btnMas,btnIgual,btnNdoble0,btnN0,btnMultiplicar,btnN3,btnN2,btnN1,btnN6,btnN5,btnN4,btnAC,btnN9,btnN8,btnN7;
    private ImageButton btnClear;
    private String memoria = "";

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

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    List<Empresa> empresas;
    private DatabaseReference bdEmpresa;


    private Spinner spn_empresa;
    double calc1, calc2,resultIva;
    int numero1,numero2,resultado;

    String id1;
    String operador;
    String rut1, nombre1,comuna1, direccion1, telefono1;

    long ahora = System.currentTimeMillis();
    Date fecha = new Date(ahora);

    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    String salida = df.format(fecha);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtLabel = findViewById(R.id.txt_label);
        edtTexto = findViewById(R.id.txtPrecio);
        btnImprimirTexto =  findViewById(R.id.btnImprimir);
        btnCerrarConexion = findViewById(R.id.btn_cerrar_conexion);
        spn_empresa = findViewById(R.id.spn_empresa);
        ivCodeContainer = findViewById(R.id.ivg_imagen);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        loadEmpresa();

        btnMas = findViewById(R.id.btnMas);
        btnIgual = findViewById(R.id.btnIgual);
        btnNdoble0 = findViewById(R.id.btnNdoble0);
        btnN0 = findViewById(R.id.btnN0);
        btnMultiplicar = findViewById(R.id.btnMultiplicar);
        btnN3 = findViewById(R.id.btnN3);
        btnN2 = findViewById(R.id.btnN2);
        btnN1 = findViewById(R.id.btnN1);
        btnClear = findViewById(R.id.btnClear);
        btnN6 = findViewById(R.id.btnN6);
        btnN5 = findViewById(R.id.btnN5);
        btnN4 = findViewById(R.id.btnN4);
        btnAC = findViewById(R.id.btnAC);
        btnN9 = findViewById(R.id.btnN9);
        btnN8 = findViewById(R.id.btnN8);
        btnN7 = findViewById(R.id.btnN7);

        id1 = firebaseAuth.getCurrentUser().getUid();


        btnMas.setOnClickListener((v) -> {
            operador = "+";
            etconcatenar = findViewById(R.id.txtPrecio);
            numero1 = Integer.parseInt(etconcatenar.getText().toString());
            edtTexto.setText("");

        });

        btnIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etconcatenar = findViewById(R.id.txtPrecio);
                numero2 = Integer.parseInt(etconcatenar.getText().toString());
                if(operador.equals("+")){
                    edtTexto.setText("");
                    resultado = numero1 + numero2;
                    edtTexto.setText(etconcatenar.getText().toString() + resultado);

                }
                else if (operador.equals("*")){
                    edtTexto.setText("");
                    resultado = numero1 * numero2;
                    edtTexto.setText(etconcatenar.getText().toString() + resultado);
                }else if (resultado == 0){
                    resultado = numero1;
                    edtTexto.setText(etconcatenar.getText().toString() + resultado);
                }
            }
        });

        btnAC.setOnClickListener((v) -> {
            operador = "AC";
            numero1 = 0;
            numero2 = 0;
            resultado = 0;
            edtTexto.setText("");



        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!edtTexto.getText().toString().equals("")) {
                    edtTexto.setText(edtTexto.getText().subSequence(0, edtTexto.getText().length() - 1) + "");
                    memoria = "";
                }
            }
        });

        btnMultiplicar.setOnClickListener((v) -> {
            operador = "*";
            etconcatenar = findViewById(R.id.txtPrecio);
            numero1 = Integer.parseInt(etconcatenar.getText().toString());
            edtTexto.setText("");


        });

        btnNdoble0.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "00");
        });

        btnN0.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "0");

        });
        btnN3.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "3");

        });
        btnN2.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "2");

        });
        btnN1.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "1");

        });
        btnN6.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "6");

        });
        btnN5.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "5");

        });
        btnN4.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "4");

        });
        btnN9.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "9");

        });
        btnN8.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "8");

        });
        btnN7.setOnClickListener((v) -> {

            etconcatenar = findViewById(R.id.txtPrecio);
            edtTexto.setText(etconcatenar.getText().toString() + "7");

        });

        btnImprimirTexto.setOnClickListener(this);
        btnCerrarConexion.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    public void loadEmpresa(){

        empresas = new ArrayList<>();
        String id = firebaseAuth.getCurrentUser().getUid();
        mDataBase.child("usuario").child(id).child("empresa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){

                        String id3 = ds.getKey();
                        String rut = ds.child("rut").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        String comuna = ds.child("comuna").getValue().toString();
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        empresas.add(new Empresa(id3, rut, nombre,comuna,direccion,telefono));

                        ArrayAdapter<Empresa> arrayAdapter = new ArrayAdapter<>(CalculadoraActivity.this, android.R.layout.simple_dropdown_item_1line, empresas);
                        spn_empresa.setAdapter(arrayAdapter);



                        spn_empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String item = parent.getSelectedItem().toString();

                                DatabaseReference mDataBase2 = FirebaseDatabase.getInstance().getReference();
                                Query q = mDataBase2.child("usuario").child(id1).child("empresa").orderByChild("nombre").equalTo(item);

                                q.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                            String id = dataSnapshot.child("id").getValue().toString();
                                            String rut =  dataSnapshot.child("rut").getValue().toString();
                                            String nombre =  dataSnapshot.child("nombre").getValue().toString();
                                            String comuna =  dataSnapshot.child("comuna").getValue().toString();
                                            String direccion =  dataSnapshot.child("direccion").getValue().toString();
                                            String telefono =  dataSnapshot.child("telefono").getValue().toString();

                                            bdEmpresa = FirebaseDatabase.getInstance().getReference("usuario").child(id1).child("empresa").child(id).child("Boleta");


                                            rut1 = "R.U.T.: "+rut;
                                            nombre1 = nombre;
                                            comuna1 = comuna;
                                            direccion1 = direccion;
                                            telefono1 = telefono;
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnImprimir:
                if (bluetoothSocket != null) {
                    try {

                        DecimalFormat df = new DecimalFormat("#");


                        dbValor dbvalor = new dbValor(CalculadoraActivity.this);
                        SQLiteDatabase db = dbvalor.getWritableDatabase();

                        if (db != null){
                            Toast.makeText(CalculadoraActivity.this,"Base creada", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(CalculadoraActivity.this,"Error", Toast.LENGTH_LONG).show();
                        }


                        calc1 = resultado / 1.19;
                        calc2 = calc1 * 1.19;
                        resultIva = calc2-calc1;

                        if(resultado > 0 ){
                            calc1 = resultado / 1.19;
                            calc2 = calc1 * 1.19;
                            resultIva = calc2-calc1;

                        }else if(resultado == 0 ){
                            String value= edtTexto.getText().toString();
                            int finalValue=Integer.parseInt(value);
                            calc1 = finalValue / 1.19;
                            calc2 = calc1 * 1.19;
                            resultIva = calc2-calc1;
                        }

                        int fuente2 = 0;
                        int negrita2 = 1;
                        int ancho2 = 0;
                        int alto2 = 0;

                        int  numero = 20;

                        String total = edtTexto.getText().toString();
                        Integer total2 = Integer.parseInt(total);

                        String st = "\n";
                        String st1 = "==============================" + "\n";
                        String st2 = rut1 + "\n";
                        String st3 = "BOLETA ELECTRONICA" + "\n";
                        String st4 = "N?? "+numero + "\n";
                        String st5 = "------------------------------" + "\n";
                        String st6= nombre1+ "\n";
                        String st7 = direccion1 + "\n";
                        String st8 = comuna1 + "\n";
                        String st9 = "+569"+telefono1 + "\n";
                        String st10 = "==============================" + "\n";
                        String st11 = "FECHA EMISION: "+salida+ "\n";
                        String st12 = "==============================" + "\n";
                        String st13= "MONTO TOTAL: "+edtTexto.getText().toString() + "\n"+ "\n";
                        String st14= "el iva incluido en esta boleta  es de $"+df.format(resultIva) + "\n";
                        String st15= "------------------------------" + "\n";
                        String st16= "TIMBRE ELECTRONICO SII" + "\n";
                        String st17= "Verifique documento en sii.cl" + "\n";

                        int iva = Integer.parseInt(df.format(resultIva));





                        if (!TextUtils.isEmpty(salida) && !TextUtils.isEmpty(edtTexto.getText().toString())) {

                            String id = bdEmpresa.push().getKey();

                            Boleta boleta = new Boleta(numero,salida,iva,total2);
                            bdEmpresa.child(id).setValue(boleta);

                            Toast.makeText(CalculadoraActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(CalculadoraActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                        }

                        String vt1 = rut1;


                        valueOfEditText = rut1+"B"+"O"+"L"+"E"+"T"+"A"+ "ELECTRONICA"+"N?? 541"+nombre1+direccion1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1+telefono1;

                        //"fecha emision: 11/06/2021 monto total: 5000 el iva incluido               "

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

                        byte[] center = new byte[]{ 0x1b, 0x61, 0x01 };
                        outputStream.write( center );

                        PrintHelper photoPrinter = new PrintHelper(CalculadoraActivity.this);
                        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                        Bitmap bitmap = ((BitmapDrawable) ivCodeContainer.getDrawable()).getBitmap();

                        outputStream.write(PrintBitmap.POS_PrintBMP(bitmap, ANCHO_IMG_58_MM,MODE_PRINT_IMG));

                        outputStream.write("\n".getBytes());


                        outputStream.write( getByteString(st16,negrita2, fuente2, ancho2, alto2));
                        outputStream.write( getByteString(st17,negrita2, fuente2, ancho2, alto2));

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
        // Cerramos la conexion antes de establecer otra
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
                                        Toast.makeText(CalculadoraActivity.this, "Dispositivo Conectado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText("");
                                        Toast.makeText(CalculadoraActivity.this, "No se pudo conectar el dispositivo", Toast.LENGTH_SHORT).show();
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

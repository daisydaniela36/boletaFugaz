package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.boletafugaz.Model.Factura;
import com.example.boletafugaz.Model.Productos;
import com.example.boletafugaz.Model.Total;
import com.example.boletafugaz.utilidades.PrintBitmap;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.UUID;

public class AgregarProductosActivity extends AppCompatActivity implements View.OnClickListener{

    private final int ANCHO_IMG_58_MM = 384;
    private static final int MODE_PRINT_IMG = 0;
    private static final int REQUEST_DISPOSITIVO = 425;
    private static final String TAG_DEBUG = "tag_debug";
    TextView txtLabel;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothSocket bluetoothSocket;
    private OutputStream out;
    private InputStream inputStream;
    private UUID aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private volatile boolean pararLectura;

    private String valueOfEditText;
    private ImageView ivCodeContainer;
    private RelativeLayout relativeLayout;
    private MultiFormatWriter writer;
    private BitMatrix bitMatrix;
    private Bitmap bitmap = null;
    private BarcodeEncoder barcodeEncoder;

    private DatabaseReference bdFactura;
    private DatabaseReference bdProductos;
    private FirebaseAuth firebaseAuth;


    ArrayList<Productos> listaProductos = new ArrayList<>();
    ArrayList<Total> listaTotal = new ArrayList<>();

    private Button btnCerrarConexion,btnVolver,btnAgregar,btnImprimirTexto;
    private TableLayout tblProductos;

    private TextView txtNombre,txtCantidad,txtPrecio,total3;
    private TableRow row;
    int suma1 = 0;

    String id2,id3,rut_empresa,comuna_empresa,direccion_empresa,fecha,empresa,giro_empresa,rut,razon_Social,giro,direccion,region,provincia,comuna;
    String id4;

    public static final byte[] ESC_ALIGN_LEFT = new byte[] { 0x1b, 'a', 0x00 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_productos);



        btnCerrarConexion = findViewById(R.id.btn_cerrar_conexion);
        btnVolver = findViewById(R.id.btn_volver);
        btnAgregar = findViewById(R.id.btn_Agregar);
        txtLabel = findViewById(R.id.txt_label);
        btnImprimirTexto = findViewById(R.id.btnImprimir2);
        ivCodeContainer = findViewById(R.id.ivg_imagen);
        total3 = findViewById(R.id.edt_Total);
        txtNombre = findViewById(R.id.txtNombre);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtPrecio = findViewById(R.id.txtPrecio);
        listaProductos = new ArrayList<>();
        bdFactura = FirebaseDatabase.getInstance().getReference();
        bdProductos = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        tblProductos = findViewById(R.id.tblProductos);


        id3 = getIntent().getStringExtra("id empresa");
        rut_empresa = getIntent().getStringExtra("rut empresa");
        comuna_empresa = getIntent().getStringExtra("comuna empresa");
        direccion_empresa = getIntent().getStringExtra("direccion empresa");
        fecha = getIntent().getStringExtra("fecha");
        empresa = getIntent().getStringExtra("empresa");
        giro_empresa = getIntent().getStringExtra("giro_Empresa");


        rut = getIntent().getStringExtra("rut");
        razon_Social = getIntent().getStringExtra("razon_Social");
        giro = getIntent().getStringExtra("giro");
        direccion = getIntent().getStringExtra("direccion");
        region = getIntent().getStringExtra("region");
        provincia = getIntent().getStringExtra("provincia");
        comuna = getIntent().getStringExtra("comuna");

        id2 = firebaseAuth.getCurrentUser().getUid();

        bdFactura = FirebaseDatabase.getInstance().getReference("usuario").child(id2).child("empresa").child(id3).child("Factura");

        System.out.println("empresa: "+empresa);
        System.out.println("giro empresa: "+giro_empresa);
        System.out.println("rut: "+rut);
        System.out.println("razon Social: "+razon_Social);
        System.out.println("giro: "+giro);
        System.out.println("direccion: "+direccion);
        System.out.println("region: "+region);
        System.out.println("provincia: "+provincia);
        System.out.println("comuna: "+comuna);

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        row = new TableRow(getBaseContext());

        textView1 = new TextView(getBaseContext());
        textView1.setText("Nombre");
        textView1.setGravity(Gravity.CENTER);
        textView1.setPadding(54,30,54,30);
        textView1.setBackgroundResource(R.color.black);
        textView1.setTextColor(Color.WHITE);
        row.addView(textView1);

        textView2 = new TextView(getBaseContext());
        textView2.setText("Cantidad");
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(54,30,54,30);
        textView2.setBackgroundResource(R.color.black);
        textView2.setTextColor(Color.WHITE);
        row.addView(textView2);

        textView3 = new TextView(getBaseContext());
        textView3.setText("Precio");
        textView3.setGravity(Gravity.CENTER);
        textView3.setPadding(54,30,54,30);
        textView3.setBackgroundResource(R.color.black);
        textView3.setTextColor(Color.WHITE);
        row.addView(textView3);

        textView4 = new TextView(getBaseContext());
        textView4.setText("Total");
        textView4.setGravity(Gravity.CENTER);
        textView4.setPadding(54,30,54,30);
        textView4.setBackgroundResource(R.color.black);
        textView4.setTextColor(Color.WHITE);
        row.addView(textView4);


        tblProductos.addView(row);

        listaProductos = new ArrayList<>();


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaTotal = new ArrayList<>();

                String cantidad = txtCantidad.getText().toString();
                String precio = txtPrecio.getText().toString();

                int cantidadInt = Integer.parseInt(cantidad);
                int precioInt = Integer.parseInt(precio);

                int total = precioInt * cantidadInt ;

                String total1 = String.valueOf(total);




                listaProductos.add(new Productos(txtNombre.getText().toString(),txtCantidad.getText().toString(),txtPrecio.getText().toString(),total));

                listaTotal.add(new Total(total));

                for(Total t : listaTotal) {

                    t = listaTotal.get(listaTotal.size()-1);

                    suma1 += t.getTotal();

                }



                String t = String.valueOf(suma1);

                total3.setText("Total: "+t);
                System.out.println(suma1);


                String[] cadena = {txtNombre.getText().toString(),txtCantidad.getText().toString(),txtPrecio.getText().toString(),total1};
                row = new TableRow(getBaseContext());

                TextView textView;


                for (int i = 0; i < 4; i++){

                    textView = new TextView(getBaseContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(30,30,30,30);
                    textView.setBackgroundResource(R.color.white);
                    textView.setText(cadena[i]);
                    textView.setTextColor(Color.BLACK);
                    row.addView(textView);

                }
                tblProductos.addView(row);
            }
        });



        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FacturaActivity.class);

                i.putExtra("empresa1", empresa);
                i.putExtra("giro_Empresa1", giro_empresa);
                i.putExtra("rut1", rut);
                i.putExtra("razon_Social1", razon_Social);
                i.putExtra("giro1", giro);
                i.putExtra("direccion1", direccion);
                i.putExtra("region1", region);
                i.putExtra("provincia1", provincia);
                i.putExtra("comuna1", comuna);


                startActivity(i);
            }
        });

        btnImprimirTexto.setOnClickListener(this);
        btnCerrarConexion.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImprimir2:
                if (bluetoothSocket != null) {
                    try {
                        String id1 = bdFactura.push().getKey();


                        int fuente2 = 0;
                        int negrita2 = 1;
                        int ancho2 = 0;
                        int alto2 = 0;

                        String st1 = " ==============================" + "\n";
                        String st2 = "     "+rut_empresa+"\n";
                        String st3 = "      FACTURA ELECTRONICA" + "\n";
                        String st4 = "             N° 10" + "\n";
                        String st5 = " ==============================" + "\n";
                        String st6 = "Vendedor: " +empresa+ "\n";
                        String st7 = "Giro: " +giro_empresa+ "\n";
                        String st8 = "Fecha emision: " +fecha+ "\n";
                        String st9 = "direccion: " +direccion_empresa+" "+comuna_empresa+"\n";
                        String st10 =  " ==============================" + "\n";
                        String st11 = "         DATOS CLIENTE" + "\n";
                        String st12 = " ==============================" + "\n";
                        String st13 = "Rut: " +rut+ "\n";
                        String st14 = "Razon Social: " +razon_Social+"\n";
                        String st15 = "Giro: " +giro+"\n";
                        String st16 = "Direccion: " +direccion+"\n";
                        String st17 = "Region: " +region+"\n";
                        String st18 = "Provincia: " +provincia+"\n";
                        String st19 = "Comuna: " +comuna+"\n";
                        String st20 = " =============================="+"\n";
                        String st21 = "        DATOS PRODUCTOS"+"\n";
                        String st22 = " =============================="+"\n";

                        valueOfEditText = empresa+"B"+"O"+"L"+"E"+"T"+"A"+ "ELECTRONICA"+"N° 541";

                        if (!TextUtils.isEmpty(empresa)) {

                            id4 = bdFactura.push().getKey();

                            Factura factura = new Factura(id4,giro_empresa,fecha,rut,razon_Social,giro,direccion,region,provincia,comuna);
                            bdFactura.child(id4).setValue(factura);

                            bdProductos = FirebaseDatabase.getInstance().getReference("usuario").child(id2).child("empresa").child(id3).child("Factura").child(id4).child("Productos");

                            Toast.makeText(AgregarProductosActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(AgregarProductosActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                        }


                        if(valueOfEditText.equals("")|| valueOfEditText == null){
                            showSnackbar(getResources().getString(R.string.etWithoutContent));
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.etWithContent), Toast.LENGTH_SHORT).show();
                            generateQrCode(valueOfEditText);
                        }




                        out.write( getByteString(st1,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st2,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st3,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st4,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st5,negrita2, fuente2, ancho2, alto2));

                        out.write(ESC_ALIGN_LEFT);
                        out.write( getByteString(st6,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st7,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st8,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st9,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st10,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st11,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st12,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st13,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st14,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st15,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st16,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st17,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st18,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st19,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st20,negrita2, fuente2, ancho2, alto2));
                        out.write( getByteString(st21,negrita2, fuente2, ancho2, alto2));




                        for(Productos p : listaProductos) {

                            String st23 ="Nombre: "+p.getNombre()+"\n"+"Cantidad: "+p.getCantidad()+"\n"+"Precio: "+p.getPrecio()+"\n"+"Total: "+p.getTotal()+"\n";
                            String st24 =" =============================="+"\n";

                            String total1 = String.valueOf(suma1);

                            String st25 = "Total: "+total1+"\n";

                            if (!TextUtils.isEmpty(empresa)) {

                                String id2 = bdProductos.push().getKey();

                                Productos p2 = new Productos(p.getNombre(),p.getCantidad(),p.getPrecio(),suma1);
                                bdProductos.child(id2).setValue(p2);

                                Toast.makeText(AgregarProductosActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(AgregarProductosActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                            }

                            System.out.println(p.getNombre());

                            out.write( getByteString(st22,negrita2, fuente2, ancho2, alto2));
                            out.write( getByteString(st23,negrita2, fuente2, ancho2, alto2));
                            out.write( getByteString(st24,negrita2, fuente2, ancho2, alto2));
                            out.write( getByteString(st25,negrita2, fuente2, ancho2, alto2));


                        }


                        PrintHelper photoPrinter = new PrintHelper(AgregarProductosActivity.this);
                        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                        Bitmap bitmap = ((BitmapDrawable) ivCodeContainer.getDrawable()).getBitmap();
                        out.write(PrintBitmap.POS_PrintBMP(bitmap, ANCHO_IMG_58_MM,MODE_PRINT_IMG));


                    } catch (IOException e) {
                        Log.e(TAG_DEBUG, "Error al escribir en el socket");

                        Toast.makeText(this, "Error al interntar imprimir texto", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }else {

                    Log.e(TAG_DEBUG, "Socket nulo");

                    txtLabel.setText("Impresora no conectada");
                }

                break;

            case R.id.btn_cerrar_conexion:
                cerrarConexion();

                break;

        }
    }

    public void clickBuscarDispositivosSync(View btn) {
        // Cerramos la conexion antes de establecer otra
        cerrarConexion();

        Intent intentLista = new Intent(this, ListaBluetoothActivity.class);
        startActivityForResult(intentLista, REQUEST_DISPOSITIVO);
    }

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
                                out = bluetoothSocket.getOutputStream();
                                inputStream = bluetoothSocket.getInputStream();

                                //empezarEscucharDatos();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText(nombreDispositivo + " conectada");
                                        Toast.makeText(AgregarProductosActivity.this, "Dispositivo Conectado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText("");
                                        Toast.makeText(AgregarProductosActivity.this, "No se pudo conectar el dispositivo", Toast.LENGTH_SHORT).show();
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

    private void cerrarConexion() {
        try {
            if (bluetoothSocket != null) {
                if (out != null) out.close();
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
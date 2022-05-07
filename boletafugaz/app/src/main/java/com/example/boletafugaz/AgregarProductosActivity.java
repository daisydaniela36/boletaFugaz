package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.boletafugaz.Model.Giro;
import com.example.boletafugaz.Model.Productos;
import com.example.boletafugaz.utilidades.PrintBitmap;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AgregarProductosActivity extends AppCompatActivity implements View.OnClickListener{

    private final int ANCHO_IMG_58_MM = 384;
    private static final int MODE_PRINT_IMG = 0;
    private static final int REQUEST_DISPOSITIVO = 425;
    private static final String TAG_DEBUG = "tag_debug";
    private static final int COD_PERMISOS = 872;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothSocket bluetoothSocket;
    private OutputStream out;
    private InputStream inputStream;
    private Thread hiloComunicacion;
    private UUID aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private byte[] bufferLectura;
    private int bufferLecturaPosicion;
    private volatile boolean pararLectura;
    private String valueOfEditText;
    private ImageView ivCodeContainer;
    private RelativeLayout relativeLayout;
    private MultiFormatWriter writer;
    private BitMatrix bitMatrix;
    private Bitmap bitmap = null;
    private BarcodeEncoder barcodeEncoder;
    int suma;

    long ahora = System.currentTimeMillis();
    Date fecha = new Date(ahora);
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    String salida = df.format(fecha);

    ArrayList<Productos> listaProductos = new ArrayList<>();


    private Button btnCerrarConexion,btnVolver,btnAgregar,btnImprimirTexto;
    private TableLayout tblProductos;

    private TextView txtNombre;
    private TextView txtCantidad;
    private TextView txtPrecio;
    private EditText total3;

    private TableRow row;
    int suma1 = 0 ;
    String s;
    TextView txtLabel;

    Context context = this;

    String rut_empresa,comuna_empresa,direccion_empresa,empresa,giro_empresa,rut,razon_Social,giro,direccion,region,provincia,comuna;

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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        tblProductos = findViewById(R.id.tblProductos);

        rut_empresa = getIntent().getStringExtra("rut empresa");
        comuna_empresa = getIntent().getStringExtra("comuna empresa");
        direccion_empresa = getIntent().getStringExtra("direccion empresa");

        empresa = getIntent().getStringExtra("empresa");
        giro_empresa = getIntent().getStringExtra("giro_Empresa");
        rut = getIntent().getStringExtra("rut");
        razon_Social = getIntent().getStringExtra("razon_Social");
        giro = getIntent().getStringExtra("giro");
        direccion = getIntent().getStringExtra("direccion");
        region = getIntent().getStringExtra("region");
        provincia = getIntent().getStringExtra("provincia");
        comuna = getIntent().getStringExtra("comuna");

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




        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaProductos = new ArrayList<>();

                String cantidad = txtCantidad.getText().toString();
                String precio = txtPrecio.getText().toString();

                int cantidadInt = Integer.parseInt(cantidad);
                int precioInt = Integer.parseInt(precio);

                int total = precioInt * cantidadInt ;

                String total1 = String.valueOf(total);

                listaProductos.add(new Productos(txtNombre.getText().toString(),txtCantidad.getText().toString(),txtPrecio.getText().toString(),total));

                for(Productos p : listaProductos) {

                    p = listaProductos.get(listaProductos.size()-1);

                    suma1 += p.getTotal();
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
                        String st7 = "Fecha emision: " +salida+ "\n";
                        String st8 = "direccion: " +direccion_empresa+" "+comuna_empresa+"\n";
                        String st9 = " ==============================" + "\n";
                        String st10 = "        DATOS CLIENTE" + "\n";
                        String st11 = "Rut: " +rut+ "\n";
                        String st12 = "Razon Social: " +razon_Social+"\n";
                        String st13 = "Giro: " +giro+"\n";
                        String st14 = "Direccion: " +direccion+"\n";
                        String st15 = "Region: " +region+"\n";
                        String st16 = "Provincia: " +provincia+"\n";
                        String st17 = "Comuna: " +comuna+"\n";



                        valueOfEditText = empresa+"B"+"O"+"L"+"E"+"T"+"A"+ "ELECTRONICA"+"N° 541";


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

    public void cargarlista(){

        listaProductos = new ArrayList<>();

        String cantidad = txtCantidad.getText().toString();
        String precio = txtPrecio.getText().toString();

        int cantidadInt = Integer.parseInt(cantidad);
        int precioInt = Integer.parseInt(precio);

        int total = precioInt * cantidadInt ;

        String total1 = String.valueOf(total);
        listaProductos.add(new Productos(txtNombre.getText().toString(),txtCantidad.getText().toString(),txtPrecio.getText().toString(),total));
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


    private void empezarEscucharDatos() {

        final byte saltoLinea = 10;

        // Inicializamos las variables para leer el inputStream
        pararLectura = false;
        bufferLecturaPosicion = 0;
        bufferLectura = new byte[1024];

        hiloComunicacion = new Thread(new Runnable() {
            @Override
            public void run() {
                // Mientras el hilo no sea interrumpido y la variable booleana esté en false
                while (!Thread.currentThread().isInterrupted() && !pararLectura) {
                    try {
                        // Cantidad de bytes disponibles para leer al inputStream
                        int bytesDisponibles = inputStream.available();

                        if (bytesDisponibles > 0) {
                            byte[] paqueteDeBytes = new byte[bytesDisponibles];// para guardar los bytes del inputStream
                            inputStream.read(paqueteDeBytes);// leemos los byte y colocamos en paqueteDeBytes

                            for (int i = 0; i < bytesDisponibles; i++) {
                                byte b = paqueteDeBytes[i];// leemos los bytes uno a uno, lo guardamos en b

                                // Si es un salto de linea asumimos que es un renglon y lo pasamos a String
                                // Para ponerlo en el txtLabel, si no lo es guardamos en bufferLectura
                                // el byte leido hasta completar el renglon
                                if (b == saltoLinea) {
                                    Log.v(TAG_DEBUG, "Encontramos salto de linea");

                                    // array de bytes para copiar el array bufferLectura y pasarlo a String
                                    byte[] bytesCopia = new byte[bufferLecturaPosicion];

                                    // Copiamos el array
                                    System.arraycopy(bufferLectura, 0, bytesCopia, 0, bytesCopia.length);

                                    // Codificamos el array de byten en caracteres tipo ASCII de estados unidos
                                    final String datosString = new String(bytesCopia, "US-ASCII");

                                    // Colocamos la posicion en cero para leer una nueva linea y guardarla en bufferLectura
                                    bufferLecturaPosicion = 0;

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // colocamos lo leido en el inputStream en el EditText
                                            txtLabel.setText(datosString);
                                        }
                                    });
                                } else {
                                    Log.v(TAG_DEBUG, "leemos un byte");

                                    // Si no es un salto de linea es otro caracter y por tanto lo guardamos
                                    bufferLectura[bufferLecturaPosicion++] = b;
                                }
                            }
                        } else {
                            Log.v(TAG_DEBUG, "no hay bytes disponibles para leer");
                        }


                    } catch (IOException e) {
                        pararLectura = true;

                        Log.e(TAG_DEBUG, "Error ecuchar datos");
                        e.printStackTrace();
                    }
                }
            }
        });

        hiloComunicacion.start();

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

    /**
     * (font:A font:B)
     *
     * @param str
     * @param bold
     * @param font
     * @param widthsize
     * @param heigthsize
     * @return
     */
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

    public class TransformacionRotarBitmap extends BitmapTransformation {

        private float anguloRotar = 0f;

        public TransformacionRotarBitmap(Context context, float anguloRotar) {
            super(context);

            this.anguloRotar = anguloRotar;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(anguloRotar);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public String getId() {
            return "rotar" + anguloRotar;
        }
    }

    /**
     * Chequea cuales permisos faltan y los pide
     *
     * @return false si hay algun permiso faltante
     */
    private boolean pedirPermisosFaltantes() {
        boolean todosConsedidos = true;
        ArrayList<String> permisosFaltantes = new ArrayList<>();

        boolean permisoCamera = (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoEscrituraSD = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoLecturaSD = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);


        if (!permisoCamera) {
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.CAMERA);
        }

        if (!permisoEscrituraSD) {
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permisoLecturaSD) {
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!todosConsedidos) {
            String[] permisos = new String[permisosFaltantes.size()];
            permisos = permisosFaltantes.toArray(permisos);

            ActivityCompat.requestPermissions(this, permisos, COD_PERMISOS);
        }

        return todosConsedidos;
    }
}
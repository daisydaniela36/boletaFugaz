package com.example.boletafugaz;

import androidx.annotation.NonNull;
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
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.boletafugaz.Model.Empresa;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacturaActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_DISPOSITIVO = 425;
    private static final String TAG_DEBUG = "tag_debug";
    private static final int COD_PERMISOS = 872;
    private Spinner spn_empresa;
    List<Empresa> empresas;
    String rut1, nombre1,comuna1, direccion1, telefono1;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    Spinner combo1,combo2,combo3;
    ArrayAdapter<String> a1,a2,a3,a4;
    String regiones[] = {"Arica y Parinacota","Tarapaca",
                        "Antofagasta","Atacama","Coquimbo",
                        "Valparaiso","Metropolitana","OHiggins",
                        "Maule","Biobio","Araucania","Los Rios",
                        "Los Lagos","Aysen","Magallanes"};

    String provincia1[] = {"Arica","Parinacota"};
    String provincia2[] = {"Iquique","Tamarugal"};
    String provincia3[] = {"Antofagasta","El Loa","Tocopilla"};
    String provincia4[] = {"Copiapó","Chañaral","Huasco"};
    String provincia5[] = {"El Qui","Choapa","Limarí"};
    String provincia6[] = {"Valparaíso","Isla de Pascua","Los Andes","Petorca","San Felipe de Aconcagua","Quillota","San Antonio","Margamarga"};
    String provincia7[] = {"Chacabuco","Cordillera","Maipo","Talagante","Melipilla","Zona Metropolitana de Santiago"};
    String provincia8[] = {"Cachapoal","Colchahua","Cardenal Caro"};
    String provincia9[] = {"Curicó","Talca","Linares","Cauquenes"};
    String provincia10[] = {"Arauco","Concepción","Bío-Bío"};
    String provincia11[] = {"Malleco","Cautín"};
    String provincia12[] = {"Valdivia","Ranco"};
    String provincia13[] = {"Osorno","Llanquihue","Chiloé","Palena"};
    String provincia14[] = {"Aysén","Coyhaique","General Carrera","General Prat"};
    String provincia15[] = {"Magallanes","Última Esperanza","Tierra del Fuego","Antártica Chilena"};

    String comuna1p1[] = {"Arica","Camarones"};
    String comuna2p1[] = {"Putre","General Lagos"};

    String comuna1p2[] = {"Iquique","Alto Hospicio"};
    String comuna2p2[] = {"Pozo Almonte","Camiña","Colchane","Huara","Pica"};

    String comuna1p3[] = {"Antofagasta","Mejillones","Sierra Gorda","Talta"};
    String comuna2p3[] = {"Calama","Ollague","San Pedro de Atacama"};
    String comuna3p3[] = {"Tocopilla","Maria Elena"};

    String comuna1p4[] = {"Copiapó","Caldera","Tierra Amarilla"};
    String comuna2p4[] = {"Chañaral","Diego de Almagro"};
    String comuna3p4[] = {"Vallenar","Alto del Carmen","Freirina","Huasco"};

    String comuna1p5[] = {"La Serena","Coquimbo","Andacollo","La Higuera","Paiguano","Vicuña"};
    String comuna2p5[] = {"Illapel","Canela","Los Vilos","Salamanca"};
    String comuna3p5[] = {"Ovalle","Combarbalá","Monte Patria","Punitaqui","Rio Hurtado"};

    String comuna1p6[] = {"Valparaíso","Casablanca","Concón","Juan Fernández","Puchuncavi","Quintero","Viña del Mar"};
    String comuna2p6[] = {"Isla de Pascua"};
    String comuna3p6[] = {"Los Andes","Calle Larga","Rinconada","San Esteban"};
    String comuna4p6[] = {"La Ligua","Cabildo","Petorca","Zapallar"};
    String comuna5p6[] = {"San Felipe","Llaillay","Putaendo","Santa María","Catemu","Panquehue"};
    String comuna6p6[] = {" Quillota","Hijuelas","La Calera","La Cruz","Nogales"};
    String comuna7p6[] = {"San Antonio","Algarrobo","Cartagena","El Quisco","El Tabo","Santo Domingo"};
    String comuna8p6[] = {"Quilpué","Limache","Olmué","Villa Alemana"};

    String comuna1p7[] = {"Colina","Lampa","Til-Til"};
    String comuna2p7[] = {"Puente Alto","San José de Maipo","Pirque "};
    String comuna3p7[] = {"San Bernardo","Calera de Tango","Buin","Paine"};
    String comuna4p7[] = {"Isla de Maipo","El Monte","Padre Hurtado","Peñaflor","Talagante"};
    String comuna5p7[] = {"Alhué","Curacaví","María Pinto","Melipilla","San Pedro"};
    String comuna6p7[] = {"Cerrillos","Cerro Navia","Conchalí","El Bosque","Estación Central","Huechuraba","Independencia",
                        "La Cisterna","La Florida","La Granja","La Pintana","La Reina","Las Condes","Lo Barnechea","Lo Espejo",
                        "Lo Prado","Macul","Maipú","Ñuñoa","Pedro Aguirre Cerda", "Peñalolén"," Providencia","Pudahuel","Quilicura",
                        "Quinta Normal","Recoleta","Renca","San Joaquín","San Miguel","San Ramón","Santiago","Vitacura"};

    String comuna1p8[] = {"Rancagua","Codegua","Coinco","Coltauco","Doñihue","Graneros","Las Cabras","Machalí","Malloa","Mostazal",
                          "Olivar","Peumo","Pichidegua","Quinta de Tilcoco","Rengo","Requínoa","San Vicente de Tagua Tagua"};
    String comuna2p8[] = {"San Fernando","Chépica","Chimbarongo","Lolol","Nancagua","Palmilla","Peralillo","Placilla","Pumanque","Santa Cruz"};
    String comuna3p8[] = {"Pichilemu","La Estrella","Litueche","Marchigüe","Navidad","Paredones"};

    String comuna1p9[] = {"Curicó","Hualañé","Licantén","Molina","Rauco","Romeral","Sagrada Familia","Teno","Vichuquén"};
    String comuna2p9[] = {"Talca","Constitución","Curepto","Empedrado","Maule","Pelarco","Pencahue","Río Claro","San Clemente","San Rafael"};
    String comuna3p9[] = {"Linares","Colbún","Longaví","Parral","Retiro","San Javier","Villa Alegre","Yerbas Buenas"};
    String comuna4p9[] = {"Cauquenes","Chanco","Pelluhue"};

    String comuna1p10[] = {"Lebu","Arauco","Cañete","Contulmo","Curanilahue","Los Álamos","Tirúa"};
    String comuna2p10[] = {"Concepción","Chiguayante","Coronel","Florida","Hualpén","Hualqui","Lota","Penco","San Pedro de la Paz","Santa Juana","Talcahuano","Tomé"};
    String comuna3p10[] = {"Los Ángeles","Alto Bío Bío","Antuco","Cabrero","Laja","Mulchén","Nacimiento","Negrete","Quilaco","Quilleco",
                           "San Rosendo","Santa Bárbara","Tucapel","Yumbel"};

    String comuna1p11[] = {"Angol","Collipulli","Curacautín","Ercilla","Lonquimay","Los Sauces","Lumaco","Purén","Renaico","Traiguén","Victoria"};
    String comuna2p11[] = {"Temuco","Carahue","Chol Chol","Cunco","Curarrehue","Freire","Galvarino","Gorbea","Lautaro","Loncoche","Melipeuco",
                           "Nueva Imperial"," Padre Las Casas","Perquenco","Pitrufquén","Pucón","Saavedra","Teodoro Schmidt","Toltén","Vilcún","Villarrica"};

    String comuna1p12[] = {"Valdivia","Corral","Lanco","Los Lagos","Máfil","Mariquina","Paillaco","Panguipulli"};
    String comuna2p12[] = {"La Unión","Futrono","Lago Ranco","Río Bueno"};

    String comuna1p13[] = {"Osorno","Puerto Octay","Purranque","Puyehue","Río Negro","San Juan de la Costa","San Pablo"};
    String comuna2p13[] = {"Puerto Montt","Calbuco","Cochamó","Fresia","Frutillar","Los Muermos","Llanquihue","Maullín","Puerto Varas"};
    String comuna3p13[] = {"Castro","Ancud","Chonchi","Curaco de Vélez","Dalcahue","Puqueldón","Queilén","Quellón","Quemchi","Quinchao"};
    String comuna4p13[] = {"Chaitén","Futaleufú","Hualaihué","Palena"};

    String comuna1p14[] = {"Aysén","Cisnes","Guaitecas"};
    String comuna2p14[] = {"Coyhaique","Lago Verde"};
    String comuna3p14[] = {"Chile Chico","Río Ibáñez"};
    String comuna4p14[] = {"Cochrane","O’Higgins","Tortel"};

    String comuna1p15[] = {"Punta Arenas","Laguna Blanca","Río Verde","San Gregorio"};
    String comuna2p15[] = {"Puerto Natales","Torres del Paine"};
    String comuna3p15[] = {"Porvenir","Primavera","Timaukel"};
    String comuna4p15[] = {"Cabo de Hornos","Antártica"};

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothSocket bluetoothSocket;
    private UUID aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread hiloComunicacion;
    private byte[] bufferLectura;
    private int bufferLecturaPosicion;
    private volatile boolean pararLectura;

    private BarcodeEncoder barcodeEncoder;
    private String valueOfEditText;
    private String valueOfEditText1;
    private String valueOfEditText2;
    private File codeFile;
    private RelativeLayout relativeLayout;
    private MultiFormatWriter writer;
    private BitMatrix bitMatrix;
    private Bitmap bitmap = null;

    private ImageView ivCodeContainer;
    private final int ANCHO_IMG_58_MM = 384;
    private static final int MODE_PRINT_IMG = 0;

    private TextView txtLabel;
    private Button btnCerrarConexion, btnImprimirTexto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        txtLabel = findViewById(R.id.txt_label);
        btnCerrarConexion = findViewById(R.id.btn_cerrar_conexion);
        btnImprimirTexto2 =  findViewById(R.id.btnImprimir2);
        combo1 = findViewById(R.id.spinner7);
        combo2 = findViewById(R.id.spinner8);
        combo3 = findViewById(R.id.spinner6);
        spn_empresa = findViewById(R.id.spn_empresa);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        loadEmpresa();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnImprimirTexto2.setOnClickListener(this);
        btnCerrarConexion.setOnClickListener(this);


        combo1.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, regiones));

        combo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0 :

                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia1));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p1));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p1));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;


                    case 1:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia2));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p2));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p2));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia3));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p3));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p3));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p3));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia4));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p4));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p4));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p4));
                                        break;

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia5));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p5));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p5));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p5));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia6));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p6));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p6));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p6));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p6));
                                        break;
                                    case 4:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna5p6));
                                        break;
                                    case 5:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna6p6));
                                        break;
                                    case 6:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna7p6));
                                        break;
                                    case 7:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna8p6));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia7));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p7));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p7));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p7));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p7));
                                        break;
                                    case 4:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna5p7));
                                        break;
                                    case 5:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna6p7));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia8));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p8));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p8));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p8));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia9));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p9));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p9));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p9));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p9));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia10));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p10));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p10));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p10));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia11));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p11));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p11));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia12));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p12));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p12));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia13));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p13));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p13));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p13));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p13));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia14));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p14));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p14));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p14));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p14));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia15));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p15));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p15));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p15));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p15));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void loadEmpresa(){
        empresas = new ArrayList<>();
        String id = firebaseAuth.getCurrentUser().getUid();
        mDataBase.child("usuario").child(id).child("empresa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey();
                        String rut = ds.child("rut").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        String comuna = ds.child("comuna").getValue().toString();
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        empresas.add(new Empresa(id, rut, nombre,comuna,direccion,telefono));

                        ArrayAdapter<Empresa> arrayAdapter = new ArrayAdapter<>(FacturaActivity.this, android.R.layout.simple_dropdown_item_1line, empresas);
                        spn_empresa.setAdapter(arrayAdapter);



                        spn_empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String id2 = firebaseAuth.getCurrentUser().getUid();

                                String item = parent.getSelectedItem().toString();

                                DatabaseReference mDataBase2 = FirebaseDatabase.getInstance().getReference();
                                Query q = mDataBase2.child("usuario").child(id2).child("empresa").orderByChild("nombre").equalTo(item);

                                q.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                            String rut =  dataSnapshot.child("rut").getValue().toString();
                                            String nombre =  dataSnapshot.child("nombre").getValue().toString();
                                            String comuna =  dataSnapshot.child("comuna").getValue().toString();
                                            String direccion =  dataSnapshot.child("direccion").getValue().toString();
                                            String telefono =  dataSnapshot.child("telefono").getValue().toString();

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
            case R.id.btnImprimir2:
                if (bluetoothSocket != null) {

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
                                        Toast.makeText(FacturaActivity.this, "Dispositivo Conectado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtLabel.setText("");
                                        Toast.makeText(FacturaActivity.this, "No se pudo conectar el dispositivo", Toast.LENGTH_SHORT).show();
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
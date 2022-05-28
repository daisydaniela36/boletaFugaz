package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.Model.Factura;
import com.example.boletafugaz.Model.Giro;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FacturaActivity extends AppCompatActivity {
    private Spinner spn_empresa;
    private Spinner spn_giro;
    Spinner combo1,combo2,combo3;
    private EditText edt_rut,edt_Razon_Social,edt_Giro,edt_Direccion;

    List<Empresa> empresas;
    List<Giro> giro;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase1;

    String id2;
    String id3,rut1, nombre1,comuna1, direccion1, telefono1;


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

    String giroN[] = {"Sin giro registrado"};

    String empresa;
    String giro_empresa;
    String region;
    String provincia;
    String comuna;

    long ahora = System.currentTimeMillis();
    Date fecha = new Date(ahora);
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    String salida = df.format(fecha);


    private RelativeLayout relativeLayout;

    private Button btnAgregarProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        spn_empresa = findViewById(R.id.spn_empresa);
        spn_giro = findViewById(R.id.spn_giro);
        edt_rut = findViewById(R.id.edt_rut);
        edt_Razon_Social = findViewById(R.id.edt_Razon_Social);
        edt_Giro = findViewById(R.id.edt_Giro);
        edt_Direccion = findViewById(R.id.edt_direccion);
        combo1 = findViewById(R.id.spinner7);
        combo2 = findViewById(R.id.spinner8);
        combo3 = findViewById(R.id.spinner6);

        id2 = getIntent().getStringExtra("id usuario");

        empresa = getIntent().getStringExtra("empresa1");
        giro_empresa = getIntent().getStringExtra("giro_Empresa1");
        String rut = getIntent().getStringExtra("rut1");
        String razon_Social = getIntent().getStringExtra("razon_Social1");
        String giro = getIntent().getStringExtra("giro1");
        String direccion = getIntent().getStringExtra("direccion1");
        region = getIntent().getStringExtra("region1");
        provincia = getIntent().getStringExtra("provincia1");
        comuna = getIntent().getStringExtra("comuna1");

        System.out.println("empresa: "+empresa);
        System.out.println("giro empresa: "+giro_empresa);
        System.out.println("rut: "+rut);
        System.out.println("razon Social: "+razon_Social);
        System.out.println("giro: "+giro);
        System.out.println("direccion: "+direccion);
        System.out.println("region: "+region);
        System.out.println("provincia: "+provincia);
        System.out.println("comuna: "+comuna);


        edt_rut.setText(rut);
        edt_Razon_Social.setText(razon_Social);
        edt_Giro.setText(giro);
        edt_Direccion.setText(direccion);


        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        btnAgregarProductos = findViewById(R.id.btn_agregar_productos);

        loadEmpresa();

        spn_empresa.getSelectedItem();


        btnAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id4 = "";

                int numero_factura= 20;
                String numero_factura1 = String.valueOf(numero_factura);

                String giro_Empresa = spn_giro.getSelectedItem().toString();
                String rut = edt_rut.getText().toString();
                String razon_Social = edt_Razon_Social.getText().toString();
                String giro = edt_Giro.getText().toString();
                String direccion = edt_Direccion.getText().toString();
                String region = combo1.getSelectedItem().toString();
                String  provincia = combo2.getSelectedItem().toString();
                String comuna = combo3.getSelectedItem().toString();

                int iva = 0;
                int total = 0;

                if(!giro_Empresa.isEmpty() && !rut.isEmpty() && !razon_Social.isEmpty()  && !giro.isEmpty() && !direccion.isEmpty()){

                    Factura f = new Factura(id4,numero_factura,giro_Empresa,salida,rut,razon_Social,giro,direccion,region,provincia,comuna,iva,total);

                    Intent i = new Intent(getApplicationContext(), AgregarProductosActivity.class);

                    i.putExtra("id empresa",id3);
                    i.putExtra("numero_factura",numero_factura1);
                    i.putExtra("rut empresa",rut1);
                    i.putExtra("comuna empresa",comuna1);
                    i.putExtra("direccion empresa",direccion1);
                    i.putExtra("empresa", nombre1);
                    i.putExtra("fecha",salida);
                    i.putExtra("giro_Empresa", giro_Empresa);
                    i.putExtra("rut", f.getRut_cliente());
                    i.putExtra("razon_Social", f.getRazon_Social());
                    i.putExtra("giro", f.getGiro());
                    i.putExtra("direccion", f.getDireccion());
                    i.putExtra("region", f.getRegion());
                    i.putExtra("provincia", f.getProvincia());
                    i.putExtra("comuna", f.getComuna());

                    startActivity(i);

                }else{
                    Toast.makeText(FacturaActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }

            }

        });

        combo1.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, regiones));
        combo1.setSelection(getIndexSpinner(combo1, region));

        combo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0 :

                        combo2.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, provincia1));
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p1));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p1));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p2));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p2));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p3));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p3));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p3));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p4));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p4));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p4));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p5));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p5));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p5));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 4:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna5p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 5:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna6p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 6:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna7p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 7:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna8p6));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 4:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna5p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 5:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna6p7));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p8));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p8));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p8));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p9));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p9));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p9));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p9));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p10));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p10));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p10));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p11));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p11));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p12));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p12));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p13));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p13));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p13));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p13));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p14));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p14));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p14));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p14));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        combo2.setSelection(getIndexSpinner(combo2, provincia));
                        combo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0 :
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna1p15));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 1:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna2p15));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 2:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna3p15));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
                                        break;
                                    case 3:
                                        combo3.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, comuna4p15));
                                        combo3.setSelection(getIndexSpinner(combo3, comuna));
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
                        String id = ds.getKey();
                        String rut = ds.child("rut").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        String comuna = ds.child("comuna").getValue().toString();
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        empresas.add(new Empresa(id, rut, nombre,comuna,direccion,telefono));

                        ArrayAdapter<Empresa> arrayAdapter = new ArrayAdapter<>(FacturaActivity.this, android.R.layout.simple_dropdown_item_1line, empresas);
                        spn_empresa.setAdapter(arrayAdapter);
                        spn_empresa.setSelection(getIndexSpinner(spn_empresa, empresa));


                        spn_empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                id2 = firebaseAuth.getCurrentUser().getUid();
                                String item = parent.getSelectedItem().toString();

                                DatabaseReference mDataBase2 = FirebaseDatabase.getInstance().getReference();
                                Query q = mDataBase2.child("usuario").child(id2).child("empresa").orderByChild("nombre").equalTo(item);

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

                                            id3 = id;
                                            rut1 = "R.U.T.: "+rut;
                                            nombre1 = nombre;
                                            comuna1 = comuna;
                                            direccion1 = direccion;
                                            telefono1 = telefono;



                                            giro = new ArrayList<>();
                                            String id2 = firebaseAuth.getCurrentUser().getUid();
                                            mDataBase.child("usuario").child(id2).child("empresa").child(id).child("giro").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        for(DataSnapshot ds: snapshot.getChildren()){
                                                            String id = ds.getKey();
                                                            String nombre = ds.child("nombre").getValue().toString();

                                                            giro.add(new Giro(id, nombre));



                                                            ArrayAdapter<Giro> arrayAdapter = new ArrayAdapter<>(FacturaActivity.this, android.R.layout.simple_dropdown_item_1line, giro);
                                                            spn_giro.setAdapter(arrayAdapter);

                                                            spn_giro.setSelection(getIndexSpinner(spn_giro, giro_empresa));
                                                        }
                                                    }else{
                                                        spn_giro.setAdapter(new ArrayAdapter<String>(FacturaActivity.this, android.R.layout.simple_spinner_dropdown_item, giroN));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

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


    public static int getIndexSpinner(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }


}
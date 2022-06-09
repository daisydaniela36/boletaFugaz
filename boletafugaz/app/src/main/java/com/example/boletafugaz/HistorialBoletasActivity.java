package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.boletafugaz.Model.Boleta;
import com.example.boletafugaz.Model.Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistorialBoletasActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase1;
    private Spinner spn_empresa;

    List<Empresa> empresas;
    List<Boleta> lista_boletas = new ArrayList<Boleta>();
    ArrayAdapter<Boleta> arrayadapterBoletas;

    Boleta arrayAdapter;

    ListView lbl_boletas;
    RecyclerView rv;


    String id1,id2;
    String rut1, nombre1,comuna1, direccion1, telefono1;

    String boletas[] = {"SIN BOLETAS REGISTRADAS"};
    String r[] = {"NO SE ENCONTRO REGISTRO POR FAVOR BUSQUE POR FORMATO DD/MM/AA"};

    Button btn_Aceptar;
    EditText edt_Buscar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_boletas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spn_empresa = findViewById(R.id.spn_empresa);
        lbl_boletas = findViewById(R.id.lbl_listaBoletas);
        edt_Buscar = findViewById(R.id.edt_Buscar);
        btn_Aceptar = findViewById(R.id.btn_Aceptar);


        firebaseAuth = FirebaseAuth.getInstance();

        id1 = firebaseAuth.getCurrentUser().getUid();
        loadEmpresa();

        btn_Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buscar = String.valueOf(edt_Buscar.getText().toString());

                buscarBoleta(buscar);

                edt_Buscar.setText("");

            }
        });


        lbl_boletas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(lista_boletas == null || lista_boletas.size() == 0 ) {
                    // el arraylist no tiene valor
                }
                else{
                    Boleta b = lista_boletas.get(position);

                    Intent i = new Intent(HistorialBoletasActivity.this, MostrarBoletaActivity.class);

                    i.putExtra("id", id2);
                    i.putExtra("rut", rut1);
                    i.putExtra("nombre", nombre1);
                    i.putExtra("comuna", comuna1);
                    i.putExtra("direccion", direccion1);
                    i.putExtra("telefono", telefono1);

                    String numero_boleta = String.valueOf(b.getNumero_boleta());
                    i.putExtra("numero_boleta", numero_boleta);

                    i.putExtra("fecha", b.getFecha());

                    String iva = String.valueOf(b.getIva());
                    i.putExtra("iva", iva);

                    String total = String.valueOf(b.getTotal());
                    i.putExtra("total", total);

                    System.out.println(rut1);
                    System.out.println(b.getFecha());
                    System.out.println(b.getTotal());

                    startActivity(i);
                }



            }
        });
    }


    public void buscarBoleta(String s){
        ArrayList<Boleta>listaBoletas = new ArrayList<>();
        for(Boleta obj: lista_boletas){
            if(obj.getFecha().toLowerCase().contains(s.toLowerCase())){
                listaBoletas.add(obj);
                ArrayAdapter<Boleta> arrayAdapter = new ArrayAdapter<Boleta>(HistorialBoletasActivity.this, R.layout.list_item, listaBoletas);
                lbl_boletas.setAdapter(arrayAdapter);
            }else{
                lbl_boletas.setAdapter(new ArrayAdapter<String>(HistorialBoletasActivity.this,R.layout.list_item, r));
            }
        }

    }

    public void loadEmpresa(){
        empresas = new ArrayList<>();
        String id = firebaseAuth.getCurrentUser().getUid();
        mDataBase1 = FirebaseDatabase.getInstance().getReference();
        mDataBase1.child("usuario").child(id).child("empresa").addListenerForSingleValueEvent(new ValueEventListener() {
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

                        ArrayAdapter<Empresa> arrayAdapter = new ArrayAdapter<>(HistorialBoletasActivity.this, android.R.layout.simple_dropdown_item_1line, empresas);
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

                                            id2 = id;
                                            rut1 = "R.U.T.: "+rut;
                                            nombre1 = nombre;
                                            comuna1 = comuna;
                                            direccion1 = direccion;
                                            telefono1 = telefono;

                                            mDataBase = FirebaseDatabase.getInstance().getReference();
                                            mDataBase.child("usuario").child(id1).child("empresa").child(id2).child("Boleta").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    lista_boletas.clear();
                                                    if(snapshot.exists()){
                                                    for (DataSnapshot objSnaoshot : snapshot.getChildren()) {
                                                        Boleta b = objSnaoshot.getValue(Boleta.class);
                                                        lista_boletas.add(b);

                                                        arrayadapterBoletas = new ArrayAdapter<Boleta>(HistorialBoletasActivity.this, R.layout.list_item, lista_boletas);
                                                        lbl_boletas.setAdapter(arrayadapterBoletas);
                                                    }
                                                }else{
                                                        lbl_boletas.setAdapter(new ArrayAdapter<String>(HistorialBoletasActivity.this,R.layout.list_item, boletas));
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
        finish();
        return true;
    }

}
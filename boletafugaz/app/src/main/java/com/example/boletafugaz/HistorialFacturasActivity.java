package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.boletafugaz.Model.Boleta;
import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.Model.Factura;
import com.example.boletafugaz.Model.Giro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistorialFacturasActivity extends AppCompatActivity {
    List<Empresa> empresas;
    List<Factura> lista_facturas = new ArrayList<Factura>();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase1;
    private Spinner spn_empresa;

    String id3;

    String id2;
    String rut1, nombre1,giro_empresa,comuna1, direccion1, telefono1;
    String id4;
    ListView lbl_facturas;

    String factura[] = {"SIN FACTURAS REGISTRADAS"};

    ArrayAdapter<Factura> arrayadapterFactura;
    ArrayAdapter<Factura> arrayadapterFacturaerror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_facturas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spn_empresa = findViewById(R.id.spn_empresa);
        lbl_facturas = findViewById(R.id.lbl_listaFacturas);

        firebaseAuth = FirebaseAuth.getInstance();
        loadEmpresa();


        lbl_facturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(lista_facturas == null || lista_facturas.size() == 0 ) {
                    // el arraylist no tiene valor
                }
                else{
                Factura f = lista_facturas.get(position);

                Intent i = new Intent(HistorialFacturasActivity.this, MostrarFacturaActivity.class);

                i.putExtra("id1", id4);
                i.putExtra("rut", rut1);
                i.putExtra("nombre", nombre1);
                i.putExtra("giro_empresa", giro_empresa);
                i.putExtra("comuna1", comuna1);
                i.putExtra("direccion1", direccion1);
                i.putExtra("telefono", telefono1);

                i.putExtra("id2", f.getId());
                i.putExtra("giro_empresa", f.getGiro_empresa());
                i.putExtra("fecha", f.getFecha());
                i.putExtra("rut_cliente", f.getRut_cliente());
                i.putExtra("razon_Social", f.getRazon_Social());
                i.putExtra("giro", f.getGiro());
                i.putExtra("direccion2", f.getDireccion());
                i.putExtra("region", f.getRegion());
                i.putExtra("provincia", f.getProvincia());
                i.putExtra("comuna2", f.getComuna());

                String total2 = String.valueOf(f.getTotal());
                i.putExtra("total", total2);



                startActivity(i);
                }
            }
        });
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

                        id3 = ds.getKey();
                        String rut = ds.child("rut").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        String comuna = ds.child("comuna").getValue().toString();
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        empresas.add(new Empresa(id3, rut, nombre,comuna,direccion,telefono));

                        ArrayAdapter<Empresa> arrayAdapter = new ArrayAdapter<>(HistorialFacturasActivity.this, android.R.layout.simple_dropdown_item_1line, empresas);
                        spn_empresa.setAdapter(arrayAdapter);



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

                                            id4 = id;
                                            rut1 = "R.U.T.: "+rut;
                                            nombre1 = nombre;
                                            comuna1 = comuna;
                                            direccion1 = direccion;
                                            telefono1 = telefono;

                                            mDataBase = FirebaseDatabase.getInstance().getReference();
                                            mDataBase.child("usuario").child(id2).child("empresa").child(id4).child("Factura").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    lista_facturas.clear();
                                                    if(snapshot.exists()){
                                                        for (DataSnapshot objSnaoshot : snapshot.getChildren()) {
                                                            Factura f = objSnaoshot.getValue(Factura.class);
                                                            lista_facturas.add(f);

                                                            arrayadapterFactura = new ArrayAdapter<Factura>(HistorialFacturasActivity.this, R.layout.list_item, lista_facturas);
                                                            lbl_facturas.setAdapter(arrayadapterFactura);
                                                        }
                                                    }else{
                                                        lbl_facturas.setAdapter(new ArrayAdapter<String>(HistorialFacturasActivity.this,android.R.layout.simple_list_item_1, factura));
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
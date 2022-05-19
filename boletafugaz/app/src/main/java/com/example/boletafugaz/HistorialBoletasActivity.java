package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private Button btn_VolverB;
    List<Empresa> empresas;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase1;
    private Spinner spn_empresa;
    String id2;
    String rut1, nombre1,comuna1, direccion1, telefono1;
    String id4;
    ListView lbl_boletas;

    List<Boleta> lista_boletas = new ArrayList<Boleta>();
    ArrayAdapter<Boleta> arrayadapterBoletas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_boletas);

        btn_VolverB = findViewById(R.id.btn_VolverB);
        spn_empresa = findViewById(R.id.spn_empresa);
        lbl_boletas = findViewById(R.id.lbl_listaBoletas);


        firebaseAuth = FirebaseAuth.getInstance();


        loadEmpresa();

        lbl_boletas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Boleta b = lista_boletas.get(position);

                Intent i = new Intent(HistorialBoletasActivity.this, MostrarBoletaActivity.class);

                i.putExtra("id", id4);
                i.putExtra("rut", rut1);
                i.putExtra("nombre", nombre1);
                i.putExtra("comuna", comuna1);
                i.putExtra("direccion", direccion1);
                i.putExtra("telefono", telefono1);
                i.putExtra("fecha", b.getFecha());
                i.putExtra("total", b.getTotal());

                startActivity(i);

            }
        });


        btn_VolverB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistorialBoletasActivity.this, ProfileActivity.class));
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
                                            mDataBase.child("usuario").child(id2).child("empresa").child(id4).child("Boleta").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    lista_boletas.clear();
                                                    for (DataSnapshot objSnaoshot : snapshot.getChildren()) {
                                                        Boleta b = objSnaoshot.getValue(Boleta.class);
                                                        lista_boletas.add(b);

                                                        arrayadapterBoletas = new ArrayAdapter<Boleta>(HistorialBoletasActivity.this, R.layout.list_item, lista_boletas);
                                                        lbl_boletas.setAdapter(arrayadapterBoletas);
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
}
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
import android.widget.ListView;
import android.widget.Spinner;

import com.example.boletafugaz.Model.Boleta;
import com.example.boletafugaz.Model.Empresa;
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

public class ListaGiroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    String id,rut,nombre,comuna,direccion,telefono;

    List<Giro> lista_giros = new ArrayList<Giro>();
    ArrayAdapter<Giro> arrayadapterGiros;
    ListView lbl_giros;

    String giros[] = {"SIN GIROS REGISTRADAS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_giro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        id = getIntent().getStringExtra("id");
        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        comuna = getIntent().getStringExtra("comuna");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");

        lbl_giros = findViewById(R.id.lbl_listaGiros);

        listarGiros();

        lbl_giros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Giro g = lista_giros.get(position);

                Intent i = new Intent(ListaGiroActivity.this, MostrarGiroActivity.class);

                i.putExtra("id", id);
                i.putExtra("rut", rut);
                i.putExtra("nombre", nombre);
                i.putExtra("comuna", comuna);
                i.putExtra("direccion", direccion);
                i.putExtra("telefono", telefono);

                i.putExtra("id1", g.getId());
                i.putExtra("nombre1", g.getNombre());

                startActivity(i);
                finish();

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), MostrarEmisorActivity.class);

        i.putExtra("id", id);
        i.putExtra("rut", rut);
        i.putExtra("nombre", nombre);
        i.putExtra("comuna", comuna);
        i.putExtra("direccion", direccion);
        i.putExtra("telefono", telefono);

        startActivity(i);
        finish();
        return true;
    }

    private void listarGiros() {
        String id1 = firebaseAuth.getCurrentUser().getUid();
        mDataBase.child("usuario").child(id1).child("empresa").child(id).child("giro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista_giros.clear();

                if(snapshot.exists()){
                for (DataSnapshot objSnaoshot : snapshot.getChildren()) {
                    Giro g = objSnaoshot.getValue(Giro.class);
                    lista_giros.add(g);


                    arrayadapterGiros = new ArrayAdapter<Giro>(ListaGiroActivity.this, R.layout.list_item, lista_giros);
                    lbl_giros.setAdapter(arrayadapterGiros);
                }
            }else{
                    lbl_giros.setAdapter(new ArrayAdapter<String>(ListaGiroActivity.this,android.R.layout.simple_list_item_1, giros));
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
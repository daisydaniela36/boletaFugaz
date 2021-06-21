package com.example.boletafugaz.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.MostrarEmisorActivity;
import com.example.boletafugaz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaEmisorFragment extends Fragment {

    private List<Empresa> lista_empresa = new ArrayList<Empresa>();
    ArrayAdapter<Empresa> arrayadaptermascota;
    private FirebaseAuth firebaseAuth;
    ListView lbl_emisor;
    private DatabaseReference mDataBase;
    private static int save = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lista_emisor, container, false);

        lbl_emisor = (ListView) root.findViewById(R.id.lbl_emisores);


        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        lbl_emisor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Empresa m = lista_empresa.get(position);

                Intent i = new Intent(getActivity(), MostrarEmisorActivity.class);

                i.putExtra("id", m.getId());
                i.putExtra("rut", m.getRut());
                i.putExtra("nombre", m.getNombre());
                i.putExtra("comuna", m.getComuna());
                i.putExtra("direccion", m.getDireccion());
                i.putExtra("telefono", m.getTelefono());

                System.out.println(m.getId());


                startActivity(i);



            }
        });

        listarEmpresas();

        return root;
    }

    private void listarEmpresas() {
        String id = firebaseAuth.getCurrentUser().getUid();
        mDataBase.child("usuario").child(id).child("empresa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista_empresa.clear();
                for (DataSnapshot objSnaoshot : snapshot.getChildren()) {
                    Empresa e = objSnaoshot.getValue(Empresa.class);
                    lista_empresa.add(e);

                    arrayadaptermascota = new ArrayAdapter<Empresa>(getContext(), R.layout.list_item, lista_empresa);
                    lbl_emisor.setAdapter(arrayadaptermascota);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
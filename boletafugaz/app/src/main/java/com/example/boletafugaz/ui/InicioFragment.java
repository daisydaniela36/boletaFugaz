package com.example.boletafugaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.boletafugaz.CalculadoraActivity;
import com.example.boletafugaz.FacturaActivity;
import com.example.boletafugaz.R;
import com.example.boletafugaz.RegistrarEmisorActivity;


public class InicioFragment extends Fragment {

    Button btncalculadora, btnfactura;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);


        btncalculadora = root.findViewById(R.id.btn_calculadora);
        btnfactura = root.findViewById(R.id.btn_factura);

        btncalculadora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), CalculadoraActivity.class);
                startActivity(intent1);
            }
        });

        btnfactura.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), FacturaActivity.class);
                startActivity(intent1);
            }
        });
        return root;


    }



}
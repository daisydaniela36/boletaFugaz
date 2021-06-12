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
import com.example.boletafugaz.R;


public class InicioFragment extends Fragment {

    Button btncalculadora;
    Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        intent = new Intent(getActivity(), CalculadoraActivity.class);

        btncalculadora = root.findViewById(R.id.btn_calculadora);

        btncalculadora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return root;


    }



}
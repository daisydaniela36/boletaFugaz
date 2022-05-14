package com.example.boletafugaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.boletafugaz.FacturaActivity;
import com.example.boletafugaz.HistorialBoletasActivity;
import com.example.boletafugaz.HistorialFacturasActivity;
import com.example.boletafugaz.Model.Empresa;
import com.example.boletafugaz.R;

public class ReporteFragment extends Fragment {

    private Button btn_Entrar;
    private RadioButton rbt_Boletas, rbt_Facturas;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reporte, container, false);

        btn_Entrar = root.findViewById(R.id.btn_Entrar);
        rbt_Boletas = root.findViewById(R.id.rbt_Boletas);
        rbt_Facturas = root.findViewById(R.id.rbt_Facturas);


        btn_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbt_Boletas.isChecked()){
                    Intent i = new Intent(getActivity(), HistorialBoletasActivity.class);
                    startActivity(i);

                }else if(rbt_Facturas.isChecked()){
                    Intent i = new Intent(getActivity(), HistorialFacturasActivity.class);
                    startActivity(i);
                }

            }
        });

        return root;
    }
}
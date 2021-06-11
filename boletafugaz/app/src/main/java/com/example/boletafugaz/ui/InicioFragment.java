package com.example.boletafugaz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.boletafugaz.R;

public class InicioFragment extends Fragment {
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn00, btnX, btnMas, btnImprimir, btnIgualC, btnACC;
    private TextView lblTotal, txtPrecio, lblCantidadItemC;
    private ImageButton btnClear;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        btn1 = root.findViewById(R.id.btnN1);
        btn2 = root.findViewById(R.id.btnN2);
        btn3 = root.findViewById(R.id.btnN3);
        btn4 = root.findViewById(R.id.btnN4);
        btn5 = root.findViewById(R.id.btnN5);
        btn6 = root.findViewById(R.id.btnN6);
        btn7 = root.findViewById(R.id.btnN7);
        btn8 = root.findViewById(R.id.btnN8);
        btn9 = root.findViewById(R.id.btnN9);
        btn0 = root.findViewById(R.id.btnN0);
        btn00 = root.findViewById(R.id.btnNdoble0);
        btnX = root.findViewById(R.id.btnMultiplicar);
        btnMas = root.findViewById(R.id.btnAgregar);
        btnClear = root.findViewById(R.id.btnClear);
        btnImprimir = root.findViewById(R.id.btnImprimir);
        btnIgualC = root.findViewById(R.id.btnIgual);
        btnACC = root.findViewById(R.id.btnAC);
        txtPrecio = root.findViewById(R.id.txtPrecio);
        lblTotal = root.findViewById(R.id.lblMostrarTotal);
        lblCantidadItemC = root.findViewById(R.id.lblCantProductos);

        return root;
    }

}
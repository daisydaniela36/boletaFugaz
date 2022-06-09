package com.example.boletafugaz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boletafugaz.Model.Boleta;
import com.example.boletafugaz.R;

import java.util.List;

public class AdapterBoleta extends RecyclerView.Adapter<AdapterBoleta.viewholderboletas> {

    List<Boleta> listaBoleta;

    public AdapterBoleta(List<Boleta> listaBoleta){
        this.listaBoleta = listaBoleta;

    }
    @NonNull
    @Override
    public viewholderboletas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        viewholderboletas holder = new viewholderboletas(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderboletas holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewholderboletas extends RecyclerView.ViewHolder {
        public viewholderboletas(@NonNull View itemView) {
            super(itemView);
        }
    }
}

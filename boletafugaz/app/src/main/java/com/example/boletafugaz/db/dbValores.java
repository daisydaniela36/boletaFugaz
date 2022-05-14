package com.example.boletafugaz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.sql.SQLData;

public class dbValores extends dbValor {

    Context context;
    private static final String TABLA_VALOR = "t_valor";

    public dbValores(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarValores(){

        long id = 0;

        try{
            dbValor dbvalor = new dbValor(context);
            SQLiteDatabase db = dbvalor.getWritableDatabase();



            ContentValues values = new ContentValues();

            id = db.insert(TABLA_VALOR,null,values);



        }catch (Exception ex){
            ex.toString();
        }
        return id;


    }

}

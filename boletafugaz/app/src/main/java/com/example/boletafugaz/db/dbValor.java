package com.example.boletafugaz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.boletafugaz.CalculadoraActivity;

public class dbValor extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "valor.db";
    private static final String TABLA_VALOR = "t_valor";



    public dbValor(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLA_VALOR + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE "+ TABLA_VALOR);
        onCreate(sqLiteDatabase);
    }
}

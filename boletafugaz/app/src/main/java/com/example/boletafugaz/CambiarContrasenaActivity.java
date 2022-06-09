package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CambiarContrasenaActivity extends AppCompatActivity {

    String id,nombre,direccion,telefono,correo,contraseña;
    EditText edt_Contraseña;
    private FirebaseAuth firebaseAuth;
    Button btn_Cambiar;


    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_Contraseña = findViewById(R.id.edt_Contraseña);
        btn_Cambiar = findViewById(R.id.btn_Cambiar);

        firebaseAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        id = firebaseAuth.getCurrentUser().getUid();

        nombre = getIntent().getStringExtra("nombre");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");
        correo = getIntent().getStringExtra("correo");

        contraseña = getIntent().getStringExtra("contraseña");

        edt_Contraseña.setText(contraseña);

        btn_Cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });

    }

    private void resetPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(edt_Contraseña.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            contraseña = edt_Contraseña.getText().toString();

                            Map<String, Object> map = new HashMap<>();

                            map.put("contraseña", contraseña);



                            mDataBase.child("usuario").child(id).updateChildren(map);

                            Toast.makeText(CambiarContrasenaActivity.this,"Se ah cambiado la contraseña",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CambiarContrasenaActivity.this,"Ingrese contraseña con 6 o mas caracteres",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);

        i.putExtra("id", id);
        i.putExtra("nombre", nombre);
        i.putExtra("direccion", direccion);
        i.putExtra("telefono", telefono);
        i.putExtra("correo", correo);
        i.putExtra("contraseña", contraseña);

        startActivity(i);
        finish();
        return true;
    }
}
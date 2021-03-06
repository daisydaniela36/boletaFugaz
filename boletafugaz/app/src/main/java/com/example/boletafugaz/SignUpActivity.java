package com.example.boletafugaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText edtNombre, edtDireccion, edtTelefono, edtCorreo, edtContraseña;

    FirebaseAuth firebaseAuth;
    DatabaseReference dr;

    Button btnIngresar, btnRegistrar;

    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String contraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnIngresar = (Button) findViewById(R.id.btn_Vingresar);
        btnRegistrar = (Button) findViewById(R.id.btn_Vregistrar);

        edtNombre = (EditText) findViewById(R.id.edt_Vnombre);
        edtDireccion = (EditText) findViewById(R.id.edt_Vdireccion);
        edtTelefono = (EditText) findViewById(R.id.edt_Vtelefono);
        edtCorreo = (EditText) findViewById(R.id.edt_Vcorreo);
        edtContraseña = (EditText) findViewById(R.id.edt_Vcontraseña);
        btnIngresar = (Button) findViewById(R.id.btn_Vingresar);
        firebaseAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference();


        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nombre = edtNombre.getText().toString();
                direccion = edtDireccion.getText().toString();
                telefono = edtTelefono.getText().toString();
                correo = edtCorreo.getText().toString();
                contraseña = edtContraseña.getText().toString();

                if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty()) {
                    if (contraseña.length() >= 6) {
                        RegistrarUsuario();

                    }else {
                        Toast.makeText(SignUpActivity.this, "La contraseña debe tener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SignUpActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void RegistrarUsuario() {

        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("direccion", direccion);
                    map.put("telefono", telefono);
                    map.put("correo", correo);
                    map.put("contraseña", contraseña);


                    String id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    dr.child("usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Usuario Registrado Correctamente", Toast.LENGTH_SHORT).show();
                                Limpiar();

                            } else {
                                Toast.makeText(SignUpActivity.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Correo ya esta en uso o este correo no esta registrado por gmail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Limpiar() {
        edtNombre.setText("");
        edtDireccion.setText("");
        edtTelefono.setText("");
        edtCorreo.setText("");
        edtContraseña.setText("");
    }
}
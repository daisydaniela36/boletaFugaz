package com.example.boletafugaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GenerarQRActivity extends AppCompatActivity {

    //Componentes visuales :: Components of the view
    private RelativeLayout relativeLayout;
    private ImageView ivCodeContainer;
    private EditText etQrContent;
    private Button btnGenerateCode;
    private ImageView saveQrCodeButton;
    private ImageView shareQrCodeButton;
    private Toolbar toolbar;
    //Componentes librería ZXING :: Components of the ZXING library
    private MultiFormatWriter writer;
    private BitMatrix bitMatrix;
    private Bitmap bitmap = null;
    //Otros :: Others
    private BarcodeEncoder barcodeEncoder;
    private String valueOfEditText;
    private File codeFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_q_r);

        relativeLayout      = findViewById(R.id.relativeLayout);
        ivCodeContainer     = findViewById(R.id.ivCodeContainer);
        etQrContent         = findViewById(R.id.etQrContent);
        btnGenerateCode     = findViewById(R.id.qrContentButton);
        saveQrCodeButton    = findViewById(R.id.saveQrCodeButton);
        shareQrCodeButton   = findViewById(R.id.shareQrCodeButton);

        initialize();
    }

    private void initialize(){

        btnGenerateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueOfEditText = etQrContent.getText().toString();
                if(valueOfEditText.equals("")|| valueOfEditText == null){
                    showSnackbar(getResources().getString(R.string.etWithoutContent));
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.etWithContent), Toast.LENGTH_SHORT).show();
                    generateQrCode(valueOfEditText);
                }
            }
        });

    }

    private void generateQrCode(String qrContent){
        writer = new MultiFormatWriter();
        try {
            bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivCodeContainer.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void showSnackbar(String message){
        final Snackbar snackBar = Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG);
        snackBar.setAction(getResources().getString(R.string.accept), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

}
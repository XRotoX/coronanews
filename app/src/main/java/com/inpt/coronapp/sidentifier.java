package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sidentifier extends AppCompatActivity {
    Button cancel,inscritp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidentifier);
        cancel = findViewById(R.id.cancelButton);
        inscritp = findViewById(R.id.buttoninsc);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(sidentifier.this, MainActivity3.class);
                startActivity(i);
            }


        });
        inscritp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(sidentifier.this, inscription.class);
                startActivity(i);
            }


        });


    }
}

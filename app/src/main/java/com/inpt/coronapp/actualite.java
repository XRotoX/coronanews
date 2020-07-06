package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class actualite extends AppCompatActivity {
    Button buttonretour,buttprev,buttsymp, buttact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite);
        buttonretour = findViewById(R.id.retour);
        buttonretour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(actualite.this, MainActivity3.class);
                startActivity(i);
            }


        });
        buttprev = findViewById(R.id.prev);
        buttprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(actualite.this, prevention.class);
                startActivity(i);
            }


        });
        buttsymp = findViewById(R.id.symp);
        buttsymp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(actualite.this, syptoms.class);
                startActivity(i);
            }


        });
        buttact = findViewById(R.id.act);
        buttact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(actualite.this, actualitemaroc.class);
                startActivity(i);
            }


        });

    }
}

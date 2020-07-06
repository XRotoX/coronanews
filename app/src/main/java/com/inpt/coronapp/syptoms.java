package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class syptoms extends AppCompatActivity {
    Button bak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syptoms);
        bak = findViewById(R.id.backsymp);
        bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(syptoms.this, actualite.class);
                startActivity(i);
            }


        });
    }
}

package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity3 extends AppCompatActivity {


    Button buttonnvcas, buttonact, buttonid;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        buttonnvcas = findViewById(R.id.button1);
        buttonact  = findViewById(R.id.button2);
        buttonid= findViewById(R.id.button3);

        buttonnvcas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity3.this, MainActivity.class);
                startActivity(i);
            }
        });
        buttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(i);
            }


        });
        buttonact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity3.this, actualite.class);
                startActivity(i);
            }


        });


    }
}

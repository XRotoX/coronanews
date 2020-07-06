package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class actualitemaroc extends AppCompatActivity {
    Button bac,comm;
    TextView text;
    ArrayList<String> words = new ArrayList<>();
    String text1 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualitemaroc);
        bac = findViewById(R.id.backact);
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(actualitemaroc.this, actualite.class);
                startActivity(i);
            }


        });
        text=findViewById(R.id.text1);
        comm = findViewById(R.id.communication);
        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new doit().execute();

            }


        });

    }
    public class doit extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Document doc = Jsoup.connect("http://www.covidmaroc.ma/Pages/Communiques.aspx").get();
                        Element div = doc.select("tbody").first();
                        for (Element element : div.children()) {
                            Element element1 = element.selectFirst("span");
                            System.out.println(String.valueOf(element1));
                            words.add(element1.text());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    int j;
                    for(j = 0; j<words.size(); j++){
                        text1 = text1 + words.get(j) + "\n";
                    }

                }
            }).start();




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            text.setText(text1);
        }
    }



}

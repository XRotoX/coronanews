package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AllCountriesActivity extends AppCompatActivity {

    private ArrayList<String> casesCount = new ArrayList<>();
    private ArrayList<Bitmap> flags = new ArrayList<>();
    private ArrayList<String> countryNames = new ArrayList<>();
    String countryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_countries);



        String summaryJson =  getSharedPreferences("data",Context.MODE_PRIVATE).getString("summary", "0");

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(summaryJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                casesCount.add(String.valueOf(jsonArray.getJSONObject(i).getInt("TotalConfirmed")));
                countryName = jsonArray.getJSONObject(i).getString("Slug");
                String flagValue = getSharedPreferences("flags", Context.MODE_PRIVATE).getString(countryName, "0");
                if(flagValue.equals("0")) flagValue = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mOMzYp1BwADvQFt/Il5GAAAAABJRU5ErkJggg==";
                flags.add(decodeBase64(flagValue));
                countryNames.add(countryName.substring(0, 1).toUpperCase() + countryName.substring(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("summary", jsonArray.toString()).apply();

        initRecyclerView(casesCount, flags, countryNames);
    }

    private void initRecyclerView(ArrayList<String> casesCount, ArrayList<Bitmap> flags, ArrayList<String> countryNames) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.allCountriesRV);
        recyclerView.setLayoutManager(linearLayoutManager);
        AllCountriesAdapter adapter = new AllCountriesAdapter(casesCount, flags, countryNames, this);
        recyclerView.setAdapter(adapter);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
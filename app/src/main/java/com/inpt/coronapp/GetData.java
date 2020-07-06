package com.inpt.coronapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetData extends AppCompatActivity {


    public static void getWorldTotal(final Context context) {
        String url = "https://api.covid19api.com/world/total";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putInt("total", response.getInt("TotalConfirmed")).apply();
                            context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putInt("recovered", response.getInt("TotalRecovered")).apply();
                            context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putInt("death", response.getInt("TotalDeaths")).apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public static void getWorldSummary(final Context context) {
        String url = "https://api.covid19api.com/summary";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Countries");
                            context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("summary", jsonArray.toString()).apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public static void getCountries(final Context context) {
        String url = "https://api.covid19api.com/countries/";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = new JSONObject();
                        for(int i = 0; i< response.length(); i++){
                            try {
                                JSONObject countryObject = new JSONObject();
                                countryObject.put("ISO2", response.getJSONObject(i).getString("ISO2"));
                                jsonObject.put(response.getJSONObject(i).getString("Slug"), countryObject);
                                context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("countries" , jsonObject.toString()).apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public static void getCountryHistory(final Context context, final String country, final String status) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dt.format(c);
        String formattedDateTime = formattedDate+"T00:00:00Z";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        String formattedDateDelayed = dt.format(calendar.getTime());
        String formattedDateTimeDelayed = formattedDateDelayed+"T00:00:00Z";

        
        String url = "https://api.covid19api.com/total/country/" + country + "/status/" + status + "?from=" + formattedDateTimeDelayed + "&to=" + formattedDateTime;
        System.out.println(url);
        System.out.println(String.valueOf(context));

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(country);
                        JSONArray jsonArray = new JSONArray();
                        for(int i = 0; i<10; i++){
                            try {
                                jsonArray.put(response.getJSONObject(i).getInt("Cases"));
                                System.out.println(String.valueOf(response.getJSONObject(i).getInt("Cases")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString(country+status , jsonArray.toString()).apply();
                        System.out.println("History received");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(context, "Couldn't get new data", Toast.LENGTH_SHORT);
                    }
                });
        queue.add(jsonObjectRequest);
    }

}

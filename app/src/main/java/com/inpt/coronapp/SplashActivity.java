package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {

    int state = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Asyn_DownLoadFile().execute(null,null,null);

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public class Asyn_DownLoadFile extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            GetData.getWorldTotal(SplashActivity.this);
            GetData.getWorldSummary(SplashActivity.this);
            int fisrtUse = getSharedPreferences("firstUse", Context.MODE_PRIVATE).getInt("used", 0);

            if(fisrtUse == 0){
                String countries = getSharedPreferences("data",Context.MODE_PRIVATE).getString("summary", "0");
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(countries);
                }catch (JSONException e) {
                    e.printStackTrace();
                    state = 1;
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        String ISO2 = jsonArray.getJSONObject(i).getString("CountryCode");
                        final String countryName = jsonArray.getJSONObject(i).getString("Slug");
                        String url0 = "https://www.countryflags.io/" + ISO2 + "/flat/64.png";
                        URL url = new URL(url0);
                        URLConnection urlConn = url.openConnection();
                        final HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                        String value = getSharedPreferences("flags", Context.MODE_PRIVATE).getString(countryName, "0");
                        if(value.equals("0")){
                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try  {

                                        InputStream in = null;
                                        httpConn.connect();
                                        in = httpConn.getInputStream();
                                        Bitmap bmpimg = BitmapFactory.decodeStream(in);
                                        System.out.println(String.valueOf(bmpimg));
                                        getSharedPreferences("flags", Context.MODE_PRIVATE).edit().putString(countryName, encodeTobase64(bmpimg)).apply();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        state = 1;
                                    }
                                }
                            });

                            thread.start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        state = 1;
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                        state = 1;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        state = 1;
                    }

                }
                if(state != 1) getSharedPreferences("firstUse", Context.MODE_PRIVATE).edit().putInt("used", 1);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Intent mainintent = new Intent(SplashActivity.this, MainActivity3.class);
            startActivity(mainintent);
            finish();
        }

    }


}
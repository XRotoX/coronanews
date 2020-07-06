package com.inpt.coronapp;




import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Integer;



public class MainActivity extends DemoBase {

    private ArrayList<LineData> chartDataA = new ArrayList<>();
    private ArrayList<LineData> chartDataR = new ArrayList<>();
    private ArrayList<LineData> chartDataD = new ArrayList<>();
    private ArrayList<String> countryNamesA = new ArrayList<>();
    private ArrayList<String> countryStatusA = new ArrayList<>();
    private ArrayList<String> countryNamesR = new ArrayList<>();
    private ArrayList<String> countryStatusR = new ArrayList<>();
    private ArrayList<String> countryNamesD = new ArrayList<>();
    private ArrayList<String> countryStatusD = new ArrayList<>();
    private LineChart chart, chartTop, chartBottom;
    private PieChart chartPie;
    private TextView affectedTV, activeTV, viewAll;
    String statusKey = "TotalConfirmed", statusLink = "confirmed";
    int allRB = 0, recoveredRB = 0, deadRB = 0;
    int count = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        affectedTV = findViewById(R.id.affectedtV);
        activeTV = findViewById(R.id.activeTV);
        viewAll = findViewById(R.id.viewAll);


        int total = getSharedPreferences("data", Context.MODE_PRIVATE).getInt("total", 0);
        int recovered = getSharedPreferences("data", Context.MODE_PRIVATE).getInt("recovered",0);
        int death = getSharedPreferences("data", Context.MODE_PRIVATE).getInt("death", 0);

        affectedTV.setText(abbreviateNumber(total));
        activeTV.setText(abbreviateNumber(total-recovered-death));
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllCountriesActivity.class);
                startActivity(intent);
            }
        });


        RadioGroup rGroup = findViewById(R.id.tabsRG);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.allRB:
                        if(allRB == 0){
                            statusKey = "TotalConfirmed";
                            statusLink = "confirmed";
                            iniHV(chartDataA, countryNamesA, countryStatusA);
                            allRB = 1;
                        }else{
                            initRecyclerView(chartDataA, countryNamesA, countryStatusA);
                        }
                        break;
                    case R.id.recoveredRB:
                        if(recoveredRB == 0){
                            statusKey = "TotalRecovered";
                            statusLink = "recovered";
                            iniHV(chartDataR, countryNamesR, countryStatusR);
                            recoveredRB = 1;
                        }else{
                            initRecyclerView(chartDataR, countryNamesR, countryStatusR);
                        }
                        break;
                    case R.id.deadRB:
                        if(deadRB == 0){
                            statusKey = "TotalDeaths";
                            statusLink = "deaths";
                            iniHV(chartDataD, countryNamesD, countryStatusD);
                            deadRB = 1;
                        }else{
                            initRecyclerView(chartDataD, countryNamesD, countryStatusD);
                        }
                        break;
                }
            }
        });

        iniHV(chartDataA, countryNamesA, countryStatusA);
        initChartTop();
        initChartBottom();
        initPieChart(total-recovered-death, recovered, death);
    }



    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "CubicLineChartActivity");
    }

    public void iniHV(ArrayList<LineData> chartData, ArrayList<String> countryNames, ArrayList<String> countryStatus){
        chartData.clear();
        countryNames.clear();
        countryStatus.clear();

        String summaryJson =  getSharedPreferences("data",Context.MODE_PRIVATE).getString("summary", "0");

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(summaryJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray sortedSummary = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonList.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                int valB = 0;

                try {
                    valA = (int) a.get(statusKey);
                    valB = (int) b.get(statusKey);
                }
                catch (JSONException e) {

                }
                return valB > valA ? 1 : (valB < valA ? -1 : 0);
            }
        });

        for (int i = 0; i < count; i++) {
            sortedSummary.put(jsonList.get(i));
        }

        for(int j=0; j < count; j++){
            ArrayList<Entry> values = new ArrayList<>();

            int days = 10;

            String country = null;
            try {
                country = sortedSummary.getJSONObject(j).getString("Slug");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //GetData.getCountry7History(MainActivity.this, country);
            String capCountry = country.substring(0, 1).toUpperCase() + country.substring(1);
            countryNames.add(capCountry);

            GetData.getCountryHistory(MainActivity.this, country, statusLink);

            String datakey = String.valueOf(country) + String.valueOf(statusLink);

            String strJson1 =  getSharedPreferences("data",Context.MODE_PRIVATE).getString(datakey, "0");

            JSONArray jsonArray1 = new JSONArray();

            try {
                jsonArray1 = new JSONArray(strJson1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            float b = 0;
            for (int i = 1; i < days; i++) {
                try {
                    int a = (int) jsonArray1.get(i) - (int) jsonArray1.get(i-1);
                    b = (float) a;
                    values.add(new Entry(i, b));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            int c = (int) b;

            if(b < 0)  countryStatus.add("- " + String.valueOf(c));
            else if(b > 0) countryStatus.add("+ " + String.valueOf(c));
            else countryStatus.add("No data, try later");

            LineDataSet set1;

            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(4f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setColor(Color.WHITE, 170);
            set1.setFillAlpha(40);
            set1.setDrawHorizontalHighlightIndicator(false);
            LineData data = new LineData(set1);
            data.setValueTypeface(tfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            chartData.add(data);

        }
        initRecyclerView(chartData, countryNames, countryStatus);
    }

    private void initPieChart(int active, int recovered, int death){

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(active));
        entries.add(new PieEntry(recovered));
        entries.add(new PieEntry(death));


        PieDataSet dataSet = new PieDataSet(entries, "Covid-19 cases");

        dataSet.setDrawIcons(false);

        //dataSet.setSliceSpace(3f);
        //dataSet.setIconsOffset(new MPPointF(0, 40));
        //dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();


        colors.add(Color.rgb(52, 138, 123));
        colors.add(Color.rgb(253, 210, 98));
        colors.add(Color.rgb(255, 131, 103));
        colors.add(Color.rgb(252, 105, 105));
        colors.add(Color.rgb(80, 155, 255));

        //Color.rgb(52, 138, 123)
        //Color.rgb(255, 131, 103)
        //Color.rgb(253, 210, 98)
        //Color.rgb(252, 105, 105)
        //Color.rgb(80, 155, 255)



        //colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chartPie));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);


        chartPie = findViewById(R.id.chartPie);
        chartPie.setUsePercentValues(false);
        chartPie.getDescription().setEnabled(false);
        chartPie.setExtraOffsets(5, 10, 5, 5);

        chartPie.setDragDecelerationFrictionCoef(0.95f);

        chartPie.setCenterTextTypeface(tfLight);
        //chartPie.setCenterText(generateCenterSpannableText());

        chartPie.setDrawHoleEnabled(true);
        chartPie.setHoleColor(Color.WHITE);

        chartPie.setTransparentCircleColor(Color.WHITE);
        chartPie.setTransparentCircleAlpha(110);

        chartPie.setHoleRadius(58f);
        chartPie.setTransparentCircleRadius(61f);

        chartPie.setDrawCenterText(false);

        chartPie.setRotationAngle(0);
        // enable rotation of the chart by touch
        chartPie.setRotationEnabled(true);
        chartPie.setHighlightPerTapEnabled(true);

        chartPie.getLegend().setEnabled(false);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chartPie.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);


        chartPie.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chartPie.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chartPie.setEntryLabelColor(Color.WHITE);
        chartPie.setEntryLabelTypeface(tfRegular);
        chartPie.setEntryLabelTextSize(12f);

        chartPie.setData(data);

        // undo all highlights
        chartPie.highlightValues(null);

        chartPie.invalidate();
    }

    private void initChartTop(){

        ArrayList<Entry> values = new ArrayList<>();

        int count = 10;
        float range = 20;

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(values, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawCircles(false);
        set1.setLineWidth(3f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(Color.WHITE);
        //set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(252, 99, 98));
        set1.setDrawHorizontalHighlightIndicator(false);

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTypeface(tfLight);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        chartTop = findViewById(R.id.chartTop);
        chartTop.setViewPortOffsets(0, 0, 0, 0);
        //chartTop.setBackgroundColor(Color.rgb(252, 99, 98));

        // no description text
        chartTop.getDescription().setEnabled(false);

        // enable touch gestures
        chartTop.setTouchEnabled(false);

        // enable scaling and dragging
        chartTop.setDragEnabled(false);
        chartTop.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chartTop.setPinchZoom(false);

        chartTop.setDrawGridBackground(false);
        chartTop.setMaxHighlightDistance(300);

        XAxis x = chartTop.getXAxis();
        x.setEnabled(false);

        YAxis y = chartTop.getAxisLeft();
        y.setEnabled(false);

        chartTop.getAxisRight().setEnabled(false);

        chartTop.getLegend().setEnabled(false);

        chartTop.animateXY(1000, 500);

        chartTop.setData(data);

        chartTop.invalidate();
    }

    private void initChartBottom(){


        ArrayList<Entry> values = new ArrayList<>();

        int count = 10;
        float range = 20;

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(values, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawCircles(false);
        set1.setLineWidth(3f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(Color.WHITE);
        //set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(132, 190, 179));
        set1.setDrawHorizontalHighlightIndicator(false);

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTypeface(tfLight);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        chartBottom = findViewById(R.id.chartBottom);
        chartBottom.setViewPortOffsets(0, 0, 0, 0);
        //chartBottom.setBackgroundColor(Color.rgb(132, 190, 179));

        // no description text
        chartBottom.getDescription().setEnabled(false);

        // enable touch gestures
        chartBottom.setTouchEnabled(false);

        // enable scaling and dragging
        chartBottom.setDragEnabled(false);
        chartBottom.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chartBottom.setPinchZoom(false);

        chartBottom.setDrawGridBackground(false);
        chartBottom.setMaxHighlightDistance(300);

        XAxis x = chartBottom.getXAxis();
        x.setEnabled(false);

        YAxis y = chartBottom.getAxisLeft();
        y.setEnabled(false);

        chartBottom.getAxisRight().setEnabled(false);

        chartBottom.getLegend().setEnabled(false);

        chartBottom.animateXY(1000, 500);

        chartBottom.setData(data);

        chartBottom.invalidate();
    }

    private void initRecyclerView(ArrayList<LineData> chartData, ArrayList<String> countryNames, ArrayList<String> countryStatus){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.dataRV);
        recyclerView.setLayoutManager(linearLayoutManager);
        RVAdapter adapter = new RVAdapter(chartData, countryNames, countryStatus, this);
        recyclerView.setAdapter(adapter);
    }

    private String abbreviateNumber(int number){

        if(number <= 0) return String.valueOf(number);
        String SI_SYMBOL[] = {"", "k", "M", "G", "T", "P", "E"};
        int tier = (int) (Math.log10(number) / 3);
        if(tier <= 0) return String.valueOf(number);
        String suffix = SI_SYMBOL[tier];
        int scale = (int) Math.pow(10, tier * 3);
        int scaled = number / scale;
        return String.valueOf(scaled) + suffix;
    }
}

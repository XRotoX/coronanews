package com.inpt.coronapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;




public class RVAdapter extends RecyclerView.Adapter<RVAdapter.viewHolder> {

    private ArrayList<LineData> chartData = new ArrayList<>();
    private ArrayList<String> countryNames = new ArrayList<>();
    private ArrayList<String> countryStatus = new ArrayList<>();
    private Context mcontext;

    public RVAdapter(ArrayList<LineData> chartData, ArrayList<String> countryNames, ArrayList<String> countryStatus, Context mcontext) {
        this.chartData = chartData;
        this.countryNames = countryNames;
        this.countryStatus = countryStatus;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrolling_chart_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        int R=0, G=0, B=0;
        int rec_rounded_green_id = mcontext.getResources().getIdentifier("rec_rounded_green", "drawable", mcontext.getPackageName());
        int rec_rounded_orange_id = mcontext.getResources().getIdentifier("rec_rounded_orange", "drawable", mcontext.getPackageName());
        int rec_rounded_yellow_id = mcontext.getResources().getIdentifier("rec_rounded_yellow", "drawable", mcontext.getPackageName());
        int rec_rounded_red_id = mcontext.getResources().getIdentifier("rec_rounded_red", "drawable", mcontext.getPackageName());
        int rec_rounded_blue_id = mcontext.getResources().getIdentifier("rec_rounded_blue", "drawable", mcontext.getPackageName());

        if(position % 5 == 0){R=52; G=138; B=123; holder.chartParentLayout.setBackground(mcontext.getDrawable(rec_rounded_green_id));} //#348A7B
        else if(position % 5 == 1){R=255; G=131; B=103; holder.chartParentLayout.setBackground(mcontext.getDrawable(rec_rounded_orange_id));} //#FF8367
        else if(position % 5 == 2){R=253; G=210; B=98; holder.chartParentLayout.setBackground(mcontext.getDrawable(rec_rounded_yellow_id));} //#FDD262
        else if(position % 5 == 3){R=252; G=105; B=105; holder.chartParentLayout.setBackground(mcontext.getDrawable(rec_rounded_red_id));} //#FC6969
        if(position % 5 == 4){R=80; G=155; B=255; holder.chartParentLayout.setBackground(mcontext.getDrawable(rec_rounded_blue_id));} //#509BFF

        //rgb(52, 138, 123)
        //rgb(255, 131, 103)
        //rgb(253, 210, 98)
        //rgb(252, 105, 105)
        //rgb(80, 155, 255)

            holder.cubicChart.setViewPortOffsets(0, 0, 0, 0);
            holder.cubicChart.setBackgroundColor(Color.rgb(R, G, B));

                // no description text
            holder.cubicChart.getDescription().setEnabled(false);

                // enable touch gestures
            holder.cubicChart.setTouchEnabled(true);

                // enable scaling and dragging
            holder.cubicChart.setDragEnabled(true);
            holder.cubicChart.setScaleEnabled(true);

                // if disabled, scaling can be done on x- and y-axis separately
            holder.cubicChart.setPinchZoom(false);

            holder.cubicChart.setDrawGridBackground(false);
            holder.cubicChart.setMaxHighlightDistance(300);

            XAxis x = holder.cubicChart.getXAxis();
            x.setEnabled(false);

            YAxis y = holder.cubicChart.getAxisLeft();
            y.setEnabled(false);
            //y.setLabelCount(6, false);
            //y.setTextColor(Color.WHITE);
            //y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            //y.setDrawGridLines(false);
            //y.setAxisLineColor(Color.WHITE);

            holder.cubicChart.getAxisRight().setEnabled(false);

            holder.cubicChart.getLegend().setEnabled(false);

            holder.cubicChart.animateXY(1000, 1000);
            holder.cubicChart.setData(chartData.get(position));
            holder.cubicChart.invalidate();

            holder.countryNameTV.setText(String.valueOf(countryNames.get(position)));
            holder.statusTV.setText(String.valueOf(countryStatus.get(position)));
    }

    @Override
    public int getItemCount() {
        return chartData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        LinearLayout chartParentLayout;
        LineChart cubicChart;
        TextView countryNameTV, statusTV;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cubicChart = itemView.findViewById(R.id.cubicChart);
            chartParentLayout = itemView.findViewById(R.id.chartParentLayout);
            countryNameTV = itemView.findViewById(R.id.countryNameTV);
            statusTV = itemView.findViewById(R.id.statusTV);

        }
    }
}

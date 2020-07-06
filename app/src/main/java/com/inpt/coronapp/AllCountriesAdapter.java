package com.inpt.coronapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;




public class AllCountriesAdapter extends RecyclerView.Adapter<AllCountriesAdapter.viewHolder> {

    private ArrayList<String> casesCount = new ArrayList<>();
    private ArrayList<Bitmap> flags = new ArrayList<>();
    private ArrayList<String> countryNames = new ArrayList<>();
    private Context mcontext;

    public AllCountriesAdapter(ArrayList<String> casesCount, ArrayList<Bitmap> flags, ArrayList<String> countryNames, Context mcontext) {
        this.casesCount = casesCount;
        this.countryNames = countryNames;
        this.flags = flags;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        
        holder.flag.setImageBitmap(flags.get(position));
        holder.cases.setText("Total cases: " + casesCount.get(position));
        holder.countryNameTV.setText(countryNames.get(position));
    }

    @Override
    public int getItemCount() {
        return countryNames.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView flag;
        TextView countryNameTV, cases;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryNameTV = itemView.findViewById(R.id.countryName);
            cases = itemView.findViewById(R.id.cases);

        }
    }
}

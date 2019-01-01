package com.blinxwift.blinxweather.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blinxwift.blinxweather.Classes.Weather;
import com.blinxwift.blinxweather.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder>{

    //Variables
    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Weather> forecasts;


    //ViewHolder class
    public class WeeklyViewHolder extends RecyclerView.ViewHolder {

        //Variables to store the ViewHolder views
        public TextView dayTextView;
        public TextView tempMaxTextView;
        public TextView tempLowTextView;
        public ImageView weeklyWeatherIcon;


        //Constructor for WeeklyViewHolder
        public WeeklyViewHolder(View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.day);
            tempMaxTextView = itemView.findViewById(R.id.tempMax);
            tempLowTextView = itemView.findViewById(R.id.tempLow);
            weeklyWeatherIcon = itemView.findViewById(R.id.weatherIconWeekly);
        }
    }


    //WeeklyAdapter constructor
    public WeeklyAdapter(Context context, ArrayList<Weather> forecasts){
        //Initialize variables
        mInflater = LayoutInflater.from(context);
        this.forecasts = forecasts;
        this.mContext = context;
    }


    @NonNull
    @Override
    public WeeklyAdapter.WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate weekly card layout
        View mItemView = mInflater.inflate(R.layout.weekly_card_layout, parent, false);

        //return WeeklyViewHolder object
        return new WeeklyAdapter.WeeklyViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(WeeklyAdapter.WeeklyViewHolder holder, int position) {

        //Get current forecast
        Weather mCurrent = forecasts.get(position);
        //Set current day's name
        holder.dayTextView.setText(mCurrent.getDay());

        //Set currentIcon string
        String currentIcon = mCurrent.getIcon();
        //then set correct icon
        switch (currentIcon) {
            case "clear-day":
                Glide.with(mContext).load(R.drawable.clearday).into(holder.weeklyWeatherIcon);
                break;
            case "clear-night":
                Glide.with(mContext).load(R.drawable.clearnight).into(holder.weeklyWeatherIcon);
                break;
            case "cloudy":
                Glide.with(mContext).load(R.drawable.cloudy).into(holder.weeklyWeatherIcon);
                break;
            case "fog":
                Glide.with(mContext).load(R.drawable.fog).into(holder.weeklyWeatherIcon);
                break;
            case "partly-cloudy-day":
                Glide.with(mContext).load(R.drawable.partlycloudyday).into(holder.weeklyWeatherIcon);
                break;
            case "partly-cloudy-night":
                Glide.with(mContext).load(R.drawable.partlycloudyday).into(holder.weeklyWeatherIcon);
                break;
            case "rain":
                Glide.with(mContext).load(R.mipmap.rainicon).into(holder.weeklyWeatherIcon);
                break;
            case "sleet":
                Glide.with(mContext).load(R.drawable.sleet).into(holder.weeklyWeatherIcon);
                break;
            case "snow":
                Glide.with(mContext).load(R.drawable.snow).into(holder.weeklyWeatherIcon);

                break;
            case "wind":
                Glide.with(mContext).load(R.drawable.wind).into(holder.weeklyWeatherIcon);
                break;
            default:
                break;
        }

        //Set high and low temperatures and use correct degree symbol depending on user's saved selection in TempTypeSettings
        if(mCurrent.getTempSet() == true){
            holder.tempLowTextView.setText(mCurrent.getTempLow() + "°C");
            holder.tempMaxTextView.setText(mCurrent.getTempMax() + "°C");
        } else if(mCurrent.getTempSet() == false){
            holder.tempLowTextView.setText(mCurrent.getTempLow() + "°F");
            holder.tempMaxTextView.setText(mCurrent.getTempMax() + "°F");
        }


    }


    //Get size of ArrayList
    @Override
    public int getItemCount() {
        return forecasts.size();
    }
}

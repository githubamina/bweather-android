package com.blinxwift.blinxweather.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class ForecastAdapter extends  RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>{



    //Variables
    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Weather> forecasts;


    //Hourly Forecast ViewHolder class
    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        //Variables to store the ViewHolder views
        public TextView timeTextView;
        public TextView temperatureTextView;
        public ImageView iconImageView;


        //Constructor for Hourly ForecastViewHolder
        public ForecastViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.hourlyIcon);
            timeTextView = itemView.findViewById(R.id.forecastsTime);
            temperatureTextView = itemView.findViewById(R.id.hourTemp);
        }
    }


    //ForecastAdapter constructor
    public ForecastAdapter(Context context, ArrayList<Weather> forecasts){
        //Initialize variables
        mInflater = LayoutInflater.from(context);
        this.forecasts = forecasts;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate hourly card layout
        View mItemView = mInflater.inflate(R.layout.hourly_card_layout, parent, false);

        //return ForecastViewHolder object
        return new ForecastViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ForecastViewHolder holder, int position) {

        //Get current forecast
        Weather mCurrent = forecasts.get(position);
        //Set current hour
        holder.timeTextView.setText(mCurrent.getTime());

        //Set appropriate icon based on icon string
        String currentIcon = mCurrent.getIcon();
        switch (currentIcon) {
            case "clear-day":
                Glide.with(mContext).load(R.drawable.clearday).into(holder.iconImageView);
                break;
            case "clear-night":
                Glide.with(mContext).load(R.drawable.clearnight).into(holder.iconImageView);
                break;
            case "cloudy":
                Glide.with(mContext).load(R.drawable.cloudy).into(holder.iconImageView);
                break;
            case "fog":
                Glide.with(mContext).load(R.drawable.fog).into(holder.iconImageView);
                break;
            case "partly-cloudy-day":
                Glide.with(mContext).load(R.drawable.partlycloudyday).into(holder.iconImageView);
                break;
            case "partly-cloudy-night":
                Glide.with(mContext).load(R.drawable.partlycloudyday).into(holder.iconImageView);
                break;
            case "rain":
                Glide.with(mContext).load(R.mipmap.rainicon).into(holder.iconImageView);
                break;
            case "sleet":
                Glide.with(mContext).load(R.drawable.sleet).into(holder.iconImageView);
                break;
            case "snow":
                Glide.with(mContext).load(R.drawable.snow).into(holder.iconImageView);

                break;
            case "wind":
                Glide.with(mContext).load(R.drawable.wind).into(holder.iconImageView);
                break;
            default:
                break;

        }


        //Set temperature and use correct degree symbol depending on user's saved selection in TempTypeSettings
        boolean isCelsius = mCurrent.getTempSet();

        if (isCelsius == true){
            holder.temperatureTextView.setText(mCurrent.getTemperature() + " °C");
        }
        if (isCelsius == false){
            holder.temperatureTextView.setText(mCurrent.getTemperature() + " °F");
        }




    }





    @Override
    public int getItemCount() {
       return forecasts.size();
    }
}

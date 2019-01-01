package com.blinxwift.blinxweather.Classes;

import android.net.Uri;

import com.android.volley.RequestQueue;

import java.net.URL;
import java.sql.Time;

/**
 * Weather object to be used for hourly and weekly forecasts
 */

public class Weather {


    public String mSummary;
    public String mIcon;
    public int mTemperature;
    public String mTime;

    public String mDay;
    public int mTempMax;
    public int mTempLow;
    public boolean mDegrees;


    //Constructor for an hourly weather object
    public Weather(String icon, int temperature, String time, Boolean degrees){
        mIcon = icon;
        mTemperature = temperature;
        mTime = time;
        mDegrees = degrees;
    }



    //Constructor for a weekly weather object
    public Weather(String day, int tempMax, int tempLow, String icon, Boolean degrees){
        mDay = day;
        mTempMax = tempMax;
        mTempLow = tempLow;
        mIcon = icon;
        mDegrees = degrees;
    }



    //Get methods to get forecast values
    public String getSummary(){
        return mSummary;
    }

    public String getIcon(){
        return mIcon;
    }


    public String getTemperature(){
        return Integer.toString(mTemperature);
    }

    public String getTempMax(){
        return Integer.toString(mTempMax);
    }

    public String getTempLow(){
        return Integer.toString(mTempLow);
    }

    public String getDay(){
        return mDay;
    }

    public String getTime(){
        return mTime;
    }

    public Boolean getTempSet(){
        return mDegrees;
    }
}

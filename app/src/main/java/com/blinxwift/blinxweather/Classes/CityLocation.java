package com.blinxwift.blinxweather.Classes;

/**
 * CityLocation class to be used to hold user's searched for locations
 */
public class CityLocation {

    String cityName;

    String mCoords;

    public CityLocation(String name, String coords){
        cityName = name;
        mCoords = coords;
    }


    public String getCityName(){
        return cityName;
    }

    public String getmCoords(){
        return mCoords;
    }
}

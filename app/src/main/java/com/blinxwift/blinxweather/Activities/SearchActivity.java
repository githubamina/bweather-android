package com.blinxwift.blinxweather.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.blinxwift.blinxweather.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class SearchActivity extends AppCompatActivity {


    //String to store the coordinates from searched for location
    public String coords;


    //PlaceAutoCompleteFragment will be used to help users search for locations
    public PlaceAutocompleteFragment autocompleteFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        //Initialize autocompleteFragment
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                //Get coordinates from place object
                LatLng latlng = place.getLatLng();

                //Get latitude from LatLng object
                Double lat = latlng.latitude;

                //Get longitude from LatLng object
                Double lon = latlng.longitude;

                //Get name of location from place object
                String cityName = place.getName().toString();

                //Initialize coords to latlng string to use in API call
                coords = lat.toString() + "," + lon.toString();


                //Get shared preferences in order to store searched location and coords in saved locations, so user may access them later
                SharedPreferences mSettings =  SearchActivity.this.getSharedPreferences("Test", 0);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(cityName, coords);
                editor.apply();




                //Once user has successfully searched for a location, create an intent with coordinates and location name,
                //and then start MainActivity so the searched location's forecast can be displayed
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("searchCoords", coords);
                intent.putExtra("cityName", cityName);
                startActivity(intent);


            }

            @Override
            public void onError(Status status) {
            }

        });

    }

}

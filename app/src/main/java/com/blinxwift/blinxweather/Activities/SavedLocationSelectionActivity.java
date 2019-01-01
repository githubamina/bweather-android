package com.blinxwift.blinxweather.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.blinxwift.blinxweather.Adapters.LocationAdapter;
import com.blinxwift.blinxweather.Classes.CityLocation;
import com.blinxwift.blinxweather.R;
import java.util.ArrayList;
import java.util.Map;

public class SavedLocationSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location_selection);
        //Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.locations_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);



        //Get sharedPreferences so saved locations can be accessed
        SharedPreferences mSettings =  this.getSharedPreferences("Test", 0);


        //Create an ArrayList to store the coordinates of each location
        ArrayList<String> coords = new ArrayList<String>();


        //ArrayList to store CityLocation objects using info retrieved from shared preferences
        final ArrayList<CityLocation> cityLocations = new ArrayList<CityLocation>();

        //This code block iterates though all entries in mSettings shared preferences and retrieves each entry's
        //name and coordinates
        Map<String, ?> allEntries = mSettings.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            coords.add((String) entry.getValue());
            cityLocations.add(new CityLocation(entry.getKey(), ((String) entry.getValue()).toString()));
        }



        //Instantiate a LocationAdapter and pass in cityLocations ArrayList
        LocationAdapter locationAdapter = new LocationAdapter(this, cityLocations);


        //Instantiate a recyclerView to display cityLocations
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //Set the locationAdapter on mRecyclerView so that the cityLocations will be displayed
        mRecyclerView.setAdapter(locationAdapter);


        //This block of code makes it possible for users to delete unwanted locations by swiping them off the screen
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((String) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(mRecyclerView);

        //Set linearlayoutmanager on the recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SavedLocationSelectionActivity.this));



    }


    //Function to remove cityLocation items when user swipes them off screen (used in ItemTouchHelper above)
    private void removeItem(String cityName){
        SharedPreferences mSettings =  this.getSharedPreferences("Test", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.remove(cityName);
        editor.apply();
    }

}

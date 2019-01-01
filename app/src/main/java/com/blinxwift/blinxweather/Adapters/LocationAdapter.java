package com.blinxwift.blinxweather.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blinxwift.blinxweather.Activities.MainActivity;
import com.blinxwift.blinxweather.Classes.CityLocation;
import com.blinxwift.blinxweather.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{


    //Variables

    private ArrayList<CityLocation> mLocationList;

    private final LayoutInflater mInflater;

    private Context mContext;






    //LocationViewHolder to store location name textview
    class LocationViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView cityTextView;

        final LocationAdapter mAdapter;


        public LocationViewHolder(View itemView, LocationAdapter adapter){
            super(itemView);

            //Initialize city name textview
            cityTextView = (TextView) itemView.findViewById(R.id.city);

            this.mAdapter = adapter;

        }

    }



    //LocationAdapter constructor
    public LocationAdapter(Context context, ArrayList<CityLocation> locationList){
        mInflater = LayoutInflater.from(context);
        this.mLocationList = locationList;

        this.mContext = context;

    }


    /**
     *Inflates view holder layout
     */
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate location card layout
        View mItemView = mInflater.inflate(R.layout.location_card_layout, parent, false);


        //Initialize LocationViewHolder to mItemView
        final LocationViewHolder mLocationViewHolder = new LocationViewHolder(mItemView, this);


        //return LocationViewHolder object
        return mLocationViewHolder;
    }


    /**
     *Sets current location item + data to current view holder
     */
    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        //Get current location
        final CityLocation mCurrent = mLocationList.get(position);
        //Set location name
        holder.cityTextView.setText(mCurrent.getCityName());

        //set tag (which will be used if user deletes location)
        holder.itemView.setTag(mCurrent.getCityName());


        //When user taps a location, create intent with location name and coords
        //then open MainActivity so selected location's forecast can be displayed
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("cityName", mCurrent.getCityName());
                intent.putExtra("coords", mCurrent.getmCoords());
                mContext.startActivity(intent);


            }
        });

    }


    /**
     * Returns the number of items in the location list
     */
    @Override
    public int getItemCount() {
        return mLocationList.size();
    }




}
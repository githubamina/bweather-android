package com.blinxwift.blinxweather.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blinxwift.blinxweather.Adapters.ForecastAdapter;
import com.blinxwift.blinxweather.Adapters.WeeklyAdapter;
import com.blinxwift.blinxweather.Classes.VolleyErrorHelperClass;
import com.blinxwift.blinxweather.Classes.Weather;
import com.blinxwift.blinxweather.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {



    public ScrollView mainScroll;

    public ArrayList<Weather> hourlyForecasts;

    public ArrayList<Weather> weeklyForecasts;

    private RequestQueue mRequestQueue;

    public TextView hourlyDayText;


    public String basePath;


    public String summary;

    public Double temp;


    public String userCoords;

    public String searchCoords;
    public String url;

    public String windDirection;

    Bundle extras;

    private FusedLocationProviderClient mFusedLocationClient;


    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;

    //To keep track of whether location is currently being requested
    public Boolean mRequestingLocationUpdates;

    public TextView currentForecast;

    public TextView currentDescription;


    public TextView sunriseText;

    public TextView sunsetText;

    public TextView realFeelText;

    public TextView humidityText;

    public TextView visibilityText;

    public TextView pressureText;

    public TextView windText;


    public TextView detailedSumText;

    public LinearLayout precipSection;

    public LinearLayout uvSection;


    public LinearLayout skinSection;

    String precipitationType;


    public TextView precipSuggestion;


    public TextView uvSuggestion;

    public String backgroundCurrentColor;

    public Boolean isCelsius;


    public TextView skinSuggestion;

    public TextView currentLocation;

    public Double lat;

    public Double lon;

    public String currentLoc;

    SharedPreferences tempTypeSettings;

    public ImageView pipImage;

    public ImageView secondImage;


    public ImageView mainImage;


    public ProgressBar progressBar;

    public LinearLayout mainLayout;

    public TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set activity_main as layout
        setContentView(R.layout.activity_main);

        //Setup custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        /**
         * @param tempTypeSettings is an instance of SharedPreferences
         * tempTypeSettings is being initialised to the SharedPreferences 'tempType'
         * in which the user's current selection (or the default selection, if the user has not selected)
         * for which unit of temperature they prefer is stored
         */
        tempTypeSettings = this.getSharedPreferences("tempType", 0);

        /**
         * @param isCelsius is a boolean value which stores the value retrieved from SharedPreferences
         * which tells us whether a user currently prefers Celsius or Farenheit
         */
        isCelsius = tempTypeSettings.getBoolean("tempType", true);


        /**
         * @param currentLoc is a String which will be used below to store the user's current location
         */
        currentLoc = "";


        /**
         * Here, all our views and layouts are being initialized so that we may use them in our code
         */

        //ImageViews
        mainImage = findViewById(R.id.mainImage);
        secondImage = findViewById(R.id.secondImage);
        pipImage = findViewById(R.id.pipImage);

        //TextViews
        errorText = findViewById(R.id.error_text);
        currentLocation = findViewById(R.id.currentLocation);
        currentForecast = findViewById(R.id.currentForecast);
        currentDescription = findViewById(R.id.currentDescription);
        sunriseText = findViewById(R.id.sunriseTime);
        sunsetText = findViewById(R.id.sunsetTime);
        realFeelText = findViewById(R.id.realFeel);
        humidityText = findViewById(R.id.humidity);
        visibilityText = findViewById(R.id.visibility);
        pressureText = findViewById(R.id.pressure);
        windText = findViewById(R.id.wind);
        hourlyDayText = findViewById(R.id.hourlyDay);
        detailedSumText = findViewById(R.id.detailedSummary);
        precipSuggestion = findViewById(R.id.precipSuggestion);
        uvSuggestion = findViewById(R.id.uvsuggestion);
        skinSuggestion = findViewById(R.id.skinSuggestion);

        skinSuggestion.setFocusable(false);
        uvSuggestion.setFocusable(false);
        precipSuggestion.setFocusable(false);





        //Other views/layouts
        progressBar = findViewById(R.id.progress_bar);
        mainLayout = findViewById(R.id.mainLayout);
        mainScroll = findViewById(R.id.mainScroll);
        precipSection = findViewById(R.id.precipitationSection);
        uvSection = findViewById(R.id.uvSection);
        skinSection = findViewById(R.id.skinSection);


        skinSection.setFocusable(false);
        precipSection.setFocusable(false);
        uvSection.setFocusable(false);


        mainScroll.smoothScrollTo(0,0);
        mainScroll.setFocusable(true);
        mainScroll.setFocusableInTouchMode(true);
        mainScroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        progressBar.setVisibility(View.VISIBLE);



        createLocationRequest();


        /**
         * First use getIntent to try and store whatever (if any) intent launched this activity
         * Then get extras from intent if any exist, to check if the user has searched for a new location
         * or selected one from their saved cities
         * If extras exist, assign
         * @param currentLoc to cityName extra (user's selected location)
         * and also set the
         * @param currentLocation textView text to be currentLoc (user's selected location)
         * If no extras exist, assign
         * @param currentLoc to empty String
         */
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            extras = intent.getExtras();
            try {
                currentLoc = extras.getString("cityName");
                currentLocation.setText(currentLoc);
            } catch (NullPointerException e) {
                currentLoc = "";
            }


            /**
             * If cityName is null, use latitude and longitude with Geocoder to get cityName
             */
            if (currentLoc == "") {
                try {
                    lat = extras.getDouble("lat");
                } catch (NullPointerException e) {
                    lat = 0.0;
                }
                try {
                    lon = extras.getDouble("lon");
                } catch (NullPointerException e) {
                    lon = 0.0;
                }


                List<Address> addresses = null;
                Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addresses = gcd.getFromLocation(lat, lon, 1);
                    currentLoc = addresses.get(0).getLocality();
                    currentLocation.setText(currentLoc);

                } catch (IOException e) {
                    currentLoc = "";
                    return;
                }

            }


            /**
             * @param searchCoords is used in API request as location (if it is not null)
             */
            if (extras.getString("searchCoords") != null) {
                //if there are extras from a search, initialize searchCoords to those coords
                searchCoords = extras.getString("searchCoords");
            } else {
                //if there are extras from a saved city selection, initialize searchCoords to those coords
                searchCoords = extras.getString("coords");
            }
        } else {
            //if there are no extras, searchCoords is given a null value
            searchCoords = null;
        }


        //set bool that keeps track of whether or not locationupdates are being requested to false
        mRequestingLocationUpdates = false;


        //create new requestqueue for api request
        mRequestQueue = Volley.newRequestQueue(this);


        //base url for api request- location coords need to be added
        basePath = "https://api.darksky.net/forecast/853e0090aa3ea5b6df7fbe4fd10a9581/";


        /**
         * The following code is the API request to DarkSky IF the user has selected or searched for a location.
         * If not, then the code below this for user's current location will be used.
         */

        //if there are coords set from extras:
        if (searchCoords != null) {

            //add coords to api request url
            url = basePath + searchCoords;


            //JSONRequest to get and set data
            getDataForCurrentLocation();

        }

        //IF no coords have been passed in from a user search or selection, the location/coords must be found by accessing the user's
        //last known/current location:
        else {

            //Initialize mFusedLocationClient to FusedLocationProviderClient so we can get the user's location


            //Check if the user has granted permissions for the app to access either fine or coarse location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //If permissions have not been granted, call requestPermissions to request the user to allow permissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            } else {

                //callback for when user's current location is updated
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        //if no locationresult, return early
                        if (locationResult == null) {
                            return;
                        }


                        //for (Location location : locationResult.getLocations()) {

                        List<Location> locations = locationResult.getLocations();

                        Location location = locations.get(0);



                        //Assign global userCoords variable to the string value of longitude and latitude from userLocation
                        userCoords = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

                        //update api request url with the current coords
                        url = basePath + userCoords;

                        lat = location.getLatitude();

                        lon = location.getLongitude();

                        Log.d("HELLO", "LOCATION IS    "  + lat + " " + lon);

                        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(lat, lon, 1);
                            if (addresses.size() > 0) {
                                currentLoc = addresses.get(0).getLocality();
                                currentLocation.setText(currentLoc);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setTitle("Error getting current location");

                            builder.setMessage(VolleyErrorHelperClass.getMessage(e, MainActivity.this));

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog errorDialog = builder.create();

                            errorDialog.show();

                            mainLayout.setVisibility(View.GONE);

                            progressBar.setVisibility(View.GONE);

                            errorText.setVisibility(View.VISIBLE);
                        }

                      getDataForCurrentLocation();

                    }
                };


                createLocationRequest();
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        }
    }










    }



    //inflate navbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.my_cities:
                Intent cityintent = new Intent(MainActivity.this, SavedLocationSelectionActivity.class);
                startActivity(cityintent);
                return true;
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.temp_type:
                Intent tempUnitIntent = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(tempUnitIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create location request will be called when a new location request is needed
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    private void startLocationUpdates() {
        //return early if location permissions are disabled
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            //requestlocation updates and set tracking bool to true

            createLocationRequest();
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mRequestingLocationUpdates = true;
        }
    }


    private void stopLocationUpdates() {
        //remove location updates and set tracking bool to false
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        //get extras to check if user has just searched for a new location
        //or if user has selected a different location from their saved cities
        extras = getIntent().getExtras();

        if (extras != null) {
            if(extras.getString("searchCoords") != null) {
                //if there are extras from a search, initialize searchCoords to those coords
                searchCoords = extras.getString("searchCoords");
            } else {
                //if there are extras from a saved city selection, initialize searchCoords to those coords
                searchCoords = extras.getString("coords");
            }
        } else {
            //if there are no extras, searchCoords is given a null value
            if((!mRequestingLocationUpdates) && (searchCoords != null)){
                //on resume, if location update tracking bool is false, call startLocationUpdates to start requesting location updates
                startLocationUpdates();
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop location updates onpause
        if((mRequestingLocationUpdates) && (searchCoords != null)){
            //on resume, if location update tracking bool is false, call startLocationUpdates to start requesting location updates
            stopLocationUpdates();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //Check if permission has been granted
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //If no permissions have been granted:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               //just return
                return;
            } else {

                //callback for when user's current location is updated
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        //if no locationresult, return early
                        if (locationResult == null) {
                            return;
                        }


                        //for (Location location : locationResult.getLocations()) {

                        List<Location> locations = locationResult.getLocations();

                        Location location = locations.get(0);


                        //Assign global userCoords variable to the string value of longitude and latitude from userLocation
                        userCoords = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

                        //update api request url with the current coords
                        url = basePath + userCoords;

                        lat = location.getLatitude();

                        lon = location.getLongitude();

                        Log.d("HELLO", "LOCATION IS    " + lat + " " + lon);

                        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(lat, lon, 1);
                            if (addresses.size() > 0) {
                                currentLoc = addresses.get(0).getLocality();
                                currentLocation.setText(currentLoc);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setTitle("Error getting current location");

                            builder.setMessage(VolleyErrorHelperClass.getMessage(e, MainActivity.this));

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog errorDialog = builder.create();

                            errorDialog.show();

                            mainLayout.setVisibility(View.GONE);

                            progressBar.setVisibility(View.GONE);

                            errorText.setVisibility(View.VISIBLE);
                        }

                      getDataForCurrentLocation();
                    }};}

                createLocationRequest();
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


        } else {
            //dialog- location permissions have been disabled. unable to retrieve weather without location.
            userCoords = null;

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Location Permissions Disabled");

            builder.setMessage("In order to use BlinxWeather App, please enable location permissions.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog errorDialog = builder.create();

            errorDialog.show();

            mainLayout.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);

            errorText.setVisibility(View.VISIBLE);

        }

    }


    public void getDataForCurrentLocation(){
        //new jsonobjectrequest to get weather json
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //if there is a successful response:
                try {

                    /**
                     * @param errorText is a TextView which will be displayed if there is an error
                     * since this code will run after a successful response, errorText will be hidden
                     * @param progressBar is a ProgressBar which will be displayed while data is being fetched from the DarkSky server
                     *                    since this code will run after data has been successfully retrieved, progressBar will be hidden
                     * @param mainLayout is where the main weather content is contained
                     *                   since this code will run after a successful response from DarkSky, the weather data should be available
                     *                   to be displayed, so mainLayout will be made visible
                     */
                    errorText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                            mainScroll.fullScroll(ScrollView.FOCUS_UP);



                    /**
                     * @param hourlyForecasts is an ArrayList which will hold the hourly temperatures/icons for the day
                     *                        and be used with forecastAdapter to populate the recyclerview in activity_main
                     *                        with the hourly forecasts
                     * @param weeklyForecasts is an ArrayList which will hold the weekly temperatures/icons for the week
                     *                        and be used with weeklyAdapter to populate the recyclerview in activity_main
                     *                        with the weekly forecasts
                     */
                    hourlyForecasts = new ArrayList<>();
                    weeklyForecasts = new ArrayList<>();


                    /**
                     * @param timeZoneID will get the current timezone from the DarkSky request
                     * @param timeZone will store the timeZoneID in a TimeZone object
                     *                 so that we can get the current time in the user's chosen location
                     */
                    String timeZoneID = response.getString("timezone");
                    TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);


                    /**
                     * @param currentObject
                     * @param hourlyJSON
                     * @param dailyObject
                     * store the JSON responses for current, hourly and weekly weather data, respectively
                     */
                    JSONObject currentObject = response.getJSONObject("currently");
                    JSONObject hourlyJSON = response.getJSONObject("hourly");
                    JSONObject dailyObject = response.getJSONObject("daily");


                    /**
                     * @param hourlyData holds a JSONArray of hourly weather data for the day
                     * @param data holds a JSONArray of weather data
                     * @param todayForecast is a JSONObject holding the weather data for the current time
                     */
                    JSONArray hourlyData = hourlyJSON.getJSONArray("data");
                    JSONArray data = dailyObject.getJSONArray("data");
                    JSONObject todayForecast = data.getJSONObject(0);


                    //SET CURRENT TIME'S TEXTVIEWS + IMAGEVIEWS


                    /**
                     * @param sunrise
                     * @param sunset
                     * store the current day's sunrise and sunset times in unix time
                     */
                    int sunrise = todayForecast.getInt("sunriseTime");
                    long sunset = todayForecast.getInt("sunsetTime");
                    long unixSeconds = todayForecast.getLong("time");

                    /**
                     * @param date is used to store current time/date from API response in milliseconds format
                     * @param sdf holds the DateFormat we want the time/date to be formatted as
                     *            (which in this case, we want it to display the name of the day)
                     * @param day holds the formatted date in day format
                     * @param hourlyDayText displays the formatted date (day) above the recyclerview displaying hourly forecasts for the day
                     */
                    Date date = new java.util.Date(unixSeconds * 1000L);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE");
                    sdf.setTimeZone(timeZone);
                    String day = sdf.format(date);
                    hourlyDayText.setText(day);


                    /**
                     * @param currentSum holds the current forecast summary for current weather
                     * @param detailedSumText is the textview below the main weather summary which will
                     *                       display the current detailed forecast summary- currentSum
                     */
                    String currentSum = hourlyJSON.getString("summary");
                    detailedSumText.setText(currentSum);


                    /**
                     * @param sunriseVal is used to store current sunrise time in milliseconds format
                     * @param timeFormat holds the DateFormat we want the time/date to be formatted as
                     *            (which in this case, we want it to display like a digital clock- hours and mintues with am and pm)
                     * @param sunriseTime holds the formatted time in digital clock format
                     * @param sunriseText displays the formatted time in the TextView displaying the current day's sunrise time
                     * @param sunsetVal is used to store the current sunset time in milliseconds
                     * @param sunsetTime holds the formatted sunset time in digital clock format
                     * @param sunsetText displays the formatted time in the TextView displaying the current day's sunset time
                     */
                    Date sunriseVal = new java.util.Date(sunrise * 1000L);
                    SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("h:mm a");
                    timeFormat.setTimeZone(timeZone);
                    String sunriseTime = timeFormat.format(sunriseVal);
                    sunriseText.setText(sunriseTime);
                    Date sunsetVal = new java.util.Date(sunset * 1000L);
                    String sunsetTime = timeFormat.format(sunsetVal);
                    sunsetText.setText(sunsetTime);


                    /**
                     * @param tsLong stores the current time converted to milliseconds
                     *
                     */
                    Long tsLong = System.currentTimeMillis() / 1000;


                    /**
                     * Check if current time is before after sunset or before sunrise
                     *  if so, change background colour of screen to dark/night background
                     */
                    if (tsLong > sunset || tsLong < sunrise) {
                        mainLayout.setBackgroundColor(getResources().getColor(R.color.night));
                    }


                    /**
                     * @param todayIcon gets the current day's weather icon from todayForecast json object
                     */
                    String todayIcon = todayForecast.getString("icon");

                    /**
                     * Use switch on todayIcon to determine which icon to use for today's main imageView
                     */
                    switch (todayIcon) {
                        case "cloudy":
                            mainImage.setImageResource(R.mipmap.cloud2);
                            ObjectAnimator cloudyAnimation = ObjectAnimator.ofFloat(mainImage, "translationX", 100f);
                            cloudyAnimation.setDuration(2000);
                            cloudyAnimation.start();
                            break;
                        case "fog":
                            mainImage.setImageResource(R.mipmap.fog);
                            ObjectAnimator fogAnimation = ObjectAnimator.ofFloat(mainImage, "translationX", 60f);
                            fogAnimation.setDuration(2000);
                            fogAnimation.start();
                            break;
                        case "sleet":
                            mainImage.setImageResource(R.mipmap.sleet);
                            ObjectAnimator sleetAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", 100f);
                            sleetAnimation.setDuration(2000);
                            sleetAnimation.start();
                            break;
                        case "snow":
                            mainImage.setImageResource(R.mipmap.snow);
                            ObjectAnimator snowAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", 100f);
                            snowAnimation.setDuration(2000);
                            snowAnimation.start();
                            break;
                        case "rain":
                            mainImage.setImageResource(R.mipmap.rain);
                            ObjectAnimator rainAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", 100f);
                            rainAnimation.setDuration(2000);
                            rainAnimation.start();
                            break;
                        case "partly-cloudy-day":
                            secondImage.setImageResource(R.mipmap.cloudtrans1);
                            if (tsLong > sunset || tsLong < sunrise) {
                                mainImage.setImageResource(R.mipmap.moon);
                                ObjectAnimator moonAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", 100f);
                                moonAnimation.setDuration(2000);
                                moonAnimation.start();
                            } else {
                                mainImage.setImageResource(R.mipmap.sun);
                                ObjectAnimator sunAnimation1 = ObjectAnimator.ofFloat(mainImage, "translationY", -100f);
                                sunAnimation1.setDuration(2000);
                                sunAnimation1.start();
                            }
                            ObjectAnimator cloudAnimation = ObjectAnimator.ofFloat(secondImage, "translationX", 100f);
                            cloudAnimation.setDuration(2000);
                            cloudAnimation.start();
                            break;
                        case "clear-day":
                            if (tsLong > sunset || tsLong < sunrise) {
                                mainImage.setImageResource(R.mipmap.moon);
                                ObjectAnimator moonAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", -100f);
                                moonAnimation.setDuration(2000);
                                moonAnimation.start();
                            } else {
                                mainImage.setImageResource(R.mipmap.sun);
                                ObjectAnimator sunAnimation = ObjectAnimator.ofFloat(mainImage, "translationY", -100f);
                                sunAnimation.setDuration(2000);
                                sunAnimation.start();
                            }
                            break;
                        case "clear-night":
                            mainImage.setImageResource(R.mipmap.moon);
                            ObjectAnimator moonAnimation = ObjectAnimator.ofFloat(mainImage, "translationX", 100f);
                            moonAnimation.setDuration(2000);
                            moonAnimation.start();
                            break;
                        case "partly-cloudy-night":
                            mainImage.setImageResource(R.mipmap.moon);
                            secondImage.setImageResource(R.mipmap.cloudtrans1);
                            ObjectAnimator moonAnimation2 = ObjectAnimator.ofFloat(mainImage, "translationY", 100f);
                            moonAnimation2.setDuration(2000);
                            moonAnimation2.start();
                            ObjectAnimator animation = ObjectAnimator.ofFloat(secondImage, "translationX", -50f);
                            animation.setDuration(2000);
                            animation.start();
                            break;
                        case "wind":
                            mainImage.setImageResource(R.mipmap.wind);
                            ObjectAnimator windAnimation = ObjectAnimator.ofFloat(mainImage, "translationX", 20f);
                            windAnimation.setDuration(2000);
                            windAnimation.start();
                            break;
                        default:
                            break;
                    }


                    //GET DATA FOR WEEKLY FORECAST

                    for (int i = 1; i < 8; i++) {
                        /**
                         * @param currentforecast gets the current iteration's forecast data
                         *                        and then
                         * @param dailyIcon
                         * @param tempMax
                         * @param tempLow
                         * @param uSeconds
                         *                  are assigned the appropriate values retireved from currentForecast jsonobject
                         *
                         *
                         */
                        JSONObject currentforecast = data.getJSONObject(i);
                        String dailyIcon = currentforecast.getString("icon");
                        Double tempMax = currentforecast.getDouble("temperatureHigh");
                        Double tempLow = currentforecast.getDouble("temperatureLow");
                        long uSeconds = currentforecast.getLong("time");


                        /**
                         * format uSeconds into the current iteration's day so that the day can be displayed in the
                         * weekly forecast recyclerview
                         */
                        Date currentdate = new java.util.Date(uSeconds * 1000L);
                        SimpleDateFormat simpledf = new java.text.SimpleDateFormat("EEEE");
                        simpledf.setTimeZone(timeZone);
                        String currentday = simpledf.format(currentdate);


                        /**
                         * @param realTempMax
                         * @param realTempLow
                         * will store the high and low temperatures for each day/iteration
                         * but we need to check if the user has celsius or farenhait mode selected first
                         * using isCelsius bool.
                         */
                        int realTempMax;
                        int realTempLow;

                        if (isCelsius == true) {
                            Double celsTempMax = (tempMax - 32) * 5 / 9;

                            Double celsTempLow = (tempLow - 32) * 5 / 9;

                            realTempMax = (int) Math.rint(celsTempMax);

                            realTempLow = (int) Math.rint(celsTempLow);
                        } else {
                            realTempMax = (int) Math.rint(tempMax);

                            realTempLow = (int) Math.rint(tempLow);
                        }

                        weeklyForecasts.add(new Weather(currentday, realTempMax, realTempLow, dailyIcon, isCelsius));


                    }


                    //GET DATA FOR HOURLY FORECAST

                    for (int i = 0; i < 24; i++) {
                        /**
                         * @param hourlyforecast gets the current iteration's forecast data
                         *                        and then
                         * @param temp
                         * @param uSeconds
                         *                  are assigned the appropriate values retireved from hourlyForecast jsonobject
                         */
                        JSONObject hourlyforecast = hourlyData.getJSONObject(i);
                        temp = hourlyforecast.getDouble("temperature");
                        long uSeconds = hourlyforecast.getLong("time");


                        /**
                         * format uSeconds into the current iteration's hour so that the next 24 hours can be displayed in the
                         * hourly Forecast recyclerview
                         */
                        Date currentdate = new java.util.Date(uSeconds * 1000L);
                        SimpleDateFormat simpledf = new java.text.SimpleDateFormat("h a");
                        simpledf.setTimeZone(timeZone);
                        String formattedDate = simpledf.format(currentdate);


                        /**
                         * @param realTemp
                         * will store the high and low temperatures for each hour/iteration
                         * but we need to check if the user has celsius or farenheit mode selected first
                         * using isCelsius bool.
                         */
                        int realTemp;
                        if (isCelsius == true) {
                            Double celsTemp = (temp - 32) * 5 / 9;
                            realTemp = (int) Math.rint(celsTemp);
                        } else {
                            realTemp = (int) Math.rint(temp);
                        }


                        hourlyForecasts.add(new Weather(hourlyforecast.getString("icon"), realTemp, formattedDate, isCelsius));

                    }

                    /**
                     * Initialize recyclerview for Hourly Forecasts and set forecastAdapter and linearLayoutManager on it
                     */
                    ForecastAdapter forecastAdapter = new ForecastAdapter(MainActivity.this, hourlyForecasts);

                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.hourlyRecycler);
                    mRecyclerView.setFocusable(false);
                    mRecyclerView.setAdapter(forecastAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

                    /**
                     * Initialize recyclerview for Weekly Forecasts and set weeklyAdapter and linearRecyclerLM on it
                     */
                    WeeklyAdapter weeklyAdapter = new WeeklyAdapter(MainActivity.this, weeklyForecasts);
                    RecyclerView mWeeklyRecycler = (RecyclerView) findViewById(R.id.weeklyRecycler);
                    mWeeklyRecycler.setFocusable(false);

                    mWeeklyRecycler.setAdapter(weeklyAdapter);
                    LinearLayoutManager linearRecyclerLM = new LinearLayoutManager(MainActivity.this);
                    mWeeklyRecycler.setLayoutManager(linearRecyclerLM);


                    summary = currentObject.getString("summary");
                    temp = currentObject.getDouble("temperature");


                    //GET DETAILED INFO FOR CURRENT WEATHER AND SET TEXTVIEWS WITH IT


                    /**
                     * Get all values from currentObject jsonObject needed for detailed info textviews
                     */
                    String precipType = currentObject.optString("precipType");
                    Double realFeel = currentObject.getDouble("apparentTemperature");
                    Double humidity = currentObject.getDouble("humidity");
                    Double pressure = currentObject.getDouble("pressure");
                    Double visibility = currentObject.getDouble("visibility");
                    int uvIndex = currentObject.getInt("uvIndex");
                    Double precipProb = currentObject.getDouble("precipProbability");
                    Double windSpeed = currentObject.optDouble("windSpeed");
                    int windBearing = currentObject.optInt("windBearing");


                    /**
                     * @param realFeelCels is realFeel temp converted to cels
                     * using isCelsius bool we check if user has celsius or farenheit mode selected and assign
                     * @param realFeelText accordingly
                     */
                    Double realFeelCels = (realFeel - 32) * 5 / 9;
                    if (isCelsius == true) {
                        int realFeelTemp = (int) Math.rint(realFeelCels);
                        realFeelText.setText(realFeelTemp + "°C");
                    } else {
                        int realFeelTemp = (int) Math.rint(realFeel);
                        realFeelText.setText(realFeelTemp + "°F");
                    }


                    /**
                     * @param realHumidity is humidity converted to a percentage
                     * @param endHumidity is humidity converted to an int without decimal places
                     * @param humidityText is then set with proper humidity and a percentage symbol
                     */
                    Double realHumidity = humidity * 100;
                    int endHumidity = (int) Math.rint(realHumidity);
                    humidityText.setText(endHumidity + "%");


                    /**
                     * @param realPressure is pressure converted to a whole number rather than a decimal
                     * @param pressureText is then set with proper pressure number and hPa
                     */
                    int realPressure = (int) Math.rint(pressure);
                    pressureText.setText(realPressure + " hPa");


                    /**
                     * @param visKM is visibility converted to KM
                     * @param visibilityKM is viskM converted to a whole number
                     * @param visibilityText is then set with visibilityKM and km symbol
                     */
                    Double visKM = visibility * 1.60934;
                    int visibilityKM = (int) Math.rint(visKM);
                    visibilityText.setText(visibilityKM + "km");


                    /**
                     * if
                     * @param precipType retrieved from currentObject jsonObject is not null,
                     * then we n we can check the probability precipType and assign
                     * @param precipSuggestion an appropriate String to display
                     */
                    if (precipType != null) {
                        if (precipProb > 0 && precipProb < 0.4) {
                            switch (precipType) {
                                case "rain":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a slight chance of " + precipType + " today. If you are going out, you might want to take an umbrella or rain poncho just in case!");
                                    break;
                                case "snow":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a slight chance of " + precipType + " today. If you are going out, you might want to take a waterproof jacket just in case!");
                                    break;
                                case "sleet":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a slight chance of " + precipType + " today. If you are going out, you might want to take an umbrella or waterproof jacket just in case!");
                                    break;
                                default:
                                    break;
                            }

                        } else if (precipProb >= 0.4 && precipProb < 0.7) {
                            switch (precipType) {
                                case "rain":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a good chance of " + precipType + " today. You might want to take an umbrella or rain poncho with you if you go out.");
                                    break;
                                case "snow":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a good chance of " + precipType + " today. You might want to take a waterproof jacket with you if you go out.");
                                    break;
                                case "sleet":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a good chance of " + precipType + " today. You might want to take an umbrella or waterproof jacket with you if you go out.");
                                    break;
                                default:
                                    break;
                            }


                        } else if (precipProb >= 0.7 && precipProb < 1) {
                            switch (precipType) {
                                case "rain":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a high chance of " + precipType + " today. You should take an umbrella or rain poncho with you if you are heading outdoors.");
                                    break;
                                case "snow":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a high chance of " + precipType + " today. You should wear waterproof clothing (e.g. jacket and shoes) if you are going outside.");
                                    break;
                                case "sleet":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a high chance of " + precipType + " today. You should take an umbrella or waterproof jacket with you if you are heading outside.");
                                    break;
                                default:
                                    break;
                            }

                        } else if (precipProb == 1) {
                            switch (precipType) {
                                case "rain":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a definite chance of " + precipType + " today. You will need an umbrella or rain jacket when going outside.");
                                    break;
                                case "snow":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a definite chance of " + precipType + " today. You will need waterproof clothing (e.g. jacket and shoes) when going outside.");
                                    break;
                                case "sleet":
                                    precipSection.setVisibility(View.VISIBLE);
                                    precipSuggestion.setText("There is a definite chance of " + precipType + " today. You will need an umbrella or a hooded waterproof jacket when going outside.");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }


                    /**
                     * Check value of
                     * @param uvIndex
                     * to determine appropriat text for
                     * @param uvSuggestion to display
                     */
                    switch (uvIndex) {
                        case 1:
                        case 2:
                            uvSection.setVisibility(View.VISIBLE);
                            uvSuggestion.setText("The UV Index is a low " + uvIndex + " today. You'll only need sun protection if you're outside for extended periods of time.");
                            break;
                        case 3:
                        case 4:
                        case 5:
                            uvSection.setVisibility(View.VISIBLE);
                            uvSuggestion.setText("The UV Index is a mild " + uvIndex + " today. You may want to wear a hat and sunglasses and apply a light sunscreen throughout the day. Try to stay in the shade around 12pm as this is when UV rays will be strongest.");
                            break;
                        case 6:
                        case 7:
                            uvSection.setVisibility(View.VISIBLE);
                            uvSuggestion.setText("The UV Index is a high " + uvIndex + " today. Try and reduce your sun exposure between 10am and 4pm. Wear a hat, sunglasses, and long sleeves to stay protected. Apply sunscreen every 2 hours.");
                            break;
                        case 8:
                        case 9:
                        case 10:
                            uvSection.setVisibility(View.VISIBLE);
                            uvSuggestion.setText("The UV Index is very high today at " + uvIndex + " today. To stay protected, you should wear a hat, sunglasses, and long sleeves, and regularly apply sunscreen every 2 hours. Avoid being outdoors for extended periods, especially between 10am and 4pm.");
                            break;
                        case 11:
                            uvSection.setVisibility(View.VISIBLE);
                            uvSuggestion.setText("The UV Index is at an extreme today at " + uvIndex + ". You must try and avoid sun exposure between 10am and 4pm. Wear a hat, sunglasses and long sleeves, and apply sunscreen every 2 hours. If left unprotected, there is an extreme risk of skin and eyes being damaged.");
                            break;
                        default:
                            break;

                    }


                    /**
                     * set current weather summary as text of
                     * @param currentDescription
                     */
                    currentDescription.setText(summary);


                    /**
                     * @param celsTemp is current temperature in celsius
                     * @param realTemp will store the current temperature (in either farenheit or celsius) as a whole number
                     * isCelsius is used to check if the user currently has celsius or farenheit selected and
                     *                 then the appropriate temperature is set as text for currentForecast TextView
                     */
                    Double celsTemp = (temp - 32) * 5 / 9;
                    int realTemp;
                    if (isCelsius == true) {
                        realTemp = (int) Math.rint(celsTemp);
                        currentForecast.setText(Integer.toString(realTemp) + " °C");
                    } else {
                        realTemp = (int) Math.rint(temp);
                        currentForecast.setText(Integer.toString(realTemp) + " °F");
                    }


                    /**
                     * @param pipAnimation will store the appropriate pip animation to display, given the current temperature
                     *                     we use
                     * @param celsTemp to determine appropriate animation to display simply because i don't understand farenheit lol
                     */
                    AnimationDrawable pipAnimation;
                    if (celsTemp <= 0) {
                        pipImage.setBackgroundResource(R.drawable.extremec_animation);
                        pipAnimation = (AnimationDrawable) pipImage.getBackground();
                        pipAnimation.start();
                    } else if (celsTemp < 20 && celsTemp > 0) {
                        pipImage.setBackgroundResource(R.drawable.winter_animation);
                        pipAnimation = (AnimationDrawable) pipImage.getBackground();
                        pipAnimation.start();
                    } else if (celsTemp > 20 && celsTemp < 25) {
                        pipImage.setBackgroundResource(R.drawable.normal_animation);
                        pipAnimation = (AnimationDrawable) pipImage.getBackground();
                        pipAnimation.start();
                    } else if (celsTemp < 40 && celsTemp >= 25) {
                        pipImage.setBackgroundResource(R.drawable.heat_animation);
                        pipAnimation = (AnimationDrawable) pipImage.getBackground();
                        pipAnimation.start();
                    } else if (celsTemp > 40) {
                        pipImage.setBackgroundResource(R.drawable.extremeh_animation);
                        pipAnimation = (AnimationDrawable) pipImage.getBackground();
                        pipAnimation.start();
                    }


                    /**
                     * Check
                     * @param endHumidity
                     * to determine which skincare suggestions to display in
                     * @param skinSuggestion
                     */
                    if (endHumidity < 50) {
                        skinSection.setVisibility(View.VISIBLE);
                        skinSuggestion.setText("Humidity is low today. Use stronger, more hydrating products. You might also want to try using a hydrating mist, and lip balm.");
                    } else if (endHumidity >= 50) {
                        skinSection.setVisibility(View.VISIBLE);
                        skinSuggestion.setText("Humidity is high today. Use products that are less hydrating + more lightweight, use toner more often, and try using a water-based moisturiser and an oil-based cleanser.");
                    }


                    /**
                     * Check if
                     * @param windSpeed retrieved from currentObject jsonObject has a value
                     *                  and if so, convert windspeed to km in
                     * @param windSpeedkm
                     * and then convert windSpeedkm to a whole number
                     * then use
                     * @param windBearing to determine which direction to set as string for
                     *                    windDirection string
                     *                    then set text for
                     * @param windText to windDirection and realWS
                     */
                    if (windSpeed != null) {
                        Double windSpeedkm = windSpeed * 1.609;

                        int realWS = (int) Math.rint(windSpeedkm);

                        if (windBearing > 11.25 && windBearing < 33.75) {
                            windDirection = "NNE";
                        } else if (windBearing >= 33.75 && windBearing < 56.25) {
                            windDirection = "NE";
                        } else if (windBearing >= 56.25 && windBearing < 78.75) {
                            windDirection = "ENE";
                        } else if (windBearing >= 78.78 && windBearing < 101.25) {
                            windDirection = "E";
                        } else if (windBearing >= 101.25 && windBearing < 123.75) {
                            windDirection = "ESE";
                        } else if (windBearing >= 123.75 && windBearing < 146.25) {
                            windDirection = "SE";
                        } else if (windBearing >= 146.25 && windBearing < 168.75) {
                            windDirection = "SSE";
                        } else if (windBearing >= 168.75 && windBearing < 191.25) {
                            windDirection = "S";
                        } else if (windBearing >= 191.25 && windBearing < 213.75) {
                            windDirection = "SSW";
                        } else if (windBearing >= 213.75 && windBearing < 236.25) {
                            windDirection = "SW";
                        } else if (windBearing >= 236.25 && windBearing < 258.75) {
                            windDirection = "WSW";
                        } else if (windBearing >= 258.75 && windBearing < 281.25) {
                            windDirection = "W";
                        } else if (windBearing >= 281.25 && windBearing < 303.75) {
                            windDirection = "WNW";
                        } else if (windBearing >= 303.75 && windBearing < 326.25) {
                            windDirection = "NW";
                        } else if (windBearing >= 326.25 && windBearing < 348.75) {
                            windDirection = "NNW";
                        } else if (windBearing >= 348.75 || windBearing < 11.25) {
                            windDirection = "N";
                        }
                        windText.setText(windDirection + " " + realWS + " km/h");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

            }

            /**
             * If there is an error, set
             * @param mainLayout and
             * @param progressBar to not appear because we only need
             * @param errorText to display
             *                  Then create an AlertDialog and use VolleyErrorHelperClass to determine what error message to display
             *                  Dialog will be dismissed when user press okay.
             */
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mainLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);

                error.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error retrieving weather data");
                builder.setMessage(VolleyErrorHelperClass.getMessage(error, MainActivity.this));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog errorDialog = builder.create();
                errorDialog.show();

            }
        });


        /**
         * Add this JSON request to mRequestQueue
         */
        mRequestQueue.add(request);


        /**
         * While request is being handled,
         * @param progressBar will be displayed and
         * @param mainLayout will not appear because we only want to show the user that the data is loading right now
         */
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

    }




}

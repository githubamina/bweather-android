package com.blinxwift.blinxweather.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.blinxwift.blinxweather.R;

public class TemperatureActivity extends AppCompatActivity {


    //Switch for user to select Farenheit or Celsius
    public Switch celFarSwitch;

    //Bool to keep track of user's selection
    public Boolean isCelsius;

    //SharedPrefs to store user's selection
    SharedPreferences tempTypeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);


        //Initialize objects
        celFarSwitch = findViewById(R.id.celFarSwitch);
        tempTypeSettings =  this.getSharedPreferences("tempType", 0);
        isCelsius = tempTypeSettings.getBoolean("tempType", true);


        //Update switch and text depending on which selection is made
        if(isCelsius == true){
            celFarSwitch.setChecked(false);
        } else {
            celFarSwitch.setChecked(true);
            celFarSwitch.setText("Farenheit mode is on");
        }


        //When user flips switch, change switch text, change bool, update shared prefs with current selection
        celFarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false){
                    celFarSwitch.setText("Celsius mode is on");
                    isCelsius = true;
                    SharedPreferences.Editor editor = tempTypeSettings.edit();
                    editor.clear();
                    editor.putBoolean("tempType", isCelsius);
                    editor.apply();
                } else if(isChecked == true) {
                    celFarSwitch.setText("Farenheit mode is on");
                    isCelsius = false;
                    SharedPreferences.Editor editor = tempTypeSettings.edit();
                    editor.clear();
                    editor.putBoolean("tempType", isCelsius);
                    editor.apply();
                }
            }
        });

    }

}

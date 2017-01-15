package me.asantiago.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.iconWeatherImageView) ImageView iconWeatherImageView;
    @BindView(R.id.iconDescriptionTextView) TextView iconDescriptionTextView;
    @BindView(R.id.currentTempTextView) TextView currentTempTextView;
    @BindView(R.id.highestTempTextView) TextView highestTempTextView;
    @BindView(R.id.lowestTempTextView) TextView lowestTempTextView;

    @BindDrawable(R.drawable.clear_night)
    Drawable clearNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Using CurrentWeather class
        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);

        currentWeather.setIconImage("rain");
        currentWeather.setDescription("Raining");
        currentWeather.setCurrentTemperature("26");
        currentWeather.setHighestTemperature("28");
        currentWeather.setLowestTemperature("10");

        //init values
        iconWeatherImageView.setImageDrawable(currentWeather.getIconDrawableResource());
        iconDescriptionTextView.setText(currentWeather.getDescription());
        lowestTempTextView.setText(currentWeather.getLowestTemperature());
        highestTempTextView.setText(currentWeather.getHighestTemperature());
        currentTempTextView.setText(currentWeather.getCurrentTemperature());




    }
    //Bind with Butterknife
    @OnClick(R.id.dailyBtnTextView)
    public void dailyWeatherClick(){
        Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
        startActivity(dailyActivityIntent);
    }
    @OnClick(R.id.hourlyBtnTextView)
    public void hourlyWeatherClick(){
        Intent hourlyActivityIntent = new Intent(MainActivity.this, HourlyWeatherActivity.class);
        startActivity(hourlyActivityIntent);
    }
    @OnClick(R.id.minutelyBtnTextView)
    public void minutelyWeatherClick(){
        Intent minutelyActivityIntent = new Intent(MainActivity.this, MinutelyWeatherActivity.class);
        startActivity(minutelyActivityIntent);
    }

}

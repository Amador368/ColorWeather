package me.asantiago.colorweather;
import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;

import me.asantiago.colorweather.Adapters.HourlyWeatherAdapter;

public class HourlyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather);

        ArrayList<Hour> hours = new ArrayList<Hour>();

        for(int i = 0; i < 30; i++){

            Hour hour = new Hour();//Hour class object

            hour.setHour("12:40");
            hour.setWeatherDescription("Description ads");

            hours.add(hour);

        }

        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hours);
        setListAdapter(hourlyWeatherAdapter);

    }
}

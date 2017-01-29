package me.asantiago.colorweather;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import me.asantiago.colorweather.Adapters.DailyWeatherAdapter;

public class DailyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        Intent intent = getIntent();
        ArrayList<Day> days = intent.getParcelableArrayListExtra(MainActivity.DAYS_ARRAY_LIST);
        DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(this, days);

        setListAdapter(dailyWeatherAdapter);
    }
}

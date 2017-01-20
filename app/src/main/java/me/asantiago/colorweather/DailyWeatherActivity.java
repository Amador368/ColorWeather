package me.asantiago.colorweather;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import me.asantiago.colorweather.Adapters.DailyWeatherAdapter;

public class DailyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        //Array list
//        ArrayList<String> daysArray = new ArrayList<>();
//
//        daysArray.add("Lunes");
//        daysArray.add("Martes");
//        daysArray.add("Miercoles");
//        daysArray.add("Jueves");
//        daysArray.add("Viernes");
//        daysArray.add("Sabado");
//        daysArray.add("Domingo");
//
//        //Array Adapter
//        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, daysArray);
        ArrayList<Day> days = new ArrayList<Day>();

        for (int i = 0; i < 500; i++){
            Day day = new Day();
            day.setDayName("Lunes");
            day.setWeatherDescription("Parece que va a llover");
            day.setRainProbability("Probabilidad : 20");
            days.add(day);
        }

        DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(this, days);

        setListAdapter(dailyWeatherAdapter);
    }
}

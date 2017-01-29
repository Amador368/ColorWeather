package me.asantiago.colorweather;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import me.asantiago.colorweather.Adapters.HourlyWeatherAdapter;
public class HourlyWeatherActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather);
        Intent intent = getIntent();
        ArrayList<Hour> hours = intent.getParcelableArrayListExtra(MainActivity.HOURS_ARRAY_LIST);
        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hours);
        setListAdapter(hourlyWeatherAdapter);
    }
}

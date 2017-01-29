package me.asantiago.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asantiago.colorweather.Adapters.MinutelyWeatherAdapter;

public class MinutelyWeatherActivity extends Activity {

    @BindView(R.id.MinutelyRecyclerView)
    RecyclerView minutelyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutely_weather);
        ButterKnife.bind(this);
        Intent intent = getIntent();


        ArrayList<Minute> minutes = intent.getParcelableArrayListExtra(MainActivity.MINUTE_ARRAY_LIST);

//        Minute minute = new Minute();
//
//        minute.setTitle("12:23");
//        minute.setRainProbability("34%");
//
//        minutes.add(minute);

        MinutelyWeatherAdapter minutelyWeatherAdapter = new MinutelyWeatherAdapter(this, minutes);

        minutelyRecyclerView.setAdapter(minutelyWeatherAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        minutelyRecyclerView.setLayoutManager(layoutManager);
        minutelyRecyclerView.setHasFixedSize(true);

    }
}

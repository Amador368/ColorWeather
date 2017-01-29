package me.asantiago.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asantiago.colorweather.Adapters.MinutelyWeatherAdapter;

public class MinutelyWeatherActivity extends Activity {

    @BindView(R.id.MinutelyRecyclerView)
    RecyclerView minutelyRecyclerView;

    @BindView(R.id.noDataTextView)
    TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutely_weather);
        ButterKnife.bind(this);
        Intent intent = getIntent();


        ArrayList<Minute> minutes = intent.getParcelableArrayListExtra(MainActivity.MINUTE_ARRAY_LIST);
        if (minutes != null && !minutes.isEmpty()){
            //show recycler view, and hide  no data text view (if data)
            noDataTextView.setVisibility(View.GONE);
            minutelyRecyclerView.setVisibility(View.VISIBLE);
            MinutelyWeatherAdapter minutelyWeatherAdapter = new MinutelyWeatherAdapter(this, minutes);

            minutelyRecyclerView.setAdapter(minutelyWeatherAdapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            minutelyRecyclerView.setLayoutManager(layoutManager);
            minutelyRecyclerView.setHasFixedSize(true);
        } else {
            //show no data text view, and hide recycler view (if no data)
            noDataTextView.setVisibility(View.VISIBLE);
            minutelyRecyclerView.setVisibility(View.GONE);
        }



    }
}

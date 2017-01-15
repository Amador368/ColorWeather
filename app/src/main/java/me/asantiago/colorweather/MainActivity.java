package me.asantiago.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView dailyWeatherTextView;
    private TextView hourlyWeatherTextView;
    private TextView minutelyWeatherTextView;

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dailyWeatherTextView = (TextView) findViewById(R.id.dailyBtnTextView);
        hourlyWeatherTextView = (TextView) findViewById(R.id.hourlyBtnTextView);
        minutelyWeatherTextView = (TextView) findViewById(R.id.minutelyBtnTextView);

        dailyWeatherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG,"mensaje click en dailly text btn");
                Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
                startActivity(dailyActivityIntent);
            }
        });

    }
}

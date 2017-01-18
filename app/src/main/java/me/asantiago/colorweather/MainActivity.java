package me.asantiago.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);

//        currentWeather.setIconImage("rain");
//        currentWeather.setDescription("Raining");
//        currentWeather.setCurrentTemperature("26");
//        currentWeather.setHighestTemperature("28");
//        currentWeather.setLowestTemperature("10");

        //init values
//        iconWeatherImageView.setImageDrawable(currentWeather.getIconDrawableResource());
//        iconDescriptionTextView.setText(currentWeather.getDescription());
//        lowestTempTextView.setText(currentWeather.getLowestTemperature());
//        highestTempTextView.setText(currentWeather.getHighestTemperature());
//        currentTempTextView.setText(currentWeather.getCurrentTemperature());


        //creating test request

        //final TextView mTextView = (TextView) findViewById(R.id.text);

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.darksky.net/forecast/b0a46a5014bcb88370a36ef5ac72eb9e/37.8267,-122.4233?units=si";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            CurrentWeather currentWeather = getCurrentWeatherFromJson(response);
                            Log.d(TAG,"lol: "+ response.substring(0,500));

                            iconWeatherImageView.setImageDrawable(currentWeather.getIconDrawableResource());
                            iconDescriptionTextView.setText(currentWeather.getDescription());
                            lowestTempTextView.setText(currentWeather.getLowestTemperature());
                            highestTempTextView.setText(String.format("H: %s °",currentWeather.getHighestTemperature()));
                            currentTempTextView.setText(String.format("L; %s °", currentWeather.getCurrentTemperature()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);




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

    private CurrentWeather getCurrentWeatherFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithCurrentlyWeather = jsonObject.getJSONObject("currently");
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject("daily");
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray("data");
        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentlyWeather.getString("summary");
        String icon =jsonWithCurrentlyWeather.getString("icon");
        String temperature = Math.round(jsonWithCurrentlyWeather.getDouble("temperature")) + "";

        String maxTemperature = Math.round(jsonWithTodayData.getDouble("temperatureMax")) + "";
        String minTemperature = Math.round(jsonWithTodayData.getDouble("temperatureMin")) + "";

        //object from CurrentWeather Class
        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);
        //Setting values from json response data
        currentWeather.setDescription(summary);
        currentWeather.setIconImage(icon);
        currentWeather.setCurrentTemperature(temperature);
        currentWeather.setHighestTemperature(maxTemperature);
        currentWeather.setLowestTemperature(minTemperature);


        return currentWeather;
    }

}

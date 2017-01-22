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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.asantiago.colorweather.Adapters.Minute;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DATA = "data";
    public static final String SUMMARY = "summary";
    public static final String ICON = "icon";
    public static final String DAILY = "daily";
    public static final String HOURLY = "hourly";
    public static final String TIME = "time";
    public static final String API_URL = "https://api.darksky.net/forecast/b0a46a5014bcb88370a36ef5ac72eb9e/37.8267,-122.4233?units=si";
    public static final String CURRENTLY = "currently";
    public static final String MINUTELY = "minutely";
    public static final String PRECIP_PROBABILITY = "precipProbability";

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API_URL ;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Day> days = getDailyWeatherFromJson(response);
                            ArrayList<Hour> hours = getHourlyWeatherFromJson(response);
                            ArrayList<Minute> minutes = getMinutelyWeatherFromJson(response);


//                            for (Day day : days) {
//                                Log.d(TAG, "dayname: "+ day.getDayName());
//                                Log.d(TAG, "day des: "+ day.getWeatherDescription());
//                                Log.d(TAG, "day prob: "+ day.getRainProbability());
//                            }
//                            for (Hour hour : hours) {
//                                Log.d(TAG, "hour title: "+ hour.getHour());
//                                Log.d(TAG, "hout des: "+ hour.getWeatherDescription());
//                            }
                            for (Minute minute : minutes) {
                                Log.d(TAG, "minute title: "+ minute.getTitle());
                                Log.d(TAG, "minute des: "+ minute.getRainProbability());
                            }

                            CurrentWeather currentWeather = getCurrentWeatherFromJson(response);
//                            Log.d(TAG,"lol: "+ response.substring(0,500));

                            iconWeatherImageView.setImageDrawable(currentWeather.getIconDrawableResource());
                            iconDescriptionTextView.setText(currentWeather.getDescription());
                            lowestTempTextView.setText(String.format("L: %s °",currentWeather.getLowestTemperature()));
                            highestTempTextView.setText(String.format("H: %s °",currentWeather.getHighestTemperature()));
                            currentTempTextView.setText(currentWeather.getCurrentTemperature());

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

        JSONObject jsonWithCurrentlyWeather = jsonObject.getJSONObject(CURRENTLY);
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);
        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentlyWeather.getString(SUMMARY);
        String icon =jsonWithCurrentlyWeather.getString(ICON);
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

    private ArrayList<Day> getDailyWeatherFromJson(String json) throws JSONException {

        DateFormat dateFormat = new SimpleDateFormat("EEEE");

      //  ArrayList<Day> days = new ArrayList<>();
        ArrayList<Day> days = new ArrayList<Day>();
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithDailyWeatherData.length(); i ++ ){

            Day day = new Day();

            JSONObject jsonWhitDayData = jsonWithDailyWeatherData.getJSONObject(i);
            String rainProbability = jsonWhitDayData.getDouble(PRECIP_PROBABILITY) + "";
            String description = jsonWhitDayData.getString(SUMMARY);
            String date = dateFormat.format(jsonWhitDayData.getLong(TIME) * 1000);

            day.setDayName(date);
            day.setRainProbability(rainProbability);
            day.setWeatherDescription(description);

            days.add(day);

        }

        return days;

    }

    private ArrayList<Hour> getHourlyWeatherFromJson (String json) throws JSONException {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        ArrayList<Hour> hours = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithHourlyWeather = jsonObject.getJSONObject(HOURLY);
        JSONArray jsonWithHourlyWeatherData = jsonWithHourlyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithHourlyWeatherData.length(); i ++ ){

            Hour hour = new Hour();

            JSONObject jsonWhitHourData = jsonWithHourlyWeatherData.getJSONObject(i);

            String description = jsonWhitHourData.getString(SUMMARY);
            String title = dateFormat.format(jsonWhitHourData.getLong(TIME) * 1000);

            hour.setHour(title);
            hour.setWeatherDescription(description);

            hours.add(hour);

        }

        return hours;
    }

    private ArrayList<Minute> getMinutelyWeatherFromJson (String json) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        ArrayList<Minute> minutes = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        Log.d(TAG, "jsonObject");

        Log.d(TAG, "minutely data  abajo");

        //JSONObject jsonWithMinutelyWeather = jsonObject.getJSONObject("minutely");
        JSONObject jsonWithMinutelyWeather = jsonObject.getJSONObject("minutely");
        JSONArray jsonWithMinutelyWeatherData = jsonWithMinutelyWeather.getJSONArray(DATA);




        for (int i = 0; i < jsonWithMinutelyWeatherData.length(); i ++ ){

            Minute minute = new Minute();


            JSONObject jsonWhithMinuteData = jsonWithMinutelyWeatherData.getJSONObject(i);

            String description = jsonWhithMinuteData.getDouble(PRECIP_PROBABILITY) + "";
            String title = dateFormat.format(jsonWhithMinuteData.getDouble(TIME) * 1000);

            minute.setTitle(title);
            minute.setRainProbability(description);

            minutes.add(minute);

        }

        return minutes;
    }



}

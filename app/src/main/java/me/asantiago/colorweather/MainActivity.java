package me.asantiago.colorweather;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DATA = "data";
    public static final String SUMMARY = "summary";
    public static final String ICON = "icon";
    public static final String DAILY = "daily";
    public static final String HOURLY = "hourly";
    public static final String TIME = "time";
    private static final int REQUEST_LOCATION = 2;
    //public static final String API_URL = "";

    String units = "units=si";
    String forecastURL = "https://api.darksky.net/forecast";
    String forecastAPI = "b0a46a5014bcb88370a36ef5ac72eb9e";
    //    String latitude = "37.8267";
//    String longitude = "-122.4233";
    String latitude;
    String longitude;


//                Toast.makeText(this, "latitude" + latitude, Toast.LENGTH_LONG).show();
//                Toast.makeText(this, "longitude" + longitude, Toast.LENGTH_LONG).show();

    public static final String CURRENTLY = "currently";
    public static final String MINUTELY = "minutely";
    public static final String PRECIP_PROBABILITY = "precipProbability";
    public static final String DAYS_ARRAY_LIST = "DAYS_ARRAY_LIST";
    public static final String HOURS_ARRAY_LIST = "HOURS_ARRAY_LIST";
    public static final String MINUTE_ARRAY_LIST = "MINUTE_ARRAY_LIST";

    @BindView(R.id.iconWeatherImageView)
    ImageView iconWeatherImageView;
    @BindView(R.id.iconDescriptionTextView)
    TextView iconDescriptionTextView;
    @BindView(R.id.currentTempTextView)
    TextView currentTempTextView;
    @BindView(R.id.highestTempTextView)
    TextView highestTempTextView;
    @BindView(R.id.lowestTempTextView)
    TextView lowestTempTextView;

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @BindDrawable(R.drawable.clear_night)
    Drawable clearNight;
    ArrayList<Day> days;
    ArrayList<Hour> hours;
    ArrayList<Minute> minutes;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Establecer punto de entrada para la API de ubicación
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //Bind with Butterknife
    @OnClick(R.id.dailyBtnTextView)
    public void dailyWeatherClick() {
        Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
        dailyActivityIntent.putParcelableArrayListExtra(DAYS_ARRAY_LIST, days);
        startActivity(dailyActivityIntent);
    }

    @OnClick(R.id.hourlyBtnTextView)
    public void hourlyWeatherClick() {
        Intent hourlyActivityIntent = new Intent(MainActivity.this, HourlyWeatherActivity.class);
        hourlyActivityIntent.putParcelableArrayListExtra(HOURS_ARRAY_LIST, hours);
        startActivity(hourlyActivityIntent);
    }

    @OnClick(R.id.minutelyBtnTextView)
    public void minutelyWeatherClick() {
        Intent minutelyActivityIntent = new Intent(MainActivity.this, MinutelyWeatherActivity.class);
        minutelyActivityIntent.putParcelableArrayListExtra(MINUTE_ARRAY_LIST, minutes);
        startActivity(minutelyActivityIntent);
    }

    private CurrentWeather getCurrentWeatherFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithCurrentlyWeather = jsonObject.getJSONObject(CURRENTLY);
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);
        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentlyWeather.getString(SUMMARY);
        String icon = jsonWithCurrentlyWeather.getString(ICON);
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
        days = new ArrayList<Day>();
        JSONObject jsonObject = new JSONObject(json);
        String timeZone = jsonObject.getString("timezone");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithDailyWeatherData.length(); i++) {

            Day day = new Day();

            JSONObject jsonWhitDayData = jsonWithDailyWeatherData.getJSONObject(i);
            String rainProbability = "Rain probability:" + jsonWhitDayData.getDouble(PRECIP_PROBABILITY) * 100 + "%";
            String description = jsonWhitDayData.getString(SUMMARY);
            String date = dateFormat.format(jsonWhitDayData.getLong(TIME) * 1000);

            day.setDayName(date);
            day.setRainProbability(rainProbability);
            day.setWeatherDescription(description);

            days.add(day);

        }

        return days;

    }

    private ArrayList<Hour> getHourlyWeatherFromJson(String json) throws JSONException {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        hours = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        String timeZone = jsonObject.getString("timezone");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        JSONObject jsonWithHourlyWeather = jsonObject.getJSONObject(HOURLY);
        JSONArray jsonWithHourlyWeatherData = jsonWithHourlyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithHourlyWeatherData.length(); i++) {

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

    private ArrayList<Minute> getMinutelyWeatherFromJson(String json) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        minutes = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        String timeZone = jsonObject.getString("timezone");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

        //JSONObject jsonWithMinutelyWeather = jsonObject.getJSONObject("minutely");
        JSONObject jsonWithMinutelyWeather = jsonObject.getJSONObject("minutely");
        JSONArray jsonWithMinutelyWeatherData = jsonWithMinutelyWeather.getJSONArray(DATA);
        for (int i = 0; i < jsonWithMinutelyWeatherData.length(); i++) {
            Minute minute = new Minute();
            JSONObject jsonWhithMinuteData = jsonWithMinutelyWeatherData.getJSONObject(i);
            String precipProbability = "Rain probability:" + jsonWhithMinuteData.getDouble(PRECIP_PROBABILITY) * 100 + "%";
            String title = dateFormat.format(jsonWhithMinuteData.getDouble(TIME) * 1000);
            minute.setTitle(title);
            minute.setRainProbability(precipProbability);
            minutes.add(minute);
        }
        return minutes;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Aquí muestras confirmación explicativa al usuario
                // por si rechazó los permisos anteriormente
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(mLastLocation.getLongitude());
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);
                String url =  forecastURL + "/" + forecastAPI + "/" + latitude + "," + longitude + "?" + units;
                //String url ="https://api.darksky.net/forecast/6403c985da8fbfc667f19d0b5840d2c5/37.8267,-122.4233?units=si";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    CurrentWeather currentWeather = getCurrentWeatherFromJson(response);
                                    //Log.d(TAG,"lol: "+ response.substring(0,500));
                                    iconWeatherImageView.setImageDrawable(currentWeather.getIconDrawableResource());
                                    iconDescriptionTextView.setText(currentWeather.getDescription());
                                    lowestTempTextView.setText(String.format("L: %s °",currentWeather.getLowestTemperature()));
                                    highestTempTextView.setText(String.format("H: %s °",currentWeather.getHighestTemperature()));
                                    currentTempTextView.setText(currentWeather.getCurrentTemperature());

                                    ArrayList<Day> days = getDailyWeatherFromJson(response);
                                    ArrayList<Hour> hours = getHourlyWeatherFromJson(response);
                                    ArrayList<Minute> minutes = getMinutelyWeatherFromJson(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"That didn't work!");
                        Toast.makeText(MainActivity.this,"Connection Error!", Toast.LENGTH_LONG) .show();
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

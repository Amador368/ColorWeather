package me.asantiago.colorweather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asantiago on 19/01/17.
 */

public class Day implements Parcelable {
    private String dayName;
    private String weatherDescription;
    private String rainProbability;

    public Day(){

    }
    //constructor parcelable
    protected Day(Parcel in) {
        dayName = in.readString();
        weatherDescription = in.readString();
        rainProbability = in.readString();
    }
    //implements parcelable methods
    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(String rainProbability) {
        this.rainProbability = rainProbability;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }


    @Override
    public int describeContents() {
        return 0;//
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dayName);
        parcel.writeString(weatherDescription);
        parcel.writeString(rainProbability);
    }
}

package me.asantiago.colorweather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.asantiago.colorweather.Hour;
import me.asantiago.colorweather.R;

/**
 * Created by asantiago on 21/01/17.
 */

public class HourlyWeatherAdapter extends BaseAdapter {

    ArrayList<Hour> hours;
    Context context;

    public HourlyWeatherAdapter(Context context, ArrayList<Hour> hours ){
        this.context = context;
        this.hours = hours;

    }

    @Override
    public int getCount() {
        if (hours == null)
            return 0;
        return hours.size();
    }

    @Override
    public Object getItem(int i) {
        return hours.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        Hour hour = hours.get(i);

        if( view == null){

            view = LayoutInflater.from(context).inflate(R.layout.hourly_list_item, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.hourTitle = (TextView) view.findViewById(R.id.hourlyListTitleTextView);
            viewHolder.hourDescription = (TextView) view.findViewById(R.id.hourlyListDescriptionTextView);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.hourTitle.setText(hour.getHour());
        viewHolder.hourDescription.setText(hour.getWeatherDescription());


        return view;
    }

    static class ViewHolder {
        TextView hourTitle;
        TextView hourDescription;
    }
}

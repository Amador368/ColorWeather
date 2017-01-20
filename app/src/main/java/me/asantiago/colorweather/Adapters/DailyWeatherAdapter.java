package me.asantiago.colorweather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.asantiago.colorweather.Day;
import me.asantiago.colorweather.R;

/**
 * Created by asantiago on 19/01/17.
 */

public class DailyWeatherAdapter extends BaseAdapter{

    ArrayList<Day> days;
    Context context;

    public DailyWeatherAdapter (Context context, ArrayList<Day> days){
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();//array size
    }

    @Override
    public Object getItem(int i) {
        return days.get(i);//item position
    }

    @Override
    public long getItemId(int i) {
        return 0;// is not using
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        Day day = days.get(i);

        if (view == null){

            view = LayoutInflater.from(context).inflate(R.layout.daily_list_item,viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.dayTitle = (TextView) view.findViewById(R.id.dailyListTitleTextView);
            viewHolder.dayDescription = (TextView) view.findViewById(R.id.dailyListDescriptionTextView);
            viewHolder.dayRainProbability = (TextView) view.findViewById(R.id.dailyListProbabilityTextView);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();

        }

//        TextView dayTitle = (TextView) view.findViewById(R.id.dailyListTitleTextView);
//        TextView dayDescription = (TextView) view.findViewById(R.id.dailyListDescriptionTextView);
//        TextView dayRainProbability = (TextView) view.findViewById(R.id.dailyListProbabilityTextView);

        viewHolder.dayTitle.setText(day.getDayName());
        viewHolder.dayDescription.setText(day.getWeatherDescription());
        viewHolder.dayRainProbability.setText(day.getRainProbability());

        return view;
    }

    static class ViewHolder {
        TextView dayTitle;
        TextView dayDescription;
        TextView dayRainProbability;


    }
}

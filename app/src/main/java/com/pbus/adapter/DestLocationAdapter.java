package com.pbus.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.bean.RouteListBean;

import java.util.ArrayList;

/**
 * Created by mindiii on 16/4/18.
 */

public class DestLocationAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<RouteListBean> list;

    public DestLocationAdapter(Activity activity, ArrayList<RouteListBean> list) {
        super(activity, R.layout.custom_spinner_view);
        this.activity = activity;
        this.list = list;

        RouteListBean bean = new RouteListBean();
        bean.stationId = "0";
        bean.station_name = activity.getResources().getString(R.string.destination_location);
        list.add(0, bean);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        RouteListBean bean = list.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(R.layout.custom_spinner_view, viewGroup, false);
        }

        TextView tvSpinnerName = view.findViewById(R.id.tvSpinnerName);
        tvSpinnerName.setText(bean.station_name);
        tvSpinnerName.setPadding((int) activity.getResources().getDimension(R.dimen._35sdp), 0, 0, 0);

        if (position == 0) {
            tvSpinnerName.setTextColor(activity.getResources().getColor(R.color.grey2));
            tvSpinnerName.setText(bean.station_name);
        } else {
            tvSpinnerName.setTextColor(activity.getResources().getColor(R.color.black));
            tvSpinnerName.setText(bean.station_name);
        }

        return view;
    }

    @Override
    public View getDropDownView(int i, View view, @NonNull ViewGroup viewGroup) {
        RouteListBean bean = list.get(i);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(R.layout.custom_spinner_view, viewGroup, false);
        }

        TextView tvSpinnerName = view.findViewById(R.id.tvSpinnerName);
        tvSpinnerName.setText(bean.station_name);
        tvSpinnerName.setPadding((int) activity.getResources().getDimension(R.dimen._5sdp), 0, 0, 0);
        return view;
    }

}



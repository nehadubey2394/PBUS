package com.pbus.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.activity.SellerMainActivity;
import com.pbus.bean.BusListBean;
import com.pbus.fragment.seller.AvailableBusesFragment;
import com.pbus.fragment.seller.BusDetailFragment;
import com.pbus.utility.Util;

import java.util.ArrayList;

/**
 * Created by mindiii on 17/4/18.
 */

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder> {

    private AvailableBusesFragment fragment;
    private ArrayList<BusListBean> list;
    private SellerMainActivity activity;
    private boolean multiClick = false;

    public BusListAdapter(SellerMainActivity activity, AvailableBusesFragment fragment, ArrayList<BusListBean> list) {
        this.activity = activity;
        this.fragment = fragment;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_available_bus_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusListBean bean = list.get(position);

        holder.tvTotalSeat.setText(bean.bus_seats);
        holder.tvBusNumber.setText(bean.bus_number);

        String[] source = bean.source.split(",");
        String[] des = bean.destination.split(",");
        String route = source[0] + " To " + des[0];
        holder.tvRouteAddress.setText(route);

        String time = Util.format12HourTime(bean.bus_time, "HH:mm:ss", "hh:mm a") + " Onwards";
        holder.tvBusTime.setText(time);

        holder.tvBusFare.setText("$" + bean.bus_fare);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTotalSeat, tvBusNumber, tvRouteAddress, tvBusTime, tvBusFare;

        ViewHolder(View v) {
            super(v);
            tvTotalSeat = v.findViewById(R.id.tvTotalSeat);
            tvBusNumber = v.findViewById(R.id.tvBusNumber);
            tvRouteAddress = v.findViewById(R.id.tvRouteAddress);
            tvBusTime = v.findViewById(R.id.tvBusTime);
            tvBusFare = v.findViewById(R.id.tvBusFare);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            view.setEnabled(false);
            if (!multiClick)
                activity.addFragment(BusDetailFragment.newInstance(fragment.fromDate, fragment.toDate, list.get(getAdapterPosition()).busId));
            multiClick = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                    multiClick = false;
                }
            }, 1500);
        }
    }
}

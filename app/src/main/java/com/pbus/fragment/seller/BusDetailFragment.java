package com.pbus.fragment.seller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.activity.SellerMainActivity;
import com.pbus.bean.BusListBean;
import com.pbus.helper.Webservices;
import com.pbus.utility.MyToast;
import com.pbus.utility.Util;
import com.pbus.volleymultipart.VolleyGetPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class BusDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BusDetailFragment.class.getSimpleName();

    private static final String ARG_FROM_DATE = "param1";
    private static final String ARG_TO_DATE = "param2";
    private static final String ARG_BUS_ID = "param3";

    private String fromDate = "", toDate = "", busId = "";
    private SellerMainActivity activity;

    private TextView tvBusNumber, sourceLoc, tvDestLoc, tvTotalSeat, tvFare, tvFromDate, tvToDate, tvTime;

    public BusDetailFragment() {
        //Empty constructor
    }

    public static BusDetailFragment newInstance(String from, String to, String busId) {
        BusDetailFragment fragment = new BusDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FROM_DATE, from);
        args.putString(ARG_TO_DATE, to);
        args.putString(ARG_BUS_ID, busId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromDate = getArguments().getString(ARG_FROM_DATE);
            toDate = getArguments().getString(ARG_TO_DATE);
            busId = getArguments().getString(ARG_BUS_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus_detail, container, false);
        bindView(v);
        return v;
    }

    private void bindView(View view) {
        view.findViewById(R.id.imgDrawerMenu).setVisibility(View.GONE);
        ImageView imgBack = view.findViewById(R.id.imgToolbarBack);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        TextView title = view.findViewById(R.id.tvToolbarTitle);
        title.setText(R.string.bus_detail);

        tvBusNumber = view.findViewById(R.id.tvBusNumber);
        sourceLoc = view.findViewById(R.id.sourceLoc);
        tvDestLoc = view.findViewById(R.id.tvDestLoc);
        tvTotalSeat = view.findViewById(R.id.tvTotalSeat);
        tvFare = view.findViewById(R.id.tvFare);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        tvToDate = view.findViewById(R.id.tvToDate);
        tvTime = view.findViewById(R.id.tvTime);

        getBusDetail();
    }

    private void getBusDetail() {
        new VolleyGetPost(activity, Webservices.BUS_DETAIL, true, TAG) {
            @Override
            public void onVolleyResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        ArrayList<BusListBean> list = new ArrayList<>();
                        JSONArray jArray = jsonObject.getJSONArray("busList");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObj = jArray.getJSONObject(i);
                            BusListBean bean = new BusListBean();

                            bean.busId = jObj.getString("busId");
                            bean.bus_name = jObj.getString("bus_name");
                            bean.bus_number = jObj.getString("bus_number");
                            bean.bus_seats = jObj.getString("bus_seats");
                            bean.bus_time = jObj.getString("bus_time");
                            bean.bus_fare = jObj.getString("bus_fare");
                            bean.source = jObj.getString("source");
                            bean.destination = jObj.getString("destination");
                            bean.from = jObj.getString("from");
                            bean.to = jObj.getString("to");

                            list.add(bean);
                        }
                        setData(list);
                    } else {
                        MyToast.getInstance(activity).customToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError() {
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("busId", busId);
                params.put("from", fromDate);
                params.put("to", toDate);
                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                params.put("authToken", activity.getUserInfo().authToken);
                return params;
            }
        }.executeVolley();
    }

    private void setData(ArrayList<BusListBean> list) {
        BusListBean bean = list.get(0);
        tvBusNumber.setText(bean.bus_number);
        sourceLoc.setText(bean.source);
        tvDestLoc.setText(bean.destination);
        tvTotalSeat.setText(bean.bus_seats);
        tvFare.setText("$ " + bean.bus_fare);
        tvFromDate.setText(Util.formatDate(bean.from, "yyyy/MM/dd", "dd/MM/yyyy"));
        tvToDate.setText(Util.formatDate(bean.to, "yyyy/MM/dd", "dd/MM/yyyy"));

        tvTime.setText(Util.format12HourTime(bean.bus_time, "HH:mm:ss", "hh:mm a"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SellerMainActivity) context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgToolbarBack:
                activity.onBackPressed();
                break;
        }
    }
}

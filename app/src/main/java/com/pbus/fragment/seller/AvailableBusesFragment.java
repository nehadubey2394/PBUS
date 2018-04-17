package com.pbus.fragment.seller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.activity.SellerMainActivity;
import com.pbus.adapter.BusListAdapter;
import com.pbus.bean.BusListBean;

import java.util.ArrayList;


public class AvailableBusesFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_ARRAY_LIST = "param1";
    private static final String ARG_FROM_DATE = "param2";
    private static final String ARG_TO_DATE = "param3";
    public String fromDate = "", toDate = "";
    private ArrayList<BusListBean> busList;
    private SellerMainActivity activity;

    public AvailableBusesFragment() {
        //Empty constructor
    }

    public static AvailableBusesFragment newInstance(ArrayList<BusListBean> busList, String from, String to) {
        AvailableBusesFragment fragment = new AvailableBusesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FROM_DATE, from);
        args.putString(ARG_TO_DATE, to);
        args.putSerializable(ARG_ARRAY_LIST, busList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            busList = (ArrayList<BusListBean>) getArguments().getSerializable(ARG_ARRAY_LIST);
            fromDate = getArguments().getString(ARG_FROM_DATE);
            toDate = getArguments().getString(ARG_TO_DATE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_available_buses, container, false);
        bindView(v);
        return v;
    }

    private void bindView(View view) {
        view.findViewById(R.id.imgDrawerMenu).setVisibility(View.GONE);
        ImageView imgBack = view.findViewById(R.id.imgToolbarBack);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        TextView title = view.findViewById(R.id.tvToolbarTitle);
        title.setText(R.string.available_buses);

        LinearLayout linLayNoBusAvail = view.findViewById(R.id.linLayNoBusAvail);
        RecyclerView rlView = view.findViewById(R.id.rvAvailableBus);


        if (busList.size() == 0) {
            linLayNoBusAvail.setVisibility(View.VISIBLE);
            rlView.setVisibility(View.GONE);
        } else {
            linLayNoBusAvail.setVisibility(View.GONE);
            rlView.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
            BusListAdapter adapter = new BusListAdapter(activity, this, busList);
            rlView.setLayoutManager(manager);
            rlView.setAdapter(adapter);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SellerMainActivity) context;
        activity.setDrawerEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.showToolbar();
        activity.setDrawerEnabled(true); //enable drawer swipe
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

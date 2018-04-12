package com.pbus.fragment.seller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbus.R;
import com.pbus.activity.SellerMainActivity;
import com.pbus.utility.MyToast;

public class NewBookingFragment extends Fragment implements View.OnClickListener {


    private SellerMainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_new_booking, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        v.findViewById(R.id.tvSearch).setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (SellerMainActivity) context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSearch:
                MyToast.getInstance(activity).customToast(getResources().getString(R.string.underDev));
                break;
        }
    }
}

package com.pbus.fragment.seller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.activity.SellerMainActivity;
import com.pbus.adapter.DestLocationAdapter;
import com.pbus.adapter.SourceLocationAdapter;
import com.pbus.bean.BusListBean;
import com.pbus.bean.RouteListBean;
import com.pbus.helper.Webservices;
import com.pbus.utility.MyToast;
import com.pbus.utility.Util;
import com.pbus.volleymultipart.VolleyGetPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class NewBookingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = NewBookingFragment.class.getSimpleName();

    private SellerMainActivity activity;
    private ArrayList<RouteListBean> sourceList, desList;
    private Spinner spinnerSource, spinnerDest;
    private TextView tvFromDate, tvToDate, tvDestLoc, tvSource;
    private boolean isFromDate;
    private String sourceId = "", destId = "", fromDate = "", toDate = "";
    private int sPos = 0, dPos;
    // variable to track event time
    private long mLastClickTime = 0;

    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String day, month;
            day = (selectedDay < 10) ? "0" + selectedDay : String.valueOf(selectedDay);
            selectedMonth += 1;
            month = (selectedMonth < 10) ? "0" + selectedMonth : String.valueOf(selectedMonth);

            String date = day + "/" + month + "/" + selectedYear;

            if (isFromDate) {
                tvFromDate.setText(date);
                fromDate = tvFromDate.getText().toString();
                tvToDate.setText("");
                toDate = "";
            }
            else {
                    if (verifyDate(date, fromDate)) {
                        tvToDate.setText(date);
                        toDate = date;
                    }
            }
        }
    };

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
        spinnerSource = v.findViewById(R.id.spinnerSource);
        spinnerDest = v.findViewById(R.id.spinnerDest);
        v.findViewById(R.id.rlFromDate).setOnClickListener(this);
        v.findViewById(R.id.rlToDate).setOnClickListener(this);
        tvFromDate = v.findViewById(R.id.tvFromDate);
        tvToDate = v.findViewById(R.id.tvToDate);
        tvSource = v.findViewById(R.id.tvSource);
        tvDestLoc = v.findViewById(R.id.tvDestLoc);
        v.findViewById(R.id.imgExchange).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        desList = new ArrayList<>();
        sourceList = new ArrayList<>();
        getRouteList();

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long l) {
                if (position > 0) {
                    sourceId = sourceList.get(position).stationId;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sPos = position;
                        }
                    }, 600);
                } else {
                    sourceId = "";
                }
                spinnerDest.setSelection(sPos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerDest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long l) {
                if (position > 0) {
                    if (desList.get(position).stationId.equalsIgnoreCase(sourceId)) {
                        spinnerDest.setSelection(0);
                        MyToast.getInstance(getContext()).customToast("Sorry, Source And Destination Can't Be Same");
                    } else {
                        destId = desList.get(position).stationId;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dPos = position;
                            }
                        }, 600);
                    }
                } else {
                    destId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getRouteList() {
        new VolleyGetPost(activity, Webservices.ROUTE_LIST, false, TAG) {
            @Override
            public void onVolleyResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        tvDestLoc.setVisibility(View.GONE);
                        tvSource.setVisibility(View.GONE);
                        JSONArray jArray = jsonObject.getJSONArray("stationList");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObj = jArray.getJSONObject(i);
                            RouteListBean bean = new RouteListBean();

                            bean.stationId = jObj.getString("stationId");
                            bean.station_name = jObj.getString("station_name");

                            sourceList.add(bean);
                        }

                        desList.addAll(sourceList);

                        //custom adapter for source spinner
                        SourceLocationAdapter adapterSource = new SourceLocationAdapter(activity, sourceList);
                        spinnerSource.setAdapter(adapterSource);

                        //custom adapter for Dest spinner
                        DestLocationAdapter adapterDest = new DestLocationAdapter(activity, desList);
                        spinnerDest.setAdapter(adapterDest);

                    } else {
                        MyToast.getInstance(activity).customToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError() {
                tvDestLoc.setVisibility(View.VISIBLE);
                tvSource.setVisibility(View.VISIBLE);
                tvSource.setText(R.string.na);
                tvDestLoc.setText(R.string.na);
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                params.put("authToken", activity.getUserInfo().authToken);
                Util.e(TAG + " authToken", params.toString());
                return params;
            }
        }.executeVolley();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (SellerMainActivity) context;
    }

    @Override
    public void onClick(View view) {
        // Preventing multiple clicks, using threshold of 1/2 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()){
            case R.id.tvSearch:
                if (verifySearchData()) getBusList();
                break;

            case R.id.rlFromDate:
                isFromDate = true;
                getDate();
                break;

            case R.id.rlToDate:
                if (!tvFromDate.getText().toString().isEmpty()) {
                    isFromDate = false;
                    getDate();
                } else {
                    MyToast.getInstance(activity).customToast("Please select date for From");
                }
                break;

            case R.id.imgExchange:
                if (!sourceId.isEmpty() && !destId.isEmpty()) {
                    spinnerSource.setSelection(dPos);
                    spinnerDest.setSelection(sPos);
                }
                break;
        }
    }

    private void getBusList() {


        new VolleyGetPost(activity, Webservices.BUS_LIST, true, TAG) {
            @Override
            public void onVolleyResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    ArrayList<BusListBean> list = new ArrayList<>();
                    String from = "", to = "";
                    if (status.equalsIgnoreCase("success")) {

                        //object inside another object handle
                        JSONObject object = jsonObject.getJSONObject("formToDate");
                        from = object.getString("from");
                        to = object.getString("to");

                        JSONArray jArray = jsonObject.getJSONArray("busList");
                        for (int i=0;i<jArray.length();i++){
                            JSONObject jObj=jArray.getJSONObject(i);
                            BusListBean bean = new BusListBean();

                            bean.busId = jObj.getString("busId");
                            bean.bus_name = jObj.getString("bus_name");
                            bean.bus_number = jObj.getString("bus_number");
                            bean.bus_seats = jObj.getString("bus_seats");
                            bean.bus_time = jObj.getString("bus_time");
                            bean.bus_fare = jObj.getString("bus_fare");
                            bean.source = jObj.getString("source");
                            bean.destination = jObj.getString("destination");

                            list.add(bean);
                        }

                        activity.addFragment(AvailableBusesFragment.newInstance(list, from, to));


                    } else if (message.equalsIgnoreCase("No Buses Available")) {
                        activity.addFragment(AvailableBusesFragment.newInstance(list, from, to));
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
                params.put("source", sourceId);
                params.put("destination", destId);
                params.put("from", fromDate);
                params.put("to", toDate);
                params.put("offset", "");
                params.put("limit", "");
                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                params.put("authToken", activity.getUserInfo().authToken);
                return params;
            }
        }.executeVolley();
    }

    /* get Date start here */
    private void getDate() {
        Calendar newCalendar = Calendar.getInstance();

        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int month = newCalendar.get(Calendar.MONTH);
        int year = newCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, datePicker, year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
        Date newDate = calendar.getTime();
        datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));

        datePickerDialog.show();

    }

    private boolean verifyDate(String toDate, String fromDate) {
        try {
            Date fDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fromDate);
            Date sDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(toDate);

            if (sDate.before(fDate)) {
                MyToast.getInstance(activity).customToast("Please select to date which is greater than from date");
                return false;
            } else return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean verifySearchData() {
        if (sourceId.isEmpty()) {
            MyToast.getInstance(activity).customToast("Please select source location");
            return false;
        } else if (destId.isEmpty()) {
            MyToast.getInstance(activity).customToast("Please select destination location");
            return false;
        } else if (fromDate.isEmpty()) {
            MyToast.getInstance(activity).customToast("Please select date for From");
            return false;
        } else if (toDate.isEmpty()) {
            MyToast.getInstance(activity).customToast("Please select date for To");
            return false;
        } else return true;
    }

/* get Date end here */
}

package com.pbus.fragment.seller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.pbus.bean.RouteListBean;
import com.pbus.helper.Webservices;
import com.pbus.utility.MyToast;
import com.pbus.utility.Progress;
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
    private TextView tvFromDate, tvToDate;
    private boolean isFromDate;
    private String sourceId = "", destId = "", fromDate = "", toDate = "";
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String day, month;
            day = (selectedDay < 10) ? "0" + selectedDay : String.valueOf(selectedDay);
            selectedMonth += 1;
            month = (selectedMonth < 10) ? "0" + selectedMonth : String.valueOf(selectedMonth);

            String date = day + "-" + month + "-" + selectedYear;
            fromDate = tvFromDate.getText().toString();
            if (isFromDate) tvFromDate.setText(date);
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        desList = new ArrayList<>();
        sourceList = new ArrayList<>();
        getRouteList();

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    sourceId = sourceList.get(position).stationId;
                } else {
                    sourceId = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    destId = desList.get(position).stationId;
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
                Progress.hide(activity);
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                params.put("authToken", activity.getUserInfo().authToken);
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
        switch (view.getId()){
            case R.id.tvSearch:
                if (sourceId.isEmpty() && destId.isEmpty()) {
                    MyToast.getInstance(activity).customToast("Please select start end location");
                } else if (fromDate.isEmpty() && toDate.isEmpty()) {
                    MyToast.getInstance(activity).customToast("Please select from and to date");
                } else {
                    getBusList();
                }
                break;

            case R.id.rlFromDate:
                isFromDate = true;
                getDate();
                break;

            case R.id.rlToDate:
                isFromDate = false;
                getDate();
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

                    if (status.equalsIgnoreCase("success")) {
                        activity.addFragment(new AvailableBusesFragment());

                      /*  JSONArray jArray=jsonObject.getJSONArray("stationList");
                        for (int i=0;i<jArray.length();i++){
                            JSONObject jObj=jArray.getJSONObject(i);
                            RouteListBean bean=new RouteListBean();

                            bean.stationId=jObj.getString("stationId");
                            bean.station_name=jObj.getString("station_name");

                            sourceList.add(bean);
                        }

                        desList.addAll(sourceList);

                        //custom adapter for source spinner
                        SourceLocationAdapter adapterSource = new SourceLocationAdapter(activity, sourceList);
                        spinnerSource.setAdapter(adapterSource);

                        //custom adapter for Dest spinner
                        DestLocationAdapter adapterDest = new DestLocationAdapter(activity, desList);
                        spinnerDest.setAdapter(adapterDest);*/

                    } else {
                        MyToast.getInstance(activity).customToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNetError() {
                Progress.hide(activity);
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
            Date fDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(fromDate);
            Date sDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(toDate);

            if (fromDate.isEmpty()) {
                MyToast.getInstance(activity).customToast("Please select from date");
                return false;
            } else {
                if (sDate.before(fDate)) {
                    MyToast.getInstance(activity).customToast("Please select to date which is greater than from date");
                    return false;
                } else return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


/* get Date end here */
}

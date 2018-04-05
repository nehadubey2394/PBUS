package com.pbus.volleymultipart;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pbus.R;
import com.pbus.helper.SessionManager;
import com.pbus.utility.Progress;
import com.pbus.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mindiii on 5/4/18.
 */

public abstract class VolleyGetPost {

    private String TAG;
    private int retryTime=20000;
    private String url;
    private Boolean isMethodPost,isDialogShow=false;
    private Activity activity;

    public VolleyGetPost(Activity activity, String url,Boolean isMethodPost,String TAG){
        this.activity=activity;
        this.url=url;
        this.isMethodPost=isMethodPost;
        this.TAG=TAG;
    }

    public void executeVolley(){
        int methodType=(isMethodPost)?Request.Method.POST:Request.Method.GET;

        if (Util.isNetworkAvailable(activity, activity.getWindow().getDecorView())) {
            Util.hideSoftKeyboard(activity);
            Progress.show(activity);
            StringRequest stringRequest = new StringRequest(methodType, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Util.e(TAG, "onResponse: " + response);
                            Progress.hide(activity);
                            onVolleyResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Progress.hide(activity);
                            NetworkResponse networkResponse = error.networkResponse;
                            String errorMessage;

                            if (networkResponse == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    errorMessage = "Request timeout";
                                    Util.showToast(activity, errorMessage, Toast.LENGTH_SHORT);
                                } else if (error.getClass().equals(NoConnectionError.class)) {
                                    errorMessage = "Failed to connect server, please try again";
                                    Util.showToast(activity, errorMessage, Toast.LENGTH_SHORT);
                                }
                            } else {
                                String result = new String(networkResponse.data);
                                try {
                                    JSONObject response = new JSONObject(result);
                                    String status = response.getString("responseCode");
                                    String message = response.getString("message");

                                    Util.e("Error Status", "" + status);
                                    Util.e("Error Message", message);
                                    errorMessage = message;

                                    if (status.equals("300")) {
                                        try {
                                            if (isDialogShow) {
                                                return;
                                            }

                                            final Dialog dialog = new Dialog(activity);

                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setContentView(R.layout.dialog_session_expire);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.setCancelable(false);
                                            dialog.setCanceledOnTouchOutside(false);

                                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                                            dialog.findViewById(R.id.okLay).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    SessionManager.getInstance().logout(activity);
                                                }
                                            });

                                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    isDialogShow = false;
                                                }
                                            });

                                            dialog.show();
                                              isDialogShow = true;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Util.showToast(activity, errorMessage, Toast.LENGTH_SHORT);
                                    }
                                    /*else if (!(errorMessage.equals("Invalid Auth Token"))) {
                                        Util.showToast(activity, errorMessage, Toast.LENGTH_SHORT);
                                    }
                                    if (networkResponse.statusCode == 300) {
                                        errorMessage = message + "Please login again";
                                    }
                                    else if (networkResponse.statusCode == 404) {
                                        errorMessage = "Resource not found";
                                    } else if (networkResponse.statusCode == 401) {
                                        errorMessage = message + " Please login again";
                                    } else if (networkResponse.statusCode == 400) {
                                        errorMessage = message + " Check your inputs";
                                    } else if (networkResponse.statusCode == 500) {
                                        errorMessage = message + "Ooops! Something went wrong,";
                                    }*/

                                } catch (JSONException e) {
                                    Util.showToast(activity,activity.getResources().getString(R.string.something_wrong),0);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    return setHeaders(header);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    return setParams(params);
                }
            };

            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest, TAG);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    retryTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else{
            onNetError();
        }
    }

    /***
     * @param retryTime set the second for 30 sec pass 30000
     */
    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    /***
     * @param response use this response
     */
    public abstract void onVolleyResponse(String response);

    /**
     * This method will be executed if internet connection is not there
     * Don't forget to dismiss the progressbar in this (if there)
     */
    public abstract void onNetError();

    /***
     * @param params you just need to call params.put(Key,Value)
     * @return params Do not Return null if method is post
     */
    public abstract Map<String, String> setParams(Map<String, String> params);

    /***
     * @param params you just need to call params.put(Key,Value)
     * @return params Do not return null
     */
    public abstract Map<String, String> setHeaders(Map<String, String> params);

}

package com.pbus.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pbus.R;
import com.pbus.snackBarPackage.TSnackbar;

/**
 * Created by mindiii on 10/4/18.
 */

public class MyToast {

    private Context context;
    private static MyToast instance;

    /**
     * @param context as parent context
     */
    private MyToast(Context context) {
        this.context = context;
    }

    /**
     * @param context as parent context
     * @return instance
     */
    public synchronized static MyToast getInstance(Context context) {
        if (instance == null) {
            instance = new MyToast(context);
        }
        return instance;
    }

    public void showCustomAlert(String title, String message) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_alert_layout, null);
        TextView tv_title = layout.findViewById(R.id.tv_title);
        TextView msgTv = layout.findViewById(R.id.tv_msg);
        tv_title.setText(title);
        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showLogoutAlert(String title, String message) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_alert_layout, null);
        TextView tv_title = layout.findViewById(R.id.tv_title);
        TextView msgTv = layout.findViewById(R.id.tv_msg);
        tv_title.setText(title);
        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void customToast(final String msg) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView tv_msg = layout.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen._10sdp));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showToast(String message, int len) {
        Toast.makeText(context, message, len).show();
    }

    public void snackbar(View coordinatorLayout, String message) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView =  sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#ffffff"));  //old color 1976d2
        textView.setGravity(Gravity.CENTER);
        snackbar.setActionTextColor(Color.parseColor("#1976d2"));
        sbView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));  //Color.WHITE
        snackbar.show();

    }

    public void snackbarTop(View coordinatorLayout, String message) {

        TSnackbar snackbar = TSnackbar.make(coordinatorLayout, message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#FF419CF5"));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#FF419CF5"));
        TextView textView =  snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}

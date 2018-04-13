package com.pbus.utility.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pbus.R;
import com.pbus.utility.MyToast;
import com.pbus.utility.Util;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static Listener listener;

    public void setListener(Listener listener){
        NetworkChangeReceiver.listener = listener;
    }

    public interface Listener{
        void onNetworkChange(boolean isConnected);
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {
        Util.e("network","newtwork change");
        int status = NetworkUtil.getConnectivityStatusString(context);

        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

                if(listener !=null){
                    listener.onNetworkChange(false);
                }else {
                    MyToast.getInstance(context).customToast(context.getString(R.string.error_msg_network));
                }
            } else {
                if(listener !=null) listener.onNetworkChange(true);
            }
        }
    }
}
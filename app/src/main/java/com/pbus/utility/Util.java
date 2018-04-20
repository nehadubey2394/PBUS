package com.pbus.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.snackBarPackage.TSnackbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mindiii on 5/4/18.
 */

public class Util {

    public static void e(String tag , String msg){
        //  Log.e(tag,msg);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity == null) return;
            if (activity.getCurrentFocus() == null) return;

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String format12HourTime(String bus_time, String pFormat, String dFormat) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat(pFormat, Locale.US);
            SimpleDateFormat displayFormat = new SimpleDateFormat(dFormat, Locale.US);
            Date dTime = parseFormat.parse(bus_time);
            return displayFormat.format(dTime);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(String date, String pFormat, String dFormat) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat(pFormat, Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat(dFormat, Locale.getDefault());
            Date dTime = parseFormat.parse(date);
            return displayFormat.format(dTime);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return (day + "-" + month + "-" + year);

    }

    public static String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return (hour + ":" + minute);
    }

    public static boolean isPasswordMatching(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmPassword);

        return matcher.matches();
    }

    public static String getHashKey(String packageName, Activity context) {
        PackageInfo info;
        String something = "";
        try {
            info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                something = new String(Base64.encode(md.digest(), 0));

                Util.e("hash key", something);
            }
        } catch (Exception e1) {
            Util.e("name not found", e1.toString());
        }
        return something;
    }

    public static void writeFile(String fileUrl, String filepath) {
        int count;
        InputStream input;
        OutputStream output;
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            input = new BufferedInputStream(url.openStream(), 8192);

            File dir = Environment.getExternalStorageDirectory();

            output = new FileOutputStream(dir + filepath);

            byte data[] = new byte[4096];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static StringBuilder readFile(String filename) {

        StringBuilder offlineText = new StringBuilder();

        File dir = Environment.getExternalStorageDirectory();

        File filepath = new File(dir, filename);

        if (filepath.exists()) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath), 8192);
                String line;

                while ((line = br.readLine()) != null) {
                    offlineText.append(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Util.e("File Doesn't Exists!!!", ">>>>>>>>>");
        }

        return offlineText;

    }

    public static void downloadFileFromServer(String fileURL, String fileName) {

        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();

        double MB_Available = (avail_sd_space / 10485783);

        Util.e("MB", "" + MB_Available);
        try {

            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            int fileSize = c.getContentLength() / 1048576;

            if (MB_Available <= fileSize) {
                c.disconnect();
                return;
            }

            try {
                FileOutputStream f = new FileOutputStream(new File(fileName));
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
            } catch (Exception e) {

                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static boolean isNetworkAvailable(Context context, View coordinatorLayout) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }

    public static boolean isNetworkAvailableTop(Context context, View coordinatorLayout) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            TSnackbar snackbar = TSnackbar
                    .make(coordinatorLayout, "No internet connection!", TSnackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#0000CC"));
            TextView textView =  snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)

                for (NetworkInfo anInfo : info)
                {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }

        return false;
    }

}

package com.pbus.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.pbus.helper.Constant;

import static com.pbus.helper.Constant.REQUEST_ID_MULTIPLE_PERMISSIONS;

/**
 * Created by mindiii on 12/12/17.
 */

public class Permission extends Activity {
    private Context context;

    public Permission(Context context) {
        this.context = context;
    }

    //Permission function starts from here
    public boolean RequestMultiplePermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (FirstPermissionResult != PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult != PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            // Creating String Array with Permissions.
            ActivityCompat.requestPermissions((Activity) context, new String[]
                    {
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        } else {
            return true;
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.MY_PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }



    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    Constant.MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        } else {
            return true;
        }
    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean locationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean externalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && locationPermission && externalStoragePermission) {

                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }
                break;

        }
    }

}


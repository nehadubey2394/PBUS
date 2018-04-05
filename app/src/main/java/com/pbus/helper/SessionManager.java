package com.pbus.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pbus.activity.WelcomeActivity;
import com.pbus.utility.PBUS;



/*
 * Created by mindiii on 8/12/17.
 */

public class SessionManager {
    private Context context;

    private static SessionManager instance = null;

    private final String PREF_NAME = "PBUS";
    private final String IS_LOGGEDIN2 = "isLoggedIn2";



    private SharedPreferences mypref;
    private SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        this.context = context;
        mypref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor.apply();
    }

    public static SessionManager getInstance() {
        if ((instance != null)) {
            return instance;
        }
        instance = new SessionManager(PBUS.getInstance().getApplicationContext());
        return instance;
    }


 /*   public void createSession(UserInfo userInfo) {
        editor.putString(USER_ID, userInfo.userID);
        editor.putString(EMAIL, userInfo.email);
        editor.putString(FULLNAME, userInfo.fullName);
        editor.putString(USERNAME, userInfo.userName);
        editor.putString(USERGENDER, userInfo.userGender);
        editor.putString(USERIMAGE, userInfo.userImage);
        editor.putString(USERLOGINTIME, userInfo.loginTime);
        editor.putString(STAGE_NAME, userInfo.stageName);
        editor.putString(VENUE_NAME, userInfo.venuName);
        editor.putString(ARTIST_TYPE, userInfo.artistType);
        editor.putString(FIRST_NAME, userInfo.firstName);
        editor.putString(LAST_NAME, userInfo.lastName);
        editor.putString(ENV, userInfo.environment);
        editor.putString(FACEBOOK_ID, userInfo.facebookId);
        editor.putBoolean(TRYDEMO_FIRST, userInfo.firstTimeDemo);
        editor.putString(ACCESSTOEKN, userInfo.userAccessToken);
        editor.putString(MAKE_ADMIN, userInfo.makeAdmin);
        editor.putString(LATITUDE, userInfo.latitude);
        editor.putString(LONGITUDE, userInfo.longitude);
        editor.putString(ADDDRESS, userInfo.address);
        editor.putString(KEY_PONTS, userInfo.keyPoints);
        editor.putString(BIO, userInfo.bio);
        editor.putBoolean(CURRENT_LOCATION, userInfo.currentLocation);
        editor.putBoolean(IS_LOGGEDIN2, true);
        editor.commit();
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.userID=(mypref.getString(USER_ID, null));
        userInfo.email=(mypref.getString(EMAIL, null));
        userInfo.fullName =(mypref.getString(FULLNAME, null));
        userInfo.userName=(mypref.getString(USERNAME, null));
        userInfo.userGender=(mypref.getString(USERGENDER, null));
        userInfo.userImage=(mypref.getString(USERIMAGE, null));
        userInfo.loginTime=(mypref.getString(USERLOGINTIME, null));
        userInfo.stageName =(mypref.getString(STAGE_NAME, null));
        userInfo.venuName=(mypref.getString(VENUE_NAME, null));
        userInfo.artistType =(mypref.getString(ARTIST_TYPE, null));
        userInfo.firstName =(mypref.getString(FIRST_NAME, null));
        userInfo.lastName =(mypref.getString(LAST_NAME, null));
        userInfo.environment =(mypref.getString(ENV, null));
        userInfo.facebookId=(mypref.getString(FACEBOOK_ID, null));
        userInfo.firstTimeDemo=(mypref.getBoolean(TRYDEMO_FIRST, false));
        userInfo.userAccessToken=(mypref.getString(ACCESSTOEKN, null));
        userInfo.makeAdmin=(mypref.getString(MAKE_ADMIN, ADMIN_NO));
        userInfo.latitude=(mypref.getString(LATITUDE, null));
        userInfo.longitude=(mypref.getString(LONGITUDE, null));
        userInfo.address=(mypref.getString(ADDDRESS, ""));
        userInfo.keyPoints=(mypref.getString(KEY_PONTS, "0"));
        userInfo.bio=(mypref.getString(BIO, ""));
        userInfo.currentLocation=(mypref.getBoolean(CURRENT_LOCATION, false));
        return userInfo;
    }*/

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN2, false);
    }

    public void logout(Activity activity) {
        editor.putBoolean(IS_LOGGEDIN2, false);
        editor.clear();
        editor.commit();

        Intent intent = new Intent(activity, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();

    }


}

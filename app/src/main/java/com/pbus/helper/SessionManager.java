package com.pbus.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pbus.activity.WelcomeActivity;
import com.pbus.bean.RememberBean;
import com.pbus.bean.UserInfoBean;
import com.pbus.utility.PBUS;



/*
 * Created by mindiii on 8/12/17.
 */

public class SessionManager {
    private static final String PREF_NAME = "PBUS";
    private static final String PREF_REMEMBER_SELLER = "prefRememberSeller";
    private static final String PREF_REMEMBER_DRIVER = "prefRememberDriver";
    private static final String PREF_TYPE = "prefType";
    private static final String IS_LOGGEDIN = "isLoggedIn2";
    private static final String USER_ID = "userId";
    private static final String FULLNAME = "fullName";
    private static final String EMAIL = "email";
    private static final String USER_TYPE = "userType";
    private static final String USERIMAGE = "userImage";
    private static final String AUTH_TOKEN = "authToken";
    private static final String THUMB_IMAGE = "thumbImage";
    private static final String TYPE="type";
    private static final String REMEMBER_NAME="rememberName";
    private static final String REMEMBER_PWD="rememberPwd";
    private static final String TYPE2="type";
    private static final String REMEMBER_NAME2="rememberName";
    private static final String REMEMBER_PWD2="rememberPwd";
    private static SessionManager instance = null;
    private Context context;
    private SharedPreferences mypref;
    private SharedPreferences.Editor editor;

    private SharedPreferences myprefSeller;
    private SharedPreferences.Editor editorSeller;

    private SharedPreferences myprefDriver;
    private SharedPreferences.Editor editorDriver;

    public SessionManager(Context context) {
        this.context = context;
        mypref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor.apply();

        myprefSeller = this.context.getSharedPreferences(PREF_REMEMBER_SELLER, Context.MODE_PRIVATE);
        editorSeller = myprefSeller.edit();
        editorSeller.apply();

        myprefDriver = this.context.getSharedPreferences(PREF_REMEMBER_DRIVER, Context.MODE_PRIVATE);
        editorDriver = myprefDriver.edit();
        editorDriver.apply();
    }

    public static SessionManager getInstance() {
        if ((instance != null)) {
            return instance;
        }
        instance = new SessionManager(PBUS.getInstance().getApplicationContext());
        return instance;
    }


    public void createRememberSeller(RememberBean rememberBean){
        editorSeller.putInt(TYPE, rememberBean.type);
        editorSeller.putString(REMEMBER_NAME, rememberBean.name);
        editorSeller.putString(REMEMBER_PWD, rememberBean.pwd);
        editorSeller.commit();
    }

    public RememberBean getRememberSeller(){
        RememberBean rememberBean=new RememberBean();
        rememberBean.type=(myprefSeller.getInt(TYPE, 0));
        rememberBean.name=(myprefSeller.getString(REMEMBER_NAME, ""));
        rememberBean.pwd=(myprefSeller.getString(REMEMBER_PWD, ""));
        return rememberBean;
    }

    public void createRememberDriver(RememberBean rememberBean){
        editorDriver.putInt(TYPE2, rememberBean.type);
        editorDriver.putString(REMEMBER_NAME2, rememberBean.name);
        editorDriver.putString(REMEMBER_PWD2, rememberBean.pwd);
        editorDriver.commit();
    }

    public RememberBean getRememberDriver(){
        RememberBean rememberBean=new RememberBean();
        rememberBean.type=(myprefDriver.getInt(TYPE2, 0));
        rememberBean.name=(myprefDriver.getString(REMEMBER_NAME2, ""));
        rememberBean.pwd=(myprefDriver.getString(REMEMBER_PWD2, ""));
        return rememberBean;
    }


    public void createSession(UserInfoBean userInfo) {
        editor.putString(USER_ID, userInfo.userId);
        editor.putString(FULLNAME, userInfo.full_name);
        editor.putString(EMAIL, userInfo.email);
        editor.putString(USER_TYPE, userInfo.user_type);
        editor.putString(USERIMAGE, userInfo.image);
        editor.putString(AUTH_TOKEN, userInfo.authToken);
        editor.putString(THUMB_IMAGE, userInfo.thumbImage);

        editor.putBoolean(IS_LOGGEDIN, true);
        editor.commit();
    }

    public UserInfoBean getUserInfo() {
        UserInfoBean userInfo = new UserInfoBean();
        userInfo.userId=(mypref.getString(USER_ID, ""));
        userInfo.full_name=(mypref.getString(FULLNAME, ""));
        userInfo.email =(mypref.getString(EMAIL, ""));
        userInfo.user_type=(mypref.getString(USER_TYPE, ""));
        userInfo.image=(mypref.getString(USERIMAGE, ""));
        userInfo.authToken=(mypref.getString(AUTH_TOKEN, ""));
        userInfo.thumbImage=(mypref.getString(THUMB_IMAGE, ""));

        return userInfo;
    }

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN, false);
    }

    public void logout(Activity activity, int preSelect) {
        editor.putBoolean(IS_LOGGEDIN, false);
        editor.clear();
        editor.commit();

        Intent intent = new Intent(activity, WelcomeActivity.class);
        intent.putExtra("preSelect", preSelect);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }


}

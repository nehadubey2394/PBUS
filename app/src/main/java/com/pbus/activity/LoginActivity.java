package com.pbus.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pbus.R;
import com.pbus.bean.RememberBean;
import com.pbus.bean.UserInfoBean;
import com.pbus.helper.SessionManager;
import com.pbus.helper.Webservices;
import com.pbus.utility.MyToast;
import com.pbus.utility.PBUS;
import com.pbus.utility.Util;
import com.pbus.utility.Validation;
import com.pbus.volleymultipart.VolleyGetPost;
import com.pbus.volleymultipart.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context=this;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private CheckBox cbRemember;
    private EditText etEmail,etPwd;
    private RememberBean rememberBean;
    private TextInputLayout inputLayEmail,inputLayPwd;

    private static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        setRememberMeData();
    }

    private void initView() {
        cbRemember=findViewById(R.id.cbRemember);
        etEmail=findViewById(R.id.etEmail);
        etPwd=findViewById(R.id.etPwd);
        inputLayEmail=findViewById(R.id.inputLayEmail);
        inputLayPwd=findViewById(R.id.inputLayPwd);
        setOnClick(findViewById(R.id.imgBack),findViewById(R.id.tvLogin),findViewById(R.id.tvForgotPwd));
    }

    private void setRememberMeData() {
        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getInt("type");
            if (type==1) rememberBean=PBUS.sessionManager.getRememberSeller();
            else rememberBean=PBUS.sessionManager.getRememberDriver();
        }

        etEmail.setText(rememberBean.name);
        etPwd.setText(rememberBean.pwd);
        if (etEmail.getText().toString().isEmpty())cbRemember.setChecked(false);
        else cbRemember.setChecked(true);
    }

    private void setOnClick(View... view) {
        for (View v:view)v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.tvForgotPwd:
                MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev));
                break;

            case R.id.tvLogin:
                if (isValidateInput()) {
                    String userType;
                    if (type==1) userType="seller";
                    else userType="driver";
                    doLogin(etEmail.getText().toString(),etPwd.getText().toString(),userType);
                }
                break;
        }
    }

    private void doLogin(final String email, final String pwd, final String userType) {
        new VolleyGetPost(LoginActivity.this, Webservices.LOGIN,true,TAG) {
            @Override
            public void onVolleyResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("status");
                    String message=jsonObject.getString("message");

                    if (success.equalsIgnoreCase("success")){
                        UserInfoBean userInfoBean=new UserInfoBean();
                        JSONObject obj=jsonObject.getJSONObject("userDetail");

                        userInfoBean.userId=obj.getString("userId");
                        userInfoBean.full_name=obj.getString("full_name");
                        userInfoBean.email=obj.getString("email");
                        userInfoBean.user_type=obj.getString("user_type");
                        userInfoBean.image=obj.getString("image");
                        userInfoBean.authToken=obj.getString("authToken");
                        userInfoBean.thumbImage=obj.getString("thumbImage");

                        PBUS.sessionManager.createSession(userInfoBean);

                        if (type == 1) {
                            setRememberMe(1);
                            Intent intent = new Intent(context, SellerMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (type == 2) {
                            setRememberMe(2);
                            Intent intent = new Intent(context, DriverHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        MyToast.getInstance(context).showCustomAlert("Alert!","Login successfully");
                        finish();
                    }else{
                       MyToast.getInstance(context).showCustomAlert("Alert!",message);
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
                params.put("email", email);
                params.put("password", pwd);
                params.put("userType", userType);

                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        }.executeVolley();

    }

    private void setRememberMe(int type){
        if (cbRemember.isChecked()){
            RememberBean rememberBean=new RememberBean();
            rememberBean.type=type;
            rememberBean.name=etEmail.getText().toString();
            rememberBean.pwd=etPwd.getText().toString();

            if (type==1)PBUS.sessionManager.createRememberSeller(rememberBean);
            else PBUS.sessionManager.createRememberDriver(rememberBean);
        }
    }

    private boolean isValidateInput(){
        Validation validate=new Validation();

        if (validate.getString(etEmail).isEmpty()){
            inputLayEmail.setError("Please enter email id");
            inputLayEmail.requestFocus();
            return false;
        }
        else if(!validate.isEmailValid(etEmail)){
            inputLayEmail.setError("Enter valid email id");
            inputLayEmail.requestFocus();
            return false;
        }else if (validate.getString(etPwd).isEmpty()){
            inputLayEmail.setError(null);
            inputLayEmail.setErrorEnabled(false);
            inputLayPwd.setError("Please enter password");
            inputLayPwd.requestFocus();
            return false;
        }else if (!validate.isPasswordValid(etPwd)){
            inputLayEmail.setError(null);
            inputLayEmail.setErrorEnabled(false);
            inputLayPwd.setError("At least 6 digit required");
            inputLayPwd.requestFocus();
            return false;
        }else{
            inputLayEmail.setError(null);
            inputLayEmail.setErrorEnabled(false);
            inputLayPwd.setError(null);
            inputLayPwd.setErrorEnabled(false);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}

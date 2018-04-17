package com.pbus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.bean.RememberBean;
import com.pbus.bean.UserInfoBean;
import com.pbus.helper.Webservices;
import com.pbus.utility.MyToast;
import com.pbus.utility.PBUS;
import com.pbus.utility.Util;
import com.pbus.utility.Validation;
import com.pbus.volleymultipart.VolleyGetPost;
import com.pbus.volleymultipart.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static int type;
    private Context context = this;
    private CheckBox cbRemember;
    private EditText etEmail, etPwd, etEmailForgot;
    private RememberBean rememberBean;
    private TextInputLayout inputLayEmail,inputLayPwd,ilEmailForgot;
    private RelativeLayout rlForgotPwd;
    private ImageView imgToolbarBack;
    private TextView tvLogin, tvForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        //header
        findViewById(R.id.imgDrawerMenu).setVisibility(View.GONE);

        etEmailForgot = findViewById(R.id.etEmailForgot);
        cbRemember=findViewById(R.id.cbRemember);
        etEmail=findViewById(R.id.etEmail);
        etPwd=findViewById(R.id.etPwd);
        inputLayEmail=findViewById(R.id.inputLayEmail);
        inputLayPwd=findViewById(R.id.inputLayPwd);
        ilEmailForgot=findViewById(R.id.ilEmailForgot);
        setOnClick(findViewById(R.id.imgBack), tvLogin = findViewById(R.id.tvLogin), tvForgotPwd = findViewById(R.id.tvForgotPwd), findViewById(R.id.tvSubmit),
                rlForgotPwd=findViewById(R.id.rlForgotPwd),imgToolbarBack=findViewById(R.id.imgToolbarBack));

        setRememberMeData();
    }

    private void setRememberMeData() {
        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getInt("type");
            if (type==1) rememberBean=PBUS.sessionManager.getRememberSeller();
            else rememberBean=PBUS.sessionManager.getRememberDriver();
        }
        inputLayEmail.setHintAnimationEnabled(false);
        inputLayPwd.setHintAnimationEnabled(false);

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

            case R.id.imgToolbarBack:
                rlForgotPwd.setVisibility(View.GONE);
                break;

            case R.id.tvForgotPwd:
                TextView tvTitle=findViewById(R.id.tvToolbarTitle);
                tvTitle.setText(R.string.forgot_password_titl);
                imgToolbarBack.setVisibility(View.VISIBLE);
                rlForgotPwd.setVisibility(View.VISIBLE);
                break;

            case R.id.rlForgotPwd:  //forgot pwd main layout
                rlForgotPwd.setVisibility(View.GONE);
                etEmailForgot.setText("");
                forgotErrorClear();
                break;

            case R.id.tvLogin:
                if (isValidateInput()) {
                    tvForgotPwd.setEnabled(false);
                    tvLogin.setEnabled(false);
                    String userType;
                    if (type==1) userType="seller";
                    else userType="driver";
                    doLogin(etEmail.getText().toString(),etPwd.getText().toString(),userType);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvForgotPwd.setEnabled(true);
                        tvLogin.setEnabled(true);
                    }
                }, 4000);
                break;

            case R.id.tvSubmit:

                if (isValidateEmail(etEmailForgot)) {
                    forgotErrorClear();
                    tvForgotPwd.setEnabled(false);
                    tvLogin.setEnabled(false);
                    rlForgotPwd.setVisibility(View.GONE);
                    forgotPwdApi(etEmailForgot.getText().toString().trim());
                    etEmailForgot.setText("");
                    // MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev));
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvForgotPwd.setEnabled(true);
                        tvLogin.setEnabled(true);
                    }
                }, 4000);
                break;
        }
    }

    private void forgotPwdApi(final String email) {
        new VolleyGetPost(LoginActivity.this, Webservices.FORGOT, true, TAG) {
            @Override
            public void onVolleyResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        MyToast.getInstance(context).customToast(message);
                    } else {
                        MyToast.getInstance(context).customToast("Please enter correct email id");
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
                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        }.executeVolley();
    }

    private void doLogin(final String email, final String pwd, final String userType) {

        new VolleyGetPost(LoginActivity.this, Webservices.LOGIN,true,TAG) {
            @Override
            public void onVolleyResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message=jsonObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
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
                        finish();
                    }else{
                        if (message.equalsIgnoreCase("Please choose correct user type")) {
                            MyToast.getInstance(context).customToast("Email Id and Password doesn't match with the user type");
                        } else if (message.equalsIgnoreCase("Wrong password") | message.equalsIgnoreCase("Invalid Email or Password")) {
                            MyToast.getInstance(context).customToast("Invalid Email Id or Password");
                        } else MyToast.getInstance(context).customToast(message);
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
                params.put("deviceToken", "123456");  //firebase device token
                params.put("deviceType", "2");

                return params;
            }

            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        }.executeVolley();

    }

    private void setRememberMe(int type){
        RememberBean rememberBean=new RememberBean();
        if (cbRemember.isChecked()){
            rememberBean.type=type;
            rememberBean.name=etEmail.getText().toString();
            rememberBean.pwd=etPwd.getText().toString();

        }else{
            rememberBean.type=type;
            rememberBean.name="";
            rememberBean.pwd="";
        }
        if (type==1)PBUS.sessionManager.createRememberSeller(rememberBean);
        else PBUS.sessionManager.createRememberDriver(rememberBean);
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

    private boolean isValidateEmail(EditText etEmailForgot){
        Validation validate=new Validation();

        if (validate.getString(etEmailForgot).isEmpty()){
            ilEmailForgot.setError("Please enter email id");
            ilEmailForgot.requestFocus();
            return false;
        }
        else if(!validate.isEmailValid(etEmailForgot)){
            ilEmailForgot.setError("Enter valid email id");
            ilEmailForgot.requestFocus();
            return false;
        }else{
            forgotErrorClear();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (rlForgotPwd.getVisibility()==View.VISIBLE)
        {
            rlForgotPwd.setVisibility(View.GONE);
            forgotErrorClear();
        }
        else{
            VolleySingleton.getInstance(context).cancelPendingRequests(TAG);
            super.onBackPressed();
            this.finish();
        }
    }

    private void forgotErrorClear() {
        ilEmailForgot.setError(null);
        ilEmailForgot.setErrorEnabled(false);
    }

    private void focusClear() {
        etPwd.clearFocus();
        Util.hideSoftKeyboard(this);
    }


}

package com.pbus.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pbus.R;
import com.pbus.utility.MyToast;
import com.pbus.utility.Util;
import com.pbus.volleymultipart.VolleyGetPost;

import java.sql.Driver;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setOnClick(findViewById(R.id.imgBack),findViewById(R.id.tvLogin));
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

            case R.id.tvLogin:
                MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev));
                Intent i=new Intent(context, SellerMainActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

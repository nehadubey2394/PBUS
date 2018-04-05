package com.pbus.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pbus.R;
import com.pbus.utility.Util;

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
                Util.customToast(context,getResources().getString(R.string.underDev));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

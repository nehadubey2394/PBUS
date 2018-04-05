package com.pbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pbus.R;
import com.pbus.utility.PBUS;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showSplash();
    }

    private void showSplash() {
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                Intent i;
                if (PBUS.sessionManager.isLoggedIn()) {
                    i = new Intent(SplashActivity.this, WelcomeActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, WelcomeActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 3 * 1000); // wait for 3 seconds

    }



}

package com.pbus.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.helper.SessionManager;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private int type, lastClicked = 0;
    private Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setClick(findViewById(R.id.mainLaySeller),findViewById(R.id.mainLayDriver));
    }

    private void setClick(View... view) {
        for (View v:view) v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.mainLaySeller:  setLastClickedRes();  setResours((FrameLayout) v,true); lastClicked = 0;  callIntent(1); break;

            case R.id.mainLayDriver:  setLastClickedRes();  setResours((FrameLayout) v,true); lastClicked = 1 ; callIntent(2); break;

        }
    }

    private void callIntent(int type) {
        Intent i=new Intent(context,LoginActivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }

    private void setLastClickedRes(){
        switch (lastClicked){
            case  0: setResours((FrameLayout) findViewById(R.id.mainLaySeller),false); break;
            case  1: setResours((FrameLayout) findViewById(R.id.mainLayDriver),false); break;
        }
    }

    /* active inactive icons */
    private void setResours(FrameLayout v, boolean isActive) {

        ImageView vi = (ImageView) v.getChildAt(1);
        TextView vt = (TextView) v.getChildAt(2);
        CardView right= (CardView) v.getChildAt(3);
        if(isActive){
            vi.setBackground(getResources().getDrawable(R.drawable.circle));
            vi.setImageResource(v.getId()==R.id.mainLayDriver?R.drawable.ic_driver_active:R.drawable.ic_seller_active);
            vt.setTextColor(getResources().getColor(R.color.colorPrimary));
            right.setVisibility(View.VISIBLE);
        }
        else {
            vi.setBackgroundResource(R.drawable.transparent);
            vi.setImageResource(v.getId()==R.id.mainLayDriver?R.drawable.ic_driver:R.drawable.ic_seller);
            vt.setTextColor(getResources().getColor(R.color.grey2));
            right.setVisibility(View.INVISIBLE);
        }
    }

}

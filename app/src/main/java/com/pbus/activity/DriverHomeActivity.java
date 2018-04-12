package com.pbus.activity;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.adapter.DrawerAdapter;
import com.pbus.bean.UserInfoBean;
import com.pbus.fragment.driver.NoBusFragment;
import com.pbus.fragment.seller.NewBookingFragment;
import com.pbus.listener.AdapterListener;
import com.pbus.utility.MyToast;
import com.pbus.utility.PBUS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DriverHomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterListener {

    private Context context=this;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout mainView;
    private ImageView imgDrawerCancel,imgDrawerMenu,imgProfile;
    private TextView tvUserType,tvFullName;

    private DrawerAdapter drawerAdapter;
    private ArrayList<String> drawerItemList;

    private boolean doubleBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        initView();
        initNavigationDrawer();
    }


    private void initView() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawerMain);
        mainView = findViewById(R.id.mainView);
        imgDrawerCancel=findViewById(R.id.imgDrawerCancel);
        imgProfile=findViewById(R.id.imgProfile);
        tvFullName=findViewById(R.id.tvFullName);
        tvUserType=findViewById(R.id.tvUserType);

        RecyclerView rvDriver=findViewById(R.id.rvDriver);
        TextView title= findViewById(R.id.tvToolbarTitle);
        title.setText(R.string.home);

        setClick(imgDrawerMenu=findViewById(R.id.imgDrawerMenu));

        drawerItemList=new ArrayList<>();
        drawerAdapter=new DrawerAdapter(context,drawerItemList);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(context);
        rvDriver.setLayoutManager(manager);
        rvDriver.setAdapter(drawerAdapter);
        drawerAdapter.setDrawerListener(this);

        replaceFragment(new NoBusFragment());
    }


    private void setClick(View... views) {
        for (View v:views)v.setOnClickListener(this);
    }

    public void initNavigationDrawer() {
        addItemInList();
        setProfileData();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                imgDrawerCancel.setVisibility(View.GONE);
                imgDrawerMenu.setVisibility(View.VISIBLE);
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                imgDrawerMenu.setVisibility(View.GONE);
                imgDrawerCancel.setVisibility(View.VISIBLE);
                super.onDrawerOpened(v);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                mainView.bringChildToFront(drawerView);
                mainView.requestLayout();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        actionBarDrawerToggle.syncState();
    }

    private void setProfileData() {
        UserInfoBean bean= PBUS.sessionManager.getUserInfo();

        Picasso.with(context).load(bean.thumbImage).placeholder(R.drawable.ic_profile_holder).into(imgProfile);
        tvFullName.setText(bean.full_name);
        tvUserType.setText(bean.user_type);
    }

    private void addItemInList() {
        for (int i = 0; i <= 5; i++) {
            String item="";
            switch (i) {
                case 0:
                    item="Home";
                    break;
                case 1:
                    item="Search Customer";
                    break;
                case 2:
                    item="Profile";
                    break;
                case 3:
                    item="Terms and conditions";
                    break;
                case 4:
                    item="About us";
                    break;
                case 5:
                    item="Logout";
                    break;
            }
            drawerItemList.add(item);
        }
        drawerAdapter.notifyDataSetChanged();
    }

    private void replaceFragment(Fragment fragmentHolder) {
        try{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String fragmentName = fragmentHolder.getClass().getName();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);  //animation
            fragmentTransaction.replace(R.id.frame_fragments_Driver, fragmentHolder,fragmentName).addToBackStack(fragmentName);
            fragmentTransaction.commit();
            hideKeyBoard();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Fragment addFragment(Fragment fragmentHolder) {
        try{ FragmentManager fragmentManager = getSupportFragmentManager();
            String fragmentName = fragmentHolder.getClass().getName();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setEnterTransition(null);
            }
            fragmentTransaction.add(R.id.frame_fragments_Driver, fragmentHolder,fragmentName).addToBackStack(fragmentName);
            fragmentTransaction.commit();

            hideKeyBoard();
            return fragmentHolder;}
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void hideKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        Handler handler = new Handler();
        Runnable runnable;
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    doubleBackPress = false;
                }
            }, 1000);
            if (doubleBackPress) {
                handler.removeCallbacks(runnable);
                finish();
            } else {
                MyToast.getInstance(context).snackbar(imgProfile,"Press back again to exit");
                doubleBackPress = true;
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgDrawerMenu:
                if (drawerLayout.isDrawerOpen(navigationView)) drawerLayout.closeDrawers();
                else drawerLayout.openDrawer(navigationView);
                break;
        }
    }

    @Override
    public void drawerItemSelected(int position) {
        switch (position){
            case 0: MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev)); drawerLayout.closeDrawers(); break;
            case 1: MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev)); drawerLayout.closeDrawers(); break;
            case 2: MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev)); drawerLayout.closeDrawers(); break;
            case 3: MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev)); drawerLayout.closeDrawers(); break;
            case 4: MyToast.getInstance(context).customToast(getResources().getString(R.string.underDev)); drawerLayout.closeDrawers(); break;
            case 5: MyToast.getInstance(context).showLogoutAlert("Alert!","Logout successfully");
                PBUS.sessionManager.logout(DriverHomeActivity.this); drawerLayout.closeDrawers(); break;
        }
    }
}

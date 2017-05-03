package com.bridge.soom.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.R;

import static com.bridge.soom.Helper.Constants.DEVICE_ID;
import static com.bridge.soom.Helper.Constants.SPLASHTIME;
import static java.security.AccessController.getContext;

public class SplashScreenActivity extends BaseActivity {
    // private ImageView splash_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesManager.init(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);
        ImageView  splash_icon = (ImageView) findViewById(R.id.splash_icon);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein_splash);
        splash_icon.startAnimation(animationFadeIn);

        saveDeviceTockennDeviceID();

    }

    private void startLOCATION() {

        if (Build.VERSION.SDK_INT < 23) {

            //We already have permission. Write your function call over hear
           onlocc();
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Here we are asking for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                //If the app is running for second time, then we already have permission. You can write your function here, if we already have permission.
           
onlocc();

            }

        }
    }

    private void onlocc() {

     
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            }
            else {
                gotoLogin();
            }
        
        
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    private void gotoLogin() {

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent (SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASHTIME);


    }

    public void saveDeviceTockennDeviceID() {
      String deviceID=  getDeviceId();
        Log.i("deviceid",deviceID);

        SharedPreferencesManager.write(DEVICE_ID,deviceID);//save string in shared preference.
    }

     protected String getDeviceId() {

          return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
           }

    @Override
    protected void onResume() {
        super.onResume();

        startLOCATION();

    }
}

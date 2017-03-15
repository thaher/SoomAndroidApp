package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
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

}

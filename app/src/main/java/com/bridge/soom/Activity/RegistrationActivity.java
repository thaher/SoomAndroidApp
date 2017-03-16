package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.R;

public class RegistrationActivity extends BaseActivity {
    LinearLayout seeker,provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        seeker = (LinearLayout)findViewById(R.id.seeker);
        provider = (LinearLayout)findViewById(R.id.provider);
        seeker.setClickable(true);
        provider.setClickable(true);
        seeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity(0);
            }
        });
        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity(1);
            }
        });

    }

    private void nextActivity(Integer select) {
        Intent intent = new Intent (RegistrationActivity.this, RegistrationFillActivity.class);
        intent.putExtra("SELECT", select);
        startActivity(intent);
    }

}

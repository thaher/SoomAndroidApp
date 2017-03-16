package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.R;

public class RegistrationPVRActivity extends BaseActivity {
    ImageButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent (RegistrationPVRActivity.this, RegistrationPVRDetailesActivity.class);
                startActivity(intent);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

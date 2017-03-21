package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.R;


public class RegistrationPVRDetailesActivity extends BaseActivity {
    private static final int REQUEST_CODE = 221;
    private  String gendertext,edutext,emptext,dobtext,addresstext,experincetext,desigtext,hourlytext,langugetext,imguri;
    private NetworkManager networkManager;
     private EditText subservice,service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvrdetailes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        service = (EditText) findViewById(R.id.service);
        subservice = (EditText) findViewById(R.id.subservice);

        gendertext = getIntent().getStringExtra("gender");
                edutext  = getIntent().getStringExtra("edu");
        emptext = getIntent().getStringExtra("emp");
                dobtext = getIntent().getStringExtra("dob");
        addresstext = getIntent().getStringExtra("add");
                experincetext = getIntent().getStringExtra("exp");
        desigtext = getIntent().getStringExtra("desig");
                hourlytext = getIntent().getStringExtra("hour");
        langugetext = getIntent().getStringExtra("lang");
        imguri= getIntent().getStringExtra("img");
        networkManager = new NetworkManager(this);

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RegistrationPVRDetailesActivity.this,GetCatActivity.class),REQUEST_CODE);

            }
        });

  // your code here
//                Log.i("Reg2_submit", " ------");
//                Log.i("Reg2_submit", " -----------------item name n pos " + categoriesName.get(position) + " " + categoriesId.get(position));
//
//                networkManager.new RetrieveGetSubCategoryListTask(RegistrationPVRDetailesActivity.this, categoriesId.get(position))
//                        .execute();
//                subservice.setVisibility(View.VISIBLE);
//

//
//
//



    }




}

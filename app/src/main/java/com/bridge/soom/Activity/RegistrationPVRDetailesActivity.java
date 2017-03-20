package com.bridge.soom.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;

import java.util.ArrayList;
import java.util.List;

public class RegistrationPVRDetailesActivity extends BaseActivity implements RegistrationProviderResponse {
    private  String gendertext,edutext,emptext,dobtext,addresstext,experincetext,desigtext,hourlytext,langugetext,imguri;
    private NetworkManager networkManager;
    List<String> categoriesName,categoriesId;
    Spinner spincat;
    ArrayAdapter<String> catdataAdapter,dataAdapter2,dataAdapter3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvrdetailes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spincat = (Spinner) findViewById(R.id.spincat);

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
        categoriesId = new ArrayList<>();
        categoriesId.add("0");
        categoriesName = new ArrayList<>();
        categoriesName.add("Choose Service");
        catdataAdapter = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, categoriesName){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.hintColor));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        catdataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spincat.setAdapter(catdataAdapter);








        Log.i("Intent",""+ gendertext+edutext+emptext+dobtext+addresstext+experincetext+desigtext+hourlytext+langugetext+imguri);
        networkManager.new RetrieveGetCategoryListTask(RegistrationPVRDetailesActivity.this)
                .execute();




    }



    @Override
    public void failedtoConnect() {

    }

    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catname) {

        for (int i=0;i<catid.size();i++)
        {
            Log.i("Reg2_submit ",catname.get(i));
            categoriesName.add(catname.get(i));
            categoriesId.add(catid.get(i));
        }


        catdataAdapter.notifyDataSetChanged();
    }
}

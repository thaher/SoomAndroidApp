package com.bridge.soom.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.RecyclerAdapService;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.Services;
import com.bridge.soom.R;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalDetailsActivity extends AppCompatActivity implements GetCatDatas {
    private RecyclerView recyclerView;
    private RecyclerAdapService mAdapter;
    private List<Services> servicesList = new ArrayList<>();
    private ImageButton addservice;
    private PopupWindow mpopup;
    private NetworkManager networkManager;
    private ArrayAdapter<String> dataAdapter,dataAdapter2;
    private List<String> services,filters,servicesid,filtersid;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private Spinner service;
    private Spinner subservice;
    private LinearLayout subservicex;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        networkManager = new NetworkManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addservice = (ImageButton)findViewById(R.id.addservice);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        services = new ArrayList<String>();
        servicesid = new ArrayList<String>();
        servicesid.clear();
        services.clear();
        services.add("Choose a Service");
        servicesid.add("0");

        filters = new ArrayList<String>();
        filtersid = new ArrayList<String>();
        filters.clear();
        filtersid.clear();
        filters.add("Choose filters");
        filtersid.add("0");
        dataAdapter = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, services){
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
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        // Creating adapter for spinner
        dataAdapter2 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, filters){
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
        dataAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner

    }

    private void showPop() {
        View popUpView = getLayoutInflater().inflate(R.layout.popup_window,
                null); // inflating popup layout
        mpopup = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true); // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);


                    mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying popup


         service = (Spinner) popUpView.findViewById(R.id.service);
         subservice = (Spinner) popUpView.findViewById(R.id.subservice);
       subservicex = (LinearLayout) popUpView.findViewById(R.id.subservicex);


        service.setAdapter(dataAdapter);


        subservice.setAdapter(dataAdapter2);
        networkManager.new RetrieveGetCategoryListTask(ProfessionalDetailsActivity.this)
                .execute();
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {  networkManager.new RetrieveGetSubCategoryListTask(ProfessionalDetailsActivity.this, servicesid.get(position))
                        .execute();
                    subservicex.setVisibility(View.VISIBLE);}
                else {
                    subservicex.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void failedtoConnect() {
        snackbar = Snackbar
                .make(cordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryListFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryList(final List<String> catid, final List<String> catname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                services.clear();
                servicesid.clear();
                services.add("Choose a Service");
                servicesid.add("0");
                for(int i=0;i<catname.size();i++)
                {
                    services.add(catname.get(i));
                    servicesid.add(catid.get(i));
                }
                dataAdapter.notifyDataSetChanged();
                Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );

            }
        });
    }

    @Override
    public void GetSubCategoryList(final List<String> subcatid, final List<String> subcatname, String highestWage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                filters.clear();
                filtersid.clear();
                filters.add("Choose filters");
                filtersid.add("0");
                for(int i=0;i<subcatid.size();i++)
                {
                    filters.add(subcatname.get(i));
                    filtersid.add(subcatid.get(i));
                }



                dataAdapter2.notifyDataSetChanged();
                Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );

            }
        });

    }

    @Override
    public void GetSubCategoryListFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetStateCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetStateListFailed(String msg) {

    }

    @Override
    public void GetCityCategoryList(List<String> subcatid, List<String> subcatname, List<String> lat, List<String> lng) {

    }

    @Override
    public void GetCityListFailed(String msg) {

    }

    @Override
    public void GetCountryCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetCountryListFailed(String msg) {

    }
}

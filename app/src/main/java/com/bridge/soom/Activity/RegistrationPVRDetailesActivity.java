package com.bridge.soom.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.PlacesAutoCompleteAdapter;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;


public class RegistrationPVRDetailesActivity extends BaseActivity implements AdapterView.OnItemClickListener , RegistrationProviderResponse {
    private static final int REQUEST_CODE = 221;
    private  String gendertext,edutext,emptext,dobtext,addresstext,experincetext,desigtext,hourlytext,langugetext,imguri,servicetext,filtertext,loctext,loctext2,loctext3,countrytext,statetext;
    private NetworkManager networkManager;
     private Spinner subservice,service,state;
    private RelativeLayout subservicex;
    private AutoCompleteTextView choselocation,choselocation1,choselocation2;

    private  EditText chosecountry;
    List<String> services,filters,servicesid,filtersid,stateid,statels;
    PlacesAutoCompleteAdapter mPlacesAdapter;
    GoogleApiClient mGoogleApiClientloc;
    ImageButton fab;

    private ArrayAdapter<String> dataAdapter,dataAdapter2,dataAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvrdetailes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        service = (Spinner) findViewById(R.id.service);
        subservice = (Spinner) findViewById(R.id.subservice);
        state = (Spinner) findViewById(R.id.state);

        subservicex = (RelativeLayout) findViewById(R.id.subservicex);

        choselocation = (AutoCompleteTextView) findViewById(R.id.choselocation);
        choselocation1 = (AutoCompleteTextView) findViewById(R.id.choselocation1);
        choselocation2 = (AutoCompleteTextView) findViewById(R.id.choselocation2);
        chosecountry = (EditText) findViewById(R.id.chosecountry);
        fab = (ImageButton) findViewById(R.id.fab);

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

//        service.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(RegistrationPVRDetailesActivity.this,GetCatActivity.class),REQUEST_CODE);
//
//            }
//        });
//
//        choselocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
//
//        choselocation.setOnItemClickListener(this);
        chosecountry.setText("India");
//        choosestate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(RegistrationPVRDetailesActivity.this,GetStateActivity.class),REQUEST_CODE);
//
//            }
//        });

        mGoogleApiClientloc = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();// location and plces api for search bar
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastKnownLocation != null) {
                mPlacesAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                        mGoogleApiClientloc, toBounds(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 12000), null);
            }
        }


        choselocation.setOnItemClickListener(mAutocompleteClickListener);
        choselocation.setAdapter(mPlacesAdapter);
        choselocation1.setOnItemClickListener(mAutocompleteClickListener);
        choselocation1.setAdapter(mPlacesAdapter);
        choselocation2.setOnItemClickListener(mAutocompleteClickListener);
        choselocation2.setAdapter(mPlacesAdapter);

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

        statels = new ArrayList<String>();
        stateid = new ArrayList<String>();
        statels.clear();
        stateid.clear();
        statels.add("Choose a State");
        stateid.add("0");
        // Creating adapter for spinner
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
        service.setAdapter(dataAdapter);

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
        subservice.setAdapter(dataAdapter2);


        // Creating adapter for spinner
        dataAdapter3 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, statels){
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
        dataAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        state.setAdapter(dataAdapter3);



        Log.i("Reg2_submit","RetrieveGetCategoryListTask");
        networkManager.new RetrieveGetCategoryListTask(RegistrationPVRDetailesActivity.this)
                .execute();

        networkManager.new RetrieveGetStateListTask(RegistrationPVRDetailesActivity.this)
                .execute();
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position!=0) {  networkManager.new RetrieveGetSubCategoryListTask(RegistrationPVRDetailesActivity.this, servicesid.get(position))
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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields())
                {
//                    servicetext
//                            filtertext,
//                            loctext,
//                            loctext2,
//                            loctext3,
//                            countrytext,
//                            statetext

                }
            }
        });
    }

    private boolean validateFields() {
        if(service.getSelectedItemPosition()==0)
        {
            //snack bar
            return false;

        }
        else if(subservice.getSelectedItemPosition()==0)
        {
            //snack bar
            return false;

        }
        else if(choselocation.getText().toString().isEmpty())
        {
            //snack bar
            return false;


        }
        else if(chosecountry.getText().toString().isEmpty())
        {
            //snack bar

            return false;

        }
        else if(state.getSelectedItemPosition()==0)
        {
            //snackbar
            return false;

        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void failedtoConnect() {

    }

    @Override
    public void GetCategoryListFailed(String msg) {

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
    public void GetSubCategoryList(final List<String> subcatid, final List<String> subcatname) {

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

    }

    @Override
    public void GetStateCategoryList(final List<String> subcatid, final List<String> subcatname) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                statels.clear();
                stateid.clear();
                statels.add("Choose a State");
                stateid.add("0");
                for(int i=0;i<subcatname.size();i++)
                {
                    statels.add(subcatname.get(i));
                    stateid.add(subcatid.get(i));
                }


                dataAdapter3.notifyDataSetChanged();
                Log.i("Reg2_submit","GetStateCategoryList ---got2" );

            }
        });


    }

    @Override
    public void GetStateListFailed(String msg) {

    }


    /* bounding lat long */
    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }


    /* autovomplete search bar*/
    AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            final String placeDesc = String.valueOf(item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClientloc, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /* adding new story by placing a marker on the result location */
    ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // storymarker placing
            final Place place = places.get(0);


            Log.i("place", "place buffer" + place.toString() + " lat lng " + place.getLatLng() + " adess" + place.getAddress() + " place name " + place.getName().toString());
            // Selecting the first object buffer.
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClientloc.connect();


    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClientloc.disconnect();

    }
}

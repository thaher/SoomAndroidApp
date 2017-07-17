package com.bridge.soom.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.MultiSpinner;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.PlacesAutoCompleteAdapter;
import com.bridge.soom.Helper.RecyclerAdapLocation;
import com.bridge.soom.Helper.RecyclerAdapService;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.ServiceandLocListner;
import com.bridge.soom.Model.PlaceLoc;
import com.bridge.soom.Model.Services;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;

public class ProfessionalDetailsActivity extends AppCompatActivity implements GetCatDatas,MultiSpinner.MultiSpinnerListener , ServiceandLocListner {
    private RecyclerView recyclerView,recyclerViewLoc;
    private RecyclerAdapService mAdapter;
    private RecyclerAdapLocation mAdapterloc;

    private List<Services> servicesList ;
    private List<PlaceLoc> locList ;
    private ImageButton addservice,addlocation;
    private NetworkManager networkManager;
    private ArrayAdapter<String> dataAdapter,dataAdapter2;
    private List<String> services,filters,servicesid,filtersid,Sfilters,SfiltersID;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private Spinner service;
    private MultiSpinner subservice;
    private LinearLayout subservicex;
    private ViewGroup hiddenPanel;
    private ViewGroup hiddenPanelloc;
    private ImageButton close_popup;
    private ImageButton close_popup_loc;
    private Button submit;
    private Button submit_loc;
    private EditText wages,experiance;
    private boolean selection =false;

    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private GoogleApiClient mGoogleApiClientloc;
    private AutoCompleteTextView choselocation;
    private Place place1;
    private ImageButton mSwitchShowSecure;
    private String AccessTocken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        networkManager = new NetworkManager(this);
        SharedPreferencesManager.init(this);
        AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");

        hiddenPanel = (ViewGroup)findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        hiddenPanelloc = (ViewGroup)findViewById(R.id.hidden_panel_loc);
        hiddenPanelloc.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewLoc = (RecyclerView) findViewById(R.id.recycler_view_lang);
        addservice = (ImageButton)findViewById(R.id.addservice);
        addlocation = (ImageButton)findViewById(R.id.addlocation);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        close_popup = (ImageButton) findViewById(R.id.close_popup);
        close_popup_loc = (ImageButton) findViewById(R.id.close_popup_loc);
        choselocation = (AutoCompleteTextView) findViewById(R.id.choselocation);


        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown(v);

            }
        });
        addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place1 = null;
                slideUpDownLoc(v);


            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManagerLang = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLoc.setLayoutManager(mLayoutManagerLang);
        recyclerViewLoc.setItemAnimator(new DefaultItemAnimator());

        servicesList = new ArrayList<Services>();
        servicesList.clear();
        locList = new ArrayList<PlaceLoc>();
        locList.clear();
        mAdapter = new RecyclerAdapService(servicesList,ProfessionalDetailsActivity.this,ProfessionalDetailsActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapterloc = new RecyclerAdapLocation(locList,ProfessionalDetailsActivity.this,ProfessionalDetailsActivity.this);
        recyclerViewLoc.setAdapter(mAdapterloc);

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
//        filters.add("Choose filters");
//        filters.add("Choose filters");
//        filters.add("Choose filters");
//        filters.add("Choose filters");
        filtersid.add("0");


        service = (Spinner) findViewById(R.id.service);
        subservice = (MultiSpinner) findViewById(R.id.subservice);
        submit = (Button) findViewById(R.id.submit);
        submit_loc = (Button) findViewById(R.id.submitloc);
        experiance = (EditText) findViewById(R.id.experiance);
        wages = (EditText) findViewById(R.id.wages);
        subservicex = (LinearLayout) findViewById(R.id.subservicex);
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
                    tv.setTextColor(getResources().getColor(R.color.bpDark_gray));
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        // Creating adapter for spinner


        service.setAdapter(dataAdapter);

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
                    tv.setTextColor(getResources().getColor(R.color.bpDark_gray));
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
//        subservice.setAdapter(dataAdapter2);


        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {  networkManager.new RetrieveGetSubCategoryListTask(ProfessionalDetailsActivity.this, servicesid.get(position))
                        .execute();}
                else {
                    subservicex.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        networkManager.new RetrieveGetCategoryListTask(ProfessionalDetailsActivity.this)
                .execute();
           networkManager.new RetrieveSelectionServiceTask(ProfessionalDetailsActivity.this,AccessTocken)
                .execute();
 networkManager.new RetrieveLocationTask(ProfessionalDetailsActivity.this,AccessTocken)
                .execute();


        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown(v);
            }
        });
        close_popup_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDownLoc(v);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    Services newService = new Services();
                    newService.setServiceId(servicesid.get(service.getSelectedItemPosition()));
                    newService.setServiceName(services.get(service.getSelectedItemPosition()));
                    newService.setSubServiceId(SfiltersID);
                    newService.setSubServiceName(Sfilters);
                    newService.setExperiance(experiance.getText().toString().trim());
                    newService.setWages(wages.getText().toString().trim());
                    servicesList.add(newService);
                    mAdapter.notifyDataSetChanged();
                    slideUpDown(v);
                    networkManager.new AddServiceTask(ProfessionalDetailsActivity.this,AccessTocken,newService)
                            .execute();



                }
            }
        });
        submit_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid_loc())
                {
                        try  {
                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {

                    }
                    PlaceLoc newLoc = new PlaceLoc();
                    newLoc.setAddress(place1.getAddress().toString());
                    newLoc.setLatitude(String.valueOf(place1.getLatLng().latitude));
                    newLoc.setLongitude(String.valueOf(place1.getLatLng().longitude));

//                    place1.getAddress().
//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(ProfessionalDetailsActivity.this, Locale.getDefault());
//
//                    try {
//                        addresses = geocoder.getFromLocation(place1.getLatLng().latitude, place1.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
////                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
////                        String city = addresses.get(0).getLocality();
////                        String state = addresses.get(0).getAdminArea();
////                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
////                        String knownName = addresses.get(0).getFeatureName();
//                        Log.i("PROFFF", " postalCode"+ postalCode);
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Log.i("PROFFF", " postalCode"+ e.getMessage());
//
//                    }




                    locList.add(newLoc);
                    mAdapterloc.notifyDataSetChanged();
                    slideUpDownLoc(v);
                    networkManager.new AddLocationTask(ProfessionalDetailsActivity.this,AccessTocken,newLoc)
                            .execute();



                }
            }
        });

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
            else {
                mPlacesAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                        mGoogleApiClientloc, toBounds(new LatLng(9.9312, 76.2673), 12000), null);
            }
        }

        choselocation.setOnItemClickListener(mAutocompleteClickListener);
        choselocation.setAdapter(mPlacesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.prof_menu, menu);
        mSwitchShowSecure = (ImageButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servicesList.size()>0&&locList.size()>0)
                {
                    Intent intent = new Intent(ProfessionalDetailsActivity.this, PersonalDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    snackbar = Snackbar
                            .make(cordi,R.string.proferror, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }
        });
        return true;
    }
    private boolean isValid_loc() {
        if(place1==null&&choselocation.getText().toString().trim().isEmpty())
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.service_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }

        return true;
    }

    private boolean isValid() {
        if(service.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.service_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else   if(checkin(servicesList,servicesid.get(service.getSelectedItemPosition())))
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.service_exist, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else  if(!selection)
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.subservice_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if (experiance.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.experiance_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }else if (wages.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.wages_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        return true;
    }

    private boolean checkin(List<Services> servicesList, String s) {

        for(Services service : servicesList)
        {
            if(service.getServiceId().trim().equals(s))
            {    return true;}

        }
        return false;
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
    public void GetServiceListFailed(String msg) {

    }

    @Override
    public void GetServiceList(final List<Services> services) {
        Log.i("PROFFFSER"," parser ading..." +services.size());
        Log.i("PROFFFSER"," parser ading..." +services.size());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                servicesList.clear();
                for (Services cat : services) {
                    servicesList.add(cat);
                }
                mAdapter.notifyDataSetChanged();

            }
        });

    }
// @Override
//    public void GetServiceList(List<Services> servicesList) {
//
//    }

    @Override
    public void GetLocationList(final List<PlaceLoc> placeLocList) {
        Log.i("PROFFFLOC"," parser ading..." +placeLocList.size());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locList.clear();
                for (PlaceLoc cat : placeLocList) {
                    locList.add(cat);
                }
                mAdapterloc.notifyDataSetChanged();



            }
        });
    }

    @Override
    public void GetLocationListFailed(String msg) {

    }

    @Override
    public void AddLocationSuccess() {
        snackbar = Snackbar
                .make(cordi, R.string.location_add_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkManager.new RetrieveLocationTask(ProfessionalDetailsActivity.this, AccessTocken)
                        .execute();
            }});

    }

    @Override
    public void AddLocationFailed(String msg) {
        snackbar = Snackbar
                .make(cordi, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkManager.new RetrieveLocationTask(ProfessionalDetailsActivity.this,AccessTocken)
                        .execute();}});
    }

    @Override
    public void DeleteLocationSuccess() {
        snackbar = Snackbar
                .make(cordi, "Success", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        networkManager.new RetrieveLocationTask(ProfessionalDetailsActivity.this,AccessTocken)
                .execute();}});

    }

    @Override
    public void DeleteLocationFailed(String msg) {
        snackbar = Snackbar
                .make(cordi, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void AddServiceSuccess() {
        snackbar = Snackbar
                .make(cordi, R.string.service_add_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        networkManager.new RetrieveSelectionServiceTask(ProfessionalDetailsActivity.this,AccessTocken)
                .execute();}});

    }

    @Override
    public void AddServiceFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

        networkManager.new RetrieveSelectionServiceTask(ProfessionalDetailsActivity.this,AccessTocken)
                .execute();}});


    }

    @Override
    public void DeleteServiceSuccess() {
        snackbar = Snackbar
                .make(cordi,"Success !", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkManager.new RetrieveSelectionServiceTask(ProfessionalDetailsActivity.this, AccessTocken)
                        .execute();
            }});

    }

    @Override
    public void DeleteServiceFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
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
//                filters.add("Choose filters");
//                filtersid.add("0");
                for(int i=0;i<subcatid.size();i++)
                {
                    filters.add(subcatname.get(i));
                    filtersid.add(subcatid.get(i));
                }

                subservice.setItems(filters, "Choose a Specialization", ProfessionalDetailsActivity.this);
                dataAdapter2.notifyDataSetChanged();

                subservicex.setVisibility(View.VISIBLE);
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

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
            service.setSelection(0);
            experiance.setText("");
            wages.setText("");

        }
    }
    public void slideUpDownLoc(final View view) {
        if (!isPanelShownLoc()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);

            hiddenPanelloc.startAnimation(bottomUp);
            hiddenPanelloc.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom);

            hiddenPanelloc.startAnimation(bottomDown);
            hiddenPanelloc.setVisibility(View.GONE);
            choselocation.setText("");


        }
    }
    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }
    private boolean isPanelShownLoc() {
        return hiddenPanelloc.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        selection =false;
        Sfilters = new ArrayList<String>();
        SfiltersID = new ArrayList<String>();
        for (int i = 0; i < filters.size(); i++) {
            if (selected[i]) {
               Log.i("SELECTED"," "+filters.get(i));
               Log.i("SELECTED"," "+filtersid.get(i));
                Sfilters.add(filters.get(i));
                SfiltersID.add(filtersid.get(i));
                selection =true;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mAdapter != null) {
            mAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public void editService(Services providerBasic) {
    }

    public void deleteService(Services providerBasic) {
        networkManager.new DeleteServiceTask(ProfessionalDetailsActivity.this,AccessTocken,providerBasic.getServiceId())
                .execute();


    }

    public void editLocation(PlaceLoc providerBasic) {
    }

    public void deleteLocation(PlaceLoc providerBasic) {
        networkManager.new DeleteLocationTask(ProfessionalDetailsActivity.this,AccessTocken,providerBasic.getId())
                .execute();
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
            Log.i("PLACEs"," "+placeId+" "+placeDesc+" ");


            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClientloc, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /*  result location */
    ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {
                Log.e("PLACEs", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                Log.i("PLACEs","Place query did not complete. Error: " +
                        places.getStatus().toString());

                return;
            }
            // storymarker placing
            final Place place = places.get(0);
            place1 = place;
            Log.i("PLACEs"," "+place.getAddress());

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

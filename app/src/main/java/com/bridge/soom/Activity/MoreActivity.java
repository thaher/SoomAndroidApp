package com.bridge.soom.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.PlacesAutoCompleteAdapter;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Interface.UpdateProfileResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MoreActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener ,UpdateProfileResponse , GetCatDatas {
        private UserModel userModel;
    private ToggleButton mSwitchShowSecure;
    private TextView tvgenderset,tvdobset,tvaddressset,tveduset,tvdesigset,tvexperset,tvwagesset,tvskillset, tvlanguageset,tvemptypeset ,
            tvlocset,tvloc2set,tvloc3set,tvserviceset,tvsubserviceset,tvcountryset,tvstateset,tvcityset;
    private EditText  evaddressset,eveduset,evdesigset,evexperset,evwagesset,evskillset,
    evlanguageset,evemptypeset;
    Spinner spinner , subservice,service,state,city,country;
    ArrayAdapter<String> dataAdapterx,dataAdapter,dataAdapter2,dataAdapter3,dataAdapter4,dataAdapter5 ;
    List<String> categories,services,filters,servicesid,filtersid,stateid,statels,cityid,cityname,countryname,countryid;

    CalendarDatePickerDialogFragment cdp;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;

    private AutoCompleteTextView choselocation,choselocation1,choselocation2;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private GoogleApiClient mGoogleApiClientloc;

    private NetworkManager networkManager;
    private Integer clicked=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        networkManager = new NetworkManager(this);

        userModel = (UserModel) getIntent().getSerializableExtra("userMore");
        tvgenderset = (TextView) findViewById(R.id.tvgenderset);

        tvdobset = (TextView) findViewById(R.id.tvdobset);
        tvaddressset = (TextView) findViewById(R.id.tvaddressset);
        tveduset = (TextView) findViewById(R.id.tveduset);
        tvdesigset = (TextView) findViewById(R.id.tvdesigset);
        tvexperset = (TextView) findViewById(R.id.tvexperset);
        tvwagesset = (TextView) findViewById(R.id.tvwagesset);
        tvskillset = (TextView) findViewById(R.id.tvskillset);
        tvlanguageset = (TextView) findViewById(R.id.tvlanguageset);
        tvemptypeset = (TextView) findViewById(R.id.tvemptypeset);
        tvserviceset = (TextView) findViewById(R.id.tvserviceset);
        tvsubserviceset = (TextView) findViewById(R.id.tvsubserviceset);

        tvlocset = (TextView) findViewById(R.id.tvlocset);
        tvloc2set = (TextView) findViewById(R.id.tvloc2set);
        tvloc3set = (TextView) findViewById(R.id.tvloc3set);
        tvcountryset = (TextView) findViewById(R.id.tvcountryset);
        tvstateset = (TextView) findViewById(R.id.tvstateset);
        tvcityset = (TextView) findViewById(R.id.tvcityset);





        evaddressset = (EditText) findViewById(R.id.evaddressset);
        eveduset = (EditText) findViewById(R.id.eveduset);
        evdesigset = (EditText) findViewById(R.id.evdesigset);
        evexperset = (EditText) findViewById(R.id.evexperset);
        evwagesset = (EditText) findViewById(R.id.evwagesset);
        evskillset = (EditText) findViewById(R.id.evskillset);
        evlanguageset = (EditText) findViewById(R.id.evlanguageset);
        evemptypeset = (EditText) findViewById(R.id.evemptypeset);

        cordi = (CoordinatorLayout)findViewById(R.id.cordi);


        spinner = (Spinner) findViewById(R.id.spingender);
       service = (Spinner) findViewById(R.id.service);
        subservice = (Spinner) findViewById(R.id.subservice);
        state = (Spinner) findViewById(R.id.state);
        city = (Spinner) findViewById(R.id.city);
        country = (Spinner) findViewById(R.id.country);

        choselocation = (AutoCompleteTextView) findViewById(R.id.choselocation);
        choselocation1 = (AutoCompleteTextView) findViewById(R.id.choselocation1);
        choselocation2 = (AutoCompleteTextView) findViewById(R.id.choselocation2);

        choselocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    clicked=0;
                }
            }
        });
        choselocation1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    clicked=1;
                }
            }
        });
        choselocation2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    clicked=2;
                }
            }
        });

      setupADPTS();





        if(userModel!=null)
        {
          loadMore();
        }


        cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(MoreActivity.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, null)
                .setDoneText("Done")
                .setCancelText("Cancel");


        tvdobset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdp.show(getSupportFragmentManager(), "DATE_FRAG");
            }
        });


    }

    private void setupADPTS() {


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
        choselocation1.setOnItemClickListener(mAutocompleteClickListener);
        choselocation1.setAdapter(mPlacesAdapter);
        choselocation2.setOnItemClickListener(mAutocompleteClickListener);
        choselocation2.setAdapter(mPlacesAdapter);


        categories = new ArrayList<String>();
        categories.add("*Gender");
        categories.add("Female");
        categories.add("Male");
        categories.add("Others");

        // Creating adapter for spinner
        dataAdapterx = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, categories){
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
        dataAdapterx.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapterx);

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
        cityname = new ArrayList<String>();
        cityid = new ArrayList<String>();
        cityname.clear();
        cityid.clear();
        cityname.add("Choose a City");
        cityid.add("0");

        countryname = new ArrayList<String>();
        countryid = new ArrayList<String>();
        countryname.clear();
        countryid.clear();
        countryname.add("Choose a Country");
        countryid.add("0");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, services){
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
        dataAdapter2 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, filters){
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
        dataAdapter3 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, statels){
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


        // Creating adapter for spinner
        dataAdapter4 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, cityname){
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
        dataAdapter4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        city.setAdapter(dataAdapter4);



        // Creating adapter for spinner
        dataAdapter5 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, countryname){
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
        dataAdapter5.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        country.setAdapter(dataAdapter5);


        Log.i("Reg2_submit","RetrieveGetCategoryListTask");
        networkManager.new RetrieveGetCategoryListTask(MoreActivity.this)
                .execute();
        networkManager.new RetrieveGetCountryListTask(MoreActivity.this)
                .execute();

//        networkManager.new RetrieveGetStateListTask(MoreActivity.this)
//                .execute();
//
//

        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position!=0) {
                    networkManager.new RetrieveGetSubCategoryListTask(MoreActivity.this, servicesid.get(position))
                        .execute();
//                    subservicex.setVisibility(View.VISIBLE);}
//                else {
//                    subservicex.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position!=0) {
                networkManager.new RetrieveGetStateListTask(MoreActivity.this, countryid.get(position))
                        .execute();
//                    choosecity.setVisibility(View.VISIBLE);}
//                else {
//                    choosecity.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position!=0) {
                    networkManager.new RetrieveGetCityListTask(MoreActivity.this, stateid.get(position))
                        .execute();
//                    choosecity.setVisibility(View.VISIBLE);}
//                else {
//                    choosecity.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadMore() {

        if(userModel.getUserGender()!=null&&!userModel.getUserGender().isEmpty())
        {
            tvgenderset.setText(userModel.getUserGender().trim());
            spinner.setSelection(findinlist(categories,userModel.getUserGender().trim().toLowerCase()));
            Log.i("FINDLOG"," "+findinlist(categories,userModel.getUserGender().trim().toLowerCase()));


        }
        else {
            tvgenderset.setText("Not Set");

        }


        if(userModel.getDob()!=null&&!userModel.getDob().isEmpty())
        {
            tvdobset.setText(userModel.getDob().trim());

        }
        else {
            tvdobset.setText("Not Set");
        }


        if(userModel.getUserAddress()!=null&&!userModel.getUserAddress().isEmpty())
        {
            tvaddressset.setText(userModel.getUserAddress().trim());
            evaddressset.setText(userModel.getUserAddress().trim());
        }
        else {
            tvaddressset.setText("Not Set");
            evaddressset.setText("");
            evaddressset.setHint("Not Set");

        }


        if(userModel.getUserEducation()!=null&&!userModel.getUserEducation().isEmpty())
        {
            tveduset.setText(userModel.getUserEducation().trim());
            eveduset.setText(userModel.getUserEducation().trim());
        }
        else{
            tveduset.setText("Not Set");
            eveduset.setText("");
            eveduset.setHint("Not Set");

        }


        if(userModel.getUserDesignation()!=null&&!userModel.getUserDesignation().isEmpty())
        {
            tvdesigset.setText(userModel.getUserDesignation().trim());
            evdesigset.setText(userModel.getUserDesignation().trim());
        }
        else {
            tvdesigset.setText("Not Set");
            evdesigset.setText("");
            evdesigset.setHint("Not Set");

        }


        if(userModel.getUserExperience()!=null&&!userModel.getUserExperience().isEmpty())
        {
            tvexperset.setText(userModel.getUserExperience().trim());
            evexperset.setText(userModel.getUserExperience().trim());
        }
        else {
            tvexperset.setText("Not Set");
            evexperset.setText("");
            evexperset.setHint("Not Set");

        }


        if(userModel.getUserWagesHour()!=null&&!userModel.getUserWagesHour().toString().isEmpty())
        {
            tvwagesset.setText(userModel.getUserWagesHour().toString().trim());
            evwagesset.setText(userModel.getUserWagesHour().toString().trim());
        }
        else {
            tvwagesset.setText("Not Set");
            evwagesset.setText("");
            evwagesset.setHint("Not Set");

        }


        if(userModel.getUserAdditionalSkill()!=null&&!userModel.getUserAdditionalSkill().isEmpty())
        {
            tvskillset.setText(userModel.getUserAdditionalSkill().trim());
            evskillset.setText(userModel.getUserAdditionalSkill().trim());
        }
        else {
            tvskillset.setText("Not Set");
            evskillset.setText("");
            evskillset.setHint("Not Set");

        }


        if(userModel.getLanguagesknown()!=null&&!userModel.getLanguagesknown().isEmpty())
        {
            tvlanguageset.setText(userModel.getLanguagesknown().trim());
            evlanguageset.setText(userModel.getLanguagesknown().trim());
        }
        else {
            tvlanguageset.setText("Not Set");
            evlanguageset.setText("");
            evlanguageset.setHint("Not Set");

        }


            if(userModel.getEmploymentType()!=null&&!userModel.getEmploymentType().isEmpty())
            {
                tvemptypeset.setText(userModel.getEmploymentType().trim());
                evemptypeset.setText(userModel.getEmploymentType().trim());

            }
            else
        {   tvemptypeset.setText("Not Set");
        evemptypeset.setText("");
        evemptypeset.setHint("Not Set");}


        if(userModel.getCategoryName()!=null&&!userModel.getCategoryName()[0].isEmpty())
        {
            tvserviceset.setText(userModel.getCategoryName()[0].trim());
            service.setSelection(findinlist(services,userModel.getCategoryName()[0].trim().toLowerCase()));

        }
        else {
           tvserviceset.setText("Not Set");
        }






        if(userModel.getFilterName()!=null&&!userModel.getFilterName()[0].isEmpty())
        {
            tvsubserviceset.setText(userModel.getFilterName()[0].trim());
            subservice.setSelection(findinlist(filters,userModel.getFilterName()[0].trim().toLowerCase()));

        }
        else {
            tvserviceset.setText("Not Set");
        }


        if(userModel.getPreLocation1()!=null&&!userModel.getPreLocation1().isEmpty())
        {
            tvlocset.setText(userModel.getPreLocation1().trim());
            choselocation.setText(userModel.getPreLocation1().trim());

        }
        else {
           tvlocset.setText("Not Set");
            choselocation.setText("");
            choselocation.setHint("Prefered Location");

        }

        if(userModel.getPreLocation2()!=null&&!userModel.getPreLocation2().isEmpty())
        {
            tvloc2set.setText(userModel.getPreLocation2().trim());
            choselocation1.setText(userModel.getPreLocation2().trim());

        }
        else {
            tvloc2set.setText("Not Set");
            choselocation1.setText("");
            choselocation1.setHint("Prefered Location (optional)");


        }

        if(userModel.getPreLocation3()!=null&&!userModel.getPreLocation3().isEmpty())
        {
            tvloc3set.setText(userModel.getPreLocation3().trim());
            choselocation2.setText(userModel.getPreLocation3().trim());


        }
        else {
            tvloc3set.setText("Not Set");
            choselocation2.setText("");
            choselocation2.setHint("Prefered Location (optional)");


        }

        if(userModel.getCountryName()!=null&&!userModel.getCountryName().isEmpty())
        {
            tvcountryset.setText(userModel.getCountryName().trim());

            country.setSelection(findinlist(countryname,userModel.getCountryName().trim().toLowerCase()));
        }
        else {
            tvcountryset.setText("Not Set");
        }

        if(userModel.getStateName()!=null&&!userModel.getStateName().isEmpty())
        {
            tvstateset.setText(userModel.getStateName().trim());
            state.setSelection(findinlist(statels,userModel.getStateName().trim().toLowerCase()));

        }
        else {
            tvstateset.setText("Not Set");
        }


        if(userModel.getCityName()!=null&&!userModel.getCityName().isEmpty())
        {
           tvcityset.setText(userModel.getCityName().trim());
            city.setSelection(findinlist(cityname,userModel.getCityName().trim().toLowerCase()));

        }
        else
        {
           tvcityset.setText("Not Set");
        }
    }

    private int findinlist(List<String> services, String trim) {
        for(int i=0;i<services.size();i++)
        {
            Log.i("FINDCAT"," "+services.get(i).trim().toLowerCase()+" "+trim);
            if(services.get(i).trim().toLowerCase().equals(trim))
            {
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_more, menu);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Log.i("FRAG"," edit view  ----");

                    spinner.setVisibility(View.VISIBLE);
                    evaddressset.setVisibility(View.VISIBLE);
                    eveduset.setVisibility(View.VISIBLE);
                    evdesigset.setVisibility(View.VISIBLE);
                    evexperset.setVisibility(View.VISIBLE);
                    evwagesset.setVisibility(View.VISIBLE);
                    evskillset.setVisibility(View.VISIBLE);
                    evlanguageset.setVisibility(View.VISIBLE);
                    evemptypeset.setVisibility(View.VISIBLE);
                    choselocation.setVisibility(View.VISIBLE);
                    choselocation1.setVisibility(View.VISIBLE);
                    choselocation2.setVisibility(View.VISIBLE);

                    service.setVisibility(View.VISIBLE);
                    subservice.setVisibility(View.VISIBLE);
                    country.setVisibility(View.VISIBLE);
                    state.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);



                    tvgenderset.setVisibility(View.GONE);
                    tvaddressset.setVisibility(View.GONE);
                    tveduset.setVisibility(View.GONE);
                    tvdesigset.setVisibility(View.GONE);
                    tvexperset.setVisibility(View.GONE);
                    tvwagesset.setVisibility(View.GONE);
                    tvskillset.setVisibility(View.GONE);
                    tvlanguageset.setVisibility(View.GONE);
                    tvemptypeset.setVisibility(View.GONE);
                    tvlocset.setVisibility(View.GONE);
                    tvloc2set.setVisibility(View.GONE);
                    tvloc3set.setVisibility(View.GONE);
                    tvserviceset.setVisibility(View.GONE);
                    tvsubserviceset.setVisibility(View.GONE);
                    tvcountryset.setVisibility(View.GONE);
                    tvstateset.setVisibility(View.GONE);
                    tvcityset.setVisibility(View.GONE);


                } else {



                    if( saveMore())
                   {   //Your code when unchecked
                    Log.i("FRAG"," save or view----");
                    spinner.setVisibility(View.GONE);
                    evaddressset.setVisibility(View.GONE);
                    eveduset.setVisibility(View.GONE);
                    evdesigset.setVisibility(View.GONE);
                    evexperset.setVisibility(View.GONE);
                    evwagesset.setVisibility(View.GONE);
                    evskillset.setVisibility(View.GONE);
                    evlanguageset.setVisibility(View.GONE);
                    evemptypeset.setVisibility(View.GONE);
                    choselocation.setVisibility(View.GONE);
                    choselocation1.setVisibility(View.GONE);
                    choselocation2.setVisibility(View.GONE);

                    service.setVisibility(View.GONE);
                    subservice.setVisibility(View.GONE);
                    country.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);




                    tvgenderset.setVisibility(View.VISIBLE);
                    tvdobset.setVisibility(View.VISIBLE);
                    tvaddressset.setVisibility(View.VISIBLE);
                    tveduset.setVisibility(View.VISIBLE);
                    tvdesigset.setVisibility(View.VISIBLE);
                    tvexperset.setVisibility(View.VISIBLE);
                    tvwagesset.setVisibility(View.VISIBLE);
                    tvskillset.setVisibility(View.VISIBLE);
                    tvlanguageset.setVisibility(View.VISIBLE);
                    tvemptypeset.setVisibility(View.VISIBLE);
                    tvlocset.setVisibility(View.VISIBLE);
                    tvloc2set.setVisibility(View.VISIBLE);
                    tvloc3set.setVisibility(View.VISIBLE);

                    tvserviceset.setVisibility(View.VISIBLE);
                    tvsubserviceset.setVisibility(View.VISIBLE);
                    tvcountryset.setVisibility(View.VISIBLE);
                    tvstateset.setVisibility(View.VISIBLE);
                    tvcityset.setVisibility(View.VISIBLE);

                    loadMore();
                   }else {
                        mSwitchShowSecure.toggle();

                    }

                }
            }
        });
        return true;
    }

    private boolean isValid() {





     return true;
    }

    private boolean saveMore() {


        if(spinner.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the gender", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else
        {userModel.setUserGender(categories.get(spinner.getSelectedItemPosition()));}

        Log.i("FINDLOG"," save"+categories.get(spinner.getSelectedItemPosition()));
        if(tvdobset.getText().toString().trim().isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi,"Please set your DOB", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setDob(tvdobset.getText().toString().trim());

        }

        userModel.setUserAddress(evaddressset.getText().toString().trim());

        if(eveduset.getText().toString().trim().isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi,"Please type in your education", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else {        userModel.setUserEducation(eveduset.getText().toString().trim());
        }
        if(evdesigset.getText().toString().trim().isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi,"Please type in your Designation", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else {
            userModel.setUserDesignation(evdesigset.getText().toString().trim());}
        if(evexperset.getText().toString().trim().isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi,"Please type in your Experiance", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else {
            userModel.setUserExperience(evexperset.getText().toString().trim());
        }

        if(evwagesset.getText().toString().trim().isEmpty())
        {

            snackbar = Snackbar
                    .make(cordi,"Please type in your Wages per Hour", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {userModel.setUserWagesHour(Double.valueOf(evwagesset.getText().toString().trim()));}


        userModel.setUserAdditionalSkill(evskillset.getText().toString().trim());


        if(evlanguageset.getText().toString().trim().isEmpty())
        {

            snackbar = Snackbar
                    .make(cordi,"Please type in your Languages Known", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setLanguagesknown(evlanguageset.getText().toString().trim());
        }
        if(evemptypeset.getText().toString().trim().isEmpty())
        {

            snackbar = Snackbar
                    .make(cordi,"Please type in your Employment Type", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {userModel.setEmploymentType(evemptypeset.getText().toString().trim());}

        if(  userModel.getPreLocation1().isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi,"Please give atleast one Location", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }

        if(service.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the Service", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setCategoryNameindex(services.get(service.getSelectedItemPosition()),0);
        }
        if(subservice.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the Sub Service", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setFilterNameindex(filters.get(subservice.getSelectedItemPosition()),0);
            Log.i("TESTIN"," "+filters.get(subservice.getSelectedItemPosition()));
        }
        if(country.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the Country", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setCountryName(countryname.get(country.getSelectedItemPosition()));
        }
        if(state.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the State", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setStateName(statels.get(state.getSelectedItemPosition()));
        }
        if(city.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, "Please select the City", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else {
            userModel.setCityName(cityname.get(city.getSelectedItemPosition()));
        }
return true;

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            Log.i("DATE18","not 18");
            snackbar = Snackbar
                    .make(cordi, R.string.dob_noteighteen, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }else
        {
            Log.i("DATE18","yes 18");
            monthOfYear+=1;

            tvdobset.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

        }

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
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
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
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCityCategoryList(final List<String> subcatid, final List<String> subcatname, List<String> lat, List<String> lng) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cityname.clear();
                cityid.clear();
                cityname.add("Choose a City");
                cityid.add("0");
                for(int i=0;i<subcatname.size();i++)
                {
                    cityname.add(subcatname.get(i));
                    cityid.add(subcatid.get(i));
                }


                dataAdapter4.notifyDataSetChanged();
                Log.i("Reg2_submit","GetStateCategoryList ---got2" );

            }
        });
    }

    @Override
    public void GetCityListFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCountryCategoryList(final List<String> subcatid, final List<String> subcatname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                countryname.clear();
                countryid.clear();
                countryname.add("Choose a Country");
                countryid.add("0");
                for(int i=0;i<subcatname.size();i++)
                {
                    countryname.add(subcatname.get(i));
                    countryid.add(subcatid.get(i));
                }


                dataAdapter5.notifyDataSetChanged();
                Log.i("Reg2_submit","GetCountryCategoryList ---got2" );

            }
        });
    }

    @Override
    public void GetCountryListFailed(String msg) {
        snackbar = Snackbar
                .make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
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

            Log.i("place","clicked - "+clicked);

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
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // storymarker placing
            final Place place = places.get(0);



            Log.i("place", "place buffer "+ clicked+" "+ place.toString() + " lat lng " + place.getLatLng() + " adess" + place.getAddress() + " place name " + place.getName().toString());
            switch (clicked)
            {
                case 0:
                    userModel.setPreLocation1(place.getName().toString());
                    userModel.setPreLocation1Lat(String.valueOf(place.getLatLng().latitude));
                    userModel.setPreLocation1Long(String.valueOf(place.getLatLng().longitude));

                    break;
                case 1:userModel.setPreLocation2(place.getName().toString());
                    userModel.setPreLocation2Lat(String.valueOf(place.getLatLng().latitude));
                    userModel.setPreLocation2Long(String.valueOf(place.getLatLng().longitude));
                    break;
                case 2:userModel.setPreLocation3(place.getName().toString());
                    userModel.setPreLocation3Lat(String.valueOf(place.getLatLng().latitude));
                    userModel.setPreLocation3Long(String.valueOf(place.getLatLng().longitude));
                    break;
            }
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

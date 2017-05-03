package com.bridge.soom.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.bridge.soom.Helper.SharedPreferencesManager;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.URLHOST;
import static com.bridge.soom.Helper.Constants.URLUPLOADFINALREG;


public class RegistrationPVRDetailesActivity extends BaseActivity implements AdapterView.OnItemClickListener , RegistrationProviderResponse {
    private static final int REQUEST_CODE = 221;
    private  String gendertext,edutext,emptext,dobtext,addresstext,experincetext,desigtext,hourlytext,langugetext,imguri,
            servicetext,filtertext,loctext,loctext2,loctext3,countrytext,statetext,loc1lat,loc1longt,loc2lat,loc2longt,loc3lat,loc3long;
    private NetworkManager networkManager;
    private Place place1,place2 ,place3;
     private Spinner subservice,service,state,city;
    private RelativeLayout subservicex,choosecity;
    private AutoCompleteTextView choselocation,choselocation1,choselocation2;

    private  EditText chosecountry;
    List<String> services,filters,servicesid,filtersid,stateid,statels,cityid,cityname;
    PlacesAutoCompleteAdapter mPlacesAdapter;
    GoogleApiClient mGoogleApiClientloc;
    ImageButton fab;
    Integer clicked=0;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private ArrayAdapter<String> dataAdapter,dataAdapter2,dataAdapter3,dataAdapter4;
    private CircleImageView profile_image;
    private File imgaefile;

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
        city = (Spinner) findViewById(R.id.city);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        choosecity = (RelativeLayout) findViewById(R.id.choosecity);
        subservicex = (RelativeLayout) findViewById(R.id.subservicex);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image.setVisibility(View.GONE);
        choselocation = (AutoCompleteTextView) findViewById(R.id.choselocation);
        choselocation1 = (AutoCompleteTextView) findViewById(R.id.choselocation1);
        choselocation2 = (AutoCompleteTextView) findViewById(R.id.choselocation2);
        chosecountry = (EditText) findViewById(R.id.chosecountry);

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

      //  profile_image.setImageURI(Uri.parse(imguri));

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


        // Creating adapter for spinner
        dataAdapter4 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, cityname){
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

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {  networkManager.new RetrieveGetCityListTask(RegistrationPVRDetailesActivity.this, stateid.get(position))
                        .execute();
                    choosecity.setVisibility(View.VISIBLE);}
                else {
                    choosecity.setVisibility(View.GONE);
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
//                    servicetext = servicesid.get(service.getSelectedItemPosition())
//                            filtertext,
//                            loctext,
//                            loctext2,
//                            loctext3,
//                            countrytext,
//                            statetext




                    String UserType ="PVR";
                    String UserGender =gendertext;
                    String UserDob = dobtext;
                    String CurrentLocation="Thrissur";
                    String LocationLat="20";
                    String LocationLong="20";

                    String PreLocation1  =null;
                    String PreLocation1Lat  =null;
                    String PreLocation1Long  =null;
                    if(place1!=null)
                    {
                         PreLocation1 =place1.getName().toString();
                         PreLocation1Lat = String.valueOf(place1.getLatLng().latitude);
                         PreLocation1Long = String.valueOf(place1.getLatLng().latitude);
                    }


                    String PreLocation2 =null;
                    String PreLocation2Lat = null;
                    String PreLocation2Long = null;
                    if(place2!=null)
                    {
                        PreLocation2 =place2.getName().toString();
                     PreLocation2Lat = String.valueOf(place2.getLatLng().latitude);
                     PreLocation2Long = String.valueOf(place2.getLatLng().latitude);}


                    String PreLocation3 =null;
                    String PreLocation3Lat =null;
                    String PreLocation3Long =null;
                    if(place3!=null)
                    {
                         PreLocation3 =place3.getName().toString();
                         PreLocation3Lat = String.valueOf(place3.getLatLng().latitude);
                         PreLocation3Long = String.valueOf(place3.getLatLng().latitude);
                    }

                    Integer CityId = Integer.valueOf(cityid.get(city.getSelectedItemPosition()));
                    String UserAddress= addresstext;
                    String UserEducation =edutext;
                    String UserDesignation=desigtext;
                    String UserExperience =experincetext;
                    String UserWagesHour = hourlytext;
                    File ProfileImage = null;
                    if(imguri!= null)
                        {

                             ProfileImage = new File(imguri);
                          //  profile_image.setImageURI(Uri.parse(ProfileImage.toString()));

                        }
                    String UserAddidtionSkil="";
                    String Categorys=servicesid.get(service.getSelectedItemPosition());
                    String CategorysFiltters=filtersid.get(subservice.getSelectedItemPosition());
                    String cultureInfo = getCurrentLocale().getLanguage();


                    SharedPreferencesManager.init(getApplicationContext());
                    String accessToken =  SharedPreferencesManager.read(ACCESS_TOCKEN,"");
                    String timeZone =String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
                    String EmploymentType =emptext;
                    String  languages =langugetext;

//                    profile_image.setImageURI(Uri.fromFile(ProfileImage));



                    Log.i("Reg_submit3"," "+UserType +UserGender+ UserDob +CurrentLocation+LocationLat+LocationLong+PreLocation1 +
                            PreLocation1Lat+PreLocation1Long +PreLocation2 +PreLocation2Lat+ PreLocation2Long+ PreLocation3 +
                            PreLocation3Lat + PreLocation3Long +" citycode :"+CityId+ UserAddress+ UserEducation + UserDesignation+
                            UserExperience + UserWagesHour +UserAddidtionSkil+ Categorys+CategorysFiltters+cultureInfo+
                            " access:"+accessToken+timeZone+EmploymentType+languages);


                    networkManager.new RetrieveUploadFinalRegTask(RegistrationPVRDetailesActivity.this,UserType ,UserGender,
                            UserDob ,CurrentLocation,LocationLat,LocationLong,PreLocation1 ,
                            PreLocation1Lat,PreLocation1Long ,PreLocation2 ,PreLocation2Lat, PreLocation2Long, PreLocation3 ,
                            PreLocation3Lat , PreLocation3Long ,CityId, UserAddress, UserEducation , UserDesignation,
                            UserExperience , UserWagesHour ,UserAddidtionSkil, Categorys,CategorysFiltters,cultureInfo,
                            accessToken,timeZone,EmploymentType,languages,ProfileImage)
                            .execute();
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }

                //    networkManager.new RetrieveRegistrationTask(RegistrationFillActivity.this,LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo)
               //             .execute();

//
//                    RequestParams params ;
//                    AsyncHttpClient clientx;
//                    clientx = new AsyncHttpClient();
//                    clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
//                    clientx.addHeader("www-request-api-version", "1.0");
//                    clientx.addHeader("enctype", "multipart/form-data");
//
//                    params = new RequestParams();
//                    params.setForceMultipartEntityContentType(true);
//                    params.put("UserType",UserType);
//                    params.put("UserGender",UserGender);
//                    params.put("UserDob",UserDob);
//                    params.put("CurrentLocation",CurrentLocation);
//                    params.put("LocationLat",LocationLat);
//                    params.put("LocationLong",LocationLong);
//                    params.put("PreLocation1",PreLocation1);
//                    params.put("PreLocation1Lat",PreLocation1Lat);
//                    params.put("PreLocation1Long",PreLocation1Long);
//                    params.put("PreLocation2",PreLocation2);
//                    params.put("PreLocation2Lat",PreLocation2Lat);
//                    params.put("PreLocation2Long",PreLocation2Long);
//                    params.put("PreLocation3",PreLocation3);
//                    params.put("PreLocation3Lat",PreLocation3Lat);
//                    params.put("PreLocation3Long",PreLocation3Long);
//                    params.put("CityId",CityId);
//                    params.put("UserAddress",UserAddress);
//                    params.put("UserEducation",UserEducation);
//                    params.put("UserDesignation",UserDesignation);
//                    params.put("UserExperience",UserExperience);
//                    params.put("UserWagesHour",UserWagesHour);
//                    try {
//                        params.put("ProfileImage",ProfileImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        Log.i("Reg2_submit"," img"+e.getMessage());
//                    }
//                    params.put("UserAddidtionSkill",UserAddidtionSkil);
//                    params.put("Categorys",Categorys);
//                    params.put("CategorysFiltters",CategorysFiltters);
//                    params.put("cultureInfo",cultureInfo);
//                    params.put("accessToken",accessToken);
//                    params.put("timeZone",timeZone);
//                    params.put("EmploymentType",EmploymentType);
//                    params.put("languages",languages);
//
//                    clientx.post(URLHOST + URLUPLOADFINALREG, params, new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            String responseStringx = new String(responseBody);
//                            Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
//                          //  regrsponse.failedtoConnect();
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            String responseStringx = new String(responseBody);
//
//                            Log.i("Reg2_submit", "ons succscess" + responseStringx + " " + Arrays.toString(headers)+" "+statusCode);
//
//                           // jsonParser.RegistrationFinalRegResponseParser(regrsponse, responseStringx, context);
//                        }
//                    } );
//
                }
            }
        });
    }

    private boolean validateFields() {
        if(service.getSelectedItemPosition()==0)
        {
            snackbar = Snackbar
                    .make(cordi, R.string.service_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if(subservice.getSelectedItemPosition()==0)
        {
             snackbar = Snackbar
                     .make(cordi, R.string.subservice_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if(place1==null)
        {
             snackbar = Snackbar
                     .make(cordi, R.string.location_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;


        }
        else if(chosecountry.getText().toString().isEmpty())
        {
             snackbar = Snackbar
                     .make(cordi, R.string.country_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

            return false;

        }
        else if(state.getSelectedItemPosition()==0)
        {
             snackbar = Snackbar
                     .make(cordi, R.string.state_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if(city.getSelectedItemPosition()==0)
        {
             snackbar = Snackbar
                     .make(cordi, R.string.city_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
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
    public void GetCityeCategoryList(final List<String> subcatid, final List<String> subcatname, List<String> lat, List<String> lng) {
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
    public void GetCityeCategoryList(String imageUrl, String accessToken, String userEmail, String userType, String userFirstName, String userLastName, Integer userStatusLevel) {
        if(userStatusLevel==3)
        {
            Intent intent = new Intent(RegistrationPVRDetailesActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
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
                case 0:place1 =place;

                    break;
                case 1:place2 = place;
                    break;
                case 2:place3 = place;
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
    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }


}

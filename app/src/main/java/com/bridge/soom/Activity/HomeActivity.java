package com.bridge.soom.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.customview.TouchInterceptFrameLayout;
import com.bridge.soom.Fragment.FormFragment;
import com.bridge.soom.Fragment.ProfileFragment;
import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.MySeekBar;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.PlacesAutoCompleteAdapter;
import com.bridge.soom.Helper.RecyclerAdap;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.IS_LOGGEDIN;
import static com.bridge.soom.Helper.Constants.USER_EMAIL;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;
import static com.bridge.soom.Helper.Constants.USER_STATUS_LEVEL;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,HomeResponse , ClusterManager.OnClusterItemInfoWindowClickListener<ProviderBasic>,
        ClusterManager.OnClusterClickListener<ProviderBasic>,
        ClusterManager.OnClusterInfoWindowClickListener<ProviderBasic>,
        ClusterManager.OnClusterItemClickListener<ProviderBasic> ,ProfileFragment.OnFragmentInteractionListener{
    //Initialize to a non-valid zoom value

    private CircleImageView profile_image;
    private TextView profile_name;

    private static final String TAG = "HomeActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 22;

    //Home
    private float previousZoomLevel = -1.0f;
    private CameraPosition mPreviousCameraPosition = null;

    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation,selectedLocation;
    GoogleApiClient mGoogleApiClientloc;
    private ProgressDialog progress;

    private UserModel user;
    private Boolean isGuest = false;
    private NavigationView navigationView;
    private   View hView;
    private  String tocken="";
    private NetworkManager networkManager;
    private AutoCompleteTextView serviceSearch;
    List<String> catname,catid;
    ArrayAdapter<String> autoAdapter;
    private ClusterManager<ProviderBasic> mClusterManager;
    private ProviderBasic clickedClusterItem;

    private boolean isZooming = false;
    private boolean isShowing = false;

    private InfoWindow.MarkerSpecification markerSpec;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;
    private SeekBar seekBar;
    private MySeekBar distance_seek;
    private TextView textView,norsult,servicetext;

    private Integer distance = 10;
    private Integer zoomLevel = 19;
    private String category;
    private ToggleButton mSwitchShowSecure;
    private RelativeLayout listRec,Maprel;
    private  SupportMapFragment mapFragment;

    private List<ProviderBasic> providerList = new ArrayList<>();
    private List<ProviderBasic> providerList_cluster = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView popuprecycler;
    private RecyclerAdap mAdapter;
    private RecyclerAdap mAdapter_cluster;
    private Menu menu;
    private Menu menuxxx;

    private RelativeLayout sercvice;
    private ImageButton filter;
    private ImageButton close,cancel1,cancel2,close_popup;
    private ViewGroup hiddenPanel;
    private ViewGroup clusterpopup;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private AutoCompleteTextView choselocation;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;
    private String Selected_Category = "" ;
    private String Selected_Category_ID ="";

    private String price_min ="";
    private String price_max ="";
    private String range_min ="";
    private String range_max ="";
    private String filters ="";
    private String filtersname ="";
    private RelativeLayout.LayoutParams p;
    boolean doubleBackToExitPressedOnce = false;
    private ConstraintLayout main_screen;
    private FrameLayout content_frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesManager.init(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView =  navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        networkManager = new NetworkManager(this);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        main_screen = (ConstraintLayout) findViewById(R.id.main_screen);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);

        profile_image = (CircleImageView)hView.findViewById(R.id.profile_image);
        profile_name = (TextView)hView.findViewById(R.id.profile_name);
        serviceSearch = (AutoCompleteTextView) findViewById(R.id.serviceSearch);
        listRec = (RelativeLayout) findViewById(R.id.listRec);
        listRec.setVisibility(View.GONE);
        Maprel= (RelativeLayout) findViewById(R.id.Maprel);
        Maprel.setVisibility(View.VISIBLE);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        distance_seek = (MySeekBar) findViewById(R.id.distance_seek);
       textView = (TextView) findViewById(R.id.textxxax);
        servicetext = (TextView) findViewById(R.id.servicetext);
        norsult = (TextView) findViewById(R.id.norsult);
        content_frame.setVisibility(View.GONE);


        seekBar.setProgress(0);
        seekBar.setMax(65);

        distance_seek.setMax(65);
        distance_seek.setProgress(0);

        setservicetext();
        sercvice = (RelativeLayout) findViewById(R.id.sercvice);
        filter = (ImageButton) findViewById(R.id.filter);
        close = (ImageButton) findViewById(R.id.closexx);
        cancel1 = (ImageButton) findViewById(R.id.cancel1);
        cancel2 = (ImageButton) findViewById(R.id.cancel2);
        close_popup = (ImageButton) findViewById(R.id.close_popup);

        choselocation = (AutoCompleteTextView) findViewById(R.id.choselocation);
//
//        Drawable img = this.getResources().getDrawable(
////                R.drawable.ic_search);
//        img.setBounds(0, 0, 24, 24);
        Drawable img2 = this.getResources().getDrawable(
                R.drawable.ic_loc_fil);
        img2.setBounds(0, 0, 24, 24);
        Drawable img3 = this.getResources().getDrawable(
                R.drawable.ic_ser_fil);
        img3.setBounds(0, 0, 24, 24);
        choselocation.setCompoundDrawables(img2, null, null, null);
        //sercvice.setCompoundDrawables(img, null, null, null);
        serviceSearch.setCompoundDrawables(img3, null, null, null);

//        choselocation.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_place), null, null, null);
        serviceSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if(choselocation.getText().toString().isEmpty())
                    {
                        choselocation.setText("Current Location");
                        selectedLocation = mLastLocation;
                    }
                    serviceSearch.showDropDown();
                }
            }
        });

        hiddenPanel = (ViewGroup)findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        clusterpopup = (ViewGroup)findViewById(R.id.clusterpopup);
        clusterpopup.setVisibility(View.INVISIBLE);
        isGuest = getIntent().getBooleanExtra("GUEST",false);

        if(!isGuest) {
            setuser();

        }



        distance_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int stepSize = 5;
                progress = (progress / stepSize) * stepSize;
                seekBar.setProgress(progress);

                progress=progress+10;
//               if(progress<=65)
//               {
                   p = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.ABOVE, seekBar.getId());

                Rect thumbRect = distance_seek.getSeekBarThumb().getBounds();
                p.setMargins(
                        thumbRect.centerX(),0,0, 0);
//               }
                textView.setLayoutParams(p);
                textView.setText(String.valueOf(progress) + " KM");
                textView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                textView.setVisibility(View.INVISIBLE);

                if(!Selected_Category.isEmpty())
                {


                    distance = seekBar.getProgress()+10;
//                 Toast.makeText(HomeActivity.this, "seekbar "+ distance, Toast.LENGTH_LONG).show();
                    if (mLastLocation != null || category != null) {

                        String pricerange ="";
                        if(!price_max.isEmpty()&&!price_min.isEmpty())
                        {
                            pricerange = price_min+","+price_max;
                        }
                        String raterange ="";
                        if(!range_min.isEmpty()&&!range_max.isEmpty())
                        {
                            raterange = range_min+","+range_max;
                        }


                        networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this, HomeActivity.this, tocken, category,
                                String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                                getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange, raterange, filters)
                                .execute();
                        showLoadingDialog();
                    } else {
                        // snackbar

                        snackbar = Snackbar.make(cordi, R.string.invalid_selection, Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }
                }
                else {
                    snackbar = Snackbar.make(cordi,"Please Select a Category", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        final TouchInterceptFrameLayout mapViewContainer =
                (TouchInterceptFrameLayout) findViewById(R.id.mapViewContainer);
        mapFragment.getMapAsync(this);


        Log.i(TAG, "ONCREATE");
        catname = new ArrayList<>();
        catid = new ArrayList<>();
        catname.clear();
        catid.clear();

        autoAdapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,catname);
        serviceSearch.setAdapter(autoAdapter);//setting the adapter data into the AutoCompleteTextView
        serviceSearch.setThreshold(0);//will start working from zero character
        serviceSearch.setTextColor(Color.WHITE);
        serviceSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "pos: "+position+" item : "+autoAdapter.getItem(position));
                category = autoAdapter.getItem(position);



                    if(selectedLocation!=null) {
//                        if(mLastLocation!=null)
//                        {   selectedLocation = mLastLocation;
//                    }
                        Selected_Category =     autoAdapter.getItem(position);
                        setservicetext();
                        assert Selected_Category != null;
                        if(!Selected_Category.toLowerCase().trim().equals(filtersname.toLowerCase().trim()))
                        {
                            clearFilters();
                        }
                       if(findIDinArray(autoAdapter.getItem(position),catname)!=-1)
                        {Selected_Category_ID = catid.get(findIDinArray(autoAdapter.getItem(position),catname));}

                        Log.i("SELCTED"," "+ autoAdapter.getItem(position) );
                        Log.i("SELCTED"," "+catid.get(findIDinArray(autoAdapter.getItem(position),catname)));
                        Log.i("SELCTED"," "+" "+ catname.get(findIDinArray(autoAdapter.getItem(position),catname)));
                    String pricerange ="";
                    if(!price_max.isEmpty()&&!price_min.isEmpty())
                    {
                        pricerange = price_min+","+price_max;
                    }
                        String raterange ="";
                        if(!range_min.isEmpty()&&!range_max.isEmpty())
                        {
                            raterange = range_min+","+range_max;
                        }

                        networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this, HomeActivity.this,tocken, autoAdapter.getItem(position),
                                String.valueOf(selectedLocation.getLatitude()), String.valueOf(selectedLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                                getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange,raterange,filters)
                                .execute();
// keyboard close

                        showLoadingDialog();
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                        }
                     }
                else {
                    // snackbar

                    snackbar = Snackbar.make(cordi, R.string.location_empty, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
                slideUpDown(view);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!Selected_Category.isEmpty()){ int stepSize = 5;
                progress = (progress / stepSize) * stepSize;
                seekBar.setProgress(progress);
                distance = progress + 10;//min value = 25
//                 Toast.makeText(HomeActivity.this, "seekbar "+ distance, Toast.LENGTH_LONG).show();
                if (mLastLocation != null || category != null) {

                    String pricerange ="";
                    if(!price_max.isEmpty()&&!price_min.isEmpty())
                    {
                        pricerange = price_min+","+price_max;
                    }
                    String raterange ="";
                    if(!range_min.isEmpty()&&!range_max.isEmpty())
                    {
                        raterange = range_min+","+range_max;
                    }


                    networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this, HomeActivity.this,tocken, category,
                            String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                            getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange, raterange, filters)
                            .execute();
                    showLoadingDialog();
                } else {
                    // snackbar

                    snackbar = Snackbar.make(cordi, R.string.invalid_selection, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }
            else {
                    snackbar = Snackbar.make(cordi,"Please Select a Category", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        networkManager.new RetrieveGetCategoryListHomeTask(HomeActivity.this,this)
                .execute();


        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);
        markerSpec =
                new InfoWindow.MarkerSpecification(offsetX, offsetY);

        infoWindowManager = new InfoWindowManager(getSupportFragmentManager());
        infoWindowManager.onParentViewCreated(mapViewContainer, savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        popuprecycler = (RecyclerView) findViewById(R.id.popuprecycler);


        providerList.clear();
        providerList_cluster.clear();

        mAdapter = new RecyclerAdap(providerList,HomeActivity.this,isGuest,HomeActivity.this);
        mAdapter_cluster = new RecyclerAdap(providerList_cluster,HomeActivity.this,isGuest,HomeActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        popuprecycler.setLayoutManager(mLayoutManager2);
        popuprecycler.setItemAnimator(new DefaultItemAnimator());
        popuprecycler.setAdapter(mAdapter_cluster);

        sercvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               slideUpDown(v);

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Selected_Category_ID.isEmpty()){
                    Intent intent = new Intent (HomeActivity.this, FilterActivity.class);
                intent.putExtra("FILTERID",Selected_Category_ID);
                    intent.putExtra("FILTERName",Selected_Category);
                    intent.putExtra("price_min",price_min);
                    intent.putExtra("price_max",price_max);
                    intent.putExtra("range_min",range_min);
                    intent.putExtra("range_max",range_max);


                    startActivityForResult(intent, 1);
                }
                else {
                    //snackbar
                    snackbar = Snackbar.make(cordi, "Please Specify Your Search", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideUpDown(v);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }

            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choselocation.setText("");
                choselocation.requestFocus();

            }
        });
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceSearch.setText("");
                Selected_Category_ID ="";
                Selected_Category="";
            }
        });

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               slidePopUpDown();
            }
        });

        // to diable the below clickss
        hiddenPanel.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});



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

        norsult.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

    }

    private void setuser() {
        user = new UserModel();
        final String AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");
        Log.i("ACCESS_TOVCKEN"," "+AccessTocken);
        user.setAccessToken(AccessTocken);
        tocken =AccessTocken;
//            user.setUserId(Integer.parseInt(SharedPreferencesManager.read(USER_ID,"0")));
        user.setUserEmail( SharedPreferencesManager.read(USER_EMAIL,""));
        user.setUserType( SharedPreferencesManager.read(USER_TYPE,"USR"));
        user.setUserFirstName( SharedPreferencesManager.read(USER_FIRST_NAME,""));
        user.setUserLastName( SharedPreferencesManager.read(USER_LAST_NAME,""));
        user.setUserStatusLevel(Integer.parseInt(SharedPreferencesManager.read(USER_STATUS_LEVEL,"0")));
        user.setProfileImageUrl( SharedPreferencesManager.read(USER_IMAGE_URL,""));


//            Glide.with(this).load(user.getProfileImageUrl().trim())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .override(90,90)
//                    .placeholder(R.drawable.avatar)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .into(profile_image);


        Glide.with(this)
                .load(user.getProfileImageUrl().trim())
                .placeholder(R.drawable.avatar)
                .into(new GlideDrawableImageViewTarget(profile_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        Log.d("Gliderrr", "readyy");

                        profile_image.setImageDrawable(drawable);

                    }
                });

        Log.i("Gliderrr"," : "+user.getProfileImageUrl().trim()+": ");


        profile_name.setText(user.getUserFirstName()+" "+user.getUserLastName());

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_login);

        // set new title to the MenuItem
        nav_camara.setTitle("Logout");
    }

    private void setservicetext() {
        if(Selected_Category.trim().isEmpty())
        {
            servicetext.setText("Choose your Service");

        }
        else {
            servicetext.setText(Selected_Category.trim());
        }
    }

    private void clearFilters() {
    }

    private Integer findIDinArray(String item, List<String> catname) {
        for(int i=0;i<catname.size();i++)

        {
            if(item.toLowerCase().trim().equals(catname.get(i).toLowerCase().trim()))
            {
                return i;
            }
        }
        return -1;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        infoWindowManager.onMapReady(mMap);

       mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(9.9312,76.2673) , 15.0f) );


        Log.i(TAG, "MAP READY");

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }


//        mMap.setMyLocationEnabled(true);
            setUpCluster();


    }

    private void setUpCluster() {

        mClusterManager = new ClusterManager<ProviderBasic>(this,mMap);
      //  mMap.setOnCameraIdleListener(mClusterManager);
        mClusterManager.setRenderer(new MyClusterRenderer(HomeActivity.this, mMap,
                mClusterManager));

//        mMap.setOnMarkerClickListener(mClusterManager);
//        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForClusters());
//        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
//                new MyCustomAdapterForItems());
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mMap.getCameraPosition();
                if(mPreviousCameraPosition == null || mPreviousCameraPosition.zoom != position.zoom) {
                    mPreviousCameraPosition = mMap.getCameraPosition();
                    mClusterManager.cluster();
                }
                isZooming =false;
                Log.i("ZOOMMM","sxds : "+mMap.getCameraPosition().zoom);
                if(infoWindowManager!=null&&formWindow!=null)
                {
                    if(previousZoomLevel != mMap.getCameraPosition().zoom)
                    {
                        if(isShowing)
                        {
                            infoWindowManager.hide(formWindow);
                        }
                        else {
                            isShowing=true;

                        }
                    }
                    previousZoomLevel = mMap.getCameraPosition().zoom;}
                }
            }
        );
        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ProviderBasic>() {
                    @Override
                    public boolean onClusterClick(Cluster<ProviderBasic> cluster) {
//                        clickedCluster = cluster;
                //               Toast.makeText(HomeActivity.this, "onClusterClick --1", Toast.LENGTH_LONG).show();

                        slidePopUpDown();

                        List<ProviderBasic> list = new ArrayList<ProviderBasic>(cluster.getItems());
                        providerList_cluster.clear();
                        for(ProviderBasic basic:list)
                        {
                            mClusterManager.addItem(basic);
                            mClusterManager.cluster();
                            providerList_cluster.add(basic);
                        }
                        mAdapter_cluster.notifyDataSetChanged();

// open the popup
                        return false;
                    }
                });

        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ProviderBasic>() {
                    @Override
                    public boolean onClusterItemClick(ProviderBasic item) {
                        clickedClusterItem = item;

                       // Toast.makeText(HomeActivity.this, "onClusterItemClick --top", Toast.LENGTH_LONG).show();

                        Fragment frag = new FormFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("PROVIDER", item);
                        bundle.putBoolean("ISGUEST",isGuest);
                        frag.setArguments(bundle);

                        formWindow = new InfoWindow(item.getPosition(),markerSpec,frag);
                        infoWindowManager.toggle(formWindow, true);

                        return false;
                    }
                });


        mClusterManager.cluster();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class MyClusterRenderer extends DefaultClusterRenderer<ProviderBasic> {

        MyClusterRenderer(Context context, GoogleMap map,
                          ClusterManager<ProviderBasic> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ProviderBasic item,
                                                   MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_wrkr));

            super.onBeforeClusterItemRendered(item, markerOptions);


        }

        @Override
        protected void onClusterItemRendered(ProviderBasic clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

//        @Override
//        protected boolean shouldRenderAsCluster(Cluster<ProviderBasic> cluster) {
//
//
////            return super.shouldRenderAsCluster(cluster);   // delete this entire override methode to enbale cluster
//            return false;
//        }
    }

//
//    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
//
//        private final View myContentsView;
//
//        MyCustomAdapterForItems() {
//            myContentsView = getLayoutInflater().inflate(
//                    R.layout.info_window, null);
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            // TODO Auto-generated method stub
//
//
//            TextView tvTitle = ((TextView) myContentsView
//                    .findViewById(R.id.title));
//            ImageView tvSnippet = ((ImageView) myContentsView
//                    .findViewById(R.id.ivPlace));
//
////            tvTitle.setTypeface(mTyFaceKreonBold);
////            tvSnippet.setTypeface(mTyFaceKreonBold);
//            if (clickedClusterItem != null) {
//                tvTitle.setText(clickedClusterItem.getUserFirstName());
//                tvSnippet.setImageResource(R.drawable.avatar);
//
//
//            }
//            return myContentsView;
//        }
//    }

    // class for Main Clusters.

    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForClusters() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub

//
//            TextView tvTitle = ((TextView) myContentsView
//                    .findViewById(R.id.txtHeader));
//            TextView tvSnippet = ((TextView) myContentsView
//                    .findViewById(R.id.txtAddress));
//            tvSnippet.setVisibility(View.GONE);
//            tvTitle.setTypeface(mTyFaceKreonBold);
//            tvSnippet.setTypeface(mTyFaceKreonBold);
//
//
//            if (clickedCluster != null) {
//                tvTitle.setText(String
//                        .valueOf(clickedCluster.getItems().size())
//                        + " more offers available");
//            }
//            return myContentsView;
            return null;

        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //snackbar
            snackbar = Snackbar.make(cordi,  "Please click BACK again to exit", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuxxx = menu;
        menuxxx.clear();
        getMenuInflater().inflate(R.menu.home, menuxxx);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setVisibility(View.VISIBLE);

        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cancelPopUpDown();
                if(b){

                    listRec.setVisibility(View.VISIBLE);
                    Maprel.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    distance_seek.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
//
//                    tv1.setVisibility(View.GONE);
//                    tv2.setVisibility(View.GONE);
//                    tv3.setVisibility(View.GONE);

Log.i("FRAG"," true----");
                } else {
                    //Your code when unchecked
                    Log.i("FRAG"," false----");
                    listRec.setVisibility(View.GONE);
                    Maprel.setVisibility(View.VISIBLE);
//                    seekBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    distance_seek.setVisibility(View.VISIBLE);
//                    tv1.setVisibility(View.VISIBLE);
//                    tv2.setVisibility(View.VISIBLE);
//                    tv3.setVisibility(View.VISIBLE);

                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;


        if (id == R.id.nav_home) {
            main_screen.setVisibility(View.VISIBLE);
            content_frame.setVisibility(View.GONE);
            if(mSwitchShowSecure!=null)
                mSwitchShowSecure.setVisibility(View.VISIBLE);

            menuxxx.clear();
            getMenuInflater().inflate(R.menu.home, menuxxx);


        } else if (id == R.id.nav_invitation) {

            if(!isGuest){
                main_screen.setVisibility(View.GONE);
                content_frame.setVisibility(View.VISIBLE);

                menuxxx.clear();


            }
            else {
                //snackbar



                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                HomeActivity.this.finish();
                            }
                        }).show();
            }


        } else if (id == R.id.nav_profile) {
            if(!isGuest){

                main_screen.setVisibility(View.GONE);
                content_frame.setVisibility(View.VISIBLE);


                menuxxx.clear();


                fragment = new ProfileFragment();


//                Intent intent = new Intent (HomeActivity.this, ProfileActivity.class);
//                intent.putExtra("MYUSER", user);
//                startActivity(intent);

            }
            else {
                //snackbar

                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                HomeActivity.this.finish();
                            }
                        }).show();
            }

        } else if (id == R.id.nav_rating) {
            if(!isGuest){
                main_screen.setVisibility(View.GONE);
                content_frame.setVisibility(View.VISIBLE);
                menuxxx.clear();


            }
            else {
                //snackbar

                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                HomeActivity.this.finish();
                            }
                        }).show();
            }

        } else if (id == R.id.nav_login) {

            if(isGuest)
            {
                SharedPreferencesManager.writeBool(IS_LOGGEDIN,false);
            Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
//                Intent intent = new Intent (HomeActivity.this, HomeActivity.class);
                SharedPreferencesManager.init(this);
                SharedPreferencesManager.write(ACCESS_TOCKEN,"");
                SharedPreferencesManager.write(USER_EMAIL,"");
                SharedPreferencesManager.write(USER_TYPE,"");
                SharedPreferencesManager.write(USER_FIRST_NAME,"");
                SharedPreferencesManager.write(USER_LAST_NAME,"");
                SharedPreferencesManager.write(USER_STATUS_LEVEL,"");
                SharedPreferencesManager.write(USER_IMAGE_URL,"");
                intent.putExtra("GUEST",true);

                startActivity(intent);
                finish();

            }
            else {
                SharedPreferencesManager.writeBool(IS_LOGGEDIN,false);
//            Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
                Intent intent = new Intent (HomeActivity.this, HomeActivity.class);
                SharedPreferencesManager.init(this);
                SharedPreferencesManager.write(ACCESS_TOCKEN,"");
                SharedPreferencesManager.write(USER_EMAIL,"");
                SharedPreferencesManager.write(USER_TYPE,"");
                SharedPreferencesManager.write(USER_FIRST_NAME,"");
                SharedPreferencesManager.write(USER_LAST_NAME,"");
                SharedPreferencesManager.write(USER_STATUS_LEVEL,"");
                SharedPreferencesManager.write(USER_IMAGE_URL,"");
                intent.putExtra("GUEST",true);

                startActivity(intent);
                finish();

            }


        } else if (id == R.id.nav_howitwrks) {

            main_screen.setVisibility(View.GONE);
            content_frame.setVisibility(View.VISIBLE);
            menuxxx.clear();


//
//            Intent intent = new Intent (HomeActivity.this, HowitworksActivity.class);
//            startActivity(intent);

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        //optionally, stop location updates if only current location is needed
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, HomeActivity.this);


        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, HomeActivity.this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, HomeActivity.this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void failedtoConnect() {

            dismissLoadingDialog();

        //snackbar
        snackbar = Snackbar.make(cordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void GetCategoryList(final List<String> catiid, final List<String> catnam) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dismissLoadingDialog();

                catname.clear();
                catid.clear();
                for (String cat : catnam) {
                    catname.add(cat);
                }
                for (String cat : catiid) {
                    catid.add(cat);
                }

                autoAdapter.notifyDataSetChanged();
            }});
    }

    @Override
    public void GetCategoryListFailed(String msg) {
        //snackbar
        dismissLoadingDialog();

        snackbar = Snackbar.make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetProviderListFailed(String msg) {
        //snackbar
        dismissLoadingDialog();
        Log.i("LOGGGG",msg);
        snackbar = Snackbar.make(cordi,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


    }

    @Override
    public void GetProviderList(final List<ProviderBasic> providers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, " providers : " + providers.size());
                dismissLoadingDialog();

                // Add cluster items (markers) to the cluster manager.

                if (providers.size() > 0) {
                    if (mClusterManager != null) {
                        addItems(providers);
//            snackbar = Snackbar.make(cordi,""+providers.size()+" No of providers Found", Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
                        norsult.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                } else {

                    mClusterManager.clearItems();
                    providerList.clear();
                    mClusterManager.cluster();
                    norsult.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
//snackbar
                    snackbar = Snackbar.make(cordi, R.string.no_providers, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }});

    }

    private void addItems(final List<ProviderBasic> providers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(selectedLocation==null)
                    selectedLocation = mLastLocation;
                Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(selectedLocation.getLatitude(),selectedLocation.getLongitude())).radius(distance*1000));
                circle.setVisible(false);

                               mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(selectedLocation.getLatitude(),selectedLocation.getLongitude()) , getZoomLevel(circle)) );

                mClusterManager.clearItems();
                providerList.clear();
                for(ProviderBasic basic:providers)
                {
                    mClusterManager.addItem(basic);
                    mClusterManager.cluster();
                    providerList.add(basic);


                }
                mAdapter.notifyDataSetChanged();


            }
        });

    }

    private int getZoomLevel(Circle circle) {
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
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

    @Override
    public void onClusterItemInfoWindowClick(ProviderBasic providerBasic) {
//        Toast.makeText(this, providerBasic.getUserFirstName() + " Clicked", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onClusterClick(Cluster<ProviderBasic> cluster) {

   //     Toast.makeText(HomeActivity.this, "onClusterClick --2", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ProviderBasic> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ProviderBasic providerBasic) {
//        Toast.makeText(this, "onClusterItemClick --down", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoWindowManager.onDestroy();
    }


    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);
            choselocation.setText("Current Location");
            choselocation.clearFocus();

            selectedLocation = mLastLocation;
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            serviceSearch.clearFocus();
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    public void slidePopUpDown() {
        if (!isPopupShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);


            clusterpopup.startAnimation(bottomUp);
            clusterpopup.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom);

            clusterpopup.startAnimation(bottomDown);
            clusterpopup.setVisibility(View.GONE);
            providerList_cluster.clear();
            mAdapter_cluster.notifyDataSetChanged();
        }
    }

    public void cancelPopUpDown() {
        if (isPopupShown()) {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom);

            clusterpopup.startAnimation(bottomDown);
            clusterpopup.setVisibility(View.GONE);
            providerList_cluster.clear();
            mAdapter_cluster.notifyDataSetChanged();
        }
    }




    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    private boolean isPopupShown() {
        return clusterpopup.getVisibility() == View.VISIBLE;
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

            Log.i("placexxx","clicked - "+placeDesc+" "+placeId);

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
                Log.e("placexxx", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // storymarker placing

            Log.e("placexxx", "Place query did not complete. Success: " +
                    places.getStatus().toString());
            final Place place = places.get(0);


           selectedLocation = new Location("dummpyprovider");
            selectedLocation.setLatitude(place.getLatLng().latitude);
            selectedLocation.setLongitude(place.getLatLng().longitude);

            // Selecting the first object buffer.

            if( !Selected_Category_ID.equals("0")||!Selected_Category_ID.isEmpty())
            {
                    setservicetext();

                    String pricerange ="";
                    if(!price_max.isEmpty()&&!price_min.isEmpty())
                    {
                        pricerange = price_min+","+price_max;
                    }
                    String raterange ="";
                    if(!range_min.isEmpty()&&!range_max.isEmpty())
                    {
                        raterange = range_min+","+range_max;
                    }

                    networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this, HomeActivity.this,tocken, Selected_Category,
                            String.valueOf(selectedLocation.getLatitude()), String.valueOf(selectedLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                            getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange,raterange,filters)
                            .execute();
// keyboard close

                    showLoadingDialog();
                slideUpDown(null);

                try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                }

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


        if(mGoogleApiClient!=null)
        {  mGoogleApiClient.disconnect();}


    }

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading_message));
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                price_min=data.getStringExtra("price_min");
                 price_max=data.getStringExtra("price_max");
                 range_min=data.getStringExtra("range_min");
                 range_max=data.getStringExtra("range_max");
                 filters=data.getStringExtra("filters");
                 filtersname=data.getStringExtra("filtersName");
                 Selected_Category_ID=data.getStringExtra("filtersID");

                String pricerange ="";
                if(!price_max.isEmpty()&&!price_min.isEmpty())
                {
                    pricerange = price_min+","+price_max;
                }
                String raterange ="";
                if(!range_min.isEmpty()&&!range_max.isEmpty())
                {
                    raterange = range_min+","+range_max;
                }

                networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this, HomeActivity.this, user.getAccessToken(), filtersname,
                        String.valueOf(selectedLocation.getLatitude()), String.valueOf(selectedLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                        getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange,raterange,filters)
                        .execute();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivity

    @Override
    protected void onResume() {
        super.onResume();
        if(!isGuest) {
            setuser();

        }
    }

    public UserModel getMyUser() {
        return user;
    }

}

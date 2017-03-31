package com.bridge.soom.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.customview.TouchInterceptFrameLayout;
import com.bridge.soom.Fragment.FormFragment;
import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.RecyclerAdap;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
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
        ClusterManager.OnClusterItemClickListener<ProviderBasic> {
    //Initialize to a non-valid zoom value
    private float previousZoomLevel = -1.0f;
    private CameraPosition mPreviousCameraPosition = null;
    private CircleImageView profile_image;
    private TextView profile_name;

    private static final String TAG = "HomeActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 22;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private UserModel user;
    private Boolean isGuest = false;
    private NavigationView navigationView;
    private   View hView;
    private NetworkManager networkManager;
    private AutoCompleteTextView serviceSearch;
    List<String> catname;
    ArrayAdapter<String> autoAdapter;
    private ClusterManager<ProviderBasic> mClusterManager;
    private ProviderBasic clickedClusterItem;

    private boolean isZooming = false;
    private boolean isShowing = false;

    private InfoWindow.MarkerSpecification markerSpec;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;
    private SeekBar seekBar;
    private Integer distance = 25;
    private String category;
    private ToggleButton mSwitchShowSecure;
    private RelativeLayout listRec,Maprel;
    private  SupportMapFragment mapFragment;

    private List<ProviderBasic> providerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdap mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesManager.init(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         navigationView = (NavigationView) findViewById(R.id.nav_view);
         hView =  navigationView.getHeaderView(0);

        networkManager = new NetworkManager(this);

        profile_image = (CircleImageView)hView.findViewById(R.id.profile_image);
        profile_name = (TextView)hView.findViewById(R.id.profile_name);
        serviceSearch = (AutoCompleteTextView) findViewById(R.id.serviceSearch);
        listRec = (RelativeLayout) findViewById(R.id.listRec);
        listRec.setVisibility(View.GONE);
        Maprel= (RelativeLayout) findViewById(R.id.Maprel);
        Maprel.setVisibility(View.VISIBLE);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(50);

        isGuest = getIntent().getBooleanExtra("GUEST",false);
        if(!isGuest) {
            user = new UserModel();
            user.setAccessToken( SharedPreferencesManager.read(ACCESS_TOCKEN,""));
//            user.setUserId(Integer.parseInt(SharedPreferencesManager.read(USER_ID,"0")));
            user.setUserEmail( SharedPreferencesManager.read(USER_EMAIL,""));
            user.setUserType( SharedPreferencesManager.read(USER_TYPE,"USR"));
            user.setUserFirstName( SharedPreferencesManager.read(USER_FIRST_NAME,""));
            user.setUserLastName( SharedPreferencesManager.read(USER_LAST_NAME,""));
            user.setUserStatusLevel(Integer.parseInt(SharedPreferencesManager.read(USER_STATUS_LEVEL,"0")));
            user.setProfileImageUrl( SharedPreferencesManager.read(USER_IMAGE_URL,""));


            Glide.with(this).load(user.getProfileImageUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .override(90,90)
                    .placeholder(R.drawable.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(profile_image);

            profile_name.setText(user.getUserFirstName()+" "+user.getUserLastName());

        }


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
        catname.clear();
        autoAdapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,catname);
        serviceSearch.setThreshold(0);//will start working from zero character
        serviceSearch.setAdapter(autoAdapter);//setting the adapter data into the AutoCompleteTextView
        serviceSearch.setTextColor(Color.WHITE);
        serviceSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "pos: "+position+" item : "+autoAdapter.getItem(position));
                category = autoAdapter.getItem(position);
                if(mLastLocation!=null){networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this,HomeActivity.this, " ",autoAdapter.getItem(position),
                        String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)), getCurrentLocale().getLanguage(),String.valueOf(distance))
                        .execute();
// keyboard close
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                }
                else {
                    // snackbar
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int stepSize = 25;

                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);

                distance = progress+25;//min value = 25

                 Toast.makeText(HomeActivity.this, "seekbar "+ distance, Toast.LENGTH_LONG).show();


                if(mLastLocation!=null|| category!=null){networkManager.new RetrieveGetProviderListHomeTask(HomeActivity.this,HomeActivity.this, " ",category,
                        String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)), getCurrentLocale().getLanguage(),String.valueOf(distance))
                        .execute();
                }
                else {


                    // snackbar
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
        providerList.clear();
        mAdapter = new RecyclerAdap(providerList,HomeActivity.this,isGuest,HomeActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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



    private class MyClusterRenderer extends DefaultClusterRenderer<ProviderBasic> {

        MyClusterRenderer(Context context, GoogleMap map,
                          ClusterManager<ProviderBasic> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ProviderBasic item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected void onClusterItemRendered(ProviderBasic clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
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
            return myContentsView;
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    listRec.setVisibility(View.VISIBLE);
                    Maprel.setVisibility(View.GONE);

Log.i("FRAG"," true----");
                } else {
                    //Your code when unchecked
                    Log.i("FRAG"," false----");
                    listRec.setVisibility(View.GONE);
                    Maprel.setVisibility(View.VISIBLE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

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
        if (mGoogleApiClient != null) {
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

    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catnam) {

        catname.clear();
        for(String cat :catnam)
        {
            catname.add(cat);
        }
        autoAdapter.notifyDataSetChanged();

    }

    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetProviderListFailed(String msg) {

    }

    @Override
    public void GetProviderList(List<ProviderBasic> providers) {
        Log.i(TAG," providers : "+providers.size());

        // Add cluster items (markers) to the cluster manager.
       if(mClusterManager!=null) {
           addItems(providers);


       }
       else {
           //snackbar
       }
    }

    private void addItems(final List<ProviderBasic> providers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mClusterManager.clearItems();
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
        Toast.makeText(this, providerBasic.getUserFirstName() + " Clicked", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onClusterClick(Cluster<ProviderBasic> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ProviderBasic> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ProviderBasic providerBasic) {
        Toast.makeText(this, "onClusterItemClick --down", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoWindowManager.onDestroy();
    }
}

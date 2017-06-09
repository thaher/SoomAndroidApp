package com.bridge.soom.Fragment;

import android.Manifest;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.customview.TouchInterceptFrameLayout;
import com.bridge.soom.Activity.FilterActivity;
import com.bridge.soom.Activity.HomeActivity;
import com.bridge.soom.Activity.MainHomeActivity;
import com.bridge.soom.Helper.MySeekBar;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.PlacesAutoCompleteAdapter;
import com.bridge.soom.Helper.RecyclerAdap;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.USER_EMAIL;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;
import static com.bridge.soom.Helper.Constants.USER_STATUS_LEVEL;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,HomeResponse,ClusterManager.OnClusterItemInfoWindowClickListener<ProviderBasic>,
        ClusterManager.OnClusterClickListener<ProviderBasic>,
        ClusterManager.OnClusterInfoWindowClickListener<ProviderBasic>,
        ClusterManager.OnClusterItemClickListener<ProviderBasic> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG ="HOME FRAGEMENT" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 22;

    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private MapView mMapView;
    private View rootView;

    private AutoCompleteTextView serviceSearch;
    private RelativeLayout listRec,Maprel;
    private SeekBar seekBar;
    private MySeekBar distance_seek;
    private TextView textView,norsult,servicetext;

    private RelativeLayout sercvice;
    private ImageButton filter;
    private ImageButton close,cancel1,cancel2;
    private AutoCompleteTextView choselocation;

    private String Selected_Category = "" ;
    private String Selected_Category_ID ="";

    private String price_min ="";
    private String price_max ="";
    private String range_min ="";
    private String range_max ="";
    private String filters ="";
    private String filtersname ="";
    private Snackbar snackbar;
    private Location mLastLocation,selectedLocation;
    private ViewGroup hiddenPanel;
    private RelativeLayout.LayoutParams p;
    private NetworkManager networkManager;
    private Integer distance = 10;
    private Integer zoomLevel = 19;
    private String category="";
    private  String tocken="";
    private ProgressDialog progress;
    private List<String> catname,catid;
    private ArrayAdapter<String> autoAdapter;
    private InfoWindow.MarkerSpecification markerSpec;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;
    private TouchInterceptFrameLayout mapViewContainer;
    private RecyclerView recyclerView;
    private List<ProviderBasic> providerList = new ArrayList<>();
    private RecyclerAdap mAdapter;
    private Boolean isGuest = true;

    private GoogleApiClient mGoogleApiClientloc;

    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private ClusterManager<ProviderBasic> mClusterManager;
    private boolean isZooming = false;
    private boolean isShowing = false;
    private CameraPosition mPreviousCameraPosition = null;
    private float previousZoomLevel = -1.0f;
    private ProviderBasic clickedClusterItem;

    private UserModel user ;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        networkManager = new NetworkManager(getActivity());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        serviceSearch = (AutoCompleteTextView) rootView.findViewById(R.id.serviceSearch);
        listRec = (RelativeLayout)rootView.findViewById(R.id.listRec);
        Maprel= (RelativeLayout) rootView.findViewById(R.id.Maprel);
        seekBar=(SeekBar)rootView.findViewById(R.id.seekBar);
        distance_seek = (MySeekBar) rootView.findViewById(R.id.distance_seek);
        textView = (TextView) rootView.findViewById(R.id.textxxax);
        servicetext = (TextView) rootView.findViewById(R.id.servicetext);
        norsult = (TextView) rootView.findViewById(R.id.norsult);
        sercvice = (RelativeLayout)  rootView.findViewById(R.id.sercvice);
        filter = (ImageButton)  rootView.findViewById(R.id.filter);
        close = (ImageButton)  rootView.findViewById(R.id.closexx);
        cancel1 = (ImageButton)  rootView.findViewById(R.id.cancel1);
        cancel2 = (ImageButton)  rootView.findViewById(R.id.cancel2);
        choselocation = (AutoCompleteTextView)  rootView.findViewById(R.id.choselocation);
        hiddenPanel = (ViewGroup)rootView.findViewById(R.id.hidden_panel);

        mapViewContainer =
                (TouchInterceptFrameLayout) rootView.findViewById(R.id.mapViewContainer);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        sercvice = (RelativeLayout) rootView.findViewById(R.id.sercvice);

        ((MainHomeActivity)getActivity()).setFragmentRefreshListener(new MainHomeActivity.FragmentRefreshListener() {

            @Override
            public void onMapView(Boolean isMapview) {

                if (isMapview)
        {
            listRec.setVisibility(View.GONE);
            Maprel.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            distance_seek.setVisibility(View.VISIBLE);

        }
        else {
            listRec.setVisibility(View.VISIBLE);
            Maprel.setVisibility(View.GONE);
            distance_seek.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

        }

            }
        });
         isGuest = getArguments().getBoolean("GUEST");

        Log.i("FRAGDATA"," guest "+isGuest);
        if(!isGuest) {
            setuser();

        }


        return rootView;
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
        String level = SharedPreferencesManager.read(USER_STATUS_LEVEL,"0");
        if(level.isEmpty())
            level="0";
        user.setUserStatusLevel(Integer.parseInt(level));

        user.setProfileImageUrl( SharedPreferencesManager.read(USER_IMAGE_URL,""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Maprel.setVisibility(View.VISIBLE);
        listRec.setVisibility(View.GONE);

        seekBar.setProgress(0);
        seekBar.setMax(65);

        distance_seek.setMax(65);
        distance_seek.setProgress(0);

        Drawable img2 = this.getResources().getDrawable(
                R.drawable.ic_loc_fil);
        img2.setBounds(0, 0, 24, 24);
        Drawable img3 = this.getResources().getDrawable(
                R.drawable.ic_ser_fil);
        img3.setBounds(0, 0, 24, 24);
        choselocation.setCompoundDrawables(img2, null, null, null);
        //sercvice.setCompoundDrawables(img, null, null, null);
        serviceSearch.setCompoundDrawables(img3, null, null, null);

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
        hiddenPanel.setVisibility(View.INVISIBLE);

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


                        networkManager.new RetrieveGetProviderListHomeTask(HomeFragment.this,getContext(), tocken, category,
                                String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                                getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange, raterange, filters)
                                .execute();
                        showLoadingDialog();
                    } else {
                        // snackbar

                        snackbar = Snackbar.make(rootView, R.string.invalid_selection, Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }
                }
                else {
                    snackbar = Snackbar.make(rootView,"Please Select a Category", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }


            }
        });
        catname = new ArrayList<>();
        catid = new ArrayList<>();
        catname.clear();
        catid.clear();

        autoAdapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item,catname);
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

                    networkManager.new RetrieveGetProviderListHomeTask(HomeFragment.this, getContext(),tocken, autoAdapter.getItem(position),
                            String.valueOf(selectedLocation.getLatitude()), String.valueOf(selectedLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                            getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange,raterange,filters)
                            .execute();
// keyboard close

                    showLoadingDialog();
                    try {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                }
                else {
                    // snackbar

                    snackbar = Snackbar.make(rootView, R.string.location_empty, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
                slideUpDown(view);
                try {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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


                        networkManager.new RetrieveGetProviderListHomeTask(HomeFragment.this, getContext(),tocken, category,
                                String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                                getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange, raterange, filters)
                                .execute();
                        showLoadingDialog();
                    } else {
                        // snackbar

                        snackbar = Snackbar.make(rootView, R.string.invalid_selection, Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }
                }
                else {
                    snackbar = Snackbar.make(rootView,"Please Select a Category", Snackbar.LENGTH_LONG);
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

        networkManager.new RetrieveGetCategoryListHomeTask(HomeFragment.this,getContext())
                .execute();
        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);
        markerSpec =
                new InfoWindow.MarkerSpecification(offsetX, offsetY);

        infoWindowManager = new InfoWindowManager(getActivity().getSupportFragmentManager());
        infoWindowManager.onParentViewCreated(mapViewContainer, savedInstanceState);

        providerList.clear();
        mAdapter = new RecyclerAdap(providerList,getContext(),isGuest,null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
                    Intent intent = new Intent ( getContext(), FilterActivity.class);
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
                    snackbar = Snackbar.make(rootView, "Please Specify Your Search", Snackbar.LENGTH_LONG);
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
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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

        // to diable the below clickss
        hiddenPanel.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});

        mGoogleApiClientloc = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .build();// location and plces api for search bar
        LocationManager locationManager = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastKnownLocation != null) {
                mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                        mGoogleApiClientloc, toBounds(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 12000), null);
            }
            else {
                mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                        mGoogleApiClientloc, toBounds(new LatLng(9.9312, 76.2673), 12000), null);
            }
        }


        choselocation.setOnItemClickListener(mAutocompleteClickListener);
        choselocation.setAdapter(mPlacesAdapter);

        norsult.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        infoWindowManager.onMapReady(mMap);

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(9.9312,76.2673) , 15.0f) );

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
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

        setUpCluster();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);


        }

    }
    private void setUpCluster() {

        mClusterManager = new ClusterManager<ProviderBasic>(getContext(),mMap);
        //  mMap.setOnCameraIdleListener(mClusterManager);
        mClusterManager.setRenderer(new MyClusterRenderer(getContext(), mMap,
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
    public boolean onClusterClick(Cluster<ProviderBasic> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ProviderBasic> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ProviderBasic providerBasic) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ProviderBasic providerBasic) {

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
//
//        @Override
//        protected boolean shouldRenderAsCluster(Cluster<ProviderBasic> cluster) {
//
//
//            return super.shouldRenderAsCluster(cluster);   // delete this entire override methode to enbale cluster
////            return false;
//        }
    }

    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForClusters() {
            myContentsView = getActivity().getLayoutInflater().inflate(
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

                networkManager.new RetrieveGetProviderListHomeTask(HomeFragment.this, getContext(),tocken, Selected_Category,
                        String.valueOf(selectedLocation.getLatitude()), String.valueOf(selectedLocation.getLongitude()), String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)),
                        getCurrentLocale().getLanguage(), String.valueOf(distance),pricerange,raterange,filters)
                        .execute();
// keyboard close

                showLoadingDialog();
                slideUpDown(null);

                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }

        }


    };


    @Override
    public void failedtoConnect() {
        dismissLoadingDialog();

        //snackbar
        snackbar = Snackbar.make(rootView, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryList(final List<String> catiid, final List<String> catnam) {
        getActivity().runOnUiThread(new Runnable() {
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

        snackbar = Snackbar.make(rootView,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void GetProviderListFailed(String msg) {
        //snackbar
        dismissLoadingDialog();
        Log.i("LOGGGG",msg);
        snackbar = Snackbar.make(rootView,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


    }

    @Override
    public void GetProviderList(final List<ProviderBasic> providers) {
        getActivity().runOnUiThread(new Runnable() {
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
                    snackbar = Snackbar.make(rootView, R.string.no_providers, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
            }});

    }

    private void addItems(final List<ProviderBasic> providers) {
        getActivity().runOnUiThread(new Runnable() {
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

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
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        infoWindowManager.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setMessage(getString(R.string.loading_message));
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
        }
    }


    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
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
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }
    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }
    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }


}

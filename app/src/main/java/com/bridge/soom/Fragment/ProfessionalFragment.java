package com.bridge.soom.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bridge.soom.Activity.ProfessionalDetailsActivity;
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
import com.bridge.soom.Model.UserModel;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfessionalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfessionalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfessionalFragment extends Fragment implements GetCatDatas,MultiSpinner.MultiSpinnerListener , ServiceandLocListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private UserModel userModel;
    private View view;

    private RecyclerView recyclerView,recyclerViewLoc;
    private RecyclerAdapService mAdapter;
    private RecyclerAdapLocation mAdapterloc;

    private List<Services> servicesList ;
    private List<PlaceLoc> locList ;
    private ImageButton addservice,addlocation;
    private NetworkManager networkManager;
    private ArrayAdapter<String> dataAdapter,dataAdapter2;
    private List<String> services,filters,servicesid,filtersid,Sfilters,SfiltersID;
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
    private boolean isEditing =false;
    private Services editingService;

    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private GoogleApiClient mGoogleApiClientloc;
    private AutoCompleteTextView choselocation;
    private Place place1;
    private ImageButton mSwitchShowSecure;
    private String AccessTocken;
    private ProgressDialog progress;

    public ProfessionalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfessionalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfessionalFragment newInstance(String param1, String param2) {
        ProfessionalFragment fragment = new ProfessionalFragment();
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
        SharedPreferencesManager.init(getActivity());
        AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_professional, container, false);
        // Inflate the layout for this fragment
        Log.i("FRAGINIT","professional tab");
        hiddenPanel = (ViewGroup)view.findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        hiddenPanelloc = (ViewGroup)view.findViewById(R.id.hidden_panel_loc);
        hiddenPanelloc.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerViewLoc = (RecyclerView) view.findViewById(R.id.recycler_view_lang);
        addservice = (ImageButton)view.findViewById(R.id.addservice);
        addlocation = (ImageButton)view.findViewById(R.id.addlocation);
        close_popup = (ImageButton) view.findViewById(R.id.close_popup);
        close_popup_loc = (ImageButton) view.findViewById(R.id.close_popup_loc);
        choselocation = (AutoCompleteTextView) view.findViewById(R.id.choselocation);


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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        RecyclerView.LayoutManager mLayoutManagerLang = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLoc.setLayoutManager(mLayoutManagerLang);
        recyclerViewLoc.setItemAnimator(new DefaultItemAnimator());
        isEditing =false;
        servicesList = new ArrayList<Services>();
        servicesList.clear();
        locList = new ArrayList<PlaceLoc>();
        locList.clear();
        mAdapter = new RecyclerAdapService(servicesList,getContext(),ProfessionalFragment.this,networkManager,AccessTocken,false,ProfessionalFragment.this);
        recyclerView.setAdapter(mAdapter);
        mAdapterloc = new RecyclerAdapLocation(locList,getContext(),ProfessionalFragment.this,networkManager,AccessTocken);
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


        service = (Spinner) view.findViewById(R.id.service);
        subservice = (MultiSpinner) view.findViewById(R.id.subservice);
        submit = (Button) view.findViewById(R.id.submit);
        submit_loc = (Button) view.findViewById(R.id.submitloc);
        experiance = (EditText) view.findViewById(R.id.experiance);
        wages = (EditText) view.findViewById(R.id.wages);
        subservicex = (LinearLayout) view.findViewById(R.id.subservicex);
        dataAdapter = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, services){
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

        dataAdapter2 = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, filters){
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
                if(position!=0) { showLoadingDialog();
                    networkManager.new RetrieveGetSubCategoryListTask(ProfessionalFragment.this, servicesid.get(position))
                        .execute();}
                else {
                    subservicex.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        networkManager.new RetrieveGetCategoryListTask(ProfessionalFragment.this)
                .execute();
        showLoadingDialog();
        networkManager.new RetrieveSelectionServiceTask(ProfessionalFragment.this,AccessTocken)
                .execute();
        networkManager.new RetrieveLocationTask(ProfessionalFragment.this,AccessTocken)
                .execute();


        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown(v);
                isEditing = false;
                editingService = null;
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
                if(isValid()) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    Services newService = new Services();
                    newService.setServiceId(servicesid.get(service.getSelectedItemPosition()));
                    newService.setServiceName(services.get(service.getSelectedItemPosition()));
                    newService.setSubServiceId(SfiltersID);
                    newService.setSubServiceName(Sfilters);
                    newService.setExperiance(experiance.getText().toString().trim());
                    newService.setWages(wages.getText().toString().trim());
                    if (!isEditing)
                    {servicesList.add(newService);}
                    mAdapter.notifyDataSetChanged();
                    slideUpDown(v);
                    showLoadingDialog();
                    if (!isEditing)
                    {
                    networkManager.new AddServiceTask(ProfessionalFragment.this,AccessTocken,newService)
                            .execute();}
                    else {
                        isEditing = false;
                        editingService = null;
                        networkManager.new EditServiceTask(ProfessionalFragment.this,AccessTocken,newService)
                                .execute();
                    }



                }
            }
        });
        submit_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid_loc())
                {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
                    showLoadingDialog();
                    networkManager.new AddLocationTask(ProfessionalFragment.this,AccessTocken,newLoc)
                            .execute();



                }
            }
        });

        mGoogleApiClientloc = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .build();// location and plces api for search bar
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

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

        return view;
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
    public void failedtoConnect() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetServiceListFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void GetServiceList(final List<Services> servicesListi) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                servicesList.clear();
                for (Services cat : servicesListi) {
                    servicesList.add(cat);
                }
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void GetLocationList(final List<PlaceLoc> placeLocListi) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();

                locList.clear();
                for (PlaceLoc cat : placeLocListi) {
                    locList.add(cat);
                }
                mAdapterloc.notifyDataSetChanged();
            }
        });
        Log.i("PROFFF", " GetLocationList");

    }

    @Override
    public void GetLocationListFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        Log.i("PROFFF", " GetLocationListFailed");


    }

    @Override
    public void AddLocationSuccess() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, R.string.location_add_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
                networkManager.new RetrieveLocationTask(ProfessionalFragment.this, AccessTocken)
                        .execute();
            }});
        Log.i("PROFFF", " AddLocationSuccess");


    }

    @Override
    public void AddLocationFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
                networkManager.new RetrieveLocationTask(ProfessionalFragment.this,AccessTocken)
                        .execute();}});
        Log.i("PROFFF", " AddLocationFailed");

    }

    @Override
    public void DeleteLocationSuccess() {
        Log.i("PROFFFDEL"," Profesional Fragment true");
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(view, "Success", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        showLoadingDialog();
                networkManager.new RetrieveLocationTask(ProfessionalFragment.this,AccessTocken)
                        .execute();

    }

    @Override
    public void DeleteLocationFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void AddServiceSuccess() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, R.string.service_add_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        showLoadingDialog();
                networkManager.new RetrieveSelectionServiceTask(ProfessionalFragment.this,AccessTocken)
                        .execute();
    }

    @Override
    public void AddServiceFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

        showLoadingDialog();
                networkManager.new RetrieveSelectionServiceTask(ProfessionalFragment.this,AccessTocken)
                        .execute();


    }

    @Override
    public void DeleteServiceSuccess() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view,"Success !", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        showLoadingDialog();
                networkManager.new RetrieveSelectionServiceTask(ProfessionalFragment.this, AccessTocken)
                        .execute();


    }

    @Override
    public void DeleteServiceFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void GetCategoryListFailed(String msg) {
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(view,msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryList(final List<String> catid, final List<String> catname) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();

                filters.clear();
                filtersid.clear();
//                filters.add("Choose filters");
//                filtersid.add("0");
                for(int i=0;i<subcatid.size();i++)
                {
                    filters.add(subcatname.get(i));
                    filtersid.add(subcatid.get(i));
                    Log.i("EDITINGSERVICE", "subcatname.get(i)" + subcatname.get(i));

                }

                if(!isEditing)
                {
                    subservice.setItems(filters, "Choose a Specialization", ProfessionalFragment.this);
                }
                else {
                    subservice.setItemsEdting(filters,filtersid, "Choose a Specialization", ProfessionalFragment.this,editingService);
//                    subservice.setItems(filters, "Choose a Specialization", ProfessionalFragment.this);

                }


                dataAdapter2.notifyDataSetChanged();
                subservicex.setVisibility(View.VISIBLE);
                Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );

            }
        });

    }

    @Override
    public void GetSubCategoryListFailed(String msg) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view,msg, Snackbar.LENGTH_LONG);
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
    private boolean isValid_loc() {
        if(place1==null&&choselocation.getText().toString().trim().isEmpty())
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.service_empty, Snackbar.LENGTH_LONG);
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
                    .make(view, R.string.service_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else   if(checkin(servicesList,servicesid.get(service.getSelectedItemPosition()))&&!isEditing)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.service_exist, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else  if(!selection)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.subservice_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if (experiance.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.experiance_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }else if (wages.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.wages_empty, Snackbar.LENGTH_LONG);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mAdapter != null) {
            mAdapter.saveStates(outState);
        }
    }

    public void editService(Services providerBasic) {
        Log.i("EDITINGSERVICE","FRAGMEN   :"+providerBasic.getServiceName());
        isEditing =true;
        editingService =providerBasic;
        slideUpDown(null);
        showLoadingDialog();
        networkManager.new RetrieveGetSubCategoryListTask(ProfessionalFragment.this,providerBasic.getServiceId())
                .execute();

        if (providerBasic.getServiceName() != null && !providerBasic.getServiceName().isEmpty()) {

            service.setSelection(findinlist(services, providerBasic.getServiceName().trim().toLowerCase()));

        }

        experiance.setText(providerBasic.getExperiance());
        wages.setText(providerBasic.getWages());

    }

//    public void deleteService(Services providerBasic) {
//        networkManager.new DeleteServiceTask(ProfessionalFragment.this,AccessTocken,providerBasic.getServiceId())
//                .execute();
//
//
//    }
//
//    public void editLocation(PlaceLoc providerBasic) {
//    }
//
//    public void deleteLocation(PlaceLoc providerBasic) {
//        networkManager.new DeleteLocationTask(ProfessionalFragment.this,AccessTocken,providerBasic.getId())
//                .execute();
//    }


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

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
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
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);

            hiddenPanelloc.startAnimation(bottomUp);
            hiddenPanelloc.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
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
    public void onStart() {
        super.onStart();
        mGoogleApiClientloc.connect();


    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClientloc.disconnect();

    }

    private int findinlist(List<String> services, String trim) {
        for(int i=0;i<services.size();i++)
        {
            Log.i("FINDCAT"," "+services.get(i).trim().toLowerCase()+" "+trim);
            if(services.get(i).trim().toLowerCase().equals(trim))
            {
                Log.i("FINDCAT","city  "+i);
                Log.i("CITYXISD","--setting" );

                return i;
            }
        }
        return 0;
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

}



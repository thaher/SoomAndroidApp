package com.bridge.soom.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.soom.Activity.MoreActivity;
import com.bridge.soom.Activity.PersonalDetailsActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.ImageUploader;
import com.bridge.soom.Interface.PersonalDetailsResponse;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment implements ProviderDetailsResponse ,CalendarDatePickerDialogFragment.OnDateSetListener,AdapterView.OnItemClickListener,PersonalDetailsResponse,GetCatDatas,ImageUploader {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserModel userModel;

    private CircleImageView profile_image;
    private ImageButton uploadimg;
    private ImageButton regfillsubmit;
    private ProgressBar uploadprogress;
    private Integer PROFILE_PIC_COUNT =0;
    private static final int REQUEST_CAMERA = 11;
    private static final int SELECT_FILE = 22;
    private Uri outputFileUri;
    private Uri selectedImage = null;

    private Spinner spinner,state,city,country;
    private ArrayAdapter<String> dataAdapter,dataAdapter3,dataAdapter4,dataAdapter5;
    private List<String> categories, stateid,statels,cityid,cityname,countryls,countryid;
    private EditText dob,address,education,languages,zip;
    private TextView msgtxt;
    private CalendarDatePickerDialogFragment cdp;
    private Snackbar snackbar;
    private String dobtextfin = "";
    private NetworkManager networkManager;
    private LinearLayout cityll,statell,lledu,lllan;
    private String AccessTocken ="";
    private String UserType ="";
    private String photurl ="";

    private View view;

    private OnFragmentInteractionListener mListener;

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
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
        networkManager = new NetworkManager(getContext());
        SharedPreferencesManager.init(getContext());
        AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");
        UserType = SharedPreferencesManager.read(USER_TYPE,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FRAGINIT","personal tab");
        userModel = (UserModel) getArguments().getSerializable("userModel");


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("profile_data"));
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);


        cityll = (LinearLayout) view.findViewById(R.id.cityll);
        statell = (LinearLayout)view.findViewById(R.id.statell);
        lledu = (LinearLayout)view.findViewById(R.id.lledu);
        lllan = (LinearLayout)view.findViewById(R.id.lllan);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        uploadimg = (ImageButton) view.findViewById(R.id.uploadimg);
        regfillsubmit = (ImageButton) view.findViewById(R.id.regfillsubmit);
        uploadprogress = (ProgressBar) view.findViewById(R.id.uploadprogress);
        spinner = (Spinner) view.findViewById(R.id.spingender);
        country = (Spinner) view.findViewById(R.id.country);
        state = (Spinner) view.findViewById(R.id.state);
        city = (Spinner) view.findViewById(R.id.city);
        dob = (EditText) view.findViewById(R.id.dob);

        address = (EditText) view.findViewById(R.id.address);
        education = (EditText) view.findViewById(R.id.eveduset);
        languages = (EditText) view.findViewById(R.id.evlanguageset);
        zip = (EditText) view.findViewById(R.id.zip);
        msgtxt = (TextView) view.findViewById(R.id.msgtxt);
        if(!UserType.trim().equals("USR")){

            msgtxt.setText("Provider");
        }
        else {
            msgtxt.setText("Seeker");
            lllan.setVisibility(View.GONE);
            lledu.setVisibility(View.GONE);
        }

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picker for camer aor gsallery
                if (Build.VERSION.SDK_INT < 23) {
                    //We already have permission. Write your function call over hear
                    takepic();
                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Here we are asking for permission
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        //If the app is running for second time, then we already have permission. You can write your function here, if we already have permission.
                        takepic();
                    }
                }
            }
        });
        regfillsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(isvalid())
                {submitdata();}
            }
        });


        categories = new ArrayList<String>();
        categories.add("*Gender");
        categories.add("Female");
        categories.add("Male");
        categories.add("Others");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, categories){
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
                    tv.setTextColor(getResources().getColor(R.color.colorDefaulltext));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(PersonalFragment.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, null)
                .setDoneText("Done")
                .setCancelText("Cancel");

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdp.show(getActivity().getSupportFragmentManager(), "DATE_FRAG");
            }
        });

        countryls = new ArrayList<String>();
        countryid = new ArrayList<String>();
        countryls.clear();
        countryid.clear();
        countryls.add("Choose a Country");
        countryid.add("0");

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
        dataAdapter3 = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, statels){
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
                    tv.setTextColor(getResources().getColor(R.color.colorDefaulltext));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        state.setAdapter(dataAdapter3);


        // Creating adapter for spinner
        dataAdapter4 = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, cityname){
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
                    tv.setTextColor(getResources().getColor(R.color.colorDefaulltext));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        city.setAdapter(dataAdapter4);



        // Creating adapter for spinner
        dataAdapter5 = new ArrayAdapter<String>(getContext(),  R.layout.simple_spinner_item, countryls){
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
                    tv.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter5.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        country.setAdapter(dataAdapter5);
        statell.setVisibility(View.GONE);
        cityll.setVisibility(View.GONE);
        networkManager.new RetrieveGetCountryListTask(PersonalFragment.this)
                .execute();

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {

                    networkManager.new RetrieveGetStateListTask(PersonalFragment.this,countryid.get(position))
                            .execute();
                    statell.setVisibility(View.VISIBLE);}
                else {
                    statell.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {  networkManager.new RetrieveGetCityListTask(PersonalFragment.this, stateid.get(position))
                        .execute();
                    cityll.setVisibility(View.VISIBLE);}
                else {
                    cityll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(this.userModel!=null)
        {
            setuserdata();
        }






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
    public void DetailsResponseSuccess(UserModel userModel) {
        Log.i("PROFILEINFO"," XXXXXXX Personal Fragment  fragment  ");
        this.userModel  = userModel;
        if(this.userModel!=null)
        {
            setuserdata();
        }



    }

    private void setuserdata() {
if(view!=null)
{
    networkManager.new RetrieveGetCountryListTask(PersonalFragment.this)
            .execute();
    if(userModel.getCountryId()!=null&&!userModel.getCountryId().toString().isEmpty())
        networkManager.new RetrieveGetStateListTask(PersonalFragment.this, userModel.getCountryId().toString())
                .execute();
    if(userModel.getStateId()!=null&&!userModel.getStateId().toString().isEmpty())
        networkManager.new RetrieveGetCityListTask(PersonalFragment.this, userModel.getStateId().toString())
                .execute();


    Glide.with(this)
                .load(userModel.getProfileImageUrl().trim())
                .placeholder(R.drawable.avatar)
                .into(new GlideDrawableImageViewTarget(profile_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        Log.d("PROFILEPERSONAL", "readyy " + userModel.getProfileImageUrl().trim());

                        profile_image.setImageDrawable(drawable);

                    }
                });
        Integer genderpos = 0;
        Log.d("PROFILEPERSONAL", "genderpos " + genderpos);

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).trim().equals(userModel.getUserGender())) {
                genderpos = i;
            }
            Log.d("PROFILEPERSONAL", "genderpos " + genderpos + " " + userModel.getUserGender());
        }
        spinner.setSelection(genderpos);
    dob.setText(userModel.getDob());
    dobtextfin = userModel.getDob();
    address.setText(userModel.getUserAddress());
    zip.setText(userModel.getZip());
    languages.setText(userModel.getLanguagesknown());
    education.setText(userModel.getUserEducation());


    }
    }

    @Override
    public void DetailsResponseFailed(String message) {

    }



    @Override
    public void ResponseSuccess(UserModel userModel) {
        this.userModel  = userModel;
        if(this.userModel!=null)
        {
            setuserdata();
        }
    }

    @Override
    public void ResponseFailed(String message) {

    }

    @Override
    public void failedtoConnect() {
        snackbar = Snackbar
                .make(view, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void UploadSuccess(final String msg, final String url) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                snackbar = Snackbar
                        .make(view, msg, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                snackbar.show();
                photurl = url;
                userModel.setProfileImageUrl(photurl);
                Glide.with(PersonalFragment.this)
                        .load(userModel.getProfileImageUrl().trim())
                        .placeholder(R.drawable.avatar)
                        .into(new GlideDrawableImageViewTarget(profile_image) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                Log.d("PROFILEPERSONAL", "readyy " + userModel.getProfileImageUrl().trim());

                                profile_image.setImageDrawable(drawable);

                            }
                        });
            }});


    }

    @Override
    public void UploadFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryListFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catname) {

    }

    @Override
    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname, String highestWage) {

    }

    @Override
    public void GetSubCategoryListFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetStateCategoryList(final List<String> subcatid, final List<String> subcatname) {
        getActivity().runOnUiThread(new Runnable() {
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

                if (userModel.getStateName() != null && !userModel.getStateName().isEmpty()) {

                    state.setSelection(findinlist(statels, userModel.getStateName().trim().toLowerCase()));

                }

                Log.i("Reg2_submit","GetStateCategoryList ---got2" );

            }
        });
    }

    @Override
    public void GetStateListFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCityCategoryList(final List<String> subcatid, final List<String> subcatname, List<String> lat, List<String> lng) {
        getActivity().runOnUiThread(new Runnable() {
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

                if (userModel.getCityName() != null && !userModel.getCityName().isEmpty())
                {
                    city.setSelection(findinlist(cityname, userModel.getCityName().trim().toLowerCase()));
                }

            }
        });
    }

    @Override
    public void GetCityListFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void GetCountryCategoryList(final List<String> subcatid, final List<String> subcatname) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                countryls.clear();
                countryid.clear();
                countryls.add("Choose a Country");
                countryid.add("0");
                for(int i=0;i<subcatname.size();i++)
                {
                    countryls.add(subcatname.get(i));
                    countryid.add(subcatid.get(i));
                }


                dataAdapter5.notifyDataSetChanged();
                Log.i("Reg2_submit","GetCountryCategoryList ---got2" );

                if (userModel.getCountryName() != null && !userModel.getCountryName().isEmpty()) {

                    country.setSelection(findinlist(countryls, userModel.getCountryName().trim().toLowerCase()));
                }




            }
        });
    }

    @Override
    public void GetCountryListFailed(String msg) {
        snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            Log.i("DATE18","not 18");
            snackbar = Snackbar
                    .make(view, R.string.dob_noteighteen, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            dob.requestFocus();
            dob.setText("Provider Should be atleast 18 Years old.");
            dob.setError("Provider Should be atleast 18 Years old.");
            dobtextfin = "";


        }else
        {
            Log.i("DATE18","yes 18");
            monthOfYear+=1;

            dob.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
            dobtextfin=dayOfMonth+"-"+monthOfYear+"-"+year;
            dob.setError(null);

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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            userModel = (UserModel)intent.getSerializableExtra("userModel");
            if(userModel!=null)
            {
                setuserdata();
            }
        }
    };

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.i("Capturing","onactivty result in personal frag");
        switch(requestCode) {

            case 11:
                if(resultCode == RESULT_OK){

                    //Uri selectedImage = imageReturnedIntent.getData();
                    selectedImage  = (Uri.parse(getPath(outputFileUri)));
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = getContentResolver();
//                    Bitmap bitmap;
//                    try {
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//                        profile_image.setImageBitmap(bitmap);
//                        Toast.makeText(this, selectedImage.toString(),
//                                Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                                .show();
//                        Log.e("Camera", e.toString());
//                    }

                    profile_image.setImageURI(selectedImage);
                    Log.i("Capturing"," result "+selectedImage.toString());
                    File myFile = new File(selectedImage.getPath());

                    networkManager.new SaveProfileImage(PersonalFragment.this,myFile,AccessTocken)
                            .execute();
                }
                break;
            case 22:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    selectedImage = Uri.parse(getPath(selectedImage));
                    profile_image.setImageURI(selectedImage);
                    Log.i("Capturing","result "+selectedImage.toString());
                    File myFile = new File(selectedImage.getPath());
                    networkManager.new SaveProfileImage(PersonalFragment.this,myFile,AccessTocken)
                            .execute();
                }
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    takepic();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor != null ? cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) : 0;
        if (cursor != null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);

            cursor.close();
            return imagePath;}
        return null;
    }

    private void takepic() {
        Log.i("Capturing","takepic");



        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    PROFILE_PIC_COUNT = 1;
//                    File photo = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+"soom_profile.jpg");
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photo));
//
//                    outputFileUri = Uri.fromFile(photo);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//                    startActivityForResult(intent, REQUEST_CAMERA);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    outputFileUri = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void submitdata() {
        String gendertext = categories.get(spinner.getSelectedItemPosition());
        String countrytext = countryid.get(country.getSelectedItemPosition());
        String  statetext = stateid.get(state.getSelectedItemPosition());
        String  citytext = cityid.get(city.getSelectedItemPosition());
        String  edutext = education.getText().toString();
        String  dobtext = dobtextfin;
        String  addresstext = address.getText().toString();
        String  langugetext = languages.getText().toString();
        String  ziptext = zip.getText().toString();
        String Aboutme ;


        networkManager.new SubmitPersonalDetailsTask(PersonalFragment.this,AccessTocken,gendertext,countrytext,statetext,citytext,edutext,dobtext,
                addresstext,langugetext,ziptext,photurl)
                .execute();


    }

    private boolean isvalid() {
        if(spinner.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.gender_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if (dobtextfin.isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.dob_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else
        if(country.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.country_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }else
        if(state.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.state_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }else
        if(city.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.city_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if(zip.getText().toString().trim().isEmpty())
        {
            // snackie
            snackbar = Snackbar
                    .make(view, R.string.city_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }

        return true;
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
}

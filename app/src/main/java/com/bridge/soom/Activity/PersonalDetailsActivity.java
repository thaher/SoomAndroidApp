package com.bridge.soom.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.ImageUploader;
import com.bridge.soom.Interface.PersonalDetailsResponse;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

public class PersonalDetailsActivity extends AppCompatActivity  implements CalendarDatePickerDialogFragment.OnDateSetListener,AdapterView.OnItemClickListener,PersonalDetailsResponse,GetCatDatas ,ImageUploader {
    private CircleImageView profile_image;
    private ImageButton uploadimg;
    private ImageButton regfillsubmit;
    private ProgressBar uploadprogress;
    private Integer PROFILE_PIC_COUNT =0;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri outputFileUri;
    private Uri selectedImage = null;

    private Spinner spinner,state,city,country;
    private ArrayAdapter<String> dataAdapter,dataAdapter3,dataAdapter4,dataAdapter5;
    private List<String> categories, stateid,statels,cityid,cityname,countryls,countryid;
    private EditText dob,address,education,languages,zip;
    private TextView msgtxt;
    private CalendarDatePickerDialogFragment cdp;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private String dobtextfin = "";
    private NetworkManager networkManager;
    private LinearLayout cityll,statell,lledu,lllan;
    private String AccessTocken ="";
    private String UserType ="";
    private String photurl ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        networkManager = new NetworkManager(this);
        SharedPreferencesManager.init(this);
        AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");
        UserType = SharedPreferencesManager.read(USER_TYPE,"");

        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        cityll = (LinearLayout) findViewById(R.id.cityll);
        statell = (LinearLayout)findViewById(R.id.statell);
        lledu = (LinearLayout)findViewById(R.id.lledu);
        lllan = (LinearLayout)findViewById(R.id.lllan);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        uploadimg = (ImageButton) findViewById(R.id.uploadimg);
        regfillsubmit = (ImageButton) findViewById(R.id.regfillsubmit);
        uploadprogress = (ProgressBar) findViewById(R.id.uploadprogress);
        spinner = (Spinner) findViewById(R.id.spingender);
        country = (Spinner) findViewById(R.id.country);
        state = (Spinner) findViewById(R.id.state);
        city = (Spinner) findViewById(R.id.city);
        dob = (EditText) findViewById(R.id.dob);
        address = (EditText) findViewById(R.id.address);
        education = (EditText) findViewById(R.id.eveduset);
        languages = (EditText) findViewById(R.id.evlanguageset);
        zip = (EditText) findViewById(R.id.zip);
        msgtxt = (TextView) findViewById(R.id.msgtxt);


        if(!UserType.trim().equals("USR")){
            lllan.setVisibility(View.GONE);
            lledu.setVisibility(View.GONE);
            msgtxt.setText("Provider");
        }
        else {
            msgtxt.setText("Seeker");
        }



        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picker for camer aor gsallery
                if (Build.VERSION.SDK_INT < 23) {
                    //We already have permission. Write your function call over hear
                    takepic();
                } else {
                    if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Here we are asking for permission
                        ActivityCompat.requestPermissions(PersonalDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                if(isvalid())
                {
                    submitdata();
                }
            }
        });


        categories = new ArrayList<String>();
        categories.add("*Gender");
        categories.add("Female");
        categories.add("Male");
        categories.add("Others");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, categories){
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
                .setOnDateSetListener(PersonalDetailsActivity.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, null)
                .setDoneText("Done")
                .setCancelText("Cancel");

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdp.show(getSupportFragmentManager(), "DATE_FRAG");
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
        dataAdapter5 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, countryls){
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
        networkManager.new RetrieveGetCountryListTask(PersonalDetailsActivity.this)
                .execute();

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {

                    networkManager.new RetrieveGetStateListTask(PersonalDetailsActivity.this,countryid.get(position))
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
                if(position!=0) {  networkManager.new RetrieveGetCityListTask(PersonalDetailsActivity.this, stateid.get(position))
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

//        dob.clearFocus();
//        address.clearFocus();
//        education.clearFocus();
//        languages.clearFocus();
//        zip.clearFocus();



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


        networkManager.new SubmitPersonalDetailsTask(PersonalDetailsActivity.this,AccessTocken,gendertext,countrytext,statetext,citytext,edutext,dobtext,
                addresstext,langugetext,ziptext)
                .execute();

//        snackbar = Snackbar
//                .make(cordi,"submitinjg........", Snackbar.LENGTH_LONG);
//        View snackBarView = snackbar.getView();
//        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//        snackbar.show();
    }

    private boolean isvalid() {
        if(spinner.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.gender_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if (dobtextfin.isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.dob_empty, Snackbar.LENGTH_LONG);
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
                    .make(cordi, R.string.country_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }else
        if(state.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.state_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }else
        if(city.getSelectedItemPosition()==0)
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.city_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;

        }
        else if(zip.getText().toString().trim().isEmpty())
        {
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.city_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }

        return true;
    }


    private void takepic() {


        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalDetailsActivity.this);
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
                    outputFileUri = getContentResolver().insert(
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.i("Capturing","onactivty result");
        switch(requestCode) {

            case 1:
                if(resultCode == RESULT_OK){

                    //Uri selectedImage = imageReturnedIntent.getData();
                    selectedImage  = (Uri.parse(getPath(outputFileUri)));
                    getContentResolver().notifyChange(selectedImage, null);
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

                    networkManager.new SaveProfileImage(PersonalDetailsActivity.this,myFile,AccessTocken)
                            .execute();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    selectedImage = Uri.parse(getPath(selectedImage));
                    profile_image.setImageURI(selectedImage);
                    Log.i("Capturing","result "+selectedImage.toString());
                    File myFile = new File(selectedImage.getPath());
                    networkManager.new SaveProfileImage(PersonalDetailsActivity.this,myFile,AccessTocken)
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
                    Toast.makeText(PersonalDetailsActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor != null ? cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) : 0;
        if (cursor != null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);

            cursor.close();
            return imagePath;}
        return null;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ResponseSuccess(String message) {

    }

    @Override
    public void ResponseFailed(String message) {

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
    public void UploadSuccess(String msg, String url) {
        snackbar = Snackbar
                .make(cordi, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        photurl = url;

    }


    @Override
    public void UploadFailed(String msg) {
        snackbar = Snackbar
                .make(cordi, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

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


    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catname) {

    }

    @Override
    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname, String highestWage) {

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

}

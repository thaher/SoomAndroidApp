package com.bridge.soom.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.R;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationPVRActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {
    private ImageButton fab;
    private CircleImageView profile_image;
    private EditText gender,dob,address,education,designation,experiance,hourlywages,languages,emptype;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Integer PROFILE_PIC_COUNT =0;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    Uri selectedImage = null;
    private Uri outputFileUri;
    private File root;
    private String fname = "dsfg";
    private File sdImageMainDirectory ;
    Spinner spinner,spinneremp; //spinneredu
    private  String gendertext,edutext,emptext,dobtext,addresstext,experincetext,desigtext,hourlytext,langugetext;
    List<String> categories,educat,empcat;
    ArrayAdapter<String> dataAdapter,dataAdapter2,dataAdapter3;
    CalendarDatePickerDialogFragment cdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         fab = (ImageButton) findViewById(R.id.fab);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
//        gender = (EditText) findViewById(R.id.gender);
        dob = (EditText) findViewById(R.id.dob);
        address = (EditText) findViewById(R.id.address);
     //   education = (EditText) findViewById(R.id.education);
        designation = (EditText) findViewById(R.id.designation);
        experiance = (EditText) findViewById(R.id.experiance);
        languages = (EditText) findViewById(R.id.languages);
      //  emptype = (EditText) findViewById(R.id.emptype);
        hourlywages = (EditText) findViewById(R.id.hourlywages);

        spinner = (Spinner) findViewById(R.id.spingender);
        education = (EditText) findViewById(R.id.education);
        spinneremp = (Spinner) findViewById(R.id.spinemptype);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);

        cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(RegistrationPVRActivity.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, null)
                .setDoneText("Done")
                .setCancelText("Cancel");

//        // Determine Uri of camera image to save.
//        root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
//        root.mkdirs();
////       fname = Utils.getUniqueImageFilename();
//        sdImageMainDirectory = new File(root, fname);
//        outputFileUri = Uri.fromFile(sdImageMainDirectory);
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
        spinner.setAdapter(dataAdapter);

        educat = new ArrayList<String>();
        educat.add("*Education");
        educat.add("cat2");
        educat.add("cat3");

        // Creating adapter for spinner
        dataAdapter2 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, educat){
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


//        dataAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
//        spinneredu.setAdapter(dataAdapter2);


        empcat = new ArrayList<String>();
        empcat.add("*Employment Type");
        empcat.add("cat11");
        empcat.add("cat22");
        empcat.add("ca2t3");

        // Creating adapter for spinner
        dataAdapter3 = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item, empcat){
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
        spinneremp.setAdapter(dataAdapter3);


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdp.show(getSupportFragmentManager(), "DATE_FRAG");
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
               if(fieldsValid()) {

                   gendertext = categories.get(spinner.getSelectedItemPosition());
                   edutext = education.getText().toString();
                   emptext = empcat.get(spinneremp.getSelectedItemPosition());
                   dobtext = dob.getText().toString();
                   addresstext = address.getText().toString();
                   experincetext = experiance.getText().toString();
                   desigtext = designation.getText().toString();
                   hourlytext  = hourlywages.getText().toString();
                   langugetext = languages.getText().toString();
                   //selectedImage.toString()


                   Intent intent = new Intent (RegistrationPVRActivity.this, RegistrationPVRDetailesActivity.class);
                   intent.putExtra("gender",gendertext);
                   intent.putExtra("edu",edutext);
                   intent.putExtra("emp",emptext);
                   intent.putExtra("dob",dobtext);
                   intent.putExtra("add",addresstext);
                   intent.putExtra("exp",experincetext);
                   intent.putExtra("desig",desigtext);
                   intent.putExtra("hour",hourlytext);
                   intent.putExtra("lang",langugetext);
                   if(selectedImage!=null)
                       intent.putExtra("img",getPath(selectedImage));

                   startActivity(intent);

               }

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picker for camer aor gsallery


                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegistrationPVRActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            PROFILE_PIC_COUNT = 1;
                            File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photo));

                            outputFileUri = Uri.fromFile(photo);
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
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    private boolean fieldsValid() {
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
        else if (dob.getText().toString().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.dob_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (education.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.edu_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (!validateName(designation.getText().toString())){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.desi_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (experiance.getText().toString().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.exp_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (hourlywages.getText().toString().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.hr_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (languages.getText().toString().trim().isEmpty()){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.lanf_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (spinneremp.getSelectedItemPosition()==0){
            // snackie
            snackbar = Snackbar
                    .make(cordi, R.string.emp_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.i("Capturing","onactivty result");
        switch(requestCode) {

            case 1:
                if(resultCode == RESULT_OK){

                    //Uri selectedImage = imageReturnedIntent.getData();
                   selectedImage  = outputFileUri;
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

                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                     selectedImage = imageReturnedIntent.getData();
                    profile_image.setImageURI(selectedImage);
                    Log.i("Capturing","result "+selectedImage.toString());

                }
                break;


        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dob.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
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
    public boolean validateName( String Name )
    {
        Log.i("validateName"," "+Name.matches( "[a-zA-Z][a-zA-Z]*" ));
        return Name.matches( "[a-zA-Z][a-zA-Z]*" );
    }

}

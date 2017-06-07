package com.bridge.soom.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ProfileUpdateListner;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.USER_EMAIL;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;
import static com.bridge.soom.Helper.Constants.USER_STATUS_LEVEL;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

public class ProfileActivity extends BaseActivity implements ProviderDetailsResponse,ProfileUpdateListner {

    private ToggleButton mSwitchShowSecure;
    private UserModel userModel;
    private TextView tvfnameset,tvlnameset,tvnumberset,tvemailset,usertype;
    private EditText evfnameset,evlnameset,evnumberset,evemailset;
    private CircleImageView profile_image;
    private RelativeLayout changepass, more;
    private NetworkManager networkManager;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;
    private View vieq1;
    private Boolean gotdata =false;

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Integer PROFILE_PIC_COUNT =0;

    Uri selectedImage = null;
    private Uri outputFileUri;
    private File profileIMG = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userModel = (UserModel) getIntent().getSerializableExtra("MYUSER");
        networkManager = new NetworkManager(this);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        tvfnameset = (TextView) findViewById(R.id.tvfnameset);
        tvlnameset = (TextView) findViewById(R.id.tvlnameset);
        tvnumberset = (TextView) findViewById(R.id.tvnumberset);
        tvemailset = (TextView) findViewById(R.id.tvemailset);

        evfnameset = (EditText) findViewById(R.id.evfnameset);
        evlnameset = (EditText) findViewById(R.id.evlnameset);
        evnumberset = (EditText) findViewById(R.id.evnumberset);
        evemailset = (EditText) findViewById(R.id.evemailset);

        usertype = (TextView) findViewById(R.id.usertype);
        changepass = (RelativeLayout) findViewById(R.id.changepass);
        more = (RelativeLayout) findViewById(R.id.more);
        more.setVisibility(View.GONE);
        vieq1 = (View) findViewById(R.id.vieq1);

        cordi = (CoordinatorLayout)findViewById(R.id.cordi);

//        evfnameset.setVisibility(View.GONE);
//        evlnameset.setVisibility(View.GONE);
        evfnameset.setEnabled(false);
        evlnameset.setEnabled(false);
        evemailset.setEnabled(false);
        evnumberset.setEnabled(false);

        if(userModel!=null)
        {
            Glide.with(this)
                    .load(userModel.getProfileImageUrl().trim())
                    .crossFade()
                    .override(150, 150)
                    .placeholder(R.drawable.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new GlideDrawableImageViewTarget(profile_image) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            Log.d("Gliderrr", "readyy");

                            profile_image.setImageDrawable(drawable);

                        }
                    });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //picker for camer aor gsallery


                    if (mSwitchShowSecure.isChecked()) {

                        if (Build.VERSION.SDK_INT < 23) {

                        //We already have permission. Write your function call over hear
                        takepic();
                    } else {

                        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            // Here we are asking for permission

                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                        } else {

                            //If the app is running for second time, then we already have permission. You can write your function here, if we already have permission.

                            takepic();

                        }

                    }


                }



                }
            });

        loadBASIC();



        }
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitchShowSecure.isChecked()) {
                    Log.i("TOGGLE :", "ISCHECKED");

                    Snackbar.make(cordi, "Discard Changes ", Snackbar.LENGTH_LONG)
                            .setAction("DISCARD", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    try  {
                                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {

                                        Log.i("FRAG"," save or view----"+e.getMessage());


                                    }

                                    evfnameset.setEnabled(false);
                                    evlnameset.setEnabled(false);


//                                    tvfnameset.setVisibility(View.VISIBLE);
//                                    tvlnameset.setVisibility(View.VISIBLE);


                                    Intent intent = new Intent (ProfileActivity.this, ChangePaswordActivity.class);
                                    intent.putExtra("tocken",userModel.getAccessToken());
                                    startActivity(intent);
                                }
                            }).show();
                }
                else {

                Intent intent = new Intent (ProfileActivity.this, ChangePaswordActivity.class);
                intent.putExtra("tocken",userModel.getAccessToken());
                startActivity(intent);
            }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gotdata){  if (mSwitchShowSecure.isChecked()) {
                    Log.i("TOGGLE :", "ISCHECKED");

                    Snackbar.make(cordi, "Discard Changes ", Snackbar.LENGTH_LONG)
                            .setAction("DISCARD", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    try  {
                                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {

                                        Log.i("FRAG"," save or view----"+e.getMessage());


                                    }

                                    evfnameset.setEnabled(false);
                                    evlnameset.setEnabled(false);


//                                    tvfnameset.setVisibility(View.VISIBLE);
//                                    tvlnameset.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent (ProfileActivity.this, MoreActivity.class);
                                    intent.putExtra("userMore",userModel);
                                    startActivityForResult(intent,3);



                                }
                            }).show();
                }
                else {
                    Intent intent = new Intent (ProfileActivity.this, MoreActivity.class);
                    intent.putExtra("userMore",userModel);
                    startActivityForResult(intent,3);
                }}
                else {
                    snackbar = Snackbar.make(cordi, "Professional Details Not Loaded", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }




            }
        });

        evfnameset.clearFocus();
        evlnameset.clearFocus();
        evemailset.clearFocus();
        evnumberset.clearFocus();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {



                if(b){
                   Log.i("FRAG"," edit view  ----");

//                    evfnameset.setVisibility(View.VISIBLE);
//                    evlnameset.setVisibility(View.VISIBLE);
                    evfnameset.setEnabled(true);
                    evlnameset.setEnabled(true);

//                    tvfnameset.setVisibility(View.GONE);
//                    tvlnameset.setVisibility(View.GONE);

                } else {
                    //Your code when unchecked
                    Log.i("FRAG", " save or view----");


                    if(isvalid()){    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                        Log.i("FRAG", " save or view----" + e.getMessage());


                    }

//                    evfnameset.setVisibility(View.GONE);
//                    evlnameset.setVisibility(View.GONE);
                        evfnameset.setEnabled(false);
                        evlnameset.setEnabled(false);

//                    tvfnameset.setVisibility(View.VISIBLE);
//                    tvlnameset.setVisibility(View.VISIBLE);


                    saveBASIC();
                    loadBASIC();
                }
                else {
                        mSwitchShowSecure.setChecked(false);


                        snackbar = Snackbar.make(cordi, "Invalid Fields", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }

                }
            }
        });
        return true;
    }

    private void loadBASIC() {

        if(userModel.getUserFirstName()!=null)
        { tvfnameset.setText(userModel.getUserFirstName());
            evfnameset.setText(userModel.getUserFirstName());}
        if(userModel.getUserLastName()!=null)
        {tvlnameset.setText(userModel.getUserLastName());
            evlnameset.setText(userModel.getUserLastName());}
        if(userModel.getUserMobile()!=null)
            tvnumberset.setText(userModel.getUserMobile());
        evnumberset.setText(userModel.getUserMobile());
        if(userModel.getUserEmail()!=null)
            tvemailset.setText(userModel.getUserEmail());
        evemailset.setText(userModel.getUserEmail());
        if(userModel.getUserType().trim().equals("USR"))
        {
            usertype.setText("User");
            vieq1.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        }
        else usertype.setText("Service Provider");
    }

    private void saveBASIC() {




        userModel.setUserFirstName(evfnameset.getText().toString().trim());
        userModel.setUserLastName(evlnameset.getText().toString().trim());
//        String imguri  =getPath(selectedImage);

        File ProfileImage = null;
        Log.i("FILEURI","selected image :"+selectedImage);
        Log.i("SAVINGIGNG","selected image :"+selectedImage);

//        if(selectedImage!= null)
//        {
//
//
//
//            (Uri.parse(getPath(selectedImage)));
//            //  profile_image.setImageURI(Uri.parse(ProfileImage.toString()));
//
//        }

        networkManager.new UpdateprofiledataTask(ProfileActivity.this,userModel,profileIMG)
                .execute();
    }


    private void takepic() {


        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    PROFILE_PIC_COUNT = 1;


                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE,  System.currentTimeMillis()+"NewPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "FromyourCamera");
                    outputFileUri = null;
                    outputFileUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);


//
//                   File photo = new File(Environment.getExternalStorageDirectory(),  System.currentTimeMillis()+"soom_profile.jpg");
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photo));
//
//                    outputFileUri = Uri.fromFile(photo);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//                    startActivityForResult(intent, REQUEST_CAMERA);
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
    public void DetailsResponseSuccess(UserModel usersodel) {
        Log.i("GETPROFILE"," user1 :"+usersodel.getUserMobile());
            userModel = usersodel;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("GETPROFILE"," user2 :"+userModel.getUserMobile());
                gotdata =true;

                if(!userModel.getUserType().trim().equals("USR"))
                {
                    more.setVisibility(View.VISIBLE);
                }
            loadBASIC();

            }
        });
    }

    @Override
    public void DetailsResponseFailed(String message) {
        //snackbar
        snackbar = Snackbar.make(cordi, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void ProfileUpdateSuccess(String message, String profile) {
        snackbar = Snackbar.make(cordi,"Saved", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        userModel.setProfileImageUrl(profile.trim());


        SharedPreferencesManager.init(this);
        SharedPreferencesManager.write(USER_FIRST_NAME,userModel.getUserFirstName());
       SharedPreferencesManager.write(USER_LAST_NAME,userModel.getUserLastName());
        SharedPreferencesManager.write(USER_IMAGE_URL,userModel.getProfileImageUrl().trim());



    }

    @Override
    public void ProfileUpdateFailed(String message) {
        snackbar = Snackbar.make(cordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void failedtoConnect() {


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        Log.i("Capturing","onactivty result1");

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.i("Capturing","onactivty result2");
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

//                    profile_image.setImageURI(selectedImage);
                    Glide.with(ProfileActivity.this)
                            .load(selectedImage) // Uri of the picture
                            .into(profile_image);

                    Log.i("Capturing"," result "+selectedImage.toString());
                    if(selectedImage!=null)
                        profileIMG = new File((Uri.parse(getPath(selectedImage))).getPath());

                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    selectedImage= null;
                    selectedImage = imageReturnedIntent.getData();
//                    profile_image.setImageURI(selectedImage);
                    Glide.with(ProfileActivity.this)
                            .load(selectedImage) // Uri of the picture
                            .into(profile_image);

                   if(selectedImage!=null)
                       profileIMG = new File(String.valueOf(Uri.parse(getPath(selectedImage))));

                    Log.i("Capturing","result "+selectedImage.toString());

                }
                break;

            case 3:
                if(resultCode == RESULT_OK){
                    userModel=(UserModel) imageReturnedIntent.getSerializableExtra("userMore");
                    Log.i("RETURN"," suxces"+userModel.getUserGender());
                }
                break;


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

    public boolean isvalid() {

        return !evfnameset.getText().toString().trim().isEmpty()|| !evlnameset.getText().toString().trim().isEmpty() ;

    }

    @Override
    protected void onResume() {
        super.onResume();
        networkManager.new GetProviderDetailsTask(ProfileActivity.this,userModel.getAccessToken())
                .execute();
        loadBASIC();



    }
}

package com.bridge.soom.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationPVRActivity extends BaseActivity {
    private ImageButton fab;
    private CircleImageView profile_image;
    private EditText gender,dob,address,education,designation,experiance,hourlywages,languages,emptype;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Integer PROFILE_PIC_COUNT =0;

    private Uri outputFileUri;
    private File root;
    private String fname = "dsfg";
    private File sdImageMainDirectory ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pvr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         fab = (ImageButton) findViewById(R.id.fab);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        gender = (EditText) findViewById(R.id.gender);
        dob = (EditText) findViewById(R.id.dob);
        address = (EditText) findViewById(R.id.address);
        education = (EditText) findViewById(R.id.education);
        designation = (EditText) findViewById(R.id.designation);
        experiance = (EditText) findViewById(R.id.experiance);
        languages = (EditText) findViewById(R.id.languages);
        emptype = (EditText) findViewById(R.id.emptype);
        hourlywages = (EditText) findViewById(R.id.hourlywages);
//        // Determine Uri of camera image to save.
//        root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
//        root.mkdirs();
////       fname = Utils.getUniqueImageFilename();
//        sdImageMainDirectory = new File(root, fname);
//        outputFileUri = Uri.fromFile(sdImageMainDirectory);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent (RegistrationPVRActivity.this, RegistrationPVRDetailesActivity.class);
                startActivity(intent);

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


        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.i("Capturing","onactivty result");
        switch(requestCode) {

            case 1:
                if(resultCode == RESULT_OK){

                    //Uri selectedImage = imageReturnedIntent.getData();
                    Uri selectedImage  = outputFileUri;
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
                    Uri selectedImage = imageReturnedIntent.getData();
                    profile_image.setImageURI(selectedImage);
                    Log.i("Capturing","result "+selectedImage.toString());

                }
                break;


        }
    }

}

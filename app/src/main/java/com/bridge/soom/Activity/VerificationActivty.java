package com.bridge.soom.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.VerificationResponse;
import com.bridge.soom.R;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_TYPE;
import static com.bridge.soom.R.id.cordi;

public class VerificationActivty extends BaseActivity implements VerificationResponse{

        EditText verCode;
    ImageButton versubmit;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;
    private NetworkManager networkManager;
    private ProgressDialog progress;
     String usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        verCode = (EditText) findViewById(R.id.verCode);
        versubmit =(ImageButton)findViewById(R.id.versubmit);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String Email = getIntent().getStringExtra("Email");
       // final String AccessTocken = getIntent().getStringExtra("AccessTocken");
        final String Timezone = getIntent().getStringExtra("Timezone");
        networkManager = new NetworkManager(this);
        SharedPreferencesManager.init(this);

        final String AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");
         usertype = SharedPreferencesManager.read(USER_TYPE,"PVR");

        versubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Verification"," onclick");


                if(validateVercode())
                {
//                    versubmit.setEnabled(false);
                    Log.i("Verification"," valid");

                    String code = verCode.getText().toString();
                    showLoadingDialog();
                    networkManager.new RetrieveVerficationTask(VerificationActivty.this,code,Email,AccessTocken,Timezone)
                            .execute();
                    Log.i("Verification"," "+code+ " "+Email+" "+AccessTocken+" "+Timezone);

                }
            }
        });



    }

    private boolean validateVercode() {
        if(verCode.getText().toString().isEmpty())
        {  snackbar = Snackbar
                .make(cordi, R.string.vercode_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            Log.i("Verification"," false");

            return false;
        }
        Log.i("Verification"," true");

        return true;

    }

    @Override
    public void verResponseSuccess(String message) {
        dismissLoadingDialog();

        if(usertype.trim().equals("USR")){
        Intent intent = new Intent(VerificationActivty.this, HomeActivity.class);
        startActivity(intent);
            finish();
    }  else if(usertype.trim().equals("PVR")){
            Intent intent = new Intent(VerificationActivty.this, RegistrationPVRActivity.class);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void verResponseFailed(String message) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(cordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void failedtoConnect() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(cordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
//        snackbar = Snackbar
//                .make(cordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

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

    protected void onResume() {
        dismissLoadingDialog();
        super.onResume();
    }
}

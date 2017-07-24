package com.bridge.soom.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.ChangePassResponse;
import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.R;

public class ChangePaswordActivity extends BaseActivity  implements ChangePassResponse{
    private EditText confirmpass,newpass,oldpass;
    private Button fab;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;
    private NetworkManager networkManager;
    private String tocken;
    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pasword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkManager = new NetworkManager(this);
        fab = (Button) findViewById(R.id.fab);
        confirmpass = (EditText) findViewById(R.id.confirmpass);
        newpass = (EditText) findViewById(R.id.newpass);
        oldpass = (EditText) findViewById(R.id.oldpass);

        confirmpass.setTypeface(Typeface.DEFAULT);
        confirmpass.setTransformationMethod(new PasswordTransformationMethod());
        newpass.setTypeface(Typeface.DEFAULT);
        newpass.setTransformationMethod(new PasswordTransformationMethod());
        oldpass.setTypeface(Typeface.DEFAULT);
        oldpass.setTransformationMethod(new PasswordTransformationMethod());

        tocken = getIntent().getStringExtra("tocken");
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);









        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(allvalid()){     String old = oldpass.getText().toString();
                String newp = newpass.getText().toString();
                if (old.trim().isEmpty() || newp.trim().isEmpty() || confirmpass.toString().trim().isEmpty()) {
                    Snackbar.make(view, "Invalid Fields!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    oldpass.setText("");
                    newpass.setText("");
                    confirmpass.setText("");
                    networkManager.new SetNewPasswordTask(ChangePaswordActivity.this, tocken, old, newp, "")
                            .execute();

                }
            }






            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean allvalid() {

        if(oldpass.getText().toString().trim().isEmpty())
        {
            oldpass.setError("Invalid Field");

            //snackbar
            snackbar = Snackbar.make(cordi, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

        return false;
        }
        else if (newpass.getText().toString().trim().isEmpty())
        {
            newpass.setError("Invalid Field");


            //snackbar
            snackbar = Snackbar.make(cordi, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (newpass.getText().toString().trim().length()<8)
        {
            newpass.setError("Requires Minimus 8 character");


            //snackbar
            snackbar = Snackbar.make(cordi, "Requires Minimus 8 character", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if( confirmpass.getText().toString().trim().isEmpty())
        {
            //snackbar
            confirmpass.setError("Invalid Field");

            snackbar = Snackbar.make(cordi, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if(!newpass.getText().toString().equals(confirmpass.getText().toString()))
        {
            confirmpass.setError("Passwords does not match!");

            //snackbar
            snackbar = Snackbar.make(cordi, "Passwords does not match!", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

            return false;
        }
        return true;

    }

    @Override
    public void changePassResponseSuccess(String message) {

        //snackbar
        snackbar = Snackbar.make(cordi, "Password Changed Successfully!", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


    }

    @Override
    public void changePassResponseFailed(String message) {
        //snackbar
        snackbar = Snackbar.make(cordi, "Passwords Change Failed!", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();


    }

    @Override
    public void failedtoConnect() {
        snackbar = Snackbar.make(cordi, "Failed to Connect!", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

//    @Override
//    public void onBackPressed() {
//
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            //snackbar
//            snackbar = Snackbar.make(cordi,  "Please click BACK again to exit", Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce=false;
//                }
//            }, 2000);
//    }

}

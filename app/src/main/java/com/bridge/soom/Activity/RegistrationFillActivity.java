package com.bridge.soom.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.RegistrationResponse;
import com.bridge.soom.R;

import java.util.Locale;
import java.util.TimeZone;

import static com.bridge.soom.Helper.Constants.DEVICE_ID;

public class RegistrationFillActivity extends BaseActivity implements RegistrationResponse,TextWatcher {

    private static final String TAG = "RegistrationFill";
    private Integer select;
    private ImageButton regfillsubmit;
    private EditText fname,lname,code,mobnum,email,pass1,pass2;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private NetworkManager networkManager;
    private String LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo;
    private ProgressDialog progress;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_fill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        select = getIntent().getIntExtra("SELECT",0);
        regfillsubmit= (ImageButton) findViewById(R.id.regfillsubmit);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        networkManager = new NetworkManager(this);
        SharedPreferencesManager.init(getApplicationContext());

        fname= (EditText) findViewById(R.id.fname);
        lname= (EditText) findViewById(R.id.lname);
        code= (EditText) findViewById(R.id.code);
        mobnum= (EditText) findViewById(R.id.mobnum);
        email= (EditText) findViewById(R.id.email);
        pass1= (EditText) findViewById(R.id.pass1);
        pass2= (EditText) findViewById(R.id.pass2);
        checkBox= (CheckBox) findViewById(R.id.checkBox);



        code.setText("+91");

        fname.addTextChangedListener(this);
        lname.addTextChangedListener(this);
        code.addTextChangedListener(this);
        mobnum.addTextChangedListener(this);
        email.addTextChangedListener(this);
        pass1.addTextChangedListener(this);
        pass2.addTextChangedListener(this);



        regfillsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
               if(validateForm())
               {
//                   regfillsubmit.setEnabled(false);
                   LastName=lname.getText().toString();
                           FirstName = fname.getText().toString();
                   MobileNumber = mobnum.getText().toString();
                           EmailId = email.getText().toString();
                   Password = pass1.getText().toString();

                  if(select==0)
                  {UserType="USR";}
                   else if(select==1)
                  {
                      UserType="PVR";
                  }

                   DevideID =SharedPreferencesManager.read(DEVICE_ID,"");
                   Timexone= String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
                   cultureInfo = getCurrentLocale().getLanguage();
                    showLoadingDialog();
                   Log.i("Reg_submit"," "+FirstName+" "+LastName+" "+MobileNumber+" "+EmailId+" "+" "+Password+" "+DevideID+" "
                           +UserType+" "+Timexone+" "+cultureInfo);
                networkManager.new RetrieveRegistrationTask(RegistrationFillActivity.this,LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo)
                       .execute();

            }
            }
        });

    }

    private boolean validateForm() {

        if(fname.getText().toString().trim().isEmpty())
        {
//  snackbar = Snackbar
//                .make(cordi, R.string.name_empty , Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();

            fname.setError("Invalid Field - First Name");
            fname.requestFocus();

        }
        else  if(fname.getText().toString().trim().length()>25)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            fname.setError("Limit Exceeded - First Name");
            fname.requestFocus();


        }
        else  if(lname.getText().toString().trim().isEmpty())
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            lname.setError("Invalid Field - Last Name");
            lname.requestFocus();


        }
        else  if(lname.getText().toString().trim().length()>25)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            lname.setError("Limit Exceeded - Last Name");
            lname.requestFocus();


        }
        else  if(email.getText().toString().isEmpty())
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.emil_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            email.setError("Invalid Field - Email");
            email.requestFocus();

        }
        else  if(email.getText().toString().trim().length()>50)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.emil_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            email.setError("Limit Exceeded - Email");
            email.requestFocus();

        }
        else  if(!isEmail(email.getText().toString()))
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.email_invalid, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            email.setError("Invalid Field - Email");
            email.requestFocus();


        }
        else  if(code.getText().toString().isEmpty())
        {
            code.setText("+91");
//            snackbar = Snackbar
//                    .make(cordi, R.string.code_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            code.setError("Empty Field - Country Code");
            code.requestFocus();


        }
        else  if(code.getText().toString().length()>4) //too lengthy
        {
            code.setText("+91");
//            snackbar = Snackbar
//                    .make(cordi, R.string.code_long, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            code.setError("Country Code Exceeds Limit");
            code.requestFocus();


        }
        else  if(mobnum.getText().toString().isEmpty())
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.mobnum_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            mobnum.setError("Invalid Field - Mobile Number");
            mobnum.requestFocus();

        }
        else  if(mobnum.getText().toString().length()<6)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.mob_length, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            mobnum.setError("Mobile Number Should be atleast 6 numbers");
            mobnum.requestFocus();



        }

        else  if(mobnum.getText().toString().length()>13)
        {
            snackbar = Snackbar
                    .make(cordi, R.string.mob_lengthy, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            mobnum.setError("Mobile Number should not exceed the limit of 13");
            mobnum.requestFocus();



        }
        else  if(!isNumberValid(mobnum.getText().toString()))
        {
            snackbar = Snackbar
                    .make(cordi, R.string.invalidmobil, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            mobnum.setError("Invalid Mobile Number");

        }
        else  if(pass1.getText().toString().trim().isEmpty())
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.password_impty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            pass1.setError("Empty Field - Password");

        }
        else  if(pass1.getText().toString().trim().length()<4)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.passwordlenght, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            pass1.setError("Password should have atleast 4 characters");

        }
        else  if(pass1.getText().toString().trim().length()>99)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.passwordlenghy, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            pass1.setError("Password should not have more than 99 characters");

        }
        else  if(!pass1.getText().toString().trim().equals(pass2.getText().toString()))
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.mismatch, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            pass2.setText("");
            pass2.setError("Passwords Mismatch");


        }
        else  if(!checkBox.isChecked())
        {
            snackbar = Snackbar
                    .make(cordi,"Please Agree the Terms and Condition", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();



        }
        else
        {
            return true;
        }
        return false;
    }

    private boolean isNumberValid(String s) {

        return PhoneNumberUtils.isGlobalPhoneNumber(s);

    }


    private boolean isEmail(String s) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    @Override
    public void registrationResponseSuccess(String  message) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(cordi, R.string.reg_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
                Intent intent = new Intent (RegistrationFillActivity.this, VerificationActivty.class);
                intent.putExtra("Email",EmailId);
                intent.putExtra("AccessTocken",DevideID);
                intent.putExtra("Timezone",Timexone);
                startActivity(intent);
                finish();
//            }
//        }, SNACKBAR);




    }

    @Override
    public void registrationResponseFailed(String message) {
dismissLoadingDialog();
        snackbar = Snackbar
                .make(cordi, message, Snackbar.LENGTH_LONG);
//        snackbar = Snackbar
//                .make(cordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
//        regfillsubmit.setEnabled(true);


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

    @TargetApi(Build.VERSION_CODES.N)
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

    public boolean validateName( String Name )
    {
        Log.i("validateName"," "+Name.matches( "[a-zA-Z][a-zA-Z]*" ));
        return Name.matches( "[a-zA-Z][a-zA-Z]*" );
    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        fname.setError(null);
        lname.setError(null);
        mobnum.setError(null);
        code.setError(null);
        email.setError(null);
        pass2.setError(null);
        pass1.setError(null);


    }
}

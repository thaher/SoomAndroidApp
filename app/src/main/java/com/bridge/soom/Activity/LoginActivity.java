package com.bridge.soom.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.Interface.LoginResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Locale;
import java.util.TimeZone;

import static com.bridge.soom.Helper.Constants.ANIMTIME;
import static com.bridge.soom.Helper.Constants.DEVICE_ID;
import static com.bridge.soom.Helper.Constants.IS_LOGGEDIN;
import static com.bridge.soom.Helper.Constants.IS_REMEMBRED;
import static com.bridge.soom.Helper.Constants.REM_EMAIL;
import static com.bridge.soom.Helper.Constants.REM_PASS;

public class LoginActivity extends BaseActivity implements ForgotResponse,LoginResponse {
    private CircularProgressBar circularProgressBar;
    private RelativeLayout loginrel,forgotrel;
    private EditText code,number,password,uremail;
    private ImageButton login,forgotsubmit;
    private Button forgotPassword,createAccount,guestUser,backlogin;
    private Integer viewCode;

    private Animation animationFadeIn ;
    private Animation animationFadeOut ;
    private NetworkManager networkManager;
    private CoordinatorLayout cordi;
    private Snackbar snackbar;
    private ProgressDialog progress;
    private CheckBox checkBox;
    private Boolean isremembred =false;
    private Boolean isloggedin = false;
    private String num ;
    private String pass;
    private Boolean doubleBackToExitPressedOnce=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.circularProgressBar);
        loginrel = (RelativeLayout) findViewById(R.id.loginrel);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        forgotrel = (RelativeLayout) findViewById(R.id.forgotrel);
        code = (EditText) findViewById(R.id.code);
        number = (EditText) findViewById(R.id.number);
        password = (EditText) findViewById(R.id.password);
        uremail = (EditText) findViewById(R.id.uremail);
        login = (ImageButton) findViewById(R.id.login);
        forgotsubmit = (ImageButton) findViewById(R.id.forgotsubmit);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);
        createAccount = (Button) findViewById(R.id.createAccount);
        guestUser = (Button) findViewById(R.id.guestUser);
        backlogin = (Button) findViewById(R.id.backlogin);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        networkManager = new NetworkManager(this);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        setViewCodefirst();
        SharedPreferencesManager.init(getApplicationContext());
         isremembred = SharedPreferencesManager.readBool(IS_REMEMBRED,false);
        isloggedin = SharedPreferencesManager.readBool(IS_LOGGEDIN,false);


        if(isloggedin)
        {
            Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        checkBox.setChecked(isremembred);
        if(isremembred)
        {
            number.setText(SharedPreferencesManager.read(REM_EMAIL,""));
            password.setText(SharedPreferencesManager.read(REM_PASS,""));

        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(code.getText().toString().isEmpty())
//                {
//                    code.setText("+91");
//                    snackbar = Snackbar
//                            .make(cordi, R.string.code_empty, Snackbar.LENGTH_LONG);
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//                    snackbar.show();
//                }
//                else if(code.getText().toString().length()>4)
//                {
//                    code.setText("+91");
//                    snackbar = Snackbar
//                            .make(cordi, R.string.code_long, Snackbar.LENGTH_LONG);
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//                    snackbar.show();
//
//                }
//                else if(number.getText().toString().isEmpty())
//                {
//                    snackbar = Snackbar
//                            .make(cordi, R.string.mobnum_empty, Snackbar.LENGTH_LONG);
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//                    snackbar.show();
//                }
                 if(!isValid(number.getText().toString()))
                {
//                    snackbar = Snackbar
//                            .make(cordi, R.string.email_invalid, Snackbar.LENGTH_LONG);
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//                    snackbar.show();
                    number.setError("Please check Email");

                }
                else if(password.getText().toString().isEmpty())
                {
//                    snackbar = Snackbar
//                            .make(cordi, R.string.password_impty, Snackbar.LENGTH_LONG);
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//                    snackbar.show();
                    password.setError("Password Empty");

                }

                else {
//                    String cod = code.getText().toString();
                    String cod = SharedPreferencesManager.read(DEVICE_ID,"");

                     num = number.getText().toString();
                     pass= password.getText().toString();
                      isremembred = checkBox.isChecked();
                     SharedPreferencesManager.writeBool(IS_REMEMBRED,isremembred);



                     showLoadingDialog();
                    networkManager.new AttemptLoginTask(LoginActivity.this, cod, num, pass)
                            .execute();

                     try  {
                         InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                         imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                     } catch (Exception e) {

                     }
                }


            }
        });
        forgotsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uremai = uremail.getText().toString();
                if(isValid(uremai))
                {
                    String Timexone = String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
                    String cultureInfo = getCurrentLocale().getLanguage();

                    showLoadingDialog();
                    networkManager.new RetrieveForgotTask(LoginActivity.this, uremai, Timexone, cultureInfo)
                            .execute();

                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }

                }
            //    forgotsubmit.setEnabled(false);

            }
        });
        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewCode();

                circularProgressBar.setProgressWithAnimation(0);

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewCode();
                circularProgressBar.setProgressWithAnimation(100);



            }
        });
        guestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
                intent.putExtra("GUEST",true);
                startActivity(intent);
                finish();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void loginResponseSuccess(String message, final UserModel userModel) {
        Log.i("Attempt_login"," -----parser success");
        dismissLoadingDialog();
       if(isremembred)
       { SharedPreferencesManager.write(REM_EMAIL,num);
        SharedPreferencesManager.write(REM_PASS,pass);}
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                circularProgressBar.setProgressWithAnimation(100);

                if(userModel.getUserStatusLevel()==1)
                {

                    //go to verification
                    Intent intent = new Intent (LoginActivity.this, VerificationActivty.class);

                    intent.putExtra("Email",userModel.getUserEmail());
                    intent.putExtra("AccessTocken",userModel.getAccessToken());
                    intent.putExtra("Timezone",userModel.getTimeZone());
                    startActivity(intent);
                }
                else if(userModel.getUserStatusLevel()==2) // need to add one more condition pvr or usr
                {
                    // go to second reg
                    Intent intent = new Intent (LoginActivity.this, ProfessionalDetailsActivity.class);
//                    Intent intent = new Intent (LoginActivity.this, RegistrationPVRActivity.class);
                    startActivity(intent);


                }
                else if(userModel.getUserStatusLevel()==3) {
//                    SharedPreferencesManager.writeBool(IS_LOGGEDIN,true);
                    //logged in , go to home
                    Intent intent = new Intent (LoginActivity.this, PersonalDetailsActivity.class);
                    startActivity(intent);

                }  else if(userModel.getUserStatusLevel()==4) {
                    SharedPreferencesManager.writeBool(IS_LOGGEDIN,true);
                    //logged in , go to home
                    Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };
        mainHandler.post(myRunnable);




    }

    @Override
    public void loginResponseFailed(String message) {
        Log.i("Attempt_login","------------ parser failed");
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(cordi, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void forgotResponseSuccess(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  setViewCode();
                Log.i("Forgot_submit", " intrface called in login usscess");
                dismissLoadingDialog();

                snackbar = Snackbar.make(cordi, R.string.for_success, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                setViewCode();
                circularProgressBar.setProgressWithAnimation(0);
                snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                snackbar.show();
                // forgotsubmit.setEnabled(true);
                // TODO  clear all fields

            }});
    }

    @Override
    public void forgotResponseFailed(String message) {
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(cordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
      //  forgotsubmit.setEnabled(true);

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


    private boolean isValid(String uremai) {
//        if(uremai.isEmpty())
//        {
////            snackbar = Snackbar
////                    .make(cordi, R.string.emil_empty, Snackbar.LENGTH_LONG);
////            View snackBarView = snackbar.getView();
////            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
////            snackbar.show();
//            number.setError("Empty Field - Email");
//            uremail.setError("Empty Field - Email");
//
//            return false;
//        }
//        else
            if(!isEmail(uremai))
        {
//            snackbar = Snackbar
//                .make(cordi, R.string.email_invalid, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            number.setError("Invalid Email");
            uremail.setError("Empty Field - Email");

            return false;

        }
        return true;
    }

    private void setViewCodefirst() {
        viewCode=0;
        loginrel.setVisibility(View.VISIBLE);
        createAccount.setVisibility(View.VISIBLE);
        guestUser.setVisibility(View.VISIBLE);
        forgotrel.setVisibility(View.GONE);
        backlogin.setEnabled(false);
        forgotPassword.setEnabled(true);


    }

    private void setViewCode() {


        if(viewCode==0)
      {viewCode=1;
          forgotPassword.setEnabled(false);
          createAccount.startAnimation(animationFadeOut);
          guestUser.startAnimation(animationFadeOut);
            loginrel.startAnimation(animationFadeOut);
          forgotrel.setVisibility(View.VISIBLE);
          forgotrel.startAnimation(animationFadeIn);

          Handler mHandler = new Handler();
          mHandler.postDelayed(new Runnable() {

              @Override
              public void run() {

                  loginrel.setVisibility(View.GONE);
                  createAccount.setVisibility(View.INVISIBLE);
                  guestUser.setVisibility(View.INVISIBLE);

                  backlogin.setEnabled(true);
              }

          }, ANIMTIME);

        }
        else if(viewCode==1)
        {viewCode=0;
            backlogin.setEnabled(false);
            loginrel.startAnimation(animationFadeIn);
            createAccount.startAnimation(animationFadeIn);
            guestUser.startAnimation(animationFadeIn);
            forgotrel.startAnimation(animationFadeOut);

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    loginrel.setVisibility(View.VISIBLE);
            createAccount.setVisibility(View.VISIBLE);
            guestUser.setVisibility(View.VISIBLE);
            forgotrel.setVisibility(View.GONE);

                    forgotPassword.setEnabled(true);

                }

            }, ANIMTIME);


        }

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
    private boolean isEmail(String s) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
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
        // clear all edit text fields here
        super.onResume();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    uremail.clearFocus();
                    uremail.setError(null);

                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //snackbar
        snackbar = Snackbar.make(cordi,  "Please click BACK again to exit", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

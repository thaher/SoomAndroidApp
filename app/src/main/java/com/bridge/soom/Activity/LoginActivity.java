package com.bridge.soom.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
        networkManager = new NetworkManager(this);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        setViewCodefirst();
        SharedPreferencesManager.init(getApplicationContext());


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
                    snackbar = Snackbar
                            .make(cordi, R.string.mob_lengthy, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }
                else if(password.getText().toString().isEmpty())
                {
                    snackbar = Snackbar
                            .make(cordi, R.string.password_impty, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }

                else {
//                    String cod = code.getText().toString();
                    String cod = SharedPreferencesManager.read(DEVICE_ID,"");

                    String num = number.getText().toString();
                    String pass= password.getText().toString();

                     showLoadingDialog();
                    networkManager.new AttemptLoginTask(LoginActivity.this, cod, num, pass)
                            .execute();
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
                    setViewCode();

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
    public void loginResponseSuccess(String message, UserModel userModel) {
        Log.i("Attempt_login"," -----parser success");
        dismissLoadingDialog();

        if(userModel.getUserEmailVerified())
        {
            //logged in , go to home
            Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            //go to verification
            Intent intent = new Intent (LoginActivity.this, VerificationActivty.class);

            intent.putExtra("Email",userModel.getUserEmail());
            intent.putExtra("AccessTocken",userModel.getAccessToken());
            intent.putExtra("Timezone",userModel.getTimeZone());
            startActivity(intent);

        }

    }

    @Override
    public void loginResponseFailed(String message) {
        Log.i("Attempt_login","---------- parser failed");
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(cordi, R.string.log_failed, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void forgotResponseSuccess(String message) {
        //  setViewCode();
        Log.i("Forgot_submit"," intrface called in login usscess");
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(cordi, R.string.for_success, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
       // forgotsubmit.setEnabled(true);

    }

    @Override
    public void forgotResponseFailed(String message) {
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(cordi, R.string.for_failed, Snackbar.LENGTH_LONG);
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
        if(uremai.isEmpty())
        {
            snackbar = Snackbar
                    .make(cordi, R.string.emil_empty, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if(!isEmail(uremai))
        {snackbar = Snackbar
                .make(cordi, R.string.email_invalid, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
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
        super.onResume();
    }

}

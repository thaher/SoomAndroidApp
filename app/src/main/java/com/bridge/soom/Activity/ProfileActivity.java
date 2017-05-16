package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements ProviderDetailsResponse {

    private ToggleButton mSwitchShowSecure;
    private UserModel userModel;
    private TextView tvfnameset,tvlnameset,tvnumberset,tvemailset,usertype;
    private EditText evfnameset,evlnameset,evnumberset,evemailset;
    private CircleImageView profile_image;
    private RelativeLayout changepass, more;
    private NetworkManager networkManager;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;

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
//        evnumberset = (EditText) findViewById(R.id.evnumberset);
//        evemailset = (EditText) findViewById(R.id.evemailset);

        usertype = (TextView) findViewById(R.id.usertype);
        changepass = (RelativeLayout) findViewById(R.id.changepass);
        more = (RelativeLayout) findViewById(R.id.more);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);

        evfnameset.setVisibility(View.GONE);
        evlnameset.setVisibility(View.GONE);

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

            if(userModel.getUserFirstName()!=null)
            { tvfnameset.setText(userModel.getUserFirstName());
                evfnameset.setText(userModel.getUserFirstName());}
            if(userModel.getUserLastName()!=null)
            {tvlnameset.setText(userModel.getUserLastName());
                evlnameset.setText(userModel.getUserLastName());}
            if(userModel.getUserMobile()!=null)
                tvnumberset.setText(userModel.getUserMobile());
            if(userModel.getUserEmail()!=null)
                tvemailset.setText(userModel.getUserEmail());
            if(userModel.getUserType().trim().equals("USR"))
            {
                usertype.setText("User");
            }
            else usertype.setText("Service Provider");

            networkManager.new GetProviderDetailsTask(ProfileActivity.this,userModel.getAccessToken())
                    .execute();

        }
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ProfileActivity.this, ChangePaswordActivity.class);
                intent.putExtra("tocken",userModel.getAccessToken());
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Saveandmove();
                Intent intent = new Intent (ProfileActivity.this, MoreActivity.class);
                intent.putExtra("userMore",userModel);
                startActivity(intent);
            }
        });

    }

    private void Saveandmove() {
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

                    evfnameset.setVisibility(View.VISIBLE);
                    evlnameset.setVisibility(View.VISIBLE);

                    tvfnameset.setVisibility(View.GONE);
                    tvlnameset.setVisibility(View.GONE);

                } else {
                    //Your code when unchecked
                    Log.i("FRAG"," save or view----");
                    evfnameset.setVisibility(View.GONE);
                    evlnameset.setVisibility(View.GONE);

                    tvfnameset.setVisibility(View.VISIBLE);
                    tvlnameset.setVisibility(View.VISIBLE);

                }
            }
        });
        return true;
    }



    @Override
    public void DetailsResponseSuccess(UserModel usersodel) {
        Log.i("GETPROFILE"," user1 :"+usersodel.getUserMobile());
            userModel = usersodel;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("GETPROFILE"," user2 :"+userModel.getUserMobile());


                if(userModel.getUserFirstName()!=null)
                {   tvfnameset.setText(userModel.getUserFirstName());
                    evfnameset.setText(userModel.getUserFirstName());}
                if(userModel.getUserLastName()!=null)
                {  tvlnameset.setText(userModel.getUserLastName());
                    evlnameset.setText(userModel.getUserLastName());}
                if(userModel.getUserMobile()!=null)
                    tvnumberset.setText(userModel.getUserMobile());
                if(userModel.getUserEmail()!=null)
                    tvemailset.setText(userModel.getUserEmail());
                if(userModel.getUserType().trim().equals("USR"))
                {
                    usertype.setText("User");
                }
                else usertype.setText("Service Provider");

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
    public void failedtoConnect() {

        snackbar = Snackbar.make(cordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }
}

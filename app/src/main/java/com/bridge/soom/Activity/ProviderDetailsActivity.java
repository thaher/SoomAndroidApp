package com.bridge.soom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderDetailsActivity extends BaseActivity implements ProviderDetailsResponse {
    private ProviderBasic providerBasic;
    private TextView category, rate, tvpreflocset, tvgenderset, tvexperset, tvdobset, tvaddressset, tveduset, ratingtxt, tvlanguageset;
    private RatingBar rating;
    private CircleImageView profile_image;
    private ImageButton call, message;
    private Button sendinvite;
    private NetworkManager networkManager;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;
    private ExpandableLayout expandableLayout1;
    private ToggleButton toggleprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        providerBasic = (ProviderBasic) getIntent().getSerializableExtra("PROVIDER");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(providerBasic.getUserFirstName() + " " + providerBasic.getUserLastName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        networkManager = new NetworkManager(this);
        category = (TextView) findViewById(R.id.category);
        rate = (TextView) findViewById(R.id.rate);
        tvpreflocset = (TextView) findViewById(R.id.tvpreflocset);
        tvgenderset = (TextView) findViewById(R.id.tvgenderset);
        tvexperset = (TextView) findViewById(R.id.tvexperset);
        tvdobset = (TextView) findViewById(R.id.tvdobset);
        tvaddressset = (TextView) findViewById(R.id.tvaddressset);
        tveduset = (TextView) findViewById(R.id.tveduset);
        ratingtxt = (TextView) findViewById(R.id.ratingtxt);
        tvlanguageset = (TextView) findViewById(R.id.tvlanguageset);


        rating = (RatingBar) findViewById(R.id.rating);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        call = (ImageButton) findViewById(R.id.call);
        message = (ImageButton) findViewById(R.id.message);
        sendinvite = (Button) findViewById(R.id.sendInvite);
        cordi = (CoordinatorLayout) findViewById(R.id.cordi);

//        Glide.with(this).load(providerBasic.getProfileImageUrl().trim())
//                .thumbnail(0.5f)
//                .crossFade()
//                .override(150, 150)
//                .placeholder(R.drawable.avatar)
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .into(profile_image);
        Glide.with(this)
                .load(providerBasic.getProfileImageUrl().trim())
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


        category.setText(providerBasic.getCategoryName().trim());
        rate.setText(providerBasic.getUserWagesHour());
        tvpreflocset.setText(providerBasic.getCurrentLocation());
        tvgenderset.setText(providerBasic.getUserGender());
        tvexperset.setText("Loading...");
        tvdobset.setText("Loading...");
        tvaddressset.setText(providerBasic.getUserAddress());
        tveduset.setText("Loading...");
        tvlanguageset.setText("Loading");
        ratingtxt.setText("Loading...");
        rating.setRating(3);
        rating.setIsIndicator(true);
//        rating.setVisibility(View.GONE);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {

                    //We already have permission. Write your function call over hear
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+providerBasic.getUserMobile()));//change the number
                    startActivity(callIntent);                            } else {

                    if (ContextCompat.checkSelfPermission(ProviderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        // Here we are asking for permission

                        ActivityCompat.requestPermissions(ProviderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);


                    } else {

                        //If the app is running for second time, then we already have permission. You can write your function here, if we already have permission.

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+providerBasic.getUserMobile()));//change the number
                        startActivity(callIntent);
                    }

                }



            }

        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + providerBasic.getUserMobile());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "The SMS text");
                startActivity(it);
            }
        });

        networkManager.new GetProviderDetailsTask(ProviderDetailsActivity.this, providerBasic.getAccessTocken())
                .execute();


        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandable_layout);
        toggleprofile = (ToggleButton) findViewById(R.id.toggleprofile);
        toggleprofile.setChecked(true);
        expandableLayout1.expand();

        toggleprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleprofile.isChecked()) {
                    expandableLayout1.expand();
                } else {
                    expandableLayout1.collapse();
                }
            }
        });


    }

    private void callPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + providerBasic.getUserMobile()));//change the number
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ProviderDetailsActivity.this.startActivity(callIntent);

            return;
        }
    }


    @Override
    public void DetailsResponseSuccess(final UserModel userModel) {
        //snackbar


        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                tvexperset.setText(userModel.getUserExperience());
                tvdobset.setText(userModel.getDob());
                tveduset.setText(userModel.getUserEducation());
                tvlanguageset.setText("N/A");
                ratingtxt.setText("N/A");
                rating.setRating(0);

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
        //snackbar
        snackbar = Snackbar.make(cordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }
}

package com.bridge.soom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderDetailsActivity extends BaseActivity  implements ProviderDetailsResponse {
    private ProviderBasic providerBasic;
    private TextView category, rate;
    private RatingBar rating;
    private ImageView profile_image;
    private ImageButton call, message;
    private Button sendinvite;
    private NetworkManager networkManager;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;

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
        rating = (RatingBar) findViewById(R.id.rating);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        call = (ImageButton) findViewById(R.id.call);
        message = (ImageButton) findViewById(R.id.message);
        sendinvite = (Button) findViewById(R.id.sendInvite);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);

        Glide.with(this).load(providerBasic.getProfileImageUrl().trim())
                .thumbnail(0.5f)
                .crossFade()
                .override(150, 150)
                .placeholder(R.drawable.avatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profile_image);


        category.setText(providerBasic.getCategoryName());
        rate.setText(providerBasic.getUserWagesHour());
        rating.setIsIndicator(true);
        rating.setVisibility(View.GONE);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + providerBasic.getUserMobile()));//change the number
                if (ActivityCompat.checkSelfPermission(ProviderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                ProviderDetailsActivity.this.startActivity(callIntent);

            }

        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+providerBasic.getUserMobile());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "The SMS text");
                startActivity(it);
            }
        });

        networkManager.new GetProviderDetailsTask(ProviderDetailsActivity.this,providerBasic)
                .execute();


    }

    @Override
    public void DetailsResponseSuccess(UserModel userModel) {
        //snackbar

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

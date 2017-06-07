package com.bridge.soom.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Fragment.HomeFragment;
import com.bridge.soom.Fragment.ProfileFragment;
import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.IS_LOGGEDIN;
import static com.bridge.soom.Helper.Constants.USER_EMAIL;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;
import static com.bridge.soom.Helper.Constants.USER_STATUS_LEVEL;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

public class MainHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener {
    private NavigationView navigationView;
    private View hView;
    private Menu menu;
    private NetworkManager networkManager;
    private CoordinatorLayout cordi;
    private CircleImageView profile_image;
    private TextView profile_name;
    private Snackbar snackbar;
    private ProgressDialog progress;
    boolean doubleBackToExitPressedOnce = false;
    private UserModel user;
    private Boolean isGuest = false;
    private  String tocken="";
    private ToggleButton mSwitchShowSecure;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);

        SharedPreferencesManager.init(getApplicationContext());
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView =  navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        networkManager = new NetworkManager(this);
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        profile_image = (CircleImageView)hView.findViewById(R.id.profile_image);
        profile_name = (TextView)hView.findViewById(R.id.profile_name);
        isGuest = getIntent().getBooleanExtra("GUEST",false);

        if(!isGuest) {
            setuser();

        }


    }



    private void setuser() {
        user = new UserModel();
        final String AccessTocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");
        Log.i("ACCESS_TOVCKEN"," "+AccessTocken);
        user.setAccessToken(AccessTocken);
        tocken =AccessTocken;
//            user.setUserId(Integer.parseInt(SharedPreferencesManager.read(USER_ID,"0")));
        user.setUserEmail( SharedPreferencesManager.read(USER_EMAIL,""));
        user.setUserType( SharedPreferencesManager.read(USER_TYPE,"USR"));
        user.setUserFirstName( SharedPreferencesManager.read(USER_FIRST_NAME,""));
        user.setUserLastName( SharedPreferencesManager.read(USER_LAST_NAME,""));
        user.setUserStatusLevel(Integer.parseInt(SharedPreferencesManager.read(USER_STATUS_LEVEL,"0")));
        user.setProfileImageUrl( SharedPreferencesManager.read(USER_IMAGE_URL,""));


//            Glide.with(this).load(user.getProfileImageUrl().trim())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .override(90,90)
//                    .placeholder(R.drawable.avatar)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .into(profile_image);


        Glide.with(this)
                .load(user.getProfileImageUrl().trim())
                .placeholder(R.drawable.avatar)
                .into(new GlideDrawableImageViewTarget(profile_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        Log.d("Gliderrr", "readyy");

                        profile_image.setImageDrawable(drawable);

                    }
                });

        Log.i("Gliderrr"," : "+user.getProfileImageUrl().trim()+": ");


        profile_name.setText(user.getUserFirstName()+" "+user.getUserLastName());

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_login);

        // set new title to the MenuItem
        nav_camara.setTitle("Logout");
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            }, 2000);        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                HomeInterface homeInterface
//                homeInterface.ViewToggle(!isChecked);
//
                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onMapView(!isChecked);
                }

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        if (itemId == R.id.nav_home) {
            // Handle the camera action
if(mSwitchShowSecure!=null)
    mSwitchShowSecure.setVisibility(View.VISIBLE);

            Bundle bundle = new Bundle();
            bundle.putBoolean("GUEST",isGuest);
            fragment = new HomeFragment();
            Log.i("FRAGDATA"," guest "+isGuest);
            fragment.setArguments(bundle);
        } else if (itemId == R.id.nav_invitation) {
            if(!isGuest){
                if(mSwitchShowSecure!=null)
                    mSwitchShowSecure.setVisibility(View.INVISIBLE);
            }
            else {
                //snackbar

                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (MainHomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainHomeActivity.this.finish();
                            }
                        }).show();
            }

        } else if (itemId == R.id.nav_profile) {
            if(!isGuest){
                if(mSwitchShowSecure!=null)
                    mSwitchShowSecure.setVisibility(View.INVISIBLE);
                fragment = new ProfileFragment();

            }
            else {
                //snackbar

                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (MainHomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainHomeActivity.this.finish();
                            }
                        }).show();
            }



        } else if (itemId == R.id.nav_rating) {


            if(!isGuest){
                if(mSwitchShowSecure!=null)
                    mSwitchShowSecure.setVisibility(View.INVISIBLE);
            }
            else {
                //snackbar

                Snackbar.make(cordi, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                        .setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (MainHomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainHomeActivity.this.finish();
                            }
                        }).show();
            }


        } else if (itemId == R.id.nav_howitwrks) {
            if(mSwitchShowSecure!=null)
                mSwitchShowSecure.setVisibility(View.INVISIBLE);


        } else if (itemId == R.id.nav_login) {
            SharedPreferencesManager.writeBool(IS_LOGGEDIN,false);
            Intent intent = new Intent (MainHomeActivity.this, LoginActivity.class);
            SharedPreferencesManager.init(this);
            SharedPreferencesManager.write(ACCESS_TOCKEN,"");
            SharedPreferencesManager.write(USER_EMAIL,"");
            SharedPreferencesManager.write(USER_TYPE,"");
            SharedPreferencesManager.write(USER_FIRST_NAME,"");
            SharedPreferencesManager.write(USER_LAST_NAME,"");
            SharedPreferencesManager.write(USER_STATUS_LEVEL,"");
            SharedPreferencesManager.write(USER_IMAGE_URL,"");

            startActivity(intent);
            finish();

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

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


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener{
        void onMapView(Boolean isMapview);
    }

}

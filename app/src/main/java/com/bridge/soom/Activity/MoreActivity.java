package com.bridge.soom.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;

public class MoreActivity extends BaseActivity {
        private UserModel userModel;
    private ToggleButton mSwitchShowSecure;
    private TextView tvgenderset,tvdobset,tvaddressset,tveduset,tvdesigset,tvexperset,tvwagesset,tvskillset,
            tvlanguageset,tvemptypeset,tvserviceset,tvlocset,tvloc2set,tvloc3set,tvcountryset,tvstateset,tvcityset;
    private EditText evgenderset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userModel = (UserModel) getIntent().getSerializableExtra("userMore");
        tvgenderset = (TextView) findViewById(R.id.tvgenderset);

        tvdobset = (TextView) findViewById(R.id.tvdobset);
        tvaddressset = (TextView) findViewById(R.id.tvaddressset);
        tveduset = (TextView) findViewById(R.id.tveduset);
        tvdesigset = (TextView) findViewById(R.id.tvdesigset);
        tvexperset = (TextView) findViewById(R.id.tvexperset);
        tvwagesset = (TextView) findViewById(R.id.tvwagesset);
        tvskillset = (TextView) findViewById(R.id.tvskillset);
        tvlanguageset = (TextView) findViewById(R.id.tvlanguageset);
        tvemptypeset = (TextView) findViewById(R.id.tvemptypeset);
        tvserviceset = (TextView) findViewById(R.id.tvserviceset);
        tvlocset = (TextView) findViewById(R.id.tvlocset);
        tvloc2set = (TextView) findViewById(R.id.tvloc2set);
        tvloc3set = (TextView) findViewById(R.id.tvloc3set);
        tvcountryset = (TextView) findViewById(R.id.tvcountryset);
        tvstateset = (TextView) findViewById(R.id.tvstateset);
        tvcityset = (TextView) findViewById(R.id.tvcityset);





        evgenderset = (EditText) findViewById(R.id.evgenderset);


        if(userModel!=null)
        {
           if(userModel.getUserGender()!=null&&!userModel.getUserGender().isEmpty())
           {
               tvgenderset.setText(userModel.getUserGender().trim());
           }
           else tvgenderset.setText("Not Set");


            if(userModel.getDob()!=null&&!userModel.getDob().isEmpty())
            {
                tvdobset.setText(userModel.getDob().trim());
            }
            else tvdobset.setText("Not Set");


            if(userModel.getUserAddress()!=null&&!userModel.getUserAddress().isEmpty())
            {
                tvaddressset.setText(userModel.getUserAddress().trim());
            }
            else tvaddressset.setText("Not Set");


            if(userModel.getUserEducation()!=null&&!userModel.getUserEducation().isEmpty())
            {
                tveduset.setText(userModel.getUserEducation().trim());
            }
            else tveduset.setText("Not Set");


            if(userModel.getUserDesignation()!=null&&!userModel.getUserDesignation().isEmpty())
            {
                tvdesigset.setText(userModel.getUserDesignation().trim());
            }
            else tvdesigset.setText("Not Set");


            if(userModel.getUserExperience()!=null&&!userModel.getUserExperience().isEmpty())
            {
                tvexperset.setText(userModel.getUserExperience().trim());
            }
            else tvexperset.setText("Not Set");


            if(userModel.getUserWagesHour()!=null&&!userModel.getUserWagesHour().toString().isEmpty())
            {
                tvwagesset.setText(userModel.getUserWagesHour().toString().trim());
            }
            else tvwagesset.setText("Not Set");



            if(userModel.getUserAdditionalSkill()!=null&&!userModel.getUserAdditionalSkill().isEmpty())
            {
                tvskillset.setText(userModel.getUserAdditionalSkill().trim());
            }
            else tvskillset.setText("Not Set");


            if(userModel.getLanguagesknown()!=null&&!userModel.getLanguagesknown().isEmpty())
            {
                tvgenderset.setText(userModel.getLanguagesknown().trim());
            }
            else tvlanguageset.setText("Not Set");


//            if(userModel.get()!=null&&!userModel.getUserGender().isEmpty())
//            {
//                tvgenderset.setText(userModel.getUserGender().trim());
//            }
//            else
                tvemptypeset.setText("NO data from server");


            if(userModel.getCategoryName()!=null&&!userModel.getCategoryName().isEmpty())
            {
                tvserviceset.setText(userModel.getCategoryName().trim());
            }
            else tvserviceset.setText("Not Set");


            if(userModel.getPreLocation1()!=null&&!userModel.getPreLocation1().isEmpty())
            {
                tvlocset.setText(userModel.getPreLocation1().trim());
            }
            else tvlocset.setText("Not Set");

            if(userModel.getPreLocation2()!=null&&!userModel.getPreLocation2().isEmpty())
            {
                tvloc2set.setText(userModel.getPreLocation2().trim());
            }
            else tvloc2set.setText("Not Set");

            if(userModel.getPreLocation3()!=null&&!userModel.getPreLocation3().isEmpty())
            {
                tvloc3set.setText(userModel.getPreLocation3().trim());
            }
            else tvloc3set.setText("Not Set");

            if(userModel.getCountryName()!=null&&!userModel.getCountryName().isEmpty())
            {
                tvcountryset.setText(userModel.getCountryName().trim());
            }
            else tvcountryset.setText("Not Set");

            if(userModel.getStateName()!=null&&!userModel.getStateName().isEmpty())
            {
                tvstateset.setText(userModel.getStateName().trim());
            }
            else tvstateset.setText("Not Set");


            if(userModel.getCityName()!=null&&!userModel.getCityName().isEmpty())
            {
                tvcityset.setText(userModel.getCityName().trim());
            }
            else
                tvcityset.setText("Not Set");
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_more, menu);
        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Log.i("FRAG"," edit view  ----");

                    evgenderset.setVisibility(View.VISIBLE);

                    tvgenderset.setVisibility(View.GONE);

                } else {
                    //Your code when unchecked
                    Log.i("FRAG"," save or view----");
                    evgenderset.setVisibility(View.GONE);

                    tvgenderset.setVisibility(View.VISIBLE);

                }
            }
        });
        return true;
    }

}

package com.bridge.soom.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MoreActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {
        private UserModel userModel;
    private ToggleButton mSwitchShowSecure;
    private TextView tvgenderset,tvdobset,tvaddressset,tveduset,tvdesigset,tvexperset,tvwagesset,tvskillset,
            tvlanguageset,tvemptypeset,tvserviceset,tvlocset,tvloc2set,tvloc3set,tvcountryset,tvstateset,tvcityset;
    private EditText  evaddressset,eveduset,evdesigset,evexperset,evwagesset,evskillset,
    evlanguageset,evemptypeset;
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    List<String> categories;

    CalendarDatePickerDialogFragment cdp;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;

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





        evaddressset = (EditText) findViewById(R.id.evaddressset);
        eveduset = (EditText) findViewById(R.id.eveduset);
        evdesigset = (EditText) findViewById(R.id.evdesigset);
        evexperset = (EditText) findViewById(R.id.evexperset);
        evwagesset = (EditText) findViewById(R.id.evwagesset);
        evskillset = (EditText) findViewById(R.id.evskillset);
        evlanguageset = (EditText) findViewById(R.id.evlanguageset);
        evemptypeset = (EditText) findViewById(R.id.evemptypeset);

        cordi = (CoordinatorLayout)findViewById(R.id.cordi);


        spinner = (Spinner) findViewById(R.id.spingender);

        categories = new ArrayList<String>();
        categories.add("*Gender");
        categories.add("Female");
        categories.add("Male");
        categories.add("Others");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,  R.layout.simple_spinner_item_black, categories){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.hintColor));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if(userModel!=null)
        {
          loadMore();
        }


        cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(MoreActivity.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, null)
                .setDoneText("Done")
                .setCancelText("Cancel");


        tvdobset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdp.show(getSupportFragmentManager(), "DATE_FRAG");
            }
        });


    }

    private void loadMore() {

        if(userModel.getUserGender()!=null&&!userModel.getUserGender().isEmpty())
        {
            tvgenderset.setText(userModel.getUserGender().trim());
            if(userModel.getUserGender().trim().toLowerCase().equals("male"))
            {
                spinner.setSelection(2);
            }
            else if(userModel.getUserGender().trim().toLowerCase().equals("female")){
                spinner.setSelection(1);

            }
            else
            {
                spinner.setSelection(3);
            }


        }
        else {
            tvgenderset.setText("Not Set");

        }


        if(userModel.getDob()!=null&&!userModel.getDob().isEmpty())
        {
            tvdobset.setText(userModel.getDob().trim());

        }
        else {
            tvdobset.setText("Not Set");
        }


        if(userModel.getUserAddress()!=null&&!userModel.getUserAddress().isEmpty())
        {
            tvaddressset.setText(userModel.getUserAddress().trim());
            evaddressset.setText(userModel.getUserAddress().trim());
        }
        else {
            tvaddressset.setText("Not Set");
            evaddressset.setText("");
            evaddressset.setHint("Not Set");

        }


        if(userModel.getUserEducation()!=null&&!userModel.getUserEducation().isEmpty())
        {
            tveduset.setText(userModel.getUserEducation().trim());
            eveduset.setText(userModel.getUserEducation().trim());
        }
        else{
            tveduset.setText("Not Set");
            eveduset.setText("");
            eveduset.setHint("Not Set");

        }


        if(userModel.getUserDesignation()!=null&&!userModel.getUserDesignation().isEmpty())
        {
            tvdesigset.setText(userModel.getUserDesignation().trim());
            evdesigset.setText(userModel.getUserDesignation().trim());
        }
        else {
            tvdesigset.setText("Not Set");
            evdesigset.setText("");
            evdesigset.setHint("Not Set");

        }


        if(userModel.getUserExperience()!=null&&!userModel.getUserExperience().isEmpty())
        {
            tvexperset.setText(userModel.getUserExperience().trim());
            evexperset.setText(userModel.getUserExperience().trim());
        }
        else {
            tvexperset.setText("Not Set");
            evexperset.setText("");
            evexperset.setHint("Not Set");

        }


        if(userModel.getUserWagesHour()!=null&&!userModel.getUserWagesHour().toString().isEmpty())
        {
            tvwagesset.setText(userModel.getUserWagesHour().toString().trim());
            evwagesset.setText(userModel.getUserWagesHour().toString().trim());
        }
        else {
            tvwagesset.setText("Not Set");
            evwagesset.setText("");
            evwagesset.setHint("Not Set");

        }


        if(userModel.getUserAdditionalSkill()!=null&&!userModel.getUserAdditionalSkill().isEmpty())
        {
            tvskillset.setText(userModel.getUserAdditionalSkill().trim());
            evskillset.setText(userModel.getUserAdditionalSkill().trim());
        }
        else {
            tvskillset.setText("Not Set");
            evskillset.setText("");
            evskillset.setHint("Not Set");

        }


        if(userModel.getLanguagesknown()!=null&&!userModel.getLanguagesknown().isEmpty())
        {
            tvlanguageset.setText(userModel.getLanguagesknown().trim());
            evlanguageset.setText(userModel.getLanguagesknown().trim());
        }
        else {
            tvlanguageset.setText("Not Set");
            evlanguageset.setText("");
            evlanguageset.setHint("Not Set");

        }


//            if(userModel.get()!=null&&!userModel.getUserGender().isEmpty())
//            {
//                tvgenderset.setText(userModel.getUserGender().trim());
//            }
//            else
        tvemptypeset.setText("NO data from server");
        evemptypeset.setText("");
        evemptypeset.setHint("Not Set");


        if(userModel.getCategoryName()!=null&&!userModel.getCategoryName().isEmpty())
        {
            tvserviceset.setText(userModel.getCategoryName().trim());
        }
        else {
            tvserviceset.setText("Not Set");
        }


        if(userModel.getPreLocation1()!=null&&!userModel.getPreLocation1().isEmpty())
        {
            tvlocset.setText(userModel.getPreLocation1().trim());
        }
        else {
            tvlocset.setText("Not Set");
        }

        if(userModel.getPreLocation2()!=null&&!userModel.getPreLocation2().isEmpty())
        {
            tvloc2set.setText(userModel.getPreLocation2().trim());
        }
        else {
            tvloc2set.setText("Not Set");
        }

        if(userModel.getPreLocation3()!=null&&!userModel.getPreLocation3().isEmpty())
        {
            tvloc3set.setText(userModel.getPreLocation3().trim());
        }
        else {
            tvloc3set.setText("Not Set");
        }

        if(userModel.getCountryName()!=null&&!userModel.getCountryName().isEmpty())
        {
            tvcountryset.setText(userModel.getCountryName().trim());
        }
        else {
            tvcountryset.setText("Not Set");
        }

        if(userModel.getStateName()!=null&&!userModel.getStateName().isEmpty())
        {
            tvstateset.setText(userModel.getStateName().trim());
        }
        else {
            tvstateset.setText("Not Set");
        }


        if(userModel.getCityName()!=null&&!userModel.getCityName().isEmpty())
        {
            tvcityset.setText(userModel.getCityName().trim());
            tvcityset.setText(userModel.getCityName().trim());
        }
        else
        {
            tvcityset.setText("Not Set");
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

                    spinner.setVisibility(View.VISIBLE);
                    evaddressset.setVisibility(View.VISIBLE);
                    eveduset.setVisibility(View.VISIBLE);
                    evdesigset.setVisibility(View.VISIBLE);
                    evexperset.setVisibility(View.VISIBLE);
                    evwagesset.setVisibility(View.VISIBLE);
                    evskillset.setVisibility(View.VISIBLE);
                    evlanguageset.setVisibility(View.VISIBLE);
                    evemptypeset.setVisibility(View.VISIBLE);

                    tvgenderset.setVisibility(View.GONE);
                    tvaddressset.setVisibility(View.GONE);
                    tveduset.setVisibility(View.GONE);
                    tvdesigset.setVisibility(View.GONE)
                    ;tvexperset.setVisibility(View.GONE);
                    tvwagesset.setVisibility(View.GONE);
                    tvskillset.setVisibility(View.GONE);
                            tvlanguageset.setVisibility(View.GONE);
                    tvemptypeset.setVisibility(View.GONE);

                } else {
                    //Your code when unchecked
                    Log.i("FRAG"," save or view----");
                    spinner.setVisibility(View.GONE);
                    evaddressset.setVisibility(View.GONE);
                    eveduset.setVisibility(View.GONE);
                    evdesigset.setVisibility(View.GONE);
                    evexperset.setVisibility(View.GONE);
                    evwagesset.setVisibility(View.GONE);
                    evskillset.setVisibility(View.GONE);
                    evlanguageset.setVisibility(View.GONE);
                    evemptypeset.setVisibility(View.GONE);

                    tvgenderset.setVisibility(View.VISIBLE);
                    tvdobset.setVisibility(View.VISIBLE);
                    tvaddressset.setVisibility(View.VISIBLE);
                    tveduset.setVisibility(View.VISIBLE);
                    tvdesigset.setVisibility(View.VISIBLE);
                    tvexperset.setVisibility(View.VISIBLE);
                    tvwagesset.setVisibility(View.VISIBLE);
                    tvskillset.setVisibility(View.VISIBLE);
                    tvlanguageset.setVisibility(View.VISIBLE);
                    tvemptypeset.setVisibility(View.VISIBLE);

                    saveMore();
                    loadMore();
                }
            }
        });
        return true;
    }
    private void saveMore() {
        userModel.setUserGender(categories.get(spinner.getSelectedItemPosition()));
        userModel.setDob(tvdobset.getText().toString().trim());
        userModel.setUserAddress(evaddressset.getText().toString().trim());
        userModel.setUserEducation(eveduset.getText().toString().trim());
        userModel.setUserDesignation(evdesigset.getText().toString().trim());
        userModel.setUserExperience(evexperset.getText().toString().trim());
        if(evwagesset.getText().toString().trim().isEmpty())
        {
            userModel.setUserWagesHour(Double.valueOf("0.0"));
        }
        else {userModel.setUserWagesHour(Double.valueOf(evwagesset.getText().toString().trim()));}
        userModel.setUserAdditionalSkill(evskillset.getText().toString().trim());
        userModel.setLanguagesknown(evlanguageset.getText().toString().trim());
//        userModel.emptype(evemptypeset.getText().toString().trim());

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            Log.i("DATE18","not 18");
            snackbar = Snackbar
                    .make(cordi, R.string.dob_noteighteen, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }else
        {
            Log.i("DATE18","yes 18");
            monthOfYear+=1;

            tvdobset.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

        }

    }
}

package com.bridge.soom.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.MySeekBar;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends BaseActivity implements GetCatDatas {
    private ImageButton tick;
//    private MySeekBar distance_seek;
    private RelativeLayout.LayoutParams p;
    private   RangeSeekBar rangeSeekBar,rangeSeekBar2;
    private ExpandableLayout expandableLayout1;
    private ToggleButton toggleprofile;
    private String FlID ="";
    private String FlNAME ="";
    private String price_min ="";
    private String price_max ="";
    private String range_min ="";
    private String range_max ="";

    private NetworkManager networkManager;
    private ListView list;
    private List<String> filtersname;
    private ArrayAdapter<String> adapter;
    private Snackbar snackbar;
    private CoordinatorLayout cordi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FlID = getIntent().getStringExtra("FILTERID");
        FlNAME = getIntent().getStringExtra("FILTERName");
        price_min = getIntent().getStringExtra("price_min");
        price_max = getIntent().getStringExtra("price_max");
        range_min = getIntent().getStringExtra("range_min");
        range_max = getIntent().getStringExtra("range_max");


//        distance_seek = (MySeekBar) findViewById(R.id.distance_seek);
//        textView = (TextView) findViewById(R.id.textxxax);
//        distance_seek.setMax(65);
//        distance_seek.setProgress(0);
        networkManager = new NetworkManager(this);
        filtersname =  new ArrayList<>();
        filtersname.clear();
        cordi = (CoordinatorLayout)findViewById(R.id.cordi);
        list = (ListView) findViewById(R.id.list);
        networkManager.new RetrieveGetSubCategoryListTask(FilterActivity.this, FlID.trim())
                .execute();

        // Seek bar for which we will set text color in code
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeSeekBarTextColorWithCode);
        rangeSeekBar2 = (RangeSeekBar) findViewById(R.id.rangeSeekBar2);

        if(!price_min.isEmpty()&&!price_max.isEmpty())
        {
            rangeSeekBar.setSelectedMinValue(Integer.valueOf(price_min));
            rangeSeekBar.setSelectedMaxValue(Integer.valueOf(price_max));

        }
        if(!range_min.isEmpty()&&!range_max.isEmpty())
        {
            rangeSeekBar2.setSelectedMinValue(Double.valueOf(range_min));
            rangeSeekBar2.setSelectedMaxValue(Double.valueOf(range_max));

        }

//        rangeSeekBar.setTextAboveThumbsColorResource(android.R.color.holo_blue_bright);
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandable_layout);
        toggleprofile = (ToggleButton) findViewById(R.id.toggleprofile);
        toggleprofile.setChecked(false);
        expandableLayout1.collapse();

        toggleprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleprofile.isChecked()) {

                    if(filtersname.size()==0)
                    {
                        toggleprofile.setChecked(false);
                        snackbar = Snackbar.make(cordi, "No Filters", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }
                    else {expandableLayout1.expand();}
                } else  {
                    expandableLayout1.collapse();
                }
            }
        });


//        distance_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progress=progress+10;
//               if(progress<=65)
//               {  p = new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//                p.addRule(RelativeLayout.ABOVE, seekBar.getId());
//
//                Rect thumbRect = distance_seek.getSeekBarThumb().getBounds();
//                p.setMargins(
//                        thumbRect.centerX(),0,0, 0);}
//                textView.setLayoutParams(p);
//                textView.setText(String.valueOf(progress) + " KM");
//                textView.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                textView.setVisibility(View.INVISIBLE);
//
//
//            }
//        });


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, filtersname);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filterok, menu);
        tick = (ImageButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FILTERSUBMIT",rangeSeekBar.getSelectedMinValue().toString());
                Log.i("FILTERSUBMIT",rangeSeekBar.getSelectedMaxValue().toString());
                Log.i("FILTERSUBMIT",rangeSeekBar2.getSelectedMinValue().toString());
                Log.i("FILTERSUBMIT",rangeSeekBar2.getSelectedMaxValue().toString());

                SparseBooleanArray checked = list.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));

                    Log.i("FILTERSUBMIT",adapter.getItem(position));


                }

                String news = android.text.TextUtils.join(",", selectedItems);
                Log.i("FILTERSUBMIT",news);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("price_min",rangeSeekBar.getSelectedMinValue().toString());
                returnIntent.putExtra("price_max",rangeSeekBar.getSelectedMaxValue().toString());
                returnIntent.putExtra("range_min",rangeSeekBar2.getSelectedMinValue().toString());
                returnIntent.putExtra("range_max",rangeSeekBar2.getSelectedMaxValue().toString());
                returnIntent.putExtra("filters",news);
                returnIntent.putExtra("filtersName",FlNAME);
                returnIntent.putExtra("filtersID",FlID);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


        return true;
    }

    @Override
    public void failedtoConnect() {

    }

    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetCategoryList(List<String> catid, List<String> catname) {

    }

    @Override
    public void GetSubCategoryList(List<String> subcatid, final List<String> subcatname) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("FILTERCAT", " yaaay " + subcatname.size());
                filtersname.clear();
                for (int i = 0; i < subcatname.size(); i++) {
                    Log.i("FILTERCAT", " yaaay " + subcatname.get(i));

                    filtersname.add(subcatname.get(i));
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void GetSubCategoryListFailed(String msg) {
        Log.i("FILTERCAT"," yaaay "+msg);


    }

    @Override
    public void GetStateCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetStateListFailed(String msg) {

    }

    @Override
    public void GetCityCategoryList(List<String> subcatid, List<String> subcatname, List<String> lat, List<String> lng) {

    }

    @Override
    public void GetCityListFailed(String msg) {

    }

    @Override
    public void GetCountryCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetCountryListFailed(String msg) {

    }
}

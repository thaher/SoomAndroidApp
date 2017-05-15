package com.bridge.soom.Activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.MySeekBar;
import com.bridge.soom.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class FilterActivity extends BaseActivity {
    private ImageButton tick;
    private MySeekBar distance_seek;
    private TextView textView ;
    private RelativeLayout.LayoutParams p;
    private   RangeSeekBar rangeSeekBar,rangeSeekBar2;
    private ExpandableLayout expandableLayout1;
    private ToggleButton toggleprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        distance_seek = (MySeekBar) findViewById(R.id.distance_seek);
        textView = (TextView) findViewById(R.id.textxxax);
        distance_seek.setMax(65);
        distance_seek.setProgress(0);



        // Seek bar for which we will set text color in code
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeSeekBarTextColorWithCode);
        rangeSeekBar2 = (RangeSeekBar) findViewById(R.id.rangeSeekBar2);

//        rangeSeekBar.setTextAboveThumbsColorResource(android.R.color.holo_blue_bright);
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandable_layout);
        toggleprofile = (ToggleButton) findViewById(R.id.toggleprofile);
        toggleprofile.setChecked(false);
        expandableLayout1.collapse();

        toggleprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleprofile.isChecked()) {
                    expandableLayout1.expand();
                } else  {
                    expandableLayout1.collapse();
                }
            }
        });


        distance_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+10;
               if(progress<=65)
               {  p = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.ABOVE, seekBar.getId());

                Rect thumbRect = distance_seek.getSeekBarThumb().getBounds();
                p.setMargins(
                        thumbRect.centerX(),0,0, 0);}
                textView.setLayoutParams(p);
                textView.setText(String.valueOf(progress) + " KM");
                textView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setVisibility(View.INVISIBLE);


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filterok, menu);
        tick = (ImageButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        return true;
    }
}

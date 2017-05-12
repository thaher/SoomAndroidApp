package com.bridge.soom.Activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.MySeekBar;
import com.bridge.soom.R;

public class FilterActivity extends BaseActivity {
    private ImageButton tick;
    private MySeekBar distance_seek;
    private TextView textView ;
    private RelativeLayout.LayoutParams p;

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
        distance_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+10;
               if(progress<=65)
               {  p = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.BELOW, seekBar.getId());

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

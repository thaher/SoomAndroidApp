package com.bridge.soom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.ListCatAdapter;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;

import java.util.ArrayList;
import java.util.List;

public class GetCatActivity extends BaseActivity implements RegistrationProviderResponse {
    private static final int REQUEST_CODE =201;
    private NetworkManager networkManager;
    private List<String> list ,catidx ;
    private RecyclerView recyclerView;
    private ListCatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkManager = new NetworkManager(this);
        list = new ArrayList<>();
        list.clear();
        catidx = new ArrayList<>();
        catidx.clear();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ListCatAdapter(list,GetCatActivity.this,GetCatActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();




    }

    private void prepareMovieData() {
        Log.i("Reg2_submit","RetrieveGetCategoryListTask");
        networkManager.new RetrieveGetCategoryListTask(GetCatActivity.this)
                .execute();
    }

    @Override
    public void failedtoConnect() {

    }

    @Override
    public void GetCategoryListFailed(String msg) {

    }

    @Override
    public void GetCategoryList(final List<String> catid, final List<String> catname) {
        Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got" );


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                GetCatActivity.this.list.clear();
                GetCatActivity.this.catidx.clear();
                for(int i=0;i<catname.size();i++)
                {
                    GetCatActivity.this.list.add(catname.get(i));
                    GetCatActivity.this.catidx.add(catid.get(i));
                }


                GetCatActivity.this.mAdapter.notifyDataSetChanged();
                Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );

            }
        });

    }

    @Override
    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetSubCategoryListFailed(String msg) {

    }


    public void startNextRest(int position) {

        Log.i("Reg2_submit","pos"+position+" "+list.get(position) );

//        startActivityForResult(new Intent(GetCatActivity.this,GetSubCatActivity.class),REQUEST_CODE);
        Intent i = new Intent(GetCatActivity.this, GetSubCatActivity.class);
        i.putExtra("filterid", catidx.get(position));
        startActivityForResult(i, REQUEST_CODE);

    }
}

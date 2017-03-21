package com.bridge.soom.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bridge.soom.Helper.ListCatAdapter;
import com.bridge.soom.Helper.ListSubCatAdapter;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;

import java.util.ArrayList;
import java.util.List;

public class GetSubCatActivity extends AppCompatActivity  implements RegistrationProviderResponse{
    private NetworkManager networkManager;
    private List<String> list ,catidx ;
    private RecyclerView recyclerView;
    private ListSubCatAdapter mAdapter;
    String FilterId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sub_cat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkManager = new NetworkManager(this);
        list = new ArrayList<>();
        list.clear();
        catidx = new ArrayList<>();
        catidx.clear();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ListSubCatAdapter(list,GetSubCatActivity.this,GetSubCatActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        FilterId = getIntent().getStringExtra("filterid");
        prepareMovieData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareMovieData() {
        networkManager.new RetrieveGetSubCategoryListTask(GetSubCatActivity.this, FilterId)
                .execute();

    }

    public void startNextRest(int position) {

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
    public void GetSubCategoryList(final List<String> subcatid, final List<String> subcatname) {
        Log.i("Reg2_submit","got sub list yaaay" );

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                GetSubCatActivity.this.list.clear();
                GetSubCatActivity.this.catidx.clear();
                for(int i=0;i<subcatid.size();i++)
                {
                    GetSubCatActivity.this.list.add(subcatname.get(i));
                    GetSubCatActivity.this.catidx.add(subcatid.get(i));
                }



                GetSubCatActivity.this.mAdapter.notifyDataSetChanged();
                Log.i("Reg2_submit","RetrieveGetCategoryListTask ---got2" );

            }
        });


    }

    @Override
    public void GetSubCategoryListFailed(String msg) {

    }
}

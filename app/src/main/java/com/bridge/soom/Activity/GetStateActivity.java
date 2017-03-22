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

import com.bridge.soom.Helper.BaseActivity;
import com.bridge.soom.Helper.ListCatAdapter;
import com.bridge.soom.Helper.ListStateAdapter;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.R;

import java.util.ArrayList;
import java.util.List;

public class GetStateActivity extends BaseActivity implements RegistrationProviderResponse {
    private static final int REQUEST_CODE =201;
    private NetworkManager networkManager;
    private List<String> list ,catidx ;
    private RecyclerView recyclerView;
    private ListStateAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_state);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkManager = new NetworkManager(this);
        list = new ArrayList<>();
        list.clear();
        catidx = new ArrayList<>();
        catidx.clear();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ListStateAdapter(list,GetStateActivity.this,GetStateActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
    }

    private void prepareMovieData() {
        Log.i("Reg2_submit","RetrieveGetStateListTask");
        networkManager.new RetrieveGetStateListTask(GetStateActivity.this)
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
    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname) {

    }

    @Override
    public void GetSubCategoryListFailed(String msg) {

    }

    @Override
    public void GetStateCategoryList(final List<String> catid, final List<String> subcatname) {
        Log.i("Reg2_submit","GetStateCategoryList : got");


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                list.clear();
                catidx.clear();
                for(int i=0;i<subcatname.size();i++)
                {
                    list.add(subcatname.get(i));
                    catidx.add(catid.get(i));
                }


                mAdapter.notifyDataSetChanged();
                Log.i("Reg2_submit","GetStateCategoryList ---got2" );

            }
        });



    }

    @Override
    public void GetStateListFailed(String msg) {

    }
}

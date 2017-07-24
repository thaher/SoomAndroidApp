package com.bridge.soom.Helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridge.soom.Activity.ProfessionalDetailsActivity;
import com.bridge.soom.Fragment.ProfessionalFragment;
import com.bridge.soom.Interface.ServiceandLocListner;
import com.bridge.soom.Model.Services;
import com.bridge.soom.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;


public class RecyclerAdapService extends RecyclerView.Adapter<RecyclerAdapService.MyViewHolder> {

    private List<Services> providerList;
    private Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private NetworkManager networkManager;
    private String AccessTocken="";
    private ServiceandLocListner lisner;
    private Boolean isActivty;
    private ProfessionalDetailsActivity activty;
    private ProfessionalFragment fragmen;



    public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView service_name, expr, numb,speciall,rateper;
    public Button edit,delete;
    ImageButton details, call, message;
        RelativeLayout rlser;
    RatingBar rating;
        SwipeRevealLayout swipeRevealLayout;

    public MyViewHolder(View view) {
        super(view);
        service_name = (TextView) view.findViewById(R.id.service_name);
        expr = (TextView) view.findViewById(R.id.expr);
        numb = (TextView) view.findViewById(R.id.numb);
        speciall = (TextView) view.findViewById(R.id.speciall);
        rateper = (TextView) view.findViewById(R.id.rateper);
        edit = (Button) view.findViewById(R.id.edit);
        delete = (Button) view.findViewById(R.id.delete);
        swipeRevealLayout = (SwipeRevealLayout) view.findViewById(R.id.swipeRevealLayout);
        rlser = (RelativeLayout) view.findViewById(R.id.rlser);
    }
}


    public RecyclerAdapService(List<Services> providerList, Context context,ServiceandLocListner listner,
                               NetworkManager networkManager,String AccessTocken,Boolean isActivty,ProfessionalDetailsActivity activty) {


        this.providerList = providerList;
        this.context = context;
        this.networkManager = networkManager;
        // uncomment the line below if you want to open only one row at a time
         viewBinderHelper.setOpenOnlyOne(true);
        this.AccessTocken = AccessTocken;
        this.lisner = listner;
        this.isActivty = isActivty;
        this.activty = activty;

    }
    public RecyclerAdapService(List<Services> providerList, Context context,ServiceandLocListner listner,
                               NetworkManager networkManager,String AccessTocken,Boolean isActivty,ProfessionalFragment fragmen) {


        this.providerList = providerList;
        this.context = context;
        this.networkManager = networkManager;
        // uncomment the line below if you want to open only one row at a time
        viewBinderHelper.setOpenOnlyOne(true);
        this.AccessTocken = AccessTocken;
        this.lisner = listner;
        this.isActivty = isActivty;
        this.fragmen = fragmen;

    }


    @Override
    public RecyclerAdapService.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_service, parent, false);

        return new RecyclerAdapService.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapService.MyViewHolder holder, int position) {
        final Services providerBasic = providerList.get(position);
        holder.service_name.setText(providerBasic.getServiceName());
        holder.expr.setText("Exp : "+providerBasic.getExperiance()+" Yrs.");
        if(position+1<=9)
            holder.numb.setText("0"+String.valueOf(position+1));
        else
            holder.numb.setText(String.valueOf(position+1));

        holder.rateper.setText(providerBasic.getWages());
        holder.speciall.setText( TextUtils.join(", ", providerBasic.getSubServiceName()));
        viewBinderHelper.bind(holder.swipeRevealLayout, providerBasic.getTableid());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SLIDECLICK","EDIT");

               if(isActivty)
               {
                  activty.editService(providerBasic);

               }
               else {
                   fragmen.editService(providerBasic);

               }


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SLIDECLICK","DELTE");
                deleteService(providerBasic);


            }
        });
        holder.rlser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }


    private void deleteService(Services providerBasic) {
        networkManager.new DeleteServiceTask(lisner,AccessTocken,providerBasic.getServiceId())
                .execute();
    }


    @Override
    public int getItemCount() {
        return providerList.size();
    }
    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }
}
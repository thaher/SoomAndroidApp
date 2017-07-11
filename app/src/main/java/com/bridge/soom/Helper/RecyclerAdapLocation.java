package com.bridge.soom.Helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bridge.soom.Activity.ProfessionalDetailsActivity;
import com.bridge.soom.Model.PlaceLoc;
import com.bridge.soom.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

/**
 * Created by Thaher on 10-07-2017.
 */

public class RecyclerAdapLocation extends RecyclerView.Adapter<RecyclerAdapLocation.MyViewHolder> {

private List<PlaceLoc> providerList;
private Context context;
private ProfessionalDetailsActivity homeActivity;
private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView location_name, expr, numb,speciall,rateper;
    public Button edit,delete;
    ImageButton details, call, message;
    RatingBar rating;
    SwipeRevealLayout swipeRevealLayout;

    public MyViewHolder(View view) {
        super(view);
        location_name = (TextView) view.findViewById(R.id.location_name);
//        expr = (TextView) view.findViewById(R.id.expr);
        numb = (TextView) view.findViewById(R.id.numb);
        speciall = (TextView) view.findViewById(R.id.speciall);
//        rateper = (TextView) view.findViewById(R.id.rateper);
        edit = (Button) view.findViewById(R.id.edit);
        delete = (Button) view.findViewById(R.id.delete);
        swipeRevealLayout = (SwipeRevealLayout) view.findViewById(R.id.swipeRevealLayout);
    }
}


    public RecyclerAdapLocation(List<PlaceLoc> providerList, Context context, ProfessionalDetailsActivity homeActivity) {
        this.providerList = providerList;
        this.context = context;
        this.homeActivity = homeActivity;
        // uncomment the line below if you want to open only one row at a time
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public RecyclerAdapLocation.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_location, parent, false);

        return new RecyclerAdapLocation.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapLocation.MyViewHolder holder, int position) {
        final PlaceLoc providerBasic = providerList.get(position);
        holder.location_name.setText(providerBasic.getPlace().getName());
//        holder.expr.setText("Exp : "+providerBasic.getExperiance()+" Yrs.");
        if(position+1<=9)
            holder.numb.setText("0"+String.valueOf(position+1));
        else
            holder.numb.setText(String.valueOf(position+1));

//        holder.rateper.setText(providerBasic.getWages());
        holder.speciall.setText(providerBasic.getPlace().getAddress());
//        viewBinderHelper.bind(holder.swipeRevealLayout, providerBasic.getTableid());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SLIDECLICK","EDIT");
                homeActivity.editLocation(providerBasic);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SLIDECLICK","DELTE");
                homeActivity.deleteLocation(providerBasic);


            }
        });


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
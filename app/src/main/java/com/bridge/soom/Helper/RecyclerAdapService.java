package com.bridge.soom.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bridge.soom.Activity.HomeActivity;
import com.bridge.soom.Activity.ProfessionalDetailsActivity;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.Services;
import com.bridge.soom.R;

import java.util.List;



public class RecyclerAdapService extends RecyclerView.Adapter<RecyclerAdapService.MyViewHolder> {

private List<Services> providerList;
        Context context;
        ProfessionalDetailsActivity homeActivity;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView profile_name, category, rate;
    ImageButton details, call, message;
    RatingBar rating;

    public MyViewHolder(View view) {
        super(view);
        profile_name = (TextView) view.findViewById(R.id.profile_name);


    }
}


    public RecyclerAdapService(List<Services> providerList, Context context, ProfessionalDetailsActivity homeActivity) {
        this.providerList = providerList;
        this.context = context;
        this.homeActivity = homeActivity;
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
        holder.profile_name.setText(providerBasic.getServiceName() + " " + providerBasic.getSubServiceName());
        holder.category.setText(providerBasic.getExperiance());
        holder.rate.setText(providerBasic.getWages());


        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.details:

                        break;

            }}
        };
        holder.details.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }
}
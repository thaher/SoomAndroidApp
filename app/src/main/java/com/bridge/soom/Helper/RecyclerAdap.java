package com.bridge.soom.Helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bridge.soom.Activity.HomeActivity;
import com.bridge.soom.Activity.LoginActivity;
import com.bridge.soom.Activity.ProviderDetailsActivity;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thaher on 31-03-2017.
 */

public class RecyclerAdap extends RecyclerView.Adapter<RecyclerAdap.MyViewHolder> {

    private List<ProviderBasic> providerList;
    Context context;
    Boolean isGuest;
    HomeActivity homeActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView profile_name, category, rate;
        CircleImageView profile_image;
        ImageButton details, call, message;
        RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            profile_name = (TextView) view.findViewById(R.id.profile_name);
            category = (TextView) view.findViewById(R.id.category);
            rate = (TextView) view.findViewById(R.id.rate);
            profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
            details = (ImageButton) view.findViewById(R.id.details);
            call = (ImageButton) view.findViewById(R.id.call);
            message = (ImageButton) view.findViewById(R.id.message);
            rating = (RatingBar) view.findViewById(R.id.rating);

        }
    }


    public RecyclerAdap(List<ProviderBasic> providerList, Context context, Boolean isGuest, HomeActivity homeActivity) {
        this.providerList = providerList;
        this.context = context;
        this.isGuest = isGuest;
        this.homeActivity = homeActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProviderBasic providerBasic = providerList.get(position);
        holder.profile_name.setText(providerBasic.getUserFirstName() + " " + providerBasic.getUserLastName());
        holder.category.setText(providerBasic.getCategoryName());
        holder.rate.setText(providerBasic.getUserWagesHour());
        Glide.with(context).load(providerBasic.getProfileImageUrl().trim())
                .thumbnail(0.5f)
                .crossFade()
                .override(90, 90)
                .placeholder(R.drawable.avatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new GlideDrawableImageViewTarget(holder.profile_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        Log.d("Gliderrr", "readyy");

                        holder.profile_image.setImageDrawable(drawable);

                    }
                });
        holder.rating.setIsIndicator(true);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.details:

                        if (!isGuest) {
                            Intent intent = new Intent(context, ProviderDetailsActivity.class);
                            intent.putExtra("PROVIDER", providerBasic);
                            context.startActivity(intent);
                        } else {
                            //snackbar

                            Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                    .setAction("LOGIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            context.startActivity(intent);
                                            homeActivity.finish();
                                        }
                                    }).show();
                        }

                        break;
                    case R.id.call:

                        if (!isGuest) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + providerBasic.getUserMobile()));//change the number
                            if (ActivityCompat.checkSelfPermission(homeActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            context.startActivity(callIntent);
                        }
                        else {
                            //snackbar

                            Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                    .setAction("LOGIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent (context, LoginActivity.class);
                                            context.startActivity(intent);
                                            homeActivity.finish();
                                        }
                                    }).show();
                        }
                        break;

                    case R.id.message:
                        if(!isGuest){  Uri uri = Uri.parse("smsto:"+providerBasic.getUserMobile());
                            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                            it.putExtra("sms_body", "The SMS text");
                            context.startActivity(it);
                        }
                        else {
                            //snackbar

                            Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                    .setAction("LOGIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent (context, LoginActivity.class);
                                            context.startActivity(intent);
                                            homeActivity.finish();
                                        }
                                    }).show();
                        }
                        break;
                }
            }
        };
        holder.details.setOnClickListener(onClickListener);
        holder.message.setOnClickListener(onClickListener);
        holder.call.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }
}
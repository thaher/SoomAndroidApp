package com.bridge.soom.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.soom.Activity.HomeActivity;
import com.bridge.soom.Activity.LoginActivity;
import com.bridge.soom.Activity.ProviderDetailsActivity;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import de.hdodenhof.circleimageview.CircleImageView;


public class FormFragment extends Fragment {
    ProviderBasic  provider ;
    Boolean isGuest;



    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_window_form_fragment, container, false);
        provider = (ProviderBasic) getArguments().getSerializable("PROVIDER");
        isGuest = (Boolean) getArguments().getBoolean("ISGUEST");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView profile_name = (TextView) view.findViewById(R.id.profile_name);
        TextView category = (TextView) view.findViewById(R.id.category);
        TextView rate = (TextView) view.findViewById(R.id.rate);
        CircleImageView profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        ImageButton details = (ImageButton) view.findViewById(R.id.details);
        ImageButton call = (ImageButton) view.findViewById(R.id.call);
        ImageButton message = (ImageButton) view.findViewById(R.id.message);
        RatingBar rating = (RatingBar) view.findViewById(R.id.rating);

        Glide.with(this).load(provider.getProfileImageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .override(90,90)
                .placeholder(R.drawable.avatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profile_image);

        profile_name.setText(provider.getUserFirstName());
        category.setText(provider.getCategoryName());
        rate.setText(provider.getUserWagesHour());
        rating.setIsIndicator(true);
        rating.setVisibility(View.GONE);
        Log.i("Glide"," "+provider.getProfileImageUrl());
//        rating.setRating(3);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.details:

                    if(!isGuest){  Intent intent = new Intent (getContext(), ProviderDetailsActivity.class);
                        intent.putExtra("PROVIDER",provider);
                        getContext().startActivity(intent);}
                        else {
                        //snackbar

                        Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                .setAction("LOGIN", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent (getContext(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }).show();
                    }

                        break;
                    case R.id.call:

                        if(!isGuest){   Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+provider.getUserMobile()));//change the number
                        startActivity(callIntent);
                        }
                        else {
                            //snackbar

                            Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                    .setAction("LOGIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent (getContext(), LoginActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }).show();
                        }
                        break;

                    case R.id.message:
                        if(!isGuest){  Uri uri = Uri.parse("smsto:"+provider.getUserMobile());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "The SMS text");
                        startActivity(it);
                        }
                        else {
                            //snackbar

                            Snackbar.make(v, "Please Login to Access More Details", Snackbar.LENGTH_LONG)
                                    .setAction("LOGIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent (getContext(), LoginActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }).show();
                        }
                        break;
                }
            }
        };
        details.setOnClickListener(onClickListener);
        message.setOnClickListener(onClickListener);
        call.setOnClickListener(onClickListener);
    }


}

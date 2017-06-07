package com.bridge.soom.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bridge.soom.Activity.ChangePaswordActivity;
import com.bridge.soom.Activity.HomeActivity;
import com.bridge.soom.Activity.MoreActivity;
import com.bridge.soom.Activity.ProfileActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ProfileUpdateListner;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProviderDetailsResponse,ProfileUpdateListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private ToggleButton mSwitchShowSecure,mSwitchShowEdit;
    private UserModel userModel;
    private TextView tvfnameset,tvlnameset,tvnumberset,tvemailset,usertype;
    private EditText evfnameset,evlnameset,evnumberset,evemailset;
    private CircleImageView profile_image;
    private RelativeLayout changepass, more;
    private NetworkManager networkManager;
    private Snackbar snackbar;
    private View vieq1;
    private Boolean gotdata =false;

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Integer PROFILE_PIC_COUNT =0;

    Uri selectedImage = null;
    private Uri outputFileUri;
    private File profileIMG = null;

    private View rootView;
    private LinearLayout coordi;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        HomeActivity activity = (HomeActivity) getActivity();
        userModel = activity.getMyUser();
        networkManager = new NetworkManager(activity);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        tvfnameset = (TextView) rootView.findViewById(R.id.tvfnameset);
        tvlnameset = (TextView) rootView.findViewById(R.id.tvlnameset);
        tvnumberset = (TextView) rootView.findViewById(R.id.tvnumberset);
        tvemailset = (TextView) rootView.findViewById(R.id.tvemailset);

        evfnameset = (EditText) rootView.findViewById(R.id.evfnameset);
        evlnameset = (EditText) rootView.findViewById(R.id.evlnameset);
        evnumberset = (EditText) rootView.findViewById(R.id.evnumberset);
        evemailset = (EditText) rootView.findViewById(R.id.evemailset);

        usertype = (TextView) rootView.findViewById(R.id.usertype);
        changepass = (RelativeLayout) rootView.findViewById(R.id.changepass);
        more = (RelativeLayout) rootView.findViewById(R.id.more);
        more.setVisibility(View.GONE);
        vieq1 = (View) rootView.findViewById(R.id.vieq1);

        coordi = (LinearLayout) rootView.findViewById(R.id.coordi);
        evfnameset.setEnabled(false);
        evlnameset.setEnabled(false);
        evemailset.setEnabled(false);
        evnumberset.setEnabled(false);


        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(userModel!=null)
        {
            Glide.with(this)
                    .load(userModel.getProfileImageUrl().trim())
                    .crossFade()
                    .override(150, 150)
                    .placeholder(R.drawable.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new GlideDrawableImageViewTarget(profile_image) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            Log.d("Gliderrr", "readyy");

                            profile_image.setImageDrawable(drawable);

                        }
                    });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //picker for camer aor gsallery


                    if (mSwitchShowSecure.isChecked()) {

                        if (Build.VERSION.SDK_INT < 23) {

                            //We already have permission. Write your function call over hear
                            takepic();
                        } else {

                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                // Here we are asking for permission

                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                            } else {

                                //If the app is running for second time, then we already have permission. You can write your function here, if we already have permission.

                                takepic();

                            }

                        }


                    }



                }
            });

            loadBASIC();

            changepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSwitchShowSecure.isChecked()) {
                        Log.i("TOGGLE :", "ISCHECKED");

                        Snackbar.make(coordi, "Discard Changes ", Snackbar.LENGTH_LONG)
                                .setAction("DISCARD", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        try  {
                                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                        } catch (Exception e) {

                                            Log.i("FRAG"," save or view----"+e.getMessage());


                                        }

                                        evfnameset.setEnabled(false);
                                        evlnameset.setEnabled(false);


//                                    tvfnameset.setVisibility(View.VISIBLE);
//                                    tvlnameset.setVisibility(View.VISIBLE);


                                        Intent intent = new Intent (getActivity(), ChangePaswordActivity.class);
                                        intent.putExtra("tocken",userModel.getAccessToken());
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                    else {

                        Intent intent = new Intent (getActivity(), ChangePaswordActivity.class);
                        intent.putExtra("tocken",userModel.getAccessToken());
                        startActivity(intent);
                    }
                }
            });



        }
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gotdata){  if (mSwitchShowSecure.isChecked()) {
                    Log.i("TOGGLE :", "ISCHECKED");

                    Snackbar.make(coordi, "Discard Changes ", Snackbar.LENGTH_LONG)
                            .setAction("DISCARD", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    try  {
                                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {

                                        Log.i("FRAG"," save or view----"+e.getMessage());


                                    }

                                    evfnameset.setEnabled(false);
                                    evlnameset.setEnabled(false);


//                                    tvfnameset.setVisibility(View.VISIBLE);
//                                    tvlnameset.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent (getContext(), MoreActivity.class);
                                    intent.putExtra("userMore",userModel);
                                    startActivityForResult(intent,3);



                                }
                            }).show();
                }
                else {
                    Intent intent = new Intent (getContext(), MoreActivity.class);
                    intent.putExtra("userMore",userModel);
                    startActivityForResult(intent,3);
                }}
                else {
                    snackbar = Snackbar.make(coordi, "Professional Details Not Loaded", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                }




            }
        });

        evfnameset.clearFocus();
        evlnameset.clearFocus();
        evemailset.clearFocus();
        evnumberset.clearFocus();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.fragmenta_menu, menu);

        mSwitchShowSecure = (ToggleButton) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected_edit);
        mSwitchShowSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {



                if(b){
                    Log.i("FRAG"," edit view  ----");

//                    evfnameset.setVisibility(View.VISIBLE);
//                    evlnameset.setVisibility(View.VISIBLE);
                    evfnameset.setEnabled(true);
                    evlnameset.setEnabled(true);

//                    tvfnameset.setVisibility(View.GONE);
//                    tvlnameset.setVisibility(View.GONE);

                } else {
                    //Your code when unchecked
                    Log.i("FRAG", " save or view----");


                    if(isvalid()){    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                        Log.i("FRAG", " save or view----" + e.getMessage());


                    }

//                    evfnameset.setVisibility(View.GONE);
//                    evlnameset.setVisibility(View.GONE);
                        evfnameset.setEnabled(false);
                        evlnameset.setEnabled(false);

//                    tvfnameset.setVisibility(View.VISIBLE);
//                    tvlnameset.setVisibility(View.VISIBLE);


                        saveBASIC();
                        loadBASIC();
                    }
                    else {
                        mSwitchShowSecure.setChecked(false);


                        snackbar = Snackbar.make(coordi, "Invalid Fields", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }

                }
            }
        });

    }

    private void loadBASIC() {
        if(userModel.getUserFirstName()!=null)
        { tvfnameset.setText(userModel.getUserFirstName());
            evfnameset.setText(userModel.getUserFirstName());}
        if(userModel.getUserLastName()!=null)
        {tvlnameset.setText(userModel.getUserLastName());
            evlnameset.setText(userModel.getUserLastName());}
        if(userModel.getUserMobile()!=null)
            tvnumberset.setText(userModel.getUserMobile());
        evnumberset.setText(userModel.getUserMobile());
        if(userModel.getUserEmail()!=null)
            tvemailset.setText(userModel.getUserEmail());
        evemailset.setText(userModel.getUserEmail());
        if(userModel.getUserType().trim().equals("USR"))
        {
            usertype.setText("User");
            vieq1.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        }
        else usertype.setText("Service Provider");
    }

    private void takepic() {
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void ProfileUpdateSuccess(String message, String profile) {
        snackbar = Snackbar.make(coordi,"Saved", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
        userModel.setProfileImageUrl(profile.trim());
        SharedPreferencesManager.init(getContext());
        SharedPreferencesManager.write(USER_FIRST_NAME,userModel.getUserFirstName());
        SharedPreferencesManager.write(USER_LAST_NAME,userModel.getUserLastName());
        SharedPreferencesManager.write(USER_IMAGE_URL,userModel.getProfileImageUrl().trim());
    }

    @Override
    public void ProfileUpdateFailed(String message) {
        snackbar = Snackbar.make(coordi,message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void DetailsResponseSuccess(UserModel userModels) {
        Log.i("GETPROFILE"," user1 :"+userModels.getUserMobile());
        userModel = userModels;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("GETPROFILE"," user2 :"+userModel.getUserMobile());
                gotdata =true;

                if(!userModel.getUserType().trim().equals("USR"))
                {
                    more.setVisibility(View.VISIBLE);
                }
                loadBASIC();

            }
        });


    }

    @Override
    public void DetailsResponseFailed(String message) {
        //snackbar
        snackbar = Snackbar.make(coordi, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    @Override
    public void failedtoConnect() {

        snackbar = Snackbar.make(coordi, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

    }

    public boolean isvalid() {

        return !evfnameset.getText().toString().trim().isEmpty()|| !evlnameset.getText().toString().trim().isEmpty() ;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        super.onResume();
        networkManager.new GetProviderDetailsTask(this,userModel.getAccessToken())
                .execute();
        loadBASIC();



    }

    private void saveBASIC() {




        userModel.setUserFirstName(evfnameset.getText().toString().trim());
        userModel.setUserLastName(evlnameset.getText().toString().trim());
//        String imguri  =getPath(selectedImage);

        File ProfileImage = null;
        Log.i("FILEURI","selected image :"+selectedImage);
        Log.i("SAVINGIGNG","selected image :"+selectedImage);

//        if(selectedImage!= null)
//        {
//
//
//
//            (Uri.parse(getPath(selectedImage)));
//            //  profile_image.setImageURI(Uri.parse(ProfileImage.toString()));
//
//        }

        networkManager.new UpdateprofiledataTask(this,userModel,profileIMG)
                .execute();
    }
}

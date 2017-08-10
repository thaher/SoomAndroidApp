package com.bridge.soom.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneNumberUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bridge.soom.Activity.RegistrationFillActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ChangePassResponse;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;

import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.DEVICE_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements ProviderDetailsResponse,ChangePassResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserModel userModel;
    private ImageButton regfillsubmit,changepass;
    private EditText fname,lname,code,mobnum,email,aboutme;
    private NetworkManager networkManager;
    private String LastName,FirstName,MobileNumber,EmailId,DevideID,Timexone,cultureInfo,Aboutme;
    private ProgressDialog progress;
    private String tocken = "";
    private  View view;
    private Snackbar snackbar;
    private ViewGroup hiddenPanel;
    private ImageButton close_popup;
    private EditText confirmpass,newpass,oldpass;
    private Button fab;
    private boolean isEditing =false;



    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
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
        networkManager = new NetworkManager(getActivity());
        SharedPreferencesManager.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dismissLoadingDialog();

        view = inflater.inflate(R.layout.fragment_account, container, false);
        userModel = (UserModel) getArguments().getSerializable("userModel");
        if(userModel!=null) Log.d("PROFILEINFO", "Account Got Argument: " + userModel.getUserFirstName());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("profile_data"));
        // Inflate the layout for this fragment
        fname= (EditText) view.findViewById(R.id.fname);
        lname= (EditText) view.findViewById(R.id.lname);
        code= (EditText) view.findViewById(R.id.code);
        mobnum= (EditText) view.findViewById(R.id.mobnum);
        email= (EditText) view.findViewById(R.id.email);
        aboutme= (EditText) view.findViewById(R.id.aboutme);
        regfillsubmit= (ImageButton) view.findViewById(R.id.regfillsubmit);
        changepass= (ImageButton) view.findViewById(R.id.changepass);
        hiddenPanel = (ViewGroup)view.findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        close_popup = (ImageButton) view.findViewById(R.id.close_popup);
        fab = (Button) view.findViewById(R.id.fab);
        confirmpass = (EditText) view.findViewById(R.id.confirmpass);
        newpass = (EditText) view.findViewById(R.id.newpass);
        oldpass = (EditText) view.findViewById(R.id.oldpass);

        confirmpass.setTypeface(Typeface.DEFAULT);
        confirmpass.setTransformationMethod(new PasswordTransformationMethod());
        newpass.setTypeface(Typeface.DEFAULT);
        newpass.setTransformationMethod(new PasswordTransformationMethod());
        oldpass.setTypeface(Typeface.DEFAULT);
        oldpass.setTransformationMethod(new PasswordTransformationMethod());

        code.setText("+91");



        disableEditText(code);
        disableEditText(mobnum);
        disableEditText(email);
        regfillsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(validateForm())
                {
//                   regfillsubmit.setEnabled(false);
                    LastName=lname.getText().toString();
                    FirstName = fname.getText().toString();
                    MobileNumber = mobnum.getText().toString();
                    EmailId = email.getText().toString();
                    Aboutme = aboutme.getText().toString().trim();
                    DevideID =SharedPreferencesManager.read(DEVICE_ID,"");
                    Timexone= String.valueOf(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
                    cultureInfo = getCurrentLocale().getLanguage();

                    showLoadingDialog();
                    Log.i("Reg_submit"," "+FirstName+" "+LastName+" "+MobileNumber+" "+EmailId);
                    if(userModel!=null){networkManager.new UpdateAccountTask(AccountFragment.this,LastName,FirstName,MobileNumber,DevideID,userModel.getUserType(),Timexone,cultureInfo,EmailId,Aboutme,userModel.getAccessToken())
                            .execute();
                        networkManager.new UpdateAboutmeTask(AccountFragment.this,Aboutme,userModel.getAccessToken())
                                .execute();}

                }
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                slideUpDown(v);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                if(allvalid()){     String old = oldpass.getText().toString();
                    String newp = newpass.getText().toString();
                    if (old.trim().isEmpty() || newp.trim().isEmpty() || confirmpass.toString().trim().isEmpty()) {
                        Snackbar.make(view, "Invalid Fields!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        oldpass.setText("");
                        newpass.setText("");
                        confirmpass.setText("");
                        showLoadingDialog();
                        networkManager.new SetNewPasswordTask(AccountFragment.this, tocken, old, newp, "")
                                .execute();

                    }
                }






            }
        });

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown(v);
            }
        });

        setuserdata();


        return view;
    }

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);


        }
    }
    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    private void setuserdata() {
        if(userModel!=null)
        {
            if(userModel.getUserType().trim().equals("USR")){
                //SEEKER
                aboutme.setVisibility(View.GONE);
            }
            else {
//
                aboutme.setVisibility(View.VISIBLE);

            }
            fname.setText(userModel.getUserFirstName());
            lname.setText(userModel.getUserLastName());
            code.setText("+91");
            mobnum.setText(userModel.getUserMobile());
            email.setText(userModel.getUserEmail());
            aboutme.setText(userModel.getAboutMe());

        }
    }

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setMessage(getString(R.string.loading_message));
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
        }
    }

    private boolean allvalid() {
        if(oldpass.getText().toString().trim().isEmpty())
        {
            oldpass.setError("Invalid Field");

            //snackbar
            snackbar = Snackbar.make(view, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

            return false;
        }
        else if (newpass.getText().toString().trim().isEmpty())
        {
            newpass.setError("Invalid Field");


            //snackbar
            snackbar = Snackbar.make(view, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if (newpass.getText().toString().trim().length()<6)
        {
            newpass.setError("Requires Minimus 6 character");


            //snackbar
            snackbar = Snackbar.make(view, "Requires Minimus 6 character", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if( confirmpass.getText().toString().trim().isEmpty())
        {
            //snackbar
            confirmpass.setError("Invalid Field");

            snackbar = Snackbar.make(view, "Invalid Field", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
            return false;
        }
        else if(!newpass.getText().toString().equals(confirmpass.getText().toString()))
        {
            confirmpass.setError("Passwords does not match!");

            //snackbar
            snackbar = Snackbar.make(view, "Passwords does not match!", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();

            return false;
        }
        return true;    }

    private boolean validateForm() {

        if(fname.getText().toString().trim().isEmpty())
        {
//  snackbar = Snackbar
//                .make(cordi, R.string.name_empty , Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();

            fname.setError("Invalid Field - First Name");
            fname.requestFocus();

        }
        else  if(fname.getText().toString().trim().length()>25)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            fname.setError("Limit Exceeded - First Name");
            fname.requestFocus();


        }
        else  if(lname.getText().toString().trim().isEmpty())
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            lname.setError("Invalid Field - Last Name");
            lname.requestFocus();


        }
        else  if(lname.getText().toString().trim().length()>25)
        {
//            snackbar = Snackbar
//                    .make(cordi, R.string.lname_empty, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
            lname.setError("Limit Exceeded - Last Name");
            lname.requestFocus();


        }
//        else  if(aboutme.getText().toString().trim().length()<10)
//        {
//            snackbar = Snackbar
//                    .make(view, R.string.invalidabout, Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
//            snackbar.show();
//            aboutme.setError("Invalid About Me!");
//
//        }
        else
        {
            return true;
        }
        return false;
    }

    private boolean isNumberValid(String s) {

        return PhoneNumberUtils.isGlobalPhoneNumber(s);

    }


    private boolean isEmail(String s) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
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
    public void DetailsResponseSuccess(UserModel userModel) {
        Log.i("PROFILEINFO"," XXXXXXX Account Fragment");
        dismissLoadingDialog();

        snackbar = Snackbar
                .make(view, "Updated", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void DetailsResponseFailed(String message) {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }

    @Override
    public void changePassResponseSuccess(String message) {
        //snackbar
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();

                snackbar = Snackbar.make(view, "Password Changed Successfully!", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                snackbar.show();
            }});


    }

    @Override
    public void changePassResponseFailed(String message) {
        //snackbar
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() { dismissLoadingDialog();

                snackbar = Snackbar.make(view, "Passwords Change Failed!", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
                snackbar.show();}});
    }

    @Override
    public void failedtoConnect() {
        dismissLoadingDialog();
        snackbar = Snackbar
                .make(view, R.string.failed_connect, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            userModel = (UserModel)intent.getSerializableExtra("userModel");
            if(userModel!=null)Log.d("PROFILEINFO", "Account Got message: " + userModel.getUserFirstName());

            setuserdata();
        }
    };

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroyView();
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
}

package com.bridge.soom.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.bridge.soom.Activity.ChangePaswordActivity;
import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ChangePassResponse;
import com.bridge.soom.R;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment implements ChangePassResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText confirmpass,newpass,oldpass;
    private Button fab;
    private Snackbar snackbar;
    private NetworkManager networkManager;
    private String tocken;
    private View view;
    private ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FRAGINIT","change password tab");
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        networkManager = new NetworkManager(getActivity());
        SharedPreferencesManager.init(getActivity());
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
        tocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");

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
                        networkManager.new SetNewPasswordTask(ChangePasswordFragment.this, tocken, old, newp, "")
                                .execute();

                    }
                }






            }
        });


        // Inflate the layout for this fragment
        return view;
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() { dismissLoadingDialog();

        snackbar = Snackbar.make(view, "Failed to Connect!", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();}});
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
}

package com.bridge.soom.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bridge.soom.Helper.NetworkManager;
import com.bridge.soom.Helper.SharedPreferencesManager;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Model.UserModel;
import com.bridge.soom.R;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileTabFragment extends Fragment implements ProfileFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,
        AboutMeFragment.OnFragmentInteractionListener,ProfessionalFragment.OnFragmentInteractionListener,PersonalFragment.OnFragmentInteractionListener,
        ChangePasswordFragment.OnFragmentInteractionListener,ProviderDetailsResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String tocken = "";
    private String[] tabTitles ;
    private NetworkManager networkManager;
    private UserModel userModel;
    private Bundle bundle;
    private ProgressDialog progress;
    private String typeUsr="";


    private OnFragmentInteractionListener mListener;


    public ProfileTabFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static ProfileTabFragment newInstance(String param1, String param2) {
        ProfileTabFragment fragment = new ProfileTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = new NetworkManager(getActivity());
        tocken = SharedPreferencesManager.read(ACCESS_TOCKEN,"");

        networkManager.new GetProfileDataTask(ProfileTabFragment.this, tocken)
                .execute();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferencesManager.init(getActivity());
        bundle = new Bundle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        typeUsr = this.getArguments().getString("USRTYP");


        Log.i("FRAGINIT","profile tab" +typeUsr);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Account"));
        if(typeUsr.trim().equals("USR")){
            //SEEKER
            tabTitles = new String[]{"Account","Personal","Change Password"};
        }
        else {
//                       provider
            tabTitles = new String[]{"Account", "Professional","Personal","Change Password"};
            tabLayout.addTab(tabLayout.newTab().setText("Professional"));

        }
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Change Password"));
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void DetailsResponseSuccess(UserModel userModel) {
        this.userModel = userModel;
        Log.i("PROFILEINFO"," XXXXXXX Priflile tab fragment  ");
        sendMessage(userModel);


    }

    private void sendMessage(UserModel userModel) {

        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("profile_data");
        // You can also include some extra data.
        intent.putExtra("userModel", userModel);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public void DetailsResponseFailed(String message) {

    }

    @Override
    public void failedtoConnect() {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Fragment fragment1 = new AccountFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("userModel", userModel);
                    fragment1.setArguments(bundle);
                    return fragment1;
                case 1:

                    if(typeUsr.trim().equals("USR")){
                        Fragment fragment3 = new PersonalFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("userModel", userModel);
                        fragment3.setArguments(bundle);
                        return fragment3;
                    }
                    else {
                   Fragment fragment2 = new ProfessionalFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("userModel", userModel);
                        fragment2.setArguments(bundle);
                        return fragment2;
                    }


                case 2:
                    if(typeUsr.trim().equals("USR")){
                        Fragment fragment4 = new  ChangePasswordFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("userModel", userModel);
                        fragment4.setArguments(bundle);
                        return fragment4;
                    }
                    else {
                        Fragment fragment3 = new PersonalFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("userModel", userModel);
                        fragment3.setArguments(bundle);
                        return fragment3;
                    }



                case 3:
                    if(typeUsr.trim().equals("USR")){

                    }
                    else {
                        Fragment fragment4 = new  ChangePasswordFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("userModel", userModel);
                        fragment4.setArguments(bundle);
                        return fragment4;
                    }



                default:
                    Fragment fragment5 = new  AccountFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("userModel", userModel);
                    fragment5.setArguments(bundle);
                    return fragment5;

            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Capturing","onactivty in tab frag");

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}

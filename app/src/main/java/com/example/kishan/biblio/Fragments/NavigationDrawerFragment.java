package com.example.kishan.biblio.Fragments;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.example.kishan.biblio.Adapters.NavDrawAdapter;
import com.example.kishan.biblio.R;

import java.util.ArrayList;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    ActionBarDrawerToggle drawerToggle;
    public DrawerLayout drawerLayout;

    boolean userLearnedDrawer;
    boolean fromSavedInstanceState;

    final String PREF_FILE = "testRef";
    final String KEY_USER_LEARNED_DRAWER ="user_learned_drawer";

    RecyclerView optionsList;
    ArrayList<String> optionsArray;

    NavDrawAdapter adapter;

    View navDrawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState != null){
            fromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rec_view_layout, container, false);


        optionsList = (RecyclerView) view.findViewById(R.id.rvRecView);
        optionsArray = new ArrayList<>();

        getArrayList();

        FragmentManager fm = getActivity().getSupportFragmentManager();

        adapter = new NavDrawAdapter(getActivity(),optionsArray,fm);
        optionsList.setAdapter(adapter);
        optionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void getArrayList() {
        String[] options = {"Have Read","Reading","Want to Read","Advanced Search","Close Library"};
        for (int i=0;i<options.length;i++){
            optionsArray.add(options[i]);
        }
    }

    public void setUp(int ndNavDraw, DrawerLayout drawerFragment, Toolbar myBar) {
        navDrawView = getActivity().findViewById(ndNavDraw);
        drawerLayout = drawerFragment;

        adapter.navDrawView = navDrawView;
        adapter.drawerLayout = drawerLayout;

        drawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,myBar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!userLearnedDrawer){
                    userLearnedDrawer=true;saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,userLearnedDrawer+"");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //Log.d("Offset :", slideOffset + "");
            }
        };

        if(!userLearnedDrawer && !fromSavedInstanceState){
            drawerLayout.openDrawer(navDrawView);
        }

        drawerLayout.setDrawerListener(drawerToggle);

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    public void saveToPreferences(Context c,String preferenceName,String preferenceValue){
        SharedPreferences sharedPreferences = c.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.commit();
    }

    public String readFromPreferences(Context c,String preferenceName,String defaultValue){
        SharedPreferences sharedPreferences = c.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }
}

package com.pdsd.project.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;


@SuppressLint("NewApi")
public class TabListener implements ActionBar.TabListener {
    private Fragment mFragment;
   
   
    public TabListener(Fragment fragment){
    	mFragment = fragment;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	 ft.replace(R.id.fragment_container, mFragment);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    	ft.remove(mFragment);
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}

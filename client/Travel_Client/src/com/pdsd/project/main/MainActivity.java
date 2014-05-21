package com.pdsd.project.main;

import login.FirstActivity;
import login.Session;
import add_location.AddLocationActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity{

	private final static String MY_PLACES="My Places";
	private final static String NEWS_FEED="News Feed";
	ActionBar.Tab myPlacesTab,newsFeedTab;
	MyPlacesFragment myPlacesFragment = new MyPlacesFragment();
	NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
	String selectedTabTag = "com.android.pdsd.prject.selectedTab";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		myPlacesTab = actionBar.newTab();
		newsFeedTab = actionBar.newTab();
		myPlacesTab.setText(MY_PLACES);
		newsFeedTab.setText(NEWS_FEED);

		myPlacesTab.setTabListener(new TabListener(myPlacesFragment));
		newsFeedTab.setTabListener(new TabListener(newsFeedFragment));

		actionBar.addTab(myPlacesTab);
		actionBar.addTab(newsFeedTab);

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		int selectedTab = sharedPref.getInt(this.selectedTabTag, 0);
		actionBar.setSelectedNavigationItem(selectedTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public static void goToFirstActivity(Activity act) {
		Intent intent = new Intent(act, FirstActivity.class);
		act.startActivity(intent);
    	act.finish();
    }
	
	public static void goToAddLocation(Activity act){
		Intent intent = new Intent(act, AddLocationActivity.class);
		act.startActivity(intent);
	}
	
	public void switchToMapView(View v){
		Intent intent = new Intent(this,MapViewActivity.class);
		startActivity(intent);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.search) {
			ActionBar actionBar = getActionBar();
			int selectedTab = actionBar.getSelectedNavigationIndex();
			
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(selectedTabTag, selectedTab);
			editor.commit();

			this.recreate();
		} else if (itemId == R.id.action_logout) {
			Session.LogOut(this);
			goToFirstActivity(this);
		} else if (itemId == R.id.add_location) {
			goToAddLocation(this);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}

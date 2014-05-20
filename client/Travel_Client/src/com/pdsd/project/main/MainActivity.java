package com.pdsd.project.main;

import login.FirstActivity;
import login.Session;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void goToFirstActivity() {
		Intent intent = new Intent(this, FirstActivity.class);
		startActivity(intent);
    	finish();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.search:
			break;
		case R.id.action_logout:
			Session.LogOut(this);
			goToFirstActivity();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}

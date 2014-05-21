package location_info_page;

import login.Session;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pdsd.project.main.MainActivity;
import com.pdsd.project.main.R;
import com.pdsd.project.main.TabListener;

public class LocationActivity extends Activity {

	ActionBar.Tab generalTab,updatesTab,ratingTab;
	GeneralFragment generalFragment = new GeneralFragment();
	UpdateFragment updateFragment = new UpdateFragment();
	RatingFragment ratingFragment = new RatingFragment();
	String selectedTabTag = "com.android.pdsd.prject.selectedTab";
	
	/* Location informations */
	public static String locationName = null;
	public static String createdBy = null;
	public static String createdData = null;
	public static String rating = null;
	public static String description = null;
	public static String subscriptionStatus = null;
	public static String locationId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		Intent intent = getIntent();
		locationId = intent.getStringExtra("location_id");
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		generalTab = actionBar.newTab();
		updatesTab = actionBar.newTab();
		ratingTab = actionBar.newTab();
		
		generalTab.setText("General");
		updatesTab.setText("Updates");
		ratingTab.setText("Rating");

		generalTab.setTabListener(new TabListener(generalFragment));
		updatesTab.setTabListener(new TabListener(updateFragment));
		ratingTab.setTabListener(new TabListener(ratingFragment));
		
		actionBar.addTab(generalTab);
		actionBar.addTab(updatesTab);
		actionBar.addTab(ratingTab);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		int selectedTab = sharedPref.getInt(this.selectedTabTag, 0);
		actionBar.setSelectedNavigationItem(selectedTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public static void onPostExecute(Activity act, JSONObject result) {		
		try {
			locationName = result.getString("location_name");
			createdBy = result.getString("username");
			createdData = result.getString("creation_date");
			rating = result.getString("rating");
			description = result.getString("description");
			subscriptionStatus = result.getString("subscription");
		
			if(subscriptionStatus.equals("You are subscribed to this location")){
					act.findViewById(R.id.favorite).setVisibility(View.GONE);
					act.findViewById(R.id.unsubscribe).setVisibility(View.VISIBLE);
				
			}
			
			TextView locationNameField = (TextView) act.findViewById(R.id.locationname);
			TextView usernameField = (TextView) act.findViewById(R.id.username);
			TextView dateField = (TextView) act.findViewById(R.id.data);
			TextView ratingAvgField = (TextView) act.findViewById(R.id.avg);
			TextView descriptionField = (TextView) act.findViewById(R.id.descriptiontext);
			TextView subscriptionStat = (TextView) act.findViewById(R.id.subscription_status);
			
			locationNameField.setText(locationName);
			usernameField.setText(createdBy);
			dateField.setText(createdData);
			ratingAvgField.setText(rating);
			descriptionField.setText(description);
			subscriptionStat.setText(subscriptionStatus);
			
			LinearLayout location_info = (LinearLayout) act.findViewById(R.id.location_info);
			location_info.setVisibility(View.VISIBLE);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        this.finish();
	        return true;
	    case R.id.action_logout:
	    	Session.LogOut(this);
	    	MainActivity.goToFirstActivity(this);
	    	return true;
	    case R.id.add_location:
	    	MainActivity.goToAddLocation(this);
	    	return true;
	    case R.id.search:
			ActionBar actionBar = getActionBar();
			int selectedTab = actionBar.getSelectedNavigationIndex();
			
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(selectedTabTag, selectedTab);
			editor.commit();

			this.recreate();
			return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}

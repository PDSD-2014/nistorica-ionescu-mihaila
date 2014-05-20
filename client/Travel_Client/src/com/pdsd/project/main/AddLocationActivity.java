package com.pdsd.project.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import location.LocationActivity;
import login.Session;

import comunication.ServerTaskObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class AddLocationActivity extends Activity {

	EditText locName,locDescript,locLat,locLon,photoURL;
	Button getCurrent,openMap,addLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		// Show the Up button in the action bar.
		setupActionBar();
		
		locName = (EditText) findViewById(R.id.add_location_name);
		locDescript = (EditText) findViewById(R.id.add_location_description);
		locLat = (EditText) findViewById(R.id.add_location_latitude);
		locLon = (EditText) findViewById(R.id.add_location_longitude);
		photoURL = (EditText) findViewById(R.id.add_photo_addr);
		
		getCurrent = (Button) findViewById(R.id.get_current_button);
		openMap = (Button) findViewById(R.id.select_on_map);
		addLocation = (Button) findViewById(R.id.add_location_button);
		
		getCurrent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		openMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void addLocationClick (View v){
		String name,lat,lon,desc,url;
		name = locName.getText().toString();
		lat = locLat.getText().toString();
		lon = locLon.getText().toString();
		desc = locDescript.getText().toString();
		url = photoURL.getText().toString();
		try {
			name = URLEncoder.encode(name,"UTF-8");
			lat = URLEncoder.encode(lat,"UTF-8");
			lon = URLEncoder.encode(lon,"UTF-8");
			desc = URLEncoder.encode(desc,"UTF-8");
			url = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Map<String,String> arg = new HashMap<String,String>();
		arg.put("type","6");
		arg.put("latitude",lat);
		arg.put("longitude",lon);
		arg.put("location_name",name);
		arg.put("description",desc);
		arg.put("image_url",url);
		arg.put("user_id",Session.getUserId(this));
		
		ServerTaskObject serverTaskObj = new ServerTaskObject("post_new.php",arg,this);
		serverTaskObj.execute();
		
	}
	
	public static void onPostExecute(Activity act, JSONObject obj){
		try{
			Common.printError(act, obj.getString("message"));
		}catch(JSONException e){e.printStackTrace();}
		
	}
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

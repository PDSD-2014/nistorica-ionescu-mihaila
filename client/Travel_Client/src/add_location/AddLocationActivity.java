package add_location;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import login.Session;

import comunication.ServerTaskObject;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import comunication.ServerTaskObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;
import comunication.ServerTaskObject;


public class AddLocationActivity extends Activity implements
			GooglePlayServicesClient.ConnectionCallbacks,
			GooglePlayServicesClient.OnConnectionFailedListener{

	EditText locName,locDescript,locLat,locLon,photoURL;
	Button getCurrent,openMap,addLocation;
	LocationClient mLocClient;
    Location mCurrentLocation;
    
    static int MAP_RESULT = 5555;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		// Show the Up button in the action bar.
		setupActionBar();

		mLocClient = new LocationClient(this,this,this);
		
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
				mCurrentLocation = mLocClient.getLastLocation();
				double la = mCurrentLocation.getLatitude();
				double lo = mCurrentLocation.getLongitude();
				
				locLat.setText(String.valueOf(la));
				locLon.setText(String.valueOf(lo));
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
	
	public void openMapClick(View v){
		
		Intent intent = new Intent(this, MapSelectActivity.class);
		startActivityForResult(intent,MAP_RESULT);
		
	}
	
	public static void onPostExecute(Activity act, JSONObject obj){
		try{
			Common.printError(act, obj.getString("message"));
		}catch(JSONException e){e.printStackTrace();}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case (5555) :{
			
			if(resultCode == Activity.RESULT_OK){
				double la = data.getDoubleExtra("lat", 0.0);
				double lo = data.getDoubleExtra("lon", 0.0);
				
				locLat.setText(String.valueOf(la));
				locLon.setText(String.valueOf(lo));
			}
		}break;
		
		}
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
	@Override
	protected void onStart() {
		super.onStart();
		mLocClient.connect();

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected from locations services", 
				Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

}

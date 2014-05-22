package add_location;

import login.Session;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pdsd.project.main.MainActivity;
import com.pdsd.project.main.R;

public class MapSelectActivity extends Activity {

	GoogleMap gMap;
	double lastLat, lastLon;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_map_select);		
		
		MapFragment smp = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = smp.getMap();
		LatLng pos = new LatLng(44.4167, 26.1000);
		gMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
		
		gMap.setOnMapClickListener(new OnMapClickListener() {
		
			@Override
			public void onMapClick(LatLng arg0) {
				
				lastLat = arg0.latitude;
				lastLon = arg0.longitude;
				MarkerOptions mO = new MarkerOptions();
				mO.position(arg0);
				mO.title(arg0.latitude + " : " + arg0.longitude);
				
				gMap.clear();
                
                gMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
                
                gMap.addMarker(mO);
								
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case android.R.id.home:
			Intent result = new Intent();
			
			result.putExtra("lat", lastLat);
			result.putExtra("lon", lastLon);
			
			setResult(Activity.RESULT_OK,result);
			
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
			this.recreate();
			return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
}

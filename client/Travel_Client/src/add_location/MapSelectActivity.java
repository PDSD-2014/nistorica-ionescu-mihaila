package add_location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
	}
	
	@Override
	public void onBackPressed() {
		
		// TODO Auto-generated method stub
		Intent result = new Intent();
		
		result.putExtra("lat", lastLat);
		result.putExtra("lon", lastLon);
		
		setResult(Activity.RESULT_OK,result);
		
		finish();

	}
}

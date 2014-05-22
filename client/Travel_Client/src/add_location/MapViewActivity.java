package add_location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import location_info_page.LocationActivity;
import login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pdsd.project.main.MainActivity;
import com.pdsd.project.main.R;
import comunication.ServerTask;

public class MapViewActivity extends Activity {
	GoogleMap gMap;
	static MapFragment smp;
	static ArrayList<Marker> markers = new ArrayList<Marker>();
	static ArrayList<Integer> ids = new ArrayList<Integer>();
	Activity context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		smp = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = smp.getMap();
		LatLng pos = new LatLng(44.4167, 26.1000);
		gMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
		
		Map<String,String> arg = new HashMap<String,String>();
		arg.put("type","2");
		
		ServerTask serverTask = new ServerTask("get_location_info.php",arg,this);
		serverTask.execute();
		
		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub
				int location_id=5;
				for (int i=0;i<markers.size();i++){
					if (arg0.equals(markers.get(i))){
						location_id = ids.get(i).intValue();
						Log.d("ID"," "+location_id);
				    	Intent intent = new Intent(context, LocationActivity.class);
				    	intent.putExtra("location_id",String.valueOf(location_id));
						 context.startActivity(intent);
						 break;
					}
				}
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	static void updateMap(MarkerOptions marker , Integer l){

			Marker m = smp.getMap().addMarker(marker);
			markers.add(m);
			ids.add(l);

	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
		
		markers.clear();
		ids.clear();
		for (int i=0;i<result.length();i++){
			
			try {
				JSONObject obj = result.getJSONObject(i);
				
				Log.d("json2",obj.toString());
				MarkerOptions mO = new MarkerOptions();
				mO.position(new LatLng( Double.valueOf(obj.getString("latitude")),
									Double.valueOf(obj.getString("longitude"))));
				mO.title(obj.getString("location_name"));
				mO.snippet(obj.getString("description"));
				
				updateMap(mO,Integer.valueOf(obj.getString("location_id")));
                
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

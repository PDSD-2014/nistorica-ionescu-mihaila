package location_info_page;

import java.util.HashMap;
import java.util.Map;

import login.Session;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;
import comunication.ServerTaskObject;

public class GeneralFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ScrollView generalLayout = (ScrollView) inflater.inflate(R.layout.general_info_loc, container,false);		
		return generalLayout;

	}
	
	@Override
	public void onResume() {
		super.onResume();

		Button addToMyPlaces = (Button) getActivity().findViewById(R.id.favorite);
		
		addToMyPlaces.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Map<String,String> arguments = new HashMap<String,String>();
				arguments.put("type", "3");
				arguments.put("location_id", LocationActivity.locationId);
				arguments.put("user_id", Session.getUserId(getActivity()));
				ServerTaskObject serverTask = new ServerTaskObject("post_new.php", arguments, getActivity());
				serverTask.execute();
			}
		});
		
		Button unsubscribe = (Button) getActivity().findViewById(R.id.unsubscribe);
		
		unsubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Map<String,String> arguments = new HashMap<String,String>();
				arguments.put("type", "4");
				arguments.put("location_id", LocationActivity.locationId);
				arguments.put("user_id", Session.getUserId(getActivity()));
				ServerTaskObject serverTask = new ServerTaskObject("post_new.php", arguments, getActivity());
				serverTask.execute();
				
			}
		});
		Map<String,String> arguments = new HashMap<String,String>();
		arguments.put("location_id", LocationActivity.locationId);
		arguments.put("type", "1");
		arguments.put("user_id", Session.getUserId(getActivity()));
		ServerTaskObject serverTask = new ServerTaskObject("get_location_info.php", arguments, getActivity());
		serverTask.execute();
	}
	
	public static void onPostExecute(Activity act, JSONObject result) {
		try {
			Common.printError(act, result.getString("message"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}

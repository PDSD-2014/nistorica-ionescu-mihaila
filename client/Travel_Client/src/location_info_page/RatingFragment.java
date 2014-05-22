package location_info_page;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;

import comunication.ServerTask;
import comunication.ServerTaskObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import login.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class RatingFragment extends Fragment {
    
	static LinearLayout ratingScrollView;
	Spinner spinnerRating;
	static JSONArray jsonLocalCopy;
	static RatingListAdapter ratingAdapter;
	static Activity localAct;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    
		ratingScrollView = (LinearLayout) inflater.inflate(R.layout.rating_loc_list, container, false);	
		return ratingScrollView;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		spinnerRating = (Spinner) ratingScrollView.findViewById(R.id.spinner);
		List<String> rates = new ArrayList<String>();
		String[] ratesStr = {"1","2","3","4","5","6","7","8","9","10"};
		rates = Arrays.asList(ratesStr);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,rates);
		spinnerRating.setAdapter(spinnerAdapter);

		Button voteButton = (Button) ratingScrollView.findViewById(R.id.rate);
		voteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				String comentariu =((EditText) ratingScrollView.findViewById(R.id.addDescription)
									).getText().toString();
				String value=spinnerRating.getSelectedItem().toString() ;
				
				try {
					comentariu=URLEncoder.encode(comentariu,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				Map<String,String> arg = new HashMap<String,String>();
				arg.put("type","2");
				arg.put("rated_entity_id",LocationActivity.locationId);
				arg.put("rating_type","1");
				arg.put("value",value);
				arg.put("body",comentariu);
				arg.put("user_id",Session.getUserId(getActivity()));
				
				JSONObject jsonObj= new JSONObject();
				try {
					jsonObj.put("value", value);
					jsonObj.put("body", comentariu);
					jsonObj.put("username", Session.getUserName(getActivity()));
					jsonObj.put("rating_date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
					JSONObject previous = (JSONObject)jsonLocalCopy.get(0);
					JSONObject previousCopy;
					jsonLocalCopy.put(0, jsonObj);
					for (int i = 1; i < jsonLocalCopy.length();i++){
						previousCopy = previous;
						previous = (JSONObject)jsonLocalCopy.get(i);
						jsonLocalCopy.put(i, previousCopy);						
					}
					jsonLocalCopy.put(previous);
					ratingAdapter = new RatingListAdapter(localAct,jsonLocalCopy);
					ListView ratingList = (ListView) localAct.findViewById(R.id.rating_list);
					ratingList.setAdapter(ratingAdapter);
					ratingAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ServerTaskObject serverTaskObj = new ServerTaskObject("post_new.php",arg,getActivity());
				serverTaskObj.execute();
			}
		});
		Map<String,String> arg = new HashMap<String,String>();
		arg.put("type","2");
		arg.put("location_id",LocationActivity.locationId);
		
		ServerTask serverTask = new ServerTask("get_rating.php",arg,getActivity());
		serverTask.execute();
	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
			ListView ratingList = (ListView) act.findViewById(R.id.rating_list);
			if (ratingList != null) {
				jsonLocalCopy = result;
				localAct = act;
				ratingAdapter = new RatingListAdapter(act, jsonLocalCopy);
				ratingList.setAdapter(ratingAdapter);
				
			}
	}

	public static void onPostExecute(Activity act, JSONObject obj){
			try{
				Common.printError(act, obj.getString("message"));
				
			} catch(JSONException e){
				e.printStackTrace();
			}		
	}

}

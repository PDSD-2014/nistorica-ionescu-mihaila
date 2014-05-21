package com.pdsd.project.main;

import java.util.HashMap;
import java.util.Map;

import login.Session;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import comunication.ServerTask;



public class MyPlacesFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.loading_screen, container, false);
		return view;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
		Map<String,String> arguments = new HashMap<String,String>();
		arguments.put("type", "1");
		arguments.put("user_id", Session.getUserId(getActivity()));
		
		ServerTask serverTask = new ServerTask("get_update.php", arguments, getActivity());
		serverTask.execute();
	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
		
		ListView myPlacesListView = (ListView) act.findViewById(R.id.list);
		if ( (result != null) && (myPlacesListView != null) ) {
			myPlacesListView.setAdapter(new MyPlacesListAdapter(act, result));
		}

	}
}

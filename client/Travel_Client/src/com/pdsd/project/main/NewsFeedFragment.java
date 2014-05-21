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
import android.widget.LinearLayout;
import android.widget.ListView;

import comunication.ServerTask;


public class NewsFeedFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout listLayout = (LinearLayout) inflater.inflate(R.layout.news_feed_list, container, false);
		Map<String,String> arguments = new HashMap<String,String>();
		arguments.put("type", "2");
		arguments.put("user_id", Session.getUserId(getActivity()));
		ServerTask serverTask = new ServerTask("get_update.php", arguments, getActivity());
		serverTask.execute();
		return listLayout;
	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
		ListView myPlacesListView = (ListView) act.findViewById(R.id.news_feed_list);

		if ( (myPlacesListView != null) && (result != null) ) {	
			myPlacesListView.setAdapter(new MyPlacesListAdapter(act,result));
		}
	}
}

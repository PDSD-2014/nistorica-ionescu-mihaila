package com.pdsd.project.main;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location_info_page.RatingListAdapter;
import login.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import comunication.ServerTask;
import comunication.ServerTaskObject;

public class UserProfileActivity extends Activity {
	Spinner spinnerRating;
	public static boolean isButtonClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		final String user_id = intent.getStringExtra("username");
		
		spinnerRating = (Spinner) findViewById(R.id.userSpinner);
		List<String> rates = new ArrayList<String>();
		String[] ratesStr = {"1","2","3","4","5","6","7","8","9","10"};
		rates = Arrays.asList(ratesStr);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,rates);
		spinnerRating.setAdapter(spinnerAdapter);
		
		final Activity actv= this;
		Button voteButton = (Button) findViewById(R.id.rateUser);
		voteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				String comentariu = ((EditText) findViewById(R.id.addUserDescription)).getText().toString();
				String value = spinnerRating.getSelectedItem().toString() ;
				
				try {
					comentariu=URLEncoder.encode(comentariu,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				Map<String,String> arg = new HashMap<String,String>();
				arg.put("type","5");
				arg.put("rated_entity_id",user_id);
				arg.put("rating_type","0");
				arg.put("value",value);
				arg.put("body",comentariu);
				arg.put("user_id",Session.getUserId(actv));
				
				ServerTaskObject serverTaskObj = new ServerTaskObject("post_new.php",arg,actv);
				serverTaskObj.execute();
				UserProfileActivity.isButtonClicked = true;
			}
		});
		
		Map<String,String> arguments = new HashMap<String,String>();
		arguments.put("uid", user_id);
		ServerTaskObject serverTask = new ServerTaskObject("get_user_info.php", arguments, this);
		serverTask.execute();	
		
		Map<String,String> arg = new HashMap<String,String>();
		arg.put("type","1");
		arg.put("user_id",user_id);
		
		ServerTask serverTask2 = new ServerTask("get_rating.php",arg,this);
		serverTask2.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
		ListView ratingList = (ListView) act.findViewById(R.id.userPostsList);
		ratingList.setAdapter(new RatingListAdapter(act, result));
	}
	
	public static void onPostExecute(Activity act, JSONObject result) {
		View view = act.findViewById(R.id.container);
		view.setVisibility(View.VISIBLE);
		view = act.findViewById(R.id.container_comment);
		view.setVisibility(View.VISIBLE);
		
		TextView userField = (TextView) act.findViewById(R.id.user);
		TextView ratingField = (TextView) act.findViewById(R.id.rating_value);
		TextView numeField = (TextView) act.findViewById(R.id.nume);
		TextView prenumeField = (TextView) act.findViewById(R.id.prenume);
		TextView mailField = (TextView) act.findViewById(R.id.mail);
		
		try {
			if (!result.getString("status").equals("error")) {
				JSONObject message = result.getJSONObject("message");

				String username = message.getString("username");
				String nume = message.getString("nume");
				String prenume = message.getString("prenume");
				String mail = message.getString("mail");
				String rating = message.getString("rating");

				userField.setText(username);
				ratingField.setText(rating);
				numeField.setText(nume);
				prenumeField.setText(prenume);
				mailField.setText(mail);
			}
			else {
				Common.printError(act, "Could not retrive user information");
			}
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
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}


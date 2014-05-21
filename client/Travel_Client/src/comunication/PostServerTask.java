package comunication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import login.FirstActivity;
import login.Session;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;


public class PostServerTask extends AsyncTask<Void, Void, JSONObject> {
	
	private String URL="http://54.72.182.209/server/";
	private String functionName;
	private ArrayList<NameValuePair> parameters;
	private HttpResponse response;
	
	/* Caller activity */
	private Activity act = null;

	
	String errorMessage = null;
	
	HttpPost httpPost = null;
	
	/* Progress bar */
	LinearLayout loader;
	
	/* Result */
	JSONObject finalResult = new JSONObject();
	
	public PostServerTask(String method, ArrayList<NameValuePair> arg, Activity act){
		this.functionName = method;
		this.parameters = arg;
		this.act = act;
		this.loader = (LinearLayout) act.findViewById(R.id.loader);
		this.loader.setVisibility(View.VISIBLE);
	}

	private String URLBuild(){
		StringBuilder str = new StringBuilder(URL);
		str.append(functionName);
		return str.toString();
	}
	
	@Override
	protected void onPreExecute () {
		if (!connectionTest.isConnected(act)) {
			this.cancel(true);
		}
	}
	
	@Override
	protected JSONObject doInBackground(Void... arg0) {
		
		String url = URLBuild();

		HttpClient httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			return null;
		}
		
		StringBuilder content = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String line;
			while (null != (line = reader.readLine())) {
			    content.append(line);
			}
		} catch (Exception e) {
			return null;
		}

		JSONTokener tokener = new JSONTokener(content.toString());
		finalResult = new JSONObject();
		try {
			finalResult = new JSONObject(tokener);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return finalResult;
	}
	
	@Override
    protected void onPostExecute(JSONObject obj) {

		String status = null, message = null;
		try {
			status = finalResult.getString("status");
			message = finalResult.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (this.functionName == "login.php") {
			if (!status.equals("error")) {
				// Save user id
		    	SharedPreferences sharedPref = act.getSharedPreferences(Session.PREFS_NAME, 0);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(Session.USERID, message);
		    	editor.putString(Session.USERNAME, parameters.get(0).getValue());
		    	editor.commit();
		    	FirstActivity.goToMainActivity(act);
			} else {
				Common.printError(act, message);
			}
		}
		else if (this.functionName == "register.php") {
			Common.printError(act, message);
		}
		this.loader.setVisibility(View.GONE);
	}
	
	@Override
	protected void onCancelled (JSONObject obj) {

		if (errorMessage != null) {
			Common.printError(act, errorMessage);
		}
		this.loader.setVisibility(View.GONE);
	}

}

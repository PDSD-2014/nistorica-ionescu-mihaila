package comunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import location_info_page.RatingFragment;
import location_info_page.UpdateFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import add_location.MapViewActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.MyPlacesFragment;
import com.pdsd.project.main.NewsFeedFragment;
import com.pdsd.project.main.R;
import com.pdsd.project.main.UserProfileActivity;


public class ServerTask extends AsyncTask<Void, Void, JSONArray> {
	
	private String URL="http://54.72.182.209/server/";
	private String functionName;
	private Map<String, String> parameters;
	private HttpResponse response;
	
	/* Caller activity */
	private Activity act = null;
	
	String errorMessage = null;
	
	HttpGet httpPost = null;
	
	/* Result */
	JSONArray finalResult = new JSONArray();
	
	/* Progress bar */
	LinearLayout loader;
	
	public ServerTask(String method,Map<String, String> arg, Activity act){
		this.functionName = method;
		this.parameters = arg;
		this.act = act;
		this.loader = (LinearLayout) act.findViewById(R.id.loader);
		this.loader.setVisibility(View.VISIBLE);
	}

	private String URLBuild(){
		StringBuilder str = new StringBuilder(URL);
		str.append(functionName).append("?");
		for (Map.Entry<String, String> entry : parameters.entrySet()){
			str.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		str.delete(str.length()-1, str.length());
		return str.toString();
	}
	
	@Override
	protected void onPreExecute () {

		if (!connectionTest.isConnected(act)) {
			this.cancel(true);
		}

	}
	
	@Override
	protected JSONArray doInBackground(Void... arg0) {
		
		String url = URLBuild();
		Log.d("url", url);
		StringBuilder content = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String line;
			while (null != (line = reader.readLine())) {
			    content.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		JSONTokener tokener = new JSONTokener(content.toString());
		try {
			finalResult = new JSONArray(tokener);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return finalResult;
	}

	@Override
    protected void onPostExecute(JSONArray obj) {
		if (this.functionName.equals("get_update.php")) {
			String type = this.parameters.get("type");
			if (type.equals("1")) {
				MyPlacesFragment.onPostExecute(act, this.finalResult);
			}
			else if (type.equals("2")) {
				NewsFeedFragment.onPostExecute(act, this.finalResult);
			}
			else if (type.equals("3")){
				UpdateFragment.onPostExecute(act, this.finalResult);
			}
		}else if (this.functionName.equals("get_rating.php")){
			String type = this.parameters.get("type");
			
			if(type.equals("1")){
				UserProfileActivity.onPostExecute(act,this.finalResult);
			}else if (type.equals("2")) {
				RatingFragment.onPostExecute(act,this.finalResult);
			}
		}else if (this.functionName.equals("get_location_info.php")) {
			
			String type = this.parameters.get("type");
			if(type.equals("2")){
				MapViewActivity.onPostExecute(act, this.finalResult);
			}
		}
		this.loader.setVisibility(View.GONE);
	}
	
	@Override
	protected void onCancelled (JSONArray obj) {

		if (errorMessage != null) {
			Common.printError(act, errorMessage);
		}
		this.loader.setVisibility(View.GONE);
	}

}

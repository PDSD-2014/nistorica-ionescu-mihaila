package comunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import location_info_page.GeneralFragment;
import location_info_page.LocationActivity;
import location_info_page.RatingFragment;
import location_info_page.UpdateFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;
import com.pdsd.project.main.UserProfileActivity;


public class ServerTaskObject extends AsyncTask<Void, Void, JSONObject> {
	
	private String URL="http://54.72.182.209/server/";
	private String functionName;
	private Map<String, String> parameters;
	private HttpResponse response;
	
	/* Caller activity */
	private Activity act = null;
	
	String errorMessage = null;
	
	HttpGet httpPost = null;
	
	/* Result */
	JSONObject finalResult = new JSONObject();
	
	/* Progress bar */
	LinearLayout loader;
	
	public ServerTaskObject(String method,Map<String, String> arg, Activity act){
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
	protected JSONObject doInBackground(Void... arg0) {

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
			finalResult = new JSONObject(tokener);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return finalResult;
	}

	@Override
    protected void onPostExecute(JSONObject obj) {
		if (this.functionName.equals("get_location_info.php")) {
			LocationActivity.onPostExecute(act, this.finalResult);
		}
		else if (this.functionName.equals("get_user_info.php")) {
			UserProfileActivity.onPostExecute(act, this.finalResult);
		}
		else if (this.functionName.equals("post_new.php")) {
			String type = this.parameters.get("type");
			
			if((type.equals("3") || type.equals("4"))){
				GeneralFragment.onPostExecute(act, this.finalResult);
			}
			else if(type.equals("2")){
				
				RatingFragment.onPostExecute(act,this.finalResult);
			}
			else if(type.equals("1")){
				UpdateFragment.onPostExecute(act, this.finalResult);
			} else if (type.equals("5")) {
				if (UserProfileActivity.isButtonClicked) {
					UserProfileActivity.isButtonClicked = false;
					act.recreate();
				}
			}
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

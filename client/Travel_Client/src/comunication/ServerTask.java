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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.MyPlacesFragment;
import com.pdsd.project.main.NewsFeedFragment;
import com.pdsd.project.main.UserProfileActivity;


public class ServerTask extends AsyncTask<Void, Void, JSONArray> {
	
	private String URL="http://54.72.182.209/server/";
	private String functionName;
	private Map<String, String> parameters;
	private HttpResponse response;
	
	/* Loading message */
	private ProgressDialog progress = null;
	private String dialogTitle = null;
	
	/* Caller activity */
	private Activity act = null;
	
	/* Task */
	private ServerTask task = null;
	
	String errorMessage = null;
	
	HttpGet httpPost = null;
	
	/* Result */
	JSONArray finalResult = new JSONArray();
	
	public ServerTask(String method,Map<String, String> arg, Activity act){
		this.functionName = method;
		this.parameters = arg;
		this.act = act;
		this.task = this;
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

		/* Initialize Progress Dialog */
		progress = new ProgressDialog(act);
		
		if (this.dialogTitle != null) {
			progress.setTitle(this.dialogTitle);
		}
		
		progress.setMessage("Waiting for data from server ...");
		
		progress.setIndeterminate(true);
		progress.setCancelable(false);
		
		/* Button Cancel */
		progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {

		        /* Cancel task */
		    	if (task != null) {
		    		task.cancel(true);
		    	}

		        /* Cancel http post */
		        if (httpPost != null) {
		        	httpPost.abort();
		        }
		        errorMessage = "Action canceled.";
		    }
		});
		
		progress.show();
		
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
			
		}
		progress.dismiss();
	}
	
	@Override
	protected void onCancelled (JSONArray obj) {
			
		progress.dismiss();
		if (errorMessage != null) {
			Common.printError(act, errorMessage);
		}
		
	}

}

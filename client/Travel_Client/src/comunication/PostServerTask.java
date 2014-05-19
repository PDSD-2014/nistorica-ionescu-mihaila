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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.pdsd.project.main.Common;


public class PostServerTask extends AsyncTask<Void, Void, JSONObject> {
	
	private String URL="http://54.72.182.209/server/";
	private String functionName;
	private ArrayList<NameValuePair> parameters;
	private HttpResponse response;
	
	/* Loading message */
	private ProgressDialog progress = null;
	private String dialogTitle = null;
	
	/* Caller activity */
	private Activity act = null;
	
	/* Task */
	private PostServerTask task = null;
	
	String errorMessage = null;
	
	HttpPost httpPost = null;
	
	/* Result */
	JSONObject finalResult = new JSONObject();
	
	public PostServerTask(String method, ArrayList<NameValuePair> arg, Activity act){
		this.functionName = method;
		this.parameters = arg;
		this.act = act;
		this.task = this;
	}

	private String URLBuild(){
		StringBuilder str = new StringBuilder(URL);
		str.append(functionName);
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
		    	editor.commit();
		    	FirstActivity.goToMainActivity(act);
			} else {
				Common.printError(act, message);
			}
		}
		else if (this.functionName == "register.php") {
			Common.printError(act, message);
		}
		progress.dismiss();
	}
	
	@Override
	protected void onCancelled (JSONObject obj) {
			
		progress.dismiss();
		if (errorMessage != null) {
			Common.printError(act, errorMessage);
		}
		
	}

}

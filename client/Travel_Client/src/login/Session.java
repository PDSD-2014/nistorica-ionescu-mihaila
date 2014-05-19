package login;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;

import comunication.PostServerTask;

public class Session {
	public static final String USERID = "user_id";
	public static final String PREFS_NAME = "user_info";
	
	public static boolean isLogedIn(Activity ctx) {
    	SharedPreferences sharedPref = ctx.getSharedPreferences(Session.PREFS_NAME, 0);
    	String user_id = sharedPref.getString(Session.USERID, null);
    	if (user_id == null) {
    		return false;
    	}
		return true;
	}
	
	public static void LogIn(ArrayList<InputObject> input, Activity act) {
		
		String username = input.get(0).InputText;
		String password = input.get(1).InputText;

		ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
		arguments.add(new BasicNameValuePair("username", username));
		arguments.add(new BasicNameValuePair("pass", password));
		PostServerTask serverTask = new PostServerTask("login.php", arguments, act);
		serverTask.execute();
	}
	
	public static void LogOut(Activity ctx) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(Session.PREFS_NAME, 0);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.remove(Session.USERID);
    	editor.commit();
	}
	
	public static String getUserId(Activity act) {
		SharedPreferences sharedPref = act.getSharedPreferences(Session.PREFS_NAME, 0);
		return sharedPref.getString(Session.USERID, null);
	}

}

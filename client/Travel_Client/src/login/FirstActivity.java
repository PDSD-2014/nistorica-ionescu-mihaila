package login;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.MainActivity;
import com.pdsd.project.main.R;
import comunication.PostServerTask;

public class FirstActivity extends Activity {
	
	ActionBar actionBar = null;

    public void onCreate(Bundle savedInstanceState) {	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_first);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (Session.isLogedIn(this)) {
    		FirstActivity.goToMainActivity(this);
    	}
    		
		FragmentManager fMan = getFragmentManager();
    	FragmentTransaction fTran = fMan.beginTransaction();
		
		actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Tab tab1 = actionBar.newTab()
	            .setText("Log in")
	            .setTabListener(new LoginTabListener<LoginFragment>(
	                    this, "Log in", LoginFragment.class));
	    actionBar.addTab(tab1);
	    
	    Tab tab2 = actionBar.newTab()
	            .setText("Register")
	            .setTabListener(new LoginTabListener<RegisterFragment>(
	                    this, "Register", RegisterFragment.class));
	    actionBar.addTab(tab2);    		
		
		fTran.commit();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if (actionBar != null)
    		actionBar.removeAllTabs();
    }
    
    public static void goToMainActivity(Activity act) {	
		Intent intent = new Intent(act, MainActivity.class);
		act.startActivity(intent);
		act.finish();
    }

    /* On click for Log in fragment */
	public void SubmitLogIn(View view) {
		
		/* Close keyboard */
		Common.closeKeyboard(this);
		
		/* Specify inputs */
		InputObjectArray input = new InputObjectArray();
		
		input.add(new InputObject( this, R.id.Username, "Username"));
		input.add(new InputObject( this, R.id.Password, "Password"));

		/* If valid inputs send user and pass */
		if (input.isValid()) {
			Session.LogIn(input, this);
		}
		
		/* If Remember username checkBox is check save username */
		final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
        	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        	SharedPreferences.Editor editor = sharedPref.edit();
        	editor.putString("username", input.get(0).InputText);
        	editor.commit();
        } else {
        	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        	SharedPreferences.Editor editor = sharedPref.edit();
        	editor.remove("username");
        	editor.commit();
        }
	}
	
	/* On click for Sign Up fragment */
	public void SubmitSignUp(View view) {
		
		/* Close keyboard */
		Common.closeKeyboard(this);
	
		/* Specify inputs */
		InputObjectArray input = new InputObjectArray();
		
		input.add(new InputObject( this, R.id.newUsername, "New Username"));
		input.add(new InputObject( this, R.id.newPass, "New Password"));
		input.add(new InputObject( this, R.id.retypePass, "Re-enter password"));
		input.add(new InputObject( this, R.id.name, "Name"));
		input.add(new InputObject( this, R.id.surname, "Surname"));
		input.add(new InputObject( this, R.id.mail, "E-mail address"));
		
		if (input.isValid()) {
			if ( !input.get(1).InputText.equals( input.get(2).InputText) ) {
				Common.printError(this, "Passwords do not match.");
				return;
			}
			/* Send data to server */
			ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("username", input.get(0).InputText));
			arguments.add(new BasicNameValuePair("pass", input.get(1).InputText));
			arguments.add(new BasicNameValuePair("prenume", input.get(3).InputText));
			arguments.add(new BasicNameValuePair("nume", input.get(4).InputText));
			arguments.add(new BasicNameValuePair("mail", input.get(5).InputText));
			PostServerTask serverTask = new PostServerTask("register.php", arguments, this);
			serverTask.execute();
		}
	}


}
package login;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pdsd.project.main.R;

public class LoginFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		
		View view =  inflater.inflate(R.layout.activity_login_activity, container, false);
		
		/* Print remembered username */
    	SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    	String username = sharedPref.getString("username", null);
    	
    	if (username != null) {

    		EditText input = ((EditText)view.findViewById(R.id.Username));
    		if (input != null)
    			input.setText(username);
    	}
		
		return view;
	}

}

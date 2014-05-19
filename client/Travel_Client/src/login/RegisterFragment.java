package login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pdsd.project.main.R;

public class RegisterFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.activity_register_activity, container, false);
		
	}

}

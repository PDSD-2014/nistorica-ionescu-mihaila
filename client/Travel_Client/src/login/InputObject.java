package login;


import android.app.Activity;
import android.widget.EditText;

import com.pdsd.project.main.Common;

public class InputObject {
	public String InputText;
	private String name;
	private Activity act;
	private int minSize = 4, maxSize = 30;
	
	public InputObject(Activity act, int id, String name) {
		InputText = ((EditText)act.findViewById(id)).getText().toString();
		this.name = name;
		this.act = act;
	}
	
	public boolean isValid() {

		
		/* Test size */
		int InputSize = InputText.length();
		if (minSize > InputSize || InputSize > maxSize) {
			Common.printError(act, "Invalid size for \"" + name + "\" field");
			return false;
		}
		
//		/* Test alphanumeric */
//		if (InputText.matches("^.*[^a-zA-Z0-9@.].*$")) {
//			Common.printError(act, "Field \"" + name + "\" contains non-alphanumeric characters");
//			return false;
//		}
		
		return true;
	}

}

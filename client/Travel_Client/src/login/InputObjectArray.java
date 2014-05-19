package login;

import java.util.ArrayList;

public class InputObjectArray extends ArrayList<InputObject> {

	/**
	 * An input object for form validation
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean isValid() {
		
		for (InputObject o: this) {
			if ( !o.isValid() ) {
				return false;
			}
		}
		return true;
	}

}

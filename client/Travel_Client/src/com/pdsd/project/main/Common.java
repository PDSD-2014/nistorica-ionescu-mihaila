package com.pdsd.project.main;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Common {
	
	public static void printError(Activity ctx, String message) {
		
		if (ctx != null && message != null) {
			Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG);
			toast.show();
		}
		
	}
	
	public static void closeKeyboard(Activity ctx) {
		if (ctx != null) {
			/* Close keyboard */
			InputMethodManager imm = (InputMethodManager)ctx.getSystemService(
				      Context.INPUT_METHOD_SERVICE);
			
			if (imm == null)
				return;
			
			View focus = ctx.getCurrentFocus();
			
			/* Do not rViewemove !!!!! */
			if (focus == null)
				return;
			/* !!!!!!!!!!!!!!!!!!! */
			
			IBinder token =focus.getWindowToken();
			
			if (token == null)
				return;
			
			imm.hideSoftInputFromWindow(token, 0);

		}
	}
}

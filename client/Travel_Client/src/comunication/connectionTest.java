package comunication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pdsd.project.main.Common;


public class connectionTest {
	/* Avem acces la retea */
	public static boolean isConnected(Activity act) {
		
		ConnectivityManager connMgr = (ConnectivityManager) 
		        act.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		// Wifi connected
		NetworkInfo WifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		// Mobile data connected
		NetworkInfo MobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (WifiInfo != null && WifiInfo.isConnected()) {
			return true;
		}
		if (MobileInfo != null && MobileInfo.isConnected()) {
			return true;
		}
			
		Common.printError( act, "Nu exista acces la retea!" );
		
		return false;

	}
}

package tw.edu.chu.csie.e_learning.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

	
	public NetworkUtils() {
	}
	
	public boolean isNetworkConnected(Context context) {
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni=cm.getActiveNetworkInfo();
		if(ni!=null && ni.isConnected()){
			return true;
		} else {
			return false;
		}
	}

}

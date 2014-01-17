package tw.edu.chu.csie.e_learning.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 網路處理的類別
 * @author yuan
 *
 */
public class NetworkUtils {

	/**
	 * 目前是否有網路連線
	 * @param context 帶入Android基底Context
	 * @return <code>true</code>目前有網路可使用
	 */
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

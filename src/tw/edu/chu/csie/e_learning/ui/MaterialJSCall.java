package tw.edu.chu.csie.e_learning.ui;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

public class MaterialJSCall {

	private Context context;
	
	public MaterialJSCall(Context context) {
		this.context = context;
	}
	
	// Annotation is needed for SDK version 17 or above.
	@JavascriptInterface
	public void learnFinish(String[] ansQID, String[] ansCheck) {
		int i;
		for(i = 0; i < ansQID.length ; i++){
			
			//Toast.makeText(this.context, ansQID[i], Toast.LENGTH_SHORT).show();			
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis()) ;
		String daystr = format.format(curDate);
		Toast.makeText(this.context, daystr , Toast.LENGTH_SHORT).show();	
		((Activity)this.context).finish();
	}
}
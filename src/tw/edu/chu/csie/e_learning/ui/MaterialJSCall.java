package tw.edu.chu.csie.e_learning.ui;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.scanner.QRCodeScanner;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.util.LearningUtils;

public class MaterialJSCall {

	private Context context;
	
	public MaterialJSCall(Context context) {
		this.context = context;
	}
	
	// Annotation is needed for SDK version 17 or above.
	@JavascriptInterface
	public void learnFinish(String[] ansQID, String[] ansCheck)  throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		int i;	
		String[] userid;
		String struserid = "";
		String tid = "";
		QRCodeScanner gettext = new QRCodeScanner();
		ClientDBProvider db = new ClientDBProvider(this.context);
		LearningUtils learn = new LearningUtils(this.context);
		
		tid = gettext.getext();
		
		userid = db.search("chu_user","UID",null);
		struserid = userid[0];
		
		for(i = 0; i < ansQID.length ; i++)
		{
			
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis()) ;
		String leave_target = format.format(curDate);
		Toast.makeText(this.context, leave_target , Toast.LENGTH_SHORT).show();	
		
//		learn.getPointIdOfLearningPoint(struserid, tid);
		
		((Activity)this.context).finish();
		Toast.makeText(this.context, "Test", 0).show();
	}
}
package tw.edu.chu.csie.e_learning.ui;

import android.annotation.SuppressLint;
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

	private MaterialActivity context;
	
	public MaterialJSCall(MaterialActivity context) {
		this.context = context;
	}
	
	// Annotation is needed for SDK version 17 or above.
	@SuppressLint("ShowToast")
	@JavascriptInterface
	public void learnFinish(String[] ansQID, String[] ansCheck)  throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		int i;	
		String[] userid;
		String struserid = "";
		String tid = "";
		
		ClientDBProvider db = new ClientDBProvider(this.context);
		LearningUtils learn = new LearningUtils(this.context);
		
		tid = String.valueOf(this.context.getMaterialId());
		Toast.makeText(context, "TID: "+tid, 0);
		
		userid = db.search("chu_user","UID",null);
		struserid = userid[0];
		
		for(i = 0; i < ansQID.length ; i++)
		{
			
		}
		
		// 呼叫到教材活動上的結束
		this.context.learnFinish();
	}
}
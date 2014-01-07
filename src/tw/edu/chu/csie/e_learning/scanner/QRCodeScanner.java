package tw.edu.chu.csie.e_learning.scanner;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.ui.MainFunctionActivity;
import tw.edu.chu.csie.e_learning.ui.MaterialActivity;
import tw.edu.chu.csie.e_learning.util.LearningUtils;
import qrcodereaderview.QRCodeReaderView;
import qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Toast;

public class QRCodeScanner extends Activity implements OnQRCodeReadListener {
	    //private TextView myTextView;
		private boolean tabEnable = true;
		private QRCodeReaderView mydecoderview;
		private String gettext = "";
		
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_decodeqr);
	        
	        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
	        mydecoderview.setOnQRCodeReadListener(this);
	        
	    }

	    
		// 當QRCode被Decode時呼叫
	    // "text" : the text encoded in QR
	    // "points" : points where QR control points are placed
		@Override
		public void onQRCodeRead(String text, PointF[] points) {
			if(tabEnable){
				tabEnable = false;
				
				this.gettext = text;
				
				if(text!="")
				{
					if(URLUtil.isNetworkUrl(text))
					{
						// TODO 拉開成String
						Toast.makeText(this, "此 QR-code內容 是個網址，非「標的編號」!!", Toast.LENGTH_LONG).show();
						finish();
					}
					else
					{
						if(text.length()>2)
						{
							// TODO 拉開成String
							Toast.makeText(this, "此內容不符合!!", Toast.LENGTH_LONG).show();
							finish();
						}
						else{
							try {
								int materialId = Integer.valueOf(text);
								// 解讀正確，進入學習教材
								if(new LearningUtils(this).isInRecommandPoint(text)) {
									Intent toLearning = new Intent(this, MaterialActivity.class);
									toLearning.putExtra("materialId", materialId);
									startActivityForResult(toLearning, 1);
									finish();
								}
								else {
									// TODO 拉開成String
									Toast.makeText(this, "這不是這次的推薦學習點喔～", Toast.LENGTH_LONG).show();
									finish();
								}
							} catch(IllegalArgumentException ex) {
								// TODO 拉開成String
								Toast.makeText(this, "此內容不是數字喔!!", Toast.LENGTH_LONG).show();
								finish();
							}
						}
					}
				}
				else
				{
					// TODO 拉開成String
					Toast.makeText(this, "掃描內容為空!!", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		}

		// 回傳標地編號，方便其他Class使用
		public String getext() 
		{
			return gettext;
		}
		
		// 當行動裝置裝置沒有Camera
		@Override
		public void cameraNotFound() {
			// TODO 拉開成String
			Toast.makeText(this, "行動裝置找不到Camera!!", Toast.LENGTH_LONG).show();
		}

		// Called when there's no QR codes in the camera preview image
		@Override
		public void QRCodeNotFoundOnCamImage() {
			
		}
	    
		@Override
		protected void onResume() {
			super.onResume();
			mydecoderview.getCameraManager().startPreview();
		}
		
		@Override
		protected void onPause() {
			super.onPause();
			mydecoderview.getCameraManager().stopPreview();
		}
	}
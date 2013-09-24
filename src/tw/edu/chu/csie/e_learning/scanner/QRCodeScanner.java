package tw.edu.chu.csie.e_learning.scanner;

import tw.edu.chu.csie.e_learning.R;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Toast;

public class QRCodeScanner extends Activity implements OnQRCodeReadListener {
	    //private TextView myTextView;
		private QRCodeReaderView mydecoderview;

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
			if(text!="")
			{
				if(URLUtil.isNetworkUrl(text))
				{
					Toast.makeText(this, "取得的URL"+text, Toast.LENGTH_LONG).show();
					Uri uri=Uri.parse(text);
					Intent intent=new Intent(Intent.ACTION_VIEW,uri);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(this, "此 QR-code內容 不是網址!!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(this, "掃描內容為空!!", Toast.LENGTH_LONG).show();
			}
		}

		
		// 當行動裝置裝置沒有Camera
		@Override
		public void cameraNotFound() {
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
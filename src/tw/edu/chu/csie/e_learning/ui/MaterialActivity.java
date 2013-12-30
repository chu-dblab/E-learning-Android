package tw.edu.chu.csie.e_learning.ui;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import tw.edu.chu.csie.e_learning.util.SettingUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class MaterialActivity extends Activity {
	
	private String thisMaterialId; //教材編號
	
	private FileUtils fileUtils;
	private WebView mWebView;
	private WebSettings webSettings;

	public MaterialActivity() {
		this.fileUtils = new FileUtils();
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material);
		mWebView = (WebView)findViewById(R.id.material_webview);
		
		// 取得目前所在的教材編號
		Intent intent = getIntent();
		this.thisMaterialId = intent.getStringExtra("materialId");
		
		if (savedInstanceState != null) {
			((WebView)findViewById(R.id.material_webview)).restoreState(savedInstanceState);
		} else {
			// 將網頁內容顯示出來
			webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			
			mWebView.addJavascriptInterface(new MaterialJSCall(this), "Android");
			mWebView.loadUrl("file://"+fileUtils.getMaterialPath()+this.thisMaterialId+".html");			
			//mWebView.loadUrl("file:///android_assets/01.html");			
		}
		
		
		// DEBUG 測試FileUtils
		Toast.makeText(this, fileUtils.getPath()+this.thisMaterialId+".html", Toast.LENGTH_SHORT).show();
	}
	
	
	protected void onSaveInstanceState(Bundle outState) {
	      mWebView.saveState(outState);
   }

	@Override
	public void onBackPressed() {
		// 判斷目前的設定檔是否允許中途離開學習點
		if(new SettingUtils(this).isLearningBackEnable()) {
			// 離開學習點
			Toast.makeText(getBaseContext(), R.string.learning_leaved_point, Toast.LENGTH_LONG).show();
			super.onBackPressed();
		} else {
			// 不允許離開學習點回到學習地圖
			Toast.makeText(getBaseContext(), R.string.learning_not_leave_point, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning, menu);
		return true;
	}

}

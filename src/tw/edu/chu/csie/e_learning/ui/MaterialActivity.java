package tw.edu.chu.csie.e_learning.ui;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.util.FileUtils;
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
	private boolean liveMaterial = false; //是否為實體教材
	
	private FileUtils fileUtils;
	private WebView mWebView;
	private WebSettings webSettings;

	public MaterialActivity() {
		this.fileUtils = new FileUtils();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material);
		mWebView = (WebView)findViewById(R.id.material_webview);
		
		// 取得目前所在的教材編號
		Intent intent = getIntent();
		this.thisMaterialId = intent.getStringExtra("materialId");
		this.liveMaterial = intent.getBooleanExtra("liveMaterial", false);
		
		// 將網頁內容顯示出來
		webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		mWebView.addJavascriptInterface(new MaterialJSCall(this), "Android");
		mWebView.loadUrl("file://"+fileUtils.getPath()+this.thisMaterialId+".html");
		
		// DEBUG 測試FileUtils
		Toast.makeText(this, fileUtils.getPath()+this.thisMaterialId+".html", Toast.LENGTH_SHORT).show();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning, menu);
		return true;
	}

}

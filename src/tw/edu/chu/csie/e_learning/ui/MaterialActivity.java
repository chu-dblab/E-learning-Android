package tw.edu.chu.csie.e_learning.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.server.BaseSettings;
import tw.edu.chu.csie.e_learning.server.ServerAPIs;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.util.AccountUtils;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import tw.edu.chu.csie.e_learning.util.LearningUtils;
import tw.edu.chu.csie.e_learning.util.SettingUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class MaterialActivity extends Activity {
	
	private int thisMaterialId; //教材編號
	private String in_target;
	private String leave_target;
	
	private FileUtils fileUtils;
	private WebView mWebView;
	private WebSettings webSettings;
	private AccountUtils account;

	public MaterialActivity() {
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO 橫向螢幕處理
		this.fileUtils = new FileUtils();
		this.account = new AccountUtils(this);
		
		// 隱藏ActionBar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		
		setContentView(R.layout.activity_material);
		mWebView = (WebView)findViewById(R.id.material_webview);
		
		// 判斷是否已經是在活動內
		if (savedInstanceState != null) {
			((WebView)findViewById(R.id.material_webview)).restoreState(savedInstanceState);
		} else {
			// 取得目前所在的教材編號
			Intent intent = getIntent();
			this.thisMaterialId = intent.getIntExtra("materialId",0);
			
			// 開始學習
			this.learnStart();
			
			// 將網頁內容顯示出來
			webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			
			mWebView.addJavascriptInterface(new MaterialJSCall(this), "Android");
			mWebView.loadUrl("file://"+fileUtils.getMaterialFilePath(this, thisMaterialId));			
			//mWebView.loadUrl("file://"+fileUtils.getMaterialPath()+this.thisMaterialId+".html");			
			//mWebView.loadUrl("file:///android_assets/01.html");			
			// DEBUG 測試FileUtils
			Toast.makeText(this, fileUtils.getPath()+this.thisMaterialId+".html", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	protected int getMaterialId() {
		return this.thisMaterialId;
	}
	
	/**
	 * 此學習點學習開始
	 */
	protected void learnStart() {
		// 取得進入學習點時間
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis()) ;
		in_target = format.format(curDate);
		Toast.makeText(this, in_target , Toast.LENGTH_SHORT).show();
		
		// 加人數
		RequestToServer request = new RequestToServer();
		request.execute("addPeople",Integer.toString(thisMaterialId));
	}
	
	/**
	 * 此學習點學習完畢
	 */
	protected void learnFinish() {
		// 取得離開學習點的時間
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis()) ;
		leave_target = format.format(curDate);
		Toast.makeText(this, leave_target , Toast.LENGTH_SHORT).show();
		
		// 清除推薦清單
		ClientDBProvider db = new ClientDBProvider(getBaseContext());
		db.delete(null, "chu_target");
		
		// 減人數
		RequestToServer request = new RequestToServer();
		request.execute("subPeople",Integer.toString(thisMaterialId));
		
		// 丟回答狀況給後端
		RequestForNextPoint nextPoint = new RequestForNextPoint();
		nextPoint.execute(Integer.toString(thisMaterialId),account.getLoginId(),in_target,leave_target);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("LearnedMaterialId",thisMaterialId);
		setResult(RESULT_OK, returnIntent);
		
		this.finish();
	}
	
	protected void onSaveInstanceState(Bundle outState) {
	      mWebView.saveState(outState);
   }

	@Override
	public void onBackPressed() {
		// 判斷目前的設定檔是否允許中途離開學習點
		if(new SettingUtils(this).isLearningBackEnable()) {
			learnFinish();
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
	
	/**
	 * 
	 */
	public class RequestToServer extends AsyncTask<String, Void, Void>
	{
		private LearningUtils learn = new LearningUtils(MaterialActivity.this);
		@Override
		protected Void doInBackground(String... params) 
		{
			try {
				changeOfPerson(params[0], params[1]);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ServerException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private void changeOfPerson(String action,String point) throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
		{
			if(action == "addPeople") learn.addPeople(point);
			else if(action == "subPeople") learn.subPeople(point);
			else Toast.makeText(getBaseContext(), "ERROR~!!", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// TODO DEBUG
			Toast.makeText(MaterialActivity.this, "傳送人數加減", 0).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// TODO DEBUG
			Toast.makeText(MaterialActivity.this, "已傳送人數加減", 0).show();
		}
	}
	
	/**
	 * 傳學生的回答狀況給伺服端 
	 *
	 */
	public class RequestForNextPoint extends AsyncTask<String, Void, Void>
	{
		private BaseSettings bs = new BaseSettings(new SettingUtils(MaterialActivity.this).getRemoteURL());
		private ServerAPIs api = new ServerAPIs(bs);
		
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				api.saveUserStatus(Integer.parseInt(params[0]), params[1], params[2], params[3]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(MaterialActivity.this, "傳送回答資料", 0).show();
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MaterialActivity.this, "OK傳送回答資料", 0).show();
		}
	}

}

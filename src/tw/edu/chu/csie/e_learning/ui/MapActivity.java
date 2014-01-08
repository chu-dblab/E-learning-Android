package tw.edu.chu.csie.e_learning.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.id;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import tw.edu.chu.csie.e_learning.R.string;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.scanner.NFCDetect;
import tw.edu.chu.csie.e_learning.scanner.QRCodeScanner;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.LoginCodeException;
import tw.edu.chu.csie.e_learning.server.exception.PostNotSameException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.util.AccountUtils;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import tw.edu.chu.csie.e_learning.util.HelpUtils;
import tw.edu.chu.csie.e_learning.util.LearningUtils;
import tw.edu.chu.csie.e_learning.util.SettingUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity {

	private FileUtils fileUtils;
	private ImageView mapView;
	private TextView nextPointView, nextPointTimeView;
	private SettingUtils config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		fileUtils = new FileUtils();
		config = new SettingUtils(this);
		mapView = (ImageView)findViewById(R.id.learning_map);
		// TODO DEBUG
		Bitmap bmp = BitmapFactory.decodeFile(fileUtils.getMaterialPath()+"map/1F.gif");
		//Toast.makeText(getActivity(), "file://"+fileUtils.getMaterialPath()+"map/map_04.jpg", 1).show();
		mapView.setImageBitmap(bmp);
		
		nextPointView = (TextView)findViewById(R.id.learning_next_point);
		nextPointTimeView = (TextView)findViewById(R.id.learning_next_point_time);
		
		// 取得下一個學習點
		if(!isHaveNextPoint()) getNextPoint();
		else updateNextPointUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 取得下一個學習點
		if(!isHaveNextPoint()) getNextPoint();
	}

	@Override
	public void onBackPressed() {
		if(config.isExitEnable()) {
			System.exit(0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		
		// DEBUG 開啟教材內容測試
		menu.add(0, 212, 0, "Tester");
		menu.add(0, 213, 0, "教材測試");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_about:
			HelpUtils.showAboutDialog(this);
			break;
		case R.id.menu_material_downloader:
			Intent toTextbookDownloader = new Intent(this, MaterialDownloaderActivity.class);
			startActivity(toTextbookDownloader);
			break;
		case R.id.menu_qrcode_scan:
			Intent toQRScan = new Intent(this, QRCodeScanner.class);
			startActivity(toQRScan);
			break;
		case R.id.menu_logout:
			LogoutTask mLogoutTask = new LogoutTask();
			mLogoutTask.execute();
			break;
       // DEBUG 開啟教材內容測試
		case 212:
			Intent toTester = new Intent(this, TesterActivity.class);
			startActivity(toTester);
			break;
       case 213:
    	   Intent toLearning = new Intent(this, MaterialActivity.class);
    	   toLearning.putExtra("materialId", 1);
    	   startActivityForResult(toLearning, 1);
    	   break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	// ========================================================================================
	protected boolean isHaveNextPoint() {
		// 檢查是否已推薦學習點了？？
		ClientDBProvider db = new ClientDBProvider(this);
		String[] query;
		query = db.search("chu_target", "TID", null);
		if(query.length > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * 取得下一個學習點
	 */
	protected void getNextPoint() {
		// 取得下一個學習點
		AccountUtils account = new AccountUtils(this);
		RequestFromServer requestFromServerTask = new RequestFromServer();
		requestFromServerTask.execute(account.getLoginId(),"0");
		
	}
	
	private void updateNextPointUI() {
		// 抓取首要推薦的學習地圖路徑
		ClientDBProvider db = new ClientDBProvider(this);
		String[] query;
		query = db.search("chu_target", "MapID", null);
		String mapFileName = query[0];
		
		// 抓取學習點編號
		query = db.search("chu_target", "TID", null);
		String tID = query[0];
		
		// 抓取學習點名稱
		query = db.search("chu_target", "TName", null);
		String tName = query[0];
		nextPointView.setText(tID+". "+tName);
		
		// 抓取預估學習時間
		query = db.search("chu_target", "TLearn_Time", null);
		String learnTime = query[0];
		nextPointTimeView.setText(learnTime);
		
		
		// 抓取預估學習時間
		
		Bitmap bmp = BitmapFactory.decodeFile(fileUtils.getMaterialPath()+"map/"+mapFileName);
		mapView.setImageBitmap(bmp);
	}
	
	public class RequestFromServer extends AsyncTask<String, Void, Void>
	{
		private LearningUtils learn = new LearningUtils(MapActivity.this);
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				learn.getPointIdOfLearningPoint(params[0],params[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				Log.d("test", Integer.toString(e.getID()));
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
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			nextPointView.setText(R.string.learning_next_point_getting);
			nextPointTimeView.setText(R.string.learning_next_point_getting);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			updateNextPointUI();
		}
	}
	
	public class LogoutTask extends AsyncTask<Void, Integer, Boolean> {

		private AccountUtils accountUtils = new AccountUtils(MapActivity.this);
		private ProgressDialog updateProgress;
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if (isCancelled()) {
				((Activity)MapActivity.this).finish();
				updateProgress.dismiss();
			}
			
			// DEBUG 當啟用無連線登入，進入學習畫面
			if(Config.DEBUG_NO_CONNECT_LOGIN) {
				return true;
			}
			else {
				try {
					accountUtils.logoutUser();
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
				} catch (PostNotSameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LoginCodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return true;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			updateProgress = new ProgressDialog(MapActivity.this);
			// TODO 拉開成String
			updateProgress.setMessage("登出中......");
    		updateProgress.setCancelable(false);
    		// TODO 拉出成字串
    		updateProgress.setButton("回到主畫面", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateProgress.dismiss();
					((Activity)MapActivity.this).finish();
				}
			});
    		updateProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		updateProgress.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			updateProgress.dismiss();
			((Activity)MapActivity.this).finish();
			//super.onPostExecute(result);
		}
	}
}

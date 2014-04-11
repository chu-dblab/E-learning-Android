package tw.edu.chu.csie.e_learning.ui;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.id;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import tw.edu.chu.csie.e_learning.R.string;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 學習地圖畫面，在此活動進行取得下一個學習點。 
 *
 */
@SuppressLint("HandlerLeak")
public class MapActivity extends Activity {

	public static final int RESULT_MATERIAL = 1;
	protected static final int REMAINED_TIME = 0x101;
	private int learnedPointID = 0;
	private FileUtils fileUtils;
	private ImageView mapView;
	private TextView nextPointView, nextPointTimeView, remainedTimeView;
	private SettingUtils config;
	private String pointID;
	private Timer updateUITimer;
	private static final String TAG = MapActivity.class.getSimpleName();
	
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
		
		remainedTimeView = (TextView)findViewById(R.id.learning_remaining_time);
		
		updateUITimer = new Timer();
		updateUITimer.schedule(new UpdateUITask(), 0, 1 * 1000);
		//updateUIThread = new Thread(new UpdateUIThread());
		//updateUIThread.start();
		// 取得下一個學習點
		if(!isHaveNextPoint()) getNextPoint();
		else updateNextPointUI();
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
		if(Config.DEBUG_ACTIVITY) {
			menu.add(0, 212, 0, "內部測試");
			menu.add(0, 213, 0, "教材測試");
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		// 當按下"關於"選項
		case R.id.menu_about:
			// 跳出"關於"對話框
			HelpUtils.showAboutDialog(this);
			break;
		// 當按下"QR Code掃描"選項
		case R.id.menu_qrcode_scan:
			// 進入QR Code掃描畫面
			Intent toQRScan = new Intent(this, QRCodeScanner.class);
			startActivityForResult(toQRScan, RESULT_MATERIAL);
			break;
		// 當按下"登出"選項
		case R.id.menu_logout:
			// 清除登入資料
			ClientDBProvider clientdb = new ClientDBProvider(MapActivity.this);
			clientdb.delete(null, "chu_user");
			clientdb.delete(null, "chu_target");
			// 停止界面上的跳動
			updateUITimer.cancel();
			// 向伺服器送出登出通知
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
    	   toLearning.putExtra("pointId", 1);
    	   startActivityForResult(toLearning, 1);
    	   break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_MATERIAL) {
			if(resultCode == RESULT_OK){
				// 從教材頁面接收剛剛學過的是哪個標地
				Bundle bundle = data.getExtras();
				this.learnedPointID = bundle.getInt("LearnedPointId");
		     }
		}
	}
	
	// ========================================================================================
	/**
	 * 檢查是否已推薦學習點了？？
	 * @return <code>true</code>已經推薦過學習點了
	 */
	protected boolean isHaveNextPoint() {
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
		requestFromServerTask.execute(String.valueOf(this.learnedPointID));
		
	}
	
	/**
	 * 更新界面上的畫面
	 */
	private void updateNextPointUI() {
		// 抓取首要推薦的學習地圖路徑
		if(!isHaveNextPoint()) {
			Toast.makeText(this, "發生逾時，重新推薦", Toast.LENGTH_SHORT).show();
			getNextPoint();
		}
		else {
			ClientDBProvider db = new ClientDBProvider(this);
			String[] query;
			// 抓取學習點編號
			query = db.search("chu_target", "TID", null);
			String tID = query[0];
			
			// 檢查是否已經學習結束
			if(tID.equals("0")) {
				// 告訴使用者已經學習完了
				// TODO 改成Alert
				Toast.makeText(this, "你已經學習完囉～", 1).show();
				
				// 登出
				db.delete(null, "chu_user");
				db.delete(null, "chu_target");
				LogoutTask mLogoutTask = new LogoutTask();
				mLogoutTask.execute();
			}
			else {
				// 如果是實體教材，要讓學生前往標的
				if(new LearningUtils(this).isEntityMaterial(tID)) {
					query = db.search("chu_target", "MapID", null);
					String mapFileName = query[0];
					
					// 更新學習點地圖
					Bitmap bmp = BitmapFactory.decodeFile(fileUtils.getMaterialPath()+"map/"+mapFileName);
					mapView.setImageBitmap(bmp);
					
					// 更新學習點名稱
					query = db.search("chu_target", "TName", null);
					String tName = query[0];
					nextPointView.setText(tID+". "+tName);
					
					// 抓取預估學習時間
					query = db.search("chu_target", "TLearn_Time", null);
					String learnTime = query[0];
					nextPointTimeView.setText(learnTime);
					
				}
				// 如果是虛擬教材，就直接進入教材
				else {
					Intent toLearning = new Intent(this, MaterialActivity.class);
					toLearning.putExtra("pointId",  Integer.parseInt(tID));
					startActivityForResult(toLearning, RESULT_MATERIAL);
				}
			}
		}
	}
	// ========================================================================================
	
	Handler updateUIHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case REMAINED_TIME:
				LearningUtils learnUtils = new LearningUtils(MapActivity.this);
				Calendar learningCal = Calendar.getInstance();
				learningCal.setTime(learnUtils.getRemainderLearningDate());
				learningCal.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				remainedTimeView.setText(learningCal.get(Calendar.HOUR_OF_DAY)+":"+learningCal.get(Calendar.MINUTE)+":"+learningCal.get(Calendar.SECOND));
				break;
			}
		};
	};
	
	class UpdateUITask extends TimerTask {

		@Override
		public void run() {
			Message message = new Message();   
			message.what = MapActivity.REMAINED_TIME;   
			  
			MapActivity.this.updateUIHandler.sendMessage(message);
		}
		
	}
	
	/**
	 * 向伺服端要求下一個推薦的學習點
	 */
	public class RequestFromServer extends AsyncTask<String, Void, Void>
	{
		private LearningUtils learn = new LearningUtils(MapActivity.this);
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				learn.getPointIdOfLearningPoint(params[0]);
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
	
	/**
	 * 向伺服器通知登出此帳號 
	 */
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

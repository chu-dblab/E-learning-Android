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
import android.os.Parcelable;
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

public class MapActivity extends Activity {

	public static final int RESULT_MATERIAL = 1;
	private int learnedPointID = 0;
	private FileUtils fileUtils;
	private ImageView mapView;
	private TextView nextPointView, nextPointTimeView;
	private SettingUtils config;
	private NfcManager manager;
	private NfcAdapter adapter;
	private IntentFilter nfc_tech;
	private IntentFilter[] nfcFilter;
	private PendingIntent nfcPendingIntent;
	private Intent nfc_intent;
	private String[][] tech_list;
	private String pointID;
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
		initialNFCDetect();
		// 取得下一個學習點
		if(!isHaveNextPoint()) getNextPoint();
		else updateNextPointUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.enableForegroundDispatch(this, nfcPendingIntent, nfcFilter, tech_list);
		// 取得下一個學習點
		if(!isHaveNextPoint()) getNextPoint();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		adapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        // 取得NdefMessage
	        NdefMessage[] messages = getNdefMessages(getIntent());
	        // 取得實際的內容
	        byte[] payload = messages[0].getRecords()[0].getPayload();
	        pointID = new String(payload);
	        sentIntentToMaterial(pointID);
	        // 往下送出該intent給其他的處理對象
	        setIntent(new Intent()); 
	    }
		super.onNewIntent(intent);
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
			startActivityForResult(toQRScan, RESULT_MATERIAL);
			break;
		case R.id.menu_logout:
			ClientDBProvider clientdb = new ClientDBProvider(MapActivity.this);
			clientdb.delete(null, "chu_user");
			clientdb.delete(null, "chu_target");
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
				Bundle bundle = data.getExtras();
				this.learnedPointID = bundle.getInt("LearnedPointId");
				Toast.makeText(this, "Learned: "+this.learnedPointID, 0).show();         
		     }
		}
	}
	
	//=========================================================================================
	//NFC 相關的code
	private void initialNFCDetect()
	{
		adapter = NfcAdapter.getDefaultAdapter(this);
		nfc_intent = new Intent(this,getClass());
		nfcPendingIntent = PendingIntent.getActivity(this, 0, nfc_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		nfc_tech = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			nfc_tech.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) { }
		nfcFilter = new IntentFilter[]{nfc_tech};
		tech_list = new String[][]{new String[]{NfcA.class.getName()}};
	}
	
	private NdefMessage[] getNdefMessages(Intent intent) {
	    // Parse the intent
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    // 識別目前的action為何
	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
	            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        // 取得parcelabelarrry的資料
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        // 取出的內容如果不為null，將parcelable轉成ndefmessage
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        } else {
	            // Unknown tag type
	            byte[] empty = new byte[] {};
	            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[] {
	                record
	            });
	            msgs = new NdefMessage[] {
	                msg
	            };
	        }
	    } else {
	        Log.d(TAG, "Unknown intent.");
	        finish();
	    }
	    return msgs;
	}
	
	private void sentIntentToMaterial(String targetID)
	{
		if(new LearningUtils(this).isInRecommandPoint(targetID)) {
			Intent toLearning = new Intent(this, MaterialActivity.class);
			toLearning.putExtra("pointId",  Integer.parseInt(targetID));
			startActivity(toLearning);
		}
		else {
			// TODO 拉開成String
			Toast.makeText(this, "這不是這次的推薦學習點喔～", Toast.LENGTH_LONG).show();
		}
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
		requestFromServerTask.execute(account.getLoginId(), String.valueOf(this.learnedPointID));
		
	}
	
	private void updateNextPointUI() {
		// 抓取首要推薦的學習地圖路徑
		if(!isHaveNextPoint()) {
			Toast.makeText(this, "發生逾時，重新推薦", Toast.LENGTH_SHORT).show();
			getNextPoint();
		}
		else {
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

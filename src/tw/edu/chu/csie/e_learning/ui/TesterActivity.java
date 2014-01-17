package tw.edu.chu.csie.e_learning.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.ui.MaterialActivity.RequestToServer;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import tw.edu.chu.csie.e_learning.util.LearningUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 內部人員測試用的輔助頁面
 * @author yuan
 *
 */
public class TesterActivity extends Activity implements OnClickListener {

	private ProgressBar sendProgress;
	private Button sql_clear_target;
	private Button sendStopSendAll, sendAddPeople, sendSubPeople, sendSaveUserStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tester);
		// 設定ActionBar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button btn1 = (Button)findViewById(R.id.tester_btn1);
		btn1.setText("取得學習點01教材路徑");
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), new FileUtils().getMaterialFilePath(getBaseContext(), 19), 0).show();
			}
		});
		
		Button btn2 = (Button)findViewById(R.id.tester_btn2);
		btn2.setText("寫入標地1進SQLite");
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClientDBProvider db = new ClientDBProvider(getBaseContext());
				db.target_insert(01, "Test!!!", "map_01_02_03.png", "01.html", 3,0);
			}
		});
		
		Button btn3 = (Button)findViewById(R.id.tester_btn3);
		btn3.setText("清除標地SQLite");
		btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClientDBProvider db = new ClientDBProvider(getBaseContext());
				db.delete(null, "chu_target");
			}
		});
		
		// ---------------------------------------------------------------------------------------------------------------------------------
		sql_clear_target = (Button)findViewById(R.id.tester_sqlite_clear_target);
		sql_clear_target.setOnClickListener(this);
		
		
		// ---------------------------------------------------------------------------------------------------------------------------------
		//sendProgress = (ProgressBar)findViewById(R.id.tester_send_progress);
		
		
		sendStopSendAll = (Button)findViewById(R.id.tester_send_stop_all_send);
		sendStopSendAll.setOnClickListener(this);
		
		sendAddPeople = (Button)findViewById(R.id.tester_send_addpeople);
		sendAddPeople.setOnClickListener(this);
		
		sendSubPeople = (Button)findViewById(R.id.tester_send_subpeople);
		sendSubPeople.setOnClickListener(this);
		
		/*sendSaveUserStatus = (Button)findViewById(R.id.tester_send_save_user_status);
		sendSaveUserStatus.setOnClickListener(this);*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tester, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
	      case android.R.id.home:
	          finish();
	          return true;
	      default:
	          return super.onMenuItemSelected(featureId, item);
	     }
	}

	@Override
	public void onClick(View v) {
		ClientDBProvider clientdb; 
		RequestToServer request;
		
		switch(v.getId()) {
		case R.id.tester_sqlite_clear_target:
			clientdb = new ClientDBProvider(TesterActivity.this);
			clientdb.delete(null, "chu_target");
			break;
		
		case R.id.tester_send_stop_all_send:
			Toast.makeText(this, "XDDD", 0).show();
			break;
		case R.id.tester_send_addpeople:
			// 加人數
			request = new RequestToServer();
			request.execute("addPeople","12");
			break;
		case R.id.tester_send_subpeople:
			// 減人數
			request = new RequestToServer();
			request.execute("subPeople","12");
			break;
		case R.id.tester_send_save_user_status:
			Toast.makeText(this, "XDDD", 0).show();
			break;
		}
	}
	
	// =================================================================================================================================
	/**
	 * 加人數、減人數
	 */
	public class RequestToServer extends AsyncTask<String, Void, Void>
	{
		private LearningUtils learn = new LearningUtils(TesterActivity.this);
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
			Toast.makeText(TesterActivity.this, "傳送人數加減", 0).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(TesterActivity.this, "已傳送人數加減", 0).show();
		}
	}

}

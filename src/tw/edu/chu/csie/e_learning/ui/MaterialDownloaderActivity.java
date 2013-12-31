package tw.edu.chu.csie.e_learning.ui;

import java.io.*;

import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.config.*;
import tw.edu.chu.csie.e_learning.server.BaseSettings;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.sync.MaterialSyncUtils;
import tw.edu.chu.csie.e_learning.util.SettingUtils;

/**
 * Created by yuan on 2013/6/3.
 */
public class MaterialDownloaderActivity extends Activity implements OnClickListener {
	
	private Button textbook_update;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_downloader);
        textbook_update = (Button)findViewById(R.id.updateButton);
        textbook_update.setOnClickListener(this);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
	public void onClick(View v) {
		// TODO 自動產生的方法 Stub
    	DownloadTextBookTask downloadTextBook = new DownloadTextBookTask(this);
//    	downloadTextBook.execute(Config.REMOTE_MATERIAL_URL);
    	SettingUtils setting = new SettingUtils(this);
    	downloadTextBook.execute(setting.getRemoteMaterialURL());
	}
    
    /**
     * 
     * @author Tony 2013/08/06
     * 
     *
     */
    public class DownloadTextBookTask extends AsyncTask<String, Integer, Void>
    {
    	private MaterialSyncUtils download = new MaterialSyncUtils();
    	private ProgressDialog updateProgress;
    	private Context message;
    	
    	public DownloadTextBookTask(Context message)
    	{
    		this.message = message;
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		try {
				download.downloadTeachingMaterial(params[0]);				
			} catch (IOException e) {
				//Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
				Log.d("Message", e.getMessage());
				e.printStackTrace();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				Log.d("Message", Integer.toString(e.getStatusCode()));
				e.printStackTrace();
			}
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void result) {
    		updateProgress.dismiss();
    		super.onPostExecute(result);
    	}

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		updateProgress = new ProgressDialog(message);
    		// TODO 拉開成String
    		updateProgress.setMessage("下載教材並解壓縮中......");
    		updateProgress.setCancelable(false);
    		updateProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		updateProgress.show();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO 自動產生的方法 Stub
    		updateProgress.setProgress(values[0]);
    		super.onProgressUpdate(values);
    	}
    	
    }
    
    // menu=============================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	      case android.R.id.home:
	          finish();
	          return true;
	      default:
	          return super.onOptionsItemSelected(item);
	     }
	 }
}

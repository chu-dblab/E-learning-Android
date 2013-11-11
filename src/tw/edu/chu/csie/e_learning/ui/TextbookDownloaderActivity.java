package tw.edu.chu.csie.e_learning.ui;

import java.io.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.config.*;
import tw.edu.chu.csie.e_learning.sync.TextbookSyncUtils;

/**
 * Created by yuan on 2013/6/3.
 */
public class TextbookDownloaderActivity extends Activity implements OnClickListener {
	
	private Button textbook_update;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textbook_downloader);
        textbook_update = (Button)findViewById(R.id.updateButton);
        textbook_update.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v) {
		// TODO 自動產生的方法 Stub
    	DownloadTextBookTask downloadTextBook = new DownloadTextBookTask(this);
    	downloadTextBook.execute(Config.REMOTE_TEXTBOOK_URL);
	}
    
    /**
     * 
     * @author Tony 2013/08/06
     * 
     *
     */
    public class DownloadTextBookTask extends AsyncTask<String, Integer, Void>
    {
    	private TextbookSyncUtils download = new TextbookSyncUtils();
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
				Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void result) {
    		// TODO 自動產生的方法 Stub
    		updateProgress.dismiss();
    		super.onPostExecute(result);
    	}

    	@Override
    	protected void onPreExecute() {
    		// TODO 自動產生的方法 Stub
    		super.onPreExecute();
    		updateProgress = new ProgressDialog(message);
    		updateProgress.setMessage("下載教材中......");
    		updateProgress.setCancelable(false);
    		updateProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		updateProgress.show();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO 自動產生的方法 Stub
    		updateProgress.setProgress(values[0]);
    		super.onProgressUpdate(values);
    	}
    	
    }
}

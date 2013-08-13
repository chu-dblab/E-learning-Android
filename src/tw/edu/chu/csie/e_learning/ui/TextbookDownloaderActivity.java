package tw.edu.chu.csie.e_learning.ui;

import java.io.*;
import java.net.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tw.edu.chu.csie.e_learning.R;

/**
 * Created by yuan on 2013/6/3.
 */
public class TextbookDownloaderActivity extends Activity implements OnClickListener {
	
	private Button textbook_update;
	private final String HTTP_URL = "http://140.126.11.163/test.html";
	
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
    	downloadTextBook.execute(HTTP_URL);
	}
    
    /**
     * 
     * @author Tony 2013/08/06
     * 
     *
     */
    @SuppressWarnings("unused")
    public class DownloadTextBookTask extends AsyncTask<String, Integer, Void>
    {
    	
		private HttpURLConnection url_con;
    	private URL http_url;
    	private InputStream is;
    	private File filepath;
    	private FileOutputStream output2SDCard;
    	private ProgressDialog updateProgress;
    	private Context message;
    	
    	public DownloadTextBookTask(Context message)
    	{
    		this.message = message;
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		// TODO 自動產生的方法 Stub
    		try {
				http_url = new URL(params[0]);
			} catch (MalformedURLException e1) {
				// TODO 自動產生的 catch 區塊
				e1.printStackTrace();
			}
    		try {
				url_con = (HttpURLConnection)http_url.openConnection();
				url_con.setRequestMethod("POST");
				url_con.setDoInput(true);
				url_con.setConnectTimeout(10000);
				if(url_con.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					is =  url_con.getInputStream();
					filepath = new File(android.os.Environment.getExternalStorageDirectory()+"/Download/test.html");
					output2SDCard = new FileOutputStream(filepath);
					int data = 0,getdata = 0;
					byte[] info = new byte[1024];
					while((getdata = is.read()) != -1)
					{
						publishProgress((data/info.length)*100);
						data += getdata;
						output2SDCard.write(info,0,getdata);
					}
					data = 0;   getdata = 0;
					output2SDCard.flush();
					output2SDCard.close();
					is.close();
					url_con.disconnect();
				}
				else
				{
					//Toast.makeText(getBaseContext(), url_con.getResponseCode(), Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO 自動產生的 catch 區塊
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
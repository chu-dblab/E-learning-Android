package tw.edu.chu.csie.e_learning.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadTextBookService extends Service
{
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO 自動產生的方法 Stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO 自動產生的方法 Stub
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO 自動產生的方法 Stub
		return super.onUnbind(intent);
	}
	
}

/**
 * 
 */
package tw.edu.chu.csie.e_learning.service;

import tw.edu.chu.csie.e_learning.util.TimerUtils;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author yuan
 *
 */
public class TimerService extends Service {
	
	private TimerUtils timer;
	
	// Binder given to clients
	private final IBinder mBinder = new TimerLocalBinder();
	
	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
    public class TimerLocalBinder extends Binder {
    	public TimerService getService() {
    		return TimerService.this;
    	}
    }

	private void onTimeOut() {
		// TODO Auto-generated method stub
		
	}
	
	// ------------------------------------------------------------------
	
	public TimerUtils getTimer() {
		return timer;
	}
	
	// ------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Create", 0).show();
		
		timer = new TimerUtils();
		timer.addListener(new TimerUtils.TimerListener() {
			
			@Override
			public void timeOut() {
				// TODO Auto-generated method stub
				onTimeOut();
			}
			
			@Override
			public void onChange() {
				
			}
		});
		super.onCreate();
	}	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Start", 0).show();
		int setMin = intent.getExtras().getInt("setTotalTimeMin", -1);
		timer.setTime(setMin, 0);
		timer.startTimeing();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		timer.stopTiming();
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return this.mBinder;
	}

}

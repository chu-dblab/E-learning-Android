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
		//Toast.makeText(this, "Start", 0).show();
		if(intent.getExtras().containsKey("setTotalSecTime")) {
			int setTotalSec = intent.getExtras().getInt("setTotalSecTime", -1);
			timer.setSumSecond(setTotalSec);
		}
		else if(intent.getExtras().containsKey("setSecTime") || intent.getExtras().containsKey("setMinTime")) {
			int setSec = 0;
			if(intent.getExtras().containsKey("setSecTime")) {
				setSec = intent.getExtras().getInt("setSecTime");
			}
			int setMin = 0;
			if(intent.getExtras().containsKey("setMinTime")) {
				setMin = intent.getExtras().getInt("setMinTime");
			}
			timer.setTime(setMin, setSec);
		}
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

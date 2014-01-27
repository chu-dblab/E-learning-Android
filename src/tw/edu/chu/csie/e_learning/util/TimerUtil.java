/**
 * 
 */
package tw.edu.chu.csie.e_learning.util;

import tw.edu.chu.csie.e_learning.service.MyTimer;
import tw.edu.chu.csie.e_learning.service.TimerService;
import tw.edu.chu.csie.e_learning.ui.TesterActivity;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author yuan
 *
 */
public class TimerUtil {

	private Context context;
	
	private TimerService timerService = null;
	private boolean isBind = false;
	private ServiceConnection mTimerConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			timerService = ((TimerService.TimerLocalBinder)service).getService();
			isBind = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			timerService = null;
			isBind = false;
			mTimerConn = null;
		}
		
	};
	
	/**
	 * 
	 */
	public TimerUtil(Context context) {
		this.context = context;
	}
	
	
	
	public void startTimer(long sumSecond) {
		this.unBind();
		this.bind();
		Intent toTimerService = new Intent(this.context, TimerService.class);
		toTimerService.putExtra("setTotalSecTime", sumSecond);
		this.context.startService(toTimerService);
	}
	
	public void startTimer(int min, int sec) {
		this.unBind();
		this.bind();
		Intent toTimerService = new Intent(this.context, TimerService.class);
		toTimerService.putExtra("setMinTime", min);
		toTimerService.putExtra("setSecTime", sec);
		this.context.startService(toTimerService);
	}

	public void stopTimer() {
		this.timerService = null;
		this.unBind();
		Intent toTimerService = new Intent(this.context, TimerService.class);
		this.context.stopService(toTimerService);
	}
	
	public void bind() {
		if(timerService == null) {
			timerService = null;
			Intent toTimerService = new Intent(this.context, TimerService.class);
			this.context.bindService(toTimerService, mTimerConn, Context.BIND_AUTO_CREATE);
		}
	}
	
	public void unBind() {
		if(timerService != null) {
			timerService = null;
			this.context.unbindService(mTimerConn);
		}
	}
	
	public int getSecond() {
		if(timerService != null) {
			return timerService.getTimer().getSecond();
		}
		else {
			return -1;
		}
	}
	
	public int getMinute() {
		if(timerService != null) {
			return timerService.getTimer().getMinute();
		}
		else {
			return -1;
		}
	}
	
	public long getTotalSecond() {
		if(timerService != null) {
			return timerService.getTimer().getSumSecond();
		}
		else {
			return -1;
		}
	}
}

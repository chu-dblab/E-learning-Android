package tw.edu.chu.csie.e_learning.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * 部份來源: http://blog.xuite.net/ray00000test/blog/66911629-%E5%80%92%E6%95%B8%E8%A8%88%E6%99%82%E5%99%A8%28%E5%80%92%E6%95%B8%E7%A7%92%E6%95%B8%E9%A1%AF%E7%A4%BA%E5%9C%A8JLabel%E4%B8%8A%29
 */

public class TimerUtils {
	
	public interface Listener{
		//通知時間到
		public void timeOut();
		//秒數變動秒數
		public void onChange();
	}
	/**
	* 設定傾聽timer事件
	* @param li
	*/
	public void addListener(Listener li){
		lis = li;
	}
	
	// ========================================================
	
	private long totalSecond;
	private boolean timeToAdd = false;
	private boolean timeToOver = false;
	
	private Listener lis;
	private Timer timer;
	
	public TimerUtils() {
		this.timer = new Timer();
	}
	
	public TimerUtils(long setSecond) {
		this();
		this.setSumSecond(setSecond);
	}
	public TimerUtils(int min, int sec) {
		this();
		this.setTime(min, sec);
	}
	
	public void setSumSecond(long setSecond) {
		this.totalSecond = setSecond;
		
	}
	public long getSumSecond() {
		return this.totalSecond;
	}
	
	// --------------------------------------------------------------------
	public void setTime(int min, int sec) {
		this.totalSecond = min*60 + sec;
	}
	
	public int getSecond() {
		return (int)(this.totalSecond % 60);
	}
	
	public int getMinute() {
		return (int)(this.totalSecond / 60);
	}
	
	// --------------------------------------------------------------------
	
	public void startTimeing() {
		timer.schedule(new TimingTask(), 0, // initial delay
				1 * 1000); // subsequent rate
	}
	public void stopTiming() {
		timer.cancel();
	}
	
	// ====================================================================
	
	private class TimingTask extends TimerTask {
		@Override
		public void run() {
			if(totalSecond != 0 && !timeToOver) {
				if(timeToAdd) {
					setSumSecond(totalSecond+1);
					lis.onChange();
				} else {
					setSumSecond(totalSecond-1);
					lis.onChange();
				}
			}
			else {
				lis.timeOut();
				timer.cancel();
			}
		}
	}
	
}
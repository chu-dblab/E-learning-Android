package tw.edu.chu.csie.e_learning.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 計時器
 * <p>
 * 來源: http://blog.xuite.net/ray00000test/blog/66911629-%E5%80%92%E6%95%B8%E8%A8%88%E6%99%82%E5%99%A8%28%E5%80%92%E6%95%B8%E7%A7%92%E6%95%B8%E9%A1%AF%E7%A4%BA%E5%9C%A8JLabel%E4%B8%8A%29
 * @author yuan
 */
public class TimerUtils {
	
	public interface TimerListener{
		/**
		 * 若時間已到，則觸發此Listener函式
		 */
		public void timeOut();
		/**
		 * 若已開始計時，每動一次秒就觸發一次此Listener函式
		 */
		public void onChange();
	}
	// ========================================================
	
	private long totalSecond;
	private boolean timeToAdd = false;
	private boolean timeToOver = false;
	
	private TimerListener lis;
	private Timer timer;
	
	public TimerUtils() {
		this.timer = new Timer();
	}
	
	/**
	 * 設定傾聽timer事件
	 * @param li
	 */
	public void addListener(TimerListener li){
		lis = li;
	}
	
	/**
	 * 建構子 設定初始總秒數
	 * @param setSecond 總秒數
	 */
	public TimerUtils(long setSecond) {
		this();
		this.setSumSecond(setSecond);
	}
	
	/**
	 * 建構子 設定初始分鐘、秒數
	 * @param min
	 * @param sec
	 */
	public TimerUtils(int min, int sec) {
		this();
		this.setTime(min, sec);
	}
	
	/**
	 * 設定總秒數
	 * @param setSecond 總秒數
	 */
	public void setSumSecond(long setSecond) {
		this.totalSecond = setSecond;
		
	}
	/**
	 * 取得計時總秒數
	 * @return 秒數
	 */
	public long getSumSecond() {
		return this.totalSecond;
	}
	
	// --------------------------------------------------------------------
	/**
	 * 設定分鐘、秒數
	 * @param min 分鐘
	 * @param sec 秒數
	 */
	public void setTime(int min, int sec) {
		this.totalSecond = min*60 + sec;
	}
	
	/**
	 * 取得計時秒數
	 * @return 秒數
	 */
	public int getSecond() {
		return (int)(this.totalSecond % 60);
	}
	
	/**
	 * 取得計時分鐘
	 * @return 分鐘
	 */
	public int getMinute() {
		return (int)(this.totalSecond / 60);
	}
	
	// --------------------------------------------------------------------
	/**
	 * 開始計時
	 */
	public void startTimeing() {
		timer.schedule(new TimingTask(), 0, // initial delay
				1 * 1000); // subsequent rate
	}
	/**
	 * 停止計時
	 */
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
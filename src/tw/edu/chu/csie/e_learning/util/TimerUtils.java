package tw.edu.chu.csie.e_learning.util;

import java.util.Date;

/**
 * 
 * 部份來源: http://blog.xuite.net/ray00000test/blog/66911629-%E5%80%92%E6%95%B8%E8%A8%88%E6%99%82%E5%99%A8%28%E5%80%92%E6%95%B8%E7%A7%92%E6%95%B8%E9%A1%AF%E7%A4%BA%E5%9C%A8JLabel%E4%B8%8A%29
 */
public class TimerUtils {
	public interface Listener{
		//通知時間到
		public void timeOut();
		//秒數變動秒數
		public void onChange(long sec);
	}
	
	/**
	* 設定傾聽timer事件
	* @param li
	*/
	public void addListener(Listener li){
		lis = li;
	}
	
	private Date thisTime;
	private Listener lis;
	
	public TimerUtils(Date setTime) {
		this.setTime(setTime);
	}
	
	public void setTime(Date setTime) {
		this.thisTime = setTime;
		
	}
	public Date getTime() {
		return this.thisTime;
	}
	
	
	private class TimingThread extends Thread {

		boolean timeToAdd = false;
		boolean timeToOver = false;
		
		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			super.interrupt();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			try {
				long timenum = thisTime.getTime();
				while(true) {
					if(timenum != 0 && !timeToOver) {
						if(timeToAdd) {
							thisTime.setTime(timenum+1);
							lis.onChange(timenum+1);
						} else {
							thisTime.setTime(timenum-1);
							lis.onChange(timenum-1);
						}
						Thread.sleep(1000);
					}
					else {
						lis.timeOut();
						this.interrupt();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

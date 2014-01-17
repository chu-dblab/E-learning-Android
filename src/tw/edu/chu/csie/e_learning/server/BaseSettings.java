package tw.edu.chu.csie.e_learning.server;

/**
 * 基底設定
 * @author 元兒～ <yuan817@moztw.org>
 * @version 1.0
 */
public class BaseSettings {
	
	private boolean sslEnable = false; //是否啟用SSL加密
	private String base_url;
	private static final String URLPART_API = "API/v1/";
	
	private boolean debugEnable = false;
	
	/**
	 * 建構子
	 */
	public BaseSettings() {
		this.setDebugEnable(false);
	}
	
	/**
	 * 建構子
	 * @param inputBaseUrl 伺服端網址
	 */
	public BaseSettings(String inputBaseUrl) {
		this.setBaseUrl(inputBaseUrl);
		this.setDebugEnable(false);
	}
	
	/**
	 * 建構子
	 * @param inputBaseUrl 伺服端網址
	 * @param inputDebugEnabled 除錯模式開關
	 */
	public BaseSettings(String inputBaseUrl, boolean inputDebugEnabled) {
		this.setBaseUrl(inputBaseUrl);
		this.setDebugEnable(inputDebugEnabled);
	}
	
	// ===========================================================
	
	/**
	 * 設定是否採用SSL加密
	 * @param input 是否啟用加密
	 */
	public void setSSLEnable(boolean input) {
		// TODO 同時檢查目前api網址有無加上"https://"字樣
		
		this.sslEnable = input;
	}
	
	/**
	 * 設定是否採用SSL加密
	 * @param input 是否啟用加密
	 */
	public void setHttpsEnable(boolean input) {
		this.setSSLEnable(input);
	}
	
	/**
	 * 取得是否啟用SSL加密
	 * @return 是否啟用加密
	 */
	public boolean getSSLEnable() {
		return this.sslEnable;
	}
	
	/**
	 * 取得是否啟用SSL加密
	 * @return 是否啟用加密
	 */
	public boolean getHttpsEnable() {
		return this.getSSLEnable();
	}
	
	// -----------------------------------------------------------
	
	/**
	 * 設定基底URL
	 * @param input 輸入的基底URL
	 */
	public void setBaseUrl(String input) {
		// 判斷是否加上"http://"
		if(input.startsWith("http://")) {
			this.setSSLEnable(false);
		}
		else if(input.startsWith("https://")) {
			this.setSSLEnable(true);
		}
		else {
			if(this.sslEnable) {
				input = "https://" + input;
			}
			else {
				input = "http://" + input;
			}
		}
		
		// 判斷最後一個字是不是為"/"
		if(input.charAt( input.length()-1 ) != '/') {
			input+='/';
		}
		this.base_url = input;
	}
	
	/**
	 * 取得基底URL
	 * @return 基底URL 
	 */
	public String getBaseUrl() {
		return this.base_url;
	}
		
	/**
	 * 取得API URL
	 * @return 完整API URL
	 */
	public String getApiUrl() {
		return getBaseUrl() + this.URLPART_API;
	}
	
	//===========================================================
	/**
	 * 設定是否為開發模式
	 * @param input
	 */
	public void setDebugEnable(boolean input) {
		this.debugEnable = input;
	}
	
	/**
	 * 取得是否為開發模式
	 * @return
	 */
	public boolean getDebugEnable() {
		return this.debugEnable;
	}

}

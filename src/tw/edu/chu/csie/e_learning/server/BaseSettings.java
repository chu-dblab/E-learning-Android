package tw.edu.chu.csie.e_learning.server;

/**
 * 
 * @author 元兒～ <yuan817@moztw.org>
 *
 */
public class BaseSettings {
	
	private String base_url;
	private static final String URLPART_API = "API/v1/";
	
	private boolean debugEnable = false;
	
	public BaseSettings() {
		this.setDebugEnabled(false);
	}
	
	public BaseSettings(String inputBaseUrl) {
		this.setBaseUrl(inputBaseUrl);
		this.setDebugEnabled(false);
	}
	
	public BaseSettings(String inputBaseUrl, boolean inputDebugEnabled) {
		this.setBaseUrl(inputBaseUrl);
		this.setDebugEnabled(inputDebugEnabled);
	}
	
	// ===========================================================
	
	/**
	 * 設定基底URL
	 * @param input 輸入的基底URL
	 */
	public void setBaseUrl(String input) {
		// TODO 判斷是否加上"http://"
		// TODO 判斷最後一個字是不是為"/"
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
	public void setDebugEnabled(boolean input) {
		this.debugEnable = input;
	}
	
	/**
	 * 取得是否為開發模式
	 * @return
	 */
	public boolean getDebugEnabled() {
		return this.debugEnable;
	}

}

/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	Config.java
 * 
 * Description: 在這邊指定本程式的內定參數
 * 
 */
package tw.edu.chu.csie.e_learning.config;

public class Config {
	/**
	 * 除錯區
	 */
	// 無連線登入模擬
	public static final boolean DEBUG_NO_CONNECT_LOGIN = false; 
	
	/**
	 * 後端連線
	 */
	public static final String REMOTE_BASE_URL = "http://140.126.11.163/elearning";
	public static final String REMOTE_TEXTBOOK_URL = REMOTE_BASE_URL+"/API/v1/Material/DownloadZip.zip";
	
	/**
	 * 本機端檔案存取設定
	 */
	public static final String APP_DIRECTORY = "CHU-Elearning/";
	public static final String MATERIAL_DIRECTORY = "TeachingMaterial/";
	/**
	 * 此應用程式性質
	 */
	// 在應用程式上顯示詳細錯誤訊息
	public static final boolean DEBUG_SHOW_MESSAGE = false;
	// 此程式是否為使用者自行下載的
	// 若false就代表這程式是給導覽專用的裝置
	public static final boolean THE_APP_IS_PUBLIC = true;
	// 是否允許未經作答就返回
	public static final boolean LEARNING_BACK_ENABLE = true;
	
	/**
	 * 無帳號登入
	 */
	// 是否啟用自動填入預設帳號密碼
	public static final boolean AUTO_FILL_LOGIN = false;
	// 預設登入的帳號
	public static final String DEFAULT_LOGIN_ID = "tester";
	// 預設登入的密碼
	public static final String DEFAULT_LOGIN_PASSWORD = "123456";
	// 是否啟用自動登入
	public static final boolean AUTO_NO_ID_LOGIN = false;
	// 前端資料庫名稱
	public static final String CDB_NAME = "chu-elearn.db";
	// 前端資料庫目前版本
	public static final int CDB_VERSION = 1;
}

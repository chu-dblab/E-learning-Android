/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 *
 * Warning: 本程式包含帳號密碼等登入資訊，請勿公開 (Commit到公開專案時請注意)
 * 
 * FileName:	ConnectConfig.java
 * 
 * Description: 在這邊指定本程式連結的內定參數
 * 
 */
package tw.edu.chu.csie.e_learning.config;

public class ConnectConfig {
	/**
	 * 伺服器連結登入資訊
	 */
	//連接網址（需在最後加上斜線）
	public static final String HTTP_URL = "http://140.126.11.163/elearning/htdocs/";
	
	/**
	 * 資料庫登入資訊
	 */
	//資料庫連接網址
	public static final String SQL_URL = "localhost";
	//資料庫連接埠
	public static final int SQL_PORT = 3306;
	//資料庫登入帳號
	public static final String SQL_LOGIN_ID = "";
	//資料庫登入密碼
	public static final String SQL_LOGIN_PASSWORD = "";
}

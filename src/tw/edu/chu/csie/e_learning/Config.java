package tw.edu.chu.csie.e_learning;

public class Config {
	/**
	 * 資料庫登入資訊
	 */
	//資料庫連接網址
	public static final String SQL_URL = "localhost";
	//資料庫連接埠
	public static final String SQL_PORT = "";
	//資料庫登入帳號
	public static final String SQL_LOGIN_ID = "";
	//資料庫登入密碼
	public static final String SQL_LOGIN_PASSWORD = "";
	
	/**
	 * 無帳號登入
	 */
	//是否啟用自動填入預設帳號密碼
	public static final boolean AUTO_FILL_LOGIN = true;
	//預設登入的帳號
	public static final String DEFAULT_LOGIN_ID = "tester";
	//預設登入的密碼
	public static final String DEFAULT_LOGIN_PASSWORD = "123456";
	//是否啟用自動登入
	public static final boolean AUTO_NO_ID_LOGIN = true;
}

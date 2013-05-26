package tw.edu.chu.csie.e_learning.config;

public class Config {
	/**
	 * 此應用程式性質
	 */
	//此程式是否為使用者自行下載的
	//若false就代表這程式是給導覽專用的裝置
	public static final boolean THE_APP_IS_PUBLIC = true;
	
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

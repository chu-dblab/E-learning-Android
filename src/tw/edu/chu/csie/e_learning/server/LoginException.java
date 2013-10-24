package tw.edu.chu.csie.e_learning.server;

/**
 * 使用者登入例外處理
 * @author 元兒～ <yuan817@moztw.org>
 *
 */
public class LoginException extends Exception {
	
	//預先定義狀態碼
	public static final int NO_FOUND = 404;
	public static final int PASSWORD_ERROR = 401;
	public static final int NO_ACTIVE = 403;
	public static final int SERVER_ERROR = 500;
	
	private int statusid;
	
	public LoginException() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 輸入錯誤代碼
	 * @param input
	 */
	public LoginException(int input) {
		this.statusid = input;
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 取得錯誤代碼
	 * @return
	 */
	public int getID() {
		return this.statusid;
	}
	
	/**
	 * 取得錯誤訊息
	 * @return
	 */
	@Override
	public String getMessage() {
		// TODO 回傳錯誤描述
		switch(this.statusid) {
		case NO_FOUND:
			return "";
		default:
			return super.getMessage();
		}
	}

}

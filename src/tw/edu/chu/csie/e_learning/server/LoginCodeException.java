package tw.edu.chu.csie.e_learning.server;

/**
 * 無登入碼例外
 * @author yuan
 *
 */
public class LoginCodeException extends Exception {

	public LoginCodeException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public LoginCodeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public LoginCodeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LoginCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}

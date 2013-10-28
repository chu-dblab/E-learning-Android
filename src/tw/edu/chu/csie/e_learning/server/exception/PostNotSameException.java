package tw.edu.chu.csie.e_learning.server.exception;

/**
 * 傳送資料與伺服端接到資料不符例外
 * 防止傳送過程中被竄改
 * @author yuan
 *
 */
public class PostNotSameException extends Exception {

	public PostNotSameException() {
		// TODO Auto-generated constructor stub
	}

	public PostNotSameException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public PostNotSameException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public PostNotSameException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}

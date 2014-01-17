package tw.edu.chu.csie.e_learning.server.exception;

public class HttpException extends Exception {

	private int statusCode;
	
	public HttpException(int input_statusCode) {
		this.statusCode = input_statusCode;
	}

	public HttpException(String detailMessage) {
		super(detailMessage);
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
}

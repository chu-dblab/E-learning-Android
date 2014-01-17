package tw.edu.chu.csie.e_learning.server.exception;

public class ServerException extends Exception {

	private int statusID;
	
	//預先定義狀態碼
	public static final int CMD_ERR = 401;
	public static final int DB_ERR = 500;
	
	public ServerException() {
		// TODO Auto-generated constructor stub
	}

	public ServerException(int statusID) {
		this.statusID = statusID;
	}
	
	public ServerException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}
	
	public int getID() {
		return statusID;
	}

}

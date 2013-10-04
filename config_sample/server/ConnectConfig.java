/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	ConnectConfig.java
 * 
 * Description: 在這邊指定本程式連結的內定參數
 * 
 */
package tw.edu.chu.csie.e_learning.server;

public class ConnectConfig {
	/**
	 * 伺服器連結登入資訊
	 */
	//連接網址（需在最後加上斜線）
	public static final String BASE_URL = "http://140.126.11.163/elearning/htdocs/";
	public static final String API_URL = BASE_URL + "api/";
	public static final String TEXTBOOK_URL = BASE_URL + "textbooks/";
}

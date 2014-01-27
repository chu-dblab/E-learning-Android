/**
 * 所有和後端伺服器連結的都寫在這兒～ （暫定）
 */
package tw.edu.chu.csie.e_learning.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.LoginCodeException;
import tw.edu.chu.csie.e_learning.server.exception.LoginException;
import tw.edu.chu.csie.e_learning.server.exception.PostNotSameException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;

/**
 * 任何與伺服端的溝通，都靠此類別庫進行與伺服端的溝通
 */
public class ServerAPIs {
	
	private BaseSettings baseSettings;
	private ServerUtils utils;
	
	public ServerAPIs(BaseSettings bs) {
		this.baseSettings = bs;
		this.utils = new ServerUtils();
	}

	/**
	 * 使用者登入
	 * @param inputLoginId 登入帳號
	 * @param inputLoginPasswd 登入密碼
	 * @return 登入碼
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws LoginException
	 * @throws PostNotSameException 
	 * @throws HttpException 
	 * @throws ServerException 
	 */
	public String userLogin(String inputLoginId, String inputLoginPasswd) 
			throws ClientProtocolException, IOException, JSONException, LoginException, PostNotSameException, HttpException, ServerException, HttpHostConnectException
	{
		//傳送的資料要用NameValuePair[]包裝
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("uid",inputLoginId));
		data.add(new BasicNameValuePair("upasswd", inputLoginPasswd));
		
		//與伺服端連線
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Users/login.php?op=login", data);
			
		//若伺服端接到的uid與傳送的不合
		//判斷有沒有吻合，請用equals()函式，直接用==會有問題
		if(!new JSONObject(message).getString("uid").equals(inputLoginId)) throw new PostNotSameException();
		//若傳送給的資料是否與伺服端接到的資料相同
		else {
			//如果伺服器傳回的狀態為正常
			boolean status_ok = new JSONObject(message).getBoolean("status_ok");
			if( status_ok ) {
				return message;
				//String login_code = new JSONObject(message).getString("ucode");
				//return login_code;
			}
			//若伺服器傳回為登入失敗
			else {
				//從伺服器取得錯誤代碼
				String status = new JSONObject(message).getString("status");
				if(status.equals("NoFound")) throw new LoginException(LoginException.NO_FOUND);
				else if(status.equals("NoActiveErr")) throw new LoginException(LoginException.NO_ACTIVE);
				else if(status.equals("PasswdErr")) throw new LoginException(LoginException.PASSWORD_ERROR);
				else throw new ServerException();
			}				
			
		}
	}
	
	/**
	 * 使用者登出
	 * @param inputLoginCode 登入碼
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException
	 * @throws PostNotSameException
	 * @throws LoginCodeException
	 * @throws ServerException 
	 */
	public void userLogout(String inputLoginCode)
			throws ClientProtocolException, IOException, HttpException, JSONException, PostNotSameException, LoginCodeException, ServerException {
		//傳送的資料要用NameValuePair[]包裝
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("ucode",inputLoginCode));
		//與伺服端連線
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Users/login.php?op=logout", data);
			
		//若伺服端接到的ucode與傳送的不合
		if(!new JSONObject(message).getString("ucode").equals(inputLoginCode)) throw new PostNotSameException();
		//若傳送給的資料是否與伺服端接到的資料相同
		else {
			//如果伺服器傳回的狀態為正常
			boolean status_ok = new JSONObject(message).getBoolean("status_ok");
			if( status_ok ) {
				//String login_code = new JSONObject(message).getString("logincode");
			}
			//若伺服器傳回為登入失敗
			else {
				//從伺服器取得錯誤代碼
				String status = new JSONObject(message).getString("status");
				
				if(status.equals("NoUserFound")) throw new LoginCodeException();
				else throw new ServerException();
			}
		}
	}
	
	/**
	 * 取得使用者資訊
	 * @param inputLoginCode 登入碼
	 * @return 使用者資訊物件
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws PostNotSameException
	 * @throws JSONException
	 * @throws LoginCodeException
	 * @throws ServerException
	 */
	public ServerUser userGetInfo(String inputLoginCode) throws ClientProtocolException, IOException, HttpException, PostNotSameException, JSONException, LoginCodeException, ServerException {
		//傳送的資料要用NameValuePair[]包裝
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("ucode",inputLoginCode));
			//與伺服端連線
			String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Users/me.php?op=get-info", data);
			
			//若伺服端接到的ucode與傳送的不合
			if(!new JSONObject(message).getString("ucode").equals(inputLoginCode)) throw new PostNotSameException();
			//若傳送給的資料是否與伺服端接到的資料相同
			else {
				//如果伺服器傳回的狀態為正常
				boolean status_ok = new JSONObject(message).getBoolean("status_ok");
				if( status_ok ) {
					return new ServerUser(new JSONObject(message));
				}
				//若伺服器傳回為登入失敗
				else {
					//從伺服器取得錯誤代碼
					String status = new JSONObject(message).getString("status");
					
					if(status.equals("NoUserFound")) throw new LoginCodeException();
					else throw new ServerException();
				}
			}
	}
	
	/**
	 * 儲存使用者的學習狀態
	 * @param pointNumber 目前所在的標地
	 * @param userID 使用者名稱
	 * @param inTime 開始學習時間
	 * @param outTime 結束學習時間
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException
	 * @throws ServerException
	 */
	public void saveUserStatus(int pointNumber,String userID,String inTime,String outTime) 
			throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("uid",userID));
		data.add(new BasicNameValuePair("point",Integer.toString(pointNumber)));
		data.add(new BasicNameValuePair("inTime",inTime));
		data.add(new BasicNameValuePair("outTime", outTime));
		
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Learn/update.php?op=upgrade", data);
		boolean status_ok = new JSONObject(message).getBoolean("status_ok");
		if(!status_ok) {
			String status = new JSONObject(message).getString("status");
			if(status == "CommandError")throw new ServerException();
			else throw new ServerException();
		}
	}
	
	public int getLearnTime() throws ClientProtocolException, IOException, HttpException, JSONException, ServerException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("op", "getTotalTime"));
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Users/login.php", data);
		boolean status_ok = new JSONObject(message).getBoolean("status_ok");
		if(!status_ok) {
			String status = new JSONObject(message).getString("status");
			if(status == "CommandError")throw new ServerException();
			else throw new ServerException();
		}
		else {
			return new JSONObject(message).getInt("LearningTime");
		}
	}
	
	// ===============================================================================================
	/**
	 * 告知伺服端此標地加人數標記
	 * @param pointNumber 標地編號
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 * @throws ServerException 
	 */
	public void addPeople(String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("amount",pointNumber ));
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Learn/people.php?op=addPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException();
	}
	
	/**
	 * 告知伺服端此標地減人數標記
	 * @param pointNumber 標地編號
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException
	 * 請用Log.d();來Debug~!!
	 * @throws ServerException 
	 */
	public void subPeople(String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("amount",pointNumber ));
		String message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Learn/people.php?op=subPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException();
	}
	
	/**
	 * 取得系統推薦的下個學習點
	 * @param userID 使用者ID
	 * @param pointNumber 剛剛學習過的標地編號
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 * @throws ServerException
	 */
	public String getPointIdOfLearningPoint(String userID,String pointNumber) throws ServerException, JSONException, ClientProtocolException, IOException, HttpException 
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("uid",userID));
		param.add(new BasicNameValuePair("point",pointNumber));
		String message = message = this.utils.getServerData(this.baseSettings.getApiUrl()+"Learn/people.php?op=recommand", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException(ServerException.DB_ERR);
		else 
		{
			String tmp = new JSONObject(message).getString("nextNode");
			return tmp;
		}
	}
}

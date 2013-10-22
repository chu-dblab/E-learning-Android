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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerUtils {
	
	private BaseSettings baseSettings;
	
	public ServerUtils(BaseSettings bs) {
		this.baseSettings = bs;
	}

	/**
	 * 使用者登入
	 * @param inputLoginId 登入帳號
	 * @param inputLoginPasswd 登入密碼
	 * @return 登入碼
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public String userLogin(String inputLoginId, String inputLoginPasswd) 
			throws ClientProtocolException, IOException, JSONException
	{
		//傳送的資料要用NameValuePair[]包裝
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("uid",inputLoginId));
		data.add(new BasicNameValuePair("upasswd", inputLoginPasswd));
		
		
		//建立HttpPost連線
		HttpPost post = new HttpPost(this.baseSettings.getApiUrl()+"Users/login.php?op=login");
		//接收HttpResponse
		post.setEntity(new UrlEncodedFormEntity(data,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient().execute(post);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			//解析從後端傳回的資料
			String message = EntityUtils.toString(response.getEntity());
			String login_code = new JSONObject(message).getString("logincode");
			// TODO 其他狀況處理（無帳號、密碼錯誤）
			return login_code;
		}
		else return null;
	}
}

/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	AccountUtils.java
 * 
 * Description: 帳號控制管理（登入、驗證、登入狀況...）的程式
 * 
 */
package tw.edu.chu.csie.e_learning.util;

import java.io.IOException;
import java.util.*;

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

import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.server.ConnectConfig;

public class AccountUtils {
	
	private boolean isLogined;
	private String loginedId;
	private String loginCode;
	private ClientDBProvider clientdb = new ClientDBProvider();
	
	
	/**
	 * 是否是已登入狀態
	 */
	public boolean islogin(){
		return isLogined;
	}
	
	/**
	 * 察看已登入的ID
	 */
	public String getLoginId() {
		return loginedId;
	}
	
	/**
	 * 取得登入碼
	 */
	public String getLoginCode() {
		return loginCode;
	}
	
	/**
	 * 登入帳號
	 * @param inputLoginId 使用者輸入的ID
	 * @param inputLoginPasswd 使用者輸入的密碼
	 * @return 是否登入成功
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public boolean loginUser(String inputLoginId, String inputLoginPasswd) 
			throws ClientProtocolException, IOException, JSONException{
		//傳送的資料要用NameValuePair[]包裝
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("mID",inputLoginId));
		data.add(new BasicNameValuePair("mPassword", inputLoginPasswd));
		
		//建立HttpPost連線
		HttpPost post = new HttpPost(ConnectConfig.API_URL+"Users/login.php?op=login");
		//接收HttpResponse
		post.setEntity(new UrlEncodedFormEntity(data,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient().execute(post);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			//解析從後端傳回的資料
			String message = EntityUtils.toString(response.getEntity());
			String status_code = new JSONObject(message).getString("code");
			loginCode = status_code;
			//將傳回來的資料寫入SQLite裡
			clientdb.update("user", status_code, null, null);
			isLogined = true;
			return true;
		}
		else return false;
	}
	/**
	 * 登出帳號
	 * @return 是否登出成功
	 */
	public boolean logoutUser(String loginID){
		//TODO 將使用者的學習狀態傳送至後端
		//清除登入資訊
		clientdb.delete("ID = "+loginID, "user");
		isLogined = false;
		return true;
	}
}

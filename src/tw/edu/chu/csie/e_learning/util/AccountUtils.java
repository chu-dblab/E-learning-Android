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

import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.server.BaseSettings;
import tw.edu.chu.csie.e_learning.server.ServerAPIs;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.LoginException;
import tw.edu.chu.csie.e_learning.server.exception.PostNotSameException;

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
	 * @throws PostNotSameException 
	 * @throws HttpException 
	 */
	public boolean loginUser(String inputLoginId, String inputLoginPasswd) 
			throws ClientProtocolException, IOException, JSONException, LoginException, PostNotSameException, HttpException
	{
		BaseSettings srvbs = new BaseSettings();
		srvbs.setBaseUrl(Config.REMOTE_BASE_URL);
		
		ServerAPIs server = new ServerAPIs(srvbs);
		
		try {
			this.loginCode = server.userLogin(inputLoginId, inputLoginPasswd);
			
			//將傳回來的資料寫入SQLite裡
			this.clientdb.update("user", this.loginCode, null, null);
			this.isLogined = true;
			
			return true;
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (JSONException e) {
			throw e;
		} catch (LoginException e) {
			throw e;
		} catch (PostNotSameException e) {
			throw e;
		}		
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

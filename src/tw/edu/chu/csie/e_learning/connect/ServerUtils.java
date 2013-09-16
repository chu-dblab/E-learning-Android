/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	ServerUtils.java
 * 
 * Description: 處理要連結到伺服器的程式
 * 
 */
package tw.edu.chu.csie.e_learning.connect;

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

import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;

public class ServerUtils 
{
	private String ID;
	private String password;
	private String address;
	public ServerUtils() 
	{	
		ID = null;
		password = null;
	}
	
	public void setID(String userID)
	{
		ID = userID;
	}
	
	public void setPassword(String userPassword)
	{
		password = userPassword;
	}
	
	public void setAddress(String url)
	{
		address = url;
	}
	
	public boolean connect2Server() throws IOException,ClientProtocolException, JSONException
	{
		//建立HttpPost連線
		HttpPost post = new HttpPost(address);
		
		// 用POST傳送的資料要用NameValuePair[]包裝
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("mId", ID));
		data.add(new BasicNameValuePair("mPassword", password));
		
		//發出HttpRequest
		post.setEntity(new UrlEncodedFormEntity(data,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient().execute(post);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			//解析從後端傳回的資料
			String message = EntityUtils.toString(response.getEntity());
			String status_code = new JSONObject(message).getString("code");
			//將傳回來的資料寫入SQLite裡
			ClientDBProvider clientdb = new ClientDBProvider();
			clientdb.update("user", status_code, null, null);
			return true;
		}
		else return false;
	}
	
	public boolean isConnected()
	{
		if()
	}
}

package tw.edu.chu.csie.e_learning.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tw.edu.chu.csie.e_learning.server.BaseSettings;
import tw.edu.chu.csie.e_learning.server.ServerUtils;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;

public class LearningUtils 
{
	private ServerUtils connect;
	private BaseSettings bs;
	public LearningUtils()
	{
		connect = new ServerUtils();
	}
	
	/**
	 * 加人數
	 * @param pointNumber
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 * 請用Log.d();來Debug~!!
	 */
	public String addPeople(String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("amount",pointNumber ));
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=addPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) return new JSONObject(message).getString("status");
		else return new JSONObject(message).getString("status");
	}
	
	/**
	 * 減人數
	 * @param pointNumber
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException
	 * 請用Log.d();來Debug~!!
	 */
	public String subPeople(String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("amount",pointNumber ));
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=subPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) return new JSONObject(message).getString("status");
		else return new JSONObject(message).getString("status");
	}
	
	/**
	 * TODO
	 * 取得系統推薦的下個學習點
	 * @param userID
	 * @param pointNumber
	 * @return
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public String getPointIdOfLearningPoint(String userID,String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException 
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("ID",userID));
		param.add(new BasicNameValuePair("point",pointNumber));
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=recommand", param);
		boolean status = new JSONObject(message).getBoolean("status");
		if(!status) return new JSONObject(message).getString("status");
		else return new JSONObject(message).getString("data");
	}
	
	
}
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

import android.content.Context;
import tw.edu.chu.csie.e_learning.provider.ClientDBProvider;
import tw.edu.chu.csie.e_learning.server.BaseSettings;
import tw.edu.chu.csie.e_learning.server.ServerUtils;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;

public class LearningUtils 
{
	private ServerUtils connect;
	private BaseSettings bs;
	private JSONDecodeUtils decode;
	private ClientDBProvider dbcon;
	public LearningUtils(Context context)
	{
		connect = new ServerUtils();
		decode = new JSONDecodeUtils();
		dbcon = new ClientDBProvider(context);
	}
	
	
	/**
	 * 加人數
	 * @param pointNumber
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
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=addPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException();
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
	 * @throws ServerException 
	 */
	public void subPeople(String pointNumber) throws ClientProtocolException, IOException, HttpException, JSONException, ServerException
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("amount",pointNumber ));
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=subPeople", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException();
	}
	
	/**
	 * TODO
	 * 取得系統推薦的下個學習點
	 * @param userID
	 * @param pointNumber
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 * @throws ServerException
	 */
	public void getPointIdOfLearningPoint(String userID,String pointNumber) throws ServerException,ClientProtocolException, IOException, HttpException, JSONException 
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("uid",userID));
		param.add(new BasicNameValuePair("point",pointNumber));
		String message = connect.getServerData(bs.getApiUrl()+"Learn/people.php?op=recommand", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) throw new ServerException(ServerException.DB_ERR);
		else 
		{
			String tmp = new JSONObject(message).getString("nextNode");
			decode.DecodeJSONData(tmp,"first");
			dbcon.target_insert(decode.getNextPoint(), decode.getMapURL(), decode.getMaterialURL(), decode.getEstimatedStudyTime());
		}
	}
	
	/**
	 * 
	 * @param userID
	 * @param pointNumber
	 * @param inTime
	 * @param outTime
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException
	 */
	public String postDataToServer(String userID,String pointNumber,String inTime,String outTime) throws ClientProtocolException, IOException, HttpException, JSONException 
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("uid", userID));
		param.add(new BasicNameValuePair("point", pointNumber));
		param.add(new BasicNameValuePair("inTime", inTime));
		param.add(new BasicNameValuePair("outTime", outTime));
		
		String message = connect.getServerData(bs.getApiUrl()+"Learn/update.php?op=upgrade", param);
		boolean status = new JSONObject(message).getBoolean("status_ok");
		if(!status) return new JSONObject(message).getString("status");
		else return new JSONObject(message).getString("status");
	}
}

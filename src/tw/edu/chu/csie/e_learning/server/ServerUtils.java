package tw.edu.chu.csie.e_learning.server;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tw.edu.chu.csie.e_learning.server.exception.HttpException;

public class ServerUtils {

	public ServerUtils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 取得伺服端"整個"內容
	 * @param url 目的網址
	 * @param data POST帶入的資料
	 * @return "整個"內容字串
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	public String getServerData(String url, List<NameValuePair> data) throws ClientProtocolException, IOException, HttpException {
		//建立HttpPost連線
		HttpPost post = new HttpPost(url);
		
		//接收HttpResponse
		post.setEntity(new UrlEncodedFormEntity(data,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient().execute(post);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			//取得從後端傳回的資料
			String message = EntityUtils.toString(response.getEntity());
			return message;
		}
		else throw new HttpException(response.getStatusLine().getStatusCode());
	}
}

/**
 * 解析內層JSON型態的輔助類別
 */
package tw.edu.chu.csie.e_learning.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONDecodeUtils 
{
	private int nextPoint;
	private String estimatedStudyTime;
	private String mapURL;
	private String materialURL;
	private JSONObject jsonData;
	
	public JSONDecodeUtils()
	{
		nextPoint = 0;
		estimatedStudyTime = "";
		mapURL = "";
		materialURL = "";
	}
	
	/**
	 * 解析JSON
	 * @param String data JSON型態的字串
	 * @throws JSONException 
	 */
	public void DecodeJSONData(String data,String orderOfNode) throws JSONException
	{
		jsonData = new JSONObject(data).getJSONObject(orderOfNode);
		nextPoint = jsonData.getInt("node");
		estimatedStudyTime = jsonData.getString("LearnTime");
		mapURL = jsonData.getString("MapUrl");
		materialURL = jsonData.getString("MaterialUrl");
	}
	
	/**
	 * 取得下一個學習點的編號
	 * @return nextPoint
	 */
	public String getNextPoint()
	{
		return Integer.toString(nextPoint);
	}
	
	/**
	 *  取得下一個學習點的預估學習時間
	 * @return　estimatedStudyTime
	 */
	public String getEstimatedStudyTime()
	{
		return estimatedStudyTime;
	}
	
	/**
	 *  取得下一個學習點的地圖檔名(含副檔名)
	 * @return　mapURL
	 */
	public String getMapURL()
	{
		return mapURL;
	}
	
	/**
	 * 取得下一個學習點的教材檔名(含副檔名)
	 * @return　materialURL
	 */
	public String getMaterialURL()
	{
		return materialURL;
	}
	
	public JSONObject getJSONData()
	{
		return jsonData;
	}
	
}

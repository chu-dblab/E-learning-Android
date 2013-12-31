/**
 * 解析內層JSON型態的輔助類別
 */
package tw.edu.chu.csie.e_learning.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONDecodeUtils 
{
	private int nextPoint;
	private int estimatedStudyTime;
	private String mapURL;
	private String materialURL;
	private JSONObject jsonData;
	
	public JSONDecodeUtils()
	{
		nextPoint = 0;
		estimatedStudyTime = 0;
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
		estimatedStudyTime = jsonData.getInt("LearnTime");
		mapURL = jsonData.getString("MapUrl");
		materialURL = jsonData.getString("MaterialUrl");
	}
	
	/**
	 * 取得下一個學習點的編號
	 * @return nextPoint
	 */
	public int getNextPoint()
	{
		return nextPoint;
	}
	
	/**
	 *  取得下一個學習點的預估學習時間
	 * @return　estimatedStudyTime
	 */
	public int getEstimatedStudyTime()
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

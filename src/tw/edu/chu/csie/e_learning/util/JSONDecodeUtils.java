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
	private int isEntity;
	private String targetName;
	private String mapURL;
	private String materialURL;
	private JSONObject jsonData;
	
	public JSONDecodeUtils()
	{
		nextPoint = 0;
		estimatedStudyTime = 0;
		isEntity = 0;
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
		targetName = jsonData.getString("TName");
		mapURL = jsonData.getString("MapURL");
		materialURL = jsonData.getString("MaterialUrl");
		isEntity = jsonData.getInt("isEntity");
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
	 * 取得標的名稱
	 * @return
	 */
	public String getTargetName()
	{
		return targetName;
	}
	
	/**
	 * 取得此學習點是否為實體學習點
	 * @return
	 */
	public int getIsEntity() 
	{
		return isEntity;
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

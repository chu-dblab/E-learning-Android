/**
 * @author kobayashi
 * @description 一切對Android儲存裝置的存取都從這個類別呼叫
 */
package tw.edu.chu.csie.e_learning.util;

import java.io.File;

import android.content.Context;
import android.os.*;

public class FileUtils 
{
	private File BasicSDPath;  //SD卡的根目錄（會依照Android版本不同而有所變化）
	private Context BasicInternalPath;		//內部儲存裝置的根目錄(預設是/data/data/[package.name]/files/)
	
	public FileUtils() 
	{
		BasicSDPath = Environment.getExternalStorageDirectory();
	}
	
	/**
	 * @title			isSDCardInsert
	 * @description	偵測這個裝置有沒有插入記憶卡
	 * @param			None
	 * @return			Boolean
	 */
	public boolean isSDCardInsert() 
	{
		if(!BasicSDPath.equals(Environment.MEDIA_REMOVED))
		{
			return true;  //SD卡已經插上去了
		}
		else return false;	//沒插入SD卡
	}
	
	/**
	 * @title			getSDPath
	 * @description	取得在SD卡上的教材路徑
	 * @param			None
	 * @return 		學習教材在SD卡上的路徑
	 */
	public String getSDPath()
	{
		return BasicSDPath+"/TeachingMaterial";
	}
	
	/**
	 * @title			getInternalStorgePath
	 * @description	取得在內部儲存裝置上的教材路徑
	 * @param			None
	 * @return 		學習教材在內部儲存裝置上的路徑
	 */
	public String getInternalStorgePath()
	{
		return BasicInternalPath+"/TeachingMaterial";
	}
	
	
	/**
	 * TODO
	 * @tile	saveFile
	 * @description	下載檔案時存檔用
	 * @param path
	 * @return
	 */
	public void saveFile(String path)
	{
		File savePath = new File(path);
		if(!savePath.exists()) //如果傳進來的資料夾路徑沒有這個資料夾
		{
			savePath.mkdir();   //建立資料夾
			
			//將檔案存到該路徑底下
		}
		else
		{
			//將檔案存到該路徑底下
		}		
	}
	
	/**
	 * TODO
	 * @tile	ReadFile
	 * @description	讀檔
	 * @param path
	 * @return
	 */
	public void ReadFile(String path)
	{
		File readPath = new File(path);
		//讀檔
	}
}

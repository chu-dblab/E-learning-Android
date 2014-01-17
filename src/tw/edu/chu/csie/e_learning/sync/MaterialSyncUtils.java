package tw.edu.chu.csie.e_learning.sync;

import java.io.IOException;
import java.net.*;

import tw.edu.chu.csie.e_learning.server.exception.HttpException;

import android.util.Log;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.util.FileUtils;

/**
 * 下載教材的類別
 * 一切有關下載與同步教材都在這個類別處理
 * @author kobayashi
 * @version v1.0
 */
public class MaterialSyncUtils 
{
	private HttpURLConnection url;
	private URL downloadPath;
	private FileUtils saveFile = new FileUtils();
	private int fileSize;
	
	
	/**
	 * 下載教材
	 * TODO 補上網址
	 * @param 　urlPath
	 * @throws IOException
	 * @throws HttpException 連線錯誤
	 */
	public void downloadTeachingMaterial(String urlPath) throws IOException,HttpException
	{
		downloadPath = new URL(urlPath);
		url = (HttpURLConnection)downloadPath.openConnection();
		url.setDoInput(true);
		url.setRequestMethod("POST");
		url.setConnectTimeout(20000);
		fileSize = url.getContentLength();
		if(url.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			String path = saveFile.getPath();
			saveFile.saveFile(path+Config.ZIP_FILE_NAME_OF_MATERIAL, url.getInputStream(),url);
			Log.d("Download", "存檔成功～！！");
			saveFile.decompressFile();
			Log.d("Download", "解壓縮檔案成功～！！");
		}
		else throw new HttpException(url.getResponseCode());
	}
	
	/**
	 * 取得檔案大小
	 * @return 檔案大小 TODO 以什麼單位計算
	 */
	public int getFileSize()
	{
		return fileSize;
	}
}

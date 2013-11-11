/**
 * TextbookSyncUtils　下載教材的類別
 * @author kobayashi
 * @version v1.0
 * 
 * 一切有關下載教材與同步都在這個類別處理
 */
package tw.edu.chu.csie.e_learning.sync;

import java.io.IOException;
import java.net.*;
import java.util.zip.*;

import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.util.FileUtils;

public class TextbookSyncUtils 
{
	private HttpURLConnection url;
	private URL downloadPath;
	private FileUtils saveFile;
	
	
	/**
	 * 下載教材
	 * @param 　urlPath
	 * @return true/false
	 * @throws IOException
	 */
	public boolean downloadTeachingMaterial(String urlPath) throws IOException
	{
		downloadPath = new URL(urlPath);
		url = (HttpURLConnection)downloadPath.openConnection();
		url.setDoInput(true);
		url.setRequestMethod("POST");
		url.setConnectTimeout(10000);
		if(url.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			String path = saveFile.getPath();
			saveFile.saveFile(path, url.getInputStream());
			return true;
		}
		return false;
	}
	
	/**TODO
	 * 解壓縮檔案
	 */
	public void decompression()
	{
		
	}
}

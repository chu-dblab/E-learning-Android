/**
 * @author kobayashi
 * @description 一切對Android儲存裝置的存取都從這個類別呼叫
 */
package tw.edu.chu.csie.e_learning.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tw.edu.chu.csie.e_learning.config.Config;
import android.content.Context;
import android.os.*;
import android.util.Log;

public class FileUtils 
{
	private File BasicSDPath;  //SD卡的根目錄（會依照Android版本不同而有所變化）
	private Context BasicInternalPath;		//內部儲存裝置的根目錄(預設是/data/data/[package.name]/files/)
	
	public FileUtils() 
	{
		BasicSDPath = Environment.getExternalStorageDirectory();
	}
	
	/**
	 * isSDCardInsert
	 * @param			None
	 * @return			Boolean
	 * 偵測這個裝置有沒有插入記憶卡
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
	 * getSDPath
	 * @param			None
	 * @return 		學習教材在SD卡上的路徑
	 * 取得在SD卡上的教材路徑
	 */
	public String getPath()
	{
		if(isSDCardInsert())
		{
			File path = new File(BasicSDPath+"/"+Config.APP_DIRECTORY+Config.MATERIAL_DIRECTORY);
			if(!path.exists()) 
			{
				path.mkdirs();
				return path.getAbsolutePath();
			}
			else return BasicSDPath+"/"+Config.APP_DIRECTORY+Config.MATERIAL_DIRECTORY;
		}
		else
		{
			File path = new File(BasicInternalPath+"/"+Config.MATERIAL_DIRECTORY);
			if(!path.exists())
			{
				path.mkdir();
				return BasicInternalPath+"/"+Config.MATERIAL_DIRECTORY;
			}
			return BasicInternalPath+"/"+Config.MATERIAL_DIRECTORY;
		}
	}
	
	/**
	 * saveFile
	 * @param 			path
	 * @return			None
	 * @throws 		IOException 
	 * 下載檔案時存檔用
	 */
	public void saveFile(String path,InputStream is,HttpURLConnection con) throws IOException
	{
		File savePath = new File(path);
		FileOutputStream write = new FileOutputStream(path);
		int str1 = 0;;
		byte[] data = new byte[1024];
		while((str1=is.read(data)) != -1)
		{
			write.write(data,0,str1);
		}
		write.flush();
		write.close();
		is.close();
		con.disconnect();
	}
	
	/**
	 * decompressFile
	 * 解壓縮檔案
	 * @throws IOException
	 */
	
	public void decompressFile() throws IOException
	{
		//打開要解壓縮的檔案
		FileInputStream fin = new FileInputStream(getPath()+Config.ZIP_FILE_NAME_OF_MATERIAL);
		BufferedInputStream in = new BufferedInputStream(fin);
		ZipInputStream zipInput = new ZipInputStream(in);

		//取得壓縮檔的目的資料夾
		ZipEntry next = zipInput.getNextEntry();
		Log.d("Name", next.getName());
		File tmp = new File(getPath()+next.getName());
		String unzipDirectory = getPath();
		if(!next.isDirectory()) //如果壓縮檔內的目的資料夾不是目錄的話
		{
			//先判斷上一層資料夾是否存在，若不存在則先建立資料夾，再解壓縮檔案			
			File save = tmp.getParentFile();
			if(!save.exists()) save.mkdirs();
			
			//解壓縮
			outputToStorage(tmp,zipInput);
			
			//將解壓縮完成的zip檔刪除
			//File remove = new File(getPath()+Config.FILE_NAME_OF_MATERIAL);
			//remove.delete();
		}
		else outputToStorage(tmp,zipInput);
	}
	/**
	 * outputToStorage
	 * 將解壓縮後的資料寫入檔案之後儲存
	 * @param fobj
	 * @param zIn
	 * @throws IOException
	 */
	private void outputToStorage(File fobj,ZipInputStream zIn) throws IOException
	{
		//開啟解壓縮要寫入的檔案
		FileOutputStream output = new FileOutputStream(fobj);
		
		//以byte讀取解壓縮後的資料
		int data = 0;
		byte[] buf = new byte[1024];
		
		//寫入檔案，然後關閉所有檔案相關串流
		while((data=zIn.read(buf)) != -1) output.write(buf,0,data);
		output.flush();
		output.close();
		zIn.close();
	}
}

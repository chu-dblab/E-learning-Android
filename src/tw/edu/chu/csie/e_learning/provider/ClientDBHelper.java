package tw.edu.chu.csie.e_learning.provider;

import tw.edu.chu.csie.e_learning.config.Config;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class ClientDBHelper extends SQLiteOpenHelper {
	
	//資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
	public ClientDBHelper(Context context,String chu_elearn, CursorFactory factory, int version) {
		super(context,Config.CDB_NAME,null,Config.CDB_VERSION); //資料庫名稱=chu-elearn，目前版本=1
	}
	
	//使用者資料表
	private static final String us = 
				"CREATE TABLE chu_user ( UID varchar(30) NOT NULL, UNickname varchar(20) DEFAULT NULL, ULogged_code varchar(32) DEFAULT NULL, In_Learn_Time varchar(50) NOT NULL, TLearn_Time INTEGER NOT NULL, PRIMARY KEY (UID));"; 
	//標的資料表
	private static final String tar = 
				"CREATE TABLE chu_target ( TID INTEGER UNSIGNED NOT NULL, TName varchar(15) NOT NULL, MapID varchar(20) NOT NULL, MaterialID varchar(20) NOT NULL, TLearn_Time INTEGER UNSIGNED NOT NULL, IsEntity INTEGER UNSIGNED NOT NULL, PRIMARY KEY (TID));";
	//輔助類建立時運行該方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(us); //建立chu_user
		db.execSQL(tar); //建立chu_target
		Log.d("success", "建表成功!!");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		// TODO Auto-generated method stub
	}
}


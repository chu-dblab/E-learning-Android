package tw.edu.chu.csie.e_learning.provider;

import tw.edu.chu.csie.e_learning.config.Config;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ClientDBHelper extends SQLiteOpenHelper {
	
	//資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
	@SuppressWarnings("unused")
	//private static final int VERSION = 1;//資料庫版本
	
	private SQLiteDatabase sqlitedatabase; 
	private Context context;
	private ClientDBHelper dbHelper;
	
	//使用者資料表
	private static final String us = 
				"CREATE TABLE user ( UID INTEGER unsigned NOT NULL, UName varchar(20) NOT NULL, ULogged_no varchar(40) default NULL, In_Learn_Time datetime NOT NULL, PRIMARY KEY (UID));"; 
	//標的資料表
	private static final String tar = 
				"CREATE TABLE target ( TID INTEGER unsigned NOT NULL, MapID INTEGER unsigned NOT NULL, Map_Url varchar(50) NOT NULL, MaterialID INTEGER unsigned NOT NULL, Material_Url varchar(50) NOT NULL, PRIMARY KEY (TID));";
	//學習關係資料表
	private static final String sstudy = 
				"CREATE TABLE study ( TID INTEGER unsigned NOT NULL, UID INTEGER unsigned NOT NULL, QID INTEGER unsigned default NULL, Answer varchar(5) default NULL, Answer_Time varchar(10) default NULL, In_TargetTime datetime NOT NULL, Out_TargetTime datetime default NULL, TCheck varchar(5) NOT NULL, FOREIGN KEY (UID) REFERENCES user, FOREIGN KEY (TID) REFERENCES target, PRIMARY KEY(UID,TID));";
		
	
	public ClientDBHelper(Context context, String chu_elearn, CursorFactory factory, int version) {
		super(context,chu_elearn,null,version); //資料庫名稱=chu-elearn，目前版本=1
		// TODO Auto-generated constructor stub
	}

	//輔助類建立時運行該方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(us); //建立student
		db.execSQL(tar); //建立target
		db.execSQL(sstudy); //建立study

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		// TODO Auto-generated method stub

	}
	
	
	public ClientDBHelper openToWrite() throws android.database.SQLException{
		
		dbHelper = new ClientDBHelper(context,Config.Chu_elearn,null,Config.version);
		sqlitedatabase = dbHelper.getWritableDatabase();
		
		return this;
	}

	public ClientDBHelper openToRead() throws android.database.SQLException{ 
		
		dbHelper = new ClientDBHelper(context,Config.Chu_elearn,null,Config.version);
		sqlitedatabase = dbHelper.getReadableDatabase();
		
		return this;
	}
	
	@Override   
	public void onOpen(SQLiteDatabase db) {    
		super.onOpen(db);
		// TODO 每次成功打開數據庫後首先被執行
	}
	
	@Override
        public synchronized void close() {
		super.close();
	}
	

}


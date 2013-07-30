package tw.edu.chu.csie.e_learning.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ClientDBHelper extends SQLiteOpenHelper {
	
	//資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
	@SuppressWarnings("unused")
	private static final int VERSION = 1;//資料庫版本
	
	
	public ClientDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	//輔助類建立時運行該方法
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		// TODO Auto-generated method stub

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

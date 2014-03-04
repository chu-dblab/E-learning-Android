package tw.edu.chu.csie.e_learning.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import tw.edu.chu.csie.e_learning.config.Config;
import android.widget.Toast;

public class ClientDBProvider {
	private Context context;
	public String select ="";
	private static int i = 0;
	private SQLiteDatabase sqlitedatabase;
	ClientDBHelper db;
	
	/**
	 * 建構子
	 * @param context 帶入Android基底Context
	 */
	public ClientDBProvider(Context context)
	{
		this.context = context;
		db = new ClientDBHelper(this.context, Config.CDB_NAME, null, Config.CDB_VERSION);
	}
	
	/**
	 * 新增使用者當資料庫
	 * @param Uid 帳號ID
	 * @param Unickname 帳號暱稱
	 * @param Ulogged_code 本次登入碼
	 * @param In_learn_time 開始學習時間（登入時間）
	 * @return
	 */
	public long user_insert(String Uid,String Unickname,String Ulogged_code,String In_learn_time, String TLearn_Time){ //"使用者"新增
	
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("UID", Uid);
		contentvalues.put("UNickname", Unickname);
		contentvalues.put("ULogged_code", Ulogged_code);
		contentvalues.put("In_Learn_Time", In_learn_time);
		contentvalues.put("TLearn_Time", TLearn_Time);
		return sqlitedatabase.insert("chu_user", null, contentvalues);
	}

	/**
	 * 新增已推薦的標地進資料庫
	 * @param Tid 標地編號
	 * @param Tname 標地名稱
	 * @param mapID 此標地的地圖檔案名稱
	 * @param materialID 此標地的教材檔案名稱
	 * @param Tlearn_time 此標地估計學習時間
	 * @param Isentity 此學習點是否為實體教材
	 * @return
	 */
	public long target_insert(int Tid,String Tname,String mapID,String materialID,int Tlearn_time,int Isentity){ //"標的"新增
		
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("TID", Tid);
		contentvalues.put("TName", Tname);
		contentvalues.put("MapID", mapID);
		contentvalues.put("MaterialID", materialID);
		contentvalues.put("TLearn_Time", Tlearn_time);
		contentvalues.put("IsEntity", Isentity);
		return sqlitedatabase.insert("chu_target", null, contentvalues);
	}	
	
	/**
	 * 刪除一筆紀錄
	 * @param where_string 查詢條件
	 * @param user_table 查詢資料表格
	 * @return
	 */
	public long delete(String where_string,String user_table){ 
		
		openToWrite();
		if(user_table == "chu_user")
		{
			if(where_string == null)
				return sqlitedatabase.delete(user_table, null, null);
			else
				return sqlitedatabase.delete(user_table, where_string, null);
		}
		else
		{
			if(where_string == null)
				return sqlitedatabase.delete(user_table, null, null);
			else
				return sqlitedatabase.delete(user_table, where_string, null);
		}
	}

	/**
	 * 更改資料庫內容
	 * @param user_table 查詢資料表格
	 * @param newv1 登入碼/標地名稱
	 * @param newv2 開始學習時間/此學習點地圖檔名
	 * @param newv3 此學習點教材檔名
	 * @param where_string 查詢條件
	 * @return
	 */
	public long update(String user_table,String newv1,String newv2,String newv3,String where_string){
		
		openToWrite();
		ContentValues contentvalues = new ContentValues();
			if(user_table == "chu_user")
			{
				contentvalues.put("ULogged_code", newv1);
				contentvalues.put("In_Learn_Time", newv2);
			}
			if(user_table == "chu_target")
			{
				contentvalues.put("TName", newv1);
				contentvalues.put("MapID", newv2);
				contentvalues.put("MaterialID", newv3);
			}
			if(where_string == null)
				return sqlitedatabase.update(user_table, contentvalues, null, null);
			else
				return sqlitedatabase.update(user_table, contentvalues, where_string, null);
	}
	
	/**
	 * 查詢資料
	 * @param user_table 資料表名稱
	 * @param search_item 查詢哪些欄位
	 * @param where_string 查詢條件
	 * @return 陣列字串
	 */
	public String[] search(String user_table,String search_item,String where_string){ //查詢
		
		openToRead();
		if(where_string == null){
			select = "SELECT" + " " + search_item + " " + "FROM" + " " + user_table + ";";
		}
		else{
			select = "SELECT" + " " + search_item + " " + "FROM" + " " + user_table + " " + "WHERE" + " " + where_string;
		}
		Cursor cursor = sqlitedatabase.rawQuery(select, null);
		int num = cursor.getCount(); //取得資料表列數
		String[] sNote = new String[cursor.getCount()];
		String result = "";
		if(num != 0) 
		{
			cursor.moveToFirst();   //將指標移至第一筆資料
			for(int i=0; i<num; i++) {
				String strCr = cursor.getString(0);
				sNote[i]=strCr; 
				cursor.moveToNext();//將指標移至下一筆資料
			}
		}
		cursor.close(); //關閉Cursor
		return sNote;
	}
	
	public ClientDBProvider openToWrite() throws android.database.SQLException{
		
		sqlitedatabase = db.getWritableDatabase();
		
		return this;
	}

	public ClientDBProvider openToRead() throws android.database.SQLException{ 
		
		sqlitedatabase = db.getReadableDatabase();
		
		return this;
	}
	
	/**
	 * 開啟資料庫
	 * @param db
	 */
	public void onOpen(SQLiteDatabase db) {
		this.db.onOpen(db);
		// TODO 每次成功打開數據庫後首先被執行
	}
	
	/**
	 * 關閉資料庫
	 */
	public void close() {
		this.db.close();
	}
	
}

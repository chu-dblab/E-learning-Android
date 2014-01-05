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
	
	public ClientDBProvider(Context context)
	{
		this.context = context;
		db = new ClientDBHelper(this.context, Config.CDB_NAME, null, Config.CDB_VERSION);
	}
	
	public long user_insert(String Uid,String Unickname,String Ulogged_code,String In_learn_time){ //"使用者"新增
	
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("UID", Uid);
		contentvalues.put("UNickname", Unickname);
		contentvalues.put("ULogged_code", Ulogged_code);
		contentvalues.put("In_Learn_Time", In_learn_time);
		return sqlitedatabase.insert("chu_user", null, contentvalues);
	}

	public long target_insert(int Tid,String Tname,String mapID,String materialID,int Tlearn_time){ //"標的"新增
		
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("TID", Tid);
		contentvalues.put("TName", Tname);
		contentvalues.put("MapID", mapID);
		contentvalues.put("MaterialID", materialID);
		contentvalues.put("TLearn_Time", Tlearn_time);
		return sqlitedatabase.insert("chu_target", null, contentvalues);
	}	
	
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
	
	public void onOpen(SQLiteDatabase db) {
		this.db.onOpen(db);
		// TODO 每次成功打開數據庫後首先被執行
	}
	
	public void close() {
		this.db.close();
	}
	
}

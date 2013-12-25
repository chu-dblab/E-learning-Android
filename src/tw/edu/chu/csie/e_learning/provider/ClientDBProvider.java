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
	
	//查詢資料庫內容，並將所有結果存到result裡
	public String All(String chtab){
		select = "SELECT * FROM"+" "+chtab;
		Cursor cursor = sqlitedatabase.rawQuery(select, null);
		String result = "";
		if(chtab == "chu_user"){
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
					result = result + cursor.getString(0) +"："+ cursor.getString(1) +"："+ cursor.getString(2)+ "\n";
			}
		}
		else
		{
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				result = result + cursor.getString(0) +"："+ cursor.getString(1) +"："+ cursor.getString(2)+"："+ cursor.getString(3)+"："+ cursor.getString(4)+ "\n";
			}
		}
		return result;
	}

	public long user_insert(String v1,String v2,String v3,String v4){ //"使用者"新增
	
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("UID", v1);
		contentvalues.put("UNickname", v2);
		contentvalues.put("ULogged_code", v3);
		contentvalues.put("In_Learn_Time", v4);
		return sqlitedatabase.insert("chu_user", null, contentvalues);
	}

	public long target_insert(String v1,String v2,String v3,String v4,String v5){ //"標的"新增
		
		openToWrite();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("TID", v1);
		contentvalues.put("MapID", v2);
		contentvalues.put("MaterialID", v3);
		contentvalues.put("Map_Url", v4);
		contentvalues.put("Material_Url", v5);
		return sqlitedatabase.insert("chu_target", null, contentvalues);
	}	
	
	public long delete(String where_string,String user_table){ 
		
		openToWrite();
		if(user_table == "chu_user")
			return sqlitedatabase.delete(user_table, where_string, null);
		else
			return sqlitedatabase.delete(user_table, where_string, null);
	}


	public long update(String user_table,String newv1,String newv2,String where_string){
		
		openToWrite();
		ContentValues contentvalues = new ContentValues();
			if(user_table == "chu_user")
			{
				contentvalues.put("ULogged_code", newv1);
				contentvalues.put("In_Learn_Time", newv2);
			}
			if(user_table == "chu_target")
			{
				contentvalues.put("Map_Url", newv1);
				contentvalues.put("Material_Url", newv2);
			}
			return sqlitedatabase.update(user_table, contentvalues, where_string, null);
	}
	
	public String search(String user_table,String search_item,String where_string){ //查詢
		
		openToRead();
		if(where_string == null){
			select = "SELECT" + " " + search_item + " " + "FROM" + " " + user_table;
		}
		else{
			select = "SELECT" + " " + search_item + " " + "FROM" + " " + user_table + " " + "WHERE" + " " + where_string;
		}
		Cursor cursor = sqlitedatabase.rawQuery(select, null);
		int num = cursor.getCount();	
		String result = "";
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				for(i=0;i<num;i++){
					result = result + cursor.getString(i) +" ";
				}	
				result = result + "\n";
			}
		return result;
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

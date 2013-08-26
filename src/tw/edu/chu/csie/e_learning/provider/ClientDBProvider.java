package tw.edu.chu.csie.e_learning.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

@SuppressWarnings("unused")
public class ClientDBProvider {
	
	public String select ="";
	private static int i = 0;
	private SQLiteDatabase sqlitedatabase;
	
	//查詢資料庫內容，並將所有結果存到result裡
	public String All(String chtab){
		select = "SELECT * FROM"+" "+chtab;
		Cursor cursor = sqlitedatabase.rawQuery(select, null);
		String result = "";
		if(chtab == "user"){
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
					result = result + cursor.getString(0) +"："+ cursor.getString(1) +"："+ cursor.getString(2)+ "\n";
			}
		}
		else if(chtab == "target")
		{
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				result = result + cursor.getString(0) +"："+ cursor.getString(1) +"："+ cursor.getString(2)+"："+ cursor.getString(3)+"："+ cursor.getString(4)+ "\n";
			}
		}
		else
		{
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				result = result + cursor.getString(0) +"："+ cursor.getString(1) +"："+ cursor.getString(2)+"："+ cursor.getString(3)+"："+ cursor.getString(4)+"："+ cursor.getString(5)+"："+ cursor.getString(6)+"："+ cursor.getString(7)+ "\n";
			}	
		}
		return result;
	}

	public long user_insert(String v1,String v2,String v3,String v4){ //"使用者"新增
	
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("UID", v1);
		contentvalues.put("UNickname", v2);
		contentvalues.put("ULogged_code", v3);
		contentvalues.put("In_Learn_Time", v4);
		return sqlitedatabase.insert("user", null, contentvalues);
	}

	public long target_insert(String v1,String v2,String v3,String v4,String v5){ //"標的"新增
		
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("TID", v1);
		contentvalues.put("MapID", v2);
		contentvalues.put("Map_Url", v3);
		contentvalues.put("MaterialID", v4);
		contentvalues.put("Material_Url", v5);
		return sqlitedatabase.insert("target", null, contentvalues);
	}	

	public long study_insert(String v1,String v2,String v3,String v4,String v5,String v6,String v7,String v8){ //"學習關係"新增
		
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("TID", v1);
		contentvalues.put("UID", v2);
		contentvalues.put("QID", v3);
		contentvalues.put("Answer", v4);
		contentvalues.put("Answer_Time", v5);
		contentvalues.put("In_TargetTime", v6);
		contentvalues.put("Out_TargetTime", v7);
		contentvalues.put("TCheck", v8);
		return sqlitedatabase.insert("study", null, contentvalues);
	}		
	
	public long delete(String where_string,String user_table){ 
		
		if(user_table == "user")
			return sqlitedatabase.delete(user_table, where_string, null);
		else if(user_table == "target")
			return sqlitedatabase.delete(user_table, where_string, null);
		else
			return sqlitedatabase.delete(user_table, where_string, null);
	}


	public long update(String user_table,String newv1,String newv2,String where_string){
		
		ContentValues contentvalues = new ContentValues();
			if(user_table == "user")
			{
				contentvalues.put("ULogged_code", newv1);
				contentvalues.put("In_Learn_Time", newv2);
			}
			if(user_table == "target")
			{
				contentvalues.put("Map_Url", newv1);
				contentvalues.put("Material_Url", newv2);
			}
			return sqlitedatabase.update(user_table, contentvalues, where_string, null);
	}
	
	public long study_update(String user_table,String newv1,String newv2,String newv3,String newv4,String where_string){
		
		ContentValues contentvalues = new ContentValues();
		
				contentvalues.put("QID", newv1);
				contentvalues.put("Answer", newv2);
				contentvalues.put("Answer_Time", newv3);
				contentvalues.put("Out_TargetTime", newv4);
				
			return sqlitedatabase.update(user_table, contentvalues, where_string, null);
	}
	
	public String search(String user_table,String search_item,String where_string){ //查詢
		select = "SELECT" + " " + search_item + " " + "FROM" + " " + user_table + " " + "WHERE" + " " + where_string;
		Cursor cursor = sqlitedatabase.rawQuery(select, null);
		String result = "";
		int num = cursor.getCount();
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				for(i=0;i<num;i++){
					result = result + cursor.getString(i) +" ";
				}	
				result = result + "\n";
			}
		return result;
	}
	
	
}

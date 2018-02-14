package com.ukm.customerUKM;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB_Account {

	public static final String MYDATABASE_NAME = "local_db.db";
	public static final String MYDATABASE_TABLE = "ACCOUNT";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_USER = "user";
	public static final String KEY_PWD = "pwd";
	
	private static final String SCRIPT_CREATE_DATABASE =
		"create table " + MYDATABASE_TABLE + " ("
		+ KEY_ID + " integer primary key autoincrement, "+ KEY_USER + " text, "+ KEY_PWD + " text);";
	
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	
	public SQLiteDB_Account(Context c)
	{
		context = c;
	}
	
	public SQLiteDB_Account openToRead() throws android.database.SQLException 
	{
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;	
	}
	
	public SQLiteDB_Account openToWrite() throws android.database.SQLException 
	{
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;	
	}
	
	public void close()
	{
		sqLiteHelper.close();
	}
	
	public long insert(String user1,String pwd1)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("user",user1);
		contentValues.put("pwd",pwd1);
		
		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
	}

	public int deleteAll(){
		return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}
	public int delete(String  a)
	{
		return sqLiteDatabase.delete(MYDATABASE_TABLE, KEY_ID+"=?", new String[] {a});
	}
	
	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(SCRIPT_CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{

		}

	}
	
	 public String MyUser(){
		  String[] columns = new String[]{KEY_USER};
		  Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, 
		    null, null, null, null, null);
		  String result = "";
		  
		  int index_CONTENT = cursor.getColumnIndex(KEY_USER);
		  for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
		   result = result + cursor.getString(index_CONTENT) + " ";
		  }
		 
		  return result;
		 }
	 public String MyPwd(){
		  String[] columns = new String[]{KEY_PWD};
		  Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, 
		    null, null, null, null, null);
		  String result = "";
		  
		  int index_CONTENT = cursor.getColumnIndex(KEY_PWD);
		  for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
		   result = result + cursor.getString(index_CONTENT) + " ";
		  }
		 
		  return result;
		 }
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + MYDATABASE_TABLE ;
		Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		sqLiteDatabase.close();
		cursor.close();
		return rowCount;
	}
	
}

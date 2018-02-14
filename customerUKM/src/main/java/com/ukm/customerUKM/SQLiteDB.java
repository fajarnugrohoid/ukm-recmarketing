package com.ukm.customerUKM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB extends SQLiteOpenHelper {
	
private static final String LOGCAT = null;
	
	public SQLiteDB(Context applicationcontext) {
		super(applicationcontext, "local_db.db", null, 1);
		Log.d(LOGCAT,"Created");
		// TODO Auto-generated constructor stub
	}
	
	public void createTable() {
		try {
			SQLiteDatabase mydb = this.getWritableDatabase();
						
			//mydb.execSQL("DROP TABLE IF EXISTS pk13_location_dering");
			mydb.execSQL("CREATE TABLE  IF NOT EXISTS  customer (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					  "`username` TEXT," +
						"`password` TEXT," +
					  "`nama` TEXT," +
					  "`lokasi` TEXT," +
					  "`lat` TEXT," +
					  "`long` TEXT," +
					  "`status_kirim` TEXT)");
			
			 
			
			mydb.close(); 
		} catch (Exception e) {
			
		}
	}
	public void insertIntoTable() {
		try {
			SQLiteDatabase mydb = this.getWritableDatabase();	 
				
			
			 
			mydb.close();
		} catch (Exception e) {
			
			
		}
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}

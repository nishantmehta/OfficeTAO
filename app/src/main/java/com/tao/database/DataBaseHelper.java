package com.tao.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DataBaseHelper extends SQLiteOpenHelper {

	//tables for the database
	public static final String TABLE_FORMS="forms";
	public static final String TABLE_LOGS="logs";
	
	//fields in the tables
	public static final String COLUMN_FORM_ID = "form_id";
	public static final String COLUMN_TIME_STAMP = "time_stamp";
	public static final String COLUMN_FORM_TEMPLATE = "form_template";
	public static final String COLUMN_FORM_NAME ="form_name";
	public static final String COLUMN_LOG_ID="log_id";
	public static final String COLUMN_DATA="data";
	public static final String COLUMN_FLAG = "flag";
	
	//database variables
	private static final String DATABASE_NAME = "tao.db";
	private static final int DATABASE_VERSION = 1;
	Context ctx;
	
	
	// Database creation sql statement
	
	//format :: form_id || time_stamp || form_name || form_template || flag :: end
	private static final String DATABASE_CREATE_FORM = "create table "
			+ TABLE_FORMS + "(" + COLUMN_FORM_ID + " integer primary key , "
			+ COLUMN_TIME_STAMP + " integer, " + COLUMN_FORM_NAME
			+ " text not null, "+COLUMN_FORM_TEMPLATE+" text not null,"+COLUMN_FLAG+" integer );";
	
	//format :: log_id || time_stamp || form_id || data || flag :: end  
	private static final String DATABASE_CREATE_LOGS = "create table "
			+ TABLE_LOGS + "(" + COLUMN_LOG_ID + " integer primary key , "
			+ COLUMN_TIME_STAMP + " integer ," + COLUMN_FORM_ID + " integer ,"
			+ COLUMN_DATA + " text not null," + COLUMN_FLAG + " integer );";
 	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		ctx=context;
		
        
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		//SQL create statements
		database.execSQL(DATABASE_CREATE_FORM);
		database.execSQL(DATABASE_CREATE_LOGS);
		
		//to store application shared variable to remember user options
		SharedPreferences myPrefs = ctx.getSharedPreferences("UserInfoTAO",android.content.Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString("var1", null);
        prefsEditor.putString("var2", null);
        prefsEditor.putString("var3","0");
        prefsEditor.commit();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DataBaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
		onCreate(db);
	}

}
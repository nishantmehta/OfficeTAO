package com.tao.database;

import java.util.ArrayList;
import java.util.List;

import com.tao.unitClass.Form;
import com.tao.unitClass.Logs;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class LogsDataSource {

	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private String[] allColumnsLog = { DataBaseHelper.COLUMN_DATA,
			DataBaseHelper.COLUMN_LOG_ID, DataBaseHelper.COLUMN_FLAG,
			DataBaseHelper.COLUMN_TIME_STAMP, DataBaseHelper.COLUMN_FORM_ID };

	// should contain CURD operation

	// add data log
	public int addOrUpdateLog(Logs log) {
		try {
			
			String sqlString = "INSERT OR REPLACE INTO logs (log_id,time_stamp,form_id,data,flag) VALUES ("
					+ "coalesce((SELECT log_id FROM logs WHERE log_id ='"
					+ log.getLogID()
					+ "'),"
					+ log.getLogID()
					+ "),"
					+log.getTimeStamp() +","
					+log.getFormID()
					+ ",'"
					+ log.getData()
					+ "',"
					 + log.getFlag() + ")";
			database.execSQL(sqlString);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e("TAO", e.getMessage());
			return -1;
		}

		// could not create a log
		return 1;

	}

    // Retrieve any
    public Logs getLog(long LogID) {
        Logs log = null;
        try {
            Cursor cursor = database.query(DataBaseHelper.TABLE_LOGS,
                    allColumnsLog, DataBaseHelper.COLUMN_LOG_ID + " = "
                    + LogID, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                log = cursorToLog(cursor);
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            cursor.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("resulthisaab", e.getMessage());
        } finally {

            return log;
        }

    }
	// Retrieve all data log
	public List<Logs> getAllLogs() {

		List<Logs> logs = new ArrayList<Logs>();
		Cursor cursor = database.query(DataBaseHelper.TABLE_LOGS,
				allColumnsLog, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Logs log = cursorToLog(cursor);
			logs.add(log);
			cursor.moveToNext();
		}
		cursor.close();

		return logs;
	}

	

	// delete data log
	public boolean deleteLog(long logID) {
		database.delete(DataBaseHelper.TABLE_LOGS,
				DataBaseHelper.COLUMN_LOG_ID + " = " + logID, null);
		
		// could not find and delete form
		
		return false;
	}

	private Logs cursorToLog(Cursor cursor){
		Logs log = new Logs();
		log.setData(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_DATA)));
		log.setFormID(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.COLUMN_FORM_ID)));
		log.setLogID(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.COLUMN_LOG_ID)));
		log.setTimeStamp(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.COLUMN_TIME_STAMP)));
		log.setFlag(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_FLAG)));
		return log;
	}
	// setup required database connection
	public LogsDataSource(Context context) {
		dbHelper = new DataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	// close database connection
	public void close() {
		dbHelper.close();
	}

}

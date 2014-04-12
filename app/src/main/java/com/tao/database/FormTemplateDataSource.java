package com.tao.database;

import java.util.ArrayList;
import java.util.List;

import com.tao.unitClass.Form;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FormTemplateDataSource {
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private String[] allColumnsForm = { DataBaseHelper.COLUMN_FLAG,
			DataBaseHelper.COLUMN_FORM_ID, DataBaseHelper.COLUMN_FORM_NAME,
			DataBaseHelper.COLUMN_FORM_TEMPLATE,
			DataBaseHelper.COLUMN_TIME_STAMP };

	// add forms
	public int addOrUpdateForm(Form form) {
		try {
			String sqlString = "INSERT OR REPLACE INTO forms (form_id,time_stamp,form_name,form_template,flag) VALUES ("
					+ "coalesce((SELECT form_id FROM forms WHERE form_id ='"
					+ +form.getFormID()
					+ "'),"
					+ form.getFormID()
					+ "),"
					+ System.currentTimeMillis()/1000
					+ ",'"
					+ form.getFormName()
					+ "','"
					+ form.getFormTemplate() + "'," + form.getFlag() + ")";
			database.execSQL(sqlString);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e("TAO", e.getMessage());
			return -1;
		}

		// could not create a form
		return 1;
	}

	// Retrieve any
	public Form getForm(long FormID) {
		Form form = null;
		try {
			Cursor cursor = database.query(DataBaseHelper.TABLE_FORMS,
					allColumnsForm, DataBaseHelper.COLUMN_FORM_ID + " = "
							+ FormID, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				form = cursorToForm(cursor);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("resulthisaab", e.getMessage());
		} finally {

			return form;
		}

	}

	// Retrieve all
	public List<Form> getAllForms() {
		List<Form> forms = new ArrayList<Form>();
		Cursor cursor = database.query(DataBaseHelper.TABLE_FORMS,
				allColumnsForm, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Form form = cursorToForm(cursor);
			forms.add(form);
			cursor.moveToNext();
		}
		cursor.close();

		return forms;
	}

	private Form cursorToForm(Cursor cursor) {
		Form form = new Form();
		form.setFormID(cursor.getLong(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_FORM_ID)));
		form.setFormName(cursor.getString(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_FORM_NAME)));
		form.setFormTemplate(cursor.getString(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_FORM_TEMPLATE)));
		form.setTimeStamp(cursor.getLong(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_TIME_STAMP)));
		form.setFlag(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_FLAG)));
		return form;
	}

	// setup required database connection
	public FormTemplateDataSource(Context context) {
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

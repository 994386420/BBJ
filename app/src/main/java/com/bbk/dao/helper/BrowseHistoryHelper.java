package com.bbk.dao.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BrowseHistoryHelper extends SQLiteOpenHelper {

	public BrowseHistoryHelper(Context context) {
		super(context, "browseHistory.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table browseHistory (id integer primary key autoincrement, groupRowKey varchar(200), date varchar(10))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}

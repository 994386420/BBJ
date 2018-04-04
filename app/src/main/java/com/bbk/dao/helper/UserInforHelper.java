package com.bbk.dao.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInforHelper extends SQLiteOpenHelper {

	public UserInforHelper(Context context) {
		super(context, "browseHistory.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table userInfor (id integer primary key autoincrement, groupRowKey varchar(200), title varchar(500), price varchar(50), hdfsImgUrl varchar(1000), userLogin varchar(1000), date varchar(10), quote varchar(10))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}

package com.bbk.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper{
	private  final static String DB_NAME = "user.db";
	public MyHelper(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table user(_id integer primary key autoincrement,"
				+ "userID text,password text,userLogin text,userEmail text,"+
				"userPhone text,nickname text,gender text,brithday text,"
				+ "province text,city text,imgUrl text"
				+ "thirdLogin text,openID text,username text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			//删除旧表
			db.execSQL("drop table if exists  user");
			//创建新表
			onCreate(db);
		}
	}

}

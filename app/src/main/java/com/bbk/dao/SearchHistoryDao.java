package com.bbk.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bbk.dao.helper.SearchHistoryHelper;
/**
 * @author Administrator
 *
 */
public class SearchHistoryDao {
	private SearchHistoryHelper helper;
	public SearchHistoryDao(Context context){
		helper = new SearchHistoryHelper(context);
	}
	
	/**
	 * @param keyword
	 */
	public void addHistory(String keyword){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into searchHistory (keyword) values (?)",new Object[]{keyword});
		db.close();
	
	}
	
	/**
	 * @param keyword
	 * @return
	 */
	public boolean exsistHistory(String keyword) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from searchHistory where keyword=?", new String[]{keyword});
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * @return
	 */
	public List<String> findAllHistories(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select keyword from searchHistory order by id desc", null);
		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("keyword")));
		}
		cursor.close();
		db.close();
		return list;
	}
	
	/**
	 */
	public void deleteAllHistory() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from searchHistory where 1=1",new Object[]{});
		db.close();
	}
	public static void main(String[] args) {
		String subTitle = "男装 女装 潮流女装 精品男包";
		int wordIndex = 100;
		int blankIndex = subTitle.indexOf(" ",wordIndex);
		String subT = null;
		if(blankIndex > 0){
			subT = subTitle.substring(0,blankIndex);
			String ss[] = subT.split(" ");
			subT = ss[ss.length-1];
		}else{
			String ss[] = subTitle.split(" ");
			subT = ss[ss.length-1];
		}
		System.out.println(subT);
	}
}

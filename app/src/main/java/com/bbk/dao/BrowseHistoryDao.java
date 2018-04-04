package com.bbk.dao;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bbk.dao.helper.BrowseHistoryHelper;
/**
 * @author Administrator
 *
 */
public class BrowseHistoryDao {
	private BrowseHistoryHelper helper;
	public BrowseHistoryDao(Context context){
		helper = new BrowseHistoryHelper(context);
	}
	
	/**
	 * @param keyword
	 */
	public void add(String groupRowkey){
		SQLiteDatabase db = helper.getWritableDatabase();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		db.execSQL("insert into browseHistory (groupRowKey, date) values (?, ?)", new Object[]{groupRowkey, date});
		db.close();
	}
	
	/**
	 * @param keyword
	 * @return
	 */
	public boolean exsist(String groupRowkey) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from browseHistory where groupRowkey = ?", new String[]{groupRowkey});
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}
	
	/**
	 * @return
	 */
	public String findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		JSONArray jsonArr = new JSONArray();
		Cursor cursor = db.rawQuery("select * from browseHistory order by id desc", null);
		try {
			while(cursor.moveToNext()){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("groupRowKey", cursor.getString(cursor.getColumnIndex("groupRowKey")));
				jsonArr.put(jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cursor.close();
		db.close();
		return jsonArr.toString();
	}
	
	/**
	 */
	public void deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from browseHistory where 1=1", new Object[]{});
		db.close();
	}
	
	/**
	 */
	public void delete(int id) {
		if(id == 0) {
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from browseHistory where id = ?", new Object[]{id});
		db.close();
	}
	
	public int getCount() {
		int count = 0;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from browseHistory order by id desc", null);
		while(cursor.moveToNext()){
			count ++;
		}
		cursor.close();
		db.close();
		return count;
	}
	
	public int getEarliestID() {
		int id = 0;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from browseHistory order by id asc", null);
		while(cursor.moveToNext()){
			id = cursor.getInt(cursor.getColumnIndex("id"));
			break;
		}
		cursor.close();
		db.close();
		return id;
	}
	
//	public void add(String groupRowkey, String title, String price, String hdfsImgUrl, String userLogin, String quote) {
//		if(!exsistHistory(groupRowkey, userLogin)) {
//			addHistory(groupRowkey, title, price, hdfsImgUrl, userLogin, quote);
//		}
//	}
	
	public void addBrowseHistory(String groupRowkey) {
		if(getCount() == 10) {
			delete(getEarliestID());
		}
		if(!exsist(groupRowkey)) {
			add(groupRowkey);
		}
	}
	public static void main(String[] args) {
		String money = "1";
		char[] chars = money.toString().toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if(i==0){
				sb.append(i);
			}else{
				sb.append(" "+i);
			}
		}
		System.out.println(sb.toString());
	}
}

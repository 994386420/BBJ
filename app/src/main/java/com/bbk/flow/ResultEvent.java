package com.bbk.flow;

import org.json.JSONObject;

public interface ResultEvent {
	public void onResultData(int requestCode,String api,JSONObject dataJo,String content);
}

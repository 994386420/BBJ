package com.bbk.entity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class XGMessageEntity {
	private final static Map<String,Class<?>> activityMap;
	private String title;
	private String content;
	private String startType;
	private String url;
	static{
		activityMap = new HashMap<String, Class<?>>();

	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public XGMessageEntity(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	public String getStartType() {
		return startType;
	}
	public void setStartType(String startType) {
		this.startType = startType;
	}
	public Class<?> getActivityClass(){
		return this.activityMap.get(startType);
	}
	public static void main(String[] args) {
		XGMessageEntity message = new XGMessageEntity("zjc", "liu");
		System.out.println(new Gson().toJson(message));
	}
	@Override
	public String toString() {
		return "XGMessageEntity [title=" + title + ", content=" + content
				+ ", startType=" + startType + "]";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}

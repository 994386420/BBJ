package com.bbk.update;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ParseXmlService {

	public HashMap<String, String> parseXml(InputStream inputStream) {
		HashMap<String, String> hashMap = null;
		boolean flag = true;
		try {
			XmlPullParser pullParser = Xml.newPullParser();
			pullParser.setInput(inputStream, "UTF-8");
			int event = pullParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					hashMap = new HashMap<String, String>();
					break;
				case XmlPullParser.START_TAG:
					flag = true;
					String name = pullParser.getName();
					if ("updateMessage".equalsIgnoreCase(name) && flag == true) {
						hashMap.put("updateMessage", pullParser.nextText().trim());
					} else if ("versionCode".equalsIgnoreCase(name)
							&& flag == true) {
						hashMap.put("versionCode", pullParser.nextText().trim());
//					} else if ("name".equalsIgnoreCase(name) && flag == true) {
//						hashMap.put("name", pullParser.nextText().trim());
					} else if ("url".equalsIgnoreCase(name) && flag == true) {
						hashMap.put("url", pullParser.nextText().trim());
//					}else if ("log".equalsIgnoreCase(name) && flag == true) {
//						hashMap.put("log", pullParser.nextText().trim());
					}
					break;
				case XmlPullParser.END_TAG:
					flag = false;
					break;
				}
				event = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("hashMap", hashMap + "===============");
		// hashMap = new HashMap<String, String>();
		// hashMap.put("versionCode", "2");
		// hashMap.put("fileName", "updateversion");
		// hashMap.put("loadUrl",
		// "http://192.168.1.30:8080/server/updateversion/updateversion.apk");
		return hashMap;
	}

}

package com.bbk.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class HttpUtil {
	
	private static int code;

	public static String getHttp (Map<String, String> map, String url,Context context) {
		return getWebService(buildHttpParam(map), url,50*1000);
	}
	public static String getHttp (Map<String, String> map, String url) {
		return getWebService(buildHttpParam(map), url,50*1000);
	}
	public static String getHttp1 (Map<String, String> map, String url,Context context,String referrer) {
		return getWebService1(buildHttpParam(map), url,50*1000,context, referrer);
	}
	public static String getHttp2 (Map<String, String> map, String url,Context context) {
		return getWebService2(buildHttpParam(map), url,50*1000,context);
	}
	public static String getHttp (Map<String, String> map, String url,Context context,int timeOut) {
		return getWebService(buildHttpParam(map), url,timeOut);
	}
	private static List<NameValuePair> buildHttpParam(Map<String, String> map) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		for(String key: map.keySet()) {
			NameValuePair nvp = new BasicNameValuePair(key, map.get(key));
			params.add(nvp);
		}
		return params;
	}
	
	private static String getWebService(List<NameValuePair> params, String url, int timeOut) {
		String result = "";
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setEntity(entity);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut); 
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
		
		try {
			HttpResponse httpResp = httpClient.execute(httpPost);
			code = httpResp.getStatusLine().getStatusCode();
			if (code == 200) {
				result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			} else {
				Log.i("HttpPost", "HttpPost false");
			}
		} catch (Exception e) {
				Log.i("HttpPost", "HttpPost false  "+e);
				Log.i("HttpPost", url);
				Log.i("HttpPost", code+"");
			e.printStackTrace();
		}
		return result;
	}
	private static String getWebService2(List<NameValuePair> params, String url,int timeOut, Context context) {
		String result = "";
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setEntity(entity);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut); 
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
		
		try {
			HttpResponse httpResp = httpClient.execute(httpPost);
			code = httpResp.getStatusLine().getStatusCode();
			if (code == 200) {
				result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			} else {
				Log.i("HttpPost", "HttpPost false");
			}
		} catch (Exception e) {
			try {
				JSONObject content = new JSONObject();
				content.put("content", "重试");
				content.put("status", 1);
				content.put("errmsg", "");
				result = content.toString();
				Log.i("HttpPost", "HttpPost false  "+e);
				Log.i("HttpPost", url);
				Log.i("HttpPost", code+"");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
	 public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setConnectTimeout(5000);
	            conn.setReadTimeout(5000);
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    

	private static String getWebService1(List<NameValuePair> params, String url,int timeOut, Context context,String referrer) {
		String result = "";
			HttpGet httpPost = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
			if (referrer!= null) {
				httpPost.setHeader("Referer", referrer);
			}
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut); 
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			
			try {
				HttpResponse httpResp = httpClient.execute(httpPost);
				code = httpResp.getStatusLine().getStatusCode();
				if (code == 200) {
					result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				} else {
					Log.i("HttpPost", "HttpPost false");
				}
			} catch (Exception e) {
				Log.i("HttpPost", "HttpPost false  "+e);
				Log.i("HttpPost", url);
				Log.i("HttpPost", code+"");
				e.printStackTrace();
			}
			return result;
		
		
	}
 
	public static String requestByHttpGet(String url, Map<String, String> map) throws Exception {
//		String path = "http://www.bibkan.com/user.php?status=send_vali&phone=" + phone + "&num=" + message + "&modelid=19958";
		String path = "";
		String params = "";
		if(null != map){
			for(String key: map.keySet()) {
				params = params + key + "=" + map.get(key) + "&";
			}
			path = url + "?" + params.substring(0, params.length() - 1);
		}else{
			path = url;
		}
		// 新建HttpGet对象
		HttpGet httpGet = new HttpGet(path);
		// 获取HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		// 获取HttpResponse实例
		HttpResponse httpResp = httpClient.execute(httpGet);
		// 判断是够请求成功
		if (httpResp.getStatusLine().getStatusCode() == 200) {
			// 获取返回的数据
			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Log.i("HttpGet", "HttpGet方式请求成功，返回数据如下：");
			Log.i("HttpGet", result);
			return result;
		} else {
			Log.i("HttpGet", "HttpGet方式请求失败  code:" + httpResp.getStatusLine().getStatusCode());
			return "";
		}
	}
	
	public static void loadData(final Context context,final Map<String, String> params, final String url, final Handler handler, final int what) {
		Thread getContentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String result = getHttp(params, url,context);
				Message msg = Message.obtain();
				msg.what = what;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
		getContentThread.start();
	}
	public static void loadData(final Context context,final Map<String, String> params, final String url, final Handler handler, final int what,final int timeOut) {
		Thread getContentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String result = getHttp(params, url,context,timeOut);
				Message msg = Message.obtain();
				msg.what = what;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
		getContentThread.start();
	}
	
	public static void OkHttpGet(String url, Callback callback) {
		
		OkHttpClient mOkHttpClient = new OkHttpClient();
		Request mRequest = new Request.Builder().url(url).build();
		Call mCall = mOkHttpClient.newCall(mRequest);
		mCall.enqueue(callback);
	}
	
}

package com.bbk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static boolean isNetworkConnected(Context context){ 
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 
        if(networkInfo==null){ 
            return false; 

        }else {
			return true;
		}
 

    }
		public static byte[] getbytes(String path){
			byte [] array = null;
			try {
				URL url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setReadTimeout(3000);
				
				if (200 == connection.getResponseCode()) {
					InputStream inputStream = connection.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					int len = 0;
					byte [] buffer = new byte[1024];
					while ((len = inputStream.read(buffer)) != -1) {
						baos.write(buffer, 0, len);
					}
					array = baos.toByteArray();
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return array;
		}
}

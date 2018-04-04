package com.bbk.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.bbk.receiver.MyBroadcastReceiver;

public class DownloadService{

	private long downloadId = -1;
	private DownloadManager manager;

	public void download(AppVersion appVersion, Context mContext) {
		
		String serviceString = Context.DOWNLOAD_SERVICE;  
		manager = (DownloadManager) mContext.getSystemService(serviceString);
		
		Uri uri = Uri.parse(appVersion.getApkUrl());
		DownloadManager.Request dmReq = new DownloadManager.Request(uri);
		dmReq.setTitle("应用更新");
		dmReq.setDescription(appVersion.getUpdateMessage());
		dmReq.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

		dmReq.setDestinationInExternalPublicDir("bbj", "bibijing.apk");
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		mContext.registerReceiver(myReceiver, filter);

		downloadId = manager.enqueue(dmReq);
	}

	public  BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			long doneDownloadId = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
			if (doneDownloadId != downloadId) {
				return;
			}
			try {
				Cursor c = manager.query(new DownloadManager.Query().setFilterById(doneDownloadId));
				Uri installUri = null;
				if(c != null){
				    c.moveToFirst();
				    installUri = Uri.parse(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
				    c.close();
				}

//				Uri installUri = manager.getUriForDownloadedFile(doneDownloadId);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(installUri, "application/vnd.android.package-archive");
				context.startActivity(installIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}

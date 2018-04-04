package com.bbk.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by rtj on 2018/2/1.
 */

public class MyBroadcastReceiver extends BroadcastReceiver{
    private long downloadId;
    private DownloadManager manager;
    public MyBroadcastReceiver(long downloadId,DownloadManager manager){
        this.downloadId = downloadId;
        this.manager = manager;
    }
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
}

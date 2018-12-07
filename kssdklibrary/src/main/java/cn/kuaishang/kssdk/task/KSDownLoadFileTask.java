package cn.kuaishang.kssdk.task;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

import cn.kuaishang.core.KSManager;
import cn.kuaishang.util.FileUtil;

/**
 * 下载文件任务
 * 
 * @author hjl
 */
public class KSDownLoadFileTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private String url;
	private String localPath;
	
//    private int fileSize = 0;  
//    private int downloadSize = 0; 

	public KSDownLoadFileTask(Context context, String url, String localPath) {
		this.context = context;
		this.url = url;
		this.localPath = localPath;
	}

	@Override
	protected String doInBackground(String... params) {
		String name = url.substring(url.lastIndexOf("/") + 1);
		File localFile = new File(localPath + name);
		try {
			if (localFile.exists())
				return "";
			KSManager.getInstance(context).downloadFile(url, localPath, name);
			//通知媒体库(如果没有通知，相册中不能及时显示刚照的照片)
//			Uri uri = Uri.parse("file://"+localPath);   
//			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
		} catch (Throwable e) {
			// 写入日志
			FileUtil.deleteFile(localFile);
		}
		return "";
	}
}

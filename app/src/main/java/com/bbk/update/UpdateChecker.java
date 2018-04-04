package com.bbk.update;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.bbk.activity.HomeActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;

public class UpdateChecker implements ResultEvent {
	private static final String TAG = "UpdateChecker";
	private Context context;
	private boolean isShowToast;
	DataFlow dataFlow = null;

	public UpdateChecker(Context context) {
		this.context = context;
		dataFlow = new DataFlow(context);
	}
	public void checkBGForUpdates() {
		this.isShowToast = false;
		dataFlow.requestData(1, "apiService/getAndroidUpdate", null, this ,false);
	}

	public void checkForUpdates() {
		this.isShowToast = true;
		dataFlow.requestData(1, "apiService/getAndroidUpdate", null, this,"检测更新中...");
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		AppVersion appVersion = parseJson(content);
		try {
			int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			if (appVersion.getApkCode() > versionCode) {
				showUpdateDialog(appVersion);
			} else {
				if(isShowToast){
					Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (PackageManager.NameNotFoundException ignored) {
		}
	}

	private AppVersion parseJson(String json) {
		AppVersion appVersion = new AppVersion();
		try {
			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.getString(AppVersion.APK_UPDATE_CONTENT);
			String apkUrl = obj.getString(AppVersion.APK_DOWNLOAD_URL);
			String forceupdate = obj.getString(AppVersion.APK_FORCEUPDATE);
			int apkCode = obj.getInt(AppVersion.APK_VERSION_CODE);
			appVersion.setApkCode(apkCode);
			appVersion.setApkUrl(apkUrl);
			appVersion.setUpdateMessage(updateMessage);
			appVersion.setForceupdate(forceupdate);
		} catch (JSONException e) {
			Log.e(TAG, "parse json error", e);
		}
		return appVersion;
	}

	public void showUpdateDialog(final AppVersion appVersion) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("有新版本");
		builder.setMessage(appVersion.getUpdateMessage().replace("。", "\n"));
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				downLoadApk(appVersion);
				Toast.makeText(context, "后台下载中", Toast.LENGTH_SHORT).show();
			}
		});
		if ("1".equals(appVersion.getForceupdate())){
			builder.setCancelable(false);
			builder.setNegativeButton("关闭APP", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					System.exit(0);
				}
			});
		}else {
			builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
		}
		builder.show();
	}

	public void downLoadApk(AppVersion appVersion) {
		DownloadService downloadService = new DownloadService();
		downloadService.download(appVersion, context);

	}

}
package com.bbk.update;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.BuildConfig;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.R;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.UpdataDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 检测安装更新文件的助手类
 *
 * @author Administrator
 */
public class UpdateVersionService implements ResultEvent {
    private static final int THREE = 3;// 用于区分正在下载
    private static final int FOUR = 4;// 用于区分正在下载
    private static final int DOWN = 1;// 用于区分正在下载
    private static final int DOWN_FINISH = 0;// 用于区分下载完成
    private HashMap<String, String> hashMap;// 存储更新版本的xml信息
    private String fileSavePath;// 下载新apk的厨房地点
//    private String updateVersionXMLPath;// 检测更新的xml文件
    private int progress;// 获取新apk的下载数据量,更新下载滚动条
    private boolean cancelUpdate = false;// 是否取消下载
    private Context context;
    private ProgressBar progressBar;
    private Dialog downLoadDialog;
    private PopupWindow mUpdatePopup;
    private TextView mNotUpdate, mUpdate;
    private TextView mUpdateContentText;
    private boolean isShowToast;
    DataFlow dataFlow = null;
    private File mFile;
    AppVersion appVersion;
    private UpdataDialog updataDialog;
    /**
     * 构造方法
     *
     * @param context              上下文
     */
    public UpdateVersionService( Context context) {
        super();
//        this.updateVersionXMLPath = updateVersionXMLPath;
        this.context = context;
        dataFlow = new DataFlow(context);
    }

    private Handler handler = new Handler() {// 更新ui
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ((Integer) msg.obj) {
                case DOWN:
                    progressBar.setProgress(progress);
                    break;
                case DOWN_FINISH:
                    Toast.makeText(context, "文件下载完成,正在安装更新", Toast.LENGTH_SHORT)
                            .show();
                    installAPK();
                    break;
                default:
                    break;
            }
        }
    };

    public void checkForUpdates() {
        this.isShowToast = true;
        dataFlow.requestData(1, "apiService/getAndroidUpdate", null, this,false);
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        appVersion = parseJson(content);

            switch (requestCode){
                case 1:
                    try {
                    int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                    if (appVersion.getApkCode() > versionCode) {
//                        showUpdateVersionDialog();
                        showUpdateDialog(appVersion);
                    }
//                    else {
//                        if(isShowToast){
//                            Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                  } catch (PackageManager.NameNotFoundException ignored) {
                   }
                    break;
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
            Log.e("====", "parse json error", e);
        }
        return appVersion;
    }
    /**
     * 更新提示框
     */
    private void showUpdateVersionDialog() {
        // 构造对话框
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.update_version_layout, null);
            mNotUpdate =  view
                    .findViewById(R.id.update_version_not_update);
            mUpdate = view.findViewById(R.id.update_version_true_update);
            mUpdateContentText = view
                    .findViewById(R.id.update_version_content_description);
            mUpdatePopup = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, false);
//            mUpdatePopup.setBackgroundDrawable(new BitmapDrawable());
            mUpdatePopup.setOutsideTouchable(true);
            mUpdatePopup.setFocusable(true);
            mUpdatePopup.showAtLocation(view, Gravity.CENTER, 0, 0);
            mUpdate.setOnClickListener(mUpdateClick);
            mNotUpdate.setOnClickListener(mNotUpdateClick);
                if (appVersion.getUpdateMessage() != null) {
                    mUpdateContentText.setText(appVersion.getUpdateMessage());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showUpdateDialog(final AppVersion appVersion) {
        if(updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context,R.layout.update_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            TextView tv_update = updataDialog.findViewById(R.id.tv_update);
            TextView tv_update_refuse = updataDialog.findViewById(R.id.tv_update_refuse);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            tv_update.setText(appVersion.getUpdateMessage().replace("。", "\n"));
            tv_update_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
//                    System.exit(0);
                }
            });
//            builder.setCancelable(false);
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    showDownloadDialog();
                }
            });
//        }
//            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.dialog);
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.update_layout, null);
//            TextView tv_update = view.findViewById(R.id.tv_update);
//            TextView tv_update_refuse = view.findViewById(R.id.tv_update_refuse);
//            TextView tv_update_gengxin = view.findViewById(R.id.tv_update_gengxin);
//            tv_update.setText(appVersion.getUpdateMessage().replace("。", "\n"));
//            tv_update_refuse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showDownloadDialog();
//                }
//            });
////            builder.setCancelable(false);
//            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.exit(0);
//                }
//            });
//            alertDialog = builder.create();
////            alertDialog.setCanceledOnTouchOutside(false);
//            Window window = alertDialog.getWindow();
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.gravity = Gravity.CENTER;
////            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
////            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//            window.setAttributes(params);
//            window.setWindowAnimations(R.style.dialog_style);
//            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//全局弹窗
//            alertDialog.show();
//        }
//        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
//        builder.setTitle("更新提示");
//        builder.setMessage(appVersion.getUpdateMessage().replace("。", "\n"));
//        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int whichButton) {
//                showDownloadDialog();
//            }
//        });
//        if ("1".equals(appVersion.getForceupdate())){
//            builder.setCancelable(false);
//            builder.setNegativeButton("关闭APP", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    System.exit(0);
//                }
//            });
//        }else {
//            builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int whichButton) {
//                }
//            });
        }
//        builder.show();
    }
    // 更新
    View.OnClickListener mUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (mUpdatePopup != null && mUpdatePopup.isShowing()) {
                    mUpdatePopup.dismiss();
                }
                showDownloadDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 不更新
    View.OnClickListener mNotUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (mUpdatePopup != null && mUpdatePopup.isShowing()) {
                    mUpdatePopup.dismiss();
                }
                if ("1".equals(appVersion.getForceupdate())) {
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 下载的提示框
     */
    protected void showDownloadDialog() {
        {
            // 构造软件下载对话框
            Builder builder = new Builder(context);
//            builder.setTitle("正在更新");
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.downloaddialog, null);
            progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
            TextView tv_update_cancle = v.findViewById(R.id.tv_update_cancle);
            tv_update_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downLoadDialog.dismiss();
                   // 设置取消状态
                    cancelUpdate = true;
                }
            });
            builder.setView(v);
//            // 取消更新
//            builder.setNegativeButton("取消", new OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    // 设置取消状态
//                    cancelUpdate = true;
//                }
//            });
            builder.setCancelable(false);
            downLoadDialog = builder.create();
            downLoadDialog.show();
            // 现在文件
            downloadApk();
        }
    }

    /**
     * 下载apk,不能占用主线程.所以另开的线程
     */
    private void downloadApk() {
        new downloadApkThread().start();
    }

    /**
     * 获取当前版本名称和服务器版本名称.如果服务器版本高于本地安装的版本.就更新
     *
     * @param context2
     * @return
     */
    private int getVersionName(Context context2) {
        int versionName = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            // PackageManager packageManager = getPackageManager();
            // PackageInfo packInfo;
            // packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = context.getPackageManager().getPackageInfo(
                    "com.bbk.activity", 0).versionCode;
            // Toast.makeText(context, "当前版本是: " + versionCode,
            // Toast.LENGTH_SHORT).show();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;

    }

    /**
     * 安装apk文件
     */
    private void installAPK() {
        if (mFile != null) {
            Uri uri2 = Uri.fromFile(new File(mFile, "bbj" + ".apk"));
            Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri2, "application/vnd.android.package-archive");
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(mFile, "bbj" + ".apk"));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(uri2, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 卸载应用程序(没有用到)
     */
    public void uninstallAPK() {
        Uri packageURI = Uri.parse("package:com.bbk.activity");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);

    }


    /**
     * 下载apk的方法
     *
     * @author rongsheng
     */
    public class downloadApkThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
//                    String sdpath = Environment.getExternalStorageDirectory()+ "/";
//                    StringBuilder sdpath = new StringBuilder();
//                    List<String> extPaths = getExtSDCardPath();
//                    for (String path : extPaths) {
//                        sdpath.append("外置SD卡路径：" + path + "\r\n");
//                    }

//                    fileSavePath = sdpath+"download";
                    URL url = new URL(appVersion.getApkUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charser",
                            "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

//                    File file = new File(fileSavePath);
                    mFile = getTempDir();
                    // 判断文件目录是否存在
                    if (!mFile.exists()) {
                        mFile.mkdir();
                    }
                    Log.i("下载文件名称","比比鲸"+"=================="+mFile);
//                    File apkFile = new File(fileSavePath,hashMap.get("name") + ".apk");
                    File apkFile = new File(mFile,"bbj" + ".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        Message message = new Message();
                        message.obj = DOWN;
                        handler.sendMessage(message);
                        if (numread <= 0) {
                            // 下载完成
                            // 取消下载对话框显示
                            downLoadDialog.dismiss();
                            Message message2 = new Message();
                            message2.obj = DOWN_FINISH;
                            handler.sendMessage(message2);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private File getTempDir(){
        File appDir = context.getExternalFilesDir(null);
        File tempDir = new File(appDir,"download");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir;
    }
}

package com.bbk.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bbk.activity.MyApplication;
import com.bbk.client.BaseApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金刚圈分享类
 */
public class ShareHaiBaoManager {

    private Context mContext;
    private List<File> files = new ArrayList<>();

    public ShareHaiBaoManager(Context mContext) {
        this.mContext = mContext;
    }

    public void setShareImage(final int flag, final List<String> stringList,final String Kdescription){

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < stringList.size(); i++) {
                            File file = Tools.saveImageToSdCard(mContext, stringList.get(i));
                            files.add(file);
                        }
                        Intent intent = new Intent();
                        ComponentName comp;
                        if (flag == 0) {
                            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                        } else {
                            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                            intent.putExtra("Kdescription", Kdescription);
                        }
                        intent.setComponent(comp);
                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        intent.setType("image/*");
                        ArrayList<Uri> imageUris = new ArrayList<Uri>();
                        if (files != null) {
                            for (File f : files) {
                                imageUris.add(Uri.fromFile(f));
                            }
                        }
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                        mContext.startActivity(intent);
                        DialogSingleUtil.dismiss(0);
                    }
                }).start();
            }catch (Exception e){
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(mContext,"连接超时");
            }
    }

    /**
     * 检查是否分享
     */
    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                String url1 = BaseApiService.Base_URL+"newService/checkIsShare";
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("userid", userid);
                paramsMap.put("type", "2");
                String s = HttpUtil.getHttp(paramsMap, url1);
            }
        }).start();
    }
}

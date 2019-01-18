package com.bbk.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.bbk.activity.MyApplication;
import com.bbk.client.BaseApiService;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.wxpay.Util;
import com.logg.Logg;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金刚圈分享类
 */
public class ShareManager {
    /** 微信分享接口 */
    private IWXAPI wxApi;
    private Context mContext;
    private List<File> files = new ArrayList<>();
    private static Handler handler = new Handler();

    public ShareManager(Context mContext) {
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
//                        Intent intent = new Intent();
//                        ComponentName comp;
//                        if (flag == 0) {
//                            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
//                        } else {
//                            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                            intent.putExtra("Kdescription", Kdescription);
//                        }
//                        intent.setComponent(comp);
//                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                        intent.setType("image/*");
//                        ArrayList<Uri> imageUris = new ArrayList<Uri>();
//                        if (files != null){
//                            for (File f : files) {
//                                if(f != null) {
//                                    imageUris.add(Uri.fromFile(f));
//                                }
//                            }
//                        }
//                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//                        mContext.startActivity(intent);
                        File file = Tools.saveImageToSdCard(mContext, stringList.get(0));
                        if (flag == 0) {
                            imageShare(file,false);
                        } else {
                            imageShare(file,true);
                        }
                        DialogSingleUtil.dismiss(0);
                        SharedPreferencesUtil.putSharedData(mContext, "isShare", "isShare", "1");
                        loadData();
                    }
                }).start();
            }catch (Exception e){
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(mContext,"连接超时");
            }
    }



    public static void sharedToQQ(Context context,Bitmap bitmap) {
        File file = bitMap2File(bitmap);
        if (file != null && file.exists() && file.isFile()) {
            //由文件得到uri
            Uri imageUri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //intent.setType("text/plain");  //文本分享
            intent.setType("image/*");
            if (imageUri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            }
            intent.setPackage("com.tencent.mobileqq");
            intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ
            DialogSingleUtil.dismiss(0);
//            SharedPreferencesUtil.putSharedData(context, "isShare", "isShare", "1");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NewConstants.isjinkouling = "0";
                }
            },1000);
            context.startActivity(intent);
        }
    }

    /**
     * 把bitmap转化为file
     * @param bitmap
     * @return
     */
    public static File bitMap2File(Bitmap bitmap) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory() + File.separator;//保存到sd根目录下
        }
        File f = new File(path, "share" + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return f;
        }
    }

    /**
     * 微信分享
     * @param file 保存到本地的图片
     * @param isShareToTimeline 分享到朋友圈（true）还是好友(false)
     */
    public void imageShare(File file,boolean isShareToTimeline){
        wxApi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID);
        try {
            if (!file.exists()) {

            }
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(file.getAbsolutePath());
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 120, 120, true);
            bmp.recycle();
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            //要分享给好友还是分享到朋友圈
            req.scene = isShareToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            wxApi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建一个唯一标志
     *
     * @param type 分享的类型分字符串
     * @return 返回唯一字符串
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 检查鲸港圈是否分享
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

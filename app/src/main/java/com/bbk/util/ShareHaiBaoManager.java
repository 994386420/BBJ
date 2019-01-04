package com.bbk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bbk.resource.Constants;
import com.bbk.wxpay.Util;
import com.logg.Logg;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 海报分享类
 */
public class ShareHaiBaoManager {
    /** 微信分享接口 */
    private IWXAPI wxApi;
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
//                        Intent intent = new Intent();
//                        ComponentName comp;
                        File file = Tools.saveImageToSdCard(mContext, stringList.get(0));
                        if (flag == 0) {
//                            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
//                            intent.setComponent(comp);
//                            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                            intent.setType("image/*");
//                            ArrayList<Uri> imageUris = new ArrayList<Uri>();
//                            if (files != null) {
//                                for (File f : files) {
//                                    if (f != null) {
//                                        imageUris.add(Uri.fromFile(f));
//                                    }
//                                }
//                            }
//                            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//                            mContext.startActivity(intent);
                            imageShare(file,false);
                        } else {
                            imageShare(file,true);
                        }
                        DialogSingleUtil.dismiss(0);
                    }
                }).start();
            }catch (Exception e){
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(mContext,"连接超时");
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
            Logg.json(req);
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
}

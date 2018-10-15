package com.bbk.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.bbk.activity.MyApplication;
import com.bbk.client.BaseApiService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金刚圈分享类
 */
public class ShareManager {

    private Context mContext;
    private List<File> files = new ArrayList<>();

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
                        if (files != null){
                            for (File f : files) {
                                if(f != null) {
                                    imageUris.add(Uri.fromFile(f));
                                }
                            }
                        }
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                        mContext.startActivity(intent);
                        DialogSingleUtil.dismiss(0);
                        loadData();
                    }
                }).start();
            }catch (Exception e){
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(mContext,"连接超时");
            }
    }

    /*
    * 将布局转化为bitmap
这里传入的是你要截的布局的根View
    * */
    public static Bitmap getBitmapByView(View headerView) {
        int h = headerView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(headerView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        headerView.draw(canvas);
        return bitmap;
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
            context.startActivity(intent);
        }
    }

    /**
     * 根据图片的url路径获得Bitmap对象 * @param url * @return
     */
    private static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    /**
     * 压缩图片
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 400) { //循环判断如果压缩后图片是否大于400kb,大于继续压缩（这里可以设置大些）
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
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
        //        File f = new File(path, System.currentTimeMillis() + ".jpg");
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

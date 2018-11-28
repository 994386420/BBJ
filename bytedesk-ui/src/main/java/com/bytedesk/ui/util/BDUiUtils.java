package com.bytedesk.ui.util;

import android.content.Context;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ningjinpeng on 2018/3/28.
 */

public class BDUiUtils {


    /**
     * 判断是否应该显示时间戳
     */
    public static Boolean shouldShowTime(String mytime, String before) {

        if(mytime == null || before == null)
            return true;

        Date mydate = toDate(mytime);
        Date beforedate = toDate(before);

        long timeInterval = mydate.getTime() - beforedate.getTime();

        if(timeInterval/1000 > 60)//显示超过一分钟的
        {
            return true;
        }

        return false;
    }


    public static void showTipDialog(Context context, String tip) {

        final QMUITipDialog tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(tip)
                .create();
        tipDialog.show();
        //
        //in your method, use the Timer Schedule function:
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                },
                2000
        );
    }

    /**
     * 以友好的方式显示时间
     */
    public static String friendlyTime(String sdate, Context ctx) {

        Date time = toDate(sdate);

        if(time == null) {
            return "Unknown";
        }

        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);

        if(curDate.equals(paramDate)){

            SimpleDateFormat formator = new SimpleDateFormat("HH:mm:ss");
            return formator.format(time);
        }

        SimpleDateFormat formator = new SimpleDateFormat("MM-dd HH:mm:ss");
        return formator.format(time);

    }


    private final static ThreadLocal<SimpleDateFormat> datetimeFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };


    /**
     * 将字符串转位日期类型
     */
    public static Date toDate(String sdate) {
        try {
            return datetimeFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 加载网络图片
     *
     * 使用方法：new DownloadImageTask(avatarImageView).execute(msgEntity.getAvatar());
     * 加载类：
     *
     private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
     ImageView bmImage;

     public DownloadImageTask(ImageView bmImage) {
     this.bmImage = bmImage;
     }

     protected Bitmap doInBackground(String... urls) {
     String urldisplay = urls[0];
     Bitmap mIcon11 = null;
     try {
     InputStream in = new java.net.URL(urldisplay).openStream();
     mIcon11 = BitmapFactory.decodeStream(in);
     } catch (Exception e) {
     Logger.e(e.getMessage());
     e.printStackTrace();
     }
     return mIcon11;
     }

     protected void onPostExecute(Bitmap result) {
     bmImage.setImageBitmap(result);
     }
     }
     */


}

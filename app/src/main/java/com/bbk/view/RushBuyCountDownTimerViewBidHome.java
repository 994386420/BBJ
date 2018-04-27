package com.bbk.view;

/**
 * Created by rtj on 2018/3/5.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.activity.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("HandlerLeak")
public class RushBuyCountDownTimerViewBidHome extends LinearLayout {

    // 小时，十位
    private TextView tv_hour_decade;
    // 小时，个位
    private TextView tv_hour_unit;
    // 分钟，十位
    private TextView tv_min_decade;
    // 分钟，个位
    private TextView tv_min_unit;
    // 秒，十位
    private TextView tv_sec_decade;
    // 秒，个位
    private TextView tv_sec_unit;
    //miao
    private TextView mtv_miao;
    private Context context;
    // 计时器
    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        };
    };


    public RushBuyCountDownTimerViewBidHome(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer1, this);

        tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
        mtv_miao = view.findViewById(R.id.tv_miao);
    }

    /**
     *
     * @Description: 开始计时
     * @param
     * @return void
     * @throws
     */
    public void start() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     *
     * @Description: 停止计时
     * @param
     * @return void
     * @throws
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void timeColor(String color) {
        tv_hour_decade.setTextColor(Color.parseColor(color));
        tv_hour_unit.setTextColor(Color.parseColor(color));
        tv_min_decade.setTextColor(Color.parseColor(color));
        tv_min_unit.setTextColor(Color.parseColor(color));
        tv_sec_decade.setTextColor(Color.parseColor(color));
        tv_sec_unit.setTextColor(Color.parseColor(color));
    }

    /**
     *
     * @Description: 倒计时
     * @param
     * @return boolean
     * @throws
     */
    public Boolean countDown() {

        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {

                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {

                        if (isDay4Unit(tv_hour_unit)) {
                            if (isDay4Decade(tv_hour_decade)) {
//                                        Toast.makeText(context, "时间到了",
//                                                Toast.LENGTH_SHORT).show();
                                        tv_hour_decade.setText("0");
                                        tv_hour_unit.setText("0");
                                        tv_min_decade.setText("0");
                                        tv_min_unit.setText("0");
                                        tv_sec_decade.setText("0");
                                        tv_sec_unit.setText("0");
                                        stop();
                                        return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化十位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化个位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化十位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isDay4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 3;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化个位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isDay4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 2;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }
    public void getTimeFromInt(long time) {
        DecimalFormat df = new DecimalFormat("00");
//        long day = time / (1 * 60 * 60 * 24);
        long hour = time / (1 * 60 * 60);// % 24
        long minute = time / (1 * 60) % 60;
        long second = time / (1) % 60;
        tv_hour_unit.setText(hour + "");
        tv_min_decade.setText(minute + "");
        tv_sec_decade.setVisibility(GONE);
        tv_sec_unit.setVisibility(GONE);
        mtv_miao.setVisibility(GONE);
        tv_hour_decade.setVisibility(GONE);
        tv_min_unit.setVisibility(GONE);
    }
    /**
     * 以友好的方式显示时间
     * @param sdate
     * @return
     */
    public void friendly_time(String sdate,String color) {
        timeColor(color);
        Date time = toDate(sdate);
        Calendar cal = Calendar.getInstance();
        long lt = time.getTime()/86400000;
        long ct = cal.getTimeInMillis()/86400000;
        int days = (int)(ct - lt);
        int hour1;
        hour1 = (int)((time.getTime()-cal.getTimeInMillis())/1000);
        getTimeFromInt(hour1);
    }

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
}

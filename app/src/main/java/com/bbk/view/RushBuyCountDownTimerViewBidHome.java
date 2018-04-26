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
    private int day_decade;
    private int day_unit;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;
    // 计时器
    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        };
    };
    private int hour = 0;
    private int min = 0;
    private int sec = 0;

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
    //只显示小时和分钟
    public  void addsumHour(String time,String color) throws ParseException {
        timeColor(color);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        Date date2;
        date2 = sDateFormat.parse(date);
        Calendar calendar2=Calendar.getInstance();
        calendar2.setTime(date2);
        Date date1 = sDateFormat.parse(time);
        calendar.setTime(date1);
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        int minute2 = calendar2.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);
        int second2 = calendar2.get(Calendar.SECOND);
        int d1 = day*24*60*60+hour*60*60+minute*60+second + year*365*24*60*60;
        int d2 = day2*24*60*60+hour2*60*60+minute2*60+second2 + year*365*24*60*60;
        int sum = d1 - d2;
        addTimeHour(sum);
    }
    // 如果:sum = 12345678
    public void addTimeHour(int sum) {
        // 求出天数
        int day = sum / 60 / 60 / 24;
        // 先获取个秒数值
        int sec = sum % 60;
        // 如果大于60秒，获取分钟。（秒数）
        int sec_time = sum / 60;
        // 再获取分钟
        int min = sec_time % 60;
        // 如果大于60分钟，获取小时（分钟数）。
        int min_time = sec_time / 60;
        // 获取小时
        int hour = min_time % 24;
        hour = 24*day+hour;
        setTimehour(hour, min);
    }
    /**
     * @throws Exception
     *
     * @Description: 设置倒计时的时长
     * @param
     * @return void
     * @throws
     */
    public void setTimehour(int hour, int min) {
        //这里的天数不写也行，我写365
        if ( min >= 60 || hour < 0 || min < 0 ) {
            tv_hour_decade.setText("0");
            tv_hour_unit.setText("0");
            tv_min_decade.setText("0");
            tv_min_unit.setText("0");
        }

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;
        min_decade = min / 10;
        min_unit = min - min_decade * 10;
        // 第个time 进行初始化
        timeCleanHour();
    }
    private void timeCleanHour() {
        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setVisibility(GONE);
        tv_sec_unit.setVisibility(GONE);
        mtv_miao.setVisibility(GONE);
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

}

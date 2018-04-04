package com.bbk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ... on 2017/9/20.
 */

/**
 * 多行文字展开和收起
 */
public class StretchyTextView extends LinearLayout implements View.OnClickListener {
    private Context context;
    /**
     * 默认的最大行数
     */
    private static final int MAX_LINES = 3;
    //无状态
    private static final int SPREADTEXT_STATE_NONE = 0;
    //缩回状态
    private static final int SPREADTEXT_STATE_RETRACT = 1;
    //展开状态
    private static final int SPREADTEXT_STATE_SPREAD = 2;

    private int maxLineCount = MAX_LINES;

    private int mState;
    //实际的行数
    private int actually_counts;
    /**
     * 显示内容的文本
     */
    private TextView tv_stretchy_content;
    /**
     * 显示操作的文本
     */
    private TextView tv_stretchy_bottom;
    /**
     * 显示按钮的图片
     */
//    private ImageView iv_stretchy_bottom;

    private LinearLayout ll_stretchy_content;
    private InnerRunnable runable;
    private boolean flag = false;

    public StretchyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //填充布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.stretchy_text_layout, this);
        view.setPadding(0, 0, 0, 0);
        tv_stretchy_content = (TextView) view.findViewById(R.id.tv_stretchy_content);
//        tv_stretchy_content.setAutoLinkMask(Linkify.WEB_URLS);
//        tv_stretchy_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_stretchy_bottom = (TextView) view.findViewById(R.id.tv_stretchy_bottom);
//        iv_stretchy_bottom = (ImageView) view.findViewById(R.id.iv_stretchy_bottom);
        ll_stretchy_content = (LinearLayout) view.findViewById(R.id.ll_stretchy_content);
        ll_stretchy_content.setOnClickListener(this);
        setBottomTextGravity(Gravity.LEFT);
        runable = new InnerRunnable();
    }

    /**
     * 设置展开标识的显示位置
     *
     * @param gravity
     */
    public void setBottomTextGravity(int gravity) {
        ll_stretchy_content.setGravity(gravity);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!flag) {
            flag = !flag;
            Log.i("test", "当前lines:" + tv_stretchy_content.getLineCount());
            Log.i("test", "实际行数:" + actually_counts);
            if (actually_counts <= MAX_LINES && tv_stretchy_content.getLineCount() <= MAX_LINES) {
                mState = SPREADTEXT_STATE_NONE;
                ll_stretchy_content.setVisibility(View.GONE);
                tv_stretchy_content.setMaxLines(MAX_LINES + 1);
            } else {
                post(runable);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        flag = false;
        requestLayout();
    }

    /**
     * 设置最大行数
     *
     * @param maxLineCount
     */
    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    /**
     * 设置文本内容颜色
     *
     * @param color
     */
    public void setContentTextColor(int color) {
        this.tv_stretchy_content.setTextColor(color);
    }

    /**
     * 设置文本内容字体大小
     *
     * @param size
     */
    public void setContentTextSize(float size) {
        this.tv_stretchy_content.setTextSize(size);
    }

    /**
     * 设置文本内容
     *
     * @param text
     */
    public final void setContent(CharSequence text,final String url) {
        SpannableString msp = new SpannableString(text+"  购买链接");
        msp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);
            }
        },text.length()+2, text.length()+6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       tv_stretchy_content.setText(msp, TextView.BufferType.SPANNABLE);
        tv_stretchy_content.setMovementMethod(LinkMovementMethod.getInstance());//激活链接

        post(new Runnable() {
            @Override
            public void run() {
                actually_counts = tv_stretchy_content.getLineCount();
                Log.i("test", "获取实际行数:" + actually_counts);
            }
        });
        mState = SPREADTEXT_STATE_SPREAD;
        flag = false;
        requestLayout();
    }

    class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mState == SPREADTEXT_STATE_SPREAD) {
                tv_stretchy_content.setMaxLines(maxLineCount);
                ll_stretchy_content.setVisibility(View.VISIBLE);
                tv_stretchy_bottom.setText("显示全文");
//                iv_stretchy_bottom.setBackgroundResource(R.mipmap.buy_trailer_arrow_unselected);
                mState = SPREADTEXT_STATE_RETRACT;
            } else if (mState == SPREADTEXT_STATE_RETRACT) {
                Log.i("test", "需要收起");
                tv_stretchy_content.setMaxLines(Integer.MAX_VALUE);
                ll_stretchy_content.setVisibility(View.VISIBLE);
                tv_stretchy_bottom.setText("收起");
//                iv_stretchy_bottom.setBackgroundResource(R.mipmap.buy_trailer_arrow_selected);
                mState = SPREADTEXT_STATE_SPREAD;
            }
        }
    }

    public String getUrl(@NonNull String text) {
        Pattern pattern = Pattern
                .compile("(http://|ftp://|https://){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
        // 空格结束
        Matcher matcher = pattern
                .matcher(text);
        while (matcher.find()) {
            return matcher.group(0);
        }

        return "";
    }

    class MyClickableSpan extends ClickableSpan {
        private Context context;
        private String text;

        public MyClickableSpan(Context context, String text) {
            this.context = context;
            this.text = text;
        }

        //在这里设置字体的大小，等待各种属性
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(Color.parseColor("#5bc3f3"));
        }

        @Override
        public void onClick(View widget) {
//            context.startActivity(new Intent(context,W));
        }

    }
}

package com.bbk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bbk.activity.R;

/**
 * Created by liuwan on 2016/9/28.
 */
public class DatePickerView extends View {

    private Context context;
    /**
     * 鏂板瀛楁 鎺у埗鏄惁棣栧熬鐩告帴寰幆鏄剧ず 榛樿涓哄惊鐜樉绀�
     */
    private boolean loop = true;
    /**
     * text涔嬮棿闂磋窛鍜宮inTextSize涔嬫瘮
     */
    public static final float MARGIN_ALPHA = 2.8f;
    /**
     * 鑷姩鍥炴粴鍒颁腑闂寸殑閫熷害
     */
    public static final float SPEED = 10;
    private List<String> mDataList;
    /**
     * 閫変腑鐨勪綅缃紝杩欎釜浣嶇疆鏄痬DataList鐨勪腑蹇冧綅缃紝涓�鐩翠笉鍙�
     */
    private int mCurrentSelected;
    private Paint mPaint, nPaint;
    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;
    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    /**
     * 婊戝姩鐨勮窛绂�
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private boolean canScroll = true;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {
                // 杩欓噷mMoveLen / Math.abs(mMoveLen)鏄负浜嗕繚鏈塵MoveLen鐨勬璐熷彿锛屼互瀹炵幇涓婃粴鎴栦笅婊�
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }
            invalidate();
        }
    };

    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        }
    }

    public void setData(List<String> datas) {
        mDataList = datas;
        mCurrentSelected = datas.size() / 4;
        invalidate();
    }

    /**
     * 閫夋嫨閫変腑鐨刬tem鐨刬ndex
     */
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        if (loop) {
            int distance = mDataList.size() / 2 - mCurrentSelected;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelected--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelected++;
                }
            }
        }
        invalidate();
    }

    /**
     * 閫夋嫨閫変腑鐨勫唴瀹�
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
        }
    }

    private void moveHeadToTail() {
        if (loop) {
            String head = mDataList.get(0);
            mDataList.remove(0);
            mDataList.add(head);
        }
    }

    private void moveTailToHead() {
        if (loop) {
            String tail = mDataList.get(mDataList.size() - 1);
            mDataList.remove(mDataList.size() - 1);
            mDataList.add(0, tail);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 鎸夌収View鐨勯珮搴﹁绠楀瓧浣撳ぇ灏�
        mMaxTextSize = mViewHeight / 7f;
        mMinTextSize = mMaxTextSize / 2.2f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<>();
        //绗竴涓猵aint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(ContextCompat.getColor(context, R.color.text2));
        //绗簩涓猵aint
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Style.FILL);
        nPaint.setTextAlign(Align.CENTER);
        nPaint.setColor(ContextCompat.getColor(context, R.color.text2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 鏍规嵁index缁樺埗view
        if (isInit) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        // 鍏堢粯鍒堕�変腑鐨則ext鍐嶅線涓婂線涓嬬粯鍒跺叾浣欑殑text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text灞呬腑缁樺埗锛屾敞鎰廱aseline鐨勮绠楁墠鑳借揪鍒板眳涓紝y鍊兼槸text涓績鍧愭爣
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 缁樺埗涓婃柟data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 缁樺埗涓嬫柟data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param position 璺濈mCurrentSelected鐨勫樊鍊�
     * @param type     1琛ㄧず鍚戜笅缁樺埗锛�-1琛ㄧず鍚戜笂缁樺埗
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, nPaint);
    }

    /**
     * 鎶涚墿绾�
     *
     * @param zero 闆剁偣鍧愭爣
     * @param x    鍋忕Щ閲�
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveLen += (event.getY() - mLastDownY);
                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    if (!loop && mCurrentSelected == 0) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        mCurrentSelected--;
                    }
                    // 寰�涓嬫粦瓒呰繃绂诲紑璺濈
                    moveTailToHead();
                    mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    if (mCurrentSelected == mDataList.size() - 1) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        mCurrentSelected++;
                    }
                    // 寰�涓婃粦瓒呰繃绂诲紑璺濈
                    moveHeadToTail();
                    mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                }
                mLastDownY = event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                doUp();
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doUp() {
        // 鎶捣鎵嬪悗mCurrentSelected鐨勪綅缃敱褰撳墠浣嶇疆move鍒颁腑闂撮�変腑浣嶇疆
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface onSelectListener {
        void onSelect(String text);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return canScroll && super.dispatchTouchEvent(event);
    }

    /**
     * 鎺у埗鍐呭鏄惁棣栧熬鐩歌繛
     */
    public void setIsLoop(boolean isLoop) {
        loop = isLoop;
    }

}
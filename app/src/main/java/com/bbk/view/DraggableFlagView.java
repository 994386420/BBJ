package com.bbk.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bbk.activity.R;
import com.bbk.util.ABAppUtil;
import com.bbk.util.ABTextUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Github: https://github.com/wangjiegulu/DraggableFlagiew
 * Date: 12/23/14.
 */
public class DraggableFlagView extends FrameLayout {
    private static final String TAG = DraggableFlagView.class.getSimpleName();
    private int viewWidth, viewHeight;  //控件的宽高

    public static interface OnDraggableFlagViewListener {
        /**
         * 拖拽销毁圆点后的回调
         *
         * @param view
         * @param x
         * @param y
         */
        void onFlagDismiss(DraggableFlagView view, float x, float y);
    }

    private OnDraggableFlagViewListener onDraggableFlagViewListener;

    public void setOnDraggableFlagViewListener(OnDraggableFlagViewListener onDraggableFlagViewListener) {
        this.onDraggableFlagViewListener = onDraggableFlagViewListener;
    }

    public DraggableFlagView(Context context) {
        super(context);
        init(context);
    }

    private int patientColor = Color.RED;

    public DraggableFlagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DraggableFlagView);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attrIndex = a.getIndex(i);
            if (attrIndex == R.styleable.DraggableFlagView_color) {
                patientColor = a.getColor(attrIndex, Color.RED);
            }
        }
        a.recycle();
        init(context);
    }

    public DraggableFlagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getWidth();
        viewHeight = getHeight();
    }

    private Context context;
    private int originRadius; // 初始的圆的半径
    private int originWidth;
    private int originHeight;

    private int maxMoveLength; // 最大的移动拉长距离
    private boolean isArrivedMaxMoved; // 达到了最大的拉长距离（松手可以触发事件）

    private int curRadius; // 当前点的半径
    private int touchedPointRadius; // touch的圆的半径
    private Point startPoint = new Point();
    private Point endPoint = new Point();

    private Paint paint; // 绘制圆形图形
    private TextPaint textPaint; // 绘制圆形图形
    private Paint.FontMetrics textFontMetrics;

    private int[] location;

    private boolean isTouched; // 是否是触摸状态

    private Triangle triangle = new Triangle();

    private String text = ""; // 正常状态下显示的文字

    private void init(Context context) {
        this.context = context;

        setBackgroundColor(Color.TRANSPARENT);

        // 设置绘制flag的paint
        paint = new Paint();
        paint.setColor(patientColor);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        // 设置绘制文字的paint
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(ABTextUtil.sp2px(context, 12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textFontMetrics = textPaint.getFontMetrics();
    }

    RelativeLayout.LayoutParams originLp; // 实际的layoutparams
    RelativeLayout.LayoutParams newLp; // 触摸时候的LayoutParams

    private boolean isFirst = true;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (isFirst && w > 0 && h > 0) {
            isFirst = false;

            originWidth = w;
            originHeight = h;

            originRadius = Math.min(originWidth, originHeight) / 2;
            curRadius = originRadius;
            touchedPointRadius = originRadius;

            maxMoveLength = ABAppUtil.getDeviceHeight(context) / 6;

            refreshStartPoint();

            ViewGroup.LayoutParams lp = this.getLayoutParams();
            if (RelativeLayout.LayoutParams.class.isAssignableFrom(lp.getClass())) {
                originLp = (RelativeLayout.LayoutParams) lp;
            }
            newLp = new RelativeLayout.LayoutParams(lp.width, lp.height);
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        refreshStartPoint();
    }

    /* 修改layoutParams后，需要重新设置startPoint
     */
    private void refreshStartPoint() {
        location = new int[2];
        this.getLocationInWindow(location);
        try {
            location[1] = location[1] - ABAppUtil.getTopBarHeight((Activity) context);
        } catch (Exception ex) {
        }

        startPoint.set(location[0], location[1] + getMeasuredHeight());
    }

    Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        int startCircleX = 0, startCircleY = 0;
        if (isTouched) { // 触摸状态

            startCircleX = startPoint.x + curRadius;
            startCircleY = startPoint.y - originRadius;
            // 绘制原来的圆形（触摸移动的时候半径会不断变化）
            canvas.drawCircle(startCircleX, startCircleY, curRadius, paint);
            // 绘制手指跟踪的圆形
            int endCircleX = endPoint.x;
            int endCircleY = endPoint.y;
            canvas.drawCircle(endCircleX, endCircleY, originRadius, paint);

            if (!isArrivedMaxMoved) { // 没有达到拉伸最大值
                path.reset();
                double sin = triangle.deltaY / triangle.hypotenuse;
                double cos = triangle.deltaX / triangle.hypotenuse;

                // A点
                path.moveTo(
                        (float) (startCircleX - curRadius * sin),
                        (float) (startCircleY - curRadius * cos)
                );
                // B点
                path.lineTo(
                        (float) (startCircleX + curRadius * sin),
                        (float) (startCircleY + curRadius * cos)
                );
                // C点
                path.quadTo(
                        (startCircleX + endCircleX) / 2, (startCircleY + endCircleY) / 2,
                        (float) (endCircleX + originRadius * sin), (float) (endCircleY + originRadius * cos)
                );
                // D点
                path.lineTo(
                        (float) (endCircleX - originRadius * sin),
                        (float) (endCircleY - originRadius * cos)
                );
                // A点
                path.quadTo(
                        (startCircleX + endCircleX) / 2, (startCircleY + endCircleY) / 2,
                        (float) (startCircleX - curRadius * sin), (float) (startCircleY - curRadius * cos)
                );
                canvas.drawPath(path, paint);
            }

            // 绘制文字
            float textH = -textFontMetrics.ascent - textFontMetrics.descent;
            canvas.drawText(text, endCircleX, endCircleY + textH / 2, textPaint);

        } else { // 非触摸状态
            if (curRadius > 0) {
                startCircleX = curRadius;
                startCircleY = originHeight - curRadius;
                canvas.drawCircle(startCircleX, startCircleY, curRadius, paint);
                if (curRadius == originRadius) { // 只有在恢复正常的情况下才显示文字
                    // 绘制文字
                    float textH = -textFontMetrics.ascent - textFontMetrics.descent;
                    canvas.drawText(text, startCircleX, startCircleY + textH / 2, textPaint);
                }
            }

        }
    }

    float downX = Float.MAX_VALUE;
    float downY = Float.MAX_VALUE;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                invalidate();
                this.setLayoutParams(newLp);
                endPoint.x = (int) downX;
                endPoint.y = (int) downY;

                changeViewHeight(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                invalidate();

                downX = event.getX() + location[0];
                downY = event.getY() + location[1];
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算直角边和斜边（用于计算绘制两圆之间的填充区）
                triangle.deltaX = event.getX() - downX;
                triangle.deltaY = -1 * (event.getY() - downY); // y轴方向相反，所有需要取反
                double distance = Math.sqrt(triangle.deltaX * triangle.deltaX + triangle.deltaY * triangle.deltaY);
                triangle.hypotenuse = distance;
                refreshCurRadiusByMoveDistance((int) distance);

                endPoint.x = (int) event.getX();
                endPoint.y = (int) event.getY();

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                invalidate();
                this.setLayoutParams(originLp);

                if (isArrivedMaxMoved) { // 触发事件
                    changeViewHeight(this, originWidth, originHeight);
                    invalidate();

                    if (null != onDraggableFlagViewListener) {
                        onDraggableFlagViewListener.onFlagDismiss(this,
                                event.getX() - viewWidth, event.getY() - viewHeight);
                    }
                    resetAfterDismiss();
                } else { // 还原
                    changeViewHeight(this, originWidth, originHeight);
                    startRollBackAnimation(500/*ms*/);
                }

                downX = Float.MAX_VALUE;
                downY = Float.MAX_VALUE;
                path.reset();
                break;
        }
        return true;
    }

    /**
     * 触发事件之后重置
     */
    private void resetAfterDismiss() {
        this.setVisibility(GONE);
        text = "";
        isArrivedMaxMoved = false;
        curRadius = originRadius;
        path.reset();
        path = null;
        path = new Path();
        invalidate();
    }

    /**
     * 根据移动的距离来刷新原来的圆半径大小
     *
     * @param distance
     */
    private void refreshCurRadiusByMoveDistance(int distance) {
        if (distance > maxMoveLength) {
            isArrivedMaxMoved = true;
            curRadius = 0;
        } else {
            isArrivedMaxMoved = false;
            float calcRadius = (1 - 1f * distance / maxMoveLength) * originRadius;
            float maxRadius = ABTextUtil.dip2px(context, 2);
            curRadius = (int) Math.max(calcRadius, maxRadius);
        }

    }

    /**
     * 改变某控件的高度
     *
     * @param view
     * @param height
     */
    private void changeViewHeight(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (null == lp) {
            lp = originLp;
        }
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 回滚状态动画
     */
    private void startRollBackAnimation(long duration) {
        ValueAnimator rollBackAnim = ValueAnimator.ofFloat(curRadius, originRadius);
        rollBackAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                curRadius = (int) value;
                invalidate();
            }
        });
        rollBackAnim.setInterpolator(new BounceInterpolator()); // 反弹效果
        rollBackAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                DraggableFlagView.this.clearAnimation();
            }
        });
        rollBackAnim.setDuration(duration);
        rollBackAnim.start();
    }

    /**
     * 计算四个坐标的三角边关系
     */
    class Triangle {
        double deltaX;
        double deltaY;
        double hypotenuse;

        @Override
        public String toString() {
            return "Triangle{" +
                    "deltaX=" + deltaX +
                    ", deltaY=" + deltaY +
                    ", hypotenuse=" + hypotenuse +
                    '}';
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.setVisibility(VISIBLE);
        invalidate();
    }
}

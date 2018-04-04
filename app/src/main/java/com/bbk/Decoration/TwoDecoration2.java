package com.bbk.Decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rtj on 2017/11/23.
 */
public class TwoDecoration2 extends RecyclerView.ItemDecoration{
    private int space;
    private Paint paint;
    private int count;

    public TwoDecoration2(int space, String color, int count) {
        this.space = space;
        this.count = count;
        paint = new Paint();
        paint.setColor(Color.parseColor(color));

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个右边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        outRect.right = space;
        //由于每行都只有2个，第一个不需要，所以第一个都是2的倍数，把右边距设为0
        if (parent.getChildLayoutPosition(view)==count-1){
            outRect.bottom = 0;
        }
        if (parent.getChildLayoutPosition(view) %2==0 && parent.getChildLayoutPosition(view)>count) {
            outRect.left = 0;
            outRect.right = 0;
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 1; i < childCount - count; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + space;
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}

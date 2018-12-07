package cn.kuaishang.kssdk.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class KSBaseCustomCompositeView extends RelativeLayout{

    public KSBaseCustomCompositeView(Context context) {
        this(context, null);
    }

    public KSBaseCustomCompositeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KSBaseCustomCompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, getResId(), this);
        initView();
        initData();
    }

    protected abstract int getResId();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View> T getViewById(@IdRes int id) {
        return (T) findViewById(id);
    }
}
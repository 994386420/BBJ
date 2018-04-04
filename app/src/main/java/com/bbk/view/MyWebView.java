package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by rtj on 2018/1/15.
 */

public class MyWebView extends WebView {
    private boolean is_gone = false;

    public MyWebView(Context context) {
        super(context);
    }
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == View.GONE) {
            try {
                WebView.class.getMethod("onPause").invoke(this);// stop flash
            } catch(Exception e) {

            }
            this.pauseTimers();
            this.is_gone = true;
        } else if (visibility == View.VISIBLE) {
            try {
                WebView.class.getMethod("onResume").invoke(this);// resume flash
            } catch(Exception e) {
            }
            this.resumeTimers();
            this.is_gone = false;
        }
    }

    @Override

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.is_gone) {
            try {
                this.destroy();
            } catch
                    (Exception e) {
            }
        }
    }

}

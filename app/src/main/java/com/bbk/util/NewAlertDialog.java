package com.bbk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;

/**
 * Created by Administrator on 2018/7/06/006.
 */

public class NewAlertDialog extends Dialog {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static AlertDialog alertDialog = null;
    private static int showCount = 0;
    private Context context;
    Activity mParentActivity;
    public NewAlertDialog(Context context, int theme) {
        super(context, theme);
        mParentActivity= (Activity) context;
    }

    public NewAlertDialog(Context context) {
        super(context);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static NewAlertDialog create(Context context, boolean cancelable, OnCancelListener cancelListener) {
        NewAlertDialog dialog = new  NewAlertDialog(context, R.style.dialog);
        dialog.setContentView(R.layout.loading_dialog);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.dialog_img);
			final RotateAnimation animation =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1000);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setRepeatMode(Animation.RESTART);
			animation.setInterpolator(LINEAR_INTERPOLATOR);
			imageView.setAnimation(animation);
			animation.startNow();
//			imageView.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE,0.5);
			dialog.setCanceledOnTouchOutside(false);
			Window window = dialog.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			params.gravity = Gravity.CENTER;
			//设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			window.setAttributes(params);
		    window.setWindowAnimations(R.style.dialog_style);
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
//    dialog.show();
        return dialog;
    }
    @Override
    public void dismiss()
    {
        // 避免依附界面销毁跑出的异常java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView
        if (mParentActivity != null && !mParentActivity.isFinishing())
        {
            super.dismiss();    //调用超类对应方法
        }
    }
}

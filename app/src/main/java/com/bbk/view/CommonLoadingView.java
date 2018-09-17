package com.bbk.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.NewDianpuHomeActivity;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class CommonLoadingView extends FrameLayout {


    //加载时显示文字
    protected TextView mLoadingTextTv;
    public Context mContext;
    //加载错误视图
    protected RelativeLayout mLoadErrorLl;
    //加载错误点击事件处理
    private LoadingHandler mLoadingHandler;
    //加载view
    private View loadingView;
    //加载失败view
    private View loadingErrorView;
    //数据为空
    private View emptyView;
    //数据为空提示文字
    private TextView emptyText;
    private TextView mallText;//去逛逛商城
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();


    public CommonLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        mLoadingHandler = loadingHandler;
    }

    public void setLoadingErrorView(View loadingErrorView) {
        this.removeViewAt(1);
        this.loadingErrorView = loadingErrorView;
        this.loadingErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingHandler != null) {
                    mLoadingHandler.doRequestData();
                    CommonLoadingView.this.load();
                }
            }
        });
        this.addView(loadingErrorView,1);
    }

    public void setLoadingView(View loadingView) {
        this.removeViewAt(0);
        this.loadingView = loadingView;
        this.addView(loadingView,0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loadingView = inflate(mContext, R.layout.loading_dialog, null);
        loadingErrorView = inflate(mContext, R.layout.no_network_layout, null);
        emptyView = inflate(mContext, R.layout.activity_no_message_layout, null);
        this.addView(loadingView);
        this.addView(loadingErrorView);
        this.addView(emptyView);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        initView(this);
    }


    public void setMessage(String message) {
        mLoadingTextTv.setText(message);
    }


    private void initView(View rootView) {
        mLoadingTextTv = (TextView) rootView.findViewById(R.id.tips_tv);
        ImageView imageView = (ImageView) findViewById(R.id.dialog_img);
        final RotateAnimation animation =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(LINEAR_INTERPOLATOR);
        imageView.setAnimation(animation);
        animation.startNow();
        emptyText = rootView.findViewById(R.id.tv_message);
        mallText = rootView.findViewById(R.id.tv_mall);
        mLoadErrorLl = rootView.findViewById(R.id.mzhanwei_layout);
        mLoadErrorLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingHandler != null) {
                    CommonLoadingView.this.load();
                    mLoadingHandler.doRequestData();
                }
            }
        });
    }

    public void load(){
        loadingView.setVisibility(VISIBLE);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }

    public void load(String message){
        mLoadingTextTv.setText(message);
        emptyText.setText(message);
        loadingView.setVisibility(VISIBLE);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }

    public void loadEmpty(String message){
        emptyText.setText(message);
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
    }


    public void loadSuccess(){
        this.loadSuccess(false);
    }

    public void loadSuccess(boolean isEmpty){
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(GONE);
        if (isEmpty) {
            emptyView.setVisibility(VISIBLE);
        }else{
            emptyView.setVisibility(GONE);
        }
    }

    public void loadHomeSuccess(final Context context,String message,String message1,boolean isEmpty){
        emptyText.setText(message);
        mallText.setText(message1);
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(GONE);
        if (isEmpty) {
            mallText.setVisibility(VISIBLE);
            mallText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    NewConstants.refeshFlag = "1";
                    Intent intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                }
            });
            emptyView.setVisibility(VISIBLE);
        }else{
            emptyView.setVisibility(GONE);
        }
    }

    public void loadMallSuccess(final Context context, boolean isEmpty){
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(GONE);
        if (isEmpty) {
            mallText.setVisibility(VISIBLE);
            mallText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewDianpuHomeActivity.class);
                    context.startActivity(intent);
                }
            });
            emptyView.setVisibility(VISIBLE);
        }else{
            emptyView.setVisibility(GONE);
        }
    }

    public void loadError(){
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(VISIBLE);
    }


    public interface LoadingHandler{
        void doRequestData();
    }
}

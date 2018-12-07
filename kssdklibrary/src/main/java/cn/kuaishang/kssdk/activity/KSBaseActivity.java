package cn.kuaishang.kssdk.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.kuaishang.kssdk.R;

/**
 * Created by Admin on 2017/2/5.
 */

public abstract class KSBaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());

        getViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
        setListener();
        initData();
        setStatusBarColor();

    }

    private void setStatusBarColor(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.ks_main));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract int getResId();
    protected abstract void initView();
    protected abstract void setListener();
    protected abstract void initData();

    protected <T extends View> T getViewById(@IdRes int id) {
        return (T) findViewById(id);
    }
}

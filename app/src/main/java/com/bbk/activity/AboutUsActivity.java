package com.bbk.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.dialog.ActionSheetDialog;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.ConfirmOrderActivity;
import com.bbk.util.DataCleanManager;
import com.bbk.util.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 关于比比鲸界面
 */
public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_app_data)
    TextView tvAppData;
    @BindView(R.id.ll_clear_data)
    LinearLayout llClearData;
    private ImageView backBtn;
    private TextView versionView;
    private TextView mversion;
    private LinearLayout magrement;
    private LinearLayout mgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        try {
            tvAppData.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
    }

    private void initView() {
        mversion = (TextView) findViewById(R.id.mversion);
        magrement = (LinearLayout) findViewById(R.id.magrement);
        magrement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(AboutUsActivity.this, WebViewActivity.class);
                String url = Constants.MAIN_BASE_URL_MOBILE + "mobile/user/agreement";
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        mgrade = (LinearLayout) findViewById(R.id.mgrade);
        mgrade.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String url = "http://app.qq.com/#id=detail&appid=1104896963";
                Intent intent = new Intent(AboutUsActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        mversion.setText(getVersionName(this));

        backBtn = (ImageView) findViewById(R.id.topbar_goback_btn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.topbar_goback_btn:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnClick(R.id.ll_clear_data)
    public void onViewClicked() {
        new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(14, "确认删除所有缓存?",
                ActionSheetDialog.SheetItemColor.Gray, null).addSheetItem(18, "确定", ActionSheetDialog.SheetItemColor.Red,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        DataCleanManager.clearAllCache(getApplicationContext());
                        try {
                            tvAppData.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
    }
}

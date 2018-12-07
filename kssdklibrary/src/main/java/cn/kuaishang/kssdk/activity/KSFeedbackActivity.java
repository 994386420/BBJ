package cn.kuaishang.kssdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.kuaishang.kssdk.KSConfig;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.util.StringUtil;

/**
 * Created by Admin on 2017/2/5.
 */

public class KSFeedbackActivity extends KSBaseActivity implements View.OnClickListener{

    private Context context = this;
    private EditText fbName;
    private EditText fbContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getResId() {
        return R.layout.ks_activity_feedback;
    }

    @Override
    protected void initView() {
        fbName = getViewById(R.id.fbName);
        fbContent = getViewById(R.id.fbContent);

        TextView titleView = getViewById(R.id.title_tv);
        titleView.setText(R.string.ks_title_leave_msg);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.fbCommit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                String name = StringUtil.getString(fbName.getText());
                String content = StringUtil.getString(fbContent.getText());
                if(StringUtil.isEmpty(name)){
                    Toast.makeText(KSFeedbackActivity.this,"请输入姓名", Toast.LENGTH_LONG).show();
                    return;
                }else if(StringUtil.isEmpty(content)){
                    Toast.makeText(KSFeedbackActivity.this,"请输入留言", Toast.LENGTH_LONG).show();
                    return;
                }
                map.put("linkman", name);
                map.put("content", content);
                KSConfig.getController(context).sendFeedback(map);
                onBackPressed();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}

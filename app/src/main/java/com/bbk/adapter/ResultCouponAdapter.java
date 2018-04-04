package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.EventIdIntentUtil;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/1/24.
 */

public class ResultCouponAdapter extends BaseAdapter {
    private List<Map<String, String>> list;
    private Context context;

    public ResultCouponAdapter(Context context, List<Map<String, String>> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.result_dialog, null);
            vh.mmoney = (TextView) convertView.findViewById(R.id.mmoney);
            vh.mdesc = (TextView) convertView.findViewById(R.id.mdesc);
            vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
            vh.mgetcoupon = (TextView) convertView.findViewById(R.id.mgetcoupon);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        if (list!= null && list.size()>0){
            Map<String,String> map = list.get(position);
            String time = map.get("time");
            String money = map.get("money");
            String desc = map.get("desc");
            final String url = map.get("url");
            vh.mmoney.setText(money+"元");
            vh.mdesc.setText(desc);
            vh.mtime.setText("有效期"+time);
            vh.mgetcoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   taobaoLogin(context,url);
                }
            });
        }
        return convertView;
    }
    public void  taobaoLogin(final Context context, final String url){
        if (AlibcLogin.getInstance().getSession()!= null){
            String nick = AlibcLogin.getInstance().getSession().nick;
            if (nick!= null && !"".equals(nick)) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);
            }else {
                AlibcLogin alibcLogin = AlibcLogin.getInstance();

                alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "登录成功 ",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url",url);
                        context.startActivity(intent);
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(context, "登录失败 ",
                                Toast.LENGTH_LONG).show();
                    }
                });
        }
        }else {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url",url);
            context.startActivity(intent);
        }

    }
    class ViewHolder{
        TextView mmoney,mdesc,mtime,mgetcoupon;

    }
}

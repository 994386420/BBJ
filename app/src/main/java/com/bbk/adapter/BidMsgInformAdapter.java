package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.PubaMessageBean;
import com.bbk.Bean.SearchResultBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidMsgInformAdapter extends BaseAdapter{
    private List<PubaMessageBean> pubaMessageBeans;
    private Context context;
    public BidMsgInformAdapter(Context context, List<PubaMessageBean> pubaMessageBeans){
        this.pubaMessageBeans = pubaMessageBeans;
        this.context = context;
    }
    public void notifyData(List<PubaMessageBean> beans){
        this.pubaMessageBeans.addAll(beans);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return pubaMessageBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return pubaMessageBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.fragment_bid_inform_listview, null);
                viewHolder.mtitle = (TextView)convertView.findViewById(R.id.mtitle);
                viewHolder.mtime = (TextView)convertView.findViewById(R.id.mtime);
                viewHolder.mcontent = (TextView)convertView.findViewById(R.id.mcontent);
                viewHolder.muserandprice = (TextView)convertView.findViewById(R.id.muserandprice);
                viewHolder.itemlayout = convertView.findViewById(R.id.result_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final PubaMessageBean pubaMessageBean = pubaMessageBeans.get(position);
            String id = pubaMessageBean.getId();
            String roledesc = pubaMessageBean.getRoledesc();
            String title = pubaMessageBean.getTitle();
            String isread = pubaMessageBean.getIsread();
            String extra = pubaMessageBean.getExtra();
            String event = pubaMessageBean.getEvent();
            String userid = pubaMessageBean.getUserid();
            String role = pubaMessageBean.getRole();
            String fbid = pubaMessageBean.getFbid();
            String head = pubaMessageBean.getHead();
            String dtimes = pubaMessageBean.getDtimes();
            viewHolder.mtitle.setText(head);
            viewHolder.mtime.setText(dtimes);
            viewHolder.mcontent.setText(roledesc+":"+title);
            viewHolder.muserandprice.setText(extra+",戳我查看详情~");
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String role = pubaMessageBean.getRole();
                        Intent intent;
                        if ("1".equals(role)) {
                            intent = new Intent(context, BidBillDetailActivity.class);
                        } else {
                            intent = new Intent(context, BidMyBillDetailActivity.class);
                        }
                        intent.putExtra("fbid", pubaMessageBean.getFbid());
                        context.startActivity(intent);
                        readSysmsg(pubaMessageBean.getId());
                }
            });
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
    class ViewHolder{
        TextView mtitle,mtime,mcontent,muserandprice;
        LinearLayout itemlayout;
    }
    private void readSysmsg(String id) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("msgid",id);
        RetrofitClient.getInstance(context).createBaseApi().readSysmsg(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        Log.i("===",s);
                    }
                    @Override
                    protected void hideDialog() {
                    }

                    @Override
                    protected void showDialog() {
                    }
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        StringUtil.showToast(context, "网络异常");
                    }
                });
    }
}

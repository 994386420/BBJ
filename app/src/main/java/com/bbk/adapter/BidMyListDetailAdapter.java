package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.BidActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.BidMyPlActivity;
import com.bbk.activity.BidMyWantPLActivity;
import com.bbk.activity.R;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/2/27.
 */

public class BidMyListDetailAdapter extends BaseAdapter implements ResultEvent{
    private Context context;
    private List<Map<String,String>> list;
    private int curposition;
    private DataFlow6 dataFlow;



    public BidMyListDetailAdapter(Context context, List<Map<String,String>> list){
        this.context = context;
        this.list = list;
        this.dataFlow = new DataFlow6(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.bid_list_detail_listview, null);
            vh.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            vh.mtypetext = (TextView) convertView.findViewById(R.id.mtypetext);
            vh.item_title = (TextView) convertView.findViewById(R.id.item_title);
            vh.mprice = (TextView) convertView.findViewById(R.id.mprice);
            vh.mcount = (TextView) convertView.findViewById(R.id.mcount);
            vh.mbidnum = (TextView) convertView.findViewById(R.id.mbidnum);
            vh.mtext1 = (TextView) convertView.findViewById(R.id.mtext1);
            vh.mtext2 = (TextView) convertView.findViewById(R.id.mtext2);
            vh.mtimebefor = (TextView) convertView.findViewById(R.id.mtimebefor);
            vh.mendprice = (TextView) convertView.findViewById(R.id.mendprice);
            vh.mtime = (RushBuyCountDownTimerView) convertView.findViewById(R.id.mtime);
            vh.mpricebox = (LinearLayout) convertView.findViewById(R.id.mpricebox);
            vh.mtextbox = (LinearLayout) convertView.findViewById(R.id.mtextbox);
            vh.mhenggang = convertView.findViewById(R.id.mhenggang);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (list.size()>0){
            Map<String,String> map = list.get(position);
            String bidstatus = map.get("bidstatus");
            final String id = map.get("bidid");
            final String fbid = map.get("id");
            final String endtime = map.get("endtime");
            initData(vh,map);
            vh.mtext1.setVisibility(View.GONE);
            switch (bidstatus){
                case "-1":
                    vh.mtypetext.setText("已失效");
                    vh.mtextbox.setVisibility(View.GONE);
                    break;
                case "0":
                    vh.mtypetext.setText("已取消");
                    vh.mtime.setVisibility(View.GONE);
                    vh.mtimebefor.setText(endtime);
                    vh.mtextbox.setVisibility(View.GONE);
                    break;
                case "1":
                    vh.mtypetext.setText("正接镖");
                    vh.mtext2.setText("取消接镖");
                    vh.mtext2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curposition = position;
                            new AlertDialog(context).builder().setTitle("提示").setMsg("是否取消发镖？")
                                    .setPositiveButton("确定", new View.OnClickListener() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onClick(View v) {
                                            upData(id,1);
                                        }
                                    }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();

                        }
                    });
                    break;
                case "2":
                    vh.mtypetext.setText("待评论");
                    vh.mtext2.setText("评论");
                    vh.mtext2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,BidMyWantPLActivity.class);
                            intent.putExtra("id",fbid);
                            context.startActivity(intent);
                        }
                    });
                    break;
                case "3":
                    vh.mtypetext.setText("已完成");
                    vh.mtext2.setText("查看评论");
                    vh.mtext2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,BidMyPlActivity.class);
                            intent.putExtra("id",fbid);
                            context.startActivity(intent);
                        }
                    });
                    break;
            }

        }
        return convertView;
    }
    public void upData(String id, int i){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jbid",id);
        dataFlow.requestData(i, "bid/cancelBid", paramsMap, this,true);
    }
    public void initData(BidMyListDetailAdapter.ViewHolder vh, Map<String, String> map){
        String title = map.get("title");
        String price = map.get("price");
        String img = map.get("img");
        String number = map.get("number");
        String bidnum = map.get("bidnum");
        String bidprice = map.get("bidprice");
        String url = map.get("url");
        vh.item_title.setText(title);
        vh.mcount.setText("x"+number);
        vh.mbidnum.setText("接镖 "+bidnum+" 人");
        vh.mprice.setText(price);
        vh.mendprice.setText("￥"+bidprice);
        Glide.with(context).load(img).placeholder(R.mipmap.zw_img_300).into(vh.item_img);

    }


    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                if(dataJo.optInt("status")<=0){
                    Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                }else{
                    list.remove(curposition);
                    notifyDataSetChanged();
                    Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:

                break;
        }
    }

    class ViewHolder {
        RushBuyCountDownTimerView mtime;
        TextView mtypetext,item_title,mprice,mcount,mbidnum,mtext1,mtext2,mtimebefor,mendprice;
        ImageView item_img;
        LinearLayout mpricebox,mtextbox;
        View mhenggang;
    }
}

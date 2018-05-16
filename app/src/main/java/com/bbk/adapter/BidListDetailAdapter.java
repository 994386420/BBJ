package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.BidActivity;
import com.bbk.activity.BidMyPlActivity;
import com.bbk.activity.BidMyWantPLActivity;
import com.bbk.activity.R;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.StringUtil;
import com.bbk.view.CircleImageView1;
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

public class BidListDetailAdapter extends BaseAdapter implements ResultEvent{
    private Context context;
    private List<Map<String,String>> list;
    private int curposition;
    private DataFlow6 dataFlow;



    public BidListDetailAdapter(Context context, List<Map<String,String>> list){
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
            vh.mhenggang = convertView.findViewById(R.id.mhenggang);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            if (list.size()>0){
                Map<String,String> map = list.get(position);
                String status = map.get("status");
                final String id = map.get("id");
                String endtime = map.get("endtime");
//                vh.mtime.start();
                switch (status){
                    case "0":
                        vh.mtimebefor.setText("距结束");
                        vh.mtypetext.setText("待审核");
                        vh.mtime.friendly_time(endtime,"#999999");
                        vh.mtime.setVisibility(View.VISIBLE);
                        initStartData(vh,map);
                        vh.mtext2.setVisibility(View.GONE);
                        vh.mtext1.setText("取消我要");
                        vh.mtext1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                curposition = position;
                                new AlertDialog(context).builder().setTitle("提示").setMsg("是否取消我要？")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onClick(View v) {
                                                upData(id,"bid/cancelBid",1);
                                            }
                                        }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();

                            }
                        });
                        break;
                    case "1":
                        vh.mtimebefor.setText("距结束");
                        vh.mtypetext.setText("待扑倒");
                        initStartData(vh,map);
                        vh.mtime.friendly_time(endtime,"#999999");
                        vh.mtime.setVisibility(View.VISIBLE);
                        vh.mtext1.setText("取消我要");
                        vh.mtext1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                curposition = position;
                                new AlertDialog(context).builder().setTitle("提示").setMsg("是否取消我要？")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onClick(View v) {
                                                upData(id,"bid/cancelBid",1);
                                            }
                                        }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        });
                        vh.mtext2.setText("延长时间");
                        vh.mtext2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                curposition = position;
                                new AlertDialog(context).builder().setTitle("提示").setMsg("只能延长一次，且延长24小时")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onClick(View v) {
                                                upData(id,"bid/extendTime",2);
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
                        vh.mtimebefor.setText("交易完成");
                        vh.mtime.setVisibility(View.GONE);
                        initEndData(vh,map);
                        rebid(vh,id);
                        vh.mtext2.setText("评论");
                        vh.mtext2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context,BidMyWantPLActivity.class);
                                intent.putExtra("id",id);
                                context.startActivity(intent);
                            }
                        });
                        break;
                    case "3":
                        vh.mtypetext.setText("已取消");
                        vh.mtimebefor.setText(endtime);
                        vh.mtime.setVisibility(View.GONE);
                        initData(vh,map);
                        vh.mtext2.setVisibility(View.GONE);
                        rebid(vh,id);
                        break;
                    case "4":
                        vh.mtypetext.setText("未通过审核");
                        vh.mtimebefor.setText(endtime);
                        vh.mtime.setVisibility(View.GONE);
                        initStartData(vh,map);
                        vh.mtext1.setText("取消我要");
                        vh.mtext1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                curposition = position;
                                new AlertDialog(context).builder().setTitle("提示").setMsg("是否取消我要？")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onClick(View v) {
                                                upData(id,"bid/cancelBid",1);
                                            }
                                        }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        });
                        vh.mtext2.setText("修改");
                        vh.mtext2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, BidActivity.class);
                                intent.putExtra("type","2");
                                intent.putExtra("id",id);
                                context.startActivity(intent);
                            }
                        });
                        break;
                    case "5":
                        vh.mtypetext.setText("已失效");
                        vh.mtimebefor.setText(endtime);
                        vh.mtime.setVisibility(View.GONE);
                        initStartData(vh,map);
                        vh.mtext2.setVisibility(View.GONE);
                        rebid(vh,id);
                        break;
                    case "6":
                        vh.mtypetext.setText("已完成");
                        vh.mtimebefor.setText("评论完成");
                        vh.mtime.setVisibility(View.GONE);
                        initEndData(vh,map);
                        vh.mtext2.setText("查看评论");
                        vh.mtext2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context,BidMyPlActivity.class);
                                intent.putExtra("id",id);
                                context.startActivity(intent);
                            }
                        });
                        rebid(vh,id);
                        break;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    public void rebid(ViewHolder vh, final String id){
        vh.mtext1.setText("再次我要");
        vh.mtext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BidActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
    }

    public void upData(String id, String api, int i){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",id);
        dataFlow.requestData(i, api, paramsMap, this,true);
    }

    public void initData(ViewHolder vh, Map<String, String> map){
            String title = map.get("title");
            String price = map.get("price");
            String img = map.get("img");
            String number = map.get("number");
            String bidnum = map.get("bidnum");
            String url = map.get("url");
            vh.item_title.setText(title);
            vh.mcount.setText("x"+number);
            vh.mbidnum.setText("扑倒 "+bidnum+" 人");
            vh.mprice.setText(price);
            Glide.with(context).load(img).placeholder(R.mipmap.zw_img_300).into(vh.item_img);

    }
    public void initStartData(ViewHolder vh, Map<String, String> map){
        try {
            String endtime = map.get("endtime");
            vh.mtime.friendly_time(endtime,"#999999");
//            vh.mtime.start();
            initData(vh,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initEndData(ViewHolder vh, Map<String, String> map){
        String finalprice = map.get("finalprice");
        if (finalprice != null){
            vh.mtime.setVisibility(View.GONE);
            vh.mhenggang.setVisibility(View.VISIBLE);
            vh.mpricebox.setVisibility(View.VISIBLE);
            vh.mendprice.setText("￥"+finalprice);
            initData(vh,map);
        }else {
            vh.mtime.setVisibility(View.VISIBLE);
            vh.mhenggang.setVisibility(View.GONE);
            vh.mpricebox.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                if(dataJo.optInt("status")<=0){
                    StringUtil.showToast(context, "取消失败");
                }else{
                    list.remove(curposition);
                    notifyDataSetChanged();
                    StringUtil.showToast(context,"取消成功");
                }
                break;
            case 2:
                switch (content){
                    case "-1":
                        StringUtil.showToast(context,"已经延长过了");
                        break;
                    case "0":
                        StringUtil.showToast(context,"延长失败");
                        break;
                    case "1":
                        StringUtil.showToast(context,"延长成功");
                        break;
                }
                break;
        }
    }

    class ViewHolder {
        RushBuyCountDownTimerView mtime;
        TextView mtypetext,item_title,mprice,mcount,mbidnum,mtext1,mtext2,mtimebefor,mendprice;
        ImageView item_img;
        LinearLayout mpricebox;
        View mhenggang;
    }
}

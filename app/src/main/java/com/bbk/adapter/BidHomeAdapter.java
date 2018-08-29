package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.BidAllHotActivity;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.DateUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bbk.view.RushBuyCountDownTimerViewBidHome;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/5.
 */

public class BidHomeAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private int ITEM_TYPE_TOP = 1; //顶部 tag,轮滑
    private int ITEM_TYPE_HOT = 2; //今日热镖
    private int ITEM_TYPE_BTN_AND_TITLE = 3; //全部热镖按钮和猜你喜欢标题
    private int ITEM_TYPE_LIST = 4; //猜你喜欢
    private OnItemClickListener mOnItemClickListener = null;
    private JSONArray toparray_tag,toparray_flipper,hotarray,listarray;
    private List<Map<String, String>> list;

    public BidHomeAdapter(Context context, List<Map<String, String>> list){
        try {
            this.context = context;
            this.list = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        } else if (position >= 1 && position <1+hotarray.length()) {
            return ITEM_TYPE_HOT;
        } else if (position == 1+hotarray.length()) {
            return ITEM_TYPE_BTN_AND_TITLE;
        }else {
            return ITEM_TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TOP) {
            TopViewHolder TopViewHolder = new TopViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_bidhome_top, parent, false));
            return TopViewHolder;
        } else if (viewType == ITEM_TYPE_HOT) {
            HotViewHolder HotViewHolder = new HotViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_bidhome_hot, parent, false));
            return HotViewHolder;
        } else if (viewType == ITEM_TYPE_BTN_AND_TITLE) {
            BtnAndTitleViewHolder BtnAndTitleViewHolder = new BtnAndTitleViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_bidhome_btnandtitle, parent, false));
            return BtnAndTitleViewHolder;
        } else if (viewType == ITEM_TYPE_LIST) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_bidhome_likelist, parent, false);
            LikeListViewHolder LikeListViewHolder = new LikeListViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return LikeListViewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof TopViewHolder) {
                TopViewHolder viewHolder = (TopViewHolder) holder;
                initTop(viewHolder);
            } else if (holder instanceof HotViewHolder) {
                HotViewHolder viewHolder = (HotViewHolder) holder;
                viewHolder.itemView.setTag(position);
                initHot(viewHolder,position-1);
            } else if (holder instanceof BtnAndTitleViewHolder) {
                BtnAndTitleViewHolder viewHolder = (BtnAndTitleViewHolder) holder;
                viewHolder.mallhotbid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, BidAllHotActivity.class);
                        context.startActivity(intent);
                    }
                });
            } else if (holder instanceof LikeListViewHolder) {
                LikeListViewHolder viewHolder = (LikeListViewHolder) holder;
                //将position保存在itemView的Tag中，以便点击时进行获取
                viewHolder.itemView.setTag(position);
                initList(viewHolder,position - 2 - hotarray.length());
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initList(LikeListViewHolder viewHolder, int position) throws JSONException {
        try {
            final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            final JSONObject object = listarray.getJSONObject(position);
            String time = object.optString("endtime");
            viewHolder.mtime.friendly_time(time,"#999999");
            viewHolder.mprice.setText("买方出价￥"+object.optString("price"));
            viewHolder.mtitle.setText(object.optString("title"));
            final String url = object.optString("url");
            String img = object.optString("img");
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (object.optString("userid").equals(userID)){
                            Intent intent = new Intent(context,BidBillDetailActivity.class);
                            intent.putExtra("fbid",object.optString("id"));
                            context.startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id",object.optString("id"));
                            context.startActivity(intent);
                        }  }catch (Exception e){
                        e.printStackTrace();
                        StringUtil.showToast(context,"网络连接异常");
                    }

                }
            });
            Glide.with(context).load(img).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initHot(HotViewHolder viewHolder, int i) throws JSONException, ParseException {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        ImageView[] img = {viewHolder.mimg1,viewHolder.mimg2,viewHolder.mimg3};
        final JSONObject object = hotarray.getJSONObject(i);
        viewHolder.mtitle.setText(object.optString("title"));
        viewHolder.mtime.addsum(object.optString("endtime"),"#b40000");
        viewHolder.mtime.start();
        viewHolder.mprice.setText("￥"+object.optString("price"));
        viewHolder.mextra.setText(object.optString("extra"));
        JSONArray imgs = object.getJSONArray("imgs");
        try {
                switch (imgs.length()){
                    case 1:
                        Glide.with(context).load(imgs.getString(0)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg1);
//                        Glide.with(context).load(imgs.getString(0)).into(viewHolder.mimg2);
                        viewHolder.mimg2.setImageResource(R.color.white);
                        viewHolder.mimg3.setImageResource(R.color.white);
//                        Glide.with(context).load(imgs.getString(0)).into(viewHolder.mimg3);
                        break;
                    case 2:
                        Glide.with(context).load(imgs.getString(0)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg1);
                        Glide.with(context).load(imgs.getString(1)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg2);
                        viewHolder.mimg3.setImageResource(R.color.white);
                        break;
                    case 3:
                        Glide.with(context).load(imgs.getString(0)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg1);
                        Glide.with(context).load(imgs.getString(1)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg2);
                        Glide.with(context).load(imgs.getString(2)).placeholder(R.mipmap.zw_img_300).into(viewHolder.mimg3);
                        break;
                }
//            for (int j = 0; j < 3; j++) {
//                switch (j){
//                    case 0:
//                        Glide.with(context).load(imgs.getString(0)).into(img[0]);
//                        break;
//                    case 1:
//                        Glide.with(context).load(imgs.getString(0)).into(img[0]);
//                        Glide.with(context).load(imgs.getString(1)).into(img[1]);
//                        break;
//                    case 2:
//                        Glide.with(context).load(imgs.getString(0)).into(img[0]);
//                        Glide.with(context).load(imgs.getString(1)).into(img[1]);
//                        Glide.with(context).load(imgs.getString(2)).into(img[2]);
//                        break;
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (object.optString("userid").equals(userID)){
                        Intent intent = new Intent(context,BidBillDetailActivity.class);
                        intent.putExtra("fbid",object.optString("id"));
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, BidDetailActivity.class);
                        intent.putExtra("id",object.optString("id"));
                        context.startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    StringUtil.showToast(context,"网络连接异常");
                }
            }
        });

    }

    private void initTop(TopViewHolder viewHolder) throws JSONException {
        TextView[] text = {viewHolder.text1,viewHolder.text2,viewHolder.text3,viewHolder.text4,viewHolder.text5};
        ImageView[] img = {viewHolder.img1,viewHolder.img2,viewHolder.img3,viewHolder.img4,viewHolder.img5};
        LinearLayout[] lin = {viewHolder.box1,viewHolder.box2,viewHolder.box3,viewHolder.box4,viewHolder.box5};
        for (int i = 0; i < 5; i++) {
            final JSONObject object = toparray_tag.getJSONObject(i);
            text[i].setText(object.optString("name"));
            Glide.with(context).load(object.optString("imgurl")).placeholder(R.mipmap.zw_img_160).into(img[i]);
            lin[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BidAcceptanceActivity.class);
                    intent.putExtra("type",object.optString("name"));
                    context.startActivity(intent);
                }
            });
        }
        loadFlipper(viewHolder);
    }

    private void loadFlipper(TopViewHolder viewHolder) throws JSONException {
        for (int i = 0; i < toparray_flipper.length(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.flipper_bidhome, null);
            TextView mtitle = (TextView) view.findViewById(R.id.mtitle);
            TextView mbuyprice = (TextView) view.findViewById(R.id.mbuyprice);
            TextView msellprice = (TextView) view.findViewById(R.id.msellprice);
            TextView mcount = (TextView) view.findViewById(R.id.mcount);
            ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
            final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            final JSONObject object = toparray_flipper.getJSONObject(i);
            final String id = object.optString("id");
            String title = object.optString("title");
            String count = object.optString("count");
            String buyprice = object.optString("buyprice");
            String img = object.optString("img");
            String sellprice = object.optString("sellprice");
            final String url = object.optString("url");
            mtitle.setText(title);
            mbuyprice.setText("买方出价 "+buyprice);
            msellprice.setText("接单价 "+sellprice);
            mcount.setText("接单中"+count+"人");
            Glide.with(context).load(img).into(mimg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (object.get("userid").equals(userID)){
                            Intent intent = new Intent(context,BidBillDetailActivity.class);
                            intent.putExtra("fbid",id);
                            context.startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id",id);
                            context.startActivity(intent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(context, R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(context, R.anim.lunbo_chu);
        viewHolder.mviewflipper.setInAnimation(ru);
        viewHolder.mviewflipper.setOutAnimation(chu);
        viewHolder.mviewflipper.startFlipping();
    }

    @Override
    public int getItemCount() {
        try {
            if (list.size() == 0){
                    this.toparray_tag = new JSONArray();
                    this.toparray_flipper = new JSONArray();
                    this.hotarray = new JSONArray();
                    this.listarray = new JSONArray();
                }else {
                    Map<String,String> map = list.get(0);
                    String list1 = map.get("list1");
                    String list2 = map.get("list2");
                    String list3 = map.get("list3");
                    String list4 = map.get("list4");
                    this.toparray_tag = new JSONArray(list1);
                    this.toparray_flipper = new JSONArray(list2);
                    this.hotarray = new JSONArray(list3);
                    this.listarray = new JSONArray(list4);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 2+hotarray.length()+listarray.length();
    }

    class LikeListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mimg;
        private TextView mtitle,mprice;
        private RushBuyCountDownTimerViewBidHome mtime;
        public LikeListViewHolder(View v) {
            super(v);
            mimg = (ImageView)v.findViewById(R.id.mimg);
            mtitle = (TextView)v.findViewById(R.id.mtitle);
            mtime = (RushBuyCountDownTimerViewBidHome) v.findViewById(R.id.mtime);
            mprice = (TextView)v.findViewById(R.id.mprice);
        }
    }
    class BtnAndTitleViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout mallhotbid;
        public BtnAndTitleViewHolder(View v) {
            super(v);
            mallhotbid = (LinearLayout)v.findViewById(R.id.mallhotbid);
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder{
        private TextView mprice,mtitle,mextra;
        private ImageView mimg1,mimg2,mimg3;
        private RushBuyCountDownTimerView mtime;
        public HotViewHolder(View v){
            super(v);
            mtime = (RushBuyCountDownTimerView) v.findViewById(R.id.mtime);
            mprice = (TextView) v.findViewById(R.id.mprice);
            mextra = (TextView) v.findViewById(R.id.mextra);
            mtitle = (TextView) v.findViewById(R.id.mtitle);
            mimg1 = (ImageView) v.findViewById(R.id.mimg1);
            mimg2 = (ImageView) v.findViewById(R.id.mimg2);
            mimg3 = (ImageView) v.findViewById(R.id.mimg3);
        }
    }


    class TopViewHolder extends RecyclerView.ViewHolder {
        private ViewFlipper mviewflipper;
        private LinearLayout  box1, box2, box3, box4, box5;
        private TextView text1, text2, text3, text4, text5;
        private ImageView img1, img2, img3, img4, img5;

        public TopViewHolder(View mView) {
            super(mView);
            mviewflipper = (ViewFlipper) mView.findViewById(R.id.mviewflipper);
            box1 = (LinearLayout) mView.findViewById(R.id.box1);
            box2 = (LinearLayout) mView.findViewById(R.id.box2);
            box3 = (LinearLayout) mView.findViewById(R.id.box3);
            box4 = (LinearLayout) mView.findViewById(R.id.box4);
            box5 = (LinearLayout) mView.findViewById(R.id.box5);
            text1 = (TextView) mView.findViewById(R.id.text1);
            text2 = (TextView) mView.findViewById(R.id.text2);
            text3 = (TextView) mView.findViewById(R.id.text3);
            text4 = (TextView) mView.findViewById(R.id.text4);
            text5 = (TextView) mView.findViewById(R.id.text5);
            img1 = (ImageView) mView.findViewById(R.id.img1);
            img2 = (ImageView) mView.findViewById(R.id.img2);
            img3 = (ImageView) mView.findViewById(R.id.img3);
            img4 = (ImageView) mView.findViewById(R.id.img4);
            img5 = (ImageView) mView.findViewById(R.id.img5);
        }
    }

}

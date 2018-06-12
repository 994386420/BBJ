package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.Bean.NewHomeBlBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomeFxBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.DataFragmentActivity;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.component.HomeAllComponent;
import com.bbk.component.HomeAllComponent1;
import com.bbk.component.HomeBijiaComponent;
import com.bbk.component.SimpleComponent;
import com.bbk.flow.DataFlow;
import com.bbk.fragment.OnClickListioner;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zyao89.view.zloading.ZLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2017/12/1.
 */

public class NewHomeAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private List<Map<String, String>> taglist, list, mList, mAddList;
    private JSONArray tag, gongneng, fabiao, banner;
    private int ITEM_TYPE_TOP = 1; //顶部 banner,tag,util
    private int ITEM_TYPE_TUIJIAN = 2; //为你推荐
    private DataFlow dataFlow;
    private String wztitle = "";
    private OnItemClickListener mOnItemClickListener = null;
    private String type = "1";
    private boolean isclear = false;
    private int page = 1, x = 1;
    JSONArray array;
    private OnClickListioner onClickListioner;
    private NewBjAdapter adapter;
    private NewBlAdapter mBlAdapter;
    private NewCzgAdapter mCzgAdapter;
    private NewFxAdapter mFxAdapter;
    //    private RecyclerView mlistview;
    List<NewHomeCzgBean> newHomeCzgBean;
    private ZLoadingView bar;
    String typee;
    List<NewHomeCzgBean> czgBeans;
    List<NewHomePubaBean> newHomePubaBeans;
    private List<NewHomeBlBean> newHomeBlBean;
    private List<NewHomeFxBean> fxBeans;
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    private boolean isHomeGudie = false;
    Guide guide;
    private int showTimes = 0;


    public NewHomeAdapter(Context context, List<Map<String, String>> taglist, JSONArray banner, JSONArray tag, JSONArray fabiao, JSONArray gongneng, String type,
                          List<NewHomeCzgBean> newHomeCzgBean, List<NewHomePubaBean> newHomePubaBeans, List<NewHomeBlBean> newHomeBlBean, List<NewHomeFxBean> fxBeans, JSONObject jo) {
        this.context = context;
        this.tag = tag;
        this.gongneng = gongneng;
        this.fabiao = fabiao;
        this.dataFlow = new DataFlow(context);
        this.taglist = taglist;
        this.banner = banner;
        this.typee = type;
        this.newHomeCzgBean = newHomeCzgBean;
        this.newHomePubaBeans = newHomePubaBeans;
        this.newHomeBlBean = newHomeBlBean;
        this.fxBeans = fxBeans;
//        this.mlistview = recyclerView;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnClickListioner(OnClickListioner onClickListioner) {
        this.onClickListioner = onClickListioner;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void notifyData(List<NewHomeCzgBean> newHomeCzgBeans) {
        if (newHomeCzgBeans != null) {
            this.newHomeCzgBean.addAll(newHomeCzgBeans);
            notifyDataSetChanged();
        }
    }

    public void notifyBjData(List<NewHomePubaBean> newHomePubaBeans) {
        if (newHomePubaBeans != null) {
            this.newHomePubaBeans.addAll(newHomePubaBeans);
            notifyDataSetChanged();
        }
    }

    public void notifyBlData(List<NewHomeBlBean> blBeans) {
        if (blBeans != null) {
            this.newHomeBlBean.addAll(blBeans);
            notifyDataSetChanged();
        }
    }

    public void notifyFxData(List<NewHomeFxBean> fxBeans) {
        if (fxBeans != null) {
            this.fxBeans.addAll(fxBeans);
            notifyDataSetChanged();
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        } else if (position >= 1) {
            return ITEM_TYPE_TUIJIAN;
        } else {
            return ITEM_TYPE_TOP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TOP) {
            TopViewHolder TopViewHolder = new TopViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_home_top, parent, false));
            return TopViewHolder;
        } else if (viewType == ITEM_TYPE_TUIJIAN) {
            if (typee.equals("1")) {
                ViewHolderCzg ViewHolder = new ViewHolderCzg(
                        LayoutInflater.from(context).inflate(R.layout.czg_item_layout, parent, false));
                return ViewHolder;
            } else if (typee.equals("2")) {
                ViewHolderBj ViewHolder = new ViewHolderBj(
                        LayoutInflater.from(context).inflate(R.layout.bid_acceptance_listview, parent, false));
                return ViewHolder;
            } else if (typee.equals("3")) {
                ViewHolderBl ViewHolder = new ViewHolderBl(
                        LayoutInflater.from(context).inflate(R.layout.bl_item_layout, parent, false));
                return ViewHolder;
            } else if (typee.equals("4")) {
                ViewHolderFx ViewHolder = new ViewHolderFx(
                        LayoutInflater.from(context).inflate(R.layout.fx_item_layout, parent, false));
                return ViewHolder;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    @Override
    public int getItemCount() {
        if (typee.equals("1")) {
            if (newHomeCzgBean != null) {
                return 1 + newHomeCzgBean.size();
            } else {
                return 1;
            }
        } else if (typee.equals("2")) {
            if (newHomePubaBeans != null) {
                return 1 + newHomePubaBeans.size();
            } else {
                return 1;
            }
        } else if (typee.equals("3")) {
            if (newHomeBlBean != null) {
                return 1 + newHomeBlBean.size();
            } else {
                return 1;
            }
        } else if (typee.equals("4")) {
            if (fxBeans != null) {
                return 1 + fxBeans.size();
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof TopViewHolder) {
                TopViewHolder viewHolder = (TopViewHolder) holder;
                initTop(viewHolder, position);
            } else if (holder instanceof ViewHolderCzg) {
                ViewHolderCzg viewHolder = (ViewHolderCzg) holder;
                initTop(viewHolder, position - 1);
            } else if (holder instanceof ViewHolderBj) {
                ViewHolderBj viewHolderBj = (ViewHolderBj) holder;
                initTop(viewHolderBj, position - 1);
            } else if (holder instanceof ViewHolderBl) {
                ViewHolderBl viewHolderBl = (ViewHolderBl) holder;
                initTop(viewHolderBl, position - 1);
            } else if (holder instanceof ViewHolderFx) {
                ViewHolderFx viewHolderFx = (ViewHolderFx) holder;
                initTop(viewHolderFx, position - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 首页引导图层
     *
     * @param targetView
     * @param targetView1
     */
    public void showGuideView(View targetView, final View targetView1) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(20)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showGuideViewBijia(targetView1);
            }
        });

        builder.addComponent(new SimpleComponent()).addComponent(new HomeAllComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    public void showGuideViewBijia(View targetView) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(20)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                if (onClickListioner != null) {
                    onClickListioner.onDissmissClick();
                }
            }
        });

        builder.addComponent(new HomeBijiaComponent()).addComponent(new HomeAllComponent1());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    private void initTop(final TopViewHolder viewHolder, int positon) {
        try {
            //判断传过来数据是否为null
            if (banner != null && banner.length() > 0) {
                loadbanner(banner, viewHolder);
            }
            if (tag != null && tag.length() > 0) {
                loadTag(tag, viewHolder);
            }
            if (fabiao != null && fabiao.length() > 0) {
                loadViewflipper(fabiao, viewHolder);
            }
            viewHolder.compareutil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BidHomeActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.queryhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QueryHistoryActivity.class);
                    context.startActivity(intent);
                }
            });
            if (gongneng.length() >= 2) {
                Glide.with(context).
                        load(gongneng.getJSONObject(0).optString("img")).
                        placeholder(R.mipmap.bjsq).into(viewHolder.compareimg);
                Glide.with(context).load(gongneng.getJSONObject(1).optString("img")).placeholder(R.mipmap.lsyg).into(viewHolder.queryhistoryimg);
            }
            switch (typee) {
                case "1":
                    setView(viewHolder);
                    setText(viewHolder.mCzgText, viewHolder.mCzgView);
                    break;
                case "2":
                    setView(viewHolder);
                    setText(viewHolder.mBjText, viewHolder.mBjView);
                    break;
                case "3":
                    setView(viewHolder);
                    setText(viewHolder.mBlText, viewHolder.mBlView);
                    break;
                case "4":
                    setView(viewHolder);
                    setText(viewHolder.mFxText, viewHolder.mFxView);
                    break;
            }
            viewHolder.mLlCzgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "1";
                    setView(viewHolder);
//                    mIdex("1",2,true);
                    setText(viewHolder.mCzgText, viewHolder.mCzgView);
                    if (onClickListioner != null) {
                        onClickListioner.onCzgClick();
                    }
                }
            });
            viewHolder.mLlbjLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "2";
                    setView(viewHolder);
//                    mIdex("2",2,true);
                    setText(viewHolder.mBjText, viewHolder.mBjView);
                    if (onClickListioner != null) {
                        onClickListioner.onBjClick();
                    }
                }
            });
            viewHolder.mLlblLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "3";
                    setView(viewHolder);
//                    mIdex("3",2,true);
                    setText(viewHolder.mBlText, viewHolder.mBlView);
                    if (onClickListioner != null) {
                        onClickListioner.onBlClick();
                    }
                }
            });
            viewHolder.mLlfxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "4";
                    setView(viewHolder);
//                    mIdex("4",2,true);
                    setText(viewHolder.mFxText, viewHolder.mFxView);
                    if (onClickListioner != null) {
                        onClickListioner.onFxClick();
                    }
                }
            });
            viewHolder.mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchMainActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.mSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SortActivity.class);
                    context.startActivity(intent);
                }
            });

            if (positon == 0 && showTimes == 0) {
                final View finalView = viewHolder.compareutil;
                final View finalView1 = viewHolder.queryhistory;
                viewHolder.compareutil.post(new Runnable() {
                    @Override
                    public void run() {
                        //首页引导页只显示一次
                        String isFirstResultUse = SharedPreferencesUtil.getSharedData(context, "isFirstHomeUse", "isFirstHomeUserUse");
                        if (TextUtils.isEmpty(isFirstResultUse)) {
                            isFirstResultUse = "yes";
                        }
                        if (isFirstResultUse.equals("yes")) {
                            showGuideView(finalView, finalView1);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    //超值购等数据
//    private void mIdex(String str,int code,boolean is){
//        type = str;
//        x=1;
//        page=1;
//        isclear = true;
//        getIndexByType(is,code);
//    }
//    //首页分类数据
//    private void getIndexByType(boolean is,int code) {
//        HashMap<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("type",type);
//        paramsMap.put("page",page+"");
//        dataFlow.requestData(code, Constants.GetQueryAppIndexByType, paramsMap, this, is);
//    }
    private void setView(TopViewHolder topViewHolder) {
        topViewHolder.mCzgText.setTextColor(context.getResources().getColor(R.color.color_line_text));
        topViewHolder.mCzgView.setVisibility(View.GONE);
        topViewHolder.mBjText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mQbText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder.mBjView.setVisibility(View.GONE);
        topViewHolder.mBlText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mDfkText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder.mBlView.setVisibility(View.GONE);
        topViewHolder.mFxText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mDfhText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder.mFxView.setVisibility(View.GONE);
    }

    private void setText(TextView text, View view) {
        text.setTextColor(context.getResources().getColor(R.color.color_line_top));
        view.setVisibility(View.VISIBLE);
    }

    class TopViewHolder extends RecyclerView.ViewHolder {
        //        private GridView mgridView;
        private ViewFlipper mviewflipper;
        private LinearLayout queryhistory, compareutil, jingtopic, box1, box2, box3, box4, box5;
        private TextView text1, text2, text3, text4, text5;
        private ImageView img1, img2, img3, img4, img5, queryhistoryimg, compareimg;
        private Banner mBanner;
        private LinearLayout mLlCzgLayout, mLlbjLayout, mLlblLayout, mLlfxLayout;
        private View mCzgView, mBjView, mBlView, mFxView;
        private TextView mCzgText, mBjText, mBlText, mFxText;
        private LinearLayout mSort, mSearch;//搜索，分类;

        public TopViewHolder(View mView) {
            super(mView);
//            mgridView = (GridView) mView.findViewById(R.id.mgridView);
            mBanner = mView.findViewById(R.id.banner);
            mSearch = mView.findViewById(R.id.msearch);
            mSort = mView.findViewById(R.id.msort);
            mBanner = (Banner) mView.findViewById(R.id.banner);
            mviewflipper = (ViewFlipper) mView.findViewById(R.id.mviewflipper);
            queryhistory = (LinearLayout) mView.findViewById(R.id.queryhistory);
            compareutil = (LinearLayout) mView.findViewById(R.id.compareutil);
            jingtopic = (LinearLayout) mView.findViewById(R.id.jingtopic);
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
            queryhistoryimg = (ImageView) mView.findViewById(R.id.queryhistoryimg);
            compareimg = (ImageView) mView.findViewById(R.id.compareimg);
            mLlCzgLayout = mView.findViewById(R.id.ll_czg_layout);
            mLlbjLayout = mView.findViewById(R.id.ll_bj_layout);
            mLlblLayout = mView.findViewById(R.id.ll_bl_layout);
            mLlfxLayout = mView.findViewById(R.id.ll_fx_layout);
            mCzgText = mView.findViewById(R.id.czg_text);
            mBjText = mView.findViewById(R.id.bj_text);
            mBlText = mView.findViewById(R.id.bl_text);
            mFxText = mView.findViewById(R.id.fx_text);
            mCzgView = mView.findViewById(R.id.czg_view);
            mBjView = mView.findViewById(R.id.bj_view);
            mBlView = mView.findViewById(R.id.bl_view);
            mFxView = mView.findViewById(R.id.fx_view);
        }
    }


    /***
     * 加载中部图标
     * @param tag
     * @param viewHolder
     * @throws Exception
     */
    private void loadTag(final JSONArray tag, TopViewHolder viewHolder) throws Exception {
        taglist.clear();
        for (int i = 0; i < tag.length(); i++) {
            JSONObject object = tag.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            String htmlUrl = object.optString("htmlUrl");
            String eventId = object.optString("eventId");
            String img = object.optString("img");
            String name = object.optString("name");
            map.put("htmlUrl", htmlUrl);
            map.put("eventId", eventId);
            map.put("text", name);
            map.put("imageUrl", img);
            taglist.add(map);
        }
        List<ImageView> imglist = new ArrayList<>();
        List<TextView> textlist = new ArrayList<>();
        List<LinearLayout> boxlist = new ArrayList<>();
        imglist.add(viewHolder.img1);
        imglist.add(viewHolder.img2);
        imglist.add(viewHolder.img3);
        imglist.add(viewHolder.img4);
        imglist.add(viewHolder.img5);
        textlist.add(viewHolder.text1);
        textlist.add(viewHolder.text2);
        textlist.add(viewHolder.text3);
        textlist.add(viewHolder.text4);
        textlist.add(viewHolder.text5);
        boxlist.add(viewHolder.box1);
        boxlist.add(viewHolder.box2);
        boxlist.add(viewHolder.box3);
        boxlist.add(viewHolder.box4);
        boxlist.add(viewHolder.box5);
        for (int i = 0; i < boxlist.size(); i++) {
            final int position = i;
            TextView textView = textlist.get(position);
            ImageView imageView = imglist.get(position);
            Map<String, String> map = taglist.get(position);
            String text = map.get("text").toString();
            textlist.get(position).setText(text);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            String imageUrl = map.get("imageUrl").toString();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.zw_img_160)
                    .thumbnail(0.5f)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 4) {
                        Intent intent = new Intent(context, DataFragmentActivity.class);
                        context.startActivity(intent);
                    } else {
                        try {
                            EventIdIntentUtil.EventIdIntent(context, tag.getJSONObject(position));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /***
     * 加载轮播
     * @param fabiao
     * @param viewHolder
     * @throws JSONException
     */
    private void loadViewflipper(JSONArray fabiao, TopViewHolder viewHolder) throws JSONException {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        for (int i = 0; i < fabiao.length(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.flipper_bidhome, null);
            TextView mtitle = view.findViewById(R.id.mtitle);
            TextView mbuyprice = view.findViewById(R.id.mbuyprice);
            TextView msellprice = view.findViewById(R.id.msellprice);
            TextView mcount = view.findViewById(R.id.mcount);
            ImageView mimg = view.findViewById(R.id.mimg);
            final JSONObject object = fabiao.getJSONObject(i);
            final String id = object.optString("id");
            String title = object.optString("title");
            final String count = object.optString("count");
            String buyprice = object.optString("buyprice");
            String img = object.optString("img");
            String sellprice = object.optString("sellprice");
            final String url = object.optString("url");
            mtitle.setText(title);
            mbuyprice.setText("我要价 " + buyprice);
            msellprice.setText("扑倒价 " + sellprice);
            mcount.setText("扑倒中" + count + "人");
            Glide.with(context).load(img).into(mimg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (object.get("userid").equals(userID)) {
                            Intent intent = new Intent(context, BidBillDetailActivity.class);
                            intent.putExtra("fbid", id);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id", id);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
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

    /**
     * 加载Banner
     *
     * @param banner
     * @param viewHolder
     */
    private void loadbanner(final JSONArray banner, final TopViewHolder viewHolder) {
        List<Object> imgUrlList = new ArrayList<>();
        try {
            for (int i = 0; i < banner.length(); i++) {
                JSONObject jo = banner.getJSONObject(i);
                String imgUrl = jo.getString("img");
                imgUrlList.add(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.mBanner.setImages(imgUrlList)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        try {
                            EventIdIntentUtil.EventIdIntent(context, banner.getJSONObject(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        viewHolder.mBanner.setOnTouchListener(new View.OnTouchListener() {
            public float startX;
            public float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewHolder.mBanner.requestDisallowInterceptTouchEvent(true);
                        // 记录手指按下的位置
                        startY = event.getY();
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取当前手指位置
                        float endY = event.getY();
                        float endX = event.getX();
                        float distanceX = Math.abs(endX - startX);
                        float distanceY = Math.abs(endY - startY);
                        viewHolder.mBanner.requestDisallowInterceptTouchEvent(true);
//                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX + 500 < distanceY) {
                            viewHolder.mBanner.requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 超值购数据
     */
    class ViewHolderCzg extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView mbidprice,mprice, youhui;
        TextView item_title, copy_title, copy_url;
        LinearLayout itemlayout, mCopyLayout;
        @BindView(R.id.dianpu_text)
        TextView dianpuText;
        @BindView(R.id.quan)
        TextView quan;
        @BindView(R.id.zuan)
        TextView zuan;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.bprice)
        TextView bprice;
        @BindView(R.id.tv_mall)
        TextView tvMall;
        @BindView(R.id.tv_sale)
        TextView tvSale;

        public ViewHolderCzg(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
            item_img = mView.findViewById(R.id.item_img);
            item_title = mView.findViewById(R.id.item_title);
            mbidprice = mView.findViewById(R.id.mbidprice);
            mprice = mView.findViewById(R.id.mprice);
            youhui = mView.findViewById(R.id.youhui_text);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
            copy_url = mView.findViewById(R.id.copy_url);
        }
    }

    private void initTop(final ViewHolderCzg viewHolder, final int position) {
        try {
            String img = newHomeCzgBean.get(position).getImgurl();
            final String title = newHomeCzgBean.get(position).getTitle();
            String price = newHomeCzgBean.get(position).getPrice();
            String dianpu = newHomeCzgBean.get(position).getDianpu();
            String youhui = newHomeCzgBean.get(position).getYouhui();
            String mbidprice = newHomeCzgBean.get(position).getHislowprice();//最低价
            viewHolder.item_title.setText("             "+title);
            viewHolder.tvSale.setText(newHomeCzgBean.get(position).getSale()+"人付款");
            try {
                if (mbidprice != null) {
                    viewHolder.mbidprice.setText("最低价 " + mbidprice);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if(newHomeCzgBean.get(position).getDomain() !=null){
                String domin = newHomeCzgBean.get(position).getDomain();
                if (domin.equals("taobao")){
                    viewHolder.tvMall.setText("淘宝");
                }else if (domin.equals("tmall")){
                    viewHolder.tvMall.setText("天猫");
                }else {
                    viewHolder.tvMall.setText("京东");
                }
            }else {
                viewHolder.tvMall.setVisibility(View.GONE);
            }
            viewHolder.mprice.setText("¥" + price);
            viewHolder.bprice.setText("¥" + newHomeCzgBean.get(position).getBprice());
            viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.price.setText(price);
            viewHolder.quan.setText(newHomeCzgBean.get(position).getQuan());
            viewHolder.zuan.setText(newHomeCzgBean.get(position).getZuan());
            viewHolder.dianpuText.setText(dianpu);
            viewHolder.youhui.setText(youhui);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    try {
//                        if (AlibcLogin.getInstance().isLogin() == true) {
                            if (JumpIntentUtil.isJump5(newHomeCzgBean, position)) {
                                intent = new Intent(context, IntentActivity.class);
                                if (newHomeCzgBean.get(position).getUrl() != null) {
                                    intent.putExtra("url", newHomeCzgBean.get(position).getRequestUrl());
                                }
                                if (newHomeCzgBean.get(position).getTitle() != null) {
                                    intent.putExtra("title", newHomeCzgBean.get(position).getTitle());
                                }
                                if (newHomeCzgBean.get(position).getDomain() != null) {
                                    intent.putExtra("domain", newHomeCzgBean.get(position).getDomain());
                                }
                                if (newHomeCzgBean.get(position).getRowkey() != null) {
                                    intent.putExtra("groupRowKey", newHomeCzgBean.get(position).getRowkey());
                                }
                                intent.putExtra("isczg", "1");
                                if (newHomeCzgBean.get(position).getBprice() != null) {
                                    intent.putExtra("bprice", newHomeCzgBean.get(position).getBprice());
                                }
                            } else {
                                intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", newHomeCzgBean.get(position).getUrl());
                                intent.putExtra("title", newHomeCzgBean.get(position).getTitle());
                            }
                            context.startActivity(intent);
//                        } else {
//                            DialogSingleUtil.show(context, "授权中...");
//                            TaoBaoLoginandLogout();//淘宝授权登陆
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(newHomeCzgBean.get(position).getUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 淘宝授权登录
     */
    private void TaoBaoLoginandLogout() {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

            @Override
            public void onSuccess() {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录成功 ");
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new Date());
                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "taobao", "taobaodata", date);
            }

            @Override
            public void onFailure(int code, String msg) {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录失败 ");
            }
        });
        DialogSingleUtil.dismiss(0);
    }

    /**
     * 扑吧数据
     */
    class ViewHolderBj extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title, mbidprice, mcount, mprice, copy_title;
        RushBuyCountDownTimerView mtime;
        LinearLayout itemlayout, mCopyLayout;

        public ViewHolderBj(View mView) {
            super(mView);
            item_img = (ImageView) mView.findViewById(R.id.item_img);
            item_title = (TextView) mView.findViewById(R.id.item_title);
            mbidprice = (TextView) mView.findViewById(R.id.mbidprice);
            mcount = (TextView) mView.findViewById(R.id.mcount);
            mprice = (TextView) mView.findViewById(R.id.mprice);
            mtime = (RushBuyCountDownTimerView) mView.findViewById(R.id.mtime);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
        }
    }

    private void initTop(final ViewHolderBj viewHolder, final int position) {
        try {
            String endtime = newHomePubaBeans.get(position).getEndtime();
            String img = newHomePubaBeans.get(position).getImg();
            String id = newHomePubaBeans.get(position).getId();
            final String title = newHomePubaBeans.get(position).getTitle();
            String price = newHomePubaBeans.get(position).getPrice();
            String number = newHomePubaBeans.get(position).getNumber();
            String type = newHomePubaBeans.get(position).getType();
            viewHolder.mtime.friendly_time(endtime, "#999999");
            viewHolder.item_title.setText(title);
            viewHolder.mbidprice.setVisibility(View.GONE);
            viewHolder.mprice.setText("¥" + price);
            viewHolder.mcount.setText("x" + number);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    try {
                        if (newHomePubaBeans.get(position).getUserid().equals(userID)) {
                            Intent intent = new Intent(context, BidBillDetailActivity.class);
                            intent.putExtra("fbid", newHomePubaBeans.get(position).getId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id", newHomePubaBeans.get(position).getId());
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 爆料数据
     */
    class ViewHolderBl extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title, mbidprice, mExtr, time, mlike, mcomment;
        TextView copy_title, copy_url;
        LinearLayout itemlayout, mCopyLayout;

        public ViewHolderBl(View mView) {
            super(mView);
            item_img = (ImageView) mView.findViewById(R.id.item_img);
            item_title = (TextView) mView.findViewById(R.id.item_title);
            mExtr = (TextView) mView.findViewById(R.id.mExtra);
            time = (TextView) mView.findViewById(R.id.bl_time);
            mlike = mView.findViewById(R.id.mlike);
            mcomment = mView.findViewById(R.id.mcomment);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
            copy_url = mView.findViewById(R.id.copy_url);
        }
    }

    private void initTop(final ViewHolderBl viewHolder, final int position) {
        try {
//            final Map<String,String> map = list.get(position);
            String mExtr = newHomeBlBean.get(position).getExtra();
            String img = newHomeBlBean.get(position).getImg();
            String time = newHomeBlBean.get(position).getDtime();
            final String title = newHomeBlBean.get(position).getTitle();
            String count = newHomeBlBean.get(position).getPlnum();
            String readnum = newHomeBlBean.get(position).getReadnum();//阅读数
            String zan = newHomeBlBean.get(position).getZannum();
            final String content = newHomeBlBean.get(position).getContent();
            viewHolder.item_title.setText(content);
            viewHolder.time.setText(time);
            viewHolder.mExtr.setText("¥" + newHomeBlBean.get(position).getPrice() + ", " + mExtr);
            viewHolder.mlike.setText(zan);
            viewHolder.mcomment.setText(readnum);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, GossipPiazzaDetailActivity.class);
                        intent.putExtra("blid", newHomeBlBean.get(position).getBlid());
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(newHomeBlBean.get(position).getUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发现数据
     */
    class ViewHolderFx extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title, mbidprice, content, time, mlike, mcomment;
        TextView copy_title, copy_url;
        LinearLayout itemlayout, mCopyLayout;

        public ViewHolderFx(View mView) {
            super(mView);
            item_img = mView.findViewById(R.id.item_img);
            item_title = mView.findViewById(R.id.item_title);
            content = mView.findViewById(R.id.content);
            time = mView.findViewById(R.id.bl_time);
            mlike = mView.findViewById(R.id.mlike);
            mcomment = mView.findViewById(R.id.mcomment);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
        }
    }

    private void initTop(final ViewHolderFx viewHolder, final int position) {
        try {
//            Map<String,String> map = list.get(position);
            String content = fxBeans.get(position).getContent();
            String img = fxBeans.get(position).getImg();
            String time = fxBeans.get(position).getAtime();
            final String title = fxBeans.get(position).getTitle();
            String count = fxBeans.get(position).getCount();
            String zan = fxBeans.get(position).getZan();
            viewHolder.item_title.setText(title);
            viewHolder.time.setText(time);
            viewHolder.content.setText(content);
            viewHolder.mlike.setText(zan);
            viewHolder.mcomment.setText(count);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        insertWenzhangGuanzhu(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发现点击事件
     */
//    private void insertWenzhangGuanzhu(int position) {
//        try {
//            wztitle  =fxBeans.get(position).getTitle();
//            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
//            String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
//            if (!TextUtils.isEmpty(userID)) {
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("userid", userID);
//                params.put("wzid",  fxBeans.get(position).getId());
//                params.put("token", token);
//                params.put("type", "2");
//                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
//            } else {
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("userid", "-1");
//                params.put("token", token);
//                params.put("wzid",  fxBeans.get(position).getId());
//                params.put("type", "2");
//                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    private void insertWenzhangGuanzhu(int position) {
        wztitle = fxBeans.get(position).getTitle();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
        if (!TextUtils.isEmpty(userID)) {
            insertWenzhangGuanzhu(userID, fxBeans.get(position).getId(), token, wztitle);
        } else {
            insertWenzhangGuanzhu("-1", fxBeans.get(position).getId(), token, wztitle);
        }

    }

    private void insertWenzhangGuanzhu(String userid, String wz, String token, final String wztitle) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userid);
        maps.put("wzid", wz);
        maps.put("token", token);
        maps.put("type", "2");
        RetrofitClient.getInstance(context).createBaseApi().insertWenzhangGuanzhu(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        Log.e("===", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Intent intent = new Intent(context, WebViewWZActivity.class);
                                intent.putExtra("title", wztitle);
                                intent.putExtra("url", jsonObject.optString("content"));
                                context.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(context);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(context, "网络异常");
                    }
                });
    }

}

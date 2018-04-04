package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bbk.Bean.HomeData;
import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyWantPLActivity;
import com.bbk.activity.DomainMoreActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.HttpUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.TencentLoginUtil;
import com.bbk.view.BaseViewHolder;
import com.bbk.view.MyGridView;
import com.bbk.view.MyXRefresh;
import com.bbk.view.RollHeaderView;
import com.bbk.view.RollHeaderView3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2017/12/1.
 */

public class HomeAdapter extends RecyclerView.Adapter implements ResultEvent, View.OnClickListener {
    private Context context;
    private List<Map<String, String>> taglist;
    private List<Map<String, String>> guesslikelist;
    private JSONArray dianpu, articles, activity, banner, tag, tujian, gongneng;
    private int ITEM_TYPE_TOP = 1; //顶部 banner,tag,util
    private int ITEM_TYPE_TUIJIAN = 2; //为你推荐
    private int ITEM_TYPE_DIANPU = 3; //店铺，商城活动
    private int ITEM_TYPE_ACTICLES = 4; //文章
    private int ITEM_TYPE_LISTHEAD = 5; //猜你喜欢头部
    private int ITEM_TYPE_LIST = 6; // 猜你喜欢
    private DataFlow dataFlow;
    private String wztitle = "";
    private OnItemClickListener mOnItemClickListener = null;
    private int topcount;
    private Thread thread;
    private int outnum = 0;
    private int innum = 0;
    private MyXRefresh refreshLayout;


    public HomeAdapter(Context context, List<Map<String, String>> taglist, List<Map<String, String>> guesslikelist,
                       List<HomeData> arrlist, MyXRefresh refreshLayout) {
        this.context = context;
        this.taglist = taglist;
        this.dianpu = arrlist.get(0).getDianpu();
        this.articles = arrlist.get(0).getArticles();
        this.activity = arrlist.get(0).getActivity();
        this.banner = arrlist.get(0).getBanner();
        this.tag = arrlist.get(0).getTag();
        this.tujian = arrlist.get(0).getTujian();
        this.guesslikelist = guesslikelist;
        this.gongneng = arrlist.get(0).getGongneng();
        this.dataFlow = new DataFlow(context);
        this.topcount = 3 + articles.length() + tujian.length();
        this.refreshLayout = refreshLayout;
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

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        } else if (position >= 1 && position < 1 + tujian.length()) {
            return ITEM_TYPE_TUIJIAN;
        } else if (position == 1 + tujian.length()) {
            return ITEM_TYPE_DIANPU;
        } else if (position > 1 + tujian.length() && position <= 1 + tujian.length() + articles.length()) {
            return ITEM_TYPE_ACTICLES;
        } else if (position <= 2 + tujian.length() + articles.length()) {
            return ITEM_TYPE_LISTHEAD;
        } else {
            return ITEM_TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TOP) {
            TopViewHolder TopViewHolder = new TopViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_home_top, parent, false));
            return TopViewHolder;
        } else if (viewType == ITEM_TYPE_TUIJIAN) {
            TuiJianViewHolder TuiJianViewHolder = new TuiJianViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.home_recomend_goods, parent, false));
            return TuiJianViewHolder;
        } else if (viewType == ITEM_TYPE_DIANPU) {
            DianpuViewHolder DianpuViewHolder = new DianpuViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_home_dianpu, parent, false));
            return DianpuViewHolder;
        } else if (viewType == ITEM_TYPE_ACTICLES) {
            ArticlesViewHolder ArticlesViewHolder = new ArticlesViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.home_acticles_list, parent, false));
            return ArticlesViewHolder;
        } else if (viewType == ITEM_TYPE_LISTHEAD) {
            ListHeadViewHolder ListHeadViewHolder = new ListHeadViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.fragment_home_articles, parent, false));
            return ListHeadViewHolder;
        } else if (viewType == ITEM_TYPE_LIST) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_guess_like, parent, false);
            ListViewHolder ListViewHolder = new ListViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return ListViewHolder;
        } else {
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return guesslikelist.size() + topcount;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof TopViewHolder) {
                TopViewHolder viewHolder = (TopViewHolder) holder;
                initTop(viewHolder);
            } else if (holder instanceof TuiJianViewHolder) {
                TuiJianViewHolder viewHolder = (TuiJianViewHolder) holder;
                initTuiJian(viewHolder, position - 1);
            } else if (holder instanceof DianpuViewHolder) {
                DianpuViewHolder viewHolder = (DianpuViewHolder) holder;
                initDianpu(viewHolder);
            } else if (holder instanceof ArticlesViewHolder) {
                ArticlesViewHolder viewHolder = (ArticlesViewHolder) holder;
                loadActicles(viewHolder, position - 2 - tujian.length());
            } else if (holder instanceof ListViewHolder) {
                ListViewHolder viewHolder = (ListViewHolder) holder;
                //将position保存在itemView的Tag中，以便点击时进行获取
                viewHolder.itemView.setTag(position - topcount);
                initList(viewHolder, position - topcount);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initList(ListViewHolder vh, int position) {
        if (guesslikelist.size() != 0) {
            final Map<String, String> map = guesslikelist.get(position);
            String title = map.get("title");
            String price = map.get("price");
            String img = map.get("img");
            String youhui = map.get("youhui");
            String st = map.get("st");
            String et = map.get("et");
            String sale = map.get("sale");
            String bigprice;
            String littleprice;
            if (price.contains(".")) {
                int end = price.indexOf(".");
                bigprice = price.substring(0, end);
                littleprice = price.substring(end, price.length());
            } else {
                bigprice = price;
                littleprice = ".0";
            }

            vh.title.setText(title);
            vh.mbigprice.setText(bigprice);
            vh.mlittleprice.setText(littleprice);
            vh.msale.setText("月销" + sale);
            vh.mdate.setText("( " + st + "~" + et + " )");
            vh.mcoupon.setText(youhui);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            @SuppressWarnings("deprecation")
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = vh.mimg.getLayoutParams();
            params.height = (width - BaseTools.getPixelsFromDp(context, 4)) / 2;
            vh.mimg.setLayoutParams(params);
            Glide.with(context)
                    .load(img)
                    .into(vh.mimg);
        }
    }


    private void initDianpu(DianpuViewHolder viewHolder) {
        try {
            if (viewHolder.dianpubox != null) {
                viewHolder.dianpubox.removeAllViews();
            }
            RollHeaderView3 bannerView1 = new RollHeaderView3(context, 0.22f, true);
            viewHolder.activitycontainer.addView(bannerView1);
            loadDomainActivity(activity, bannerView1);
            loadDianPu(dianpu, viewHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class TuiJianViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mdomain1, mdomain2;
        private RecyclerView mrecy;
        private TextView tagtext1, tagtext2, title, text, mbigprice, mlittleprice, mdianpu, text2, mbigprice2, mlittleprice2, mdianpu2;
        private RelativeLayout tag1, tag2, mmore, mseebijia, mseebijia2;
        private ImageView tagimg, mimg2, mimg;

        public TuiJianViewHolder(View view) {
            super(view);
            mdomain1 = (LinearLayout) view.findViewById(R.id.domain1);
            mdomain2 = (LinearLayout) view.findViewById(R.id.domain2);
            mrecy = (RecyclerView) view.findViewById(R.id.mrecy);
            tagtext1 = (TextView) view.findViewById(R.id.tagtext1);
            tagtext2 = (TextView) view.findViewById(R.id.tagtext2);
            tag1 = (RelativeLayout) view.findViewById(R.id.tag1);
            tag2 = (RelativeLayout) view.findViewById(R.id.tag2);
            tagimg = (ImageView) view.findViewById(R.id.tagimg);
            mmore = (RelativeLayout) view.findViewById(R.id.mmore);
            title = (TextView) view.findViewById(R.id.title);
            mimg = (ImageView) view.findViewById(R.id.img1);
            text = (TextView) view.findViewById(R.id.text1);
            mbigprice = (TextView) view.findViewById(R.id.mbigprice1);
            mlittleprice = (TextView) view.findViewById(R.id.mlittleprice1);
            mdianpu = (TextView) view.findViewById(R.id.mdianpu1);
            mseebijia = (RelativeLayout) view.findViewById(R.id.seebijia1);
            mimg2 = (ImageView) view.findViewById(R.id.img2);
            text2 = (TextView) view.findViewById(R.id.text2);
            mbigprice2 = (TextView) view.findViewById(R.id.mbigprice2);
            mlittleprice2 = (TextView) view.findViewById(R.id.mlittleprice2);
            mdianpu2 = (TextView) view.findViewById(R.id.mdianpu2);
            mseebijia2 = (RelativeLayout) view.findViewById(R.id.seebijia2);
        }
    }

    private void initTuiJian(TuiJianViewHolder viewHolder, int i) {
        try {
            JSONObject object = tujian.getJSONObject(i);
            addTuijian(object, viewHolder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initTop(TopViewHolder viewHolder) {
        try {
            loadbanner(banner, viewHolder);
            loadTag(tag, viewHolder);
            loadViewflipper(viewHolder);
            viewHolder.compareutil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (TextUtils.isEmpty(userID)){
                        Intent intent4= new Intent(context, UserLoginNewActivity.class);
                        context.startActivity(intent4);
                    }else {
//                        Intent intent = new Intent(context, BidHomeActivity.class);
                        Intent intent = new Intent(context, SearchMainActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            viewHolder.queryhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QueryHistoryActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.jingtopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.initone();
                }
            });
            if (gongneng.length() >= 2) {
                Glide.with(context).
                        load(gongneng.getJSONObject(0).optString("img")).
                        placeholder(R.mipmap.bjsq).into(viewHolder.compareimg);
                Glide.with(context).load(gongneng.getJSONObject(1).optString("img")).placeholder(R.mipmap.lsyg).into(viewHolder.queryhistoryimg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class TopViewHolder extends RecyclerView.ViewHolder {
        //        private GridView mgridView;
        private ViewFlipper mviewflipper;
        private LinearLayout queryhistory, compareutil, jingtopic, box1, box2, box3, box4, box5;
        private TextView text1, text2, text3, text4, text5;
        private ImageView img1, img2, img3, img4, img5, queryhistoryimg, compareimg;
        private Banner banner;

        public TopViewHolder(View mView) {
            super(mView);
//            mgridView = (GridView) mView.findViewById(R.id.mgridView);
            banner = (Banner) mView.findViewById(R.id.banner);
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

        }
    }

    class DianpuViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout activitycontainer, dianpubox;

        public DianpuViewHolder(View mView) {
            super(mView);
            activitycontainer = (LinearLayout) mView.findViewById(R.id.activitycontainer);
            dianpubox = (LinearLayout) mView.findViewById(R.id.dianpubox);
        }
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {
        private TextView mtitle, mauthor, mcomment, mlike;
        private ImageView mimg, img1;

        public ArticlesViewHolder(View view) {
            super(view);
            mtitle = (TextView) view.findViewById(R.id.title);
            mauthor = (TextView) view.findViewById(R.id.mauthor);
            mcomment = (TextView) view.findViewById(R.id.mcomment);
            mlike = (TextView) view.findViewById(R.id.mlike);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            img1 = (ImageView) view.findViewById(R.id.img1);
        }
    }

    class ListHeadViewHolder extends RecyclerView.ViewHolder {
        public ListHeadViewHolder(View mView) {
            super(mView);
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView mimg;
        TextView title, mbigprice, mlittleprice, msale, mdate, mcoupon;

        public ListViewHolder(View v) {
            super(v);
            mimg = (ImageView) v.findViewById(R.id.mimg);
            title = (TextView) v.findViewById(R.id.title);
            mbigprice = (TextView) v.findViewById(R.id.mbigprice);
            mlittleprice = (TextView) v.findViewById(R.id.mlittleprice);
            msale = (TextView) v.findViewById(R.id.msale);
            mdate = (TextView) v.findViewById(R.id.mdate);
            mcoupon = (TextView) v.findViewById(R.id.mcoupon);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        viewHolder.banner.setImages(imgUrlList)
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
        viewHolder.banner.setOnTouchListener(new View.OnTouchListener() {
            public float startX;
            public float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewHolder.banner.requestDisallowInterceptTouchEvent(true);
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
                        viewHolder.banner.requestDisallowInterceptTouchEvent(true);
                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX+500 < distanceY) {
                            viewHolder.banner.requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    private void loadDomainActivity(final JSONArray ja, RollHeaderView3 bannerView1) {
        List<Object> imgUrlList = new ArrayList<>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String imgUrl = jo.getString("img");
                imgUrlList.add(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (imgUrlList.size() != 0) {
            bannerView1.setOnHeaderViewClickListener(new RollHeaderView3.HeaderViewClickListener() {

                @Override
                public void HeaderViewClick(int position) {
                    JSONObject object;
                    try {
                        object = ja.getJSONObject(position);
                        if (null != object.optString("eventId")) {
                            EventIdIntentUtil.EventIdIntent(context, object);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            bannerView1.setImgUrlData(imgUrlList);
        }
    }

    private void loadDianPu(final JSONArray dianpu, DianpuViewHolder viewHolder) throws Exception {
        for (int i = 0; i < dianpu.length(); i++) {
            final JSONObject object = dianpu.getJSONObject(i);
            String img = object.optString("img");
            ImageView image = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 20, 0);
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            Glide.with(context).load(img).into(image);
            image.setAdjustViewBounds(true);
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    EventIdIntentUtil.EventIdIntent(context, object);
                }
            });
            viewHolder.dianpubox.addView(image);
        }

    }

    private void loadActicles(ArticlesViewHolder viewHolder, int i) {
        try {
            JSONObject object = articles.getJSONObject(i);
            final String title = object.optString("title");
            String type = object.optString("type");
            String author = object.optString("author");
            String count = object.optString("count");
            String zan = object.optString("zan");
            String img = object.optString("img");
            final String id = object.optString("id");
            viewHolder.mtitle.setText(title);
            viewHolder.mlike.setText(zan);
            viewHolder.mcomment.setText(count);
            viewHolder.mauthor.setText(author);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            @SuppressWarnings("deprecation")
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = viewHolder.mimg.getLayoutParams();
            params.height = width * 500 / 1190;
            viewHolder.mimg.setLayoutParams(params);
            initimg1(type, viewHolder.img1);
            Glide.with(context).load(img).placeholder(R.mipmap.zhanwei_01).into(viewHolder.mimg);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    insertWenzhangGuanzhu(title, id);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertWenzhangGuanzhu(String title, String id) {
        wztitle = title;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
        if (!TextUtils.isEmpty(userID)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userid", userID);
            params.put("wzid", id);
            params.put("token", token);
            params.put("type", "2");
            dataFlow.requestData(1, "newService/insertWenzhangGuanzhu", params, this);
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userid", "-1");
            params.put("token", token);
            params.put("wzid", id);
            params.put("type", "2");
            dataFlow.requestData(1, "newService/insertWenzhangGuanzhu", params, this);
        }

    }

    private void initimg1(String type, ImageView img1) {
        switch (type) {
            case "健康指南":
                img1.setImageResource(R.mipmap.txt_03);
                break;
            case "旅游出行":
                img1.setImageResource(R.mipmap.txt_02);
                break;
            case "美食盘点":
                img1.setImageResource(R.mipmap.txt_04);
                break;
            case "早秋穿搭":
                img1.setImageResource(R.mipmap.txt_01);
                break;
            case "我的娃儿":
                img1.setImageResource(R.mipmap.txt_05);
                break;

            default:
                break;
        }
    }

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
                        Intent intent = new Intent(context, DomainMoreActivity.class);
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

//        HomeFirstGridAdapter tagadapter = new HomeFirstGridAdapter(context, taglist);
//        viewHolder.mgridView.setAdapter(tagadapter);
//        viewHolder.mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                if (position == 4) {
//                    Intent intent = new Intent(context, DomainMoreActivity.class);
//                    context.startActivity(intent);
//                }else {
//                    try {
//                        EventIdIntentUtil.EventIdIntent(context,tag.getJSONObject(position));
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

    }

    private void loadViewflipper(TopViewHolder viewHolder) throws JSONException {
        List<String> viewflipperlist = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < articles.length(); i++) {
                JSONObject object2 = articles.getJSONObject(i);
                final String title = object2.optString("title");
                viewflipperlist.add(title);
            }
        }

        for (int i = 0; i < articles.length(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.viewflipper_home, null);
            TextView title1 = (TextView) view.findViewById(R.id.title1);
            TextView title2 = (TextView) view.findViewById(R.id.title2);
            title1.setText(viewflipperlist.get(2 * i));
            title2.setText(viewflipperlist.get(2 * i + 1));
            viewHolder.mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(context, R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(context, R.anim.lunbo_chu);
        viewHolder.mviewflipper.setInAnimation(ru);
        viewHolder.mviewflipper.setOutAnimation(chu);
        viewHolder.mviewflipper.startFlipping();
    }

    private void addTuijian(final JSONObject object, TuiJianViewHolder viewHolder) {
        try {
            JSONArray arr = object.getJSONArray("mainlist");
            JSONObject object2 = arr.getJSONObject(0);
            if (arr.length() == 1) {
                addLinearlayout1(viewHolder, object2);
                viewHolder.mdomain2.setVisibility(View.GONE);
            }
            if (arr.length() >= 2) {
                JSONObject object3 = arr.getJSONObject(1);
                addLinearlayout1(viewHolder, object2);
                addLinearlayout2(viewHolder, object3);
                viewHolder.mdomain2.setVisibility(View.VISIBLE);
            }
            final String producttype = object.optString("producttype");
            String kouhao = object.optString("kouhao");
            if (object.optString("keyword") != null && !"".equals(object.optString("keyword"))) {
                String keyword = object.optString("keyword");
                if (keyword.contains("|")) {
                    String[] str = keyword.split("\\|");
                    if (str.length >= 2) {
                        viewHolder.tagtext1.setText(str[0]);
                        viewHolder.tagtext2.setText(str[1]);
                        viewHolder.tag1.setVisibility(View.VISIBLE);
                        viewHolder.tag2.setVisibility(View.VISIBLE);
                        viewHolder.tagimg.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tagtext1.setText(str[0]);
                        viewHolder.tagtext2.setText(str[1]);
                        viewHolder.tag1.setVisibility(View.VISIBLE);
                        viewHolder.tag2.setVisibility(View.VISIBLE);
                    }

                } else {
                    viewHolder.tagtext1.setText(keyword);
                    viewHolder.tag1.setVisibility(View.VISIBLE);
                }
            }
            JSONArray otherlist;
            otherlist = object.getJSONArray("otherlist");
            viewHolder.title.setText(kouhao);
            viewHolder.mmore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context, ResultMainActivity.class);
                    intent.putExtra("keyword", producttype);
                    context.startActivity(intent);
                }
            });

            addHroziDomain(viewHolder.mrecy, otherlist);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void addLinearlayout1(TuiJianViewHolder viewHolder, JSONObject object) throws Exception {
        String img = object.optString("img");
        String title = object.optString("title");
        String price = object.optString("price");
        String domain = object.optString("domain");
        String service = object.optString("service");
        String list = object.getJSONArray("list").toString();
        String url = object.optString("url");
        String rowkey = object.optString("rowkey");
        addlinearlayoutData(img, title, price, domain, service, list, url, rowkey, viewHolder.mdomain1
                , viewHolder.mimg, viewHolder.text, viewHolder.mbigprice,
                viewHolder.mlittleprice, viewHolder.mdianpu, viewHolder.mseebijia);

    }

    private void addlinearlayoutData(String img, final String title, String price, final String domain, String service,
                                     final String list, final String url, final String rowkey, LinearLayout mdomain, ImageView mimg,
                                     TextView text, TextView mbigprice, TextView mlittleprice, TextView mdianpu, RelativeLayout mseebijia) {
        mdianpu.setText(service);
        text.setText(title);
        mdomain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent;
                if (JumpIntentUtil.isJump1(domain)) {
                    intent = new Intent(context, IntentActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("domain", domain);
                    intent.putExtra("url", url);
                    intent.putExtra("groupRowKey", rowkey);
                } else {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("groupRowKey", rowkey);
                }
                context.startActivity(intent);
            }
        });
        mseebijia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ResultDialogActivity.class);
                intent.putExtra("tarr", list);
                intent.putExtra("keyword", "推荐");
                context.startActivity(intent);
            }
        });
        if (price.contains(".")) {
            int end = price.indexOf(".");
            String bigprice = price.substring(0, end);
            String littleprice = price.substring(end, price.length());
            mbigprice.setText(bigprice);
            mlittleprice.setText(littleprice);
        } else {
            mbigprice.setText(price);
        }
        Glide.with(context).load(img).placeholder(R.mipmap.zhanwei_01).into(mimg);

    }

    private void addLinearlayout2(TuiJianViewHolder viewHolder, JSONObject object) throws Exception {
        String img = object.optString("img");
        String title = object.optString("title");
        String price = object.optString("price");
        String domain = object.optString("domain");
        String service = object.optString("service");
        String list = object.getJSONArray("list").toString();
        String url = object.optString("url");
        String rowkey = object.optString("rowkey");
        addlinearlayoutData(img, title, price, domain, service, list, url, rowkey,
                viewHolder.mdomain2, viewHolder.mimg2, viewHolder.text2, viewHolder.mbigprice2,
                viewHolder.mlittleprice2, viewHolder.mdianpu2, viewHolder.mseebijia2);
    }

    private void addHroziDomain(RecyclerView mrecy, JSONArray otherlist) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < otherlist.length(); i++) {
            Map<String, String> map = new HashMap<>();
            JSONObject object = otherlist.getJSONObject(i);
            map.put("imgUrl", object.optString("imgUrl"));
            map.put("price", object.optString("price"));
            map.put("title", object.optString("title"));
            map.put("rowkey", object.optString("rowkey"));
            map.put("url", object.optString("url"));
            map.put("domain", object.optString("domain"));
            list.add(map);
        }
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mrecy.setLayoutManager(linearLayoutManager);
        //设置适配器
        HomeTuijianRecyAdapter mAdapter = new HomeTuijianRecyAdapter(context, list);
        mrecy.setAdapter(mAdapter);
    }

    private void NowPrice() {
        thread = new Thread(new Runnable() {

            public void run() {
                try {
                    int outcount = tujian.length();

                    for (int i = 0; i < outcount; i++) {
                        outnum = i;
                        int a = tujian.getJSONObject(outnum).getJSONArray("mainlist").length();
                        JSONObject object = tujian.getJSONObject(i);
                        Map<String, String> params = new HashMap<>();
                        for (int j = 0; j < 12; j++) {
                            if (j == 1) {
                                if (a >= 2) {
                                    requestdata();
                                } else {
                                    continue;
                                }
                            } else if (j == 0) {
                                requestdata();
                            } else {
                                requestdata();
                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void requestdata() {
//        if (!"0".equals(tujian.getJSONObject(outnum).getJSONArray("mainlist"))) {
//            String str;
//            String content;
//            params.put("domain", gridlist1.get(requestnum).get("domain1").toString());
//            params.put("rowkey", gridlist1.get(requestnum).get("groupRowKey").toString());
//            params.put("fromwhere", "android" + titlelist.get(currentIndex).get("keyword"));
//            if (gridlist1.get(requestnum).get("purl").toString().contains("||")) {
//                String url = gridlist1.get(requestnum).get("purl").toString();
//                String[] split = url.split("\\|\\|");
//                String referrer = split[1];
//                content = HttpUtil.getHttp1(params, split[0], getActivity(), referrer);
//                params.put("pcontent", content);
//                str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", getActivity());
//            } else {
//                content = HttpUtil.getHttp1(params, gridlist1.get(requestnum).get("purl").toString(), getActivity(), null);
//                params.put("pcontent", content);
//                str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", getActivity());
//            }
//            JSONObject object = new JSONObject(str);
//            if ("3".equals(object.optString("type"))) {
//                if ("".equals(object.optString("url"))) {
//                    content = HttpUtil.getHttp1(params, gridlist1.get(requestnum).get("url").toString(), getActivity(), null);
//                } else {
//                    content = HttpUtil.getHttp1(params, object.optString("url"), getActivity(), null);
//                }
//
//                params.put("pcontent", content);
//                String url = Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct";
//                str = HttpUtil.getHttp(params, url, getActivity());
//            }
//            Message mes = handler.obtainMessage();
//            mes.obj = str;
//            mes.arg1 = requestnum;
//            mes.what = 0;
//            handler.sendMessage(mes);
//        }
//        if (requestnum + 1 >= gridlist1.size()) {
//            isrequest = false;
//        }
//        requestnum++;
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode) {
            case 1:
                Intent intent = new Intent(context, WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

}

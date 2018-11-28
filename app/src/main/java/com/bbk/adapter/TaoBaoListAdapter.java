package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.TaobaoCarListBean;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.fragment.CarFrament;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.AdaptionSizeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rtj on 2018/3/7.
 */

public class TaoBaoListAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<TaobaoCarListBean> taobaoCarListBeans;
    private ClipboardManager clipboardManager;
    private int drawS;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    private UpdataDialog updataDialog;
    private boolean cancleJump = true;
    private Handler handler = new Handler();
    KelperTask mKelperTask;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferencesJd;
    private SharedPreferences.Editor editorJd;
    private int carNum;
    private String domain;
    private   int num;
    private String name;
    private String list;

    public TaoBaoListAdapter(Context context, List<TaobaoCarListBean> taobaoCarListBeansn,int carNum,String domain,String name,String list) {
//        this.list = list;
        this.context = context;
        this.taobaoCarListBeans = taobaoCarListBeansn;
        this.carNum = carNum;
        this.domain =domain;
        this.name = name;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.car_item_layout, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (taobaoCarListBeans != null && taobaoCarListBeans.size() > 0) {
            return taobaoCarListBeans.size();
        } else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView mbidprice, mprice, youhui;
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
        @BindView(R.id.ll_quan)
        LinearLayout llQuan;
        @BindView(R.id.myouhuitext)
        TextView myouhuitext;
        @BindView(R.id.youhui)
        TextView tvYouhui;
        @BindView(R.id.tv_click_statu)
        TextView tvClickStatu;

        public ViewHolder(View mView) {
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

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final TaobaoCarListBean taobaoCarListBean = taobaoCarListBeans.get(position);
            String img = taobaoCarListBean.getImgurl();
            final String title = taobaoCarListBean.getTitle();
            String price = taobaoCarListBean.getPrice();
            String spec = taobaoCarListBean.getSpec();
//            String youhui = ntaobaoCarListBean.getYouhui();
//            String mbidprice = taobaoCarListBean.getHislowprice();//最低价
            viewHolder.myouhuitext.setVisibility(View.GONE);
//            viewHolder.myouhuitext.setText("券后价");
            viewHolder.bprice.setVisibility(View.GONE);
            viewHolder.tvSale.setVisibility(View.VISIBLE);
            if (taobaoCarListBean.getNum() != null) {
                viewHolder.tvSale.setText("x" + taobaoCarListBean.getNum());
            } else {
                viewHolder.tvSale.setText("x1");
            }

            /**
             * 京东超市显示与隐藏
             */
            if (taobaoCarListBean.getIsJDMarket() != null){
                if (taobaoCarListBean.getIsJDMarket().equals("1")) {
                    viewHolder.item_title.setText("                 " + title);
                    viewHolder.tvMall.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.item_title.setText(title);
                    viewHolder.tvMall.setVisibility(View.GONE);
                }
            }else {
                viewHolder.item_title.setText(title);
                viewHolder.tvMall.setVisibility(View.GONE);
            }


            viewHolder.price.setText(price);
            if (taobaoCarListBean.getQuan1() != null && !taobaoCarListBean.getQuan1().equals("") && !taobaoCarListBean.getQuan1().equals("0")) {
                viewHolder.llQuan.setVisibility(View.VISIBLE);
                viewHolder.quan.setText(taobaoCarListBean.getQuan1());
            } else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (taobaoCarListBean.getZuan() != null) {
                viewHolder.zuan.setVisibility(View.VISIBLE);
                viewHolder.zuan.setText(taobaoCarListBean.getZuan());
            } else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            if (taobaoCarListBean.getZuan() == null
                    && taobaoCarListBean.getQuan1() == null) {
                viewHolder.tvClickStatu.setVisibility(View.GONE);
                viewHolder.tvYouhui.setVisibility(View.VISIBLE);
                viewHolder.tvYouhui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SearchMainActivity.class);
                        intent.putExtra("keyword", taobaoCarListBean.getTitle());
                        SharedPreferencesUtil.putSharedData(context, "shaixuan", "shaixuan", "yes");
                        NewConstants.clickpositionFenlei = 5200;
                        NewConstants.clickpositionDianpu = 5200;
                        NewConstants.clickpositionMall = 5200;
                        context.startActivity(intent);
                    }
                });
            } else {
                viewHolder.tvYouhui.setVisibility(View.GONE);
                viewHolder.tvClickStatu.setVisibility(View.VISIBLE);
                //获取点击缓存判断item是否点击过
                if (domain.equals("jd")) {
                    sharedPreferences = context.getSharedPreferences("MyActionJd", Context.MODE_PRIVATE);
                    sharedPreferencesJd = context.getSharedPreferences("MyActionJdzy", Context.MODE_PRIVATE);
                    if (sharedPreferences.getString("action", null) != null) {
                        String result = sharedPreferences.getString("action", null);
                        if (result != null && result.length() > 0) {
                            NewConstants.mJdMessageMap = NewConstants.getJsonObject(result);
                            if (name.equals("京东自营")) {
                                if (sharedPreferencesJd.getString("actionjdzy", null) != null) {
                                    String jdzyresult =  sharedPreferencesJd.getString("actionjdzy", null);
                                    if (jdzyresult != null && jdzyresult.length() > 0) {
                                        NewConstants.mJdzycsMessageMap = NewConstants.getJsonObject(jdzyresult);
                                        Logg.json(NewConstants.mJdzycsMessageMap);
                                        if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()) != null) {
                                            viewHolder.tvClickStatu.setText("结算可得佣金");
                                            viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                            viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                            if (NewConstants.mJdzycsMessageMap.size() == 2) {
                                                if (NewConstants.mJdzycsMessageMap.get("1").equals("1") && NewConstants.mJdzycsMessageMap.get("0").equals("0")) {
                                                    num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum - NewConstants.jdcsNum;
                                                    if (CarFrament.tvWeiTz !=null) {
                                                        CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                    if (CarActivity.tvWeiTz != null) {
                                                        CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                }
                                            } else if (NewConstants.mJdzycsMessageMap.size()==1){
                                                if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("1")) {
                                                    num = carNum - NewConstants.mJdMessageMap.size() -NewConstants.jdcsNum;
                                                    if (CarFrament.tvWeiTz !=null) {
                                                        CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                    if (CarActivity.tvWeiTz != null) {
                                                        CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                } else if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("0")) {
                                                    num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum;
                                                    if (CarFrament.tvWeiTz !=null) {
                                                        CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                    if (CarActivity.tvWeiTz != null) {
                                                        CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                }
                                            }else if (NewConstants.mJdzycsMessageMap.size()==0){
                                                num = carNum - NewConstants.mJdMessageMap.size();
                                                if (CarFrament.tvWeiTz !=null) {
                                                    CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                                if (CarActivity.tvWeiTz != null) {
                                                    CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                            }
                                        } else {
                                            Logg.json("====>>>京东自营");
                                            viewHolder.tvClickStatu.setText("点击跳转得佣金");
                                            viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle12);
                                            viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.white));
                                        }
                                    }
                                }
                            } else {
                                Logg.json("jd====>>>京东"+NewConstants.mJdMessageMap.get(list));
                                if (NewConstants.mJdMessageMap.get(list) != null) {
                                    if (NewConstants.mJdMessageMap.get(list).equals(list)) {
                                        if (NewConstants.mJdzycsMessageMap.size() == 2) {
                                            if (NewConstants.mJdzycsMessageMap.get("1").equals("1") && NewConstants.mJdzycsMessageMap.get("0").equals("0")) {
                                                num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum - NewConstants.jdcsNum;
                                                if (CarFrament.tvWeiTz !=null) {
                                                    CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                                if (CarActivity.tvWeiTz != null) {
                                                    CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                            }
                                        }else if (NewConstants.mJdzycsMessageMap.size()==1){
                                            if (taobaoCarListBean.getIsJDMarket() != null) {
                                                if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("1")) {
                                                    num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdcsNum;
                                                    if (CarFrament.tvWeiTz !=null) {
                                                        CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                    if (CarActivity.tvWeiTz != null) {
                                                        CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                } else if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("0")) {
                                                    num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum;
                                                    if (CarFrament.tvWeiTz !=null) {
                                                        CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                    if (CarActivity.tvWeiTz != null) {
                                                        CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                    }
                                                }
                                            }
                                        }else if (NewConstants.mJdzycsMessageMap.size()==0){
                                            num = carNum - NewConstants.mJdMessageMap.size();
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                        viewHolder.tvClickStatu.setText("结算可得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    }
                                } else {
                                    Logg.json("jd====>>>京东1"+NewConstants.mJdMessageMap.get(list)+"==="+NewConstants.mJdzycsMessageMap.size());
//                                    viewHolder.tvClickStatu.setText("结算可得佣金");
//                                    viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
//                                    viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    if (NewConstants.mJdzycsMessageMap.size() == 2) {
                                        if (NewConstants.mJdzycsMessageMap.get("1").equals("1") && NewConstants.mJdzycsMessageMap.get("0").equals("0")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum - NewConstants.jdcsNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                        viewHolder.tvClickStatu.setText("结算可得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    }else if (NewConstants.mJdzycsMessageMap.size()==1){
                                        if (taobaoCarListBean.getIsJDMarket() != null) {
                                            if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("1")) {
                                                num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdcsNum;
                                                if (CarFrament.tvWeiTz !=null) {
                                                    CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                                if (CarActivity.tvWeiTz != null) {
                                                    CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                            } else if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("0")) {
                                                num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum;
                                                if (CarFrament.tvWeiTz !=null) {
                                                    CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                                if (CarActivity.tvWeiTz != null) {
                                                    CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                                }
                                            }
                                        }else {
                                            Logg.json("jd====>>>京东2"+NewConstants.mJdMessageMap.get(list));

                                            if (NewConstants.mJdMessageMap.get(list) == null) {
                                                viewHolder.tvClickStatu.setText("点击跳转得佣金");
                                                viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle12);
                                                viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.white));
                                            }else {
                                                viewHolder.tvClickStatu.setText("结算可得佣金");
                                                viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                                viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                            }
                                        }

                                    }else if (NewConstants.mJdzycsMessageMap.size()==0){
                                        viewHolder.tvClickStatu.setText("点击跳转得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle12);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.white));
                                        num = carNum - NewConstants.mJdMessageMap.size();
                                        if (CarFrament.tvWeiTz !=null) {
                                            CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                        if (CarActivity.tvWeiTz != null) {
                                            CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (sharedPreferencesJd.getString("actionjdzy", null) != null) {
                            String jdzyresult =  sharedPreferencesJd.getString("actionjdzy", null);
                            if (jdzyresult != null && jdzyresult.length() > 0) {
                                NewConstants.mJdzycsMessageMap = NewConstants.getJsonObject(jdzyresult);
                                Logg.json(NewConstants.mJdzycsMessageMap);
                                if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()) != null) {
                                    viewHolder.tvClickStatu.setText("结算可得佣金");
                                    viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                    viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    if (NewConstants.mJdzycsMessageMap.size() == 2) {
                                        if (NewConstants.mJdzycsMessageMap.get("1").equals("1") && NewConstants.mJdzycsMessageMap.get("0").equals("0")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum - NewConstants.jdcsNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                    } else if (NewConstants.mJdzycsMessageMap.size()==1){
                                        if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("1")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdcsNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        } else if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("0")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                    }else if (NewConstants.mJdzycsMessageMap.size()==0){
                                        num = carNum - NewConstants.mJdMessageMap.size();
                                        if (CarFrament.tvWeiTz !=null) {
                                            CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                        if (CarActivity.tvWeiTz != null) {
                                            CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                    }
                                } else {
                                    if (NewConstants.mJdzycsMessageMap.size() == 2) {
                                        if (NewConstants.mJdzycsMessageMap.get("1").equals("1") && NewConstants.mJdzycsMessageMap.get("0").equals("0")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum - NewConstants.jdcsNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                        viewHolder.tvClickStatu.setText("结算可得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    } else if (NewConstants.mJdzycsMessageMap.size()==1){
                                        if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("1")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdcsNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        } else if (NewConstants.mJdzycsMessageMap.get(taobaoCarListBean.getIsJDMarket()).equals("0")) {
                                            num = carNum - NewConstants.mJdMessageMap.size() - NewConstants.jdzyNum;
                                            if (CarFrament.tvWeiTz !=null) {
                                                CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                            if (CarActivity.tvWeiTz != null) {
                                                CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                            }
                                        }
                                        viewHolder.tvClickStatu.setText("结算可得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                    }else if (NewConstants.mJdzycsMessageMap.size()==0){
                                        viewHolder.tvClickStatu.setText("点击跳转得佣金");
                                        viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle12);
                                        viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.white));
                                        num = carNum - NewConstants.mJdMessageMap.size();
                                        if (CarFrament.tvWeiTz !=null) {
                                            CarFrament.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                        if (CarActivity.tvWeiTz != null) {
                                            CarActivity.tvWeiTz.setText("未跳转的" + num + "条不可得佣金");
                                        }
                                    }
                                }
                            }
                        }else {
                            if (CarFrament.tvWeiTz !=null) {
                                CarFrament.tvWeiTz.setText("未跳转的" + carNum + "条不可得佣金");
                            }
                            if (CarActivity.tvWeiTz != null) {
                                CarActivity.tvWeiTz.setText("未跳转的" + carNum + "条不可得佣金");
                            }
                        }
                    }
                } else {
                    sharedPreferences = context.getSharedPreferences("MyActionTaobao", Context.MODE_PRIVATE);
                    if (sharedPreferences.getString("action", null) != null) {
                        String result = sharedPreferences.getString("action", null);
                        if (result != null && result.length() > 0) {
                            NewConstants.mMessageMap = NewConstants.getJsonObject(result);
                            Logg.json("TAOBAO===>>>",NewConstants.mMessageMap.size()+"===="+carNum);
                            if (NewConstants.mMessageMap.get(taobaoCarListBean.getRowkey()) != null) {
                                NewConstants.carnum = carNum - NewConstants.mMessageMap.size();
                                if (CarFrament.tvWeiTz !=null) {
                                    CarFrament.tvWeiTz.setText("未跳转的" + NewConstants.carnum + "条不可得佣金");
                                }
                                if (CarActivity.tvWeiTz != null) {
                                    CarActivity.tvWeiTz.setText("未跳转的" +NewConstants.carnum + "条不可得佣金");
                                }
                                if (NewConstants.mMessageMap.get(taobaoCarListBean.getRowkey()).equals(taobaoCarListBean.getRowkey())) {
                                    viewHolder.tvClickStatu.setText("结算可得佣金");
                                    viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle13);
                                    viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.tuiguang_color11));
                                }
                            } else {
                                viewHolder.tvClickStatu.setText("点击跳转得佣金");
                                viewHolder.tvClickStatu.setBackgroundResource(R.drawable.bg_cicyle12);
                                viewHolder.tvClickStatu.setTextColor(context.getResources().getColor(R.color.white));
                            }
                        }
                    }else {
                        if (CarFrament.tvWeiTz !=null) {
                            CarFrament.tvWeiTz.setText("未跳转的" + carNum + "条不可得佣金");
                        }
                        if (CarActivity.tvWeiTz != null) {
                            CarActivity.tvWeiTz.setText("未跳转的" + carNum + "条不可得佣金");
                        }
                    }
                }
            }
//            if (domain.equals("jd")){
//                num = carNum-NewConstants.mJdMessageMap.size();
//                CarFrament.tvWeiTz.setText("未跳转的"+num+"条不可得佣金");
//            }else {
//                num = carNum-NewConstants.mMessageMap.size();
//                CarFrament.tvWeiTz.setText("未跳转的"+num+"条不可得佣金");
//            }
            viewHolder.dianpuText.setText(spec);
            viewHolder.mprice.setText("¥" + price);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);



            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        NewConstants.refeshFlag = "0";
                        taobaoCarListBean.setClick(true);
//                        Logg.json("==============>>"+taobaoCarListBean.getRowkey(),name);
                        //保存item点击数据
                        if (domain.equals("jd")) {
                            if (name.equals("京东自营")){
//                                Logg.e("==============>>"+name,name);
                                sharedPreferencesJd = context.getSharedPreferences("MyActionJdzy", MODE_PRIVATE);
                                editorJd = sharedPreferencesJd.edit();
                                NewConstants.mJdzycsMessageMap.put(taobaoCarListBean.getIsJDMarket(), taobaoCarListBean.getIsJDMarket());
                                editorJd.putString("actionjdzy", com.alibaba.fastjson.JSONObject.toJSON(NewConstants.mJdzycsMessageMap).toString());
                                editorJd.commit();
                            }else {
                                sharedPreferences = context.getSharedPreferences("MyActionJd", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                NewConstants.mJdMessageMap.put(list, list);
                                editor.putString("action", com.alibaba.fastjson.JSONObject.toJSON(NewConstants.mJdMessageMap).toString());
                                editor.commit();
                            }
                        }else {
                            sharedPreferences = context.getSharedPreferences("MyActionTaobao", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            NewConstants.mMessageMap.put(taobaoCarListBean.getRowkey(),taobaoCarListBean.getRowkey());
                            editor.putString("action", com.alibaba.fastjson.JSONObject.toJSON(NewConstants.mMessageMap).toString());
                            editor.commit();
                        }
                        if (taobaoCarListBean.getZuan() == null
                                && taobaoCarListBean.getQuan1() == null) {
                            showLoadingDialog(context, "taobao", taobaoCarListBean.getQuan1(), taobaoCarListBean.getZuan(), taobaoCarListBean.getUrl());
                        }else {
                            showLoadingDialog(context, "taobao", taobaoCarListBean.getQuan1(), taobaoCarListBean.getZuan(), taobaoCarListBean.getJumpurl());
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
            viewHolder.copy_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(taobaoCarListBean.getUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoadingDialog(final Context context, String jumpdomain, String quans, String zuan, final String jumpUrl) {
//        Logg.e(zuan);
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.disanfang_dialog,
                    new int[]{R.id.ll_close});
            updataDialog.show();
            LinearLayout img_close = updataDialog.findViewById(R.id.ll_close);
            ImageView imgLoading = updataDialog.findViewById(R.id.img_loading);
            ImageView imageView = updataDialog.findViewById(R.id.img_app);
            AdaptionSizeTextView adaptionSizeTextViewQuan = updataDialog.findViewById(R.id.quan);
            AdaptionSizeTextView adaptionSizeTextViewQuan1 = updataDialog.findViewById(R.id.quan1);
            if (jumpUrl.contains("tmall")) {
                jumpdomain = "jumptmall";
            } else if (jumpUrl.contains("taobao")) {
                jumpdomain = "jumptaobao";
            } else {
                jumpdomain = "jumpjd";
            }
            if (quans != null && !quans.equals("") && !quans.equals("0")) {
                adaptionSizeTextViewQuan1.setVisibility(View.VISIBLE);
                adaptionSizeTextViewQuan1.setText("领券减" + quans + "元");
            } else {
                adaptionSizeTextViewQuan1.setVisibility(View.VISIBLE);
            }

            if (zuan != null && !zuan.equals("") && !zuan.equals("0")) {
                adaptionSizeTextViewQuan.setVisibility(View.VISIBLE);
                adaptionSizeTextViewQuan.setText("本商品" + zuan.replace("预估", "") + "元");
            } else {
                adaptionSizeTextViewQuan.setVisibility(View.VISIBLE);
            }
            int drawS = context.getResources().getIdentifier(jumpdomain, "mipmap", context.getPackageName());
            imageView.setImageResource(drawS);
            Glide.with(context).load(R.drawable.tuiguang_d05).into(imgLoading);
            if (jumpUrl != null) {
                if (jumpUrl.contains("tmall") || jumpUrl.contains("taobao")) {
                    showUrl(jumpUrl);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (cancleJump) {
                                updataDialog.dismiss();
                                try {
                                    KeplerApiManager.getWebViewService().openJDUrlPage(jumpUrl, mKeplerAttachParameter, context, mOpenAppAction, 1500);
                                    notifyDataSetChanged();
                                } catch (KeplerBufferOverflowException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 2000);
                }
            }
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    cancleJump = false;
                }
            });
        }
    }

    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status) {
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
            } else {
                mKelperTask = null;
            }
        }
    };

    /**
     * 打开指定链接
     */
    public void showUrl(String url) {
        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        alibcShowParams.setClientType("taobao_scheme");
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        final String text = url;
        if (TextUtils.isEmpty(text)) {
            StringUtil.showToast(context, "URL为空");
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cancleJump) {
                    updataDialog.dismiss();
                    AlibcTrade.show((Activity) context, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
                    notifyDataSetChanged();
                }
            }
        }, 2000);
    }
}

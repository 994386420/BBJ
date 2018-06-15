package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.Bean.SearchResultBean;
import com.bbk.activity.BidActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.component.HomeAllComponent5;
import com.bbk.component.SearchComponent;
import com.bbk.dialog.ResultDialog;
import com.bbk.dialog.WebViewAlertDialog;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultMyGridAdapter extends RecyclerView.Adapter implements PopupWindow.OnDismissListener {
    private Activity context;
    private List<SearchResultBean> searchResultBeans;
    private PopupWindow popupWindow;
    private int showTimes = 0;

    public ResultMyGridAdapter(List<SearchResultBean> searchResultBeans, Activity context) {
        this.searchResultBeans = searchResultBeans;
        this.context = context;
    }

    public void notifyData(List<SearchResultBean> beans) {
        if (beans != null && beans.size() > 0) {
            this.searchResultBeans.addAll(beans);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.listview_item_result4, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            init(viewHolder, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemCount() {
        return searchResultBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, item_offer, mbigprice, mlittleprice, juan;
        LinearLayout domainLayout;
        RelativeLayout intentbuy, mmoredomain;
        RelativeLayout itemlayout;
        @BindView(R.id.quan)
        TextView quan;
        @BindView(R.id.zuan)
        TextView zuan;
        @BindView(R.id.ll_quan)
        LinearLayout llQuan;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
            img = (ImageView) mView.findViewById(R.id.item_img);
            title = (TextView) mView.findViewById(R.id.item_title);
            item_offer = (TextView) mView.findViewById(R.id.item_offer);
            mlittleprice = (TextView) mView.findViewById(R.id.mlittleprice);
            mbigprice = (TextView) mView.findViewById(R.id.mbigprice);
            juan = (TextView) mView.findViewById(R.id.juan);
            domainLayout = (LinearLayout) mView.findViewById(R.id.domain_layout);
            mmoredomain = (RelativeLayout) mView.findViewById(R.id.mmoredomain);
            intentbuy = (RelativeLayout) mView.findViewById(R.id.intentbuy);
            itemlayout = mView.findViewById(R.id.result_item);
        }
    }

    private void init(final ViewHolder vh, final int position) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            LayoutParams params = vh.img.getLayoutParams();
            params.height = width / 2;
            vh.img.setLayoutParams(params);
            final SearchResultBean dataSet = searchResultBeans.get(position);
            String title = dataSet.getTitle();
            String img = dataSet.getImgUrl();
            String price = dataSet.getPrice();
            String hnumber = dataSet.getComnum();
            vh.title.setText(title);
            String bigprice;
            String littleprice;
            /**
             * 引导图层
             */
            if (position == 0 && showTimes == 0) {
                final View finalView = vh.intentbuy;
                vh.intentbuy.post(new Runnable() {
                    @Override
                    public void run() {
                        String isFirstResultUse = SharedPreferencesUtil.getSharedData(context, "isFirstUse", "isFirstResultUse");
                        if (TextUtils.isEmpty(isFirstResultUse)) {
                            isFirstResultUse = "yes";
                        }
                        if (isFirstResultUse.equals("yes")) {
                            SharedPreferencesUtil.putSharedData(context, "isFirstUse", "isFirstResultUse", "no");
                            showGuideView(finalView);
                        }
                    }
                });
            }
            if (dataSet.getYongjin() != null && !dataSet.getYongjin().equals("")) {
                vh.zuan.setVisibility(View.VISIBLE);
                vh.zuan.setText(dataSet.getYongjin());
            } else {
                vh.zuan.setVisibility(View.GONE);
            }
            if (dataSet.getQuan() != null && !dataSet.getQuan().equals("")) {
                vh.llQuan.setVisibility(View.VISIBLE);
                vh.quan.setText(dataSet.getQuan());
            } else {
                vh.llQuan.setVisibility(View.GONE);
            }
            if (price.contains(".")) {
                int end = price.indexOf(".");
                bigprice = price.substring(0, end);
                littleprice = price.substring(end, price.length());
            } else {
                bigprice = price;
                littleprice = ".0";
            }
            vh.mbigprice.setText(bigprice);
            vh.mlittleprice.setText(littleprice);
            if (dataSet.getTarr() != null) {
                JSONArray array = new JSONArray(dataSet.getTarr());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String price1 = object.optString("price");
                    if (price1.contains(".")) {
                        int end = price1.indexOf(".");
                        bigprice = price1.substring(0, end);
                        littleprice = price1.substring(end, price1.length());
                    } else {
                        bigprice = price1;
                        littleprice = ".0";
                    }
                    vh.mbigprice.setText(bigprice);
                    vh.mlittleprice.setText(littleprice);
                }
            }
            if (Integer.valueOf(hnumber) > 10000) {
                if (Integer.valueOf(hnumber) > 100000000) {
                    DecimalFormat df = new DecimalFormat("###.0");
                    String num = df.format(Double.valueOf(hnumber) / 100000000);
                    vh.item_offer.setText("全网总评" + num + "亿条  ");
                } else {
                    DecimalFormat df = new DecimalFormat("###.0");
                    String num = df.format(Double.valueOf(hnumber) / 10000);
                    vh.item_offer.setText("全网总评" + num + "万条  ");
                }
            } else {
                vh.item_offer.setText("全网总评" + hnumber + "条  ");
            }
//				if ("1".equals(dataSet.get("isxianshi"))) {
            final String groupRowKey = dataSet.getGroupRowkey();
            if (dataSet.getHidebutton() != null){
                switch (dataSet.getHidebutton()){
                    case "1":
                        vh.intentbuy.setVisibility(View.VISIBLE);
                        vh.mmoredomain.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        vh.intentbuy.setVisibility(View.VISIBLE);
                        vh.mmoredomain.setVisibility(View.GONE);
                        break;
                    case "3":
                        vh.intentbuy.setVisibility(View.GONE);
                        vh.mmoredomain.setVisibility(View.GONE);
                        break;
                    case "4":
                        vh.intentbuy.setVisibility(View.GONE);
                        vh.mmoredomain.setVisibility(View.VISIBLE);
                        break;
                }
            }
            vh.mmoredomain.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
//                    Intent intent = new Intent(context, ResultDialogActivity.class);
////						intent.putExtra("tarr",dataSet.get("tarr").toString() );
//                    intent.putExtra("keyword", dataSet.getKeyword());
//                    intent.putExtra("rowkey", groupRowKey);
//                    context.startActivity(intent);
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    queryCompareByUr(dataSet.getTitle(),dataSet.getUrl(),userID,dataSet.getGroupRowkey());
                }
            });
            vh.intentbuy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
//						Intent intent = new Intent(context, CompareActivity.class);
//						intent.putExtra("rowkey",groupRowKey );
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    Intent intent;
                    if (TextUtils.isEmpty(userID)) {
                        intent = new Intent(context, UserLoginNewActivity.class);
                        context.startActivityForResult(intent, 2);
                    } else {
                        //二级页面去发标
                        intent = new Intent(context, BidActivity.class);
                        intent.putExtra("rowkey", groupRowKey);
                        intent.putExtra("type", "1");
                        intent.putExtra("price",dataSet.getPrice());
                        intent.putExtra("title",dataSet.getTitle());
                        intent.putExtra("imags",dataSet.getDetailImages());
                        context.startActivity(intent);
                    }
                }
            });

            if (dataSet.getYjson() != null) {
                if (dataSet.getSaleinfo() == null) {
                    vh.juan.setVisibility(View.GONE);
                } else {
                    vh.juan.setVisibility(View.VISIBLE);
                    vh.juan.setText("劵");
                }
            } else {
                try {
                    if (dataSet.getYjson() != null) {
                        vh.juan.setVisibility(View.VISIBLE);
                        String yjson = dataSet.getYjson();
                        final JSONObject object = new JSONObject(yjson);
                        vh.juan.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new ResultDialog(context).buildDiloag(object, v, context);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Glide.with(context)
                    .load(img)
                    .placeholder(R.mipmap.zw_img_300)
                    .priority(Priority.HIGH)
                    .into(vh.img);
            vh.itemlayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    Intent intent;
                    if (JumpIntentUtil.isJump4(searchResultBeans, position)) {
                        intent = new Intent(context, IntentActivity.class);
                        if (searchResultBeans.get(position).getAndroidurl() != null) {
                            intent.putExtra("url", searchResultBeans.get(position).getUrl());
                        } else {
                            intent.putExtra("url", searchResultBeans.get(position).getAndroidurl());
                        }
                        if (searchResultBeans.get(position).getTitle() != null) {
                            intent.putExtra("title", searchResultBeans.get(position).getTitle());
                        }
                        if (searchResultBeans.get(position).getDomain() != null) {
                            intent.putExtra("domain", searchResultBeans.get(position).getDomain());
                        }
                        if (searchResultBeans.get(position).getGroupRowkey() != null) {
                            intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
                        }
                        if (searchResultBeans.get(position).getPrice() != null) {
                            intent.putExtra("bprice", searchResultBeans.get(position).getPrice());
                        }
                    } else {
                        intent = new Intent(context, WebViewActivity.class);
                        if (searchResultBeans.get(position).getAndroidurl() != null) {
                            intent.putExtra("url", searchResultBeans.get(position).getUrl());
                        } else {
                            intent.putExtra("url", searchResultBeans.get(position).getUrl());
                        }
                        if (searchResultBeans.get(position).getGroupRowkey() != null) {
                            intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
                        }
                    }
                    context.startActivity(intent);
                }
            });
            vh.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    openPopupWindowCars(view, position);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 引导图层
     *
     * @param targetView
     */
    public void showGuideView(View targetView) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(40)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {

            }
        });

        builder.addComponent(new SearchComponent()).addComponent(new HomeAllComponent5());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    /**
     * 复制弹窗
     */
    private void openPopupWindowCars(View v, int i) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(context).inflate(R.layout.copy_layout, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.AlertDialogStyle);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupCarsViewClick(view, i);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupCarsViewClick(View view, final int i) {
        TextView copy_title, copy_url;
        copy_title = view.findViewById(R.id.copy_title);
        copy_url = view.findViewById(R.id.copy_url);
        copy_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(searchResultBeans.get(i).getTitle());
                StringUtil.showToast(context, "复制成功");
                popupWindow.dismiss();
            }
        });
        copy_url.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(searchResultBeans.get(i).getUrl());
                StringUtil.showToast(context, "复制成功");
                popupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private void queryCompareByUr(String webtitle,String weburl,String userID,String hrowkey) {
        Map<String, String> params = new HashMap<>();
        params.put("title", webtitle);
        params.put("url", weburl);
        params.put("userid", userID);
        params.put("rowkey", hrowkey);
        RetrofitClient.getInstance(context).createBaseApi().queryCompareByUrl(
                params, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                new WebViewAlertDialog(context).builder(content, "0",true,"webview").show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog(){
                        DialogSingleUtil.show(context);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

}

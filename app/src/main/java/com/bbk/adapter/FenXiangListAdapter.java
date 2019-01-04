package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.FenXiangItemBean;
import com.bbk.Bean.FenXiangListBean;
import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.component.FenXiangComponent;
import com.bbk.component.HomeAllComponent;
import com.bbk.component.HomeAllComponent1;
import com.bbk.component.HomeAllComponent6;
import com.bbk.component.SimpleComponent;
import com.bbk.dialog.AlertDialog;
import com.bbk.shopcar.adapter.ShopcatAdapter;
import com.bbk.update.AppVersion;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.NoFastClickUtils;
import com.bbk.util.ShareFenXiangUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyGridView;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.kepler.jd.login.KeplerApiManager;
import com.logg.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FenXiangListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private List<FenXiangListBean> fenXiangListBeans;
    private OnItemClickListener mOnItemClickListener = null;
    private ShareFenXiangUtil shareFenXiangUtil;
    private UpdataDialog updataDialog;
    private String isFirstClick;
    public static boolean cancelCheck = true;// 是否取消查询
    private LogInterface logInterface;

    public FenXiangListAdapter(Context context, List<FenXiangListBean> fenXiangListBeans) {
        this.context = context;
        this.fenXiangListBeans = fenXiangListBeans;
    }


    public LogInterface getLogInterface() {
        return logInterface;
    }

    public void setLogInterface(LogInterface logInterface) {
        this.logInterface = logInterface;
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

    public void notifyData(List<FenXiangListBean> fenXiangListBeans) {
        if (fenXiangListBeans != null && fenXiangListBeans.size() > 0) {
            this.fenXiangListBeans.addAll(fenXiangListBeans);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fenxiang_list_layout, parent, false);
        ViewHolder ViewHolder = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final int position22 = position;
            final FenXiangListBean fenXiangListBean = fenXiangListBeans.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            //将position保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);
            if (fenXiangListBean.getNickname() != null) {
                viewHolder.mname.setText(fenXiangListBean.getNickname());
            }
            if (fenXiangListBean.getTime() != null) {
                viewHolder.mtime.setText(fenXiangListBean.getTime());
            }
            if (fenXiangListBean.getTitle() != null) {
                viewHolder.mcontent.setText(fenXiangListBean.getTitle());
            }
            if (fenXiangListBean.getHeadurl() != null) {
                CircleImageView1.getImg1(context, fenXiangListBean.getHeadurl(), viewHolder.mimg);
            }
            if (fenXiangListBean.getItems() != null) {
                final List<FenXiangItemBean> fenXiangItemBeans = JSON.parseArray(fenXiangListBean.getItems(), FenXiangItemBean.class);
                viewHolder.mrecy.setFocusable(false);
                recyGrid(viewHolder, fenXiangItemBeans);
                viewHolder.llShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)) {
//                            Intent intent = new Intent(context, UserLoginNewActivity.class);
//                            context.startActivity(intent);
                            logInterface.IntentLog(v, fenXiangListBean.getRowkeys(), fenXiangListBean.getTitle());
                        } else {
                            if (NoFastClickUtils.isFastClick()){
                                StringUtil.showToast(context,"对不起，您的点击太快了，请休息一下");
                            }else {
                                if (fenXiangListBean.getRowkeys() != null){
                                    viewHolder.llShare.setClickable(false);
                                    shareCpsInfos(v, fenXiangListBean.getRowkeys(), fenXiangListBean.getTitle(),viewHolder);
                                }
                            }
                        }
                    }
                });
            }
//            viewHolder.llShare.post(new Runnable() {
//                //                    @Override
//                public void run() {
//                    //引导页只显示一次
//                    String isFirstResultUse = SharedPreferencesUtil.getSharedData(context, "isfenxiang", "isfenxiang");
//                    if (TextUtils.isEmpty(isFirstResultUse)) {
//                        isFirstResultUse = "yes";
//                    }
//                    if (isFirstResultUse.equals("yes") && position == 0) {
//                        showGuideView(viewHolder.llShare);
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (fenXiangListBeans != null && fenXiangListBeans.size() > 0) {
            return fenXiangListBeans.size();
        }else {
            return 0;
        }
    }

    private void recyGrid(final ViewHolder viewHolder, final List<FenXiangItemBean> fenXiangItemBeans) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.mrecy.getLayoutParams();
        //根据图片个数来设置布局
        if (fenXiangItemBeans.size() == 1) {
            viewHolder.mrecy.setNumColumns(1);
            layoutParams.width = width / 2;
        } else if (fenXiangItemBeans.size() == 2) {
            viewHolder.mrecy.setNumColumns(2);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        } else if (fenXiangItemBeans.size() == 4) {
            viewHolder.mrecy.setNumColumns(2);
            layoutParams.width = width * 2 / 3;
        } else {
            viewHolder.mrecy.setNumColumns(3);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }
        viewHolder.mrecy.setLayoutParams(layoutParams);
        FenXiangImageAdapter adapter = new FenXiangImageAdapter(context, fenXiangItemBeans);
        viewHolder.mrecy.setAdapter(adapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mimg)
        ImageView mimg;
        @BindView(R.id.mname)
        TextView mname;
        @BindView(R.id.mtime)
        TextView mtime;
        @BindView(R.id.mcontent)
        TextView mcontent;
        @BindView(R.id.mrecy)
        MyGridView mrecy;
        @BindView(R.id.result_item)
        LinearLayout resultItem;
        @BindView(R.id.ll_share)
        LinearLayout llShare;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface LogInterface {
        void IntentLog(View v,String rowkeys,String title);
    }

    /**
     * 分享多张图片到朋友圈
     * @param v
     * @param rowkeys
     * @param title
     */
    private void shareCpsInfos(final View v, String rowkeys, final String title, final ViewHolder viewHolder) {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("rowkeys", rowkeys);
        RetrofitClient.getInstance(context).createBaseApi().shareCpsInfos(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                List<String> DetailimgUrlList = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
//                                Logg.json(jsonObject1);
//                                isFirstClick = SharedPreferencesUtil.getSharedData(
//                                        context, "isFirstClick", "isFirstClick");
//                                if (TextUtils.isEmpty(isFirstClick)) {
//                                    isFirstClick = "yes";
//                                }
//                                if (isFirstClick.equals("yes")) {
//                                    if (jsonObject1.has("errmsg")) {
//                                        if (jsonObject1.optString("errmsg") != null && !jsonObject1.optString("errmsg").equals("")) {
//                                            showMessageDialog(context, userID);
//                                            SharedPreferencesUtil.putSharedData(context, "isFirstClick", "isFirstClick", "no");
//                                        } else {
//                                            Share(v, title, DetailimgUrlList, jsonObject1);
//                                        }
//                                    }
//                                }else {
//                                    Log.i("======",jsonObject1+"============");
//                                    StringUtil.showToast(context,jsonObject1.optString("errmsg"));
//                                    if (jsonObject1.has("wenan")){
//                                        String wenan = jsonObject1.optString("wenan");
//                                        if (wenan != null &&!wenan.equals("")) {
//                                            wenan = jsonObject1.optString("wenan").replace("|", "\n");
//                                        }
//                                        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
//                                        cm.setText(title+"\n"+wenan);
//                                    }
//                                    if (jsonObject1.has("imgs")) {
//                                        JSONArray detailImags = new JSONArray(jsonObject1.optString("imgs"));
//                                        for (int i = 0; i < detailImags.length(); i++) {
//                                            String imgUrl = detailImags.getString(i);
//                                            DetailimgUrlList.add(imgUrl);
//                                        }
//                                        //调用转发微信功能类
//                                        shareFenXiangUtil = new ShareFenXiangUtil((Activity) context, v, title, DetailimgUrlList);
//                                    }
                                if (cancelCheck) {
                                    Share(v, title, DetailimgUrlList, jsonObject1);
                                }
//                                }
                            }else {
                                StringUtil.showToast(context,jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
//                        DialogSingleUtil.dismiss(0);
                        cancelCheck = true;
                        DialogCheckYouhuiUtil.dismiss(0);
                        viewHolder.llShare.setClickable(true);
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(context);
                        DialogCheckYouhuiUtil.show(context,"正在生成您的专属分享图片...");
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        viewHolder.llShare.setClickable(true);
//                        DialogSingleUtil.dismiss(0);
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(context, e.message);
                    }
                });
    }


    /**
     * 成为合伙人
     */
    private static void updateCooperationByUserid(final Context context, String userID) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(context).createBaseApi().updateCooperationByUserid(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            Intent intent;
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(context,"恭喜成为合伙人");
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

    /**
     * 不是合伙人弹窗
     * @param context
     * @param useid
     */
    public void showMessageDialog(final Context context, final String useid) {
        if(updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.hehuo_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_refuse = updataDialog.findViewById(R.id.tv_update_refuse);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tv_update_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    updateCooperationByUserid(context,useid);
                }
            });
        }
    }

    /**
     * 微信分享
     * @param v
     * @param title
     * @param DetailimgUrlList
     * @param jsonObject1
     */
    private void Share( View v,String title,List<String> DetailimgUrlList,JSONObject jsonObject1){
     try {
        if (jsonObject1.has("wenan")){
            String wenan = jsonObject1.optString("wenan");
            if (wenan != null &&!wenan.equals("")) {
                wenan = jsonObject1.optString("wenan").replace("|", "\n");
            }
            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(title+"\n"+wenan);
        }
        if (jsonObject1.has("imgs")) {
            JSONArray detailImags = new JSONArray(jsonObject1.optString("imgs"));
            for (int i = 0; i < detailImags.length(); i++) {
                String imgUrl = detailImags.getString(i);
                DetailimgUrlList.add(imgUrl);
            }
            //调用转发微信功能类
            shareFenXiangUtil = new ShareFenXiangUtil((Activity) context, v, title, DetailimgUrlList);
        }
    } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showGuideView(View targetView) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(60)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                SharedPreferencesUtil.putSharedData(context, "isfenxiang", "isfenxiang", "no");
            }
        });

        builder.addComponent(new FenXiangComponent()).addComponent(new HomeAllComponent6());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }
}

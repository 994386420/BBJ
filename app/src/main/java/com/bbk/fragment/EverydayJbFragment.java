package com.bbk.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bbk.activity.BrokerageActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.CoinWithdrawListAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.model.MainActivity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bbk.fragment.UserFragment.ketiMoney;

public class EverydayJbFragment extends Fragment implements ResultEvent {
    @BindView(R.id.tv_txmoney)
    TextView tvTxmoney;
    Unbinder unbinder;
    private View mView;
    private ViewFlipper mviewflipper;
    private TextView mjbcoin, mwithdrawnum, mwithdraw;
    private ListView mlistview;
    private List<Map<String, String>> list;
    private DataFlow dataFlow;
    private String userID;
    private CoinWithdrawListAdapter adapter;
    private SmartRefreshLayout xrefresh;
    private boolean isoncreat = false;
    private int page = 1;
    private boolean isclear = true;
    private UpdataDialog updataDialog;
    private String txMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_everyday_jb, null);
        dataFlow = new DataFlow(getActivity());
        initView();
        initData();
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private void initView() {
        list = new ArrayList<Map<String, String>>();

        xrefresh = (SmartRefreshLayout) mView.findViewById(R.id.xrefresh);
        mviewflipper = (ViewFlipper) mView.findViewById(R.id.mviewflipper);
        mjbcoin = (TextView) mView.findViewById(R.id.mjbcoin);
        mwithdrawnum = (TextView) mView.findViewById(R.id.mwithdrawnum);
        mwithdraw = (TextView) mView.findViewById(R.id.mwithdraw);
        mlistview = (ListView) mView.findViewById(R.id.mlistview);
        mwithdraw.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Integer.valueOf(mjbcoin.getText().toString()) >= 5000) {
                    initDialog2();
                } else {
                    initDialog1();
                }

            }
        });
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                isclear = true;
                page = 1;
                initData();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });
    }

    private void initDialog1() {
        new AlertDialog(getActivity()).builder().setTitle("提示")
                .setMsg("亲,累计"+txMsg+"哦~")
                .setMsg2("明后天继续来签到就有机会提现")
                .setLeft()
                .setNegativeButton("我知道了", new OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {

                    }
                }).setNegativeButtonColor("#333333")
                .setPositiveButton("领更多鲸币", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MyCoinActivity.class);
                        startActivity(intent);
                    }
                }).setPositiveButtonColor("#ff7d41")
                .show();
    }

    private void initDialog2() {
//		new AlertDialog(getActivity()).builder().setTitle("提现攻略")
//		.setMsg("第1步：搜索并关注“比比鲸大数据”公众号")
//		.setMsg2("第2步：发送“提现”")
//		.setMsg3("第3步：绑定账号，验证身份")
//		.setMsg4("第4步：领取红包")
//		.setLeft()
//		.setPositiveButton("OK,去微信提现", new View.OnClickListener() {
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//
//			}
//		}).setPositiveButtonColor("#ffffff").setPositiveBackgroundColor("#ff7d41").show();
        showMessageDialog(getActivity());
    }

    private void initData() {
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (!TextUtils.isEmpty(userID)) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("userid", userID);
            paramsMap.put("page", page + "");
            dataFlow.requestData(1, "newService/queryJingbiListByUserid", paramsMap, this, false);
        }

    }

    private void initViewflipper() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.jbwithdraw_viewflipper, null);
        CircleImageView userimg = (CircleImageView) view.findViewById(R.id.userimg);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView rmb = (TextView) view.findViewById(R.id.rmb);
        mviewflipper.addView(view);
    }

    private void initlist(JSONArray list2) throws JSONException {
        if (isclear) {
            list.clear();
            isclear = false;
        }
        if (list2.length() < 10) {
            xrefresh.setEnableLoadMore(false);
        } else {
            xrefresh.setEnableLoadMore(true);
        }
        for (int i = 0; i < list2.length(); i++) {
            JSONObject object = list2.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("jinbi", object.optString("jinbi"));
            map.put("message", object.optString("message"));
            map.put("time", object.optString("time"));
            map.put("type", object.optString("type"));
            list.add(map);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CoinWithdrawListAdapter(list, getActivity());
            mlistview.setAdapter(adapter);
        }
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        xrefresh.finishRefresh();
        xrefresh.finishLoadMore();
        switch (requestCode) {
            case 1:
                try {
                    JSONObject object = new JSONObject(content);
                    if (isclear) {
                        int jinbi = object.optInt("jinbi");
                        String money = object.optString("money");
                        mwithdrawnum.setText("当前可提现额度" + money + "元");
                        mjbcoin.setText(jinbi + "");
                        txMsg = object.optString("txmoneymsg");
                        tvTxmoney.setText(txMsg);
                    }
                    JSONArray list = object.getJSONArray("list");
                    initlist(list);
                    mviewflipper.setFocusable(true);
                    mviewflipper.setFocusableInTouchMode(true);
                    mviewflipper.requestFocus();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isoncreat) {
            initData();
        } else {
            isoncreat = true;
        }
    }

    public void showMessageDialog(final Context context) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.jinbitixian_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            TextView tv_tixian = updataDialog.findViewById(R.id.tv_tixian);
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID, false);
                    if (api.isWXAppInstalled()) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        startActivity(intent);
                    } else {
                        StringUtil.showToast(getActivity(), "微信未安装");
                    }
                    updataDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

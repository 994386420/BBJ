package com.bbk.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewFxBean;
import com.bbk.adapter.FindListAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新版发现页面
 */
public class NewRankActivty extends BaseActivity implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
//    private View rank_head, mView;
    private ListView mlistView;
    private int x = 1;
    private FindListAdapter listadapter;
    private SmartRefreshLayout xrefresh;
    private int topicpage = 1;
    private List<NewFxBean> fxBeans;
    private CommonLoadingView zLoadingView;//加载框

//
//	@Override
//	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rank);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
//		initstateView();
        initView();
        initData();
        titleText.setText("数据");
    }

//	@Override
//	@Nullable
//	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//			@Nullable Bundle savedInstanceState) {
//		if (null == mView) {
//			mView = inflater.inflate(R.layout.fragment_rank, null);
//			rank_head = mView.findViewById(R.id.rank_head);
//			ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getActivity().getWindow(),true);
//			ImmersedStatusbarUtils.MIUISetStatusBarLightMode(getActivity(),true);
//			initstateView();
//			initView();
//		}

    //		return mView;
//	}
    private void initData() {
        xrefresh.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("page", topicpage + "");
        maps.put("type", "比比鲸原创");
        RetrofitClient.getInstance(NewRankActivty.this).createBaseApi().queryArticleByType(
                maps, new BaseObserver<String>(NewRankActivty.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                fxBeans = JSON.parseArray(jsonObject.optString("content"), NewFxBean.class);
                                if (x == 1) {
                                    listadapter = new FindListAdapter(fxBeans, NewRankActivty.this);
                                    mlistView.setAdapter(listadapter);
                                } else if (x == 2) {
                                    if (fxBeans != null && fxBeans.size() > 0) {
                                        listadapter.notifyData(fxBeans);
                                    } else {
                                        xrefresh.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.loadSuccess();
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        mlistView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(NewRankActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mlistView.setVisibility(View.GONE);
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        StringUtil.showToast(NewRankActivty.this, e.message);
                    }
                });
    }

    private void initView() {
        zLoadingView =findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        xrefresh = findViewById(R.id.xrefresh);
        refreshAndloda();
        mlistView = findViewById(R.id.mlistview);
    }

    private void refreshAndloda() {
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                topicpage = 1;
                x = 1;
                initData();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                topicpage++;
                x = 2;
                initData();
            }
        });
    }


    // 状态栏高度
    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    // 沉浸式状态栏
    private void initstateView() {
//        if (Build.VERSION.SDK_INT >= 19) {
//            rank_head.setVisibility(View.VISIBLE);
//        }
//        int result = getStatusBarHeight();
//        LayoutParams layoutParams = rank_head.getLayoutParams();
//        layoutParams.height = result;
//        rank_head.setLayoutParams(layoutParams);
    }


//	@Override
//	protected void loadLazyData() {
//		initData();
//	}


    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }

    @OnClick(R.id.title_back_btn)
    public void onViewClicked() {
        finish();
    }
}

package com.bbk.typeshaixuan.view;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.typeshaixuan.adapter.DianpuAdapter;
import com.bbk.typeshaixuan.adapter.FenLeiAdapter;
import com.bbk.typeshaixuan.adapter.GoodsAttrListAdapter;
import com.bbk.typeshaixuan.adapter.GoodsAttrsAdapter;
import com.bbk.typeshaixuan.vo.SaleAttributeNameVo;
import com.bbk.typeshaixuan.vo.SaleAttributeVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * 筛选商品属性选择的popupwindow
 */
public class FilterPopupWindow extends PopupWindow {
    private View contentView;
    private Activity context;
    private View goodsNoView;

    private GridView serviceGrid,dianpuGrid,fenleiGrid;
    private ListView selectionList;
    private TextView filterReset;
    private TextView filterSure;
    private GoodsAttrListAdapter adapter;
    private GoodsAttrsAdapter serviceAdapter;
    private DianpuAdapter dianpuAdapter;
    private FenLeiAdapter fenLeiAdapter;
    private List<SaleAttributeNameVo> itemData;
    private List<SaleAttributeVo> serviceList,dianpulist,fenleilist;
    private String[] serviceStr,dianpuStr,fenleiStr;
    private String string,domain = "",dianpu = "",fenlei = "";
    private EditText zuidiText,zuigaoText;
    public static String ACTION_NAME = "FilterPopupWindow";
    private TextView tv_dianpu,tv_fenlei,tv_shancgheng;
    public TextView name,fenleiname,dianpuname;
    public ImageView img,feileiimg,dianpuimg;
    private RelativeLayout ll_domain,ll_fenlei,ll_dianpu;
    private boolean isGlobalMenuShow = true,isDomainShow = true,isDianPuShow = true;

    /**
     * 商品属性选择的popupwindow
     */
    public FilterPopupWindow(final Activity context,String[] serviceStr,String[] dianpuStr,String[] fenleiStr) {
        this.context = context;
        this.serviceStr = serviceStr;
        this.dianpuStr = dianpuStr;
        this.fenleiStr = fenleiStr;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_goods_details, null);
        goodsNoView = contentView.findViewById(R.id.popup_goods_noview);
        serviceGrid = (GridView) contentView.findViewById(R.id.yuguo_service);
        dianpuGrid = (GridView) contentView.findViewById(R.id.dianpu_grid);
        fenleiGrid = (GridView) contentView.findViewById(R.id.fenlei_grid);
        selectionList = (ListView) contentView.findViewById(R.id.selection_list);
        filterReset = (TextView) contentView.findViewById(R.id.filter_reset);
        filterSure = (TextView) contentView.findViewById(R.id.filter_sure);
        zuidiText = contentView.findViewById(R.id.zuidi_price);
        zuigaoText = contentView.findViewById(R.id.zuigao_price);
        tv_dianpu = contentView.findViewById(R.id.tv_dianpu);
        tv_shancgheng = contentView.findViewById(R.id.tv_shangcheng);
        tv_fenlei = contentView.findViewById(R.id.tv_fenlei);
        img = contentView.findViewById(R.id.attr_list_img);
        name = contentView.findViewById(R.id.attr_list_name);
        feileiimg= contentView.findViewById(R.id.attr_fenlei_img);
        fenleiname = contentView.findViewById(R.id.attr_fenlei_name);
        dianpuimg= contentView.findViewById(R.id.attr_dianpu_img);
        dianpuname = contentView.findViewById(R.id.attr_dianpu_name);
        ll_domain = contentView.findViewById(R.id.ll_img_domain);
        ll_fenlei = contentView.findViewById(R.id.ll_img_fenlei);
        ll_dianpu = contentView.findViewById(R.id.ll_img_dianpu);
        goodsNoView.setOnClickListener(new CancelOnClickListener());
        contentView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });
        serviceList = new ArrayList<SaleAttributeVo>();
        if (serviceStr != null && serviceStr.length>0) {
            for (int i = 0; i < serviceStr.length; i++) {
                SaleAttributeVo vo = new SaleAttributeVo();
                if (serviceStr[i].equals("jd")) {
                    vo.setValue("京东");
                }
                if (serviceStr[i].equals("tmall")) {
                    vo.setValue("天猫");
                }
                if (serviceStr[i].equals("taobao")) {
                    vo.setValue("淘宝");
                }
                serviceList.add(vo);
            }
            if (NewConstants.clickpositionMall == 5200){
            }else {
                serviceList.get(NewConstants.clickpositionMall).setChecked(true);
                domain = serviceList.get(NewConstants.clickpositionMall).getValue();
                name.setText(domain);
            }
            serviceAdapter = new GoodsAttrsAdapter(context);
            serviceGrid.setAdapter(serviceAdapter);
            serviceAdapter.notifyDataSetChanged(false, serviceList);
            ll_domain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDomainShow = !isDomainShow;
                    if (isDomainShow) {
                        img.setImageResource(R.drawable.sort_common_down);
                        serviceAdapter.notifyDataSetChanged(false, serviceList);
                    }else {
                        img.setImageResource(R.drawable.sort_common_up);
                        serviceAdapter.notifyDataSetChanged(true, serviceList);
                    }
                }
            });
            serviceGrid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //设置当前选中的位置的状态为非。
                    serviceList.get(arg2).setChecked(!serviceList.get(arg2).isChecked());
                    NewConstants.clickpositionMall = arg2;
                    if (serviceList.get(arg2).isChecked()) {
                        domain = serviceList.get(arg2).getValue();
                    }else {
                        domain = "";
                    }
                    name.setText(domain);
                    for (int i = 0; i < serviceList.size(); i++) {
                        //跳过已设置的选中的位置的状态
                        if (i == arg2) {
                            continue;
                        }
                        serviceList.get(i).setChecked(false);
                    }
                    isDomainShow = !isDomainShow;
                    img.setImageResource(R.drawable.sort_common_up);
                    serviceAdapter.notifyDataSetChanged(true, serviceList);
                }
            });
        }else {
            tv_shancgheng.setVisibility(View.GONE);
            serviceGrid.setVisibility(View.GONE);
            ll_domain.setVisibility(View.GONE);
        }

        itemData = new ArrayList<SaleAttributeNameVo>();
        adapter = new GoodsAttrListAdapter(context, itemData);
        selectionList.setAdapter(adapter);


        /**
         * 店铺
         */
        if (dianpuStr != null && dianpuStr.length>0) {
        dianpulist = new ArrayList<SaleAttributeVo>();
        for (int i = 0; i < dianpuStr.length; i++) {
            SaleAttributeVo vo = new SaleAttributeVo();
            vo.setValue(dianpuStr[i]);
            dianpulist.add(vo);
        }
            if (NewConstants.clickpositionDianpu == 5200){
            }else {
                dianpulist.get(NewConstants.clickpositionDianpu).setChecked(true);
                dianpu = dianpulist.get(NewConstants.clickpositionDianpu).getValue();
                dianpuname.setText(dianpu);
            }
            ll_dianpu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDianPuShow = !isDianPuShow;
                    if (isDianPuShow) {
                        dianpuimg.setImageResource(R.drawable.sort_common_down);
                        dianpuAdapter.notifyDataSetChanged(false, dianpulist);
                    }else {
                        dianpuimg.setImageResource(R.drawable.sort_common_up);
                        dianpuAdapter.notifyDataSetChanged(true, dianpulist);
                    }
                }
            });
        dianpuAdapter = new DianpuAdapter(context);
        dianpuGrid.setAdapter(dianpuAdapter);
        dianpuAdapter.notifyDataSetChanged(true, dianpulist);
        dianpuGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //设置当前选中的位置的状态为非。
                NewConstants.clickpositionDianpu = arg2;
                dianpulist.get(arg2).setChecked(!dianpulist.get(arg2).isChecked());
                if (dianpulist.get(arg2).isChecked()) {
                    dianpu = dianpulist.get(arg2).getValue();
                }else {
                    dianpu = "";
                }
                for (int i = 0; i < dianpulist.size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == arg2) {
                        continue;
                    }
                    dianpulist.get(i).setChecked(false);
                }
                isDianPuShow = !isDianPuShow;
                img.setImageResource(R.drawable.sort_common_up);
                dianpuAdapter.notifyDataSetChanged(true, dianpulist);
            }
        });
        }else {
            tv_dianpu.setVisibility(View.GONE);
            dianpuGrid.setVisibility(View.GONE);
            ll_dianpu.setVisibility(View.GONE);
        }


        /**
         * 分类
         */
        if (fenleiStr != null && fenleiStr.length>0) {
            fenleilist = new ArrayList<SaleAttributeVo>();
            for (int i = 0; i < fenleiStr.length; i++) {
                SaleAttributeVo vo = new SaleAttributeVo();
                vo.setValue(fenleiStr[i]);
                fenleilist.add(vo);
            }
            if (NewConstants.clickpositionFenlei == 5200) {

            }else {
                fenleilist.get(NewConstants.clickpositionFenlei).setChecked(true);
                fenlei = fenleilist.get(NewConstants.clickpositionFenlei).getValue();
                fenleiname.setText(fenlei);
            }
            fenLeiAdapter = new FenLeiAdapter(context);
            fenleiGrid.setAdapter(fenLeiAdapter);
            fenLeiAdapter.notifyDataSetChanged(false, fenleilist);
            ll_fenlei.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isGlobalMenuShow = !isGlobalMenuShow;
                    if (isGlobalMenuShow) {
                        feileiimg.setImageResource(R.drawable.sort_common_down);
                        fenLeiAdapter.notifyDataSetChanged(false, fenleilist);
                    }else {
                        feileiimg.setImageResource(R.drawable.sort_common_up);
                        fenLeiAdapter.notifyDataSetChanged(true, fenleilist);
                    }
                }
            });
            fenleiGrid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //设置当前选中的位置的状态为非。
                    NewConstants.clickpositionFenlei = arg2;
                    fenleilist.get(arg2).setChecked(!fenleilist.get(arg2).isChecked());
                    if (fenleilist.get(arg2).isChecked()) {
                        fenlei = fenleilist.get(arg2).getValue();
                    }else {
                        fenlei = "";
                    }
                    fenleiname.setText(fenlei);
                    for (int i = 0; i < fenleilist.size(); i++) {
                        //跳过已设置的选中的位置的状态
                        if (i == arg2) {
                            continue;
                        }
                        fenleilist.get(i).setChecked(false);
                    }
                    isGlobalMenuShow = !isGlobalMenuShow;
                    feileiimg.setImageResource(R.drawable.sort_common_up);
                    fenLeiAdapter.notifyDataSetChanged(true, fenleilist);
                }
            });
        }else {
            tv_fenlei.setVisibility(View.GONE);
            fenleiGrid.setVisibility(View.GONE);
            ll_fenlei.setVisibility(View.GONE);
        }

//        String str = "["
//                + "{\"nameId\":\"V2QASD\",\"saleVo\":["
//                + "{\"value\":\"2核\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"1\"},"
//                + "{\"value\":\"4核\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
//                + "{\"value\":\"6核\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
//                + "{\"value\":\"8核\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"}"
//                + "],\"name\":\"店铺\"},"
//                + "{\"nameId\":\"V2QASD\",\"saleVo\":["
//                + "{\"value\":\"全网通\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
//                + "{\"value\":\"移动4G\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"1\"},"
//                + "{\"value\":\"电信4G\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
//                + "{\"value\":\"联通4G\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"}"
//                + "],\"name\":\"分类\"}"
////                + "{\"nameId\":\"V2QASD\",\"saleVo\":["
////                + "{\"value\":\"OPPO\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"荣耀\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"苹果\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"1\"},"
////                + "{\"value\":\"鸭梨\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"月饼\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"vivo\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"}"
////                + "],\"name\":\"品牌\"},"
////                + "{\"nameId\":\"V2QASD\",\"saleVo\":["
////                + "{\"value\":\"音乐\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"1\"},"
////                + "{\"value\":\"拍照\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"待机长\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"}"
////                + "],\"name\":\"主打\"},"
////                + "{\"nameId\":\"V2QLAH\",\"saleVo\":["
////                + "{\"value\":\"4.5英寸\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"5英寸\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"5.5英寸\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"0\"},"
////                + "{\"value\":\"6英寸\",\"goods\":null,\"goodsAndValId\":\"C6VOWQ\",\"checkStatus\":\"1\"}"
////                + "],\"name\":\"尺寸\"}"
//                + "]";
//        JSONArray json = null;
//        try {
//            json = new JSONArray(string);
//            refreshAttrs(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // 重置的点击监听，将所有选项全设为false
        filterReset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                for (int i = 0; i < itemData.size(); i++) {
//                    for (int j = 0; j < itemData.get(i).getSaleVo().size(); j++) {
//                        itemData.get(i).getSaleVo().get(j).setChecked(false);
//                    }
//                }
//                adapter.notifyDataSetChanged();
                if (fenleilist != null && fenleilist.size()>0) {
                    for (int i = 0; i < fenleilist.size(); i++) {
                        fenleilist.get(i).setChecked(false);
                    }
                }
                if (serviceList != null && serviceList.size()>0) {
                    for (int i = 0; i < serviceList.size(); i++) {
                        serviceList.get(i).setChecked(false);
                    }
                }
                if (dianpulist != null && dianpulist.size()>0) {
                    for (int i = 0; i < dianpulist.size(); i++) {
                        dianpulist.get(i).setChecked(false);
                    }
                }
                if (serviceAdapter != null) {
                    serviceAdapter.notifyDataSetChanged();
                }
                if (dianpuAdapter != null) {
                    dianpuAdapter.notifyDataSetChanged();
                }
                if (fenLeiAdapter != null) {
                    fenLeiAdapter.notifyDataSetChanged();
                }
                dianpu = "";
                fenlei = "";
                domain = "";
                dianpuname.setText("");
                fenleiname.setText("");
                name.setText("");
                NewConstants.clickpositionFenlei = 5200;
                NewConstants.clickpositionDianpu = 5200;
                NewConstants.clickpositionMall = 5200;
            }
        });
        // 确定的点击监听，将所有已选中项列出
        filterSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = "";
//                for (int i = 0; i < itemData.size(); i++) {
//                    for (int j = 0; j < itemData.get(i).getSaleVo().size(); j++) {
//                        if (itemData.get(i).getSaleVo().get(j).isChecked()) {
//                            str = str + itemData.get(i).getSaleVo().get(j).getValue();
//                        }
//                    }
//                }
//                Log.i("==============",fenleilist+"===========");
                if (this != null) {
                    Intent intent = new Intent(ACTION_NAME);
                    intent.putExtra("productType",fenlei);
                    intent.putExtra("dianpu",dianpu);
                    intent.putExtra("bprice",zuidiText.getText().toString());
                    intent.putExtra("eprice",zuigaoText.getText().toString());
                    intent.putExtra("domain",domain);
                    context.sendBroadcast(intent);
                    dismiss();
                }
//                Toast.makeText(FilterPopupWindow.this.context, dianpu+domain+fenlei+zuigaoText.getText().toString()+zuidiText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        this.setContentView(contentView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        darkenBackgroud(0.5f);
        // 设置动画效果
        this.setAnimationStyle(R.style.popwindow_style);
        this.update();
        this.setOnDismissListener(onDismissListener);

    }

    /**
     * 刷新商品属性
     *
     * @param json
     * @throws JSONException
     */
    public void refreshAttrs(JSONArray json) throws JSONException {
        itemData.clear();
        for (int i = 0; i < json.length(); i++) {
            SaleAttributeNameVo saleName = new SaleAttributeNameVo();
            JSONObject obj = (JSONObject) json.opt(i);
            saleName.setName(obj.getString("name"));
            List<SaleAttributeVo> list = new ArrayList<SaleAttributeVo>();
            JSONArray array = new JSONArray();
            array = new JSONArray(obj.getString("saleVo"));
            for (int j = 0; j < array.length(); j++) {
                JSONObject object = array.getJSONObject(j);
                SaleAttributeVo vo = new SaleAttributeVo();
                vo.setGoods(object.getString("goods"));
                vo.setValue(object.getString("value"));
                vo.setGoodsAndValId(object.getString("goodsAndValId"));
                if ("1".equals(object.getString("checkStatus"))) {
                    vo.setChecked(true);
                } else {
                    vo.setChecked(false);
                }
                list.add(vo);
            }
            saleName.setSaleVo(list);
            // 是否展开
            saleName.setNameIsChecked(false);
            itemData.add(saleName);
        }
        adapter.notifyDataSetChanged();
    }

    public class CancelOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public boolean onKeyDown(Activity context, int keyCode, KeyEvent event) {
        this.context = context;
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return true;
    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    private void darkenBackgroud(Float bgcolor) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
    OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            darkenBackgroud(1.0f);
        }
    };
}

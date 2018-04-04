package com.bbk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.adapter.ResultCouponAdapter;
import com.bbk.util.BaseTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/1/24.
 */

public class ResultDialog {
    private Context context;
    private PopupWindow popupWindow;
    private Display display;
    TextView mcoupontitle,mclose;
    ListView mcouponlist;

    public ResultDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public  ResultDialog buildDiloag(JSONObject object, View v, final Activity activity){
        try {

            View dialogView = LayoutInflater.from(context).inflate(
                    R.layout.result_dialog_main, null);

            //  设置可以获取焦点
            mcoupontitle = (TextView) dialogView.findViewById(R.id.mcoupontitle);
            mclose = (TextView) dialogView.findViewById(R.id.mclose);
            mcouponlist = (ListView) dialogView.findViewById(R.id.mcouponlist);
            String store = object.optString("store");
            mcoupontitle.setText(store+" 优惠劵");
            List<Map<String,String>> list = new ArrayList<>();
            JSONArray ylist = object.getJSONArray("ylist");
            for (int i = 0; i < ylist.length(); i++) {
                Map<String,String> map = new HashMap<>();
                JSONObject jsonObject =  ylist.getJSONObject(i);
                map.put("time",jsonObject.optString("time"));
                map.put("desc",jsonObject.optString("desc"));
                map.put("money",jsonObject.optString("money"));
                map.put("url",jsonObject.optString("url"));
                list.add(map);
            }
            mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            ResultCouponAdapter adapter = new ResultCouponAdapter(context,list);
            mcouponlist.setAdapter(adapter);
            popupWindow = new PopupWindow(dialogView,
                    BaseTools.getWindowsWidth((Activity) context), BaseTools.getWindowsHeight((Activity) context)*3/5);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(0x000000);
            popupWindow.setBackgroundDrawable(cd);
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.4f;
            activity.getWindow().setAttributes(lp);
            // 设置背景颜色
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            popupWindow.setAnimationStyle(R.style.AnimationPreview);
            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = activity.getWindow()
                            .getAttributes();
                    lp.alpha = 1f;
                    activity.getWindow().setAttributes(lp);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


}

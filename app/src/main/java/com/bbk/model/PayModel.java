package com.bbk.model;

import android.app.Activity;
import android.util.Log;
import com.bbk.resource.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/22.
 */
public class PayModel {
    private Activity mContext;
    private StringBuffer sb;
    private PayReq req;

    public PayModel(Activity context) {
        this.mContext = context;
        sb = new StringBuffer();
    }
    public interface wxListener {
        void onResult(PayReq req);
    }




//    /**
//     * 支付宝支付
//     */
//    public void aliPay(String orderinfo, final alipayListener listener) {
//
//        // 完整的符合支付宝参数规范的订单信息
////        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();
//        final String payInfo = orderinfo;
//            Log.i("支付宝订单数据",payInfo+"==================");
//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//                try{
//                // 构造PayTask 对象
//                PayTask alipay = new PayTask(mContext);
//                // 调用支付接口，获取支付结果
//                if (payInfo!= null) {
//                    String result = alipay.pay(payInfo);
//                    Message msg = new Message();
//                    msg.what = Constants.SDK_PAY_FLAG;
//                    msg.obj = result;
//                    listener.onResult(msg);
//                }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//
//    }

    public void wxPay(JSONObject object, wxListener listener) {
        req = new PayReq();
        Log.i("微信支付数据", object.optString("prepayid") + "===" + object.optString("nonce_str")+ "===" + object.optString("timestamp"));
        req.appId = Constants.APP_ID;
        req.partnerId = "1312675401";
//        req.prepayId = resultunifiedorder.get("prepay_id");
        if(object.optString("prepayid")!=null){
            req.prepayId = object.optString("prepayid");
        }

        req.packageValue = "Sign=WXPay";
//        req.nonceStr = genNonceStr();
        if(object.optString("nonce_str")!=null) {
            req.nonceStr = object.optString("nonce_str");
        }
//        req.timeStamp = String.valueOf(genTimeStamp());
        if(object.optString("timestamp")!=null) {
            req.timeStamp = object.optString("timestamp");
        }
        if(object.optString("sign")!=null) {
            req.sign = object.optString("sign");
        }
        Log.e("orion", req.toString());
//        req.sign = genAppSign(signParams);
        listener.onResult(req);
    }
}

package cn.kuaishang.kssdk.callback;


import java.util.Map;

public interface OnSendMessageCallback {

    void onSuccess(Map result);
    void onFailure(String message);
}

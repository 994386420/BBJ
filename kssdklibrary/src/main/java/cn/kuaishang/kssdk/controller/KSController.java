package cn.kuaishang.kssdk.controller;

import java.util.List;
import java.util.Map;

import cn.kuaishang.kssdk.callback.OnConversationOpenCallback;
import cn.kuaishang.kssdk.callback.OnLastQueryCallback;
import cn.kuaishang.kssdk.callback.OnSendMessageCallback;
import cn.kuaishang.kssdk.model.BaseMessage;

public interface KSController {

    public void onConversationOpen(OnConversationOpenCallback callback);

    public String getConversationResult();

    public boolean isShield();

    public Integer getCurStatus();

    public Integer getCurCsId();

    public String getCurCsName();

    public void saveDialogRecord(Map map);

    public List<BaseMessage> getDialogRecords(String addTime);

    public void sendMessage(String content, OnSendMessageCallback callback);

    public void sendImage(String filePath, OnSendMessageCallback callback);

    public void sendVoice(String filePath, OnSendMessageCallback callback);

    public void sendPredicte(String content);

    public void sendServiceEvaluate(String value,String content);

    public void sendFeedback(Map map);

    public void queryLastRecords(OnLastQueryCallback callback);

}

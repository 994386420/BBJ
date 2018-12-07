package cn.kuaishang.kssdk.model;

import cn.kuaishang.util.StringUtil;

public class VoiceMessage extends BaseMessage {

    private String voiceUrl;

    public VoiceMessage() {
        setMessageType(TYPE_MESSAGE_VISITOR);
        setContentType(TYPE_CONTENT_VOICE);
        setAddTime(StringUtil.getCurrentDate());
    }

    public VoiceMessage(String content){
        this();
        setContent(content);
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }
}
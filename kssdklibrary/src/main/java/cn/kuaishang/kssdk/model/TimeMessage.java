package cn.kuaishang.kssdk.model;

public class TimeMessage extends BaseMessage {

    public TimeMessage() {
        setMessageType(TYPE_MESSAGE_TIME);
        setContentType(TYPE_CONTENT_TEXT);
    }

    public TimeMessage(String content){
        this();
        setContent(content);
    }

}
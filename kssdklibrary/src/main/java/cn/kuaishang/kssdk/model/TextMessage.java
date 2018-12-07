package cn.kuaishang.kssdk.model;

import cn.kuaishang.util.StringUtil;

public class TextMessage extends BaseMessage {

    public TextMessage() {
        setMessageType(TYPE_MESSAGE_VISITOR);
        setContentType(TYPE_CONTENT_TEXT);
        setAddTime(StringUtil.getCurrentDate());
    }

    public TextMessage(String content){
        this();
        setContent(content);
    }

}
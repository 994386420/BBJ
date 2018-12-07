package cn.kuaishang.kssdk.model;

import cn.kuaishang.util.StringUtil;

public class ImageMessage extends BaseMessage {

    private String imageUrl;

    public ImageMessage() {
        setMessageType(TYPE_MESSAGE_VISITOR);
        setContentType(TYPE_CONTENT_IMAGE);
        setAddTime(StringUtil.getCurrentDate());
    }

    public ImageMessage(String content){
        this();
        setContent(content);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
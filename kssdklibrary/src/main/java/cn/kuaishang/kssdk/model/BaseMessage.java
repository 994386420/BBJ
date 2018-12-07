package cn.kuaishang.kssdk.model;

public class BaseMessage {

    public static final String STATE_SENDING = "sending";
    public static final String STATE_SUCCESS = "success";
    public static final String STATE_FAILED = "failed";

    public static final int TYPE_MESSAGE_VISITOR = 0;
    public static final int TYPE_MESSAGE_CUSTOMER = 1;
    public static final int TYPE_MESSAGE_SYSTEM = 2;
    public static final int TYPE_MESSAGE_TIME = 3;
    public static final int TYPE_MESSAGE_EVALUATE = 4;

    public static final String TYPE_CONTENT_TEXT = "text";
    public static final String TYPE_CONTENT_IMAGE = "image";
    public static final String TYPE_CONTENT_VOICE = "voice";

    public static final int MAX_TYPE = 5;

    private long id;
    private Integer customerId;
    private String senderName;
    private String content;
    private String addTime;

    private int messageType;
    private String contentType;
    private String status;
    private String localId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}

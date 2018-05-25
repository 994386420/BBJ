package com.bbk.client;



public class BaseResponse<T> {

    private int status ;
    private String errmsg;
    private T content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setContent(T content) {
        this.content = content;
    }


    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getContent() {
        return content;
    }

    public boolean isOk() {
        return status == 0;
    }

}

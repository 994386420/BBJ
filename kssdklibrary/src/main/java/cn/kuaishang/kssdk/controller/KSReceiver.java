package cn.kuaishang.kssdk.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.model.BaseMessage;
import cn.kuaishang.model.ModelDialogRecord;
import cn.kuaishang.util.KSConstant;

public abstract class KSReceiver extends BroadcastReceiver {
    private String mConversationId;

    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if(KSConstant.ACTION_RECEIVEMESSAGES.equals(action)){
            List<ModelDialogRecord> list = (List<ModelDialogRecord>) intent.getSerializableExtra("data");
            List<BaseMessage> messages = new ArrayList<>();
            for(ModelDialogRecord record : list){
                messages.add(KSUIUtil.newMessage(context, record));
            }
            receiveMessages(messages);
        }else if(KSConstant.ACTION_INPUT_START.equals(action)){
            receiveInputStart();
        }else if(KSConstant.ACTION_INPUT_STOP.equals(action)){
            receiveInputStop();
        }else if(KSConstant.ACTION_VISITOR_SUBINFO.equals(action)){
            receiveVisitorInfo();
        }
    }

    public abstract void receiveMessages(List<BaseMessage> messages);
    public abstract void receiveInputStart();
    public abstract void receiveInputStop();
    public abstract void receiveVisitorInfo();
}

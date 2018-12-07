package cn.kuaishang.kssdk.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kuaishang.core.KSManager;
import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.KSViewHolder;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.activity.KSConversationActivity;
import cn.kuaishang.kssdk.activity.KSShowPhotoActivity;
import cn.kuaishang.kssdk.model.BaseMessage;
import cn.kuaishang.kssdk.model.ImageMessage;
import cn.kuaishang.kssdk.model.TimeMessage;
import cn.kuaishang.kssdk.model.VoiceMessage;
import cn.kuaishang.kssdk.task.KSDownLoadFileTask;
import cn.kuaishang.kssdk.util.KSAudioPlayer;
import cn.kuaishang.kssdk.util.KSEmotionUtil;
import cn.kuaishang.kssdk.widget.KSDialogEvaluation;
import cn.kuaishang.util.FileUtil;
import cn.kuaishang.util.KSKey;
import cn.kuaishang.util.StringUtil;

import static cn.kuaishang.kssdk.model.BaseMessage.TYPE_MESSAGE_CUSTOMER;
import static cn.kuaishang.kssdk.model.BaseMessage.TYPE_MESSAGE_VISITOR;

public class KSChatAdapter extends BaseAdapter {

	private KSConversationActivity mActivity;
	private List<BaseMessage> mDatas;
    private int curVoiceIndex = -1;
	
    public KSChatAdapter(KSConversationActivity mActivity, List<BaseMessage> datas) {
    	this.mActivity = mActivity;
        this.mDatas = new ArrayList<>();
        addKSMessage(datas);
    }

    public void setKSMessage(List<BaseMessage> messages){
        this.mDatas.clear();
        addKSMessage(messages);
    }

    public void addKSMessage(List<BaseMessage> messages) {
        if(messages==null || messages.size()==0)return;
        for(BaseMessage message : messages){
            checkMessageTime(mDatas, message);
            if(hasSameMessage(message))continue;
            mDatas.add(message);
        }
        notifyDataSetChanged();
    }

    public void addKSMessage(BaseMessage baseMessage) {
        checkMessageTime(mDatas, baseMessage);
    	mDatas.add(baseMessage);
        notifyDataSetChanged();
    }

    public void addKSMessage(BaseMessage baseMessage, int location) {
    	mDatas.add(location, baseMessage);
        notifyDataSetChanged();
    }

    private void checkMessageTime(List<BaseMessage> list, BaseMessage baseMessage){
        if(list.size()==0){
            newMessageTime(list, StringUtil.stringToDateAndTime(baseMessage.getAddTime()));
        }else{
            BaseMessage lastMessage = list.get(list.size()-1);
            Date lastTime = StringUtil.stringToDateAndTime(lastMessage.getAddTime());
            Date curTime = StringUtil.stringToDateAndTime(baseMessage.getAddTime());
            if((curTime.getTime()-lastTime.getTime())>60*1000){
                newMessageTime(list, curTime);
            }
        }
    }

    private boolean hasSameMessage(BaseMessage baseMessage){
        if(mDatas.size()==0){
            return false;
        }else{
            BaseMessage lastMessage = mDatas.get(mDatas.size()-1);
            String lastLocalId = StringUtil.getString(lastMessage.getLocalId());
            String curLocalId = StringUtil.getString(baseMessage.getLocalId());
            if(StringUtil.isNotEmpty(curLocalId)
                && StringUtil.isNotEmpty(lastLocalId)
                && curLocalId.equals(lastLocalId)){
                return true;
            }else{
                return false;
            }
        }
    }

    private void newMessageTime(List<BaseMessage> list, Date time){
        TimeMessage tm = new TimeMessage(StringUtil.getDialogTimeStr(time));
        tm.setAddTime(StringUtil.getDateStr(time, "yyyy-MM-dd HH:mm:ss"));
        list.add(tm);
    }

    public void loadMoreMessage(List<BaseMessage> messages) {
        if(messages==null || messages.size()==0)return;
        List<BaseMessage> list = new ArrayList<>();
        for(BaseMessage message : messages){
            checkMessageTime(list, message);
            list.add(message);
        }
    	mDatas.addAll(0, list);
        notifyDataSetChanged();
    }

    public BaseMessage getFirstMessage(){
        if(mDatas!=null || mDatas.size()>0){
            return mDatas.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getMessageType();
    }

    @Override
    public int getViewTypeCount() {
        return BaseMessage.MAX_TYPE;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final BaseMessage message = mDatas.get(position);
    	int itemViewType = getItemViewType(position);
        String contentType = message.getContentType();
        String content = StringUtil.Html2Text(message.getContent());
    	if (convertView == null) {
            switch (itemViewType) {
                case BaseMessage.TYPE_MESSAGE_CUSTOMER:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.ks_item_chat_left, null);
                    break;
                case BaseMessage.TYPE_MESSAGE_VISITOR:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.ks_item_chat_right, null);
                    break;
                case BaseMessage.TYPE_MESSAGE_TIME:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.ks_item_chat_time, null);
                    break;
                case BaseMessage.TYPE_MESSAGE_SYSTEM:
                case BaseMessage.TYPE_MESSAGE_EVALUATE:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.ks_item_chat_tip, null);
                    break;
            }
        }
        View contentView = KSViewHolder.get(convertView, R.id.contentLayout);
    	TextView textView = KSViewHolder.get(convertView, R.id.textContent);
        final ImageView imageView = KSViewHolder.get(convertView, R.id.imageContent);
        final ImageButton imagePoint = KSViewHolder.get(convertView, R.id.imagePoint);
        if (itemViewType == BaseMessage.TYPE_MESSAGE_TIME) {
        	//显示对话时间
        	textView.setText(content);
        } else if (itemViewType == BaseMessage.TYPE_MESSAGE_SYSTEM) {
        	//显示系统消息
            textView.setText(KSUIUtil.getSystemContent(content));
        } else if(itemViewType == BaseMessage.TYPE_MESSAGE_EVALUATE) {
            //客服推送服务评价
            content = KSUIUtil.getSystemContent(content);
            int startIndex = content.indexOf("，")+1;
            int endIndex = content.length();
            SpannableStringBuilder spannable = new SpannableStringBuilder(content);
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ServiceEvaluateClick(), startIndex, endIndex , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannable);
        } else if (itemViewType == BaseMessage.TYPE_MESSAGE_CUSTOMER || itemViewType == TYPE_MESSAGE_VISITOR) {
            if(itemViewType == BaseMessage.TYPE_MESSAGE_CUSTOMER){
                //显示客服图标
                ImageView imageIcon = KSViewHolder.get(convertView, R.id.imageIcon);
                boolean isWeb = KSManager.getInstance(mActivity).isWeb();
                String iconUrl = "";
                if(isWeb){
                    Integer custId = message.getCustomerId();
                    String filePath = KSManager.getInstance(mActivity).getFilePath();
                    iconUrl = KSManager.getInstance(mActivity).getCustIcon(custId);
                    ImageLoader.getInstance().displayImage(filePath+iconUrl, imageIcon, KSUIUtil.getDisplayImageOptionsByCircular(R.drawable.chaticon_client));
                }else{
                    iconUrl = "http://image.bibijing.cn:443/images/zhanwei/logo.png";
                    ImageLoader.getInstance().displayImage(iconUrl, imageIcon, KSUIUtil.getDisplayImageOptionsByCircular(R.drawable.chaticon_client));
                }

            }
        	//显示消息：文字、图片、语音
            if (BaseMessage.TYPE_CONTENT_TEXT.equals(contentType)) {
                // 文字
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                if(imagePoint!=null)imagePoint.setVisibility(View.GONE);
                if (!"".equals(content)) {
                	textView.setText(KSEmotionUtil.getEmotionText(mActivity, content, 20));
                }
            }else if (BaseMessage.TYPE_CONTENT_IMAGE.equals(contentType)) {
                // 图片
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if(imagePoint!=null)imagePoint.setVisibility(View.GONE);
                ImageMessage im = (ImageMessage) message;
                final String imageUrl = im.getImageUrl();
                File file = new File(imageUrl);
                if(file.exists()){
                    //imageView.setImageBitmap(KSUIUtil.getSmallBitmap(file.getPath(),500,500));
                    ImageLoader.getInstance().displayImage("file://"+file.getPath(), imageView, KSUIUtil.getDisplayImageOptions(R.drawable.placeholder_image));
                }else{
                    String name = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
                    file = new File(FileUtil.getCachePath()+name);
                    if(file.exists()){
                        //imageView.setImageBitmap(KSUIUtil.getSmallBitmap(file.getPath(),500,500));
                        ImageLoader.getInstance().displayImage("file://"+file.getPath(), imageView, KSUIUtil.getDisplayImageOptions(R.drawable.placeholder_image));
                    }else{
                        ImageLoader.getInstance().displayImage(imageUrl+"?size=500x500", imageView, KSUIUtil.getDisplayImageOptions(R.drawable.placeholder_image));
                        new KSDownLoadFileTask(mActivity, imageUrl, FileUtil.getCachePath()).execute();
                    }
                }
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String[]> datas = getPicList();
                        int index = 0;
                        for(String[] obj : datas){
                            String path = StringUtil.getString(obj[0]);
                            if(imageUrl.equals(path))break;
                            index++;
                        }
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put(KSKey.KEY_CONTENT, datas);
                        data.put(KSKey.KEY_CURINDEX, index);
                        KSUIUtil.openActivity(mActivity, data, KSShowPhotoActivity.class);
                    }
                });
            }else if(BaseMessage.TYPE_CONTENT_VOICE.equals(contentType)){
                //语音
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if(imagePoint!=null)imagePoint.setVisibility(View.GONE);
                VoiceMessage vm = (VoiceMessage) message;
                final String voiceUrl = vm.getVoiceUrl();
                final Integer msgType = vm.getMessageType();
                if(msgType==TYPE_MESSAGE_CUSTOMER){
                    //判断是否点击过
                    String name = voiceUrl.substring(voiceUrl.lastIndexOf("/")+1);
                    String localPath = FileUtil.getVideoPath()+name;
                    File localFile = new File(localPath);
                    if(imagePoint!=null){
                        if(!localFile.exists()){
                            imagePoint.setVisibility(View.VISIBLE);
                        }else{
                            imagePoint.setVisibility(View.GONE);
                        }
                    }
                }
                resetVoiceImage(imageView, msgType, position);
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(imagePoint!=null)imagePoint.setVisibility(View.GONE);
                        curVoiceIndex = position;
                        new KSDownLoadFileTask(mActivity, voiceUrl, FileUtil.getVideoPath()).execute();
                        resetVoiceImage(imageView, msgType, position);
                        //notifyDataSetChanged();
                        KSAudioPlayer.playSound(voiceUrl, new KSAudioPlayer.Callback() {
                            @Override
                            public void onError() {
                                curVoiceIndex = -1;
                                KSAudioPlayer.release();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCompletion() {
                                curVoiceIndex = -1;
                                KSAudioPlayer.release();
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }
        return convertView;
    }

    private List getPicList(){
        List<String[]> list = new ArrayList<String[]>();
        for(BaseMessage message : mDatas){
            String contentType = message.getContentType();
            if(BaseMessage.TYPE_CONTENT_IMAGE.equals(contentType)){
                String[] obj = new String[3];
                ImageMessage im = (ImageMessage)message;
                obj[0] = StringUtil.getString(im.getImageUrl());
                obj[1] = StringUtil.getString(message.getSenderName());
                obj[2] = StringUtil.getDialogTimeStr(StringUtil.stringToDateAndTime(message.getAddTime()));
                list.add(obj);
            }
        }
        return list;
    }

    private void resetVoiceImage(ImageView imageView, int msgType, int position){
        if(curVoiceIndex == position){
            if(msgType==TYPE_MESSAGE_VISITOR){
                imageView.setImageResource(R.drawable.ks_anim_voice_right_playing);
            }else if(msgType==TYPE_MESSAGE_CUSTOMER){
                imageView.setImageResource(R.drawable.ks_anim_voice_left_playing);
            }
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
        }else{
            if(msgType==TYPE_MESSAGE_VISITOR){
                imageView.setImageResource(R.drawable.ks_voice_right_normal);
            }else if(msgType==TYPE_MESSAGE_CUSTOMER){
                imageView.setImageResource(R.drawable.ks_voice_left_normal);
            }
        }
    }

    private class ServiceEvaluateClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            new KSDialogEvaluation(mActivity).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
package cn.kuaishang.kssdk.controller;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.kuaishang.callback.SdkLastQueryCallback;
import cn.kuaishang.callback.SdkOpenCallback;
import cn.kuaishang.callback.SdkSendMessageCallback;
import cn.kuaishang.core.KSManager;
import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.callback.OnConversationOpenCallback;
import cn.kuaishang.kssdk.callback.OnLastQueryCallback;
import cn.kuaishang.kssdk.callback.OnSendMessageCallback;
import cn.kuaishang.kssdk.model.BaseMessage;
import cn.kuaishang.model.ModelDialogRecord;
import cn.kuaishang.util.KSConstant;
import cn.kuaishang.util.StringUtil;

import static cn.kuaishang.util.StringUtil.getString;

public class ControllerImpl implements KSController {

    public Context mContext;
    public KSManager ksManager;

    public ControllerImpl(Context context) {
        this.mContext = context;
        this.ksManager = KSManager.getInstance(context);
        // 初始化图片缓存组件
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                //.showStubImage(R.drawable.icon_weixin_visitor)//设置图片在下载期间显示的图片
                //.showImageForEmptyUri(R.drawable.icon_weixin_visitor)//设置图片Uri为空或是错误的时候显示的图片
//					.showImageOnFail(R.drawable.placeholder_image)//设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                //.cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                //.memoryCache(new UsingFreqLimitedMemoryCache(4*1024*1024))
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileCount(100)
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 60, null)
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions)
                //.enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onConversationOpen(final OnConversationOpenCallback callback) {
        SdkOpenCallback sdkCallBack = new SdkOpenCallback() {
            @Override
            public void onResult() {
                callback.onResult();
                ksManager.openKsService();
            }
        };
        ksManager.gotoDialog(sdkCallBack);
    }

    @Override
    public String getConversationResult() {
        String result = ksManager.getConversationResult();
        if(KSConstant.CONVERSATIONRESULT_OFFLINE.equals(result)){
            result = mContext.getString(R.string.ks_title_offline);
        }else if(KSConstant.CONVERSATIONRESULT_QUEUE.equals(result)){
            result = mContext.getString(R.string.ks_title_queue);
        }else if(KSConstant.CONVERSATIONRESULT_ISSHIELD.equals(result)){
            result = mContext.getString(R.string.ks_title_isshield);
        }else if(KSConstant.CONVERSATIONRESULT_UNCONN.equals(result)){
            result = mContext.getString(R.string.ks_title_unconn);
        }else if(KSConstant.CONVERSATIONRESULT_DIALOG.equals(result)){
            String name = ksManager.getCurCsName();
            result = StringUtil.getPromptingString(mContext.getString(R.string.ks_title_dialoging), StringUtil.getString(name));
        }
        return result;
    }

    @Override
    public boolean isShield() {
        return ksManager.isShield();
    }

    @Override
    public Integer getCurStatus() {
        return ksManager.getCurStatus();
    }

    @Override
    public Integer getCurCsId() {
        return ksManager.getCurCsId();
    }

    @Override
    public String getCurCsName() {
        return ksManager.getCurCsName();
    }

    @Override
    public void saveDialogRecord(Map map) {
        ksManager.saveDialogRecord(map);
    }

    @Override
    public List<BaseMessage> getDialogRecords(String addTime) {
        List result = new ArrayList();
        List<Map> list = ksManager.getDialogRecords(addTime);
        for(Map map : list){
            Long recId = StringUtil.getLong(map.get("recId"));
            Integer customerId = StringUtil.getInteger(map.get("customerId"));
            String senderName = getString(map.get("senderName"));
            Integer recType = StringUtil.getInteger(map.get("recType"));
            String recContent = getString(map.get("recContent"));
            String time = getString(map.get("addTime"));
            String localId = getString(map.get("localId"));

            ModelDialogRecord record = new ModelDialogRecord();
            record.setRecId(recId);
            record.setCustomerId(customerId);
            record.setSenderName(senderName);
            record.setRecType(recType);
            record.setRecContent(recContent);
            record.setAddTime(time);
            record.setLocalId(localId);

            result.add(KSUIUtil.newMessage(mContext, record));
        }
        return result;
    }

    @Override
    public void sendMessage(String content, final OnSendMessageCallback callback) {
        SdkSendMessageCallback sdkCallBack = new SdkSendMessageCallback() {
            @Override
            public void onSuccess(Map result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        };
        ksManager.sendMessage(content, sdkCallBack);
    }

    @Override
    public void sendImage(String filePath, final OnSendMessageCallback callback) {
        SdkSendMessageCallback sdkCallBack = new SdkSendMessageCallback() {
            @Override
            public void onSuccess(Map result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        };
        ksManager.uploadImage(filePath, sdkCallBack);
    }

    @Override
    public void     sendVoice(String filePath, final OnSendMessageCallback callback) {
        SdkSendMessageCallback sdkCallBack = new SdkSendMessageCallback() {
            @Override
            public void onSuccess(Map result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        };
        ksManager.uploadVoice(filePath, sdkCallBack);
    }

    @Override
    public void sendPredicte(String content) {
        ksManager.sendPredicte(content);
    }

    @Override
    public void sendServiceEvaluate(String value, String content) {
        ksManager.sendServiceEvaluate(value, content);
    }

    @Override
    public void sendFeedback(Map map) {
        ksManager.sendFeedback(map);
    }

    @Override
    public void queryLastRecords(final OnLastQueryCallback callback) {
        SdkLastQueryCallback sdkCallBack = new SdkLastQueryCallback() {
            @Override
            public void onSuccess(boolean hasNewRecord) {
                callback.onSuccess(hasNewRecord);
            }

            @Override
            public void onFailure(String message) {

            }
        };
        ksManager.queryInfoAndRecord(sdkCallBack);
    }
}

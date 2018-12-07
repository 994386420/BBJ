package cn.kuaishang.kssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kuaishang.kssdk.KSConfig;
import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.adapter.KSChatAdapter;
import cn.kuaishang.kssdk.callback.OnConversationOpenCallback;
import cn.kuaishang.kssdk.callback.OnLastQueryCallback;
import cn.kuaishang.kssdk.callback.OnSendMessageCallback;
import cn.kuaishang.kssdk.controller.KSController;
import cn.kuaishang.kssdk.controller.KSReceiver;
import cn.kuaishang.kssdk.model.BaseMessage;
import cn.kuaishang.kssdk.model.ImageMessage;
import cn.kuaishang.kssdk.model.TextMessage;
import cn.kuaishang.kssdk.model.VoiceMessage;
import cn.kuaishang.kssdk.util.BizConfig;
import cn.kuaishang.kssdk.util.KSAudioRecorder;
import cn.kuaishang.kssdk.widget.KSCustomKeyboardLayout;
import cn.kuaishang.kssdk.widget.KSPopupVoice;
import cn.kuaishang.util.FileUtil;
import cn.kuaishang.util.KSConstant;
import cn.kuaishang.util.KSKey;
import cn.kuaishang.util.StringUtil;

/**
 * Created by Admin on 2017/2/5.
 */

public class KSConversationActivity extends KSBaseActivity implements View.OnClickListener, View.OnTouchListener{

    private Context mContext = this;
    private KSController mController;
    private MessageReceiver mMessageReceiver;

    private TextView mTitleTv;
    private ProgressBar mProgressBar;

    private EditText mInputEt;
    private TextView mInputTv;
    private ImageView mVoiceIv;
    private ImageView mEmotionIv;
    private ImageView mFunctionIv;
    private TextView mSendTv;
    private KSCustomKeyboardLayout mCustomKeyboardLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private List messages;
    private KSChatAdapter mAdapter;

    //图片上传相关
    private List<String> uploadList; // 要上传的文件路径
    private Map<String, String> uploadTime; // 要上传的文件时间串

    // 照相机相关
    private File camerafile; // 相机中拍的照片文件

    //发送语音动画弹出层
    private KSPopupVoice mPop;

    private void setTitle(String title){
        if(StringUtil.isNotEmpty(title)){
            mTitleTv.setText(title);
        }else{
            if(mController==null)return;
            Integer curStatus = mController.getCurStatus();
            //Integer curCsId = mController.getCurCsId();
            if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_WAITING)){
                //排队中
                mTitleTv.setText(R.string.ks_title_queue);
            }else if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_TURNING)){
                //转接中
                mTitleTv.setText(R.string.ks_title_transfering);
            }else if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_DIALOGING)){
                //对话中
                String curCsName = mController.getCurCsName();
                mTitleTv.setText(StringUtil.getPromptingString(getString(R.string.ks_title_dialoging), StringUtil.getString(curCsName)));
            }else if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_LEAVE)){
                //已离开
                mTitleTv.setText(R.string.ks_title_offline);
            }
        }
    }

    public void setBottomView(){
        setBottomView(false);
    }

    public void setBottomView(boolean isInit){
        if(mController==null)return;
        Integer curStatus = mController.getCurStatus();
        if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_WAITING)
                || StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_TURNING)
                || StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_DIALOGING)){
            findViewById(R.id.bottom_ll).setVisibility(View.VISIBLE);
            mCustomKeyboardLayout.closeAllKeyboard();
            findViewById(R.id.operator_ll).setVisibility(View.GONE);
        }else{
            findViewById(R.id.bottom_ll).setVisibility(View.GONE);
            findViewById(R.id.operator_ll).setVisibility(View.VISIBLE);
            if(isInit){
                if(getString(R.string.ks_title_unconn).equals(mTitleTv.getText().toString())) {
                    ((TextView)findViewById(R.id.oper_title)).setText(R.string.ks_tip_unConn);
                    doConn();
                } else if (mController.isShield()) {
                    ((TextView)findViewById(R.id.oper_title)).setText(R.string.ks_tip_isShield);
                } else {
                    ((TextView)findViewById(R.id.oper_title)).setText(R.string.ks_tip_notCustomers);
                }
                findViewById(R.id.oper_newDialog_tv).setVisibility(View.GONE);
            }else{
                ((TextView)findViewById(R.id.oper_title)).setText(R.string.ks_tip_dialogEnd);
                if (mController.isShield()) {
                    findViewById(R.id.oper_newDialog_tv).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.oper_newDialog_tv).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mController = KSConfig.getController(this);
        mController.onConversationOpen(new OnConversationOpenCallback() {
            @Override
            public void onResult() {
                setTitle(mController.getConversationResult());
                setBottomView(true);
                initMessage();
                mProgressBar.setVisibility(View.GONE);
            }
        });
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        registerReceiver();
    }

    @Override
    protected int getResId() {
        return R.layout.ks_activity_conversation;
    }

    @Override
    protected void initView() {
        mTitleTv = getViewById(R.id.title_tv);

        mProgressBar = getViewById(R.id.progressbar);
        mInputEt = getViewById(R.id.input_et);
        mInputTv = getViewById(R.id.input_tv);
        mVoiceIv = getViewById(R.id.voice_iv);
        mEmotionIv = getViewById(R.id.emotion_iv);
        mFunctionIv = getViewById(R.id.function_iv);
        mSendTv = getViewById(R.id.send_tv);
        mCustomKeyboardLayout = getViewById(R.id.customKeyboardLayout);
        mSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mListView = getViewById(R.id.messages_lv);

        mVoiceIv.setOnClickListener(this);
        mEmotionIv.setOnClickListener(this);
        mFunctionIv.setOnClickListener(this);
        findViewById(R.id.oper_newDialog_tv).setOnClickListener(this);
        findViewById(R.id.oper_feedback_tv).setOnClickListener(this);
        getViewById(R.id.send_tv).setOnClickListener(this);
    }

    @Override
    protected void setListener() {
        mCustomKeyboardLayout.init(this, mInputEt, new KSCustomKeyboardLayout.Callback() {
            @Override
            public void onAudioRecorderFinish(int time, String filePath) {

            }

            @Override
            public void onAudioRecorderTooShort() {

            }

            @Override
            public void scrollContentToBottom() {

            }

            @Override
            public void onAudioRecorderNoPermission() {

            }

            @Override
            public void openCamera() {
                camerafile = KSUIUtil.openCameraActivity(mContext);
            }
        });

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mController.sendPredicte(s.toString().trim());
                if (!TextUtils.isEmpty(s)) {
                    showSendView();
                } else {
                    hideSendView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputEt.setOnTouchListener(this);
        mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //mSendTextBtn.performClick();
                    KSUIUtil.closeKeyboard(KSConversationActivity.this);
                    return true;
                }
                return false;
            }
        });
        mInputTv.setOnTouchListener(voiceListener);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    mCustomKeyboardLayout.closeAllKeyboard();
                }
                return false;
            }
        });
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                String content = mChatMessageList.get(arg2).getContent();
//                if (!TextUtils.isEmpty(content)) {
//                    MQUtils.clip(MQConversationActivity.this, content);
//                    MQUtils.show(MQConversationActivity.this, R.string.mq_copy_success);
//                    return true;
//                }
//                return false;
//            }
//        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore();
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.ks_title_connection));
        mProgressBar.setVisibility(View.VISIBLE);
        uploadList = new ArrayList<String>();
        uploadTime = new HashMap<String, String>();
        mAudioRecorder = new KSAudioRecorder(this);
        mAudioRecorder.setAudioStatusUpdateListener(new KSAudioRecorder.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
//                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
//                mTextView.setText(TimeUtils.long2String(time));
                mPop.updateImageView(db);
            }

            @Override
            public void onStop() {

            }
        });

        BizConfig.newInstance().initBizConfit(this);
        mPop = new KSPopupVoice(this);
//        Intent intent = new Intent();
//        intent.setClass(this, KSService.class);
//        this.startService(intent);
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        // 注册消息接收
        mMessageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KSConstant.ACTION_RECEIVEMESSAGES);
        intentFilter.addAction(KSConstant.ACTION_INPUT_START);
        intentFilter.addAction(KSConstant.ACTION_INPUT_STOP);
        intentFilter.addAction(KSConstant.ACTION_VISITOR_SUBINFO);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);

        // 网络监听
//        mNetworkChangeReceiver = new NetworkChangeReceiver();
//        IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(mNetworkChangeReceiver, mFilter);
    }

    /**
     * 初始化对话记录
     */
    private void initMessage(){
        messages = mController.getDialogRecords(null);
        mAdapter = new KSChatAdapter(this, messages);
        mListView.setAdapter(mAdapter);
        KSUIUtil.scrollToBottom(mListView);

        checkStatusAndInfo();
    }

    /**
     * 去服务器下载最新访客信息+对话记录
     */
    private void checkStatusAndInfo() {
        mController.queryLastRecords(new OnLastQueryCallback() {
            @Override
            public void onSuccess(final boolean hasNewRecord) {
                KSConversationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(null);
                        setBottomView();
                        if(hasNewRecord) {
                            messages = mController.getDialogRecords(null);
                            mAdapter.setKSMessage(messages);
                        }
                    }
                });
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMore(){
        BaseMessage message = mAdapter.getFirstMessage();
        if(message!=null){
            List messages = mController.getDialogRecords(message.getAddTime());
            mAdapter.loadMoreMessage(messages);
            mSwipeRefreshLayout.setRefreshing(false);
            if(messages.size()< KSConstant.MAX_RECORD_SIZE)mSwipeRefreshLayout.setEnabled(false);
        }else{
            mSwipeRefreshLayout.setEnabled(false);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null)return;
        Map<String,Object> data = (Map<String, Object>) intent.getSerializableExtra(KSKey.DATA);
        if(data==null)return;
        String type = StringUtil.getString(data.get(KSKey.TYPE));
        if(type.equals("text")){

        }else if(type.equals("image")){
            //图片发送
            List<String> list = (List<String>) data.get(KSKey.LIST);
            if(list==null || list.size()==0)return;
            int size = 0;
            for(String path : list){
                if(uploadTime.containsKey(path))//正在上传的图片不处理
                    continue;
                newUploadFile(path);
                size++;
            }
            if(uploadList.size()==size)
                new UploadTask().execute();
        }
    }

    private void newUploadFile(String path){
        String filePath = path;
//        listView.addUploadScale(filePath);
        uploadList.add(filePath);

//        SdkTdDialogRecordForm f = new SdkTdDialogRecordForm();
//        f.setId(null);
//        f.setRecId(recId);
//        f.setCurCsId(getMyId());
//        f.setCompId(getMyCompId());
//        f.setSenderName(getMyNickName());
//        f.setRecContent("{\"type\":\"image\",\"fileName\":\""+filePath+"\",\"isLocal\":\"true\"}}");
//        f.setAddTime(sendTime);
//        f.setRecType(OcConstant.TD_DR_RECTYPE_CUSTOMER);
//        f.setLocalStatus(OcConstant.MESSAGESTATUS_TIMEOUT);
//        f.setLocalId(timeStr);

        String timeStr = StringUtil.newLocalId();
//        String content = "{\"type\":\"image\",\"fileName\":\""+filePath+"\",\"isLocal\":\"true\"}}";
//        Date addTime = new Date();
//        ImageMessage im = new ImageMessage();
//        im.setContent(content);
//        im.setImageUrl(KSUIUtil.getImageUrl(mContext, content, addTime));
//        mAdapter.addKSMessage(im);

        uploadTime.put(filePath, timeStr);
//        getDbService().saveSdkRecord(f, visitorId);
//        listView.addData(f);
    }

    private class UploadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String filePath = uploadList.remove(0);
            uploadTime.remove(filePath);
//            listView.removeUploadScale(filePath);
//
//            if(result!=null){
//                //发送通知-更新界面
//                Map<String,Object> data = new HashMap<String, Object>();
//                data.put(KSKey.SDK_RECORD, result);
//                KSUIUtil.sendBroadcast(context, KSKey.ACTION_SDK_SENDSUCCESS, data);
//                //更新数据库
//                String localId = result.getLocalId();
//                Date sendTime = result.getAddTime();
//                getDbService().updateSdkRecord(localId, sendTime,OcConstant.MESSAGESTATUS_NORMAL, OcConstant.TD_DR_RECTYPE_CUSTOMER);
//
//                //转移到下载目录，并且删除
//                String realUrl = HtmlUtil.getSdkImageUrl(result.getRecContent(), result.getRecType(), sendTime);
//                String name = realUrl.substring(realUrl.lastIndexOf("/")+1);
//                FileUtil.copyFile(filePath, FileUtil.getCachePath()+name);
//                //FileUtil.deleteFile(new File(filePath));
//            }else{
//                listView.notifyDataSetChanged();
//            }
            if(uploadList.size()==0)return;
            new UploadTask().execute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            int scale = values[0];
//            if(scale>=100)scale=100;
//            listView.updateUploadScale(uploadList.get(0), scale);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if(!KSUIUtil.isNetworkConnected(mContext))return null;
                final String path = uploadList.get(0);
                String fileName = path.substring(
                        path.lastIndexOf("/") + 1,
                        path.lastIndexOf("."));
                String filePath = FileUtil.getUploadPath()+fileName+".jpg";

                mController.sendImage(filePath, new OnSendMessageCallback() {
                    @Override
                    public void onSuccess(Map result) {
                        if(result!=null){
                            result.put("recContent", "{\"type\":\"image\",\"fileName\":\""+path+"\",\"isLocal\":\"true\"}}");
                            mController.saveDialogRecord(result);
                            String content = StringUtil.getString(result.get("recContent"));
                            String time = StringUtil.getString(result.get("addTime"));
                            ImageMessage im = new ImageMessage();
                            im.setContent(content);
                            im.setImageUrl(KSUIUtil.getImageUrl(mContext, content, StringUtil.stringToDateAndTime(time)));
                            mAdapter.addKSMessage(im);
                            KSUIUtil.scrollToBottom(mListView);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(KSConversationActivity.this,"图片发送失败", Toast.LENGTH_LONG).show();
                    }
                });
//                Map<String,Object> parms=new HashMap<String, Object>();
//                parms.put("recId", recId);
//                parms.put("visitorId", visitorId);
//                parms.put("type", OcConstant.WX_TYPE_IMAGE);
//                parms.put("senderName", getMyNickName());
//                parms.put("localId", uploadTime.get(path));
//
//                File file = FileUtil.createFile(filePath);
//                KsMessage km = (KsMessage)Httptools.uploadBsFile(UrlConstantAndroid.SDK_UPLOADFILE, file, parms, new ProgressListener() {
//                    long totalSize;
//
//                    @Override
//                    public void setTotalSize(long totalSize) {
//                        this.totalSize = totalSize;
//                    }
//
//                    @Override
//                    public void updateProgress(long downSize) {
//                        publishProgress((int) ((downSize / (float) totalSize) * 100));
//                        try {
////							if(KSUtil.IS_DEVMODE)
//                            Thread.sleep(20);
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//                if(km.getCode()!= CodeConstant.SUCCESS){
//                    throw new ServerException(km.getCode());
//                }else{
//                    return (SdkTdDialogRecordForm)km.getBean();
//                }
                return "";
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if(requestCode == KSKey.KEY_REQUESTCODE_CAMERA){
                if (camerafile != null && camerafile.exists()) {
                    String path = camerafile.getPath();
                    //通知媒体库(如果没有通知，相册中不能及时显示刚照的照片)
                    Uri uri = Uri.parse("file://"+path);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    //图片处理
                    Bitmap bm = KSUIUtil.revitionImageSize(path);
                    String newStr = path.substring(
                            path.lastIndexOf("/") + 1,
                            path.lastIndexOf("."));
                    FileUtil.saveUploadBitmap(bm, "" + newStr);
                    //上传图片
                    newUploadFile(path);
                    if(uploadList.size()==1)
                        new UploadTask().execute();
                }
                return;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.voice_iv){
            toggleVoiceKeyboardInput();
        }else if(id == R.id.emotion_iv){
            changeToKeyboardInput();
            mCustomKeyboardLayout.toggleEmotionOriginKeyboard();
        }else if(id == R.id.function_iv){
            changeToKeyboardInput();
            mCustomKeyboardLayout.toggleFunctionOriginKeyboard();
        }else if(id == R.id.send_tv){
             sendMessage();
        }else if(id == R.id.oper_newDialog_tv){
            doConn();
        }else if(id == R.id.oper_feedback_tv){
            //用户反馈
            KSUIUtil.openActivity(mContext, null, KSFeedbackActivity.class);
        }
        checkInputResource();
    }

    private void doConn(){
        setTitle(getString(R.string.ks_title_connection));
        mProgressBar.setVisibility(View.VISIBLE);
        mController.onConversationOpen(new OnConversationOpenCallback() {
            @Override
            public void onResult() {
                setTitle(mController.getConversationResult());
                setBottomView(true);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessage(){
        final String content = StringUtil.getString(mInputEt.getText()).trim();
        if("".equals(content))return;
        mInputEt.getText().clear();
        mController.sendMessage(content, new OnSendMessageCallback() {
            @Override
            public void onSuccess(Map result) {
                if(result!=null){
                    mController.saveDialogRecord(result);
                    TextMessage tm = new TextMessage(content);
                    mAdapter.addKSMessage(tm);
                    KSUIUtil.scrollToBottom(mListView);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(KSConversationActivity.this,"消息发送失败", Toast.LENGTH_LONG).show();
                //重新检查对话状态
                checkStatusAndInfo();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCustomKeyboardLayout.closeCustomKeyboard();
        checkInputResource();
        return false;
    }

    /**
     * 切换语音输入和键盘输入
     */
    private void toggleVoiceKeyboardInput(){
        mCustomKeyboardLayout.closeAllKeyboard();
        if(isVoiceInputVisible()){
            changeToKeyboardInput();
            mCustomKeyboardLayout.changeToOriginalKeyboard();
        }else{
            changeToVoiceInput();
        }
    }

    private boolean isVoiceInputVisible(){
        return mInputTv.getVisibility() == View.VISIBLE;
    }

    private boolean isKeyboardInputVisible(){
        return mInputEt.getVisibility() == View.VISIBLE;
    }

    private void changeToVoiceInput(){
        mInputTv.setVisibility(View.VISIBLE);
        mInputEt.setVisibility(View.GONE);
    }

    private void changeToKeyboardInput(){
        mInputTv.setVisibility(View.GONE);
        mInputEt.setVisibility(View.VISIBLE);
    }

    private void checkInputResource(){
        mCustomKeyboardLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isVoiceInputVisible()){
                    mVoiceIv.setImageResource(R.drawable.msg_input_keyboard);
                }else{
                    mVoiceIv.setImageResource(R.drawable.msg_input_voice);
                }
                if(mCustomKeyboardLayout.isEmotionKeyboardVisible()){
                    mEmotionIv.setImageResource(R.drawable.msg_input_keyboard);
                }else{
                    mEmotionIv.setImageResource(R.drawable.msg_input_emotion);
                }
                if("".equals(mInputEt.getText().toString())){
                    hideSendView();
                }else{
                    showSendView();
                }
            }
        },100);
    }

    private void hideSendView(){
        mSendTv.setVisibility(View.GONE);
    }

    private void showSendView(){
        mSendTv.setVisibility(View.VISIBLE);
    }


    private static final int RECORDER_MAX_TIME = 60;
    private static final int VOICE_LEVEL_COUNT = 9;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_CANCEL = 3;

    private int mCurrentState = STATE_NORMAL;
    private boolean mIsRecording;
    private boolean mIsOvertime = false;
    private boolean mHasPermission = false;
    private KSAudioRecorder mAudioRecorder;
    private float mTime;
    private float downY;
    private boolean hasPrepare = false;
    private View.OnTouchListener voiceListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    hasPrepare = mAudioRecorder.prepareAudio();
                    downY = (int) event.getY();
                    mIsOvertime = false;
                    mHasPermission = true;
                    changeState(STATE_RECORDING);
                    mPop.showAtLocation(findViewById(R.id.contentLayout), Gravity.CENTER,0,0);
                    mIsRecording = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mIsOvertime && mIsRecording && mHasPermission) {
                        float moveY = event.getY();
                        if (downY - moveY > 100) {
                            changeState(STATE_WANT_CANCEL);
                        } else {
                            changeState(STATE_RECORDING);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(hasPrepare)handleActionUp();
                    mPop.dismiss();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mAudioRecorder.cancel();
                    mPop.dismiss();
                    reset();
                    break;
            }
            return true;
        }
    };

    private void changeState(int status) {
        if (mCurrentState != status) {
            mCurrentState = status;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    mPop.updateTextView(R.string.ks_audio_status_normal);
                    break;
                case STATE_RECORDING:
                    mPop.updateTextView(R.string.ks_audio_status_recording);
                    break;
                case STATE_WANT_CANCEL:
                    mPop.updateTextView(R.string.ks_audio_status_want_cancel);
                    break;
            }
        }
    }

    private void handleActionUp() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsOvertime && mHasPermission) {
                    // 录音没有超时的情况

//                    if (!mIsRecording || mTime < 1) {
//                        // prepare未完成
////                        mAudioRecorder.cancel();
////
////                        if (System.currentTimeMillis() - mLastTipTooShortTime > 1000) {
////                            mLastTipTooShortTime = System.currentTimeMillis();
////                            mCallback.onAudioRecorderTooShort();
////                        }
//                    } else
                    if (mCurrentState == STATE_RECORDING) {
                        endRecorder();
                    } else if (mCurrentState == STATE_WANT_CANCEL) {
                        mAudioRecorder.cancel();
                    }
                } else if (mIsRecording) {
                    // 录音超时，并且正在录音时
                    endRecorder();
                }
                reset();
            }
        });
    }

    /**
     * 结束录音
     */
    private void endRecorder() {
        mAudioRecorder.release();
        final String currentFilePath = mAudioRecorder.getCurrenFilePath();
        if (!TextUtils.isEmpty(currentFilePath)) {
            //上传文件
            mController.sendVoice(currentFilePath, new OnSendMessageCallback() {
                @Override
                public void onSuccess(Map result) {
                    if(result!=null){
                        result.put("recContent", "{\"type\":\"voice\",\"fileName\":\""+currentFilePath+"\",\"isLocal\":\"true\"}}");
                        mController.saveDialogRecord(result);
                        String content = StringUtil.getString(result.get("recContent"));
                        String time = StringUtil.getString(result.get("addTime"));
                        VoiceMessage im = new VoiceMessage();
                        im.setContent(content);
                        im.setVoiceUrl(KSUIUtil.getVoiceUrl(mContext, content, StringUtil.stringToDateAndTime(time)));
                        mAdapter.addKSMessage(im);
                        KSUIUtil.scrollToBottom(mListView);
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(KSConversationActivity.this,"语音发送失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 恢复到初始状态
     */
    private void reset() {
        mIsRecording = false;
        mHasPermission = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private class MessageReceiver extends KSReceiver{

        @Override
        public void receiveMessages(List<BaseMessage> messages) {
            if(messages==null)return;
            mAdapter.addKSMessage(messages);
            KSUIUtil.scrollToBottom(mListView);
        }

        @Override
        public void receiveInputStart() {
            setTitle(getString(R.string.ks_title_inputting));
        }

        @Override
        public void receiveInputStop() {
            setTitle(null);
        }

        @Override
        public void receiveVisitorInfo() {
            setTitle(null);
            setBottomView();
        }
    }

    @Override
    protected void onDestroy() {
        KSUIUtil.closeKeyboard(this);
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}

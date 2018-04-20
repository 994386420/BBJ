package com.bbk.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bbk.activity.WebViewWZActivity;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SoftHideKeyBoardUtil;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.presenter.ChatPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ChatView;
import com.bbk.activity.R;
import com.bbk.chat.adapters.ChatAdapter;
import com.bbk.chat.model.CustomMessage;
import com.bbk.chat.model.FileMessage;
import com.bbk.chat.model.FriendProfile;
import com.bbk.chat.model.FriendshipInfo;
import com.bbk.chat.model.GroupInfo;
import com.bbk.chat.model.ImageMessage;
import com.bbk.chat.model.Message;
import com.bbk.chat.model.MessageFactory;
import com.bbk.chat.model.TextMessage;
import com.bbk.chat.model.UGCMessage;
import com.bbk.chat.model.VideoMessage;
import com.bbk.chat.model.VoiceMessage;
import com.bbk.chat.utils.FileUtil;
import com.bbk.chat.utils.MediaUtil;
import com.bbk.chat.utils.RecorderUtil;
import com.tencent.qcloud.ui.ChatInput;
import com.tencent.qcloud.ui.TemplateTitle;
import com.tencent.qcloud.ui.VoiceSendingView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends FragmentActivity implements ChatView {

    private static final String TAG = "ChatActivity";

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatPresenter presenter;
    private ChatInput input;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private static final int VIDEO_RECORD = 500;
    private Uri fileUri;
    private VoiceSendingView voiceSendingView;
    private String identify;
    private RecorderUtil recorder = new RecorderUtil();
    private TIMConversationType type;
    private String titleStr;
    private Handler handler = new Handler();
    private TIMFriendshipManager timFriendshipManager;
    private TIMFriendshipManagerExt timFriendshipManagerExt;
    private String tag = "ChatActivity=====";
    private String leftImageUri,rightImageUrl;


    public static void navToChat(Context context, String identify, TIMConversationType type){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SoftHideKeyBoardUtil.assistActivity(this,getStatusBarHeight(ChatActivity.this));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View topView = findViewById(R.id.root);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        identify = getIntent().getStringExtra("identify");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");
        getSelfProfile();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (input != null){
            if (input.getText().length() > 0){
                TextMessage message = new TextMessage(input.getText());
                presenter.saveDraft(message.getMessage());
            }else{
                presenter.saveDraft(null);
            }
            RefreshEvent.getInstance().onRefresh();
            presenter.readMessages();
            MediaUtil.getInstance().stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!= null){
            presenter.stop();
        }
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage){
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType){
                        case TYPING:
                            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                            title.setTitleText(getString(R.string.chat_typing));
                            handler.removeCallbacks(resetTitle);
                            handler.postDelayed(resetTitle,3000);
                            break;
                        default:
                            break;
                    }
                }else{
                    if (messageList.size()==0){
                        mMessage.setHasTime(null);
                    }else{
                        mMessage.setHasTime(messageList.get(messageList.size()-1).getMessage());
                    }
                    messageList.add(mMessage);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(adapter.getCount()-1);
                }

            }
        }

    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i){
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted) continue;
            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1){
                mMessage.setHasTime(messages.get(i+1));
                messageList.add(0, mMessage);
            }else{
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(newMsgNum);
    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {
        for (Message msg : messageList) {
            TIMMessageExt ext = new TIMMessageExt(msg.getMessage());
            if (ext.checkEquals(timMessageLocator)) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList){
            if (msg.getMessage().getMsgUniqueId() == id){
                switch (code){
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        adapter.notifyDataSetChanged();

    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(getPackageManager()) != null) {
            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        Message message = new TextMessage(input.getText());
        presenter.sendMessage(message.getMessage());
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }


    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();

    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else if (recorder.getTimeInterval() > 60) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_long), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        presenter.sendMessage(message.getMessage());
    }


    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice() {

    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C){
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void videoAction() {
        Intent intent = new Intent(this, TCVideoRecordActivity.class);
        startActivityForResult(intent, VIDEO_RECORD);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()){
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }else if (message.getMessage().isSelf()){
            menu.add(0, 4, Menu.NONE, getString(R.string.chat_pullback));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage){
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            case 4:
                presenter.revokeMessage(message.getMessage());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW){
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri",false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists()){
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    if (file.length() == 0 && options.outWidth == 0) {
                        Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                    }else {
                        if (file.length() > 1024 * 1024 * 10){
                            Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
                        }else{
                            Message message = new ImageMessage(path,isOri);
                            presenter.sendMessage(message.getMessage());
                        }
                    }
                }else{
                    Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                String videoPath = data.getStringExtra("videoPath");
                String coverPath = data.getStringExtra("coverPath");
                long duration = data.getLongExtra("duration", 0);
                Message message = new UGCMessage(videoPath, coverPath, duration);
                presenter.sendMessage(message.getMessage());
            }
        }

    }


    private void showImagePreview(String path){
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    private void sendFile(String path){
        if (path == null) return;
        File file = new File(path);
        if (file.exists()){
            if (file.length() > 1024 * 1024 * 10){
                Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
            }else{
                Message message = new FileMessage(path);
                presenter.sendMessage(message.getMessage());
            }
        }else{
            Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSelfProfile();
    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
            title.setTitleText(titleStr);
        }
    };

    private void getSelfProfile(){
        timFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>(){
            @Override
            public void onError(int code, String desc){
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.e(tag, "getSelfProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(TIMUserProfile result){
//                Log.e(tag, "getSelfProfile succ");
//                Log.e(tag, "identifier: " + result.getIdentifier() + " nickName: " + result.getNickName()
//                        + " remark: " + result.getRemark() + " allow: " + result.getAllowType()+ " url: "+result.getFaceUrl());
                if (result.getFaceUrl() != null){
                    rightImageUrl = result.getFaceUrl();
                    getFriendsProfile();
                }
            }
        });
    }

    private void getFriendsProfile(){
//        getSelfProfile();
        //待获取用户资料的好友列表
        List<String> users = new ArrayList<String>();
        users.add(identify);
//        Log.e(tag, "getUsersProfile " + " iddddd"+identify);
        //获取好友资料
        timFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
//                Log.e(tag, "getUsersProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result){
                Log.e(tag, "getUsersProfile succ");
                for(TIMUserProfile res : result){
//                    Log.e(tag, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
//                            + " remark: " + res.getRemark());
                    presenter = new ChatPresenter(ChatActivity.this, identify, type);
                    input = (ChatInput) findViewById(R.id.input_panel);
                    input.setChatView(ChatActivity.this);
                    if (messageList != null){
                        adapter = new ChatAdapter(ChatActivity.this, R.layout.item_message, messageList,res.getFaceUrl(),rightImageUrl);
                        listView = (ListView) findViewById(R.id.list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                    listView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    input.setInputMode(ChatInput.InputMode.NONE);
                                    break;
                            }
                            return false;
                        }
                    });
                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                        private int firstItem;

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                                //如果拉到顶端读取更多消息
                                presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);

                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            firstItem = firstVisibleItem;
                        }
                    });
                    registerForContextMenu(listView);
                    TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                    switch (type) {
                        case C2C:
                            title.setMoreImg(R.drawable.btn_person);
                            if (FriendshipInfo.getInstance().isFriend(identify)){
                                title.setMoreImgAction(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                                        intent.putExtra("identify", identify);
                                        startActivity(intent);
                                    }
                                });
                                FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
                                title.setTitleText(titleStr = profile == null ? identify : profile.getName());
                            }else{
                                title.setTitleText(titleStr = res.getNickName());
                            }
                            break;
                        case Group:
                            title.setMoreImg(R.drawable.btn_group);
                            title.setMoreImgAction(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChatActivity.this, GroupProfileActivity.class);
                                    intent.putExtra("identify", identify);
                                    startActivity(intent);
                                }
                            });
                            title.setTitleText(GroupInfo.getInstance().getGroupName(identify));
                            break;

                    }
                    voiceSendingView = (VoiceSendingView) findViewById(R.id.voice_sending);
                    presenter.start();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSelfProfile();
    }
}

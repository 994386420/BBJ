package cn.kuaishang.kssdk.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.kuaishang.core.KSManager;
import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.activity.AlbumBucketActivity;
import cn.kuaishang.kssdk.activity.KSConversationActivity;
import cn.kuaishang.kssdk.util.KSEmotionUtil;
import cn.kuaishang.util.KSConstant;
import cn.kuaishang.util.KSKey;
import cn.kuaishang.util.StringUtil;

public class KSCustomKeyboardLayout extends KSBaseCustomCompositeView {
    private static final int WHAT_SCROLL_CONTENT_TO_BOTTOM = 1;
    private static final int WHAT_CHANGE_TO_EMOTION_KEYBOARD = 2;
    private static final int WHAT_CHANGE_TO_FUNCTION_KEYBOARD = 3;

    private KSEmotionKeyboardLayout mEmotionKeyboardLayout;
    private KSFunctionKeyboardLayout mFunctionKeyboardLayout;

    private Activity mActivity;
    private EditText mContentEt;
    private Callback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CHANGE_TO_EMOTION_KEYBOARD:
                    showEmotionKeyboard();
                    break;
                case WHAT_CHANGE_TO_FUNCTION_KEYBOARD:
                    showFunctionKeyboard();
                    break;
                case WHAT_SCROLL_CONTENT_TO_BOTTOM:
                    mCallback.scrollContentToBottom();
                    break;
            }
        }
    };

    public KSCustomKeyboardLayout(Context context) {
        super(context);
    }

    public KSCustomKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KSCustomKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getResId() {
        return R.layout.ks_layout_custom_keyboard;
    }

    @Override
    protected void initView() {
        mEmotionKeyboardLayout = getViewById(R.id.emotionKeyboardLayout);
        mFunctionKeyboardLayout = getViewById(R.id.functionKeyboardLayout);

        mFunctionKeyboardLayout.setCallback(new KSFunctionKeyboardLayout.Callback() {
            @Override
            public void onPhotoClick() {
                closeAllKeyboard();
                Map<String,Object> data = new HashMap<String, Object>();
                data.put(KSKey.CLASS, KSConversationActivity.class);
                KSUIUtil.openActivity(mActivity, data, AlbumBucketActivity.class);
            }

            @Override
            public void onCameraClick() {
                closeAllKeyboard();
                mCallback.openCamera();
            }

            @Override
            public void onEvaluateClick() {
                closeAllKeyboard();
                Integer curStatus = KSManager.getInstance(mActivity).getCurStatus();
                if(StringUtil.isEqualsInt(curStatus, KSConstant.VISITOR_STATUS_WAITING)){
                    Toast.makeText(mActivity,"没有与任何客服建立连接，不能进行评价！", Toast.LENGTH_LONG).show();
                    return;
                }
                KSDialogEvaluation dialog = new KSDialogEvaluation(mActivity);
                dialog.show();
            }
        });
    }

    @Override
    protected void initData() {
        mEmotionKeyboardLayout.setCallback(new KSEmotionKeyboardLayout.Callback() {
            @Override
            public void onDelete() {
                mContentEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }

            @Override
            public void onInsert(String text) {
                // 在当前光标位置插入文本
                int cursorPosition = mContentEt.getSelectionStart();
                StringBuilder sb = new StringBuilder(mContentEt.getText());
                sb.insert(cursorPosition, text);
                mContentEt.setText(KSEmotionUtil.getEmotionText(getContext(), sb.toString(), 20));
                mContentEt.setSelection(cursorPosition + text.length());
            }
        });
    }

    /**
     * 切换表情键盘和软键盘
     */
    public void toggleEmotionOriginKeyboard() {
        if (isEmotionKeyboardVisible()) {
            changeToOriginalKeyboard();
        } else {
            changeToEmotionKeyboard();
        }
    }

    /**
     * 切换功能键盘和软键盘
     */
    public void toggleFunctionOriginKeyboard() {
        if (isFunctionKeyboardVisible()) {
            changeToOriginalKeyboard();
        } else {
            changeToFunctionKeyboard();
        }
    }

    /**
     * 切换到语音键盘
     */
    public void changeToFunctionKeyboard() {
        KSUIUtil.closeKeyboard(mActivity);

        if (isCustomKeyboardVisible()) {
            showFunctionKeyboard();
        } else {
            mHandler.sendEmptyMessageDelayed(WHAT_CHANGE_TO_FUNCTION_KEYBOARD, KSUIUtil.KEYBOARD_CHANGE_DELAY);
        }
    }

    /**
     * 切换到表情键盘
     */
    public void changeToEmotionKeyboard() {
        if (!mContentEt.isFocused()) {
            mContentEt.requestFocus();
            mContentEt.setSelection(mContentEt.getText().toString().length());
        }

        KSUIUtil.closeKeyboard(mActivity);

        if (isCustomKeyboardVisible()) {
            showEmotionKeyboard();
        } else {
            mHandler.sendEmptyMessageDelayed(WHAT_CHANGE_TO_EMOTION_KEYBOARD, KSUIUtil.KEYBOARD_CHANGE_DELAY);
        }
    }

    /**
     * 切换到系统原始键盘
     */
    public void changeToOriginalKeyboard() {
        closeCustomKeyboard();
        KSUIUtil.openKeyboard(mContentEt);
        // 打开系统键盘也延时了的，这里延时2倍再滚动到底部
        mHandler.sendEmptyMessageDelayed(WHAT_SCROLL_CONTENT_TO_BOTTOM, KSUIUtil.KEYBOARD_CHANGE_DELAY * 2);
    }

    /**
     * 显示表情键盘
     */
    private void showEmotionKeyboard() {
        mEmotionKeyboardLayout.setVisibility(VISIBLE);
        sendScrollContentToBottomMsg();

        closeFunctionKeyboard();
    }

    /**
     * 显示功能键盘
     */
    private void showFunctionKeyboard() {
        mFunctionKeyboardLayout.setVisibility(VISIBLE);
        sendScrollContentToBottomMsg();

        closeEmotionKeyboard();
    }

    /**
     * 延时发送滚动内容到底部的消息给Handler
     */
    private void sendScrollContentToBottomMsg() {
        mHandler.sendEmptyMessageDelayed(WHAT_SCROLL_CONTENT_TO_BOTTOM, KSUIUtil.KEYBOARD_CHANGE_DELAY);
    }

    /**
     * 关闭表情键盘
     */
    public void closeEmotionKeyboard() {
        mEmotionKeyboardLayout.setVisibility(GONE);
    }

    /**
     * 关闭功能键盘
     */
    public void closeFunctionKeyboard() {
        mFunctionKeyboardLayout.setVisibility(GONE);
    }

    /**
     * 关闭自定义键盘
     */
    public void closeCustomKeyboard() {
        closeEmotionKeyboard();
        closeFunctionKeyboard();
    }

    /**
     * 关闭所有键盘
     */
    public void closeAllKeyboard() {
        closeCustomKeyboard();
        KSUIUtil.closeKeyboard(mActivity);
    }

    /**
     * 表情键盘是否可见
     *
     * @return
     */
    public boolean isEmotionKeyboardVisible() {
        return mEmotionKeyboardLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 语音键盘是否可见
     *
     * @return
     */
    public boolean isFunctionKeyboardVisible() {
        return mFunctionKeyboardLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 自定义键盘是否可见，在Activity的onBackPressed中处理返回按钮
     *
     * @return
     */
    public boolean isCustomKeyboardVisible() {
        return isEmotionKeyboardVisible() || isFunctionKeyboardVisible();
    }

    /**
     * 初始化，必须调用该方法
     * @param activity
     * @param contentEt
     */
    public void init(Activity activity, EditText contentEt, Callback callback) {
        if (activity == null || contentEt == null || callback == null) {
            throw new RuntimeException(KSCustomKeyboardLayout.class.getSimpleName() + "的init方法的参数均不能为null");
        }

        mActivity = activity;
        mContentEt = contentEt;
        mCallback = callback;


        mContentEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCustomKeyboardVisible()) {
                    closeCustomKeyboard();
                }
                sendScrollContentToBottomMsg();
            }
        });

        mContentEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    closeAllKeyboard();
                } else {
                    sendScrollContentToBottomMsg();
                }
            }
        });
    }

    public interface Callback {
        /**
         * 录音完成
         *
         * @param time     录音时长
         * @param filePath 音频文件路径
         */
        void onAudioRecorderFinish(int time, String filePath);

        /**
         * 录音时间太短
         */
        void onAudioRecorderTooShort();

        /**
         * 滚动内容到最底部
         */
        void scrollContentToBottom();

        /**
         * 没有录音权限
         */
        void onAudioRecorderNoPermission();

        /**
         * 打开相机
         */
        void openCamera();
    }
}
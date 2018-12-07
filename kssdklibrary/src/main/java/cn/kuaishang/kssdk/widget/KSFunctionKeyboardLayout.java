package cn.kuaishang.kssdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.kuaishang.kssdk.R;

/**
 * Created by Admin on 2017/2/7.
 */

public class KSFunctionKeyboardLayout extends KSBaseCustomCompositeView implements View.OnClickListener{

    private Context context;

    public KSFunctionKeyboardLayout(Context context) {
        super(context);
        this.context = context;
    }

    public KSFunctionKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public KSFunctionKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected int getResId() {
        return R.layout.ks_layout_function_keyboard;
    }

    @Override
    protected void initView() {
        findViewById(R.id.fun_photo_tv).setOnClickListener(this);
        findViewById(R.id.fun_camera_tv).setOnClickListener(this);
        findViewById(R.id.fun_evaluate_tv).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if(mCallback==null)return;
        int id = v.getId();
        if(id == R.id.fun_photo_tv){
            mCallback.onPhotoClick();
        }else if(id == R.id.fun_camera_tv) {
            mCallback.onCameraClick();
        }else if(id == R.id.fun_evaluate_tv){
            mCallback.onEvaluateClick();
        }
    }

    private Callback mCallback;
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onPhotoClick();
        void onCameraClick();
        void onEvaluateClick();
    }
}

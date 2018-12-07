package cn.kuaishang.kssdk.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.kuaishang.kssdk.R;

public class KSPopupVoice {

    private Context mContext;
    private PopupWindow mPop;
    private ImageView imageView;
    private TextView textView;


    public KSPopupVoice(Context context){
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.ks_popup_voice, null);
        imageView = (ImageView) view.findViewById(R.id.voiceImage);
        textView = (TextView) view.findViewById(R.id.voiceTip);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //PopupWindow(布局，宽度，高度)
        mPop = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPop.setFocusable(true);
        // 重写onKeyListener,按返回键消失
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });
        //点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPop != null && mPop.isShowing()) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }});
    }

    public void updateImageView(double db){
        int i = (int) (3000 + 6000 * db / 100);
        imageView.getDrawable().setLevel(i);
//        switch (i) {
//            case 0:
//                imageView.setImageResource(R.drawable.voic_volume0);
//                break;
//            case 1:
//                imageView.setImageResource(R.drawable.voic_volume1);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.voic_volume2);
//                break;
//            case 3:
//                imageView.setImageResource(R.drawable.voic_volume3);
//                break;
//            case 4:
//                imageView.setImageResource(R.drawable.voic_volume4);
//                break;
//            case 5:
//                imageView.setImageResource(R.drawable.voic_volume5);
//                break;
//            case 6:
//                imageView.setImageResource(R.drawable.voic_volume6);
//                break;
//            case 7:
//                imageView.setImageResource(R.drawable.voic_volume7);
//                break;
//            case 8:
//                imageView.setImageResource(R.drawable.voic_volume8);
//                break;
//            case 9:
//                imageView.setImageResource(R.drawable.voic_volume9);
//                break;
//            case 10:
//                imageView.setImageResource(R.drawable.voic_volume9);
//                break;
//            case 11:
//                imageView.setImageResource(R.drawable.voic_volume10);
//                break;
//            default:
//                imageView.setImageResource(R.drawable.voic_volume10);
//                break;
//        }
    }

    public void updateTextView(int resId){
        textView.setText(resId);
    }

    public void showAtLocation(View parent, int gravity, int x, int y){
        if(mPop.isShowing()){
            return ;
        }
        mPop.showAtLocation(parent, gravity, x, y);
    }

    public void showAsDropDown(View anchor){
        showAsDropDown(anchor,0,0);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff){
        if(mPop.isShowing()){
            return ;
        }
        mPop.showAsDropDown(anchor, xoff, yoff);
    }

    public void dismiss(){
        if (mPop.isShowing()) {
            mPop.dismiss();
        }
    }

}

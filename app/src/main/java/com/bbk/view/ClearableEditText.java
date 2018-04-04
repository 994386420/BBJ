package com.bbk.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearableEditText extends EditText {

	public interface OnTextClearListener {
		public void onTextClear(ClearableEditText v);
	}

	private Context mContext;
	private Drawable mDrawableRight;
	private Drawable mDrawableLeft;
	private Rect mBounds;

	private OnTextClearListener mOnTextClearListener;

	public ClearableEditText(Context context) {
		super(context);
		initialize(context);
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public ClearableEditText(Context context, AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
        initialize(context);
    }

	private void initialize(Context context) {
		
		setDrawable();
        addTextChangedListener(new TextWatcher() { // ���ı����ݸı���м���
            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }
 
            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                ClearableEditText.this.setDrawable();
            }

			@Override
			public void afterTextChanged(Editable s) {
			}
        });
	}
	
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if (right != null) {
			mDrawableRight = right;
		}
		if(left != null) {
			mDrawableLeft = left;
		}
		super.setCompoundDrawables(left, top, null, bottom);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN && mDrawableRight != null) {

			mBounds = mDrawableRight.getBounds();
//			Rect boundsLeft = mDrawableLeft.getBounds();
//			int leftX = boundsLeft.width();
//			int leftY = boundsLeft.height();


			final int x = (int) event.getX();
			final int y = (int) event.getY();

			if (x >= (this.getWidth() - mBounds.width() - this.getPaddingLeft())
					&& x <= (this.getWidth() - this.getPaddingRight())
					&& y >= this.getPaddingTop()
					&& y <= (this.getHeight() - this.getPaddingBottom())) {
				clear(); 
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}

		return super.onTouchEvent(event);
	}

	public void setDrawable() {
//		setText(text);
		String text = getText().toString();
		if (text == null || text.length() == 0) {
			super.setCompoundDrawables(mDrawableLeft, null, null, null);
		} else {
			super.setCompoundDrawables(mDrawableLeft, null, mDrawableRight, null);
		}
	}

	private void clear() {
		setText("");
		setDrawable();
		if (mOnTextClearListener != null) {
			mOnTextClearListener.onTextClear(this);
		}
		super.setCompoundDrawables(mDrawableLeft, null, null, null);
	}

	public void setOnTextClearListener(OnTextClearListener onTextClearListener) {
		mOnTextClearListener = onTextClearListener;
	}

	@Override
	public void finalize() throws Throwable {
		mDrawableRight = null;
		mBounds = null;
		super.finalize();
	}
}
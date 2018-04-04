package com.bbk.view;

//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.bbk.activity.R;
		import android.content.Context;
		import android.util.AttributeSet;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.widget.ListView;
		import android.widget.ProgressBar;
		import android.widget.TextView;

		import com.bbk.activity.R;

public class MyScrollListView extends ListView {

//	public MyScrollListView(Context context) {
//		super(context);
//	}
//
//	public MyScrollListView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public MyScrollListView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//			case MotionEvent.ACTION_MOVE:
//				getParent().requestDisallowInterceptTouchEvent(true);
//				break;
//			case MotionEvent.ACTION_UP:
//			case MotionEvent.ACTION_CANCEL:
//				getParent().requestDisallowInterceptTouchEvent(false);
//				break;
//		}
//		return super.dispatchTouchEvent(ev);
//	}

		private Context context;
		private View footer;
		private ProgressBar progressBar;
		private TextView tv;

		public MyScrollListView(Context context) {
			super(context);
			init(context);
		}
		public MyScrollListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(context);
		}
		public MyScrollListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init(context);
		}
		private void init(Context context) {
			this.context=context;
			footer= LayoutInflater.from(context).inflate(R.layout.activity_footer, null);
			this.addFooterView(footer);
			progressBar=(ProgressBar) footer.findViewById(R.id.progressBar);
			tv=(TextView) footer.findViewById(R.id.tv);
			tv.setText("上拉加载更多");
		}

		//正在加载数据，将listview底部提示文字置为"正在加载中。。。。"
		public void onLoading(){
			progressBar.setVisibility(VISIBLE);
			tv.setText("正在加载中...");
		}

		//加载完毕，将listView底部提示文字改为"上拉加载更多"
		public void LoadingComplete(){
			progressBar.setVisibility(GONE);
			tv.setText("上拉加载更多");
		}
	public void LoadingCompleted(){
		progressBar.setVisibility(GONE);
		tv.setText("没有更多了");
	}


		//重写onMeasure，解决scrollview与listview冲突
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		}


}

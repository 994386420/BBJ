package cn.kuaishang.kssdk.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.util.KSEmotionUtil;

public class KSEmotionKeyboardLayout extends KSBaseCustomCompositeView {
    private static final int EMOTION_COLUMN = 7;
    private static final int EMOTION_ROW = 3;
    private static final int EMOTION_PAGE_SIZE = EMOTION_COLUMN * EMOTION_ROW - 1;

    private ViewPager mContentVp;
    private LinearLayout mIndicatorLl;
    private ArrayList<ImageView> mIndicatorIvList;
    private ArrayList<GridView> mGridViewList;

    private Callback mCallback;

    public KSEmotionKeyboardLayout(Context context) {
        super(context);
    }

    public KSEmotionKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KSEmotionKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getResId() {
        return R.layout.ks_layout_emotion_keyboard;
    }

    @Override
    protected void initView() {
        mContentVp = getViewById(R.id.vp_emotion_keyboard_content);
        mIndicatorLl = getViewById(R.id.ll_emotion_keyboard_indicator);
    }

    @Override
    protected void initData() {
        mIndicatorIvList = new ArrayList<>();
        mGridViewList = new ArrayList<>();

        int emotionPageCount = (KSEmotionUtil.sEmotionKeyArr.length - 1) / EMOTION_PAGE_SIZE + 1;

        ImageView indicatorIv;
        LinearLayout.LayoutParams indicatorIvLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = KSUIUtil.dip2px(getContext(), 5);
        indicatorIvLp.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < emotionPageCount; i++) {
            indicatorIv = new ImageView(getContext());
            indicatorIv.setLayoutParams(indicatorIvLp);
            indicatorIv.setImageResource(R.drawable.ks_selector_emotion_indicator);
            indicatorIv.setEnabled(false);
            mIndicatorIvList.add(indicatorIv);
            mIndicatorLl.addView(indicatorIv);

            mGridViewList.add(getGridView(i));
        }
        mIndicatorIvList.get(0).setEnabled(true);
        mContentVp.setAdapter(new EmotionPagerAdapter());

        mContentVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mIndicatorIvList.size(); i++) {
                    mIndicatorIvList.get(i).setEnabled(false);
                }
                mIndicatorIvList.get(position).setEnabled(true);
            }
        });
    }

    private GridView getGridView(int position) {
        int edge = KSUIUtil.dip2px(getContext(), 5);
        GridView gridView = new GridView(getContext());

        gridView.setPadding(edge, edge, edge, edge);
        gridView.setNumColumns(EMOTION_COLUMN);
        gridView.setVerticalSpacing(edge);
        gridView.setHorizontalSpacing(edge);
        gridView.setOverScrollMode(OVER_SCROLL_NEVER);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setVerticalFadingEdgeEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setHorizontalFadingEdgeEnabled(false);
        gridView.setSelector(android.R.color.transparent);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmotionAdapter adapter = (EmotionAdapter) parent.getAdapter();
                if (position == adapter.getCount() - 1) {
                    // 删除
                    if (mCallback != null) {
                        mCallback.onDelete();
                    }
                } else {
                    // 插入表情
                    if (mCallback != null) {
                        mCallback.onInsert(adapter.getItem(position));
                    }
                }
            }
        });

        int start = position * EMOTION_PAGE_SIZE;
        List<String> tempEmotionList = Arrays.asList(Arrays.copyOfRange(KSEmotionUtil.sEmotionKeyArr, start, start + EMOTION_PAGE_SIZE));
        List<String> emotionList = new ArrayList<>();
        emotionList.addAll(tempEmotionList);
        emotionList.add("");

        gridView.setAdapter(new EmotionAdapter(emotionList));
        return gridView;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    class EmotionPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGridViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mGridViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mGridViewList.get(position));
            return mGridViewList.get(position);
        }
    }

    class EmotionAdapter extends BaseAdapter {
        private List<String> mDatas;

        public EmotionAdapter(List<String> datas) {
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public String getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.ks_item_emotion_keyboard, null);
            }

            ImageView iconIv = (ImageView) convertView;
            if (position == getCount() - 1) {
                iconIv.setImageResource(R.drawable.ks_emoji_delete);
                iconIv.setVisibility(VISIBLE);
            } else {
                String key = mDatas.get(position);
                if (TextUtils.isEmpty(key)) {
                    iconIv.setVisibility(INVISIBLE);
                } else {
                    iconIv.setImageResource(KSEmotionUtil.getImgByName(key));
                    iconIv.setVisibility(VISIBLE);
                }
            }

            return convertView;
        }
    }

    public interface Callback {
        void onDelete();
        void onInsert(String text);
    }
}

package com.bbk.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DensityUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ValidatorUtil;
import com.bbk.view.SearchPromptView;
import com.bbk.view.SearchRecommendView;
import com.bbk.view.SearchRecommendView.OnPlusClickListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;


public class SearchFragment extends BaseViewPagerFragment implements OnClickListener {
	
	private Context context;
	private SearchHistoryDao dao;
	private ListView mlistView;
	private View mView;
	private ArrayAdapter<String> adapter;
	private EditText searchEt;
	private ImageView searchImgBtn;
	private ImageView cleanImgBtn;
	private ImageView voiceImgBtn;
	private DataFlow dataFlow;
	private FrameLayout contentFrame;
	
	private SearchRecommendView recommendView;
	private SearchPromptView promptView;
	
	private SpeechRecognizer mIat;
	private RecognizerDialog mIatDialog;
	private HashMap<String, String> mIatResults = new HashMap<String, String>();
	
	private static int flag = 0;
	
	//搜索选项卡
	private TextView stypeText;
	private LinearLayout stypeLayout;
	private PopupWindow stypePopupWindow;
	private ImageView stypeArrowImg;
	private List<String> dataList;
	public static int stypeWay = 0;
	public final static String[] stypeArr = { "全网购","海外购"};
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String dataStr = (String) msg.obj;
				try {
					JSONObject data = new JSONObject(dataStr);
					JSONArray array = data.getJSONArray("content");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String lable = object.getString("label");
						Log.e("=====lable======", lable+"");
						dataList.add(lable);
					}
					if (mlistView.getAdapter()!=null) {
						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	};
	private View search_head;
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		return super.onCreateView(inflater, container, savedInstanceState);
		SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID + "=5768ef9e");
		mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
		dataFlow = new DataFlow(getActivity());
		if(null == mView) {
			
			dao = new SearchHistoryDao(getActivity());
			recommendView = new SearchRecommendView(getActivity(), null);
			recommendView.setOnPlusClickListener(new OnPlusClickListener() {
				@Override
				public void onPlusClickListener(View view, String str) {
					searchEt.setText(str);
				}
			});
			promptView = new SearchPromptView(getActivity(), null);
			
			mView = inflater.inflate(R.layout.fragment_search, null);
			search_head = mView.findViewById(R.id.search_head);
			initstateView();
			
			initView();
		}
		return mView;
	}
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getContext().getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    } 
	private void initstateView() {
		if (Build.VERSION.SDK_INT >=19) {
			search_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = search_head.getLayoutParams();
		layoutParams.height = result;
		search_head.setLayoutParams(layoutParams);
	}
//	@Override
//	public void lazyLoad() {
//	}

	@Override
	protected void loadLazyData() {
		flag = 0;
		recommendView.notifyData();
		stypeText.setText(stypeArr[stypeWay]);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadLazyData();
	}
	
	public void initView() {
		dataList = new ArrayList<String>();
		String str = "1";
		dataList.add(str);
		searchEt = (EditText) mView.findViewById(R.id.search_edit);
		cleanImgBtn = (ImageView) mView.findViewById(R.id.search_clean_img_btn);
		searchImgBtn = (ImageView) mView.findViewById(R.id.search_img);
		voiceImgBtn = (ImageView) mView.findViewById(R.id.search_voice_img_btn);
		mlistView = (ListView) mView.findViewById(R.id.mlistView);
		contentFrame = (FrameLayout) mView.findViewById(R.id.content_frame);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,dataList);
		contentFrame.addView(recommendView);
		
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
				String text = mlistView.getAdapter().getItem(i).toString();
				searchEt.setText(text);
				searchEt.setSelection(text.length());
				mlistView.setVisibility(View.GONE);
				doSearch();
			}
		});
		cleanImgBtn.setOnClickListener(this);
		searchImgBtn.setOnClickListener(this);
		voiceImgBtn.setOnClickListener(this);
		
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0) {
					searchEt.setSelection(s.length());
					cleanImgBtn.setVisibility(View.VISIBLE);
					voiceImgBtn.setVisibility(View.GONE);
					mlistView.setVisibility(View.VISIBLE);
					dataList.clear();
					getHttpData();

				} else {
					cleanImgBtn.setVisibility(View.INVISIBLE);
					voiceImgBtn.setVisibility(View.VISIBLE);
					mlistView.setVisibility(View.GONE);
				}
				
				if(s.length() >= 3 || ValidatorUtil.isContainsChinese(s.toString())) {
					contentFrame.removeAllViews();
					contentFrame.addView(promptView);
					promptView.getHttpData(s.toString(), 1);
				} else {
					contentFrame.removeAllViews();
					contentFrame.addView(recommendView);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
		            if (imm.isActive()) {  
		                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
		            }
		            doSearch();
		            return true;
				}
				return false;
			}
		});
		stypeText = (TextView) mView.findViewById(R.id.stype_text);
		stypeLayout = (LinearLayout) mView.findViewById(R.id.stype_layout);
		stypeLayout.setOnClickListener(this);
		stypeArrowImg = (ImageView) mView.findViewById(R.id.stype_img);
		mlistView.setAdapter(adapter);
		
	}
	
	public void doSearch() {
		String keyword = searchEt.getText().toString();
		
		if(keyword.length() <= 0) {
			Toast toast = Toast.makeText(getActivity(), "搜索内容为空", Toast.LENGTH_LONG );
			toast.show();
			return;
		}
		
		if (!dao.exsistHistory(keyword)) {
			dao.addHistory(keyword);
		}
		
		Intent intent = new Intent(getActivity(), ResultMainActivity.class);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_clean_img_btn:
			searchEt.setText("");
			break;
		case R.id.search_img:
			doSearch();
			break;
		case R.id.search_voice_img_btn:
			voiceSearch();
			break;
		case R.id.stype_layout:
			initStypePopupWindow();
			showAnimation(stypeArrowImg);
			stypePopupWindow.showAsDropDown(v, 0, 0);
			break;
		default:
			break;
		}
	}
	public void initStypePopupWindow() {
		View stypeView = inflateView(R.layout.popup_window_sreach_stype);
		stypePopupWindow = new PopupWindow(stypeView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		stypePopupWindow.setFocusable(true);
		stypePopupWindow.setTouchable(true);
		stypePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		stypePopupWindow.setOutsideTouchable(true);

		stypeView.findViewById(R.id.popup_window_stype_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stypePopupWindow.dismiss();
			}
		});
		final LinearLayout popupWindowStypeAddLayout = (LinearLayout) stypeView.findViewById(R.id.popup_window_stype_add_layout);
		for (int i = 0; i < stypeArr.length; i++) {
			View itemView = inflateView(R.layout.listview_item_stype);
			TextView itemText = (TextView) itemView.findViewById(R.id.item_text);
			View itemDivider = itemView.findViewById(R.id.item_divider);
			if (i != stypeArr.length - 1) {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemDivider.getLayoutParams();
				params.setMargins(DensityUtil.dip2px(getActivity(), 8), 0,DensityUtil.dip2px(getActivity(), 8), 0);
				itemDivider.setLayoutParams(params);
			}

			itemText.setText(stypeArr[i]);
			itemView.setTag(i + "");

			if (stypeWay == i) {
				itemText.setTextColor(Color.parseColor("#0098FF"));
			}

			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String clickIndexStr = v.getTag().toString();
					int clickIndex = Integer.parseInt(clickIndexStr);
					if (stypeWay != clickIndex) {
						TextView itemTextAA = (TextView) v.findViewById(R.id.item_text);
						itemTextAA.setTextColor(Color.parseColor("#0098FF"));

						View itemViewBB = popupWindowStypeAddLayout.getChildAt(stypeWay);
						TextView itemTextBB = (TextView) itemViewBB.findViewById(R.id.item_text);
						itemTextBB.setTextColor(Color.parseColor("#464646"));
						stypeWay = clickIndex;
						stypeText.setText(stypeArr[stypeWay]);
					}
					stypePopupWindow.dismiss();
				}
			});
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 40));
			popupWindowStypeAddLayout.addView(itemView,i, params);
		}
		stypePopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismisssAnimation(stypeArrowImg);
			}
		});
	}
	private void showAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f,
				180f);
		animator.setDuration(500);
		animator.start();
	}
	private void dismisssAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation",
				180f, 0f);
		animator.setDuration(500);
		animator.start();
	}
	private View inflateView(int id) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(id, null);
		return view;
	}
	public void voiceSearch() {
		mIatResults.clear();
		searchEt.setText("");
		setParam();
		mIatDialog = new RecognizerDialog(getActivity(), mInitListener);
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");    
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");    
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");    
		mIatDialog.setListener(mRecognizerDialogListener);
		mIatDialog.show();
	}
	
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		
		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		
		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}
	
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(getActivity().toString(), "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(getActivity(), "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			dealResult(results);
		}
		/**
		 * 识别回调错误.
		 */
		@Override
		public void onError(SpeechError error) {
			Toast.makeText(getActivity(), error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
		}

	};
	
	private void dealResult(RecognizerResult results) {
		String text = parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		
		searchEt.setText(resultBuffer.toString());
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(flag == 0) {
					doSearch();
					flag ++;
				}
			}
		}, 1000);
	}
	
	public String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();
	}
	private void getHttpData() {
		final String url = Constants.MAIN_BASE_URL_MOBILE+"searchAutoService/getAutoApp";
		String text = searchEt.getText().toString();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("key", text);
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						String dataStr = HttpUtil.getHttp(params, url, context);
						Message msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = dataStr;
						handler.sendMessage(msg);
						
					}
				}).start();
		
	}
	
}

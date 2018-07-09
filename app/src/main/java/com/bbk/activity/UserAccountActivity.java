package com.bbk.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bbk.PhotoPicker.utils.ImageCaptureManager;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.DataFragment;
import com.bbk.lubanyasuo.Luban;
import com.bbk.lubanyasuo.OnCompressListener;
import com.bbk.resource.Constants;
import com.bbk.util.DateUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.DialogUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.NumberUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.TencentLoginUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.CustomDatePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

public class UserAccountActivity extends BaseActivity implements OnClickListener, ResultEvent {

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private static final int NICKNAME = 4;// 昵称
	private String actionUrl = Constants.MAIN_BASE_URL_MOBILE + "newService/uploadHead";
	private static final int TAKE_PICTURE = 0x000000;

	private DataFlow dataFlow;
	private TextView signOutTv;
	private ImageButton goBackBtn;
	private File tempFile;
	private Bitmap bitmap;
	private LinearLayout muserimg, musername, msex, mbirthday, mphone, mpassword;
	private ImageView userImg;
	private String userID;
	private TextView mdata, mnickname, mphonenumber,sextext;
	private CustomDatePicker customDatePicker;
	private String thirdLogin;
	private TextView muserid;
	private ImageCaptureManager captureManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_account);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
		clearMemory();
		// MyApplication.getInstance().addActivity(this);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		goBackBtn = $(R.id.topbar_goback_btn);
		muserimg = $(R.id.muserimg);
		musername = $(R.id.musername);
		msex = $(R.id.msex);
		sextext = $(R.id.sextext);
		mbirthday = $(R.id.mbirthday);
		mphone = $(R.id.mphone);
		mpassword = $(R.id.mpassword);
		muserid = $(R.id.muserid);

		mdata = $(R.id.mdata);
		mnickname = $(R.id.mnickname);
		mphonenumber = $(R.id.mphonenumber);

		userImg = $(R.id.user_img);
		signOutTv = $(R.id.user_sign_out);
		signOutTv.setOnClickListener(this);
		muserimg.setOnClickListener(this);
		musername.setOnClickListener(this);
		msex.setOnClickListener(this);
		mbirthday.setOnClickListener(this);
		mpassword.setOnClickListener(this);
		goBackBtn.setOnClickListener(this);
		initDatePicker();
	}

	private void initData() {
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		String nickname1 = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "nickname");
		String brithday = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "brithday");
		String imgUrl = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "imgUrl");
		String userPhone = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userPhone");
		String gender = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "gender");
		if (gender.equals("1")) {
			sextext.setText("男");
		}else if(gender.equals("0")){
			sextext.setText("女");
		}
		muserid.setText(userID);
		mnickname.setText(nickname1);
		mdata.setText(brithday);
		mphonenumber.setText(userPhone);
		CircleImageView1.getImg(this, imgUrl, userImg);

	}

	private void updateUserInfo(String type, String value) {
		Map<String, String> params = new HashMap<>();
		params.put("type", type);
		params.put("value", value);
		params.put("userid", userID);
		dataFlow.requestData(1, "newService/updateUserInfo", params, this);
	}

	/*
	 * 从相册获取
	 */
	public void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * 从相机获取
	 */
	public void camera() {
		captureManager = new ImageCaptureManager(this);
		try {
			Intent intent = captureManager.dispatchTakePictureIntent();
			startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ActivityNotFoundException e) {
			// TODO No Activity Found to handle Intent
			e.printStackTrace();
		}
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		// 判断存储卡是否可以用，可用进行存储
//		if (hasSdcard()) {
//			intent.putExtra(MediaStore.EXTRA_OUTPUT,
//					Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "little_girl")));
//		}
//		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}



	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateHttpThread() {

	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveBitmap(Bitmap bitmap, String picName) {

		if (bitmap == null) {
			return;
		}

		makeRootDirectory("/sdcard/bbkFolder/");
		File file = new File("/sdcard/bbkFolder/", picName + ".png");
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
				fileOut.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(3, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			intent = new Intent();
			setResult(3, intent);
			finish();
			break;
		case R.id.muserimg:
			StatService.onEvent(UserAccountActivity.this, "updateusrimg", "更新用户头像:个人信息页面");
			new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(18,"拍照",
					ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							camera();
						}
					}).addSheetItem(18,"我的相册", ActionSheetDialog.SheetItemColor.Blue,
							new ActionSheetDialog.OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
//									if (ContextCompat.checkSelfPermission(UserAccountActivity.this,
//							                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//							                != PackageManager.PERMISSION_GRANTED)
//							        {
//							            ActivityCompat.requestPermissions(UserAccountActivity.this,
//							                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//							                    7);
//
//							        }else {
//							        	gallery();
//							        }
									gallery();
								}
							})
					.show();
			break;
		case R.id.musername:
			intent = new Intent(this, NickNameActivity.class);
			startActivityForResult(intent, NICKNAME);
			break;
		case R.id.msex:
			new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(18,"男",
					ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							updateUserInfo("u_sex", "1");
							sextext.setText("男");
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "gender","1");
							
						}
					}).addSheetItem(18,"女", ActionSheetDialog.SheetItemColor.Blue,
							new ActionSheetDialog.OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									updateUserInfo("u_sex", "0");
									sextext.setText("女");
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "gender","0");
								}
							})
					.show();
			break;
		case R.id.mbirthday:
			if (mdata.getText().toString().isEmpty()) {
				customDatePicker.show("1986-12-12");
			} else {
				customDatePicker.show(mdata.getText().toString());
			}

			break;

		case R.id.mpassword:
			intent = new Intent(this, UserNewPasswordActivity2.class);
			startActivity(intent);
			break;

		case R.id.user_sign_out:
			new AlertDialog(this).builder().setTitle("提示")
					.setMsg("确认退出帐号？")
					.setPositiveButton("确认", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//友盟登出
							DialogSingleUtil.show(UserAccountActivity.this,"退出中...");
							MobclickAgent.onProfileSignOff();
							StatService.onEvent(UserAccountActivity.this, "loginout", "退出登录:个人设置页面");
							Tencent mTencent = Tencent.createInstance(Constants.QQ_APP_ID, UserAccountActivity.this);
							mTencent.logout(getApplicationContext());
							//退出腾讯云通讯
							TencentLoginUtil.Loginout(getApplicationContext());
							//清除用户信息
							SharedPreferencesUtil.cleanShareData(getApplicationContext(), "userInfor");
							SharedPreferencesUtil.cleanShareData(getApplicationContext(), "isFirstClick");
							Intent intent = new Intent();
							setResult(2, intent);
//							DataFragment.login_remind.setVisibility(View.VISIBLE);
							DialogSingleUtil.dismiss(0);
							finish();
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			}).show();
			break;
		default:
			break;
		}

	}

	public void uploadFileThread(final File file) {
		Thread uploadFileThread = new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("userid", userID);
				String result;
				try {
					result = post(actionUrl, params, file);
					Message msg = Message.obtain();
					msg.what = 3;
					msg.obj = result;
					mHandler.sendMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		uploadFileThread.start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isFinishing()) {
				return;
			}
			switch (msg.what) {
			case 3:
				String result = msg.obj.toString();
				try {
					JSONObject object = new JSONObject(result);

					if (object.optString("status").equals("1")) {
						SharedPreferencesUtil.putSharedData(UserAccountActivity.this, "userInfor", "imgUrl",
								object.optString("content"));
						CircleImageView1.getImg(UserAccountActivity.this, object.optString("content"), userImg);
						Toast.makeText(getApplicationContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
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

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 *
	 * @param url
	 *            Service net address
	 * @param params
	 *            text content
	 * @param file
	 *            pictures
	 * @return String result of Service response
	 * @throws IOException
	 */
	public String post(String url, Map<String, String> params, File file) throws IOException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
		conn.connect();

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (file != null) {
			StringBuilder sb1 = new StringBuilder();
			sb1.append(PREFIX);
			sb1.append(BOUNDARY);
			sb1.append(LINEND);
			sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINEND);
			sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
			sb1.append(LINEND);
			outStream.write(sb1.toString().getBytes());

			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[512];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}

			is.close();
			outStream.write(LINEND.getBytes());

		}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		try {
			int res = conn.getResponseCode();
			if (res == 200) {
				InputStream in = conn.getInputStream();

				// ----------------------------------------
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder sb1 = new StringBuilder();
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb1.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return sb1.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "-1";
	}

	

	private void initDatePicker() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		String now = sdf.format(new Date());
		mdata.setText(now.split(" ")[0]);
		customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
			@Override
			public void handle(String time) { // 回调接口，获得选中的时间
				mdata.setText(time.split(" ")[0]);
				updateUserInfo("u_birthday", time.split(" ")[0]);
				SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "brithday",time.split(" ")[0]);
			}
		}, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
		customDatePicker.showSpecificTime(false); // 不显示时和分
		customDatePicker.setIsLoop(true); // 允许循环滚动
	}

	public void clearMemory() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Glide.get(getApplicationContext()).clearDiskCache();
//			}
//		}).start();

		Glide.get(this).clearMemory();
	}

	@Override
	protected void onDestroy() {
		clearMemory();
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
	            Cursor cursor = getContentResolver().query(uri,
	                    filePathColumn, null, null, null);
	            String picturePath;
	            if (cursor!= null) {
	            	 cursor.moveToFirst();
	 	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	 	            //picturePath就是图片在储存卡所在的位置
	 	            picturePath = cursor.getString(columnIndex);
	 	            cursor.close();
				}else{
					picturePath = uri.getPath();
				}
	           
				File file = new File(picturePath);
				if (file.exists()) {
					Luban.with(UserAccountActivity.this).load(file)
					.setCompressListener(new OnCompressListener() {

						@Override
						public void onSuccess(File file) {
							uploadFileThread(file);
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(Throwable e) {
							// TODO Auto-generated method stub

						}
					}).launch();
				}else{
					file.mkdir();
				}
				
//				crop(uri);
			}
			break;
		case PHOTO_REQUEST_CAMERA:
			try{
			if (captureManager == null) {
				captureManager = new ImageCaptureManager(this);
			}
			String path = captureManager.getCurrentPhotoPath();
				tempFile = new File(path);
				Luban.with(UserAccountActivity.this).load(tempFile)
						.setCompressListener(new OnCompressListener() {

							@Override
							public void onSuccess(File file) {
								uploadFileThread(file);
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError(Throwable e) {
								// TODO Auto-generated method stub

							}
						}).launch();
		} catch (Exception e) {
			e.printStackTrace();
		}
//			if (hasSdcard()) {
//				tempFile = new File(Environment.getExternalStorageDirectory(), "little_girl");
////				Uri uri = Uri.fromFile(tempFile);
////				File file = new File(uri.toString());
//				Luban.with(UserAccountActivity.this).load(tempFile)
//				.setCompressListener(new OnCompressListener() {
//
//					@Override
//					public void onSuccess(File file) {
//						uploadFileThread(file);
//					}
//
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//						// TODO Auto-generated method stub
//
//					}
//				}).launch();
//				crop(uri);
//			} else {
//				Toast.makeText(UserAccountActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
//			}
			break;
		case PHOTO_REQUEST_CUT:
			if (resultCode == RESULT_OK) {
				try {
//					uploadFileThread();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

			}
			break;
		case NICKNAME:
			if (data != null) {
				String nickname = data.getStringExtra("nickname");
				mnickname.setText(nickname);
				SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "nickname", nickname);
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			
			break;

		default:
			break;
		}
	}
}

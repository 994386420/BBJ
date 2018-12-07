package cn.kuaishang.kssdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.kuaishang.core.KSManager;
import cn.kuaishang.kssdk.model.BaseMessage;
import cn.kuaishang.kssdk.model.ImageMessage;
import cn.kuaishang.kssdk.model.TextMessage;
import cn.kuaishang.kssdk.model.VoiceMessage;
import cn.kuaishang.kssdk.util.BizConfig;
import cn.kuaishang.model.ModelDialogRecord;
import cn.kuaishang.util.FileUtil;
import cn.kuaishang.util.KSConstant;
import cn.kuaishang.util.KSKey;
import cn.kuaishang.util.StringUtil;

import static cn.kuaishang.util.StringUtil.getDateStr;

public class KSUIUtil {


	public static final String DEF_CHAR_SPLIT = "§";

	/**
	 * 键盘切换延时时间
	 */
	public static final int KEYBOARD_CHANGE_DELAY = 50;
	private static Handler sHandler = new Handler(Looper.getMainLooper());

	public static void runInThread(Runnable task) {
		new Thread(task).start();
	}

	public static void runInUIThread(Runnable task) {
		sHandler.post(task);
	}

	public static void runInUIThread(Runnable task, long delayMillis) {
		sHandler.postDelayed(task, delayMillis);
	}

	/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 打开键盘
	 * @param editText
	 */
	public static void openKeyboard(final EditText editText) {
		runInUIThread(new Runnable() {
			@Override
			public void run() {
				editText.requestFocus();
				editText.setSelection(editText.getText().toString().length());
				InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
			}
		}, 300);
	}

	/**
	 * 关闭activity中打开的键盘
	 * @param activity
	 */
	public static void closeKeyboard(Activity activity) {
		if (activity == null) {
			return;
		}
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight){
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			//KSLog.print("getSmallBitmap===filePath:"+filePath+"  width:"+options.outWidth+"  height:"+options.outHeight+"  size:"+options.inSampleSize);
			int maxSize = Math.max(options.outWidth,options.outHeight);
			if(maxSize>1024 && options.inSampleSize<2)
				options.inSampleSize = 2;

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(filePath, options);
			if(bm==null)return null;
			int degree = readPictureDegree(filePath);
			if(degree!=0){
				//KSLog.print("getSmallBitmap===degree:"+degree);
				bm = rotaingImageView(degree, bm);
			}

			if(options.inSampleSize==1)return bm;
			int width = bm.getWidth();
			int height = bm.getHeight();
			float scale = 1.0f;
			if(width>height)
				scale = scale*width/reqWidth;
			else
				scale = scale*height/reqHeight;

			Matrix matrix = new Matrix();
			float scaleWidth = 1/scale;
			float scaleHeight = 1/scale;
			//KSLog.print("getSmallBitmap===scale:"+scale+"  width:"+width+"  height:"+height+"  scaleWidth:"+scaleWidth+"  scaleHeight:"+scaleHeight);
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//			KSLog.print("getSmallBitmap===bitmap:"+bitmap+"  bm:"+bm);
//			if (bm != null && !bm.isRecycled())
//				bm.recycle();
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		//KSLog.print("------------reqWidth:"+reqWidth+"  reqHeight:"+reqHeight);
		//KSLog.print("------------width:"+width+"  height:"+height);

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			//KSLog.print("------------widthRatio:"+widthRatio+"  heightRatio:"+heightRatio);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 图片压缩
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				int degree = readPictureDegree(path);
				if(degree!=0){
					bitmap = rotaingImageView(degree, bitmap);
				}
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path  图片绝对路径
	 * @return degree 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 获取imageloader配置
	 * @param resId
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int resId){
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(resId)//设置图片在下载期间显示的图片
				.showImageForEmptyUri(resId)//设置图片Uri为空或是错误的时候显示的图片
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		return defaultOptions;
	}

	public static DisplayImageOptions getDisplayImageOptionsByCircular(int resId){
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(resId)//设置图片在下载期间显示的图片
				.showImageForEmptyUri(resId)//设置图片Uri为空或是错误的时候显示的图片
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(360))
				.build();
		return defaultOptions;
	}

	public static void scrollToBottom(final AbsListView absListView) {
		if (absListView != null) {
			if (absListView.getAdapter() != null && absListView.getAdapter().getCount() > 0) {
				absListView.post(new Runnable() {
					@Override
					public void run() {
						absListView.setSelection(absListView.getAdapter().getCount() - 1);
					}
				});
			}
		}
	}

	/**
	 * 获取消息类型
	 * @param message
	 * @return
	 */
	public static String getContentType(String message){
		if(message==null)return "text";
		String msgType = "text";
		try {
			if(message.startsWith("{") && message.endsWith("}")){
				JSONObject json = new JSONObject(message);
				msgType = json.getString("type");
			}
		} catch (Exception e) {
		}
		return msgType;
	}

	/**
	 * 获取图片url
	 * @param context
	 * @param message
	 * @param sendTime
	 * @return
	 */
	public static String getImageUrl(Context context, String message, Date sendTime){
		String url = "";
		try {
			if(message.startsWith("{") && message.endsWith("}")){
				JSONObject json = new JSONObject(message);
				String media_id=json.getString("fileName");
				if(!json.isNull("isLocal"))
					url = media_id;
				else
					//url = KSHttp.getFileHost()+"/upload/sdk/"+getDateStr(sendTime, "yyyy/MM/dd")+"/"+media_id;
					url = KSManager.getInstance(context).getFilePath()+"/upload/sdk/"+getDateStr(sendTime, "yyyy/MM/dd")+"/"+media_id;
			}
		} catch (Exception e) {
		}
		return url;
	}

	/**
	 * 获取语音url
	 * @param context
	 * @param message
	 * @param sendTime
	 * @return
	 */
	public static String getVoiceUrl(Context context, String message, Date sendTime){
		String url = "";
		try {
			if(message.startsWith("{") && message.endsWith("}")){
				JSONObject json = new JSONObject(message);
				String media_id=json.getString("fileName");
				if(!json.isNull("isLocal"))
					url = media_id;
				else
					url = KSManager.getInstance(context).getFilePath()+"/upload/sdk/"+getDateStr(sendTime, "yyyy/MM/dd")+"/"+media_id;
			}
		} catch (Exception e) {
		}
		return url;
	}

	public static String getSystemContent(String str){
		if(str==null)return "";
		str = replaceAll(str, "$", "├┤");
		str = replaceAll(str, "\\", "┼┽");
		String regex = "(┣[^┫^┣]*┫)";
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile(regex).matcher(str);
		while(m.find()){
			String key = m.group(0);
			key = key.substring(1,key.length()-1);//过滤前后的字符

			String[] msgPS=null;
			if(key.endsWith(DEF_CHAR_SPLIT)){
				key += " ";
			}
			if(key.indexOf(DEF_CHAR_SPLIT)!=-1){
				msgPS=key.split(DEF_CHAR_SPLIT);
			}else{
				msgPS=key.split(",");  //为支持旧版记录而保留的
			}
			msgPS[0]= BizConfig.newInstance().getLanguageValue(msgPS[0]);
			//KSUtil.print("轨迹后[msgPS]:"+msgPS[0]);

			String value=getReplaceMessage(msgPS);//消息多语言操作
			//KSUtil.print("轨迹后[value]:"+value);
			//KSUtil.print("value="+value);

			m.appendReplacement(sb, value);
		}
		m.appendTail(sb);
		return replaceAll(replaceAll(sb.toString(), "├┤", "$"), "┼┽", "\\");
	}

	public static String replaceAll(String strSc, String oldStr, String newStr) {
		int i = -1;
		while ((i = strSc.indexOf(oldStr)) != -1) {
			strSc = new StringBuffer(strSc.substring(0, i)).append(newStr)
					.append(strSc.substring(i + oldStr.length())).toString();
		}
		return strSc;
	}

	public static String getReplaceMessage(String[] messages){
		if(messages==null)return "";
		String str=messages[0];
		for(int index=1;index<messages.length;index++){
			str=str.replaceAll("\\{"+index+"\\}",messages[index]);
		}
		return str;
	}

	/**
	 * 判断是否有网络连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 打开Activity
	 * @param context
	 * @param data
	 * @param clazz
	 */
	public static void openActivity(Context context, Map<String,Object> data, Class<?> clazz){
		try{
			Intent intent = new Intent(context, clazz);
			if(data != null){
				Bundle bundle = new Bundle();
				bundle.putSerializable(KSKey.DATA, (Serializable) data);
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		}catch (Exception e) {
		}
	}

	/**
	 * 打开Activity(返回activity,并finish期间打开的activity)
	 * @param context
	 * @param data
	 * @param clazz
	 */
	public static void openActivityForClear(Context context,Map<String,Object> data,Class<?> clazz){
		try{
			Intent intent = new Intent(context, clazz);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			if(data != null){
				Bundle bundle = new Bundle();
				bundle.putSerializable(KSKey.DATA, (Serializable) data);
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		}catch (Exception e) {
		}
	}

	/**
	 * 打开照相机
	 * @param context
	 */
	public static File openCameraActivity(Context context){
		File file = null;
		try {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				file = new File(FileUtil.getCameraPath());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				((Activity)context).startActivityForResult(intent, KSKey.KEY_REQUESTCODE_CAMERA);
			} else {
				Toast.makeText(context, context.getString(R.string.ks_nosdcard), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
		}
		return file;
	}

	public static BaseMessage newMessage(Context context, ModelDialogRecord record){
		String recContent = record.getRecContent();
		String contentType = KSUIUtil.getContentType(recContent);
		if(BaseMessage.TYPE_CONTENT_IMAGE.equals(contentType)){
			//图片
			return newImageMessage(context, record);
		}else if(BaseMessage.TYPE_CONTENT_VOICE.equals(contentType)){
			//语音
			return newVoiceMessage(context, record);
		}else{
			//文字、表情
			return newTextMessage(record);
		}
	}

	private static TextMessage newTextMessage(ModelDialogRecord record){
		TextMessage message = new TextMessage();
		message.setContentType(BaseMessage.TYPE_CONTENT_TEXT);
		setMessageContent(message, record);
		return message;
	}

	private static ImageMessage newImageMessage(Context context, ModelDialogRecord record){
		ImageMessage message = new ImageMessage();
		message.setContentType(BaseMessage.TYPE_CONTENT_IMAGE);
		message.setImageUrl(KSUIUtil.getImageUrl(context, record.getRecContent(), StringUtil.stringToDateAndTime(record.getAddTime())));
		setMessageContent(message, record);
		return message;
	}

	private static VoiceMessage newVoiceMessage(Context context, ModelDialogRecord record){
		VoiceMessage message = new VoiceMessage();
		message.setContentType(BaseMessage.TYPE_CONTENT_VOICE);
		message.setVoiceUrl(KSUIUtil.getImageUrl(context, record.getRecContent(), StringUtil.stringToDateAndTime(record.getAddTime())));
		setMessageContent(message, record);
		return message;
	}

	private static void setMessageContent(BaseMessage message, ModelDialogRecord record){
		int recType = record.getRecType();
		if(recType == KSConstant.RECTYPE_CUSTOMER){
			message.setMessageType(BaseMessage.TYPE_MESSAGE_CUSTOMER);
		}else if(recType == KSConstant.RECTYPE_SYSMSG){
			message.setMessageType(BaseMessage.TYPE_MESSAGE_SYSTEM);
		}else if(recType == KSConstant.RECTYPE_EVALUATE){
			message.setMessageType(BaseMessage.TYPE_MESSAGE_EVALUATE);
		}
		message.setAddTime(record.getAddTime());
		message.setContent(record.getRecContent());
		message.setCustomerId(record.getCustomerId());
		message.setSenderName(record.getSenderName());
		message.setLocalId(record.getLocalId());
	}
}

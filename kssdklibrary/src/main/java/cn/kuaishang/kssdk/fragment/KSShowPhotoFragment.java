package cn.kuaishang.kssdk.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.activity.KSShowPhotoActivity;
import cn.kuaishang.kssdk.adapter.KSPhotoCache;
import cn.kuaishang.util.FileUtil;
import cn.kuaishang.util.StringUtil;

@SuppressLint("ValidFragment")
public class KSShowPhotoFragment extends Fragment {
	private Bitmap bm;
    private String url;
    private ImageView imageView;
    private LinearLayout progressView;
    private ProgressBar progressBar;
    private TextView progressText;
    private boolean needDownload = false;
    
    public KSShowPhotoFragment() {
	}
    
	public KSShowPhotoFragment(String url) {
    	this.url = url;
	}

	@Override
	public void onDestroyView() {
		String name = url.substring(url.lastIndexOf("/")+1);
		File file = new File(FileUtil.getCachePath()+name);
		if(file.exists()){
			String cacheKey = file.getPath();
			bm = KSUIUtil.getSmallBitmap(cacheKey,480,800);
			if(bm==null){
				FileUtil.deleteFile(file);
			}
		}else{
		}
		super.onDestroyView();
	}

    @SuppressLint("InlinedApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.zap_ol_showphoto, container, false);
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	/**
	 * 设置界面
	 * 
	 */
	private void initView() {
		try {
			if(StringUtil.isEmpty(url)){
				getActivity().finish();
				return;
			}
			imageView = (ImageView) getView().findViewById(R.id.imageView);
//			imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//				@Override
//				public void onPhotoTap(View view, float x, float y) {
//					KSShowPhotoActivity activity = (KSShowPhotoActivity) getActivity();
//					activity.checkStatusBar();
//				}
//			});
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					KSShowPhotoActivity activity = (KSShowPhotoActivity) getActivity();
					activity.checkStatusBar();
				}
			});
			progressView = (LinearLayout) getView().findViewById(R.id.progressView);
			progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
			progressText = (TextView) getView().findViewById(R.id.progressText);
			
			progressView.setVisibility(View.GONE);
			
			File file = new File(url);
	    	if(file.exists()){
	    		String cachKey = file.getPath();
	    		bm = KSPhotoCache.getInstance().get(cachKey);
	    		if(bm==null){
	    			bm = KSUIUtil.revitionImageSize(file.getPath());
	    			KSPhotoCache.getInstance().put(cachKey, bm);
	    		}
	    		imageView.setImageBitmap(bm);
	    	}else{
	    		String name = url.substring(url.lastIndexOf("/")+1);
	    		file = new File(FileUtil.getCachePath()+name);
	    		if(file.exists()){
	    			//ImageLoader.getInstance().displayImage("file://"+file.getPath(),imageView);
	    			String cachKey = file.getPath();
	    			bm = KSPhotoCache.getInstance().get(cachKey);
	        		if(bm==null){
	        			//bm = KSUIUtil.getSmallBitmap(file.getPath(),480,800);
	        			bm = KSUIUtil.revitionImageSize(file.getPath());
	        			KSPhotoCache.getInstance().put(cachKey, bm);
	        		}
	    			imageView.setImageBitmap(bm);
	    		}else{
	    			ImageLoader.getInstance().displayImage(url+"?size=200x200", imageView, KSUIUtil.getDisplayImageOptions(R.drawable.placeholder_image));
	    			needDownload = true;
	    		}
	    	}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 取得文件的大小
	 * 
	 * @param size
	 * @return
	 */
	private static String getSize(long size) {
		String pos = "";
		DecimalFormat decim = new DecimalFormat("0.00");
		double s = size / (1024.0 * 1024.0);
		if (s < 1) {
			s = size / 1024.0;
			pos = decim.format(s) + "KB";
		} else {
			pos = decim.format(s) + "MB";
		}
		return pos;
	}

	public void setProgressText(Long down, Long total){
		String downStr = getSize(down);
		String totalStr = getSize(total);
		progressText.setText("正在加载图片("+downStr+"/"+totalStr+")");
	}
	
	/**
	 * 初始化数据
	 * 
	 */
	public void downLoad() {
		needDownload = false;
		if(!needDownload)return;
		progressBar.setSecondaryProgress(0);
		progressView.setVisibility(View.VISIBLE);
		setProgressText(0l,0l);
		new AsyncTask<Void, Long, Long>(){
			@Override
			protected Long doInBackground(Void... params) {
				int downloadCount = 0; 
		        int currentSize = 0; 
		        long loadSize = 0; 
		        long totalSize = 0; 
		          
		        HttpURLConnection httpConnection = null;
		        InputStream is = null;
		        FileOutputStream fos = null;
		        try { 
		            httpConnection = (HttpURLConnection)new URL(url).openConnection();
		            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient"); 
		            if(currentSize > 0) { 
		                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-"); 
		            } 
		            httpConnection.setConnectTimeout(10000); 
		            httpConnection.setReadTimeout(20000); 
		            totalSize = httpConnection.getContentLength(); 
		            if (httpConnection.getResponseCode() == 404) { 
		                throw new Exception("fail!");
		            } 
		            is = httpConnection.getInputStream();
		            int index = url.lastIndexOf("/");
		    		String name = url.substring(index+1);
		            fos = new FileOutputStream(FileUtil.getCachePath()+name, false);
		            byte buffer[] = new byte[4096]; 
		            int readsize = 0;
		            long num=0;
		            while((readsize = is.read(buffer)) > 0){ 
		                fos.write(buffer, 0, readsize); 
		                loadSize += readsize; 
		                num = (int)loadSize*100/totalSize;
		                if((downloadCount == 0)||num-3>downloadCount){  
		                    downloadCount += 3; 
		    				publishProgress(num,loadSize,totalSize);
//		                    "正在下载，已完成"+num+"%"
		                }
		                Thread.sleep(100);
		            } 
		            publishProgress(100l,totalSize,totalSize);
		        }catch (Exception e) {
		        } finally { 
		            if(httpConnection != null) { 
		                httpConnection.disconnect(); 
		            } 
		            try {
			            if(is != null) { 
							is.close();
			            } 
		            } catch (IOException e) {
		            } 
		            try {
			            if(fos != null) { 
			            	fos.close();
			            } 
		            } catch (IOException e) {
		            } 
		        } 
				return loadSize;
			}
			
			@Override
			protected void onProgressUpdate(Long... progress) {
				Long num = progress[0];
				Long loadSize = progress[1];
				Long totalSize = progress[2];
				int index=(int) (long)StringUtil.getLong(num);
				progressBar.setSecondaryProgress(index);
				setProgressText(loadSize, totalSize);
			}
			
			@Override
			protected void onPostExecute(Long result) {
				super.onPostExecute(result);
				needDownload = false;
				progressView.setVisibility(View.GONE);
				String name = url.substring(url.lastIndexOf("/")+1);
	    		File file = new File(FileUtil.getCachePath()+name);
	    		if(file.exists()){
	    			String cacheKey = file.getPath();
	    			bm = KSUIUtil.getSmallBitmap(cacheKey,480,800);
	    			imageView.setImageBitmap(bm);
	    			KSPhotoCache.getInstance().put(cacheKey, bm);
	    			//通知媒体库(如果没有通知，相册中不能及时显示刚照的照片)
//	    			Uri uri = Uri.parse("file://"+cacheKey);   
//	    			getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
	    		}else{
	    		}
			}
		}.execute();
	}

	public void saveImage(){
		File file = new File(url);
		String name = url.substring(url.lastIndexOf("/")+1);
		String saveFile = FileUtil.getImagePath()+name;
    	if(file.exists()){
    		FileUtil.copyFile(file.getPath(), saveFile);
    		//发送通知
    		Uri uri = Uri.parse("file://"+saveFile);
			getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
			Toast.makeText(getActivity(),"图片已保存至"+FileUtil.getImagePath()+"文件夹", Toast.LENGTH_LONG).show();
    	}else{
    		file = new File(FileUtil.getCachePath()+name);
    		if(file.exists()){
    			FileUtil.copyFile(file.getPath(), saveFile);
    			//发送通知
        		Uri uri = Uri.parse("file://"+saveFile);
    			getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
				Toast.makeText(getActivity(),"图片已保存至"+FileUtil.getImagePath()+"文件夹", Toast.LENGTH_LONG).show();
    		}else{
				Toast.makeText(getActivity(),"图片未下载", Toast.LENGTH_LONG).show();
    			ImageLoader.getInstance().displayImage(url+"?size=200x200", imageView, KSUIUtil.getDisplayImageOptions(R.drawable.placeholder_image));
    			needDownload = true;
    		}
    	}
	}
}

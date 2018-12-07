/* 
 * This source code is the confidential and proprietary information of
 * Kuaishang(Xiamen). The user shall not, in whole or in part, modify, copy,
 * publish, disclose or make any use of this source code unless
 * specifically authorized in a written agreement with Kuaishang(Xiamen).
 */
package cn.kuaishang.kssdk.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 自定义任务进度提示框
 * Handler在子线程中不能创建
 * @author hjl
 *
 */
public class KsProgressDialog {
	
	private ProgressDialog builder;
	
	public KsProgressDialog(final Context context, CharSequence title, CharSequence message, boolean cancelable){
		builder = new ProgressDialog(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCanceledOnTouchOutside(false);
		if(cancelable){
			builder.setButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
		}
	}
	
	public void show(){
//		MainHandler.getInstance().post(new Runnable() {
//		    public void run() {
//		    }
//		}, 1);
		builder.show();
	}
	
	public void dismiss(){
		builder.dismiss();
	}
	
	public void setMessage(CharSequence message){
		builder.setMessage(message);
	}
}

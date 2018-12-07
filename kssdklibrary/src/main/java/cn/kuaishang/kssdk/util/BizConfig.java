/* 
 * This source code is the confidential and proprietary information of
 * Kuaishang(Xiamen). The user shall not, in whole or in part, modify, copy,
 * publish, disclose or make any use of this source code unless
 * specifically authorized in a written agreement with Kuaishang(Xiamen).
 */
package cn.kuaishang.kssdk.util;

import android.content.Context;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import cn.kuaishang.kssdk.R;

/**
 * 多语言显示数据
 * @author hjl
 *
 */
public class BizConfig {

	private static BizConfig instance=new BizConfig();
	private Properties bizLanguageValues = new Properties();
	
	private BizConfig(){}
	
	public static BizConfig newInstance() {
		return instance;
	}

	public void initBizConfit(Context context){
		InputStream is = null;
		try {
			Locale locale = context.getResources().getConfiguration().locale;
			if(locale.equals(Locale.US)){
				is=context.getResources().openRawResource(R.raw.zh);
			}else{
				is=context.getResources().openRawResource(R.raw.zh);
			}
			bizLanguageValues.load(is);
			is.close();
		} catch (Exception e) {
		}
	}

	 public String getLanguageValue(String key){
		String value=bizLanguageValues.getProperty(key);
		if(value==null)value="";
		return value;
	}
	

}

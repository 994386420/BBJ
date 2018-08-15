package com.bbk.util;

import android.content.Context;
import android.widget.ImageView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.youth.banner.loader.ImageLoader;


public class GlideImageGuanggaoLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .priority(Priority.NORMAL)
                .into(imageView);
    }
}

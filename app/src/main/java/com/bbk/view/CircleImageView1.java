package com.bbk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.io.ByteArrayOutputStream;

/**
 * 将图片变为圆形图片
 */
public class CircleImageView1 {

    public static void getImg(final Context cont, Object url, final ImageView img) {

        Glide.with(cont).load(url).asBitmap().centerCrop().placeholder(R.mipmap.logo_01).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(cont.getResources(), resource);

                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void getImg(final Context cont, Bitmap bitmap, final ImageView img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes=baos.toByteArray();

        Glide.with(cont).load(bytes).asBitmap().skipMemoryCache(true).centerCrop().placeholder(R.mipmap.logo_01).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(cont.getResources(), resource);

                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
                resource.recycle();
            }
        });
    }
    
    public static void getImg(final Context cont, int url, final ImageView img) {

        Glide.with(cont).load(url).asBitmap().skipMemoryCache(true).centerCrop().placeholder(R.mipmap.logo_01).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(cont.getResources(), resource);

                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
                resource.recycle();
            }
        });
    }


    public static void getImgBid(final Context cont, Object url, final ImageView img) {

        Glide.with(cont).load(url).asBitmap().centerCrop().placeholder(R.mipmap.bj_05).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(cont.getResources(), resource);

                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}

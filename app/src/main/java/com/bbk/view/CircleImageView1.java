package com.bbk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * 将图片变为圆形图片
 */
public class CircleImageView1 {

    /***
     * Glide中的图片缓存key的生成是通过一个散列算法来实现的，所以很难手动去删除指定的图片缓存
     * Glide的图片缓存都有对应的唯一标识符，如果是相同的，就不加载调用缓存
     * 不过改变标识符很困难，所以Glide提供signature()方法，来附加一个数据到缓存key中
     * 如果链接是文件，就用StringSignature，
     * 比如.signature(nre StringSignature(yourVersionMetadata)).
     * 如果链接是多媒体，就用MediaStoreSignature，
     * 比如.signature(new MediaStoreSignature(mimeType, dateModified, orientation))
     * @param cont
     * @param url
     * @param img
     */
    public static void getImg1(final Context cont, Object url, final ImageView img) {

        Glide.with(cont).load(url).asBitmap().signature(new StringSignature(UUID.randomUUID().toString())).centerCrop().placeholder(R.mipmap.logo_01).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(cont.getResources(), resource);

                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
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

package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bbk.view.MyViewPager;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DesPictureActivity extends BaseActivity {

    private ViewPager myViewPager;
    private ArrayList<String> list = new ArrayList<>();
//    private ArrayList<String> des = new ArrayList<>();
    private int position;
    private PhotoViewAttacher attacher;
    private Context context;
    private VideoView mvideoview;
    private int videoposition = -1;
    private ImageView mplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des_pictrue);
        myViewPager = (MyViewPager) findViewById(R.id.activity_des_picture_viewPager);
//        x.view().inject(this);
        context = DesPictureActivity.this;
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
//        des = intent.getStringArrayListExtra("des");
        position = intent.getIntExtra("position", 0);
        myViewPager.setAdapter(new MyAdapter());
        myViewPager.setCurrentItem(position);
        myViewPager.setOffscreenPageLimit(9);
        myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mvideoview!= null){
                    mvideoview.pause();
                    MediaController controller = new MediaController(DesPictureActivity.this);
                    mvideoview.setMediaController(controller);
                    if (mplay!= null){
                        if (position == videoposition){
                            mplay.setVisibility(View.VISIBLE);
                        } else {
                            mplay.setVisibility(View.GONE);
                        }
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Glide.with(context).onStart();
            if (list.size() <= 0) {
                return super.instantiateItem(container, position);
            } else {
                final View view = LayoutInflater.from(context).inflate(R.layout.activity_des_pictrue_img, container, false);
                PhotoView imageView = (PhotoView) view.findViewById(R.id.activity_des_img);
//                TextView tv = (TextView) view.findViewById(R.id.activity_des_img_text);
//                tv.setVisibility(View.VISIBLE);
//                if (des != null && des.size() > 0) {
//                    String str = des.get(position);
//                    if (!TextUtils.isEmpty(str))
//                        tv.setText(str);
//                } else {
//                    tv.setVisibility(View.GONE);
//                }
                if (list != null) {
                    if (list.get(position).contains(".mp4")) {
                        videoposition = position;
                        mplay = (ImageView) view.findViewById(R.id.mplay);
                        mvideoview = (VideoView) view.findViewById(R.id.mvideoview);
                        mvideoview.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
//                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(list.get(position), MediaStore.Video.Thumbnails.MINI_KIND);
//                    mvideoview.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        mvideoview.setMediaController(new MediaController(DesPictureActivity.this));
                        mplay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mvideoview.start();
                                mplay.setVisibility(View.GONE);
                            }
                        });
                        if (list.get(position).contains("http")) {
                            Uri uri = Uri.parse(list.get(position));
                            mvideoview.setVideoURI(uri);
                        } else {
                            mvideoview.setVideoPath(list.get(position));
                        }
                        mvideoview.start();
                        mvideoview.requestFocus();
                        mvideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mplay.setVisibility(View.VISIBLE);
                            }
                        });
                        mvideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {

                            }
                        });
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        if ("add".equals(list.get(position))) {
                            Glide.with(DesPictureActivity.this).load(R.mipmap.icon_addpic_unfocused)
                                    .into(imageView);
                        } else {
                            String s = list.get(position);
                            Glide.with(DesPictureActivity.this).
                                    load(list.get(position))
                                    .into(imageView);
                        }
                    }

                }
                container.addView(view);
                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        finish();
                    }
                });
                return view;
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Glide.with(context).onStop();


        }
    }


}

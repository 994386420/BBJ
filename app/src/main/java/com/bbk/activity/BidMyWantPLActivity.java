package com.bbk.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.PhotoPicker.PhotoPicker;
import com.bbk.PhotoPicker.PhotoPreview;
import com.bbk.PhotoPicker.utils.ImageCaptureManager;
import com.bbk.adapter.MyGossipGirdAdapter;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.lubanyasuo.Luban;
import com.bbk.lubanyasuo.OnCompressListener;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BidMyWantPLActivity extends BaseActivity implements ResultEvent {

    private MyGridView mgridview;
    private MyGossipGirdAdapter adapter;
    private List<String> list;
    private List<String> litlelist;
    private ImageCaptureManager captureManager;
    final List<File> list1 = new ArrayList<>();
    private int length = 0;
    private Toast toast;
    private DataFlow6 dataFlow;
    private String id;
    private MaterialRatingBar mratingbar;
    private TextView mprice,mtitle,msend;
    private ImageView mimg;
    private EditText mpltext;
    private String biduserid;
    private ImageView topbar_goback_btn;
    private String pinglun = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_my_want_pl);
        id = getIntent().getStringExtra("id");
//        id = "2";
        dataFlow = new DataFlow6(this);
        initView();
        initData();
    }

    private void initView() {
        list = new ArrayList<>();
        litlelist = new ArrayList<>();
        list.add("add");
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mimg = (ImageView) findViewById(R.id.mimg);
        mprice = (TextView) findViewById(R.id.mprice);
        mtitle = (TextView) findViewById(R.id.mtitle);
        mpltext = (EditText) findViewById(R.id.mpltext);
        msend = (TextView) findViewById(R.id.msend);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(pinglun)){
                    loadData();
                }else {
                    ToastUtil("为了保护你的消费权益，请于24小时后进行评论。");
                }

            }
        });
        mratingbar = (MaterialRatingBar) findViewById(R.id.mratingbar);
        mratingbar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {

            }
        });
        mgridview = (MyGridView)findViewById(R.id.mgridview);
        adapter = new MyGossipGirdAdapter(this, list);
        mgridview.setAdapter(adapter);
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if ("add".equals(list.get(position))) {
                    ActionSheetDialog dialog = new ActionSheetDialog(BidMyWantPLActivity.this).builder().setCancelable(true)
                            .setCanceledOnTouchOutside(true).addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            photo();
                                        }
                                    })
                            .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
//											Intent intent = new Intent(MyGossipActivity.this, TestPicActivity.class);
//											startActivity(intent);
                                            PhotoPicker.builder()
                                                    .setPhotoCount(4 - list.size())
                                                    .setGridColumnCount(3)
                                                    .setShowGif(true)
                                                    .start(BidMyWantPLActivity.this);


                                        }
                                    })
                            ;
                    dialog.show();

                } else {
                    litlelist.clear();
                    litlelist.addAll(list);
                    if ("add".equals(list.get(litlelist.size() - 1))) {
                        litlelist.remove(litlelist.size() - 1);
                    }
                    Intent Intent = new Intent(BidMyWantPLActivity.this, DesPictureActivity.class);
                    Intent.putStringArrayListExtra("list", (ArrayList<String>) litlelist);
                    Intent.putExtra("position", position);
                    startActivity(Intent);
                }
            }
        });
    }
    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",id);
        dataFlow.requestData(1, "bid/queryFinishBid", paramsMap, this,true);
    }
    private void loadData() {
            DialogSingleUtil.show(this);
            if ("add".equals(list.get(list.size() - 1))) {
                length = list.size() - 1;
            } else {
                length = list.size();
            }
//                    if (isnotshenhe){

            if (length == 0){
                initsend();
            }else {
                list1.clear();
                downloadimg();
            }
    }
    public void photo() {
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
    }

    private void downloadimg(){
        for (int i = 0; i < length; i++) {
            final int j = i;
            Glide.with(this).load(list.get(i)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    try {
                        File file = createImageFile(j);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        lubanyasuo(file,j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void lubanyasuo(File file,final int i){
        if (file.exists()) {
            Luban.with(this).load(file).setFilename(i+"JPEG_")
                    .setCompressListener(new OnCompressListener() {

                        @Override
                        public void onSuccess(File file) {
                            list1.add(file);
                            Collections.sort(list1);
                            if (list1.size() == length) {
                                Log.e("==========",list1+"");
                                initsend();
                            }
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
        } else {
            file.mkdir();
        }
    }
    private File createImageFile(int j) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = j+"JPEG_"+ timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                Log.e("TAG", "Throwing Errors....");
                throw new IOException();
            }
        }

        File image = new File(storageDir, imageFileName);
        return image;
    }

    public void ToastUtil(String s){
        if (toast!= null){
            toast.cancel();
        }
        toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }


    private void initsend() {
        final HashMap<String, String> params = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        if (userID.equals(biduserid)){
            params.put("role", "1");
        }else {
            params.put("role", "-1");
        }
        params.put("type", "3");
        params.put("userid", userID);
        params.put("title", mpltext.getText().toString());
        params.put("fbid", id);
        float numStars1 = mratingbar.getRating();
        params.put("number", numStars1+"");

        new Thread(new Runnable() {

            @Override
            public void run() {
                String post;
                try {
                    String actionUrl = Constants.MAIN_BASE_URL_MOBILE+"bid/insertPinglun";
                    post = post(actionUrl, params, list1);
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.obj = post;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void DeleteImage(String imgPath) {
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=?",
                new String[] { imgPath }, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = this.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                for (int i = 0; i < list1.size(); i++) {
                    DeleteImage(list1.get(i).getPath());
                }
                String post = msg.obj.toString();
                try {
                    JSONObject object = new JSONObject(post);
                    if (object.optInt("status") <= 0) {
                        Toast.makeText(BidMyWantPLActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BidMyWantPLActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BidMyWantPLActivity.this, BidListDetailActivity.class);
                        intent.putExtra("status", "0");
                        startActivity(intent);
                    }
                    DialogSingleUtil.dismiss(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    DialogSingleUtil.dismiss(0);
                }
            }
        }

        ;
    };

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url    Service net address
     * @param params text content
     * @param files  pictures
     * @return String result of Service response
     * @throws IOException
     */
    public String post(String url, Map<String, String> params, List<File> files) throws IOException {
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
        if (files != null) {
            for (File file : files) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                Log.e("===filename====",""+file.getName());
                sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\"" + file.getName() + "\""
                        + LINEND);
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
            // TODO: handle exception
        }
        return "-1";
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1) {

                switch (requestCode) {
                    case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                        try {
                            if (captureManager == null) {
                                captureManager = new ImageCaptureManager(this);
                            }
                            String path = captureManager.getCurrentPhotoPath();
                            list.add(list.size() - 1, path);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case PhotoPicker.REQUEST_CODE:
                        addimage(data);
                        break;
                    case PhotoPreview.REQUEST_CODE:
                        addimage(data);
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    private void addimage(Intent data) {
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            list.remove(list.size() - 1);
            list.addAll(photos);
            if (list.size() < 9) {
                list.add("add");
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            JSONObject object = new JSONObject(content);
            String title = object.optString("title");
            String img = object.optString("img");
            String bidprice = object.optString("bidprice");
            biduserid = object.optString("userid");
            pinglun = object.optString("pinglun");
            mtitle.setText(title);
            mprice.setText("￥"+bidprice);
            Glide.with(this).load(img).placeholder(R.mipmap.zw_img_300).into(mimg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.bbk.shopcar;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ShopOrderBean;
import com.bbk.PhotoPicker.PhotoPicker;
import com.bbk.PhotoPicker.PhotoPreview;
import com.bbk.PhotoPicker.utils.ImageCaptureManager;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.BidMyPlActivity;
import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.MyGossipGirdAdapter;
import com.bbk.adapter.ShopOrderWaiCengAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.lubanyasuo.Luban;
import com.bbk.lubanyasuo.OnCompressListener;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.logg.Logg;

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
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MyWantPLActivity extends BaseActivity implements ResultEvent {

    @BindView(R.id.data_head)
    View dataHead;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.mimg)
    ImageView mimg;
    @BindView(R.id.mtitle)
    TextView mtitle;
    @BindView(R.id.mratingbar)
    MaterialRatingBar mratingbar;
    @BindView(R.id.mpltext)
    EditText mpltext;
    @BindView(R.id.mgridview)
    MyGridView mgridview;
    @BindView(R.id.msend)
    TextView msend;
    private MyGossipGirdAdapter adapter;
    private List<String> list;
    private List<String> litlelist;
    private ImageCaptureManager captureManager;
    final List<File> list1 = new ArrayList<>();
    private int length = 0;
    private Toast toast;
    private DataFlow6 dataFlow;
    private String id,ordernum,dianpuid,img;
    private String biduserid;
    private String pinglun = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_pl_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        id = getIntent().getStringExtra("id");
        mtitle.setText(getIntent().getStringExtra("title"));
        ordernum = getIntent().getStringExtra("ordernum");
        dianpuid = getIntent().getStringExtra("dianpuid");
        img = getIntent().getStringExtra("img");
        Glide.with(this).load(getIntent().getStringExtra("img")).placeholder(R.mipmap.zw_img_300).into(mimg);
        dataFlow = new DataFlow6(this);
        initView();
//        initData();
    }

    private void initView() {
        titleText.setText("发表评论");
        list = new ArrayList<>();
        litlelist = new ArrayList<>();
        list.add("add");
        mratingbar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                Log.e("------------", "当前的评价等级：" + rating);
            }
        });
        adapter = new MyGossipGirdAdapter(this, list);
        mgridview.setAdapter(adapter);
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if ("add".equals(list.get(position))) {
                    ActionSheetDialog dialog = new ActionSheetDialog(MyWantPLActivity.this).builder().setCancelable(true)
                            .setCanceledOnTouchOutside(true).addSheetItem(18, "拍照", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            photo();
                                        }
                                    })
                            .addSheetItem(18, "我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
//											Intent intent = new Intent(MyGossipActivity.this, TestPicActivity.class);
//											startActivity(intent);
                                            PhotoPicker.builder()
                                                    .setPhotoCount(4 - list.size())
                                                    .setGridColumnCount(3)
                                                    .setShowGif(true)
                                                    .start(MyWantPLActivity.this);


                                        }
                                    });
                    dialog.show();

                } else {
                    litlelist.clear();
                    litlelist.addAll(list);
                    if ("add".equals(list.get(litlelist.size() - 1))) {
                        litlelist.remove(litlelist.size() - 1);
                    }
                    Intent Intent = new Intent(MyWantPLActivity.this, DesPictureActivity.class);
                    Intent.putStringArrayListExtra("list", (ArrayList<String>) litlelist);
                    Intent.putExtra("position", position);
                    startActivity(Intent);
                }
            }
        });
    }

    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid", id);
        dataFlow.requestData(1, "bid/queryFinishBid", paramsMap, this, true);
    }

    private void loadData() {
        DialogSingleUtil.show(this);
        if ("add".equals(list.get(list.size() - 1))) {
            length = list.size() - 1;
        } else {
            length = list.size();
        }
//                    if (isnotshenhe){

        if (length == 0) {
            initsend();
//            insertPinlunr();
        } else {
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

    private void downloadimg() {
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
                        lubanyasuo(file, j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void lubanyasuo(File file, final int i) {
        if (file.exists()) {
            Luban.with(this).load(file).setFilename(i + "JPEG_")
                    .setCompressListener(new OnCompressListener() {

                        @Override
                        public void onSuccess(File file) {
                            list1.add(file);
                            Collections.sort(list1);
                            if (list1.size() == length) {
                                Log.e("==========", list1 + "");
                                initsend();
//                                insertPinlunr();
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
        String imageFileName = j + "JPEG_" + timeStamp + ".jpg";
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

    public void ToastUtil(String s) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    //spid（商品id）content（内容）userid    star（分数）
    private void initsend() {
        final HashMap<String, String> params = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        params.put("userid", userID);
        params.put("content", mpltext.getText().toString());
        params.put("spid", id);
        float numStars1 = mratingbar.getRating();
        params.put("star", numStars1 + "");
        params.put("ordernum", ordernum);
        params.put("dianpuid",dianpuid);

        new Thread(new Runnable() {

            @Override
            public void run() {
                String post;
                try {
                    String actionUrl = BaseApiService.Base_URL + "mallService/insertPinlun";
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
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
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
                    Log.i("======", object + "=======");
                    if (object.optInt("status") <= 0) {
                        StringUtil.showToast(MyWantPLActivity.this, object.optString("errmsg"));
                    } else {
                        StringUtil.showToast(MyWantPLActivity.this, "评论成功");
                        Intent intent = new Intent(MyWantPLActivity.this, MyPlActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("img", img);
                        startActivity(intent);
                        DialogSingleUtil.dismiss(0);
                        finish();
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
        String BOUNDARY = UUID.randomUUID().toString();
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
                Log.e("===filename====", "" + file.getName());
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
        super.onActivityResult(requestCode, resultCode, data);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.title_back_btn, R.id.msend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.msend:
                loadData();
                break;
        }
    }


    private void insertPinlunr() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        Map<String, String>  params = new HashMap<String, String>();
        params.put("userid", userID);
        params.put("content", mpltext.getText().toString());
        params.put("spid", id);
        float numStars1 = mratingbar.getRating();
        params.put("star", numStars1 + "");
        params.put("ordernum", ordernum);
        params.put("dianpuid",dianpuid);
        RetrofitClient.getInstance(this).createBaseApi().insertPinlun(
                params, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                    StringUtil.showToast(MyWantPLActivity.this, "评论成功");
                                    Intent intent = new Intent(MyWantPLActivity.this, MyPlActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("img", img);
                                    startActivity(intent);
                                    DialogSingleUtil.dismiss(0);
                                    finish();
                            }else {
                                StringUtil.showToast(MyWantPLActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(MyWantPLActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(MyWantPLActivity.this, e.message);
                    }
                });
    }

}

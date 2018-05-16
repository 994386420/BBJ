package com.bbk.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.bbk.util.StringUtil;
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

/**
 *
 * 发镖_01_填写
 */

public class BidFbActivity extends BaseActivity implements ResultEvent{
    private DataFlow6 dataFlow;
    private View data_head;
    private EditText mname,mcount,mprice,mdetail;
    private MyGridView mgridview;
    private TextView mcommit;
    private RadioGroup mradioGroup;
    private Toast toast;
    private MyGossipGirdAdapter adapter;
    private List<String> list;
    private List<String> litlelist;
    private ImageCaptureManager captureManager;
    final List<File> list1 = new ArrayList<>();
    private int length = 0;
    private ScrollView mscrollview;
    private ImageView magrement;
    private boolean isagrement = true;
    private ImageView topbar_goback_btn;
    private TextView mStartTime;//发镖时间

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bid);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);

        initView();
    }


    private void initView() {
        list = new ArrayList<>();
        litlelist = new ArrayList<>();
        list.add("add");
        topbar_goback_btn = (ImageView)findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
//                SharedPreferencesUtil.putSharedData(BidFbActivity.this, "Bidhomeactivty", "type", "0");
                Intent intent = new Intent(BidFbActivity.this, BidHomeActivity.class);
                setResult(1,intent);
                finish();
            }
        });
//        magrement = (ImageView) findViewById(R.id.magrement);
//        magrement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isagrement){
//                    isagrement = false;
//                    magrement.setImageResource(R.mipmap.bj_09_01);
//                    mcommit.setBackgroundColor(Color.parseColor("#999999"));
//                    mcommit.setClickable(false);
//                }else {
//                    isagrement = true;
//                    magrement.setImageResource(R.mipmap.bj_09_02);
//                    mcommit.setBackgroundColor(Color.parseColor("#b40000"));
//                    mcommit.setClickable(true);
//                }
//            }
//        });
        mStartTime = findViewById(R.id.mstart_time);
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        mStartTime.setText(format.format(date));
        mscrollview = (ScrollView)findViewById(R.id.mscrollview);
        mname = (EditText)findViewById(R.id.mname);
        mcount = (EditText)findViewById(R.id.mcount);
        mprice = (EditText)findViewById(R.id.mprice);
        mdetail = (EditText)findViewById(R.id.mdetail);
        mgridview = findViewById(R.id.mgridview);
        mcommit = (TextView)findViewById(R.id.mcommit);
        mradioGroup = (RadioGroup)findViewById(R.id.mradioGroup);
        mradioGroup.check(R.id.mbtn1);
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        adapter = new MyGossipGirdAdapter(this, list);
        mgridview.setAdapter(adapter);
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if ("add".equals(list.get(position))) {
                    ActionSheetDialog dialog = new ActionSheetDialog(BidFbActivity.this).builder().setCancelable(true)
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
                                            PhotoPicker.builder()
                                                    .setPhotoCount(4 - list.size())
                                                    .setGridColumnCount(3)
                                                    .setShowGif(true)
                                                    .start(BidFbActivity.this);


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
                    Intent Intent = new Intent(BidFbActivity.this, DesPictureActivity.class);
                    Intent.putStringArrayListExtra("list", (ArrayList<String>) litlelist);
                    Intent.putExtra("position", position);
                    startActivity(Intent);
                }
            }
        });

    }
    public void photo() {
        captureManager = new ImageCaptureManager(BidFbActivity.this);
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
    private void loadData() {
        if (TextUtils.isEmpty(mname.getText().toString())){
            StringUtil.showToast(this,"名称不能为空");
        }else if (TextUtils.isEmpty(mprice.getText().toString())){
            StringUtil.showToast(this,"单价不能为空");
        }
        else {
            DialogSingleUtil.show(BidFbActivity.this,"我要中...");
            if ("add".equals(list.get(list.size() - 1))) {
                length = list.size() - 1;
            } else {
                length = list.size();
            }
            if (length == 0){
                initsend();
            }else {
                list1.clear();
                downloadimg();
            }

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
            Luban.with(BidFbActivity.this).load(file).setFilename(i+"JPEG_")
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
        toast = Toast.makeText(BidFbActivity.this,s,Toast.LENGTH_LONG);
        toast.show();
    }
    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = this.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    private void initstateView() {
        if (Build.VERSION.SDK_INT >= 19) {
            data_head.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight();
        ViewGroup.LayoutParams layoutParams = data_head.getLayoutParams();
        layoutParams.height = result;
        data_head.setLayoutParams(layoutParams);
        ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(this.getWindow(),true);
    }
    private void initsend() {
        final HashMap<String, String> params = new HashMap<String, String>();
        RadioButton radioButton = (RadioButton)findViewById(mradioGroup.getCheckedRadioButtonId());
        String mins = radioButton.getText().toString();
        //发镖改回24，48，72
        if (mins.contains("24")){
            mins = "24";
        }else if (mins.contains("48")){
            mins = "48";
        }else {
            mins = "72";
        }
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        String openID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "openID");
//        Log.i("发镖信息openid",openID+"==========");
        params.put("userid", userID);
        params.put("type", "1");
        params.put("mins", mins);
        params.put("title",mname.getText().toString());
        params.put("price",mprice.getText().toString());
        //发镖数量默认不填写为1
        if (mcount.getText().toString() != null){
            params.put("number",mcount.getText().toString());
        }else {
            params.put("number","1");
        }
        params.put("extra",mdetail.getText().toString());
        params.put("openid",openID);

        new Thread(new Runnable() {

            @Override
            public void run() {
                String post;
                try {
                    String actionUrl = Constants.MAIN_BASE_URL_MOBILE+"bid/insertFabiao";
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
                        StringUtil.showToast(BidFbActivity.this, object.optString("errmsg"));
                    } else {
                        StringUtil.showToast(BidFbActivity.this, "发镖成功");
                        mname.setText("");
                        mprice.setText("");
                        mcount.setText("");
                        mdetail.setText("");
                        mradioGroup.check(R.id.mbtn1);
                        list.clear();
                        list.add("add");
                        litlelist.clear();
                        list1.clear();
                        adapter.notifyDataSetChanged();
                        mscrollview.fullScroll(ScrollView.FOCUS_UP);
                        Intent intent = new Intent(BidFbActivity.this, BidListDetailActivity.class);
                        intent.putExtra("status", "1");
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
    public void IntentResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1) {

                switch (requestCode) {
                    case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                        try {
                            if (captureManager == null) {
                                captureManager = new ImageCaptureManager(BidFbActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult(requestCode, resultCode, data);
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {

    }
}

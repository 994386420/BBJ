package com.bbk.activity;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.PhotoPicker.PhotoPicker;
import com.bbk.PhotoPicker.PhotoPreview;
import com.bbk.PhotoPicker.utils.ImageCaptureManager;
import com.bbk.adapter.MyGossipGirdAdapter;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.lubanyasuo.Luban;
import com.bbk.lubanyasuo.OnCompressListener;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MyGossipActivity extends BaseActivity implements OnClickListener, ResultEvent {
    private ImageButton topbar_goback_btn;
    private EditText mtitle, mcontent;
    private GridView noScrollgridview;
    private MyGossipGirdAdapter adapter;
    private TextView msend, mydraft;
    private DataFlow dataFlow;
    private String actionUrl = Constants.MAIN_BASE_URL_MOBILE + "newService/insertBaoliao";
    final List<File> list1 = new ArrayList<>();
    private String userID;
    private EditText murl;
    private int po;
    private boolean isDraft = false;
    private List<String> list;
    private List<String> litlelist;
    private ImageCaptureManager captureManager;
    private int length = 0;
    private boolean isnotshenhe = false;
    private boolean hasvideo = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gossip);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow(this);
        initVeiw();
        initData();
        if (getIntent().getStringExtra("dtime") != null) {
            try {
                initintentData();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void initVeiw() {
        list = new ArrayList<>();
        litlelist = new ArrayList<>();
        list.add("add");
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);

        mcontent = (EditText) findViewById(R.id.mcontent);
        mtitle = (EditText) findViewById(R.id.mtitle);
        murl = (EditText) findViewById(R.id.murl);
        msend = (TextView) findViewById(R.id.msend);
        mydraft = (TextView) findViewById(R.id.mydraft);

        topbar_goback_btn.setOnClickListener(this);
        msend.setOnClickListener(this);
        mydraft.setOnClickListener(this);
    }

    private void initData() {
        adapter = new MyGossipGirdAdapter(this, list);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if ("add".equals(list.get(position))) {
                    ActionSheetDialog dialog = new ActionSheetDialog(MyGossipActivity.this).builder().setCancelable(true)
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
                                                    .setPhotoCount(10 - list.size())
                                                    .setGridColumnCount(3)
                                                    .setShowGif(true)
                                                    .start(MyGossipActivity.this);


                                        }
                                    })
                            ;
                    hasvideo = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).contains(".mp4")){
                            hasvideo = true;
                            break;
                        }
                    }
                    if (!hasvideo){
                        dialog.addSheetItem("我的视频", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent();
                                        intent.setType("video/*.mp4");
//                                        intent.setType("video/*.mp4");
                                        intent.setAction(Intent.ACTION_PICK);
//                                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        startActivityForResult(intent, 5);
                                        Toast.makeText(MyGossipActivity.this, "请选择小于10M的文件", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    dialog.show();

                } else {
                    litlelist.clear();
                    litlelist.addAll(list);
                    if ("add".equals(list.get(litlelist.size() - 1))) {
                        litlelist.remove(litlelist.size() - 1);
                    }
                    Intent Intent = new Intent(MyGossipActivity.this, DesPictureActivity.class);
                    Intent.putStringArrayListExtra("list", (ArrayList<String>) litlelist);
                    Intent.putExtra("position", position);
                    startActivity(Intent);
                }
            }
        });
    }
    public  void recordVideo(int limit_time, int size) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if (size != 0) {
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, size * 1024 * 1024L);//限制录制大小(10M=10 * 1024 * 1024L)
        }
        if (limit_time != 0) {
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, limit_time);//限制录制时间(10秒=10)
        }

        File videoFile = createVideoFile();
        if (videoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(intent, 5);
        }}
    private File createVideoFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "video_"+ timeStamp + ".mp4";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {

            }
        }

        File image = new File(storageDir, imageFileName);
        return image;
    }
    private void initintentData() throws JSONException {
        String content = getIntent().getStringExtra("content");
        String title = getIntent().getStringExtra("title");
        String imgs = getIntent().getStringExtra("imgs");
        String url = getIntent().getStringExtra("url");
        String position1 = getIntent().getStringExtra("position");
        if (!"审核未通过".equals(position1)) {
            po = Integer.valueOf(position1);
            isDraft = true;
        }else {
            isnotshenhe = true;
        }
        mcontent.setText(content);
        mtitle.setText(title);
        murl.setText(url);
        JSONArray array = new JSONArray(imgs);
        list.clear();
        for (int i = 0; i < array.length(); i++) {
            String img = array.getString(i);
            list.add(img);
        }
        if (list.size() < 9) {
            list.add("add");
        }
        adapter.notifyDataSetChanged();
    }

    protected void onRestart() {
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";
    private Toast toast;

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    case 5:
                        Uri uri = data.getData();
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri,
                                filePathColumn, null, null, null);
                        String path;
                        if (cursor!= null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            //picturePath就是图片在储存卡所在的位置
                            path = cursor.getString(columnIndex);
                            cursor.close();
                        }else{
                            path = uri.getPath();
                        }
                        File file = new File(path);
                        long fileSizes = getFileSizes(file);
                        if (fileSizes>10*1024*1024){
                            Toast.makeText(this,"文件不得超过10M",Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e("===path===",path+"");
                            list.add(list.size() - 1, path);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /*得到传入文件的大小*/
    public long getFileSizes(File f) throws Exception {

        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }

        return s;
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

    private void saveAndback() {
        if (list.size() != 1 || !mtitle.getText().toString().isEmpty()
                || !mcontent.getText().toString().isEmpty()) {
            new AlertDialog(MyGossipActivity.this).builder().setTitle("提示").setMsg("是否保存草稿？")
                                        .setPositiveButton("保存", new View.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    String gossip = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
                                                            "gossip", "gossip");
                                                    JSONArray array;
                                if (!TextUtils.isEmpty(gossip)) {
                                    array = new JSONArray(gossip);
                                } else {
                                    array = new JSONArray();
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                                String now = sdf.format(new Date());
                                JSONObject object = new JSONObject();
                                JSONArray array2 = new JSONArray();
                                int count = 0;
                                if (list.size() == 9){
                                    count = 9;
                                }else {
                                    count = list.size()-1;
                                }
                                for (int i = 0; i < count; i++) {
                                    String path = list.get(i);
                                    array2.put(path);
                                    if (path.contains(".mp4")){
                                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                                        File file = saveBitmapFile(bitmap);
                                        object.put("vidioimg", file.getPath().toString());
                                    }
                                }
                                object.put("path", array2);
                                object.put("title", mtitle.getText().toString());
                                object.put("content", mcontent.getText().toString());
                                object.put("url", murl.getText().toString());
                                object.put("dtime", now);

                                if (isDraft) {
                                    array.remove(array.length() - po - 1);
                                }
                                array.put(object);
                                if (array.length() > 10) {
                                    array.remove(0);
                                }
                                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "gossip",
                                        "gossip", array.toString());
                                finish();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();

        } else {
            finish();
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveAndback();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_goback_btn:
                saveAndback();
                break;
            case R.id.msend:
                if (mtitle.getText().toString().isEmpty()) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!mtitle.getText().toString().isEmpty() && mcontent.getText().toString().isEmpty()) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(this, "内容不能为空！", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    msend.setClickable(false);
                    DialogSingleUtil.show(MyGossipActivity.this);
                    if ("add".equals(list.get(list.size() - 1))) {
                        length = list.size() - 1;
                    } else {
                        length = list.size();
                    }
//                    if (isnotshenhe){
                    if (list.size() == 1){
                        initsend();
                        msend.setClickable(true);
                    }else {
                        list1.clear();
                        downloadimg();
                    }

//                    }else {
//                        for (int i = 0; i < length; i++) {
//                            File file = new File(list.get(i));
//                            lubanyasuo(file,i);
//                        }
//                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                msend.setClickable(true);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case R.id.mydraft:
                Intent intent = new Intent(this, GossipActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void lubanyasuo(File file,final int i){
        if (file.exists()) {
            Luban.with(MyGossipActivity.this).load(file).setFilename(i+"JPEG_")
                    .setCompressListener(new OnCompressListener() {

                        @Override
                        public void onSuccess(File file) {
                            list1.add(file);
                            Collections.sort(list1);
                            if (list1.size() == length) {
                                Log.e("==========",list1+"");
                                initsend();
                                msend.setClickable(true);
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
    public File saveBitmapFile(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "VIDEO_FIRST_IMG"+ timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file=new File(storageDir,imageFileName);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private void initsend() {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("userid", userID);
        params.put("title", mtitle.getText().toString());
        params.put("content", mcontent.getText().toString());
        params.put("address", murl.getText().toString());

        new Thread(new Runnable() {

            @Override
            public void run() {
                String post;
                try {
                    for (int i = 0; i <list.size() ; i++) {
                        if (list.get(i).contains(".mp4")){
                            File file = new File(list.get(i));
                            list1.set(i,file);
                            //第一帧图片
//                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(list.get(i), MediaStore.Video.Thumbnails.MINI_KIND);
//                            File file1 = saveBitmapFile(bitmap);
//                            list1.add(file1);
                        }
                    }
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

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                String post = msg.obj.toString();
                try {
                    JSONObject object = new JSONObject(post);
                    if (object.optInt("status") <= 0) {
                        Toast.makeText(MyGossipActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyGossipActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyGossipActivity.this, GossipActivity.class);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        finish();
                    }
                    DialogSingleUtil.dismiss(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

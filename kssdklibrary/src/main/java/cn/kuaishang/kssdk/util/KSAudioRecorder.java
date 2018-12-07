package cn.kuaishang.kssdk.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import cn.kuaishang.util.FileUtil;

public class KSAudioRecorder {
    private MediaRecorder mMediaRecorder;
    private File mCurrentFile;

    private boolean mIsPrepared;
    private Context mContext;

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间
    private long startTime;
    private long endTime;
    private OnAudioStatusUpdateListener audioStatusUpdateListener;
    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double)mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if(null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db,System.currentTimeMillis()-startTime);
                }
            }
//            double ratio = (double)mMediaRecorder.getMaxAmplitude() / 2700;
//            audioStatusUpdateListener.onUpdate(ratio,System.currentTimeMillis()-startTime);
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public KSAudioRecorder(Context context) {
        mContext = context.getApplicationContext();
    }

    public boolean prepareAudio() {
        try {
            mCurrentFile = new File(getVoiceCacheDir(mContext), UUID.randomUUID().toString()+".amr");
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOutputFile(mCurrentFile.getAbsolutePath());
            // 设置音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    	/* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
	    	 * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
	    	 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置音频的编码为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setMaxDuration(60*1000);
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mIsPrepared = true;

            startTime = System.currentTimeMillis();
            mHandler.postDelayed(mUpdateMicStatusTimer, 200);
            return true;
        } catch (Exception e) {
            Toast.makeText(mContext,"请检查应用权限是否开启！", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public int getVoiceLevel(int maxLevel) {
        if (mIsPrepared) {
            try {
                return Math.max(Math.min((int) (25 * Math.log10(mMediaRecorder.getMaxAmplitude() / 500)) / 4, maxLevel), 1);

                // 没有设置音频源之前获取声音振幅会报IllegalStateException，直接返回1
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        try {
            endTime = System.currentTimeMillis();
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.release();
            }
            audioStatusUpdateListener.onStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFile != null) {
            mCurrentFile.delete();
            mCurrentFile = null;
        }
    }

    @Nullable
    public String getCurrenFilePath() {
        return mCurrentFile == null ? null : mCurrentFile.getAbsolutePath();
    }

    /**
     * 获取录音文件缓存目录
     *
     * @param context
     * @return
     */
    public static File getVoiceCacheDir(Context context) {
        File voiceCacheDir = new File(FileUtil.getVideoPath());
        return voiceCacheDir;
    }

    /**
     * 根据文件服务器上的key值，获取本地缓存文件
     *
     * @param context
     * @param url
     * @return
     */
    public static File getCachedVoiceFileByUrl(Context context, String url) {
        String key = url.substring(url.lastIndexOf("/") + 1);
        return new File(getVoiceCacheDir(context), key);
    }

    /**
     * 重命名录音文件
     *
     * @param context 应用程序上下文
     * @param path    原录音文件绝对路径
     * @param key     文件服务器上文件对应的key值
     * @return 新的录音文件的绝对路径
     */
    public static String renameVoiceFilename(Context context, String path, String key) {
        File oldFile = new File(path);
        // 注意：替换掉文件服务器返回的key前面的audio/
        File newFile = new File(getVoiceCacheDir(context), key.replace("audio/", ""));
        oldFile.renameTo(newFile);
        return newFile.getAbsolutePath();
    }

    public void setAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         * @param db 当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db,long time);

        /**
         * 停止录音
         */
        public void onStop();
    }
}
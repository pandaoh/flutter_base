package com.stpass.imes.plugin.sbt.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

import com.stpass.imes.plugin.sbt.R;
/**
 * 声音播放工具类
 */
public class SoundPlayerUtil {
    public static SoundPool mSoundPlayer;
    public static SoundPlayerUtil soundPlayUtils;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int WARNING = 3;
    public static final int REGGED = 4;
    // 上下文
    static Context mContext;
    private static Vibrator vibrator;

    /**
     * 初始化
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayerUtil();
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setMaxStreams(10); // 设置最大声音容量
            spb.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC)// 设置音频流的合适的属性
                    .build()); // 转换音频格式
            mSoundPlayer = spb.build(); // 创建SoundPool对象
            // 初始化声音
            mContext = context;
            mSoundPlayer.load(mContext, R.raw.sucess, 1);// 1
            mSoundPlayer.load(mContext, R.raw.error, 1);// 2
            mSoundPlayer.load(mContext, R.raw.warning, 1);// 3
            mSoundPlayer.load(mContext, R.raw.regged, 1);// 4
        }
        if (vibrator == null) {
            vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

    /**
     * 播放成功的声音
     */
    public static void playSuccess() {
        mSoundPlayer.play(SUCCESS, 1, 1, 0, 0, 1);
    }

    /**
     * 播放错误提示音
     */
    public static void playError() {
        mSoundPlayer.play(ERROR, 1, 1, 0, 0, 1);
    }

    /**
     * 播放通知的声音
     */
    public static void playWarning() {
        mSoundPlayer.play(WARNING, 1, 1, 0, 0, 1);
    }

    /**
     * 播放扫描成功的声音
     */
    public static void playRegged() {
        mSoundPlayer.play(REGGED, 1, 1, 0, 0, 1);
    }

    /**
     * 震动
     */
    public static void playVibrator() {
        vibrator.vibrate(new long[] { 100, 200 }, -1);
    }

    /**
     * 验证不通过的震动
     */
    public static void playVibrator1() {
        vibrator.vibrate(new long[] { 100, 500 }, -2);

    }

    public static void relesed() {
        mSoundPlayer.release();
        soundPlayUtils = null;
        vibrator.cancel();
    }
}


package com.stpass.imes.plugin.sbt.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.stpass.imes.plugin.sbt.R;

public class SoundPlayer {
    interface SoundType {
        int SUCCESS = 1;
        int ERROR = 2;
        int WARNING = 3;
        int REGGED = 4;
    }

    private SoundPool pool;
    private Vibrator vibrator;

    private SoundPlayer(){ }
    private static class SoundPlayerHolder {
        private final static SoundPlayer instance = new SoundPlayer();
    }
    public static SoundPlayer getInstance(){
        return SoundPlayerHolder.instance;
    }

    public SoundPlayer init(Context context){
        if(pool == null){
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setMaxStreams(10); // 设置最大声音容量
            spb.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC)// 设置音频流的合适的属性
                    .build()); // 转换音频格式
            pool = spb.build(); // 创建SoundPool对象
            // 初始化声音
            pool.load(context, R.raw.sucess, 1);// 1
            pool.load(context, R.raw.error, 1);// 2
            pool.load(context, R.raw.warning, 1);// 3
            pool.load(context, R.raw.regged, 1);// 4
        }
        if(vibrator == null)
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        return this;
    }

    /**
     * 播放成功
     */
    public void playSuccess(){
        pool.play(SoundType.SUCCESS, 1, 1, 0, 0, 1);
    }


    /**
     * 播放错误提示音
     */
    public void playError() {
        pool.play(SoundType.ERROR, 1, 1, 0, 0, 1);
    }

    /**
     * 播放通知的声音
     */
    public void playWarning() {
        pool.play(SoundType.WARNING, 1, 1, 0, 0, 1);
    }

    /**
     * 扫描成功的声音
     */
    public void playRegged(){
        pool.play(SoundType.REGGED, 1, 1, 0, 0, 1);
    }

    /**
     * 震动
     */
    public void playVibrator() {
        vibrator.vibrate(VibrationEffect.createWaveform(new long[] { 100, 200 }, -1));
    }

    /**
     * 验证不通过的震动
     */
    public void playFailedVibrator() {
        vibrator.vibrate(VibrationEffect.createOneShot(VibrationEffect.DEFAULT_AMPLITUDE, 500));
    }
}

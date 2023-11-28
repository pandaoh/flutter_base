package com.stpass.imes.plugin.sbt.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class AbstractBarcodeService extends BaseOperatorService implements IBarcodeSender {

    final String TAG = "AbstractBarcodeService";

    //接受广播
    private final static String RACE_DATA_ACTION = "com.se4500.onDecodeComplete";

    //调用扫描广播
    private final static String START_SCAN_ACTION = "com.geomobile.se4500barcode";

    private final static String STOP_SCAN = "com.geomobile.se4500barcode.poweroff";

    protected boolean isRepeat = false;

    private Timer timer;

    private void sleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //注册系统广播  接受扫描到的数据
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    protected void init(Context context) {
        receiver = new BroadcastReceiver() {
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                String action = intent.getAction();
                if (!RACE_DATA_ACTION.equals(action)) return;

                String data = intent.getStringExtra("se4500");
                sendBarcodeWithFlutter(data);
                // 重复扫描，停止扫描然后重新启动
                if (isRepeat) {
                    sleep();
                    cancelRepeat();
                    startRepeat(context);
                }
            }
        };
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(RACE_DATA_ACTION);
        context.registerReceiver(receiver, iFilter);
    }

    protected void close(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.geomobile.se4500barcode.poweroff");
        context.sendBroadcast(intent);
        context.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver;

    protected void startRepeat(final Context context) {
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scanOnce(context);
            }
        }, 100, 4 * 1000);
    }

    protected void cancelRepeat() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 发送广播  调用系统扫描
     */
    protected void scanOnce(Context context) {
        Log.d(TAG, "======================[scanOnce]=======================");
        SystemProperties.set("persist.sys.scanstopimme", "false");
        Intent intent = new Intent();
        intent.setAction(START_SCAN_ACTION);
        context.sendBroadcast(intent, null);
    }

}

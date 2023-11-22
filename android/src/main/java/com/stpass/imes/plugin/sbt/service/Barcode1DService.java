package com.stpass.imes.plugin.sbt.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.stpass.imes.plugin.sbt.utils.MessageContent;


public class Barcode1DService extends BaseOperatorService {
    // 是否启动
    public boolean isBarcodeOpen = false;
    // 条码是否正在扫描
    public boolean loopBar = false;
    private BarcodeUtility barcodeUtility;
    private BarcodeDataReceiver barcodeDataReceiver;
    private IBarcodeSender barcodeSender;

    private Barcode1DService(){
    }
    private static class Barcode1DHolder {
        private final static Barcode1DService instance = new Barcode1DService();
    }
    public static Barcode1DService getInstance(){
        return Barcode1DHolder.instance;
    }

    @Override
    public MessageContent addContent(MessageContent params){
        params.isBarcodeOpen = this.isBarcodeOpen;
        params.loopBar = this.loopBar;
        return params;
    }

    public void initBarcode1DWithSoft(Context context){
        EXEC.execute(new InitTask1DScan11(context, this));
        pushMessageAndPlaySound(buildMessageContent(true, "initBarcode1DWithSoft","开始启动条码初始化",null));
    }

    private static class InitTask1DScan11 implements Runnable {
        Context context;
        Barcode1DService scan;
        public InitTask1DScan11(Context context, Barcode1DService scan){
            this.context = context;
            this.scan = scan;
            if(this.scan.barcodeUtility != null) this.scan.close(context);
            this.scan.barcodeUtility = BarcodeUtility.getInstance();
        }
        @Override
        public void run() {
            scan.open(context, new IBarcodeSender() {
                @Override
                public void sendBarcodeWithFlutter(String barcode) {
                    MessageContent params = scan.buildMessageContent(true, "getBarcode", "BARCODE", null);
                    params.data = barcode;
                    scan.loopBar = false;
                    scan.pushMessageAndPlaySound(params);
                }
                @Override
                public void sendErrWithFlutter(String errMsg) {
                    scan.loopBar = false;
                    scan.pushMessageAndPlaySound(scan.buildMessageContent(false, "getBarcode",
                            "1D-BARCODE扫码失败", errMsg));
                }
            });
        }
    }

    //开始扫码
    public void startScan(Context context) {
        if (barcodeUtility != null) {
            barcodeUtility.startScan(context, BarcodeUtility.ModuleType.BARCODE_1D);
            loopBar = true;
            pushMessageAndPlaySound(buildMessageContent(true,"scan1DBarcode", "1DBarcode开始扫码成功",null));
        }else{
            pushMessageAndPlaySound(buildMessageContent(false,"scan1DBarcode", "1DBarcode开始扫码失败", "barcodeUtilityIsNull"));
        }
    }

    //停止扫描
    public void stopScan(Context context) {
        if (barcodeUtility != null) {
            String TAG = "Scanner_barcodeTest";
            Log.i(TAG, "stopScan1D");
            barcodeUtility.stopScan(context, BarcodeUtility.ModuleType.BARCODE_1D);
        }
    }

    protected MessageContent buildMessageContent(boolean isOk,String fun, String message, Object err){
        MessageContent params = new MessageContent();
        params.fun = fun;
        params.type = "BARCODE";
        if(isOk){
            params.messageType = "SUCCESS";
            params.message = message;
        }else{
            params.messageType = "ERROR";
            params.message = message;
            params.error = err;
        }
        return params;
    }


    //打开
    public void open(Context context, IBarcodeSender barcodeSender) {
        if (barcodeUtility != null && context != null) {
            this.barcodeSender = barcodeSender;
            barcodeUtility.setOutputMode(context, 2);//设置广播接收数据
            barcodeUtility.setScanResultBroadcast(context, "com.scanner.broadcast", "data");//设置接收数据的广播
            barcodeUtility.setReleaseScan(context, false);//设置松开扫描按键，不停止扫描
            barcodeUtility.setScanFailureBroadcast(context, true);//扫描失败也发送广播
            barcodeUtility.enableContinuousScan(context, false);//关闭键盘助手连续扫描
            barcodeUtility.enablePlayFailureSound(context, false);//关闭键盘助手 扫描失败的声音
            //barcodeUtility.enablePlaySuccessSound(context, false);//关闭键盘助手 扫描成功的声音
            barcodeUtility.enableEnter(context, false);//关闭回车
            barcodeUtility.setBarcodeEncodingFormat(context, 1);
            pushMessageAndPlaySound(buildMessageContent(true,"initBarcode1DWithSoft","设置1D条形码",null));

            barcodeUtility.open(context, BarcodeUtility.ModuleType.BARCODE_1D);//打开
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (barcodeDataReceiver == null) {
                barcodeDataReceiver = new BarcodeDataReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.scanner.broadcast");
                context.registerReceiver(barcodeDataReceiver, intentFilter);
            }
            pushMessageAndPlaySound(buildMessageContent(true,"initBarcode1DWithSoft","启动1D条形码成功",null));
            isBarcodeOpen = true;
        }else{
            pushMessageAndPlaySound(buildMessageContent(false,"initBarcode1DWithSoft", "启动1D条形码失败", "barcodeUtilityOrContext Is NULL"));
        }
    }

    //关闭
    public void close(Context context) {
        if (barcodeUtility != null && context != null) {
            barcodeUtility.close(context, BarcodeUtility.ModuleType.BARCODE_1D);//关闭
            if (barcodeDataReceiver != null) {
                context.unregisterReceiver(barcodeDataReceiver);
                barcodeDataReceiver = null;
            }
            isBarcodeOpen = false;
        }
    }

    protected class BarcodeDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String barCode = intent.getStringExtra("data");
            String status = intent.getStringExtra("SCAN_STATE");

            if (status != null && (status.equals("cancel"))) {
                barcodeSender.sendErrWithFlutter("1DBarCodeScanErr,Status="+status);
            } else {
                if (barCode == null) barCode = "Scan fail";

                if (barcodeSender != null)
                    barcodeSender.sendBarcodeWithFlutter(barCode);
            }
        }
    }
}
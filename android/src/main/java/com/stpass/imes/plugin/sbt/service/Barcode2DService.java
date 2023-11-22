package com.stpass.imes.plugin.sbt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.stpass.imes.plugin.sbt.utils.MessageContent;


/**
 * Created by wskyo on 2021-6-18.
 */

public class Barcode2DService extends BaseOperatorService {
    // 是否启动
    public boolean isBarcodeOpen = false;
    // 条码是否正在扫描
    public boolean loopBar = false;

    private final String TAG = "Barcode2D_SCAN";
    //    private final BarcodeUtility barcodeUtility;
//    private BarcodeDataReceiver barcodeDataReceiver = null;
    private IBarcodeSender iBarcodeResult = null;


    private Barcode2DService() {
//        this.barcodeUtility = BarcodeUtility.getInstance();
    }

    private static class Barcode1DHolder {
        private final static Barcode2DService instance = new Barcode2DService();
    }

    public static Barcode2DService getInstance() {
        return Barcode1DHolder.instance;
    }


    public void initBarcode2DWithSoft(Context context) {
        MessageContent params = new MessageContent();
        params.fun = "initBarcode2DWithSoft";
        params.type = "BARCODE";
        params.messageType = "SUCCESS";
        EXEC.execute(new InitTask2DScan11(context, this));
        pushMessageAndPlaySound(params);
    }

    private static class InitTask2DScan11 implements Runnable {
        private final Context context;
        private final Barcode2DService scan;

        public InitTask2DScan11(Context context, Barcode2DService scan) {
            this.context = context;
            this.scan = scan;
        }

        @Override
        public void run() {
            scan.open(context, new IBarcodeSender() {
                @Override
                public void sendBarcodeWithFlutter(String barcode) {
                    MessageContent params = new MessageContent();
                    params.fun = "getBarcode";
                    params.message = "BARCODE";
                    params.type = "BARCODE";
                    params.messageType = "SUCCESS";
                    params.data = barcode;
                    scan.pushMessageAndPlaySound(params);
                }

                @Override
                public void sendErrWithFlutter(String errMsg) {

                }
            });
        }
    }

    //开始扫码
    public void startScan(Context context) {
//        if (barcodeUtility != null) {
//            Log.i(TAG, "ScanBarcode");
//            barcodeUtility.startScan(context, BarcodeUtility.ModuleType.BARCODE_2D);
//        }
    }

    //停止扫描
    public void stopScan(Context context) {
//        if (barcodeUtility != null) {
//            Log.i(TAG, "stopScan");
//            barcodeUtility.stopScan(context, BarcodeUtility.ModuleType.BARCODE_2D);
//        }
    }

    //打开
    public void open(Context context, IBarcodeSender iBarcodeResult) {
//        if (barcodeUtility != null) {
//            this.iBarcodeResult = iBarcodeResult;
//            barcodeUtility.setOutputMode(context, 2);//设置广播接收数据
//            barcodeUtility.setScanResultBroadcast(context, "com.scanner.broadcast", "data");//设置接收数据的广播
//            barcodeUtility.open(context, BarcodeUtility.ModuleType.BARCODE_2D);//打开2D
//            barcodeUtility.setReleaseScan(context, false);//设置松开扫描按键，不停止扫描
//            barcodeUtility.setScanFailureBroadcast(context, true);//扫描失败也发送广播
//            barcodeUtility.enableContinuousScan(context, false);//关闭键盘助手连续扫描
//            barcodeUtility.enablePlayFailureSound(context, false);//关闭键盘助手 扫描失败的声音
//            //barcodeUtility.enablePlaySuccessSound(context, false);//关闭键盘助手 扫描成功的声音
//            barcodeUtility.enableEnter(context, false);//关闭回车
//            barcodeUtility.setBarcodeEncodingFormat(context, 1);
//
//            if (barcodeDataReceiver == null) {
//                barcodeDataReceiver = new BarcodeDataReceiver();
//                IntentFilter intentFilter = new IntentFilter();
//                intentFilter.addAction("com.scanner.broadcast");
//                context.registerReceiver(barcodeDataReceiver, intentFilter);
//            }
//        }
    }

    //关闭
    public void close(Context context) {
//        if (barcodeUtility != null) {
//            barcodeUtility.close(context, BarcodeUtility.ModuleType.BARCODE_2D);//关闭2D
//            if (barcodeDataReceiver != null) {
//                context.unregisterReceiver(barcodeDataReceiver);
//                barcodeDataReceiver = null;
//            }
//        }
    }

    @Override
    public MessageContent addContent(MessageContent params) {
        params.isBarcodeOpen = this.isBarcodeOpen;
        params.loopBar = this.loopBar;
        return params;
    }

    protected class BarcodeDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String barCode = intent.getStringExtra("data");
            String status = intent.getStringExtra("SCAN_STATE");

            if (status != null && (status.equals("cancel"))) {
                Log.d(TAG, "1D scan err,Status=" + status);
            } else {
                if (barCode == null) {
                    barCode = "Scan fail";
                }
                if (iBarcodeResult != null)
                    iBarcodeResult.sendBarcodeWithFlutter(barCode);
            }
        }
    }
}


//if (barCode != null && !barCode.equals("")) {
//success
//byte[] barcodeBytes = intent.getByteArrayExtra("dataBytes");//获取原始的bytes数据
//if(barcodeBytes!=null) {
//    byte[] decodeData= Base64.decode(barcodeBytes,Base64.DEFAULT);
//    barCode=StringUtility.bytes2HexString(decodeData);
//}

//}

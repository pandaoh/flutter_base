package com.stpass.imes.plugin.sbt.service;


import android.content.Context;
import android.util.Log;

import com.stpass.imes.plugin.sbt.utils.MessageContent;


public class Barcode1DService extends AbstractBarcodeService {
    // 是否启动
    public boolean isBarcodeOpen = false;

    String TAG = "Scanner_barcodeTest";

    private Barcode1DService() {
    }

    private static class Barcode1DHolder {
        private final static Barcode1DService instance = new Barcode1DService();
    }

    public static Barcode1DService getInstance() {
        return Barcode1DHolder.instance;
    }

    public void initBarcode1DWithSoft(Context context) {
        // 已经打开
        if (isBarcodeOpen) {
            pushMessageAndPlaySound(buildMessageContent(true, "initBarcode1DWithSoft", "1D条形码已启动", null));
            return;
        }
        super.init(context);
        pushMessageAndPlaySound(buildMessageContent(true, "initBarcode1DWithSoft", "启动1D条形码成功", null));
        isBarcodeOpen = true;
    }

    //关闭
    public void close(Context context) {
//        if (!isBarcodeOpen) {
//            pushMessageAndPlaySound(buildMessageContent(false, "close", "1D条形码未启动", null));
//            return;
//        }
//        super.close(context);
//        isBarcodeOpen = false;
        pushMessageAndPlaySound(buildMessageContent(true, "close", "关闭1D条形码成功", null));
    }

    //开始扫码
    public void startScan(Context context) {
        if( !isBarcodeOpen){
            pushMessageAndPlaySound(buildMessageContent(false, "scan1DBarcode", "1D条形码未启动", null));
            return;
        }
        if (isRepeat) {
            pushMessageAndPlaySound(buildMessageContent(false, "scan1DBarcode", "1DBarcode扫码已启动", null));
            return;
        }
        isRepeat = true;
        startRepeat(context);
        pushMessageAndPlaySound(buildMessageContent(true, "scan1DBarcode", "1DBarcode开始扫码成功", null));
    }

    //停止扫描
    public void stopScan(Context context) {
        if (!isRepeat) return;
        isRepeat = false;
        cancelRepeat();
    }

    protected MessageContent buildMessageContent(boolean isOk, String fun, String message, Object err) {
        MessageContent params = new MessageContent();
        params.fun = fun;
        params.type = "BARCODE";
        if (isOk) {
            params.messageType = "SUCCESS";
            params.message = message;
        } else {
            params.messageType = "ERROR";
            params.message = message;
            params.error = err;
        }
        return params;
    }

    @Override
    public MessageContent addContent(MessageContent params) {
        params.isBarcodeOpen = this.isBarcodeOpen;
        params.loopBar = this.isRepeat;
        return params;
    }

    @Override
    public void sendBarcodeWithFlutter(String barcode) {
        MessageContent params = buildMessageContent(true, "getBarcode", "BARCODE", null);
        params.data = barcode;
        pushMessageAndPlaySound(params);
    }

    @Override
    public void sendErrWithFlutter(String errMsg) {
    }
}
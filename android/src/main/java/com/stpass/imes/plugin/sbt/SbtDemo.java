package com.stpass.imes.plugin.sbt;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.bean.SpdReadData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.interfaces.OnSpdReadListener;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class SbtDemo implements FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {

    private MethodChannel channel;
    private EventChannel eventChannel;
    private Context context;
    public EventChannel.EventSink sink = null;

    // 定义扫描模块
    public static IUHFService iuhfService;
    // 是否正在循环扫描
    public boolean loopFlag = false;
    // RFID是否已经初始化开启
    public boolean isOpenRfidModule = false;
    // RFID是否正在初始化
    public boolean isInitialization = false;
    // 默认功率
    public int rfidPower = 10;
//    // 二维码SDK模块
////    public Barcode2DWithSoft barcode2DWithSoft;
//    // 二维码是否启动
//    public boolean isBarcodeOpen = false;
//    // 条码是否正在扫描
//    public boolean loopBar = false;
//    // 条码类型
//    String seldata = "ASCII"; // 编码
//    // 扫描回调函数
//    // public Barcode2DWithSoft.ScanCallback ScanBack;
//    public boolean disabledInitPlay = false;
//
//    // 一维条码
//    // private Barcode1D barcode1DWithSoft;
//    private boolean is1DBarcodeOpened = false;
//    private boolean loopber1D = false;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {
                sink.success(ObjectUtils.getObjectToMap(msg.obj));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    };

    final static Executor exec = new ThreadPoolExecutor(4, 4, 20,
            TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(20));

    @Override
    public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "sbt_plugin");
        channel.setMethodCallHandler(this);
        context = binding.getApplicationContext();
        eventChannel = new EventChannel(binding.getBinaryMessenger(), "com.stpass.sbt/event_channel");
        eventChannel.setStreamHandler((EventChannel.StreamHandler) this);
        SoundPlayerUtil.init(context);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        eventChannel.setStreamHandler(null);
        // 关闭扫描模块
        if (iuhfService != null) {
            iuhfService.closeDev();
            iuhfService = null;
            UHFManager.closeUHFService();
        }
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        sink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android Version:" + android.os.Build.VERSION.RELEASE);
                break;
            case "toast":
                String txt = call.argument("text");
                Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
                break;
            case "eventChannelTest":
                BuildType params = new BuildType();
                params.type = "OTHER";
                params.messageType = "SUCCESS";
                buildMessage(params);
                break;
            case "testSkin":
                break;
            case "setDisabledInitPlay":
                break;
            case "initRfid":
                initRfid();
                break;
            case "rfidFree":
                rfidFree();
                break;
            case "startInventoryTag":
                startInventoryTag();
                break;
            case "stopInventory":
                stopInventory();
                break;
            case "setPower":
                int n = call.argument("n");
                setPower(n);
                break;
            case "getPower":
                getPower();
                break;
            case "initBarcode2DWithSoft":
//                initBarcode2DWithSoft();
                break;
            case "scanBarcode":
//                scanBarcode();
                break;
            case "barcodeClose":
//                barcodeClose();
                break;
            case "initBarcode1DWithSoft":
//                initBarcode1DWithSoft();
                break;
            case "scan1DBarcode":
//                scan1DBarcode();
                break;
            case "barcode1DClose":
//                barcode1DClose();
                break;
            case "soundPlayer":
                String soundType = call.argument("soundType");
                SoundPlayerUtil.play(soundType);
                break;
            case "scan1DBarcodeStop":
//                scan1DBarcodeStop();
                break;
            case "barcodeStop":
//                barcodeStop();
                break;
            default:
                result.notImplemented();
        }
    }

    // 查看功率
    public void getPower() {
        BuildType params = new BuildType();
        params.fun = "getPower";
        params.type = "GET_POWER";
        try {
            int power = iuhfService.getAntennaPower();
            params.messageType = "SUCCESS";
            params.message = "获取RFID功率成功";
            params.data = "" + power;
        } catch (Exception e) {
            params.messageType = "ERROR";
            params.message = "获取RFID功率失败";
            params.data = "";
        }
        buildMessage(params);
    }

    // 设置功率
    public void setPower(int n) {
        BuildType params = new BuildType();
        params.fun = "setPower";
        params.type = "RFID";
        try {
            int setPowerResult = iuhfService.setAntennaPower(n);
            if (setPowerResult == 0) {
                rfidPower = n;
                params.messageType = "SUCCESS";
                params.message = "设置RFID功率成功" + n;
            } else {
                rfidPower = 5;
                params.messageType = "ERROR";
                params.message = "设置RFID功率失败" + n;
            }
        } catch (Exception e) {
            rfidPower = 5;
            params.messageType = "ERROR";
            params.message = "设置RFID功率失败" + n;
        }
        buildMessage(params);
    }

    // 开始扫描
    public void startInventoryTag() {
        BuildType params = new BuildType();
        params.fun = "startInventoryTag";
        params.type = "RFID";
        try {
            iuhfService.inventoryStart();
            loopFlag = true;
//            exec.execute(new TagThread());
            params.messageType = "SUCCESS";
            params.message = "开始扫描RFID成功";
            buildMessage(params);
        } catch (Exception ex) {
            loopFlag = false;
            params.messageType = "ERROR";
            params.message = "开始扫描RFID失败";
            params.error = ex.getMessage();
            buildMessage(params);
        }
    }

    // 停止扫描
    public void stopInventory() {
        loopFlag = false;
        BuildType params = new BuildType();
        params.fun = "inventoryStop";
        params.type = "RFID";
        try {
            iuhfService.inventoryStop();
            params.messageType = "SUCCESS";
            params.message = "停止扫描RFID成功";
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "停止扫描RFID失败";
            params.error = ex.getMessage();
            buildMessage(params);
        }
    }

    // RFID初始化方法
    public void initRfid() {
        isInitialization = true;
        try {
            iuhfService = UHFManager.getUHFService(context);
            // 添加盘点监听
            iuhfService.setOnInventoryListener(newInventoryListener());
            // 添加读监听
            iuhfService.setOnReadListener(newReadListener());
        } catch (Exception ex) {
            isInitialization = false;
            BuildType params = new BuildType();
            params.type = "RFID";
            params.messageType = "ERROR";
            params.fun = "initRfid";
            params.message = "初始化RFID失败，请检查！";
            params.error = ex.getMessage();
            buildMessage(params);
            return;
        }

        // 异步初始化模块
        if (iuhfService != null) InitTask.execute(this::initFunc, this::initCallBack);
    }

    // 关闭RFID模块
    public void rfidFree() {
        BuildType params = new BuildType();
        params.fun = "rfidFree";
        params.type = "RFID";
        try {
            if (iuhfService != null) {
                iuhfService.closeDev();
                iuhfService = null;
                UHFManager.closeUHFService();
                isOpenRfidModule = false;
            }
            params.messageType = "SUCCESS";
            params.message = "关闭RFID成功";
            buildMessage(params);
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "关闭RFID失败";
            params.error = ex.getMessage();
            buildMessage(params);
        }
    }

    private boolean initFunc() {
        BuildType params = new BuildType();
        params.type = "RFID";
        params.fun = "initRfid";
        params.messageType = "PENDING";
        params.message = "正在初始化RFID模块...";
        buildMessage(params);
        return iuhfService.openDev() == 0;
    }

    private void initCallBack(boolean result) {
        // 失败
        isInitialization = false;
        BuildType params = new BuildType();
        params.fun = "initRfid";
        params.type = "RFID";
        if (!result) {
            params.messageType = "ERROR";
            params.message = "初始化RFID失败，请检查设备...";
        } else {
            isOpenRfidModule = true;
            params.messageType = "SUCCESS";
            params.message = "初始化RFID成功";
            // 设置循环盘点同时读取 EPC、TID 模式
            iuhfService.setInvMode(1, 0, 0);
            // 设置功率
            iuhfService.setAntennaPower(rfidPower);
        }
        buildMessage(params);
    }

    // 构建返回消息
    private void buildMessage(BuildType params) {
        params.loopFlag = loopFlag;
        params.isOpenRfidModule = isOpenRfidModule;
        params.isInitialization = isInitialization;
        params.rfidPower = rfidPower;
        params.isBarcodeOpen = false;
        params.loopBar = false;
        Message msg = handler.obtainMessage();
        msg.obj = params;
        handler.sendMessage(msg);
//        if ((disabledInitPlay && params.fun == "initBarcode2DWithSoft") || (disabledInitPlay && params.fun == "initRfid")) {
//            return;
//        }
        switch (params.messageType) {
            case "SUCCESS":
                SoundPlayerUtil.playSuccess();
                break;
            case "ERROR":
                SoundPlayerUtil.playError();
                break;
            case "PENDING":
                SoundPlayerUtil.playWarning();
                break;
            default:
        }
    }

    private OnSpdInventoryListener newInventoryListener(){
        return new OnSpdInventoryListener(){

            @Override
            public void getInventoryData(SpdInventoryData spdInventoryData) {
                System.out.println(spdInventoryData);
            }

            @Override
            public void onInventoryStatus(int i) {
                System.out.println(i);
            }
        };
    }

    private OnSpdReadListener newReadListener(){
        return new OnSpdReadListener() {
            @Override
            public void getReadData(SpdReadData spdReadData) {
                String strTid;
                String strResult;
                //TODO 读卡回调
                if (spdReadData.getStatus() == 0) {
                    //读卡成功
                    byte[] readData = spdReadData.getReadData();
                    strTid = new String(readData);
                    if (strTid.length() != 0 && !strTid.equals("0000000" + "000000000")
                            && !strTid.equals("000000000000000000000000")) {
                        strResult = strTid;
                    } else {
                        strResult = "";
                    }
                    BuildType params = new BuildType();
                    params.fun = "TagThread";
                    params.type = "RFID";
                    params.messageType = "SUCCESS";
                    params.message = "RFID扫描结果";
                    params.data = strResult;
                    buildMessage(params);
                } else {
                    //读卡失败
                }
            }
        };
    }
}
package com.stpass.imes.plugin.sbt.service;


import android.content.Context;

import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.bean.SpdReadData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.interfaces.OnSpdReadListener;
import com.stpass.imes.plugin.sbt.utils.CommonUtils;
import com.stpass.imes.plugin.sbt.utils.MessageContent;

public class UHFReaderService extends BaseOperatorService {

    // 定义扫描模块
    IUHFService iuhfService;
    // 是否正在循环扫描
    boolean loopFlag = false;
    // RFID是否已经初始化开启
    boolean isOpenRfidModule = false;
    // RFID是否正在初始化
    boolean isInitialization = false;
    // 默认功率
    int rfidPower = 10;

    private UHFReaderService() {
    }

    private static class UHFReaderHolder {
        private final static UHFReaderService instance = new UHFReaderService();
    }

    public static UHFReaderService getInstance() {
        return UHFReaderHolder.instance;
    }

    private OnSpdInventoryListener newInventoryListener() {
        return new OnSpdInventoryListener() {

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

    private OnSpdReadListener newReadListener() {
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
                    MessageContent params = new MessageContent();
                    params.fun = "TagThread";
                    params.type = "RFID";
                    params.messageType = "SUCCESS";
                    params.message = "RFID扫描结果";
                    params.data = strResult;
                    pushMessageAndPlaySound(params);
                } else {
                    //读卡失败
                }
            }
        };
    }

    public void init(Context context) {
        isInitialization = true;

        MessageContent params = new MessageContent();
        params.type = "RFID";
        params.fun = "initRfid";
        params.messageType = "PENDING";
        params.message = "正在初始化RFID模块...";
        pushMessageAndPlaySound(params);

        iuhfService = UHFManager.getUHFService(context);
        // 添加盘点监听
        iuhfService.setOnInventoryListener(newInventoryListener());
        // 添加读监听
        iuhfService.setOnReadListener(newReadListener());

        EXEC.execute(() -> initFunc());
    }

    public void startScan() {
        MessageContent params = new MessageContent();
        params.fun = "startInventoryTag";
        params.type = "RFID";
        try {
            iuhfService.inventoryStart();
            loopFlag = true;
            params.messageType = "SUCCESS";
            params.message = "开始扫描RFID成功";
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            loopFlag = false;
            params.messageType = "ERROR";
            params.message = "开始扫描RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }

    public void stopScan() {
        loopFlag = false;
        MessageContent params = new MessageContent();
        params.fun = "stopInventory";
        params.type = "RFID";
        try {
            iuhfService.inventoryStop();
            params.messageType = "SUCCESS";
            params.message = "停止扫描RFID成功";
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "停止扫描RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }

    public void setPower(int n) {
        MessageContent params = new MessageContent();
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
        pushMessageAndPlaySound(params);
    }

    public void getPower() {
        MessageContent params = new MessageContent();
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
        pushMessageAndPlaySound(params);
    }

    public void free() {
        MessageContent params = new MessageContent();
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
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "关闭RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }

    private void initFunc() {
        boolean result = iuhfService.openDev() == 0;
        MessageContent params = new MessageContent();
        params.type = "RFID";
        params.fun = "initRfid";
        if (result) {
            params.messageType = "SUCCESS";
            params.message = "初始化RFID成功";
            isOpenRfidModule = true;
            // 设置循环盘点同时读取 EPC、TID 模式
            iuhfService.setInvMode(1, 0, 0);
            // 设置功率
            iuhfService.setAntennaPower(rfidPower);
        } else {
            params.messageType = "ERROR";
            params.message = "初始化RFID失败，请检查设备...";
        }
        pushMessageAndPlaySound(params);
    }


    @Override
    public MessageContent addContent(MessageContent params) {
        params.loopFlag = loopFlag;
        params.isOpenRfidModule = isOpenRfidModule;
        params.isInitialization = isInitialization;
        params.rfidPower = rfidPower;
        return params;
    }

}

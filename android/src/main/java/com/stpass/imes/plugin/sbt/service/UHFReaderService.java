package com.stpass.imes.plugin.sbt.service;


import com.stpass.imes.plugin.sbt.utils.CommonUtils;
import com.stpass.imes.plugin.sbt.utils.MessageContent;

public class UHFReaderService extends BaseOperatorService {

    // 定义扫描模块
    RFIDWithUHFUART mReader;
    // 是否正在循环扫描
    boolean loopFlag = false;
    // RFID是否已经初始化开启
    boolean isOpenRfidModule = false;
    // RFID是否正在初始化
    boolean isInitialization = false;
    // 默认功率
    int rfidPower = 10;

    private UHFReaderService(){}
    private static class UHFReaderHolder {
        private final static UHFReaderService instance = new UHFReaderService();
    }
    public static UHFReaderService getInstance(){
        return UHFReaderHolder.instance;
    }

    public void init(){
        isInitialization = true;

        MessageContent params = new MessageContent();
        params.type = "RFID";
        params.fun = "initRfid";
        params.messageType = "PENDING";
        params.message = "正在初始化RFID模块...";
        pushMessageAndPlaySound(params);

        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (ConfigurationException e) {
            isInitialization = false;
            params = new MessageContent();
            params.type = "RFID";
            params.messageType = "ERROR";
            params.fun = "initRfid";
            params.message = "初始化RFID失败，请检查！";
            params.error = e.getMessage();
            pushMessageAndPlaySound(params);
            e.printStackTrace();
        }

        EXEC.execute(new ReaderInitTask(this));
    }

    public void startScan() {
        MessageContent params = new MessageContent();
        params.fun = "startInventoryTag";
        params.type = "RFID";
        try {
            boolean startInventoryTagResult = mReader.startInventoryTag();
            if (startInventoryTagResult) {
                loopFlag = true;
                EXEC.execute(new ScanTask(this));
                params.messageType = "SUCCESS";
                params.message = "开始扫描RFID成功";
            } else {
                loopFlag = false;
                params.messageType = "ERROR";
                params.message = "开始扫描RFID失败";
                params.error = CommonUtils.getMessage(mReader.getErrCode());
            }
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            loopFlag = false;
            params.messageType = "ERROR";
            params.message = "开始扫描RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }
    public void stopScan(){
        loopFlag = false;
        MessageContent params = new MessageContent();
        params.fun = "stopInventory";
        params.type = "RFID";
        try {
            boolean stopInventoryResult = mReader.stopInventory();
            if (stopInventoryResult) {
                params.messageType = "SUCCESS";
                params.message = "停止扫描RFID成功";
            } else {
                params.messageType = "ERROR";
                params.message = "停止扫描RFID失败";
                params.error = CommonUtils.getMessage(mReader.getErrCode());
            }
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "停止扫描RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }
    public void setPower(int n){
        MessageContent params = new MessageContent();
        params.fun = "setPower";
        params.type = "RFID";
        try {
            boolean setPowerResult = mReader.setPower(n);
            if (setPowerResult) {
                rfidPower = n;
                params.messageType = "SUCCESS";
                params.message = "设置RFID功率成功" + n;
            } else {
                rfidPower = 5;
                params.messageType = "ERROR";
                params.message = "设置RFID功率失败" + n;
                params.error = CommonUtils.getMessage(mReader.getErrCode());
            }
        } catch (Exception e) {
            rfidPower = 5;
            params.messageType = "ERROR";
            params.message = "设置RFID功率失败" + n;
            params.error = e.getMessage();
        }
        pushMessageAndPlaySound(params);
    }
    public void getPower(){
        MessageContent params = new MessageContent();
        params.fun = "getPower";
        params.type = "GET_POWER";
        try {
            int power = mReader.getPower();
            params.messageType = "SUCCESS";
            params.message = "获取RFID功率成功";
            params.data = Integer.toString(power);
        } catch (Exception e) {
            params.messageType = "ERROR";
            params.message = "获取RFID功率失败";
            params.data = "--";
            params.error = e.getMessage();
        }
        pushMessageAndPlaySound(params);
    }
    public void free() {
        MessageContent params = new MessageContent();
        params.fun = "rfidFree";
        params.type = "RFID";
        try {
            if (mReader != null) {
                boolean freeResult = mReader.free();
                if (freeResult) {
                    isOpenRfidModule = false;
                    params.messageType = "SUCCESS";
                    params.message = "关闭RFID成功";
                }else{
                    params.error = CommonUtils.getMessage(mReader.getErrCode());
                    params.messageType = "ERROR";
                    params.message = "关闭RFID失败";
                }
            }else {
                params.messageType = "ERROR";
                params.message = "关闭RFID失败,mReader为空";
            }
            pushMessageAndPlaySound(params);
        } catch (Exception ex) {
            params.messageType = "ERROR";
            params.message = "关闭RFID失败";
            params.error = ex.getMessage();
            pushMessageAndPlaySound(params);
        }
    }

    private static class ReaderInitTask implements Runnable {
        UHFReaderService reader;
        RFIDWithUHFUART mReader;
        int rfidPower;
        ReaderInitTask(UHFReaderService reader){
            this.reader = reader;
            this.mReader = reader.mReader;
            this.rfidPower = reader.rfidPower;
        }
        @Override
        public void run() {
            mReader.free();
            boolean result = mReader.init();
            MessageContent params = new MessageContent();
            params.type = "RFID";
            params.fun = "initRfid";
            if(result){
                mReader.setEPCAndTIDMode();  // 设置循环盘点同时读取 EPC、TID 模式
                mReader.setPower(rfidPower); // 设置功率
                this.reader.isOpenRfidModule = true;
                params.messageType = "SUCCESS";
                params.message = "初始化RFID成功";
            }else{
                params.messageType = "ERROR";
                params.message = "初始化RFID失败，请检查设备...";
                params.error = CommonUtils.getMessage(mReader.getErrCode());
            }
            this.reader.isInitialization = false;
            reader.pushMessageAndPlaySound(params);
        }
    }

    private static class ScanTask implements Runnable {
        UHFReaderService reader;
        ScanTask(UHFReaderService reader){
            this.reader = reader;
        }
        @Override
        public void run() {
            String strTid;
            String strResult;
            UHFTAGInfo res;
            while (reader.loopFlag) {
                res = reader.mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res.getTid();

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
                    params.epcData = res.getEPC();
                    reader.pushMessageAndPlaySound(params);
                }
            }
        }
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

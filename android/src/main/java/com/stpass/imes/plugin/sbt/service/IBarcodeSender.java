package com.stpass.imes.plugin.sbt.service;

public interface IBarcodeSender {
    void sendBarcodeWithFlutter(String barcode);

    void sendErrWithFlutter(String errMsg);
}
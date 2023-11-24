/*
 * @Author: wuxh
 * @Date: 2020-12-24 18:28:56
 * @LastEditTime: 2021-12-06 18:02:34
 * @LastEditors: wuxh
 * @Description: 
 * @FilePath: /flutter-plugin-cwrfid/lib/cwrfid.dart
 */

import 'dart:async';

import 'package:flutter/services.dart';

import 'call.dart';

class Cwrfid {
  // static final EventBus eventBus = new EventBus();

  static const MethodChannel _channel =
      MethodChannel('sbt_plugin/method');
  static const EventChannel _eventChannel =
      EventChannel('sbt_plugin/event');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> toast(String text) async {
    return _channel.invokeMethod('toast', {"text": text});
  }

  static Future<String> eventChannelTest(String text) async {
    return _channel.invokeMethod('eventChannelTest', {"text": text});
  }

  static initRfid() {
    _channel.invokeMethod('initRfid');
  }

  static rfidFree() {
    _channel.invokeMethod('rfidFree');
  }

  static startInventoryTag() {
    _channel.invokeMethod('startInventoryTag');
  }

  static testSkin() {
    _channel.invokeMethod('testSkin');
  }

  static stopInventory() {
    _channel.invokeMethod('stopInventory');
  }

  static setPower(int n) {
    _channel.invokeMethod('setPower', {"n": n});
  }

  static getPower() {
    _channel.invokeMethod('getPower');
  }


  static setDisabledInitPlay({bool disabledInitPlay = false}) {
    _channel.invokeMethod(
        'setDisabledInitPlay', {"disabledInitPlay": disabledInitPlay});
  }

  static initBarcode2DWithSoft() {
    _channel.invokeMethod('initBarcode2DWithSoft');
  }

  static scanBarcode() {
    _channel.invokeMethod('scanBarcode');
  }

  static barcodeClose() {
    _channel.invokeMethod('barcodeClose');
  }

  static initBarcode1DWithSoft() {
    _channel.invokeMethod('initBarcode1DWithSoft');
  }

  static scan1DBarcode() {
    _channel.invokeMethod('scan1DBarcode');
  }

  static barcode1DClose() {
    _channel.invokeMethod('barcode1DClose');
  }

  static scan1DBarcodeStop() {
    _channel.invokeMethod('scan1DBarcodeStop');
  }

  static barcodeStop() {
    _channel.invokeMethod('barcodeStop');
  }

  static soundPlayer({String soundType = 'SUCCESS'}) {
    _channel.invokeMethod('soundPlayer', {"soundType": soundType});
  }

  static barcode1DIsPowerOn() {
    _channel.invokeMethod('barcode1DIsPowerOn');
  }

  // ignore: cancel_subscriptions
  static StreamSubscription<dynamic> _sink;
  static Future eventInit() async {
    if (_sink == null) {
      _sink = _eventChannel.receiveBroadcastStream().listen((event) {
        Call.dispatch('native-to-view', data: event);
      });
    }
  }
}

/*
 * @Author: wuxh
 * @Date: 2020-12-24 18:28:57
 * @LastEditTime: 2021-12-06 18:01:45
 * @LastEditors: wuxh
 * @Description: 
 * @FilePath: /flutter-plugin-cwrfid/example/lib/main.dart
 */

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';

import 'call.dart';
import 'plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  static const stream = const EventChannel('com.stpass.cwrfid/keycode');
  @override
  void initState() {
    super.initState();
    initPlatformState();
    Cwrfid.eventInit();
    Call.addCallBack('native-to-view', this._callBack);
    stream.receiveBroadcastStream().listen((event) {
      print(event);
    });
  }

  _callBack(data) {
    print(data);
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Cwrfid.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('cwrfid example'),
      ),
      body: SingleChildScrollView(
        child: Wrap(
          children: [
            // Button(
            //     child: Text('测试event'),
            //     onPressed: () async {
            //       print('eventChannelTest');
            //       Cwrfid.eventChannelTest('88888');
            //       // Platform
            //     }),
            Button(
                child: Text('初始化RFID模块'),
                onPressed: () async {
                  Cwrfid.initRfid();
                }),
            Button(
                child: Text('开始RFID扫描'),
                onPressed: () async {
                  Cwrfid.startInventoryTag();
                }),
            Button(
                child: Text('停止RFID扫描'),
                onPressed: () async {
                  Cwrfid.stopInventory();
                }),
            Button(
                child: Text('关闭RFID模块'),
                onPressed: () async {
                  Cwrfid.rfidFree();
                }),
            Button(
                child: Text('设置功率'),
                onPressed: () async {
                  Cwrfid.setPower(20);
                }),
            Button(
                child: Text('获取功率'),
                onPressed: () async {
                  Cwrfid.getPower();
                }),
            HorizontalLine(),
            Button(
                child: Text('初始化二维码扫描'),
                onPressed: () async {
                  Cwrfid.initBarcode2DWithSoft();
                }),
            Button(
                child: Text('二维码扫描'),
                onPressed: () async {
                  Cwrfid.scanBarcode();
                }),
            Button(
                child: Text('停止二维码扫描'),
                onPressed: () async {
                  Cwrfid.barcodeStop();
                }),
            Button(
                child: Text('关闭二维码扫描'),
                onPressed: () async {
                  Cwrfid.barcodeClose();
                }),
            HorizontalLine(),
            Button(
                child: Text('初始化一维码条码扫描'),
                onPressed: () async {
                  Cwrfid.initBarcode1DWithSoft();
                }),
            Button(
                child: Text('一维码扫描'),
                onPressed: () async {
                  Cwrfid.scan1DBarcode();
                }),
            Button(
                child: Text('停止一维码扫描'),
                onPressed: () async {
                  Cwrfid.scan1DBarcodeStop();
                }),
            Button(
                child: Text('关闭一维码扫描'),
                onPressed: () async {
                  Cwrfid.barcode1DClose();
                }),
            HorizontalLine(),
            Button(
                child: Text('声音-成功'),
                onPressed: () async {
                  Cwrfid.soundPlayer(soundType: 'SUCCESS');
                }),
            Button(
                child: Text('声音-失败'),
                onPressed: () async {
                  Cwrfid.soundPlayer(soundType: 'ERROR');
                }),
            Button(
                child: Text('声音-警告'),
                onPressed: () async {
                  Cwrfid.soundPlayer(soundType: 'WARNING');
                }),
            Button(
                child: Text('声音-错误'),
                onPressed: () async {
                  Cwrfid.soundPlayer(soundType: 'REGGED');
                }),
            // ElevatedButton(
            //     child: Text('震动'),
            //     onPressed: () async {
            //       Cwrfid.soundPlayer(soundType: 'VIBRATOR');
            //     }),
            // ElevatedButton(
            //     child: Text('震动-错误'),
            //     onPressed: () async {
            //       Cwrfid.soundPlayer(soundType: 'VIBRATOR_ERROR');
            //     }),
          ],
        ),
      ),
    ));
  }
}

// ignore: non_constant_identifier_names
Widget Button({Widget child, Function onPressed}) {
  return Container(
      padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
      child: ElevatedButton(child: child, onPressed: onPressed));
}

// ignore: non_constant_identifier_names
Widget HorizontalLine() {
  return SizedBox(
    width: 375,
    height: 0.5,
    child: DecoratedBox(
      decoration: BoxDecoration(color: Color(0xFF4272CF)),
    ),
  );
}

/*
 * @Author: HxB
 * @Date: 2023-11-02 16:50:42
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 10:23:19
 * @Description: 主程序
 * @FilePath: \rfid_pda\lib\main.dart
 */
import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:provider/provider.dart';
import 'package:path_provider/path_provider.dart';
import 'package:sbt_rfid_pda/tools/logger.dart';
import 'providers/main_model.dart';
import 'config/routes.dart';

final GlobalKey<NavigatorState> navigatorKey = GlobalKey();

void main() {
  FlutterError.onError = (FlutterErrorDetails details) {
    reportErrorAndLog(details);
  };

  runZonedGuarded(
    () {
      runApp(
        MultiProvider(
          providers: [ChangeNotifierProvider(create: (_) => MainModel())],
          child: MyApp(),
        ),
      );
    },
    (error, stackTrace) {
      reportErrorAndLog(makeDetails(error, stackTrace));
    },
  );
}

FlutterErrorDetails makeDetails(Object error, StackTrace stackTrace) {
  return FlutterErrorDetails(stack: stackTrace, exception: error);
}

void reportErrorAndLog(FlutterErrorDetails details) {
  // 获取错误信息
  String errorInfo = details.toString();

  // 写入本地文件
  Logger.logError(errorInfo, details.stack);

  // 在控制台打印错误信息
  FlutterError.dumpErrorToConsole(details);
}

class MyApp extends StatefulWidget {
  const MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> with WidgetsBindingObserver {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance?.addObserver(this);
  }

  @override
  void dispose() {
    WidgetsBinding.instance?.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) async {
    switch (state) {
      case AppLifecycleState.resumed:
        Logger.logInfo('应用进入前台');
        break;
      case AppLifecycleState.inactive:
        Logger.logInfo('应用处于闲置状态，切换到后台');
        break;
      case AppLifecycleState.paused:
        Logger.logInfo('应用处于不可见状态');
        break;
      case AppLifecycleState.detached:
        Logger.logInfo('当前页面即将退出');
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      builder: EasyLoading.init(),
      home: Scaffold(
        body: MaterialApp(
          navigatorKey: navigatorKey,
          debugShowCheckedModeBanner: false,
          title: "血液管理手持终端",
          theme: ThemeData(primaryColor: Colors.white),
          routes: routes,
        ),
      ),
    );
  }
}

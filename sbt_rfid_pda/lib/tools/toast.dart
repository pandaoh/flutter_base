/*
 * @Author: HxB
 * @Date: 2023-11-15 17:50:50
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-15 17:50:54
 * @Description: toast
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\tools\toast.dart
 */
import 'package:flutter_easyloading/flutter_easyloading.dart';

class Toast {
  Toast._() {
    // EasyLoading 已全局初始化构建
    // EasyLoading.instance.loadingStyle = EasyLoadingStyle.custom;
    // 此处可自定义风格
  }
  static final Toast _instance = Toast._();

  static const String SUCCESS = "SUCCESS";
  static const String ERROR = "ERROR";
  static const String WARNING = "WARNING";
  static const String INFO = "INFO";

  static loading(String msg) {
    EasyLoading.show(status: msg);
  }

  static progress(double value, String msg) {
    EasyLoading.showProgress(value, status: msg);
  }

  static show(String msg, {String type}) {
    switch (type) {
      case Toast.SUCCESS:
        EasyLoading.showSuccess(msg);
        break;
      case Toast.ERROR:
        EasyLoading.showError(msg);
        break;
      case Toast.WARNING:
        EasyLoading.showInfo(msg);
        break;
      case Toast.INFO:
      default:
        EasyLoading.showToast(msg);
        break;
    }
  }

  static hide() {
    EasyLoading.dismiss();
  }
}

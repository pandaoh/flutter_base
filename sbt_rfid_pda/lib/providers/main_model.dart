/*
 * @Author: HxB
 * @Date: 2023-11-03 09:56:25
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 09:56:37
 * @Description: 状态管理
 * @FilePath: \rfid_pda\lib\providers\main_model.dart
 */

import 'dart:core';
import 'package:flutter/material.dart';

class MainModel with ChangeNotifier {
  Map _data = {};

  void setProviderData(key, value) {
    _data[key] = value;
    notifyListeners();
  }
}

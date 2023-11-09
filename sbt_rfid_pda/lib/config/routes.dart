/*
 * @Author: HxB
 * @Date: 2023-11-03 10:05:10
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 11:04:19
 * @Description: 路由配置文件
 * @FilePath: \rfid_pda\lib\config\routes.dart
 */
import 'package:flutter/material.dart';
import 'package:sbt_rfid_pda/home_page.dart';
import 'package:sbt_rfid_pda/pages/test_page.dart';
// 导入其他页面

final Map<String, WidgetBuilder> routes = {
  '/': (BuildContext context) => HomePage(),
  'test_page': (BuildContext context) => TestPage(),
};

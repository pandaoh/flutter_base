/*
 * @Author: HxB
 * @Date: 2023-11-03 09:54:47
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-09 15:00:29
 * @Description: 测试页面
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\pages\test_page.dart
 */
import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:sbt_rfid_pda/tools/logger.dart';
import 'package:sbt_rfid_pda/widgets/x_button.dart'; 

class TestPage extends StatefulWidget {
  const TestPage({Key key}) : super(key: key);

  @override
  _TestPageState createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  final List<Map<String, dynamic>> buttons = [
    {
      'name': 'Button 1',
      'key': Key('button1'),
    },
    {
      'name': 'Button 2',
      'key': Key('button2'),
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Test Page'),
      ),
      body: Wrap(
        direction: Axis.horizontal, //排列方向，默认水平方向排列
        alignment: WrapAlignment.center, //子控件在主轴上的对齐方式
        spacing: 10, //主轴上子控件中间的间距
        runAlignment: WrapAlignment.center, //子控件在交叉轴上的对齐方式
        runSpacing: 10, //交叉轴上子控件之间的间距
        crossAxisAlignment: WrapCrossAlignment.center, //交叉轴上子控件的对齐方式
        children: [
          ...buttons.map((button) {
            return XButton(
              name: button['name'],
              key: button['key'],
              onClick: () async {
                Logger.logInfo('${button['name']} 点击');
                EasyLoading.showSuccess('${button['name']} 点击');
              },
            );
          }).toList(),
        ],
      ),
    );
  }
}

/*
 * @Author: HxB
 * @Date: 2023-11-03 10:25:42
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 11:03:22
 * @Description: 按钮封装
 * @FilePath: \rfid_pda\lib\widgets\x_button.dart
 */
import 'package:flutter/material.dart';

class XButton extends StatelessWidget {
  final String name;
  final Key key;
  final VoidCallback onClick;
  final double width;
  final Color color;
  final Color textColor;
  final double fontSize;
  final double borderRadius;
  final EdgeInsets padding;
  final BorderSide borderSide;

  const XButton({
    this.name,
    this.key,
    this.onClick,
    this.width,
    this.color = Colors.blue,
    this.textColor = Colors.white,
    this.fontSize = 16.0,
    this.borderRadius = 8.0,
    this.padding = const EdgeInsets.all(10.0),
    this.borderSide,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      child: ElevatedButton(
        key: key,
        onPressed: onClick,
        style: ElevatedButton.styleFrom(
          primary: color,
          onPrimary: textColor,
          textStyle: TextStyle(fontSize: fontSize),
          padding: padding,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(borderRadius),
            side: borderSide ?? BorderSide.none,
          ),
        ),
        child: Text(name),
      ),
    );
  }
}

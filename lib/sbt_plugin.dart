import 'dart:async';

import 'package:flutter/services.dart';

class SbtPlugin {
  static const MethodChannel _channel = MethodChannel('sbt_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static startInventoryTag() {
    _channel.invokeMethod('startInventoryTag');
  }

  static stopInventory() {
    _channel.invokeMethod('stopInventory');
  }

}

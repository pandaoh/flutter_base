/*
 * @Author: HxB
 * @Date: 2023-11-15 17:58:01
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-17 16:13:32
 * @Description: 本地存储封装
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\tools\store.dart
 */
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class Storage {
  static SharedPreferences _preferences;
  static Storage _instance;

  Storage._();

  /// 获取 Storage 实例
  static Future<Storage> getInstance() async {
    if (_instance == null) {
      await _initialize();
      _instance = Storage._();
    }
    return _instance;
  }

  /// 初始化 SharedPreferences
  static Future<void> _initialize() async {
    _preferences = await SharedPreferences.getInstance();
  }

  /// 存储字符串列表
  Future<void> setStringList(String key, List<String> value) async {
    await _preferences.setStringList(key, value);
  }

  /// 获取字符串列表
  List<String> getStringList(String key) {
    return _preferences.getStringList(key) ?? [];
  }

  /// 存储字符串
  Future<void> setString(String key, String value) async {
    await _preferences.setString(key, value);
  }

  /// 获取字符串
  String getString(String key) {
    return _preferences.getString(key) ?? '';
  }

  /// 存储布尔值
  Future<void> setBool(String key, bool value) async {
    await _preferences.setBool(key, value);
  }

  /// 获取布尔值
  bool getBool(String key) {
    return _preferences.getBool(key) ?? false;
  }

  /// 存储整型值
  Future<void> setInt(String key, int value) async {
    await _preferences.setInt(key, value);
  }

  /// 获取整型值
  int getInt(String key) {
    return _preferences.getInt(key) ?? 0;
  }

  /// 存储浮点型值
  Future<void> setDouble(String key, double value) async {
    await _preferences.setDouble(key, value);
  }

  /// 获取浮点型值
  double getDouble(String key) {
    return _preferences.getDouble(key) ?? 0.0;
  }

  /// 存储 Map
  Future<void> setMap(String key, Map<String, dynamic> value) async {
    final encodedValue = jsonEncode(value);
    await _preferences.setString(key, encodedValue);
  }

  /// 获取 Map
  Map<String, dynamic> getMap(String key) {
    final encodedValue = _preferences.getString(key);
    if (encodedValue != null) {
      final decodedValue = jsonDecode(encodedValue);
      if (decodedValue is Map<String, dynamic>) {
        return decodedValue;
      }
    }
    return {};
  }

  /// 移除键值对
  Future<void> remove(String key) async {
    await _preferences.remove(key);
  }

  /// 清除所有键值对
  Future<void> clear() async {
    await _preferences.clear();
  }
}

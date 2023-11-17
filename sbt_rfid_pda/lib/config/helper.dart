/*
 * @Author: HxB
 * @Date: 2023-11-03 10:05:10
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-17 16:24:19
 * @Description: 全局通用方法
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\config\helper.dart
 */
import 'dart:convert';
import 'package:crypto/crypto.dart';

class Helper {
  /// Base64 编码
  static String Base64Encode(String input) {
    return base64.encode(utf8.encode(input));
  }

  /// Base64 解码
  static String Base64Decode(String input) {
    return utf8.decode(base64.decode(input));
  }

  /// MD5 加密
  static String MD5(String input) {
    var bytes = utf8.encode(input);
    var digest = md5.convert(bytes);
    return digest.toString();
  }

  /// SHA-256 加密
  static String SHA256(String input) {
    var bytes = utf8.encode(input);
    var digest = sha256.convert(bytes);
    return digest.toString();
  }

  /// HMAC 加密
  static String HMAC(String input, String key) {
    var bytes = utf8.encode(input);
    var hmac = Hmac(sha256, utf8.encode(key));
    var digest = hmac.convert(bytes);
    return digest.toString();
  }

  static String getApiUrl() {
    return 'http://192.168.110.13';
  }
}

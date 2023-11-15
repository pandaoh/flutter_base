/*
 * @Author: HxB
 * @Date: 2023-11-03 10:10:35
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-15 10:54:04
 * @Description: 日志打印工具封装
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\tools\logger.dart
 */
import 'dart:async';
import 'dart:developer';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:sbt_rfid_pda/config/global.dart';

class Logger {
  static Future<void> logError(dynamic error, StackTrace stackTrace) async {
    final errorInfo = _formatError(error, stackTrace);
    await _writeLogToFile(errorInfo);
    if (Global.IS_DEV) {
      log(errorInfo); // 在控制台打印错误信息
    }
  }

  static Future<void> logInfo(String message) async {
    final logInfo = _formatLog('INFO', message);
    await _writeLogToFile(logInfo);
    if (Global.IS_DEV) {
      print(logInfo); // 在控制台打印日志信息
    }
  }

  static Future<void> logDebug(String message) async {
    final logInfo = _formatLog('DEBUG', message);
    await _writeLogToFile(logInfo);
    if (Global.IS_DEV) {
      log(logInfo); // 在控制台打印日志信息
    }
  }

  static Future<void> _writeLogToFile(String logInfo) async {
    try {
      // getApplicationDocumentsDirectory
      final appDir = await getExternalStorageDirectory();
      final dateDirectoryName = DateTime.now().toString().split(' ')[0];
      final logDirectory = Directory('${appDir.path}/logs');
      if (!(await logDirectory.exists())) {
        await logDirectory.create();
      }
      final dateDirectory = Directory('${appDir.path}/logs/$dateDirectoryName');
      if (!(await dateDirectory.exists())) {
        await dateDirectory.create();
      }

      final timeFileName = DateTime.now().hour.toString();
      final logFile = File('${dateDirectory.path}/$timeFileName.txt');
      if (!(await logFile.exists())) {
        await logFile.create();
      }

      // 上报到云端
      await logFile.writeAsString('$logInfo\n\n', mode: FileMode.append);
    } catch (e) {
      // 上报到云端
      if (Global.IS_DEV) {
        log('Error writing log file: $e');
      }
    }
  }

  static String _formatError(dynamic error, StackTrace stackTrace) {
    final timestamp = DateTime.now().toString();
    final errorString = error.toString();
    final stackTraceString = stackTrace?.toString() ?? '';
    return '【\n($timestamp) [全局捕获 ERROR]: $errorString\n\n$stackTraceString\n】';
  }

  static String _formatLog(String level, String message) {
    final timestamp = DateTime.now().toString();
    return '($timestamp) [$level]: $message';
  }
}

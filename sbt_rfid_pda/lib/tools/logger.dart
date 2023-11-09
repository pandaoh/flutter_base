/*
 * @Author: HxB
 * @Date: 2023-11-03 10:10:35
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 11:45:21
 * @Description: 日志打印工具封装
 * @FilePath: \rfid_pda\lib\tools\logger.dart
 */
import 'dart:async';
import 'dart:developer';
import 'dart:io';
import 'package:path_provider/path_provider.dart';

class Logger {
  static Future<void> logError(dynamic error, StackTrace stackTrace) async {
    final errorInfo = _formatError(error, stackTrace);
    await _writeLogToFile(errorInfo);
    log(errorInfo); // 在控制台打印错误信息
  }

  static Future<void> logInfo(String message) async {
    final logInfo = _formatLog('INFO', message);
    await _writeLogToFile(logInfo);
    print(logInfo); // 在控制台打印日志信息
  }

  static Future<void> logDebug(String message) async {
    final logInfo = _formatLog('DEBUG', message);
    await _writeLogToFile(logInfo);
    log(logInfo); // 在控制台打印日志信息
  }

  static Future<void> _writeLogToFile(String logInfo) async {
    try {
      // getApplicationDocumentsDirectory
      final appDir = await getExternalStorageDirectory();
      final dateDirectoryName = DateTime.now().toString().split(' ')[0];
      final dateDirectory = Directory('${appDir.path}/$dateDirectoryName');
      if (!(await dateDirectory.exists())) {
        await dateDirectory.create();
      }

      final timeFileName = DateTime.now().hour.toString();
      final logFile = File('${dateDirectory.path}/$timeFileName.txt');
      if (!(await logFile.exists())) {
        await logFile.create();
      }

      await logFile.writeAsString('$logInfo\n\n', mode: FileMode.append);
    } catch (e) {
      log('Error writing log file: $e');
    }
  }

  static String _formatError(dynamic error, StackTrace stackTrace) {
    final errorString = error.toString();
    final stackTraceString = stackTrace?.toString() ?? '';
    return 'ERROR: $errorString\n\n$stackTraceString';
  }

  static String _formatLog(String level, String message) {
    final timestamp = DateTime.now().toString();
    return '$timestamp [$level]: $message';
  }
}

/*
 * @Author: HxB
 * @Date: 2023-11-15 18:00:07
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-17 15:54:23
 * @Description: 文件管理
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\tools\fs.dart
 */
import 'dart:collection';
import 'dart:convert';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:sbt_rfid_pda/tools/logger.dart';

class Fs {
  static final Queue<_FileWriteOperation> _writeQueue = Queue<_FileWriteOperation>();
  static bool _isWriting = false;

  static Future<void> setFileContent(String filePath, String data, {bool append = false}) async {
    final writeOperation = _FileWriteOperation(filePath, data, append);
    _writeQueue.add(writeOperation);

    if (!_isWriting) {
      _isWriting = true;
      await _processWriteQueue();
    }
  }

  static Future<void> _processWriteQueue() async {
    while (_writeQueue.isNotEmpty) {
      final writeOperation = _writeQueue.first;
      await _writeToFile(writeOperation);
      _writeQueue.removeFirst();
    }
    _isWriting = false;
  }

  static Future<void> _writeToFile(_FileWriteOperation writeOperation) async {
    try {
      final file = File(writeOperation.filePath);
      await createFile(writeOperation.filePath);
      if (writeOperation.append) {
        await file.writeAsString(writeOperation.data, mode: FileMode.append, encoding: utf8);
      } else {
        await file.writeAsString(writeOperation.data, encoding: utf8);
      }
    } catch (e) {
      Logger.logDebug('Error writing data to file: $e');
    }
  }

  static Future<Directory> getApplicationDocumentsDirectory() async {
    try {
      return await getExternalStorageDirectory();
    } catch (e) {
      Logger.logDebug('Error getting application documents directory: $e');
      return null;
    }
  }

  static Future<void> createFile(String filePath) async {
    try {
      final file = File(filePath);
      if (!await file.exists()) {
        await file.create(recursive: true);
      }
    } catch (e) {
      Logger.logDebug('Error creating file: $e');
    }
  }

  static Future<void> createDirectory(String directoryPath) async {
    try {
      final directory = Directory(directoryPath);
      if (!(await directory.exists())) {
        await directory.create(recursive: true);
      }
    } catch (e) {
      Logger.logDebug('Error creating directory: $e');
    }
  }

  static Future<String> readFileContent(String filePath) async {
    try {
      final file = File(filePath);
      if (await file.exists()) {
        return await file.readAsString();
      } else {
        return null;
      }
    } catch (e) {
      Logger.logDebug('Error reading file content: $e');
    }
    return null;
  }

  static Future<void> clearFileContent(String filePath) async {
    try {
      final file = File(filePath);
      if (await file.exists()) {
        await file.writeAsString('');
      }
    } catch (e) {
      Logger.logDebug('Error clearing file content: $e');
    }
  }

  static Future<void> deleteFile(String filePath) async {
    try {
      final file = File(filePath);
      if (await file.exists()) {
        await file.delete();
      }
    } catch (e) {
      Logger.logDebug('Error deleting file: $e');
    }
  }

  static Future<void> deleteDirectory(String directoryPath) async {
    try {
      final directory = Directory(directoryPath);
      if (await directory.exists()) {
        await directory.delete(recursive: true);
      }
    } catch (e) {
      Logger.logDebug('Error deleting directory: $e');
    }
  }
}

class _FileWriteOperation {
  final String filePath;
  final String data;
  final bool append;

  _FileWriteOperation(this.filePath, this.data, this.append);
}

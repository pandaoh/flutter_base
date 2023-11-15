/*
 * @Author: HxB
 * @Date: 2023-11-09 14:58:23
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-15 17:44:33
 * @Description: event bus
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\tools\event_bus.dart
 */

class EventBus {
  final Map<String, List<Function>> _callBackMap = <String, List<Function>>{};

  Future<void> addCallBack(String eventKey, Function callback) async {
    if (_callBackMap[eventKey] == null) {
      _callBackMap[eventKey] = [];
    }
    if (!await hasCallBack(eventKey, callback)) {
      _callBackMap[eventKey].add(callback);
    }
  }

  Future<bool> hasCallBack(String eventKey, Function callBack) async {
    if (_callBackMap[eventKey] == null) {
      return false;
    }
    return _callBackMap[eventKey].contains(callBack);
  }

  Future<void> removeCallBack(String eventKey, Function callBack) async {
    if (_callBackMap[eventKey] == null) {
      return;
    }
    _callBackMap[eventKey].removeWhere((callBackFunc) => callBackFunc == callBack);
  }

  Future<void> dispatch(String eventKey, {var data}) async {
    if (_callBackMap[eventKey] == null) {
      throw Exception('未找到回调事件 $eventKey 的监听');
    }
    for (var callBackFunc in _callBackMap[eventKey]) {
      callBackFunc(data);
    }
  }
}

/*
 * @Author: wuxh
 * @Date: 2020-12-25 14:48:01
 * @LastEditTime: 2020-12-25 16:39:19
 * @LastEditors: wuxh
 * @Description: 
 * @FilePath: /cwrfid/lib/base.dart
 */
///订阅者回调签名
typedef void EventCallback(arg);

class EventBus {
  //私有构造函数
  EventBus._internal();

  //保存单例
  static EventBus _singleton = new EventBus._internal();

  //工厂构造函数
  factory EventBus()=> _singleton;

  //保存事件订阅者队列，key:事件名(id)，value: 对应事件的订阅者队列
  var _emap = new Map<Object, List<EventCallback>>();

  //添加订阅者
  void on(eventName, EventCallback f) {
    if (eventName == null || f == null) return;
    // _emap[eventName] ??= new List<EventCallback>();
    _emap[eventName] ??= new List<EventCallback>();
    _emap[eventName].add(f);
  }

  //移除订阅者
  void off(eventName, [EventCallback f]) {
    var list = _emap[eventName];
    if (eventName == null || list == null) return;
    if (f == null) {
      _emap[eventName] = null;
    } else {
      list.remove(f);
    }
  }

  //触发事件，事件触发后该事件所有订阅者会被调用
  void emit(eventName, [arg]) {
    var list = _emap[eventName];
    if (list == null) return;
    int len = list.length - 1;
    //反向遍历，防止订阅者在回调中移除自身带来的下标错位 
    for (var i = len; i > -1; --i) {
      list[i](arg);
    }
  }
}

//定义一个top-level（全局）变量，页面引入该文件后可以直接使用bus
// var bus = new EventBus();


//事件派发及监听使用
class Call {

  static Map<String, List<Function>> _callMap = Map<String, List<Function>>();

  static Future<void> addCallBack(String type, Function callback) async {
    if(_callMap[type] == null ) {
      _callMap[type] = [];
    }
    if(await hasCallBack(type, callback) == false) {
      _callMap[type].add(callback);
    }
  }

  static Future<bool> hasCallBack(type,Function callBack) async {
    if(_callMap[type] == null ) {
      return false;
    }
    return _callMap[type].contains(callBack);
  }

  static Future<void> removeCallBack(type,Function callBack) async {
    if(_callMap[type] == null ) {
      return;
    }
    _callMap[type].removeWhere((element) => element == callBack);
  }

  static Future<void> dispatch(String type, {dynamic data = null }) async {
    if(_callMap[type] == null) {
      throw Exception('回调事件 $type 没有监听，发送失败');
    }
    _callMap[type].forEach((element) {
      element(data);
    });
  }

}
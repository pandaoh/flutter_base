/*
 * @Author: HxB
 * @Date: 2023-11-03 10:03:46
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-15 10:00:46
 * @Description: 全局配置文件
 * @FilePath: \sbt_rfid_pda\sbt_rfid_pda\lib\config\global.dart
 */
class Global {
  /// 全局防抖时间
  static const int DELAY_TIME = 2800;

  /// 是否开发环境
  static const bool IS_DEV = !bool.fromEnvironment('dart.vm.product');
}

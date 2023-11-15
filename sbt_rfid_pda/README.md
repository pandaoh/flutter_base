# rfid_pda_flutter_demo

> 新设备 PDA 重构

## 启动

> `flutter run` or `F5 By VsCode`

## 打包

> `npm run build`

## 目录说明

```txt
android-安卓文件目录
ios-苹果文件目录
linux-Linux 文件目录
web-Web 文件目录

lib-主程序代码
-config-全局配置目录
-pages-页面定义目录
-providers-状态管理
-tools-工具库
-widgets-组件库

pubspec.yaml-依赖配置文件
package.json-Node 脚本管理
README.md-项目说明文件
```

## 开发注意事项

* 推荐环境：`Flutter 2.5.3`、`Dart 2.14.4`、`java version "1.8.0_221"`
* 正式环境打包记得需**配置证书**
* 修改依赖配置文件需会重新执行 `flutter pub get`
* 截屏指令 `flutter screenshot`
* 清除缓存 `flutter clean`
* 录屏 `adb shell screenrecord /sdcard/test.mp4` 执行开始录屏，CTRL+C终止录屏，后面为保存路径（adb设备目录）。

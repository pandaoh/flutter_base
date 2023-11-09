# sbt_rfid_pda

> 智能制造新设备 PDA 重构

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
* [Flutter 环境搭建参考](https://sharing8.yuque.com/vi2gfy/ii2v9m/owofbq)
* 正式环境打包需**配置证书**
* 修改依赖配置文件需会重新执行 `flutter pub get`
* 截屏指令 `flutter screenshot`

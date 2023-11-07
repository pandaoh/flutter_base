# sbt_plugin

## 目录说明

```txt
android-安卓插件代码目录（已使用 java 替换 kotlin）
lib-安卓插件主程序，通过 MethodChannel 暴露安卓底层方法给 flutter。

sbt_rfid_pda-思必拓 RFID APP 目录
-android-思必拓 RFID APP 安卓目录
-lib-思必拓 RFID APP 主程序
```

> 开发插件只需修改根目录的 android 代码即可，运行调试 APP 均在 `sbt_rfid_pda/lib` 下进行。

* 注意：`开发完成插件后，需在根目录 lib 下提供暴露方法，方便 sbt_rfid_pda 目录 下的 app 调用插件。

## 参考资料

> PDA 参数：https://www.speedata.cn/show_service/id/51
> 开发环境 Tips：Flutter 2.5.3Dart 2.14.4
> Flutter 插件开发原生 gradle 运行报错问题参考
> https://blog.csdn.net/a1004084857/article/details/124601320
> 文档介绍：https://sharing8.yuque.com/vi2gfy/il6y00/usd57lb5w6le2rk3

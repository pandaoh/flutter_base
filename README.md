# sbt_plugin

## 目录说明

```txt
android-安卓插件代码目录（已使用 java 替换 kotlin）
lib-安卓插件主程序，通过 MethodChannel 暴露安卓底层方法给 flutter。

sbt_rfid_pda-APP 目录
-android-APP 安卓目录
-lib-APP 主程序
```

> 开发插件只需修改根目录的 android 代码即可，运行调试 APP 均在 `sbt_rfid_pda/lib` 下进行。

* 注意：`开发完成插件后，需在根目录 lib 下提供暴露方法，方便 sbt_rfid_pda 目录 下的 app 调用插件。

## 参考资料

* PDA 参数：https://www.speedata.cn/show_service/id/51
* 开发环境 Tips：Flutter 2.5.3Dart 2.14.4
* Flutter 插件开发原生 gradle 运行报错问题参考
* https://blog.csdn.net/a1004084857/article/details/124601320
* 文档介绍：https://sharing8.yuque.com/vi2gfy/il6y00/usd57lb5w6le2rk3

## @TODO

[x] 本地插件集成
[x] 日志本机集成
[x] UI 组件集成
[x] 常用插件集成
[x] 异常处理与捕获
[x] 全局统一数据管理
[x] 全局统一状态管理
[ ] 全局统一请求管理
[ ] 全量更新
[ ] EventBus
[ ] 拦截返回退出 APP
[ ] 登录页
[ ] 首页
[ ] appBar 导航栏、启动图、图标
[x] 加密解密算法插件
[ ] apk 生产环境证书签名
[ ] apk 授权认证系统-device_info
[ ] 日志上传云端
[ ] TTS
[ ] JSON-VIEW
[ ] SliverHeader

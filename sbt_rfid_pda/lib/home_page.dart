/*
 * @Author: HxB
 * @Date: 2023-11-03 09:53:10
 * @LastEditors: DoubleAm
 * @LastEditTime: 2023-11-03 11:01:13
 * @Description: 首页
 * @FilePath: \rfid_pda\lib\home_page.dart
 */
import 'package:flutter/material.dart';
import 'package:sbt_rfid_pda/config/routes.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('RFID 测试 Demo'),
      ),
      body: ListView.builder(
        itemCount: routes.length,
        itemBuilder: (context, index) {
          String routeName = routes.keys.elementAt(index);
          WidgetBuilder routeBuilder = routes[routeName];
          return ListTile(
            title: Text(routeName),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: routeBuilder),
              );
            },
          );
        },
      ),
    );
  }
}

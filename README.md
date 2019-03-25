# Squirrel text extractor
> A simple util for extracting strings from a huge text.    

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Install](#install)
* [Get started](#get-started)
* [How to implement a WidgetParser](#how-to-implement-a-widgetparser)
* [How to add a click listener](#how-to-add-a-click-listener)
* [Documents](#documents)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)
* [Inspiration](#inspiration)
* [Contact](#contact)

## General info

> We always use regex expression for extracting strings, but regex expression is hard to learn. This is a very easy util for  extracting strings from a huge text.     
    
For example, this is a search result, we want to extract the search results.  
<img src="./img/img1.png" width="800">

The following is our extract result.
<img src="./img/img2.png" width="800">


[Currently support flutter widgets and properties](WIDGETS.md)

## Screenshots
<img src="./img/demo1.gif" width="400">

## Install
#### 1. Depend on it
Add this to your package's pubspec.yaml file:
```
dependencies:
  dynamic_widget: ^0.0.1
```

#### 2. Install it
You can install packages from the command line:

with Flutter:
```
$ flutter packages get
```

Alternatively, your editor might support `flutter packages get`. Check the docs for your editor to learn more.

#### 3. Import it
Now in your Dart code, you can use:
```dart
import 'package:dynamic_widget/dynamic_widget.dart';
```

## Get started
You should use `DynamicWidgetBuilder().build` method to covert a json string into flutter widget. It will be time-consuming. so you'd better using `FutureBuilder` to build the UI.

```dart
import 'package:dynamic_widget/dynamic_widget.dart';
class PreviewPage extends StatelessWidget{

  final String jsonString;

  PreviewPage(this.jsonString);


  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          // Here we take the value from the MyHomePage object that was created by
          // the App.build method, and use it to set our appbar title.
          title: Text("Preview"),
        ),
        body: FutureBuilder<Widget>(
          future: _buildWidget(),
          builder: (BuildContext context, AsyncSnapshot<Widget> snapshot){
            return snapshot.hasData?snapshot.data:Text("Loading...");
          },
        ),
    );
  }

  Future<Widget> _buildWidget() async{

    return DynamicWidgetBuilder().build(jsonString, null);
  }
}
```
## How to implement a WidgetParser
1. You need to implement the `WidgetParser` abstract class.
2. Add new created WidgetParser to `DynamicWidgetBuilder.parsers` list.

This is a RaisedButton widget parser.
```dart
import 'package:dynamic_widget/dynamic_widget/utils.dart';
import 'package:dynamic_widget/dynamic_widget.dart';
import 'package:flutter/material.dart';

class RaisedButtonParser extends WidgetParser{
  @override
  bool forWidget(String widgetName) {
    return "RaisedButton" == widgetName;
  }

  @override
  Widget parse(Map<String, dynamic> map) {
    return RaisedButton(
      color: map.containsKey('color') ? parseHexColor(map['color']) : null,
      disabledColor: map.containsKey('disabledColor') ? parseHexColor(map['disabledColor']) : null,
      disabledElevation: map.containsKey('disabledElevation') ? map['disabledElevation'] : 0.0,
      disabledTextColor: map.containsKey('disabledTextColor') ? parseHexColor(map['disabledTextColor']) : null,
      elevation: map.containsKey('elevation') ? map['elevation'] : 0.0,
      padding: map.containsKey('padding') ? parseEdgeInsetsGeometry(map['padding']) : null,
      splashColor : map.containsKey('splashColor') ? parseHexColor(map['splashColor']) : null,
      textColor: map.containsKey('textColor') ? parseHexColor(map['textColor']) : null,
      child: DynamicWidgetBuilder().buildFromMap(map['child']),
      onPressed: (){},
    );
  }

}
```

Add it to parsers list.
```dart
class DynamicWidgetBuilder{

  final Logger log = Logger('DynamicWidget');

  static final parsers = [
    ContainerWidgetParser(),
    TextWidgetParser(),
    RaisedButtonParser(),
    RowWidgetParser()
  ];


  Widget build(String json){

    var map = jsonDecode(json);
    return buildFromMap(map);
  }

  Widget buildFromMap(Map<String, dynamic> map){
    String widgetName = map['type'];

    for (var parser in parsers) {
      if (parser.forWidget(widgetName)) {
        return parser.parse(map);
      }
    }

    log.warning("Not support type: $widgetName");
    return null;
  }

  List<Widget> buildWidgets(List<dynamic> values){
    List<Widget> rt = [];
    for (var value in values) {
      rt.add(buildFromMap(value));
    }
    return rt;
  }
}
```
## How to add a click listener
Add "click_event" property to your widget json definition. for example:
```dart
var raisedButton_json =
'''
{
  "type": "Container",
  "alignment": "center",
  "child": {
    "type": "RaisedButton",
    "color": "##FF00FF",
    "padding": "8,8,8,8",
    "textColor": "#00FF00",
    "elevation" : 8.0,
    "splashColor" : "#00FF00",
    "click_event" : "route://productDetail?goods_id=123",
    "child" : {
      "type": "Text",
      "data": "I am a button"
    }  
  }
}
```

We suggest you'd better to use an URI to define the event, as the exmaple, it's a event for going to a product detail page.

Then, define a ClickListener
```dart
class DefaultClickListener implements ClickListener{
  @override
  void onClicked(String event) {
    print("Receive click event: " + event);
  }

}
```

Finally, pass the listener to build method.
```dart
  Future<Widget> _buildWidget() async{

    return DynamicWidgetBuilder().build(jsonString, new DefaultClickListener());
  }
```
  

## Documents
[Currently support widgets and properties](WIDGETS.md)

## Setup
Checkout this project and run deom.

## Code Examples
Checkout this project and run deom.

## Features
Already completed widgets:
* Container
* Text & TextSpan
* Row & Column
* AssetImage
* NetworkImage
* FileImage
* Placeholder
* GridView
* ListView
* PageView
* Expanded
* Padding
* Center
* Align
* AspectRatio 
* FittedBox
* Baseline


You can view [Currently support widgets and properties](WIDGETS.md) here.


## Contact
Created by [@deng.yin@gmail.com](https://www.jianshu.com/u/efa51344ce61) - feel free to contact me!
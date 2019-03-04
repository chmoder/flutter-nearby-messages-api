import 'dart:async';

import 'package:flutter/services.dart';

class NearbyMessagesApi {
  MethodChannel _channel = const MethodChannel('nearby_messages_api');

  NearbyMessagesApi(){
    _channel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case 'onFound':
          return '${call.arguments}';
        case 'onLost':
          return '${call.arguments}';
        default:
          throw MissingPluginException();
      }
    });
  }

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<void> backgroundSubscribe() async {
    await _channel.invokeMethod('backgroundSubscribe');
  }

  Future<void> backgroundUnsubscribe() async {
    await _channel.invokeMethod('backgroundUnsubscribe');
  }
}
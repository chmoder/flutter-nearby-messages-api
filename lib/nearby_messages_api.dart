import 'dart:async';

import 'package:flutter/services.dart';

class NearbyMessagesApi {
  static const MethodChannel _channel =
      const MethodChannel('nearby_messages_api');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> backgroundSubscribe() async {
    await _channel.invokeMethod('backgroundSubscribe');
  }

  static Future<void> backgroundUnsubscribe() async {
    await _channel.invokeMethod('backgroundUnsubscribe');
  }
}

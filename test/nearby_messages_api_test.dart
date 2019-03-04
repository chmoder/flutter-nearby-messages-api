import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nearby_messages_api/nearby_messages_api.dart';

void main() {
  const MethodChannel channel = MethodChannel('nearby_messages_api');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    NearbyMessagesApi nearbyMessagesApi = new NearbyMessagesApi();
    expect(await nearbyMessagesApi.platformVersion, '42');
  });
}

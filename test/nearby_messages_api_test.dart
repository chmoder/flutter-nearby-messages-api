import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nearby_messages_api/nearby_messages_api.dart';

void main() {
  const MethodChannel channel = MethodChannel('nearby_messages_api');

  /**
   * This method gets the message when an a nearby device sends one
   */
  void onFound(dynamic arguments) {
  print(arguments);
  }

  /**
   * This method gets the message when a nearby device leaves
   */
  void onLost(dynamic arguments) {
  print(arguments);
  }

  NearbyMessagesApi nearbyMessagesApi = NearbyMessagesApi(onFound, onLost);

  Future<void> main() async {
    await nearbyMessagesApi.backgroundSubscribe();
  }

  Future<void> dispose() async {
    await nearbyMessagesApi.backgroundUnsubscribe();
  }

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

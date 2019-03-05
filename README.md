# Nearby Messages API

Google Nearby Messages API for Flutter

### Installation
First, add `nearby_messages_api` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

### iOS
Coming Soon

### Android
Get and add a google cloud nearby messages API key to your project [https://developers.google.com/nearby/messages/android/get-started](https://developers.google.com/nearby/messages/android/get-started)

### Example
```dart
import 'package:nearby_messages_api/nearby_messages_api.dart';

class main {
    /**
    * This method gets the message when an a nearby device sends one
    */
    static void onFound(dynamic arguments) {
      print(arguments);
    }
    
    /**
    * This method gets the message when a nearby device leaves
    */
    static void onLost(dynamic arguments) {
      print(arguments);
    }
    
    NearbyMessagesApi nearbyMessagesApi = NearbyMessagesApi(onFound, onLost);
    
    Future<void> main() async {
      await nearbyMessagesApi.backgroundSubscribe();
    }
    
    Future<void> dispose() async {
      await nearbyMessagesApi.backgroundUnsubscribe();
    }
}
```

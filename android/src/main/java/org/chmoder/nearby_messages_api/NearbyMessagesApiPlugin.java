package org.chmoder.nearby_messages_api;

import android.app.Activity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** NearbyMessagesApiPlugin */
public class NearbyMessagesApiPlugin implements MethodCallHandler {
  private NearbyMessagesApiMCMs nearbyMessagesApiMcms;

  private NearbyMessagesApiPlugin(Activity activity) {
    this.nearbyMessagesApiMcms = new NearbyMessagesApiMCMs(activity);
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "nearby_messages_api");
    channel.setMethodCallHandler(new NearbyMessagesApiPlugin(registrar.activity()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
      switch (call.method) {
          case "getPlatformVersion":
              result.success("Android " + android.os.Build.VERSION.RELEASE);
              break;
          case "backgroundSubscribe":
              nearbyMessagesApiMcms.backgroundSubscribe();
              break;
          case "backgroundUnsubscribe":
              nearbyMessagesApiMcms.backgroundUnsubscribe();
              break;
          default:
              result.notImplemented();
              break;
      }
  }
}

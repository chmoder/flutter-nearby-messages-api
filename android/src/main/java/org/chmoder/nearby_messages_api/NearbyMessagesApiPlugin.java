package org.chmoder.nearby_messages_api;

import android.app.Activity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * NearbyMessagesApiPlugin
 */
public class NearbyMessagesApiPlugin implements MethodCallHandler {
    private NearbyMessagesApiMCMs nearbyMessagesApiMcms;

    // Channel is a static field because it needs to be accessible to the
    // {@link ShortcutHandlerActivity} which has to be a static class with
    // no-args constructor.
    // It is also mutable because it is derived from {@link Registrar}.
    private static MethodChannel channel;
    private static MethodChannel messageReceiverChannel;

    private NearbyMessagesApiPlugin(Activity activity) {
        this.nearbyMessagesApiMcms = new NearbyMessagesApiMCMs(activity);
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "nearby_messages_api");
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
                result.success(null);
                break;
            case "backgroundUnsubscribe":
                nearbyMessagesApiMcms.backgroundUnsubscribe();
                result.success(null);
                break;
            default:
                result.notImplemented();
        }
    }
}

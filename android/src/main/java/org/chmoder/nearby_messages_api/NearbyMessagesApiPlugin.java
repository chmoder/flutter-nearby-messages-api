package org.chmoder.nearby_messages_api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.ContentValues.TAG;

/**
 * NearbyMessagesApiPlugin
 */
public class NearbyMessagesApiPlugin extends BroadcastReceiver implements MethodCallHandler {
    private final Activity activity;
    private final Registrar registrar;
    private final MethodChannel channel;
    private Message mActiveMessage;
    private PendingIntent pendingIntent;

    /**
     * set up the broadcast receiver and nearby message listener
     * @param activity
     * @param registrar
     * @param methodChannel
     */
    private NearbyMessagesApiPlugin(Activity activity, Registrar registrar, MethodChannel methodChannel) {
        this.activity = activity;
        this.registrar = registrar;
        this.channel = methodChannel;

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(registrar.context());
        manager.registerReceiver(this, new IntentFilter());
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(
                registrar.messenger(),
                "nearby_messages_api"
        );
        final NearbyMessagesApiPlugin plugin = new NearbyMessagesApiPlugin(
                registrar.activity(),
                registrar,
                channel
        );
        channel.setMethodCallHandler(plugin);
    }

    /**
     * attache the nearby messages listener to a broadcast receiver
     * background service can pass messages to the host this way
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found message via PendingIntent: " + message);
                String messageContent = new String(message.getContent());
                channel.invokeMethod("onFound", messageContent);
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost message via PendingIntent: " + message);
                String messageContent = new String(message.getContent());
                channel.invokeMethod("onLost", messageContent);
            }
        });
    }

    /**
     * background receiver subscription
     */
    void backgroundSubscribe() {
        Log.i(TAG, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(activity).subscribe(getPendingIntent(), options);
    }

    /**
     * unsubscribe from broadcast receiver
     */
    void backgroundUnsubscribe() {
        Nearby.getMessagesClient(activity).unsubscribe(getPendingIntent());
        pendingIntent = null;
    }

    /**
     * makes this class a pending intent for the broadcast receiver
     * @return
     */
    private PendingIntent getPendingIntent() {
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getBroadcast(
                    activity,
                    0,
                    new Intent(activity, NearbyMessagesApiPlugin.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

        }
        return pendingIntent;
    }

    void publish() {
        unPublish();

        String deviceId = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mActiveMessage = new Message(deviceId.getBytes());
        Strategy strategy = new Strategy.Builder()
                .setTtlSeconds(Strategy.TTL_SECONDS_MAX)
                .build();
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(strategy)
                .build();
        Nearby.getMessagesClient(activity).publish(mActiveMessage, options);
    }

    void unPublish() {
        if (mActiveMessage != null) {
            Nearby.getMessagesClient(activity).unpublish(mActiveMessage);
            mActiveMessage = null;
        }
    }

    /**
     * Host will invoke these methods to set up the nearby messages API listener
     * @param call
     * @param result
     */
    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "backgroundSubscribe":
                this.backgroundSubscribe();
                result.success(null);
                break;
            case "backgroundUnsubscribe":
                this.backgroundUnsubscribe();
                result.success(null);
                break;
            case "publish":
                this.publish();
                result.success(null);
                break;
            case "unPublish":
                this.unPublish();
                result.success(null);
                break;
            default:
                result.notImplemented();
        }
    }
}

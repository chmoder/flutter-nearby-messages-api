package org.chmoder.nearby_messages_api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import io.flutter.plugin.common.MethodChannel;

import static android.content.ContentValues.TAG;

public class NearbyMessageReceiver extends BroadcastReceiver {
    MethodChannel channel;
    MethodChannel.Result result;

    public NearbyMessageReceiver(MethodChannel channel, MethodChannel.Result result) {
        this.channel = channel;
        this.result = result;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found message via PendingIntent: " + message);
                channel.invokeMethod("onFound", message);
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost message via PendingIntent: " + message);
                channel.invokeMethod("onLost", message);
            }
        });
    }
}

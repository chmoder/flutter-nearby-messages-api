package org.chmoder.nearby_messages_api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import static android.content.ContentValues.TAG;

class NearbyMessagesApiMCMs {
    private Activity activity;

    NearbyMessagesApiMCMs(Activity activity) {
        this.activity = activity;
    }

    void backgroundSubscribe() {
        Log.i(TAG, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(activity).subscribe(getPendingIntent(), options);
    }

    void backgroundUnsubscribe() {
        Nearby.getMessagesClient(activity).unsubscribe(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(
                activity,
                0,
                new Intent(activity, NearbyMessageReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

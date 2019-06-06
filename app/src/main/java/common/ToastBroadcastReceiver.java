package common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import network.Service;

public class ToastBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, Service.class);
        context.startService(serviceIntent);

    }
}

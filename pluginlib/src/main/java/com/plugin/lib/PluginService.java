package com.plugin.lib;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

public class PluginService extends Service{

    private static final String TAG = "PluginService";

    public void setProxy(Service hostService, Resources hostRes){
        Log.e(TAG, "setProxy: " );
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }
}

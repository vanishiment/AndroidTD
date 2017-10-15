package com.plugin.lib;

import android.content.Intent;

public abstract class HandleService {

    public abstract void onStartCommand(Intent intent, int flags, int startId);

}

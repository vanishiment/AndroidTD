package com.loader.lib.loader.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.loader.lib.loader.proxy.ProxyService;

/*
* 接收广播，并将接收到的广播通过 ProxyService 转发给插件中处理
* */
public class ProxyReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    Intent serviceIntent = new Intent();
    serviceIntent.setClassName(context, ProxyService.class.getName());
    serviceIntent.putExtra("apk_action",action);
    serviceIntent.putExtra("apk_service_id",0);
    context.startService(serviceIntent);
  }
}

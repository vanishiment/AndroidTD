package com.loader.lib;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.loader.lib.utils.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ProxyService extends Service {

    private static final String TAG = "ProxyService";

    private static final String TARGET_CLASS = "com.plugin.lib.PluginService";

    private Resources mResource;

    private Service mRemoteService;

    private void loadResource(String zipPath){
        Resources superRes = super.getResources();
        mResource = new Resources(ReflectUtil.getAsset(zipPath),superRes.getDisplayMetrics(),superRes.getConfiguration());
    }

    private void setRemoteService(Object object){
        if (object instanceof Service){
            mRemoteService = (Service) object;
        }else {
            Log.e(TAG, "setRemoteService: can't cast to Service.");
        }
    }

    void launchTargetService(String className, final Intent intent, final int flags, final int startId){
        if (mRemoteService == null){
            final PluginLoader pluginLoader = new PluginLoader(getApplicationContext());
            pluginLoader.loadClass(className, new OnClassLoadListener() {
                @Override
                public void onLoadClass(boolean result, Class<?> clazz) {
                    if (result){
                        loadResource(DexLoader.getInstance().getDexPath());
                        try {
                            Constructor<?> pluginClzCons = clazz.getConstructor();
                            Object instance = pluginClzCons.newInstance();
                            setRemoteService(instance);

                            Method setProxy = clazz.getMethod("setProxy", Service.class,Resources.class);
                            setProxy.setAccessible(true);
                            setProxy.invoke(instance, ProxyService.this,mResource);

                            Method onStartCommand = clazz.getDeclaredMethod("onStartCommand", Intent.class,int.class,int.class);
                            onStartCommand.setAccessible(true);
                            onStartCommand.invoke(instance, intent,flags,startId);

                        } catch (Exception e) {
                            e.printStackTrace();
                            stopSelf();
                        }
                    }else {
                        stopSelf();
                    }
                }
            });
        }else {
            mRemoteService.onStartCommand(intent,flags,startId);
        }
    }

    @Override
    public Resources getResources() {
        return mResource == null ? super.getResources() : mResource;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int from;
        if (intent != null){
            from = intent.getIntExtra("from",0);
            if (from == 0){
                launchTargetService(TARGET_CLASS,intent,flags,startId);
            }else {
                String serviceName = intent.getStringExtra("extra_class");
                if (!TextUtils.isEmpty(serviceName)){
                    launchTargetService(serviceName,intent,flags,startId);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

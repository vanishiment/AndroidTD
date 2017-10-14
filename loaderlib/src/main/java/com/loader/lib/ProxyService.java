package com.loader.lib;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loader.lib.utils.ReflectUtil;

public class ProxyService extends Service {

    private static final String TAG = "ProxyService";

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

    void launchTargetService(String className,Intent intent,int flags,int startId){
        if (mRemoteService == null){

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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

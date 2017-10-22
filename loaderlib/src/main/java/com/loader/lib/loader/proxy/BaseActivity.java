package com.loader.lib.loader.proxy;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import com.loader.lib.loader.core.DexLoader;
import com.loader.lib.loader.core.OnClassLoadListener;
import com.loader.lib.loader.core.PluginLoader;
import com.loader.lib.utils.ReflectUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import static com.loader.lib.loader.extra.IntentParam.Activity.FROM;
import static com.loader.lib.loader.extra.IntentParam.Activity.FROM_LOADER;

public class BaseActivity extends Activity {

  private static final String TAG = "BaseActivity";

  Resources mResource;
  Activity                mRemoteActivity;
  HashMap<String, Method> mActivityLifeCircleMethods = new HashMap<String, Method>();

  void loadResource(String zipPath){
    Resources superRes = super.getResources();
    mResource = new Resources(ReflectUtil.getAsset(zipPath),superRes.getDisplayMetrics(),superRes.getConfiguration());
  }

  void setRemoteActivity(Object activity){
    if (activity instanceof Activity){
      mRemoteActivity = (Activity) activity;
    }else {
      Log.e(TAG, "setRemoteService: can't cast to Service.");
    }
  }

  void launchTargetActivity(String className){
    PluginLoader loader = new PluginLoader(getApplicationContext());
    loader.loadClass(className, new OnClassLoadListener() {
      @Override public void onLoadClass(boolean result, Class<?> clazz) {
        if (result){
          loadResource(DexLoader.getInstance().getDexPath());

          try {
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            setRemoteActivity(instance);

            mActivityLifeCircleMethods = ReflectUtil.instantiateLifeCircleMethods(clazz);

            Method setProxy = clazz.getMethod("setProxy", Activity.class, Resources.class );
            setProxy.setAccessible(true);
            setProxy.invoke(instance, BaseActivity.this, mResource );

            Method onCreate = mActivityLifeCircleMethods.get("onCreate");
            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_LOADER);
            onCreate.invoke(instance, bundle );
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
  }
}

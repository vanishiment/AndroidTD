package com.loader.lib.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;

import android.os.Bundle;
import android.view.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectUtil {

  public static AssetManager getAsset(String zipPath) {
    AssetManager mAssetManager = null;
    try {
      AssetManager assetManager = AssetManager.class.newInstance();
      Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
      addAssetPath.invoke(assetManager, zipPath);
      mAssetManager = assetManager;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mAssetManager;
  }

  public static HashMap<String, Method> instantiateLifeCircleMethods(Class<?> localClass) {
    HashMap<String, Method> mActivityLifeCircleMethods = new HashMap<String, Method>();
    String[] methodNames = new String[] {
        "onRestart", "onStart", "onStop", "onPause", "onResume", "onDestroy"
    };
    for (String methodName : methodNames) {
      Method method = null;
      try {
        method = localClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
      mActivityLifeCircleMethods.put(methodName, method);
    }

    Method onCreate = null;
    try {
      onCreate = localClass.getDeclaredMethod("onCreate", Bundle.class);
      onCreate.setAccessible(true);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    mActivityLifeCircleMethods.put("onCreate", onCreate);

    Method onKeyDown = null;
    try {
      onKeyDown = localClass.getDeclaredMethod("onKeyDown", int.class, KeyEvent.class);
      onKeyDown.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    mActivityLifeCircleMethods.put("onKeyDown", onKeyDown);
    return mActivityLifeCircleMethods;
  }

  public static void exeMethodByName(HashMap<String, Method> ms, Activity a, String name) {
    Method m = ms.get(name);
    if (m != null) {
      try {
        m.invoke(a);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void exeOnActivityResult(HashMap<String, Method> ms, Activity a, String name,
      int requestCode, int resultCode, Intent data) {
    Method onActivityResult = ms.get(name);
    if (onActivityResult != null) {
      try {
        onActivityResult.invoke(a, requestCode, resultCode, data);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean exeOnKeyDown(HashMap<String, Method> ms, Activity a, String name,
      int keyCode, KeyEvent event) {
    Method onKeyDown = ms.get(name);
    if (onKeyDown != null) {
      try {
        return (Boolean) onKeyDown.invoke(a, keyCode, event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return false;
  }
}

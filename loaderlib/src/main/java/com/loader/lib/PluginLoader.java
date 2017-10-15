package com.loader.lib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import dalvik.system.DexClassLoader;

public class PluginLoader {

    private static final String TAG = "PluginLoader";

    private DexLoader mDexLoader;
    private DexClassLoader mDCL;
    private ClassLoader mContextClassLoader;
    private String zipPath;
    private String dexPath;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public PluginLoader(Context context) {
        mDexLoader = DexLoader.getInstance();
        mDCL = mDexLoader.getDexClassLoader();
        mContextClassLoader = context.getClassLoader();
        zipPath = context.getCacheDir().getAbsolutePath() + File.separator + "zip";
        dexPath = context.getCacheDir().getAbsolutePath() + File.separator + "dex";
    }

    public synchronized void loadClass(final String className, final OnClassLoadListener loadListener) {
        if (mDCL == null) {
            if (!mDexLoader.isIsLoading()) {
                mDexLoader.setIsLoading(true);
                new LoaderTask().execute(className,loadListener,zipPath,dexPath);
            } else {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadClass(className, loadListener);
                    }
                }, 1000);
            }
        } else {
            Class<?> clazz;
            try {
                clazz = mDCL.loadClass(className);
                loadListener.onLoadClass(true, clazz);
            } catch (ClassNotFoundException e) {
                try {
                    clazz = mContextClassLoader.loadClass(className);
                    if (clazz != null){
                        loadListener.onLoadClass(true,clazz);
                    }else {
                        loadListener.onLoadClass(false,null);
                    }
                } catch (ClassNotFoundException e1) {
                    loadListener.onLoadClass(false,null);
                }
            }
        }
    }

    private class LoaderTask extends com.loader.lib.AsyncTask<Object,Void,DexClassLoader>{

        private String className;
        private OnClassLoadListener listener;
        String zipPath;
        String dexOutputPath;

        @Override
        protected DexClassLoader doInBackground(Object... params) {
            className = (String) params[0];
            listener = (OnClassLoadListener) params[1];
            zipPath = (String) params[2];
            dexPath = (String) params[3];
            if (!TextUtils.isEmpty(zipPath)){
                mDexLoader.setDexPath(zipPath);
                try {
                    mDCL = new DexClassLoader(zipPath,dexOutputPath,null,ClassLoader.getSystemClassLoader());
                }catch (Exception e){
                    Log.e(TAG, "class load error:" + e.getLocalizedMessage() );
                    File zipFile = new File(zipPath);
                    if (zipFile.exists() && zipFile.isFile()){
                        zipFile.delete();
                    }
                }
            }
            return mDCL;
        }

        @Override
        protected void onPostExecute(DexClassLoader dexClassLoader) {
            super.onPostExecute(dexClassLoader);
            mDexLoader.setIsLoading(false);
            if (dexClassLoader != null){
                mDexLoader.setDexClassLoader(dexClassLoader);

                try {
                    Class<?> clazz = dexClassLoader.loadClass(className);
                    if (clazz != null){
                        listener.onLoadClass(true,clazz);
                    }else {
                        listener.onLoadClass(false,null);
                    }
                } catch (ClassNotFoundException e) {
                    listener.onLoadClass(false,null);
                }
            }else {
                listener.onLoadClass(false,null);
            }
        }
    }
}

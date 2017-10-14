package com.loader.lib;

import dalvik.system.DexClassLoader;

public class PluginLoader {

    private static final String TAG = "PluginLoader";

    private DexLoader mDexLoader;
    private DexClassLoader mDCL;

    public PluginLoader() {
        mDexLoader = DexLoader.getInstance();
        mDCL = mDexLoader.getDexClassLoader();
    }

    public synchronized void loadClass(String className,OnClassLoadListener loadListener){
        if (mDCL == null){
            if (!mDexLoader.isIsLoading()){
                mDexLoader.setIsLoading(true);
                // TODO: 2017/10/14 开始加载 zip
            }else {
                // TODO: 2017/10/14 延时 loadClass
            }
        }else {
            if (!hasUpdateZip()){
                Class<?> clazz;
                try {
                    clazz = mDCL.loadClass(className);
                    loadListener.onLoadClass(true,clazz);
                } catch (ClassNotFoundException e) {
                    // TODO: 2017/10/14 使用 Context 的 ClassLoader 加载 class
                }
            }else {
                mDCL = null;
                loadClass(className, loadListener);
            }
        }
    }

    private boolean hasUpdateZip(){
        return false;
    }


}

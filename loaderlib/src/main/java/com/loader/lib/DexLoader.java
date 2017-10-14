package com.loader.lib;

import dalvik.system.DexClassLoader;

public class DexLoader {

    private DexClassLoader mDCL;
    private boolean mIsLoading;
    private String mDexPath;

    private DexLoader() {
    }

    private static class DexLoaderHolder{
        static final DexLoader DEX_LOADER = new DexLoader();
    }

    public static DexLoader getInstance(){
        return DexLoaderHolder.DEX_LOADER;
    }

    public DexClassLoader getDexClassLoader() {
        return mDCL;
    }

    public void setDexClassLoader(DexClassLoader dexClassLoader) {
        this.mDCL = dexClassLoader;
    }

    public boolean isIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.mIsLoading = isLoading;
    }

    public String getDexPath() {
        return mDexPath;
    }

    public void setDexPath(String dexPath) {
        this.mDexPath = dexPath;
    }
}

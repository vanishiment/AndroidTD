package com.plugin.lib.util;

import android.content.res.Resources;

public class ResIdUtil {

  private Resources mRes;

  private static final class ResHolder{
    static ResIdUtil RES_ID = new ResIdUtil();
  }

  public static ResIdUtil getInstance(){
    return ResHolder.RES_ID;
  }

  public Resources getRes(){
    return mRes;
  }

  public void setRes(Resources res){
    mRes = res;
  }

  public int getResourceId(String resName,String defPackage){
    int id = 0;
    if (null != mRes){
      try {
        String[] strings = resName.split("\\.");
        id = mRes.getIdentifier(strings[2],strings[1],defPackage);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    return id;
  }
}

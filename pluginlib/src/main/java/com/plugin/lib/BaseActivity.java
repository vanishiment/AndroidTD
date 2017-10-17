package com.plugin.lib;

import android.app.Activity;
import android.content.res.Resources;
import com.plugin.lib.util.ResIdUtil;

/*
* 设置 Resources ，获取 Context
* */
public class BaseActivity extends Activity {

  private Activity that;

  public void setProxy(Activity activity,Resources resources){
    ResIdUtil.getInstance().setRes(resources);
    that = activity;
  }

}

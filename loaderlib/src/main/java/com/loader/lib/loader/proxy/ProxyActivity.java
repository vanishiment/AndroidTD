package com.loader.lib.loader.proxy;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import com.loader.lib.utils.ReflectUtil;

import static com.loader.lib.loader.extra.IntentParam.Activity.EXTRA_CLASS;

public class ProxyActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout l = new LinearLayout(this);
    setContentView(l);
    l.setBackgroundColor(Color.TRANSPARENT);
    String mClass = getIntent().getStringExtra(EXTRA_CLASS);
    if (mClass != null) {
      launchTargetActivity(mClass);
    }
  }

  @Override
  public Resources getResources() {
    return mResource == null ? super.getResources() : mResource;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    ReflectUtil.exeOnActivityResult(mActivityLifeCircleMethods, mRemoteActivity, "onActivityResult",requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onStart() {
    super.onStart();
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onStart");
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    boolean down =
        ReflectUtil.exeOnKeyDown(mActivityLifeCircleMethods, mRemoteActivity, "onKeyDown", keyCode,
            event);
    return down || super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onRestart");
  }

  @Override
  protected void onResume() {
    super.onResume();
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onResume");
  }

  @Override
  protected void onPause() {
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onPause");
    super.onPause();
  }

  @Override
  protected void onStop() {
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onStop");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    ReflectUtil.exeMethodByName(mActivityLifeCircleMethods, mRemoteActivity, "onDestroy");
    super.onDestroy();
  }
}

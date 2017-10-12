package com.zhangyn.androidtd;

import android.app.Application;
import android.util.Log;
import timber.log.Timber;

public class AndroidTDApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }else {
      Timber.plant(new CrashReportingTree());
    }
  }

  private static class CrashReportingTree extends Timber.Tree {

    @Override protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }

      FakeCrashLibrary.log(priority, tag, message);

      if (t != null) {
        if (priority == Log.WARN) {
          FakeCrashLibrary.logWarning(t);
        } else if (priority == Log.ERROR) {
          FakeCrashLibrary.logError(t);
        }
      }
    }
  }
}

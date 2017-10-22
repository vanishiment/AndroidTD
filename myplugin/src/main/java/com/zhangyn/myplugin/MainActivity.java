package com.zhangyn.myplugin;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.loader.lib.loader.proxy.ProxyService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.plugin_load).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        loadPlugin();
      }
    });
  }

  private void loadPlugin(){
    loadPluginFromAssets();
    File apk = new File(getFilesDir().getAbsolutePath() + File.separator + "zip" + File.separator + "plugin.apk");
    boolean is = apk.exists();

    if (is) {
      Intent intent = new Intent(MainActivity.this, ProxyService.class);
      intent.putExtra("from",0);
      MainActivity.this.startService(intent);
    } else {
      Toast.makeText(getApplicationContext(), "SDcard根目录未检测到plugin.apk插件", Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void loadPluginFromAssets(){
    AssetManager manager = getAssets();
    File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "zip" + File.separator);
    if (!dir.exists()){
      dir.mkdirs();
    }
    File apk = new File(dir, "plugin.apk");
    if (apk.exists()){
      apk.delete();
    }
    try {
      apk.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Log.e(TAG, "loadPluginFromAssets: " + apk.exists() );
    InputStream is = null;
    FileOutputStream fos = null;
    try {
      is = manager.open("pluginlib-debug.apk");
      fos = new FileOutputStream(apk);
      byte[] buffer = new byte[512];
      int len = -1;
      while ((len = is.read(buffer))!= -1){
        fos.write(buffer,0,len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      if (null != fos){
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (null != is){
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

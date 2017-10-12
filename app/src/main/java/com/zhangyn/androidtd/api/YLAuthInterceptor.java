package com.zhangyn.androidtd.api;

import android.text.TextUtils;
import android.util.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
* 在 tokenAuth 时检查 token 和 macKey，如果为空我们就抛出一个异常（1）。
* 但这其实只能处理我们初始化时存在问题的情况，如果我们被挤下线，导致 token 失效，我们应该怎么处理呢？
* 而进一步抽象这个问题，其实就是 token/macKey 如何管理。
*解决方案其实很简单，我们把 interceptor 作为一个单例依赖，
*首先注入到登录注册模块中，登陆成功之后，我们就为它更新 token/macKey，
*其次我们的 API Error 要有一个集中处理的地方，我们把 interceptor 也注入进去，
*在捕获到 token 失效的错误后，我们就清除 interceptor 的 token/macKey。
*至于 UI 上怎么给用户提示，我们可以在 BaseActivity/BaseFragment 中监听错误的发生，并弹出对话框
* */

public class YLAuthInterceptor implements Interceptor {

  private final String mBaseAuthId;
  private final String mBaseAurhPass;

  private volatile String mToken;
  private volatile String mKey;

  public YLAuthInterceptor(String baseAuthId, String baseAurhPass) {
    this.mBaseAuthId = baseAuthId;
    this.mBaseAurhPass = baseAurhPass;
  }

  public void setAuth(String token, String key) {
    mToken = token;
    mKey = key;
  }

  @Override public Response intercept(Chain chain) throws IOException {

    Request origin = chain.request();
    Headers originHeaders = origin.headers();

    Headers.Builder newHeaders = new Headers.Builder();

    String authType = "Token";
    for (int i = 0; i < originHeaders.size(); i++) {
      if (!TextUtils.equals(originHeaders.name(i), "Auth-Type")) {
        newHeaders.add(originHeaders.name(i), originHeaders.value(i));
      } else {
        authType = originHeaders.value(i);
      }
    }

    Request.Builder newReq = origin.newBuilder().headers(newHeaders.build());
    switch (authType) {
      case "Basic":
        baseAuth(newReq, origin.url(), System.currentTimeMillis());
        break;
      case "Token":
      default:
        tokenAuth(newReq, origin.url(), System.currentTimeMillis());
        break;
    }

    return chain.proceed(newReq.build());
  }

  private void baseAuth(Request.Builder newRequest, HttpUrl url, long timestamp) {

    String text = "timestamp=" + timestamp;
    String macKey = mBaseAuthId + mBaseAurhPass;
    String mac = text + "mac_key=" + macKey;

    HttpUrl.Builder newUrl = url.newBuilder()
        .addQueryParameter("timestamp", String.valueOf(timestamp))
        .addQueryParameter("mac", mac);

    newRequest.url(newUrl.build());

    try {
      String auth = baseAuthHeader(mBaseAuthId, mBaseAurhPass);
      newRequest.addHeader("Authorization", auth);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private String baseAuthHeader(String name, String pw) throws UnsupportedEncodingException {
    String nameAndPw = name + ":" + pw;
    return "Basic" + Base64.encodeToString(nameAndPw.getBytes("UTF-8"), Base64.NO_WRAP);
  }

  private void tokenAuth(Request.Builder newRequest, HttpUrl url, long timestamp) {

    if (TextUtils.isEmpty(mToken) || TextUtils.isEmpty(mKey)) {
      // TODO: 2017/10/12 token or key error.
    }
    String text = "token=" + mToken + "timestamp=" + timestamp;
    String mac = text + "mac_key=" + mKey;

    HttpUrl.Builder newUrl = url.newBuilder()
        .addQueryParameter("timestamp", String.valueOf(timestamp))
        .addQueryParameter("mac", mac)
        .addQueryParameter("token", mToken);

    newRequest.url(newUrl.build());
  }
}

package com.zhangyn.androidtd;

import android.text.TextUtils;
import com.google.gson.Gson;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class RxNetErrorProcessor implements Consumer<Throwable>{

  private final Gson mGson;

  public RxNetErrorProcessor(Gson gson) {
    this.mGson = gson;
  }

  @Override public void accept(Throwable throwable) {
    try {

    }catch (Exception e){

    }
  }

  public boolean tryWithApiError(Throwable throwable,Consumer<ApiError> handler){
    if (throwable instanceof HttpException){
      HttpException exception = (HttpException) throwable;

      try {
        String errorBody = exception.response().errorBody().string();
        ApiError apiError = mGson.fromJson(errorBody,ApiError.class);

        if (!TextUtils.isEmpty(apiError.errorMsg)){
          handler.accept(apiError);
          return true;
        }

      } catch (Exception e) {
        accept(throwable);
      }
    }else {
      accept(throwable);
    }
    return false;
  }
}

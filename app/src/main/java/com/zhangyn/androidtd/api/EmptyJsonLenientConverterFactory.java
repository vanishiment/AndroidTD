package com.zhangyn.androidtd.api;

import java.io.EOFException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* 处理返回空 Json 的情况
* */

public class EmptyJsonLenientConverterFactory extends Converter.Factory {

  private final GsonConverterFactory mGsonConverterFactory;

  public EmptyJsonLenientConverterFactory(GsonConverterFactory gsonConverterFactory) {
    this.mGsonConverterFactory = gsonConverterFactory;
  }

  @Nullable @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return mGsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
  }

  @Nullable @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {

    final Converter<ResponseBody,?> delegateConverter = mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit);

    return null;
  }
}

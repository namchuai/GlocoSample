package com.vnindie.glocoapp.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceGenerator {
  private ServiceGenerator() {
  }

  public static ApiService createService() {
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(AppConstant.API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(httpClient.build())
      .build();
    return retrofit.create(ApiService.class);
  }
}

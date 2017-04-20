package com.vnindie.glocoapp.utils;

import com.vnindie.glocoapp.data.Doctor;
import com.vnindie.glocoapp.data.Region;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
  @GET("doctors")
  Call<List<Doctor>> getDoctors();

  @POST("region")
  Call<Region> setRegion(@Query("lat") float latitude,
                         @Query("lon") float longitutde,
                         @Query("radius") float radius);
}

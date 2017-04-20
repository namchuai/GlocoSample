package com.vnindie.glocoapp.utils;

import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
  @GET("doctors")
  Call<List<Doctor>> getDoctors();
}

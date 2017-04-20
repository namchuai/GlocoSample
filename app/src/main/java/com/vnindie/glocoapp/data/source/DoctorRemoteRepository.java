package com.vnindie.glocoapp.data.source;

import com.vnindie.glocoapp.data.Doctor;
import com.vnindie.glocoapp.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRemoteRepository implements DoctorDataSource {
  private static volatile DoctorRemoteRepository sInstance;

  private DoctorRemoteRepository() {
    // prevent outside initialization
  }

  public static DoctorRemoteRepository getsInstance() {
    if (sInstance == null) {
      synchronized (DoctorRemoteRepository.class) {
        if (sInstance == null) {
          sInstance = new DoctorRemoteRepository();
        }
      }
    }
    return sInstance;
  }

  @Override
  public void getDoctors(final OnGetDoctorsCallback callback) {
    Call<List<Doctor>> call = ServiceGenerator.createService().getDoctors();
    call.enqueue(new Callback<List<Doctor>>() {
      @Override
      public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
        if (response.isSuccessful()) {
          callback.onSuccess(response.body());
        } else {
          callback.onFailed(response.message());
        }
      }

      @Override
      public void onFailure(Call<List<Doctor>> call, Throwable t) {
        callback.onFailed(t.getMessage());
      }
    });
  }

  @Override
  public void saveDoctors(List<Doctor> doctors) {
    throw new UnsupportedOperationException("Can't save doctor at remote repo");
  }

  @Override
  public void clearAllDoctors() {
    throw new UnsupportedOperationException("Can't clear doctor at remote repo");
  }
}

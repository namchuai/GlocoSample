package com.vnindie.glocoapp.data.source;

import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

public interface DoctorDataSource {
  void getDoctors(OnGetDoctorsCallback callback);

  void saveDoctors(List<Doctor> doctors);

  interface OnGetDoctorsCallback {
    void onSuccess(List<Doctor> doctors);

    void onFailed(String message);
  }
}

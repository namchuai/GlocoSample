package com.vnindie.glocoapp.data.source;

import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

import io.realm.Realm;

public class DoctorLocalRepository implements DoctorDataSource {
  private static volatile DoctorLocalRepository sInstance;
  private Realm mRealm;

  private DoctorLocalRepository() {
    // prevent outside initialization
    mRealm = Realm.getDefaultInstance();
  }

  public static DoctorLocalRepository getsInstance() {
    if (sInstance == null) {
      synchronized (DoctorLocalRepository.class) {
        if (sInstance == null) {
          sInstance = new DoctorLocalRepository();
        }
      }
    }
    return sInstance;
  }

  @Override
  public void getDoctors(OnGetDoctorsCallback callback) {
    List<Doctor> doctors = mRealm.where(Doctor.class).findAll();
    if (doctors != null && doctors.size() > 0) {
      callback.onSuccess(doctors);
    } else {
      callback.onFailed("No doctors available");
    }
  }

  @Override
  public void saveDoctors(final List<Doctor> doctors) {
    mRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        for (Doctor doc : doctors) {
          mRealm.copyToRealm(doc);
        }
      }
    });
  }
}

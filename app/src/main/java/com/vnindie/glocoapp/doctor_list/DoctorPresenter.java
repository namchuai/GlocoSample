package com.vnindie.glocoapp.doctor_list;

import com.vnindie.glocoapp.data.Doctor;
import com.vnindie.glocoapp.data.source.DoctorDataSource;

import java.util.List;

class DoctorPresenter implements DoctorContract.Presenter {
  private final DoctorContract.View mView;
  private final DoctorDataSource mLocalDocRepo;
  private final DoctorDataSource mRemoteDocRepo;

  DoctorPresenter(DoctorContract.View mView,
                  DoctorDataSource mLocalDocRepo,
                  DoctorDataSource mRemoteDocRepo) {
    this.mView = mView;
    this.mLocalDocRepo = mLocalDocRepo;
    this.mRemoteDocRepo = mRemoteDocRepo;
  }

  @Override
  public void saveDoctors(List<Doctor> doctors) {
    mLocalDocRepo.saveDoctors(doctors);
  }

  @Override
  public void loadDoctorsRemotely() {
    mRemoteDocRepo.getDoctors(new DoctorDataSource.OnGetDoctorsCallback() {
      @Override
      public void onSuccess(List<Doctor> doctors) {
        if (doctors !=null && !doctors.isEmpty()) {
          mView.updateData(doctors);
        } else {
          mView.displayMessage("Data not available on remote server!");
        }
      }

      @Override
      public void onFailed(String message) {
        mView.displayMessage(message);
      }
    });
  }

  @Override
  public void loadDoctorsLocally() {
    mLocalDocRepo.getDoctors(new DoctorDataSource.OnGetDoctorsCallback() {
      @Override
      public void onSuccess(List<Doctor> doctors) {
        if (doctors != null && !doctors.isEmpty()) {
          mView.updateData(doctors);
        } else {
          mView.displayMessage("Data not available locally! Try to refresh!");
        }
      }

      @Override
      public void onFailed(String message) {
        mView.displayMessage(message);
      }
    });
  }

  @Override
  public void start() {
    mView.initView();
  }
}

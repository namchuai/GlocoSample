package com.vnindie.glocoapp.home;

import com.vnindie.glocoapp.data.Doctor;
import com.vnindie.glocoapp.data.source.DoctorDataSource;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
  private final HomeContract.View mView;
  private final DoctorDataSource mLocalDocRepo;
  private final DoctorDataSource mRemoteDocRepo;

  public HomePresenter(HomeContract.View mView,
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
  public void start() {
  }
}

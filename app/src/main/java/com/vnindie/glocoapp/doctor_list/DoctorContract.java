package com.vnindie.glocoapp.doctor_list;

import com.vnindie.glocoapp.base.BasePresenter;
import com.vnindie.glocoapp.base.BaseView;
import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

interface DoctorContract {
  interface View extends BaseView<Presenter> {
    void initView();

    void setLoadingIndicator(boolean active);

    void displayMessage(String message);

    void updateData(List<Doctor> doctors);
  }

  interface Presenter extends BasePresenter {
    void saveDoctors(List<Doctor> doctors);

    void loadDoctorsRemotely();

    void loadDoctorsLocally();
  }
}

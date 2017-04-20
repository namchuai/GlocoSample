package com.vnindie.glocoapp.home;

import com.vnindie.glocoapp.base.BasePresenter;
import com.vnindie.glocoapp.base.BaseView;

interface HomeContract {
  interface View extends BaseView<Presenter> {
    void initView();

    void displayMessage(String message);

    void navigateToDoctorAct();

    boolean isConnectThroughCellularData();

    boolean isConnectThroughWifi();

    void navigateToGeofenceAct();
  }

  interface Presenter extends BasePresenter {
    void onFirstRequirementClicked();

    void onSecondRequirementClicked();
  }
}

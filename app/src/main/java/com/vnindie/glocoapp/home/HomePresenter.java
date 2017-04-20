package com.vnindie.glocoapp.home;

public class HomePresenter implements HomeContract.Presenter {
  private final HomeContract.View mView;

  public HomePresenter(HomeContract.View mView) {
    this.mView = mView;
  }

  @Override
  public void start() {
    mView.initView();
  }

  @Override
  public void onFirstRequirementClicked() {
    if (!mView.isConnectThroughWifi()) {
      mView.displayMessage("Need using wifi!");
      return;
    }
    mView.navigateToGeofenceAct();
  }

  @Override
  public void onSecondRequirementClicked() {
    if (!mView.isConnectThroughCellularData()) {
      mView.displayMessage("Need using cellular data!");
      return;
    }

    mView.navigateToDoctorAct();
  }
}

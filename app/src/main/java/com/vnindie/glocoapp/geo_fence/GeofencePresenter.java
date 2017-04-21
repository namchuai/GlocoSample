package com.vnindie.glocoapp.geo_fence;

class GeofencePresenter implements GeofenceContract.Presenter {
  private final GeofenceContract.View mView;

  GeofencePresenter(GeofenceContract.View mView) {
    this.mView = mView;
  }

  @Override
  public void start() {
    mView.initView();
    mView.initConnectionToGoogleApi();
  }

  @Override
  public void onConnectionFailed(String errorMessage) {
    mView.displayMessage(errorMessage);
  }
}

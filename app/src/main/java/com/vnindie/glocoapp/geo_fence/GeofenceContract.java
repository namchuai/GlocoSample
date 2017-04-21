package com.vnindie.glocoapp.geo_fence;

import com.vnindie.glocoapp.base.BasePresenter;
import com.vnindie.glocoapp.base.BaseView;

public interface GeofenceContract {
  interface Presenter extends BasePresenter {

    void onConnectionFailed(String errorMessage);
  }

  interface View extends BaseView<Presenter> {
    void initView();

    void initConnectionToGoogleApi();

    void displayMessage(String message);
  }
}

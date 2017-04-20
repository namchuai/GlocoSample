package com.vnindie.glocoapp.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.base.BaseActivity;
import com.vnindie.glocoapp.databinding.HomeActBinding;
import com.vnindie.glocoapp.doctor_list.DoctorAct;
import com.vnindie.glocoapp.geo_fence.GeofenceAct;
import com.vnindie.glocoapp.utils.ConnectivityUtils;

public class HomeAct extends BaseActivity implements HomeContract.View {
  private HomeContract.Presenter mPresenter;
  private HomeActBinding mActBinding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setPresenter(new HomePresenter(this));
    mPresenter.start();
  }

  @Override
  protected void initBindingData() {
    mActBinding = DataBindingUtil.setContentView(this, R.layout.home_act);
  }

  @Override
  public void setPresenter(HomeContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void initView() {
    mActBinding.firstRequirementBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mPresenter.onFirstRequirementClicked();
      }
    });

    mActBinding.secondRequirementBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mPresenter.onSecondRequirementClicked();
      }
    });
  }

  @Override
  public void displayMessage(String message) {
    new AlertDialog.Builder(this)
      .setMessage(message)
      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      })
      .show();
  }

  @Override
  public boolean isConnectThroughCellularData() {
    return ConnectivityUtils.isConnectedMobile(this);
  }

  @Override
  public boolean isConnectThroughWifi() {
    return ConnectivityUtils.isConnectedWifi(this);
  }

  @Override
  public void navigateToDoctorAct() {
    startActivity(new Intent(this, DoctorAct.class));
  }

  @Override
  public void navigateToGeofenceAct() {
    startActivity(new Intent(this, GeofenceAct.class));
  }
}

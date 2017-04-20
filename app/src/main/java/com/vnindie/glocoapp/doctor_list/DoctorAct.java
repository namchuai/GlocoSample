package com.vnindie.glocoapp.doctor_list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;

import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.base.BaseActivity;
import com.vnindie.glocoapp.data.Doctor;
import com.vnindie.glocoapp.data.source.DoctorLocalRepository;
import com.vnindie.glocoapp.data.source.DoctorRemoteRepository;
import com.vnindie.glocoapp.databinding.DoctorActBinding;

import java.util.ArrayList;
import java.util.List;

public class DoctorAct extends BaseActivity implements DoctorContract.View {
  private DoctorContract.Presenter mPresenter;
  private DoctorActBinding mActBinding;
  private DoctorAdapter mAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setPresenter(new DoctorPresenter(this, DoctorLocalRepository.getsInstance(),
      DoctorRemoteRepository.getsInstance()));
    mPresenter.start();
  }

  @Override
  public void initView() {
    mAdapter = new DoctorAdapter(new ArrayList<Doctor>(0));
    mActBinding.doctorListView.setAdapter(mAdapter);
    mActBinding.doctorListView.setLayoutManager(new LinearLayoutManager(this));
    mActBinding.doctorListView.setHasFixedSize(true);
  }

  @Override
  public void displayMessage(String message) {
    new AlertDialog.Builder(this)
      .setMessage(message)
      .show();
  }

  @Override
  public void updateData(List<Doctor> doctors) {
    mAdapter.updateData(doctors);
  }

  @Override
  public void setPresenter(DoctorContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  protected void initBindingData() {
    mActBinding = DataBindingUtil.setContentView(this, R.layout.doctor_act);
  }
}

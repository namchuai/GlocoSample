package com.vnindie.glocoapp.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.base.BaseActivity;
import com.vnindie.glocoapp.base.BaseView;

public class HomeAct extends BaseActivity implements BaseView<HomePresenter> {
  private HomePresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void setPresenter(HomePresenter presenter) {
    this.mPresenter = presenter;
  }
}

package com.vnindie.glocoapp.home;

import com.vnindie.glocoapp.base.BasePresenter;
import com.vnindie.glocoapp.base.BaseView;
import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

/**
 * Created by Nguyen Hoang NAM on 4/20/2017.
 */

interface HomeContract {
  interface View extends BaseView<Presenter> {

  }

  interface Presenter extends BasePresenter {
    void saveDoctors(List<Doctor> doctors);
  }
}

package com.vnindie.glocoapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GlocoApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    configRealm();
  }

  private void configRealm() {
    RealmConfiguration config = new RealmConfiguration.Builder()
      .deleteRealmIfMigrationNeeded()
      .build();
    Realm.setDefaultConfiguration(config);
  }
}

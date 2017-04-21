package com.vnindie.glocoapp.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.vnindie.glocoapp.R;

public class GeofenceTransitionsIntentService extends IntentService {
  private static final String TAG = "GeofenceTransitions";
  private static final int GEOFENCE_NOTIFICATION_ID = 0;

  public GeofenceTransitionsIntentService(String name) {
    super(name);
  }

  public GeofenceTransitionsIntentService() {
    super("GeofenceTransitionsIntentService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

    if (geofencingEvent.hasError()) {
      return;
    }

    int geofenceTransition = geofencingEvent.getGeofenceTransition();

    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
      sendNotification();
    }
  }

  private void sendNotification() {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(getString(R.string.app_name))
      .setContentText("Entered!");

    NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notifyMgr.notify(GEOFENCE_NOTIFICATION_ID, mBuilder.build());
  }
}

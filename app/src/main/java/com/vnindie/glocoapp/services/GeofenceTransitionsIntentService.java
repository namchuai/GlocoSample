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

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

    if (geofencingEvent.hasError()) {
      String errorMsg = getErrorString(geofencingEvent.getErrorCode());
      Log.e(TAG, errorMsg);
      return;
    }

    int geofenceTransition = geofencingEvent.getGeofenceTransition();

    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
      sendNotification();
    }
  }

  private static String getErrorString(int errorCode) {
    switch (errorCode) {
      case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
        return "GeoFence not available";
      case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
        return "Too many GeoFences";
      case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
        return "Too many pending intents";
      default:
        return "Unknown error.";
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

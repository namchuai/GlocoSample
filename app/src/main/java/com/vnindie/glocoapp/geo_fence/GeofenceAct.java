package com.vnindie.glocoapp.geo_fence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.base.BaseActivity;
import com.vnindie.glocoapp.databinding.GeofenceActBinding;
import com.vnindie.glocoapp.services.GeofenceTransitionsIntentService;

public class GeofenceAct extends BaseActivity implements
  LocationListener,
  GeofenceContract.View,
  ResultCallback<Status>,
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "GeofenceAct";
  private GeofenceContract.Presenter mPresenter;
  private GeofenceActBinding mActBinding;

  private GoogleApiClient mGoogleApiClient;
  private PendingIntent mGeofencePendingIntent;
  private static final int GEOFENCE_REQ_CODE = 0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setPresenter(new GeofencePresenter(this));
    mPresenter.start();
  }

  @Override
  public void initView() {
  }

  @Override
  public void initData() {
  }

  @Override
  public void initConnectionToGoogleApi() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mGoogleApiClient.disconnect();
  }

  @Override
  protected void initBindingData() {
    mActBinding = DataBindingUtil.setContentView(this, R.layout.geofence_act);
  }

  public Geofence createGeofence(String geoFenceId, double lat, double lon, float radius) {
    return new Geofence.Builder()
      .setRequestId(geoFenceId)
      .setCircularRegion(lat, lon, 1000)
      .setExpirationDuration(5 * 60 * 1000) // five mins
      .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
      .build();
  }

  public GeofencingRequest createGeofenceRequest(Geofence geofence) {
    GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
    builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER); // to trigger GEOFENCE_TRANSITION_ENTER when the device is already inside the geofence
    builder.addGeofence(geofence);
    return builder.build();
  }

  public PendingIntent createGeofencePendingIntent() {
    Log.d(TAG, "createGeofencePendingIntent: ");
    if (mGeofencePendingIntent != null) {
      return mGeofencePendingIntent;
    }

    Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
    return PendingIntent.getService(this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private void addGeofence(GeofencingRequest request) {
    Log.d(TAG, "addGeofence");
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      Log.d(TAG, "addGeofence: dont have permission");
      return;
    }
    LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, request, createGeofencePendingIntent()).setResultCallback(this);
  }

  @Override
  public void setPresenter(GeofenceContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    mPresenter.onConnectionFailed(connectionResult.getErrorMessage());
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
  public void onConnected(@Nullable Bundle bundle) {
    Log.d(TAG, "onConnected: ");

    Geofence geofence = createGeofence("My geofence", 21.040151, 105.811416, 500.0f);
    GeofencingRequest request = createGeofenceRequest(geofence);
    addGeofence(request);

    getLastKnownLocation();
  }

  private Location lastLocation;

  public void getLastKnownLocation() {
    Log.d(TAG, "getLastKnownLocation()");
    if (checkPermission()) {
      lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      if (lastLocation != null) {
        Log.i(TAG, "LasKnown location. " + "Long: " + lastLocation.getLongitude() + " | Lat: " + lastLocation.getLatitude());
        writeLastLocation();
        startLocationUpdates();
      } else {
        Log.w(TAG, "No location retrieved yet");
        startLocationUpdates();
      }
    } else askPermission();
  }

  private LocationRequest locationRequest;
  // Defined in mili seconds.
  // This number in extremely low, and should be used only for debug
  private final int UPDATE_INTERVAL = 1000;
  private final int FASTEST_INTERVAL = 900;

  // Start location Updates
  private void startLocationUpdates() {
    Log.i(TAG, "startLocationUpdates()");
    locationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setInterval(UPDATE_INTERVAL)
      .setFastestInterval(FASTEST_INTERVAL);

//    if ( checkPermission() )
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d(TAG, "onConnectionSuspended: ");
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG, "onLocationChanged ["+location+"]");
    lastLocation = location;
    writeActualLocation(location);
  }

  private boolean checkPermission() {
    Log.d(TAG, "checkPermission()");
    // Ask for permission if it wasn't granted yet
    return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED );
  }

  // Asks for permission
  private void askPermission() {
    Log.d(TAG, "askPermission()");
    ActivityCompat.requestPermissions(
      this,
      new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
      REQ_PERMISSION
    );
  }

  private final int REQ_PERMISSION = 100;

  // Write location coordinates on UI
  private void writeActualLocation(Location location) {
//    textLat.setText( "Lat: " + location.getLatitude() );
//    textLong.setText( "Long: " + location.getLongitude() );
  }

  private void writeLastLocation() {
    writeActualLocation(lastLocation);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    Log.d(TAG, "onRequestPermissionsResult()");
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch ( requestCode ) {
      case REQ_PERMISSION: {
        if ( grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
          // Permission granted
          getLastKnownLocation();

        } else {
          // Permission denied
          permissionsDenied();
        }
        break;
      }
    }
  }

  // App cannot work without the permissions
  private void permissionsDenied() {
    Log.w(TAG, "permissionsDenied()");
  }

  @Override
  public void onResult(@NonNull Status status) {
    Log.d(TAG, "onResult: " + status.isSuccess());
  }
}

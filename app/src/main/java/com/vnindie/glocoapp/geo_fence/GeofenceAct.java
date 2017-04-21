package com.vnindie.glocoapp.geo_fence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.base.BaseActivity;
import com.vnindie.glocoapp.databinding.GeofenceActBinding;
import com.vnindie.glocoapp.services.GeofenceTransitionsIntentService;

public class GeofenceAct extends BaseActivity implements
  OnMapReadyCallback,
  LocationListener,
  GeofenceContract.View,
  ResultCallback<Status>,
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
  GoogleMap.OnMarkerClickListener,
  GoogleMap.OnMapClickListener {

  private static final String TAG = "GeofenceAct";
  private GeofenceContract.Presenter mPresenter;
  private GeofenceActBinding mActBinding;

  private GoogleApiClient mGoogleApiClient;
  private Location mLastLocation;
  private final int REQ_PERMISSION = 100;
  private LocationRequest mLocationRequest;
  private final int UPDATE_INTERVAL = 1000;
  private final int FASTEST_INTERVAL = 900;
  private PendingIntent mGeofencePendingIntent;
  private final int GEOFENCE_REQ_CODE = 0;
  private Circle mGeoFenceLimits;

  private MapFragment mMapFragment;
  private GoogleMap mMap;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setPresenter(new GeofencePresenter(this));
    mPresenter.start();
  }

  @Override
  public void initView() {
    mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    mMapFragment.getMapAsync(this);

    mActBinding.start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startGeofence();
      }
    });
  }

  @Override
  public void initConnectionToGoogleApi() {
    Log.d(TAG, "initConnectionToGoogleApi: ");
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    }
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

  private GeofencingRequest createGeofenceRequest(Geofence geofence) {
    Log.d(TAG, "createGeofenceRequest");
    return new GeofencingRequest.Builder()
      .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
      .addGeofence(geofence)
      .build();
  }

  private PendingIntent createGeofencePendingIntent() {
    Log.d(TAG, "createGeofencePendingIntent");
    if (mGeofencePendingIntent != null)
      return mGeofencePendingIntent;

    Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
    return PendingIntent.getService(
      this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  // Add the created GeofenceRequest to the device's monitoring list
  private void addGeofence(GeofencingRequest request) {
    Log.d(TAG, "addGeofence");
    if (checkPermission())
      LocationServices.GeofencingApi.addGeofences(
        mGoogleApiClient,
        request,
        createGeofencePendingIntent()
      ).setResultCallback(this);
  }

  @Override
  public void setPresenter(GeofenceContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG, "onConnectionFailed: ");
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

    getLastKnownLocation();
  }

  private void getLastKnownLocation() {
    Log.d(TAG, "getLastKnownLocation()");
    if (checkPermission()) {
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      if (mLastLocation != null) {
        Log.i(TAG, "LasKnown location. " +
          "Long: " + mLastLocation.getLongitude() +
          " | Lat: " + mLastLocation.getLatitude());
        writeLastLocation();
        startLocationUpdates();
      } else {
        Log.w(TAG, "No location retrieved yet");
        startLocationUpdates();
      }
    } else askPermission();
  }


  // Start location Updates
  private void startLocationUpdates() {
    Log.i(TAG, "startLocationUpdates()");
    mLocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setInterval(UPDATE_INTERVAL)
      .setFastestInterval(FASTEST_INTERVAL);

    if (checkPermission())
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d(TAG, "onConnectionSuspended: ");
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG, "onLocationChanged [" + location + "]");
    mLastLocation = location;
    writeActualLocation(location);
  }

  private static final long GEO_DURATION = 60 * 60 * 1000;
  private static final String GEOFENCE_REQ_ID = "My Geofence";
  private static final float GEOFENCE_RADIUS = 500.0f; // in meters

  private Geofence createGeofence(LatLng latLng, float radius) {
    Log.d(TAG, "createGeofence");
    return new Geofence.Builder()
      .setRequestId(GEOFENCE_REQ_ID)
      .setCircularRegion(latLng.latitude, latLng.longitude, radius)
      .setExpirationDuration(GEO_DURATION)
      .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
        | Geofence.GEOFENCE_TRANSITION_EXIT)
      .build();
  }

  private void writeActualLocation(Location location) {
    markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    Log.d(TAG, "lat, long: " + location.getLatitude() + ", " + location.getLongitude());
  }

  private void writeLastLocation() {
    writeActualLocation(mLastLocation);
  }

  private Marker locationMarker;

  // Create a Location Marker
  private void markerLocation(LatLng latLng) {
    Log.i(TAG, "markerLocation(" + latLng + ")");
    String title = latLng.latitude + ", " + latLng.longitude;
    MarkerOptions markerOptions = new MarkerOptions()
      .position(latLng)
      .title(title);
    if (mMap != null) {
      // Remove the anterior marker
      if (locationMarker != null)
        locationMarker.remove();
      locationMarker = mMap.addMarker(markerOptions);
      float zoom = 14f;
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
      mMap.animateCamera(cameraUpdate);
    }
  }

  private Marker geoFenceMarker;

  // Create a marker for the geofence creation
  private void markerForGeofence(LatLng latLng) {
    Log.i(TAG, "markerForGeofence(" + latLng + ")");
    String title = latLng.latitude + ", " + latLng.longitude;
    // Define marker options
    MarkerOptions markerOptions = new MarkerOptions()
      .position(latLng)
      .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
      .title(title);
    if (mMap != null) {
      // Remove last geoFenceMarker
      if (geoFenceMarker != null)
        geoFenceMarker.remove();

      geoFenceMarker = mMap.addMarker(markerOptions);
    }
  }

  // Check for permission to access Location
  private boolean checkPermission() {
    Log.d(TAG, "checkPermission()");
    // Ask for permission if it wasn't granted yet
    return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED);
  }

  // Asks for permission
  private void askPermission() {
    Log.d(TAG, "askPermission()");
    ActivityCompat.requestPermissions(
      this,
      new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
      REQ_PERMISSION
    );
  }

  // Verify user's response of the permission requested
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    Log.d(TAG, "onRequestPermissionsResult()");
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQ_PERMISSION: {
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
//    displayMessage("Geofence created successfully");
    Log.i(TAG, "onResult: " + status);
    if (status.isSuccess()) {
      drawGeofence();
    } else {
      // inform about fail
    }
  }

  // Start Geofence creation process
  private void startGeofence() {
    Log.i(TAG, "startGeofence()");
    if (geoFenceMarker != null) {
      Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
      GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
      addGeofence(geofenceRequest);
    } else {
      Log.e(TAG, "Geofence marker is null");
    }
  }

  private void drawGeofence() {
    Log.d(TAG, "drawGeofence()");

    if (mGeoFenceLimits != null)
      mGeoFenceLimits.remove();

    CircleOptions circleOptions = new CircleOptions()
      .center(geoFenceMarker.getPosition())
      .strokeColor(Color.argb(50, 70, 70, 70))
      .fillColor(Color.argb(100, 150, 150, 150))
      .radius(GEOFENCE_RADIUS);
    mGeoFenceLimits = mMap.addCircle(circleOptions);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnMapClickListener(this);
    mMap.setOnMarkerClickListener(this);
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    return false;
  }

  @Override
  public void onMapClick(LatLng latLng) {
    Log.d(TAG, "onMapClick(" + latLng + ")");
    markerForGeofence(latLng);
  }
}

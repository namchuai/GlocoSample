package com.vnindie.glocoapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectivityUtils {

  private ConnectivityUtils() {
  }

  /**
   * Get the network info
   * @param context context to access android's function
   * @return NetworkInfo object which contains connection information
   */
  public static NetworkInfo getNetworkInfo(Context context){
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }

  /**
   * Check if there is any connectivity to a Wifi network
   * @param context context to access android's function
   * @return boolean if the phone is connected through wifi
   */
  public static boolean isConnectedWifi(Context context){
    NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
  }

  /**
   * Check if there is any connectivity to a mobile network
   * @param context context to access android's function
   * @return boolean if the phone is connected through cellular data
   */
  public static boolean isConnectedMobile(Context context){
    NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
  }
}
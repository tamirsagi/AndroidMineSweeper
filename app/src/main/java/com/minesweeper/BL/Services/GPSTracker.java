package com.minesweeper.BL.Services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class GPSTracker extends Service implements LocationListener {

    public static final String TAG = "GPSTracker";
    public static final String INTENT_FILTER_NAME = "GPSTracker SERVICE";
    public static final String BUNDLE_ACTION = "ACTION";
    public static final String BUNDLE_DATA = "BUNDLE DATA";

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    private final IBinder tracker = new MyLocalBinder();

    private LocationManager locationManager;
    private Location location;

    private boolean isGPSENabled = false;
    private boolean canGetLocation = false;
    private boolean isNetworkEnabled = false;


    @Override
    public IBinder onBind(Intent intent) {
        setLocationManager();
        return tracker;
    }

    private void setLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSENabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        canGetLocation = isGPSENabled && isNetworkEnabled;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onProviderEnabled(String provider) {
        if (locationManager != null) {
            if (provider.equals(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(provider,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

            else if (provider.equals(LocationManager.GPS_PROVIDER))
                if (location == null) {
                    locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                }
        }
    }


    public void stopUsingGPS() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. do you want to go to setting menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public class MyLocalBinder extends Binder {

        public GPSTracker gerService() {
            return GPSTracker.this;
        }

    }

    /**
     * update game activity to change the board
     *
     * @param action - the action to take place where service is bonded
     */
    private void sendMessageToActivity(String action, String data) {
        Intent intent = new Intent(INTENT_FILTER_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ACTION, action);
        bundle.putString(BUNDLE_DATA, data);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

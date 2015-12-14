package com.minesweeper.BL.Services;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import com.minesweeper.UI.Activities.GameActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tamir Sagi
 *         This class manges GPS service. it is binned from Game activity and keep the location updated.
 *         we call its methods when needed, it's kept alive until we unbinned it on Game Activity (on Stop method)
 */
public class GPSTracker extends Service implements LocationListener {

    public static final String TAG = "GPSTracker";
    public static final String INTENT_FILTER_NAME = "GPS_SERVICE NOTIFIER";

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 0;
    public static final long MIN_TIME_BW_UPDATES = 0;

    private final IBinder tracker = new MyLocalBinder();

    private LocationManager mLocationManager;
    private Location mLastLocation;
    private Geocoder mGeoCoder;

    private boolean isGPSEnabled = false;
    private boolean canGetLocation = false;


    @Override
    public IBinder onBind(Intent intent) {
        setLocationManager();
        return tracker;
    }

    @SuppressWarnings("ResourceType")
    private void setLocationManager() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        canGetLocation = isGPSEnabled;
        if(isGPSEnabled()){
            Toast.makeText(this, "GPS is Enabled ", Toast.LENGTH_LONG).show();
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
            mGeoCoder = new Geocoder(this);
        }
        else{
            Toast.makeText(this, "Please turn GPS on ", Toast.LENGTH_LONG).show();
            //notifyUser(GeneralServiceParams.ACTIONS.Go_TO_SETTING_WINDOW.toString(),"");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLastLocation = location;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        switch (status){
//            case LocationProvider.AVAILABLE:
//                Toast.makeText(this, "GPS is Enabled ", Toast.LENGTH_LONG).show();
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
//                        MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
//                mGeoCoder = new Geocoder(this);
//                break;
//            default:
//                Toast.makeText(this, "Please turn GPS on ", Toast.LENGTH_LONG).show();
//                // notifyUser(GeneralServiceParams.ACTIONS.Go_TO_SETTING_WINDOW.toString(),"");
//                break;
//        }

    }


    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    /**
     * disable GPS
     */
    public void stopUsingGPS() {
        try {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(GPSTracker.this);
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }


    public Location getLastLocation() {
        return mLastLocation;
    }


    /**
     * get location (latitude, longitude, City and country)
     *
     * @return hash map contains these values
     */
    public HashMap<String, String> getLocationValues() {
        double lat = mLastLocation.getLatitude();
        double log = mLastLocation.getLongitude();
        HashMap<String, String> location = new HashMap<String, String>();
        try {
            List<Address> addresses = mGeoCoder.getFromLocation(lat, log, 1);
            if (addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                location.put(GameActivity.KEY_LOCATION_CITY, city);
                location.put(GameActivity.KEY_LOCATION_COUNTRY, country);
                location.put(GameActivity.KEY_LOCATION_LATITUDE, "" + lat);
                location.put(GameActivity.KEY_LOCATION_LONGITUDE, "" + log);
                return location;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return location;
    }


    public class MyLocalBinder extends Binder {

        public GPSTracker gerService() {
            return GPSTracker.this;
        }

    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    };






    /**
     * notify user to enable GPS
     * @param action
     * @param data
     */
    private void notifyUser(String action, String data) {
        Intent intent = new Intent(INTENT_FILTER_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(GeneralServiceParams.BUNDLE_ACTION, action);
        bundle.putString(GeneralServiceParams.BUNDLE_DATA, data);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

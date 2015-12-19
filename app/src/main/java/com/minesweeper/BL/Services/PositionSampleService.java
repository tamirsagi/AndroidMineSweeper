package com.minesweeper.BL.Services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import java.text.DecimalFormat;


/**
 * @author Tamir Sagi
 *         This class manages accelerator sensor service. it's binned when timer starts(On Game Activity)
 *         It keeps updating the phone angle and sends broadcast messages back to Game activity.
 */
public class PositionSampleService extends Service implements SensorEventListener {

    public static final String TAG = "PositionSampleService";

    public static final String INTENT_FILTER_NAME = "POSITION_SERVICE NOTIFIER";
    public static final String BUNDLE_DATA_ADD_MINES = "phone's angle was deviated for long time, mines added to board";
    public static final int Number_Of_AXIS = 3;
    private static final int MILI = 1000;
    private static final int SECONDS_MINUTE = 60;
    private static final int RADIANS_PI = 180;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private final int minAngleDeviation = 10;
    private final int minTimeToAngleDeviation = 10; // after 10 seconds
    private double initialAngle;
    private boolean timerStarted = false;
    private boolean addMines = false;
    private long startTime;
    private long endTime;

    //Bind the Client which is the game Activity to the service
    //We use an Object for that
    private final IBinder positioningKeeper = new MyLocalBinder();

    private Sensor accelerometer;
    private SensorManager sensorManager;
    private boolean getUpdatedInitialAngle;
    private boolean work = false;                        //determines whether we should send the data or not

    public PositionSampleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        setAccelerationSensor();
        return positioningKeeper;
    }

    private void setAccelerationSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setGetUpdatedInitialAngle(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String position, action, angle;
        position = "X:" + decimalFormat.format(event.values[0]) + "\nY:" + decimalFormat.format(event.values[1]) +
                "\nZ:" + decimalFormat.format(event.values[2]);
        double radians = (Math.atan2(event.values[0], event.values[1])); //radians
        double vAngle = radians * (RADIANS_PI / Math.PI);                      //radians to degrees
        angle = "angle : " + decimalFormat.format(vAngle);
        if (getUpdatedInitialAngle) {
            initialAngle = vAngle;
            position = "Initial\n" + position + "\n" + angle;
            action = GeneralServiceParams.ACTIONS.UPDATE_INITIAL_POSITION.toString();
            GeneralServiceParams.sendMessageToActivity(this, INTENT_FILTER_NAME, action,position);
        }

        //work = true only when game timer starts
        if (work) {
            //update current phone angle
            position = "Current\n" + position + "\n" + angle;
            action = GeneralServiceParams.ACTIONS.UPDATE_CURRENT_POSITION.toString();
            //update UI with position
            GeneralServiceParams.sendMessageToActivity(this, INTENT_FILTER_NAME, action, position);
            //Check duration for deviation
            if (Math.abs(vAngle - initialAngle) >= minAngleDeviation) {
                if (!timerStarted) {
                    timerStarted = true;
                    startTime = System.currentTimeMillis() / MILI;
                }
                endTime = System.currentTimeMillis() / MILI;
                if (!addMines && endTime - startTime >= minTimeToAngleDeviation) {
                    addMines = true;
                }

                if (addMines) {
                    action = GeneralServiceParams.ACTIONS.ADD_MINES_TO_GAME_BOARD.toString();
                    GeneralServiceParams.sendMessageToActivity(this, INTENT_FILTER_NAME, action, "");
                }
            } else {
                timerStarted = false;
                startTime = 0;
                endTime = 0;
                addMines = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //stop service from updating with position and restart values
    public void stopUpdatingCurrentAngle() {
        work = false;

    }

    //let the service work
    public void startUpdatingCurrentAngle() {
        setGetUpdatedInitialAngle(false);
        work = true;

    }

    public void setGetUpdatedInitialAngle(boolean update){
        getUpdatedInitialAngle = update;
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    public void registerListener() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    //we need to extends Binder in order to make our object as a binder
    public class MyLocalBinder extends Binder {

        public PositionSampleService gerService() {
            return PositionSampleService.this;
        }
    }

}



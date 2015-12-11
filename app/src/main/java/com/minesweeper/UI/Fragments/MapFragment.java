package com.minesweeper.UI.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.BL.DB.PlayerRecord;
import com.minesweeper.UI.Activities.DBRecordsFragmentActivity;
import com.minesweeper.UI.Activities.R;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends SupportMapFragment {

    public static final String PAGE_NUMBER = "PAGE NUMBER";

    private MapView mapView;
    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;
    private List<Marker> markers;


    public static MapFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, page);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) getView().findViewById(R.id.RecordsMapView);
        mGoogleMap = mapView.getMap();
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);

        createRecordsMarkers();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        return root;
    }


    /**
     * function get the chosen table and fill up the map with relevant markers
     */
    private void createRecordsMarkers(){
        markers = new ArrayList<Marker>();
        List<PlayerRecord> records = DbManager.dbManager.getRecords(DBRecordsFragmentActivity.mDefaultTable);
        for(PlayerRecord record : records){
    Marker recordMArker = 






        }



    }

    class MapOverLayMarker extends Overlay {

        private PlayerRecord mOriginalLocation;
        private LatLng mOriginalLocationInPxl;

        public MapOverLayMarker(PlayerRecord record) {
            this.mOriginalLocation = record;
        }

        @Override
        public void draw(Canvas canvas, com.google.android.maps.MapView mapView, boolean b) {
            super.draw(canvas, mapView, b);
            if (mOriginalLocation != null) {
                LatLng mOriginalLocationInPxl = new LatLng(mOriginalLocation.getLatitude(), mOriginalLocation.getLongitude());
                Point p  = mGoogleMap.getProjection().toScreenLocation(mOriginalLocationInPxl);
                Bitmap marker = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
                canvas.drawBitmap(marker, p.x, p.y, null);
            }
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, com.google.android.maps.MapView mapView) {

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOriginalLocationInPxl,15));
            // Zoom in, animating the camera.
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent, com.google.android.maps.MapView mapView) {
            return super.onTouchEvent(motionEvent, mapView);
        }
    }

}

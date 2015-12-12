package com.minesweeper.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.BL.DB.PlayerRecord;
import com.minesweeper.UI.Activities.DBRecordsFragmentActivity;
import com.minesweeper.UI.Activities.R;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String PAGE_NUMBER = "PAGE NUMBER";

    private MapView mapView;
    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;
    private HashMap<Marker, PlayerRecord> markers;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) root.findViewById(R.id.RecordsMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }


    /**
     * Method will get the map in Async manner
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);                     //enable location
        mUiSettings = mGoogleMap.getUiSettings();                 //get map UI controller
        mUiSettings.setMyLocationButtonEnabled(true);             //enable location button
        mUiSettings.setCompassEnabled(true);                     //enable compass
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        createRecordsMarkers();
        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(markers));
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                // Zoom in, animating the camera.
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                marker.showInfoWindow();
                return true;
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * function get the chosen table and fill up the map with relevant markers
     */
    private void createRecordsMarkers() {
        markers = new HashMap<Marker, PlayerRecord>();
        List<PlayerRecord> records = DbManager.dbManager.getRecords(DBRecordsFragmentActivity.mDefaultTable);
        for (PlayerRecord record : records) {
            LatLng position = new LatLng(record.getLatitude(), record.getLongitude());
            Marker recordMarker = mGoogleMap.addMarker(
                    new MarkerOptions().position(position)
                            .visible(true)
                            .draggable(false)
                            .snippet(record.getFullName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_40_75)));
            markers.put(recordMarker, record);
        }
    }


    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private HashMap<Marker, PlayerRecord> mData;

        public CustomInfoWindowAdapter(HashMap<Marker, PlayerRecord> data) {
            mData = data;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View root = getView().inflate(getContext(), R.layout.info_window_layout, null);
            PlayerRecord recordInMarker = mData.get(marker);
            TextView name = (TextView) root.findViewById(R.id.map_info_window_player_name);
            TextView recordTime = (TextView) root.findViewById(R.id.map_info_window_record);
            TextView location = (TextView) root.findViewById(R.id.map_info_window_location);
            name.setText(recordInMarker.getFullName());
            recordTime.setText(recordInMarker.getRoundTime());
            location.setText(recordInMarker.getCity() + "," + recordInMarker.getCountry());
            return root;
        }

        public void updateMarkers(HashMap<Marker, PlayerRecord> mData){
            this.mData = mData;
        }
    }


}

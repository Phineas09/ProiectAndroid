package ro.mta.proiect.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;

import ro.mta.proiect.MainActivity;
import ro.mta.proiect.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class GoogleMaps extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    boolean isPermissionGranted;
    GoogleMap myGoogleMap;
    FloatingActionButton floatingActionButton;
    private FusedLocationProviderClient clientLocationProvider;

    public GoogleMaps() {

        isPermissionGranted = MainActivity.isIsPermissionGranted();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);

        Toolbar toolbar = getActivity().getWindow().findViewById(R.id.toolbar);
        toolbar.setTitle("User current location");

        floatingActionButton = view.findViewById(R.id.fragment_google_maps_floating_action_button);

        if(isPermissionGranted) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.iframe_google_maps);
            supportMapFragment.getMapAsync(this);
        }

        clientLocationProvider = new FusedLocationProviderClient(getActivity());

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        clientLocationProvider.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Location location = task.getResult();
                if(location != null)
                    goToLocation(location.getLatitude(), location.getLongitude());
            }
        });

    }

    private void goToLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        myGoogleMap.moveCamera(cameraUpdate);
        myGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //Set title of marker
        markerOptions.title("You are here!");
        //Remove other markers
        myGoogleMap.clear();
        //Animate zoom marker
        myGoogleMap.addMarker(markerOptions);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //when map is ready
        myGoogleMap = googleMap;
        //myGoogleMap.setMyLocationEnabled(true);

        /**
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //When you click on the map
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //Set title of marker
                markerOptions.title("test title");
                //Remove other markers
                googleMap.clear();
                //Animate zoom marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                googleMap.addMarker(markerOptions);

            }
        });
         */

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        goToLocation(location.getLatitude(), location.getLongitude());
    }
}
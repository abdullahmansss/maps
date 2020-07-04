package softagi.maps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();
        createLocationRequest();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations())
                {
                    // Update UI with location data
                    // ...
                    mMap.clear();
                    LatLng home = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(home)
                                    .title("Marker in Home"))
                            .setDraggable(true);
                    mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(home, 21.0f)
                    );
                }
            }
        };
    }

    private void initViews()
    {
        Button get = findViewById(R.id.get_my_location_maps);

        get.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //getLocation();
                startLocationUpdates();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDragStart(Marker marker)
            {

            }

            @Override
            public void onMarkerDrag(Marker marker)
            {

            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                LatLng po = marker.getPosition();
                Toast.makeText(getApplicationContext(), String.valueOf(po.latitude), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
            getLocation();
        } else
        {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location)
                        {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null)
                            {
                                // Logic to handle location object
                                LatLng home = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(home)
                                                .title("Marker in Home"))
                                        .setDraggable(true);
             /*                   mMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(home, 17.0f)
                                );*/
                                mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(home, 17.0f)
                                );
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //startLocationUpdates();
    }

    private void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
            getLocation();
        } else
        {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
/*            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location)
                        {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null)
                            {
                                // Logic to handle location object
                                LatLng home = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(home)
                                                .title("Marker in Home"))
                                        .setDraggable(true);
             *//*                   mMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(home, 17.0f)
                                );*//*
                                mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(home, 17.0f)
                                );
                            }
                        }
                    });*/
        }
    }

    protected void createLocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
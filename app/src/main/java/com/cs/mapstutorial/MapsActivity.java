package com.cs.mapstutorial;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cs.mapstutorial.directionhelpers.FetchURL;
import com.cs.mapstutorial.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    Button button;
    double mCurrentLat, mCurrentLng, mDestinationLat, mDestinationLng;
    LatLng home, swe;
    private GoogleMap mMap;
    private Marker mCurrent, mDestination;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        button = findViewById(R.id.btnDriver);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        home = new LatLng(27.196708, 77.943734);
        swe = new LatLng(27.215424, 77.950458);

        mCurrent = mMap.addMarker(new MarkerOptions().position(home).draggable(true).title("Current"));
        mDestination = mMap.addMarker(new MarkerOptions().position(swe).draggable(true).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 10f));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Log.d("System out", "onMarkerDragEnd...");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                Log.d("aa", arg0.getTitle());
                Log.d("bb", arg0.getPosition().toString());

                if (arg0.getTitle().equals("Current")) {
                    mCurrentLat = arg0.getPosition().latitude;
                    mCurrentLng = arg0.getPosition().longitude;
                }
                if (arg0.getTitle().equals("Destination"))
                {
                    mDestinationLat = arg0.getPosition().latitude;
                    mDestinationLng = arg0.getPosition().longitude;
                }
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MapsActivity.this, Double.toString(mCurrentLat), Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, Double.toString(mCurrentLng), Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, Double.toString(mDestinationLat), Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, Double.toString(mDestinationLng), Toast.LENGTH_SHORT).show();
                LatLng aa = new LatLng(mCurrentLat, mCurrentLng);
                LatLng bb = new LatLng(mDestinationLat, mDestinationLng);
                new FetchURL(MapsActivity.this).execute(getUrl(aa, bb, "driving"), "driving");
                LocationHelper helper = new LocationHelper(mCurrentLat, mCurrentLng, mDestinationLat, mDestinationLng);
                FirebaseDatabase.getInstance().getReference("Locations").setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MapsActivity.this, "Stored", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Not Stored", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
package com.cs.mapstutorial;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    Button button;
    double d1, d2, d3, d4;
    LatLng home, swe;
    private GoogleMap mMap;
    Marker mCurrent, mDestination;
    private Polyline currentPolyline;
    AlertDialog.Builder builder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        button = findViewById(R.id.btnRider);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRider);
        mapFragment.getMapAsync(this);
        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng def = new LatLng(22.1059,77.7832);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(def, 1f));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              new FetchURL(MapsActivity.this).execute(getUrl(home, swe, "driving"), "driving");

                FirebaseDatabase.getInstance().getReference().child("Locations").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            d1 = (double) dataSnapshot.child("currentLat").getValue();
                            d2 = (double) dataSnapshot.child("currentLng").getValue();
                            d3 = (double) dataSnapshot.child("destinationLat").getValue();
                            d4 = (double) dataSnapshot.child("destinationLng").getValue();

                            Log.d("t1", String.valueOf(d1));
                            Log.d("t1", String.valueOf(d2));
                            Log.d("t1", String.valueOf(d3));
                            Log.d("t1", String.valueOf(d4));

                            home = new LatLng(d1, d2);
                            swe = new LatLng(d3, d4);

                            mCurrent = mMap.addMarker(new MarkerOptions().position(home).title("Current"));

                            mDestination = mMap.addMarker(new MarkerOptions()
                                    .position(swe)
                                    .title("Destination")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 10f));
                        } catch (Exception e) {
                            Log.d("exception", String.valueOf(e.getMessage()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                if (marker.getTitle().equals("Current")) {

                    builder.setTitle("Confirm");
                    builder.setMessage("Confirm Ride");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int aa) {
                            Toast.makeText(getBaseContext(), "Correct", Toast.LENGTH_SHORT).show();

                            Geocoder geocoder;
                            List<Address> addressesCurrent = null, addressesDestination = null;
                            geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

                            try {

                                addressesCurrent = geocoder.getFromLocation(mCurrent.getPosition().latitude, mCurrent.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                addressesDestination = geocoder.getFromLocation(mDestination.getPosition().latitude, mDestination.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String addressC = addressesCurrent.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String cityC = addressesCurrent.get(0).getLocality();
                            String stateC = addressesCurrent.get(0).getAdminArea();
                            String countryC = addressesCurrent.get(0).getCountryName();
                            String postalCodeC = addressesCurrent.get(0).getPostalCode();

                            String addressD = addressesDestination.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String cityD = addressesDestination.get(0).getLocality();
                            String stateD = addressesDestination.get(0).getAdminArea();
                            String countryD = addressesDestination.get(0).getCountryName();
                            String postalCodeD = addressesDestination.get(0).getPostalCode();

                            Intent intent = new Intent(getBaseContext(), Confirmed.class);

                            intent.putExtra("addressC",addressC);
                            intent.putExtra("cityC",cityC);
                            intent.putExtra("stateC",stateC);
                            intent.putExtra("countryC",countryC);
                            intent.putExtra("postalCodeC",postalCodeC);
                            intent.putExtra("addressD",addressD);
                            intent.putExtra("cityD",cityD);
                            intent.putExtra("stateD",stateD);
                            intent.putExtra("countryD",countryD);
                            intent.putExtra("postalCodeD",postalCodeD);

                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int aa) {
                            Toast.makeText(getBaseContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
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
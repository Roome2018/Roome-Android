package roome.hackathon.com.roome.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.Models.Item;
import roome.hackathon.com.roome.Models.Medium;
import roome.hackathon.com.roome.R;
import roome.hackathon.com.roome.Utils.SharedPrefUtil;
import roome.hackathon.com.roome.Utils.Util;
import roome.hackathon.com.roome.Utils.VolleyRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static roome.hackathon.com.roome.Interface.AppConstants.MY_LOCATION_REQUEST_CODE;
import static roome.hackathon.com.roome.Interface.AppConstants.REQUEST_LOCATION;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AppCompatImageView ic_logout = findViewById(R.id.ic_logout);
        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView fab_add = findViewById(R.id.img_add);

        getRoomes(MainActivity.this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(AppConstants.TAG, "onClick");
                enabeldMyLocation();
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ic_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.Logout(MainActivity.this);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {
                Log.e(AppConstants.TAG, "Error in LocationServices");
            }
        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);
            Log.e(AppConstants.TAG, "builder");
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {

                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.e(AppConstants.TAG, "builder 1");
                            try {
                                status.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(AppConstants.TAG, "builder catch");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                Util.checkLocationPermission(MainActivity.this);
            }
        } else {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                Util.checkLocationPermission(MainActivity.this);
                mMap.setMyLocationEnabled(true);
            }
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.info_item, null);
                Item item = (Item) marker.getTag();
                final TextView title = (TextView) v.findViewById(R.id.tv_title);
                TextView subTitle = (TextView) v.findViewById(R.id.tv_subTitle);
                TextView numAvailable = (TextView) v.findViewById(R.id.tv_numAvailable);
                title.setText(item.getTitle());
                subTitle.setText(item.getInfo());
                numAvailable.setText(item.getRemain() + "");
                return v;
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        Item item = (Item) marker.getTag();
        intent.putExtra(AppConstants.ITEM_PARC, item);
        startActivity(intent);
        finish();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnecting()) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (Exception e) {
                Log.i(AppConstants.TAG, "error");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                Log.e(AppConstants.TAG, "builder REQUEST_LOCATION");
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Log.e(AppConstants.TAG, "builder RESULT_OK");
                        // All required changes were successfully made
                        Toast.makeText(MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        Log.e(AppConstants.TAG, "builder RESULT_CANCELED");
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(MainActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(AppConstants.TAG, "length > 0");
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.i(AppConstants.TAG, "PERMISSION_GRANTED");

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        enabeldMyLocation();
                        Util.myLocation(mMap);
                        Util.getGPSLocation(MainActivity.this);
                    } else {
                        Util.getGPSLocation(MainActivity.this);
                        mMap.setMyLocationEnabled(true);
                        enabeldMyLocation();
                        Util.myLocation(mMap);
                        Log.i(AppConstants.TAG, " else PERMISSION_GRANTED");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "تم منع إذن الوصول الى الموقع", Toast.LENGTH_LONG).show();
                    Log.i(AppConstants.TAG, " permission denied");

                }
                return;
            }
        }
    }

    private void enabeldMyLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(AppConstants.TAG, "SDK_INT >= Build.VERSION_CODES.M");

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Util.getGPSLocation(MainActivity.this);
                Log.i(AppConstants.TAG, "checkSelfPermission");
                mMap.setMyLocationEnabled(true);
                Util.myLocation(mMap);
            } else {
                Util.checkLocationPermission(MainActivity.this);
                Util.getGPSLocation(MainActivity.this);
                Log.i(AppConstants.TAG, "checkLocationPermission");
            }
        } else {
            Log.i(AppConstants.TAG, "SDK_INT < Build.VERSION_CODES.M");
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                Util.myLocation(mMap);
            } else {
                Util.getGPSLocation(MainActivity.this);
                mMap.setMyLocationEnabled(true);
                Util.myLocation(mMap);
            }
        }
    }

    private void getRoomes(Activity activity) {
        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
            @Override
            public void onDataReceived(Object o) {
                boolean status = ((JSONObject) o).optBoolean("status");
                if (status) {
                    JSONArray items = ((JSONObject) o).optJSONArray("items");

                    if (status) {
                        itemList = new Gson().fromJson(items.toString(), new TypeToken<List<Item>>() {
                        }.getType());
                        for (Item item : itemList) {
                            Marker m = mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_point))
                                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                    .position(new LatLng(item.getLocationLatitude(), item.getLocationLongitude())));
                            m.setTag(item);
                        }
//                        itemList = g.fromJson(((JSONObject) o).toString(), type);
                    }
                } else {
                    /******  return to thi point later  and get maage**********/
                    Toast.makeText(MainActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyRequests.ErrorReceiveData() {
            @Override
            public void onErrorDataReceived(VolleyError o) {
                Toast.makeText(MainActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
            }
        }).roomes(MainActivity.this);
    }
}

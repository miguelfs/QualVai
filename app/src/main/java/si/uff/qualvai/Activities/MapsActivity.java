package si.uff.qualvai.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;

import si.uff.qualvai.auxClasses.Place;
import si.uff.qualvai.R;
import si.uff.qualvai.auxClasses.auxMethods;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String FOOD_VARIANT_ID = "si.uff.qualvai.FOOD_VARIANT_ID";
    public static final String OUTDOOR_INDOOR_ID = "si.uff.qualvai.OUTDOOR_INDOOR_ID";
    public static final String BAIRRO_VARIANT_ID = "si.uff.qualvai.BAIRRO_VARIANT_ID";
    public static final String GAME_NAME = "si.uff.qualvai.GAME_NAME";
    public static final String PHOTO_ID1 =  "si.uff.qualvai.PHOTO_ID1";
    public static final String PHOTO_ID2 =  "si.uff.qualvai.PHOTO_ID2";
    int mImageId1;
    int mImageId2;

    public static String PLACE_VARIANT_ID = "si.uff.qualvai.PLACE_VARIANT_ID";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<Place> mArrayPlaces;
    private String mGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mArrayPlaces = auxMethods.generatePlaces();

        //alternative what to get location of user
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .setAccountName("users.account.name@gmail.com")
                .build();
        mGoogleApiClient.connect();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map = googleMap;
        final String[] markerName = new String[1];

        Button mopenMarkerButton = (Button) findViewById(R.id.openMarkerButton);
        if (mLastLocation != null) {
            LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
        }

        Intent intent = getIntent();
        int foodVId = intent.getIntExtra(FOOD_VARIANT_ID, 0);
        int OutdoorVId = intent.getIntExtra(OUTDOOR_INDOOR_ID, 0);
        int BairroVId = intent.getIntExtra(BAIRRO_VARIANT_ID, 0);
        int PlaceVId = intent.getIntExtra(PLACE_VARIANT_ID, 0);
        mImageId1 = intent.getIntExtra(PHOTO_ID1, 0);
        mImageId2 = intent.getIntExtra(PHOTO_ID2, 0);
        mGameName = intent.getStringExtra(GAME_NAME);
        String[] foodVArray = getResources().getStringArray(R.array.food_variant_array);
        String[] outdoorVArray = getResources().getStringArray(R.array.outdoor_array);
        String[] bairroVArray = getResources().getStringArray(R.array.bairro_array);
        String[] placeVArray = getResources().getStringArray(R.array.places_array);

        //set marker for places and set mImage with the pics of the last iterated place
        for(int index = 0; index < auxMethods.generatePlaces().size(); index++){
            if (mArrayPlaces.get(index).customEquals(bairroVArray[BairroVId], placeVArray[PlaceVId], foodVArray[foodVId], outdoorVArray[OutdoorVId])) {
                LatLng latLng = new LatLng(mArrayPlaces.get(index).getLat(), mArrayPlaces.get(index).getLon());
                map.addMarker(new MarkerOptions().position(latLng).title(mArrayPlaces.get(index).getName())).showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
                markerName[0] = mArrayPlaces.get(index).getName();
                mImageId1 = mArrayPlaces.get(index).getIdImage1();
                mImageId2 = mArrayPlaces.get(index).getIdImage2();
            }

            //set the images when the marker is clicked
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    markerName[0] = marker.getTitle();
                    mImageId1 = mArrayPlaces.get(auxMethods.getPlaceIndexByName(marker.getTitle())).getIdImage1();
                    mImageId2 = mArrayPlaces.get(auxMethods.getPlaceIndexByName(marker.getTitle())).getIdImage2();
                    return false;
                }
            });

            //start GoingActivity
            mopenMarkerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this, GoingActivity.class);
                    intent.putExtra(GoingActivity.MARKER_NAME, markerName[0]);
                    intent.putExtra(GoingActivity.GAME_NAME, mGameName);
                    intent.putExtra(MapsActivity.PHOTO_ID1, mImageId1);
                    intent.putExtra(MapsActivity.PHOTO_ID2, mImageId2);
                    startActivity(intent);
                }
            });
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}

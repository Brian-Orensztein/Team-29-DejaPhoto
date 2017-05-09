package com.team29.cse110.team29dejaphoto;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static final long TWO_HOURS = 7200000;
    static final float ONE_K_FT = 305;
    private final String TAG = "MainActivity";

    WallpaperManager background;

    private DisplayCycle displayCycle = new DisplayCycle();


    Button loadPhotosButton; // click to load all photos
    ImageButton buttonLeft;  // click to cycle back
    ImageButton buttonRight; // click to cycle forward

    private final int PERMISSIONS_REQUEST_MEDIA = 1; // int value for permission to access MEDIA
    private final int PERMISSIONS_NEXT_WALLPAPER = 2;     // int value for permission to change to the next wallpaper
    private final int PERMISSIONS_PREV_WALLPAPER = 3; // int value for permission to change to the previous wallpaper

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPhotosButton = (Button) findViewById(R.id.loadPhotos);
        buttonLeft = (ImageButton) findViewById(R.id.leftArrow);
        buttonRight = (ImageButton) findViewById(R.id.rightArrow);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged() called.");
                Log.d(TAG, "Latitude is: " + String.valueOf(location.getLatitude()));
                Log.d(TAG, "Longitude is: " + String.valueOf(location.getLongitude()));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            return;
        }


        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider,
                TWO_HOURS, ONE_K_FT, locationListener);



    }


    public void cycleBack() {
        background = WallpaperManager.getInstance(getApplicationContext());
        DejaPhoto dejaPhoto = displayCycle.getPrevPhoto();
        Log.d(TAG, "Previous Photo was successfully retrieved");
        try {
            background.setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), dejaPhoto.getPhotoUri()));
            Toast.makeText(this, "Displaying Photo: " + dejaPhoto.getPhotoUri(), Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cycleForward() {
        background = WallpaperManager.getInstance(getApplicationContext());
        DejaPhoto dejaPhoto = displayCycle.getNextPhoto();
        Log.d(TAG, "Next Photo was successfully retrieved");
        try {
            background.setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), dejaPhoto.getPhotoUri()));
            Toast.makeText(this, "Displaying Photo: " + dejaPhoto.getPhotoUri(), Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * This is the onClick method for the load images button.
     */
    public void onClickLoadPhotos(View view) {

        loadPhotosButton.setEnabled(false);  /* Disable button immediately so user cannot
                                                repeatedly load all photos */

        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_MEDIA);

    }

    public void changeWallpaper(View view) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SET_WALLPAPER}, PERMISSIONS_NEXT_WALLPAPER);
    }

    public void changeWallpaper2(View view) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SET_WALLPAPER}, PERMISSIONS_PREV_WALLPAPER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch ( requestCode ) {
            case PERMISSIONS_REQUEST_MEDIA : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    loadPhotosIntoDisplayCycle();
                    Toast.makeText(this, "Done Loading Photos", Toast.LENGTH_SHORT).show();
                    return;

                }
                else {
                    Toast.makeText(this, "Error setting permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            case PERMISSIONS_NEXT_WALLPAPER : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Setting Next Wallpaper", Toast.LENGTH_SHORT).show();
                    cycleForward();
                    return;
                }
                else {
                    Toast.makeText(this, "Error setting permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            case PERMISSIONS_PREV_WALLPAPER : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Setting Prev Wallpaper", Toast.LENGTH_SHORT).show();
                    cycleBack();
                    return;
                }
                else {
                    Toast.makeText(this, "Error setting permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            default: {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    /*
     * This method fills a DisplayCycle object with DejaPhoto objects. The method uses a
     * PhotoLoader object to handle the details for retrieving photos.
     */
    private void loadPhotosIntoDisplayCycle() {

        PhotoLoader photoLoader = new DejaPhotoLoader();
        DejaPhoto[] gallery = photoLoader.getPhotosAsArray(this);
        displayCycle.fillDisplayCycle(gallery);

    }

}

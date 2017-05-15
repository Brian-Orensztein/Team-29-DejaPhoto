package com.team29.cse110.team29dejaphoto;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    public static SharedPreferences dejaPreferences; // Holds the reference to the SharedPreferences file
    public static final String DEJA_PREFS = "Deja_Preferences"; // SharedPreference file key
    public static final String IsAppRunning = "IsAppRunning"; // App running key
    public static final String IsDejaVuModeOn = "IsDejaVuModeOn"; // DejaVu mode key
    public static final String IsLocationOn = "IsLocationOn"; // Location key
    public static final String IsTimeOn = "IsTimeOn"; // Time key
    public static final String IsDateOn = "IsDateOn"; // Date key
    public static final String UpdateInterval = "UpdateInterval"; // Update interval key


    boolean useDefaultGallery = true; // By default use the user's custom default album

    /* Declaration of xml UI Design TextViews */
    TextView appToggle;
    TextView dejavuToggle;
    TextView locationToggle;
    TextView timeToggle;
    TextView dateToggle;
    TextView updateIntervalText;
    TextView updateIntervalNumber;

    /* Declaration of xml UI Design Switches */
    Switch appOnOff;
    Switch dejavu;
    Switch location;
    Switch time;
    Switch date;

    /* Declaration of xml UI Design SeekBar */
    SeekBar upDateInterval;

    /* Declaration of the listeners */
    CompoundButton.OnCheckedChangeListener appOnOffSwitchListener;
    CompoundButton.OnCheckedChangeListener dejavuSwitchListener;
    CompoundButton.OnCheckedChangeListener locationSwitchListener;
    CompoundButton.OnCheckedChangeListener timeSwitchListener;
    CompoundButton.OnCheckedChangeListener dateSwitchListener;
    SeekBar.OnSeekBarChangeListener timeInterValListener;

    /* Declaration of xml UI Design Radio */
    RadioGroup radio;

    private final int PERMISSIONS_REQUEST_MEDIA = 1; // int value for permission to access MEDIA
    private final int PERMISSIONS_LOCATION = 2;      // int value for permission to access location
    private final int PERMISSIONS_REQUEST_ALL = 3;   // int value for both permissions combined

    /* Called when the APP is first created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dejaPreferences = getSharedPreferences(DEJA_PREFS, 0); // Instantiate the shared preferences file

        /* Find the ID's for the UI TextViews to be displayed */

        appToggle = (TextView) findViewById(R.id.appSwitch);
        dejavuToggle = (TextView) findViewById(R.id.dejavuText);
        locationToggle = (TextView) findViewById(R.id.locationText);
        timeToggle = (TextView) findViewById(R.id.timeText);
        dateToggle = (TextView) findViewById(R.id.dateText);
        updateIntervalText = (TextView) findViewById(R.id.updateIntervalText);
        updateIntervalNumber = (TextView) findViewById(R.id.updateIntervalNumber);

        /* Find the ID's for the UI Designs to be used and linked to onClicks, listeners, etc */
        appOnOff = (Switch) findViewById(R.id.serviceButton);
        dejavu = (Switch) findViewById(R.id.dejavuMode);
        location = (Switch) findViewById(R.id.location);
        time = (Switch) findViewById(R.id.time);
        date = (Switch) findViewById(R.id.date);
        radio = (RadioGroup) findViewById(R.id.RadioGroup);
        upDateInterval = (SeekBar) findViewById(R.id.updateIntervalBar);

        /* Linker initialization for the switches, toggling if they can be clicked, if they are
         * checked, and updating shared preferences so that the user's preferences are saved
         * when the close and open the app
         */

        appOnOffSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "app is ON");
                    appToggle.setText("DejaPhoto is enabled");
                    toggleSetting(IsAppRunning, true);
                    dejavu.setClickable(true);
                    dejavu.setChecked(true);
                    upDateInterval.setEnabled(true);
                    starter();
                }

                else {
                    Log.d(TAG, "app is OFF");
                    appToggle.setText("DejaPhoto is disabled");
                    Log.d(TAG, "Dejaphoto disable called");
                    toggleSetting(IsAppRunning, false);
                    dejavu.setChecked(false);
                    dejavu.setClickable(false);
                    upDateInterval.setEnabled(false);
                    stopper();
                }
            }
        };

        dejavuSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dejavuToggle.setText("DejaVu enabled");
                    location.setChecked(true);
                    time.setChecked(true);
                    date.setChecked(true);
                    location.setClickable(true);
                    time.setClickable(true);
                    date.setClickable(true);
                    toggleSetting(IsDejaVuModeOn, true);
                }

                else {
                    dejavuToggle.setText("DejaVu disabled");
                    location.setChecked(false);
                    time.setChecked(false);
                    date.setChecked(false);
                    location.setClickable(false);
                    time.setClickable(false);
                    date.setClickable(false);
                    toggleSetting(IsDejaVuModeOn, false);
                }
            }
        };

        locationSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationToggle.setText("Location enabled");
                    toggleSetting(IsLocationOn, true);
                }

                else {
                    locationToggle.setText("Location disabled");
                    toggleSetting(IsLocationOn, false);
                }
            }
        };

        timeSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeToggle.setText("Time enabled");
                    toggleSetting(IsTimeOn, true);
                }

                else {
                    timeToggle.setText("Time disabled");
                    toggleSetting(IsTimeOn, false);
                }
            }
        };

        dateSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateToggle.setText("Date enabled");
                    toggleSetting(IsDateOn, true);
                }

                else {
                    dateToggle.setText("Date disabled");
                    toggleSetting(IsDateOn, false);
                }
            }
        };

        /* Listener for the seek bar to allow user to set their own interval */
        timeInterValListener = new SeekBar.OnSeekBarChangeListener() {

            /* Change the text as the slider changes */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateIntervalNumber.setText((progress + 1)/60 + " hours " + (progress + 1)%60 + " minutes");
            }

            /* Method needs to be overridden */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /* Update shared preferences file once the user is done sliding */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor  = dejaPreferences.edit();
                editor.putInt(UpdateInterval, (seekBar.getProgress() + 1)*60000);
                editor.apply();
            }
        };

        /* Linking listeners to switches */
        appOnOff.setOnCheckedChangeListener(appOnOffSwitchListener);
        dejavu.setOnCheckedChangeListener(dejavuSwitchListener);
        location.setOnCheckedChangeListener(locationSwitchListener);
        time.setOnCheckedChangeListener(timeSwitchListener);
        date.setOnCheckedChangeListener(dateSwitchListener);
        upDateInterval.setOnSeekBarChangeListener(timeInterValListener);

        boolean appRunCheck = dejaPreferences.contains(IsAppRunning); // Has the app started yet

        /* Check if app is supposed to be running */
        if(appRunCheck) {
                appOnOff.setChecked(true);
                appToggle.setText("DejaPhoto is enabled");

            /* Check if the user has Dejavu Mode enabled */
            if(dejaPreferences.getBoolean(IsDejaVuModeOn, true)) {
                dejavu.setOnCheckedChangeListener(null);
                dejavu.setChecked(true);
                dejavu.setOnCheckedChangeListener(dejavuSwitchListener);

                /* Check if user has location enabled */
                if(dejaPreferences.getBoolean(IsLocationOn, true)) {
                    location.setOnCheckedChangeListener(null);
                    location.setChecked(true);
                    location.setOnCheckedChangeListener(locationSwitchListener);
                }

                else {
                    location.setChecked(true);
                    location.setChecked(false);
                }

                /* Check if user has location on */
                if(dejaPreferences.getBoolean(IsTimeOn, true)) {
                    time.setOnCheckedChangeListener(null);
                    time.setChecked(true);
                    time.setOnCheckedChangeListener(timeSwitchListener);
                }

                else {
                    time.setChecked(true);
                    time.setChecked(false);
                }

                /* Check if user has date on */
                if(dejaPreferences.getBoolean(IsDateOn, true)) {
                    date.setOnCheckedChangeListener(null);
                    date.setChecked(true);
                    date.setOnCheckedChangeListener(dateSwitchListener);
                }

                else {
                    date.setChecked(true);
                    date.setChecked(false);
                }
            }

            else {
                dejavu.setChecked(true);
                dejavu.setChecked(false);
            }

            /* Check what update interval the user has set */
            upDateInterval.setProgress(dejaPreferences.getInt(UpdateInterval, 300000)/60000 - 1);
        }

        else {
            Log.d(TAG, "disable app");
            appOnOff.setChecked(true);
            appOnOff.setChecked(false);
            dejavu.setChecked(true);
            dejavu.setChecked(false);
        }
    }

    /* Permissions Handling */

    public boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        }

        return true;
    }

    public void requestAllPermissions() {

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                PERMISSIONS_REQUEST_ALL);
    }

    /* Method to decide what to do if a permission is allowed or denied */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_MEDIA : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                else {
                    Toast.makeText(this, "Error setting permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            case PERMISSIONS_LOCATION : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                else {
                    Toast.makeText(this, "Error setting permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            case PERMISSIONS_REQUEST_ALL : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    starter();
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

    /* Start/Stop Service Toggle Listeners */

    /* Method to call onStartCommand when the user turns on the app */
    public void starter() {
        Log.d(TAG, "Starter button pushed");

        /* Permission was granted so the service can be started */
        if(checkPermissions()) {
            Intent intent = new Intent(MainActivity.this, PhotoService.class);
            startService(intent);

        } else {
            requestAllPermissions();
        }
    }

    /* Method to call onDestroy when the user turns off the app */
    public void stopper() {
        Log.d(TAG, "Stopper button pushed");
        Intent intent = new Intent(MainActivity.this, PhotoService.class);
        stopService(intent);
    }


    /* Method to display the onClick for which gallery to use, only one for now */
    public void onClickRadioButton(View view) {

        switch(view.getId()) {

            case R.id.DefaultAlbum:
                if (useDefaultGallery) {
                    break;
                }

                else {
                    useDefaultGallery = !useDefaultGallery;
                    break;
                }

            case R.id.DejaAlbum:
                if (useDefaultGallery) {
                    useDefaultGallery = !useDefaultGallery;
                    break;
                }

                else {
                    break;
                }
        }
    }

    /*
     * Method to toggle boolean values stored in SharedPreferences. The settingName parameter is the
     * key value for the boolean you want to change.
     */
    private void toggleSetting(String settingName, boolean onOff) {

        SharedPreferences.Editor editor  = dejaPreferences.edit();

        boolean setting = dejaPreferences.getBoolean(settingName, true);
        Log.d(TAG, "" + setting);

        editor.putBoolean(settingName, onOff);

        if ( editor.commit() ) {
            Log.d(TAG, "Successfully edited preferences");
        }
        else {
            Log.d(TAG, "Could not edit preferences");
        }
    }

    /* To be done next milestone */
    public void onCreateCustomAlbum(View view) {

        Intent intent = new Intent(getApplicationContext(), CustomAlbumActivity.class);
        startActivity(intent);
    }
}

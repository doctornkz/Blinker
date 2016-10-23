package local.doctor.blinker;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.hardware.camera2.CameraManager;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String TAG = "Blinker";
    private Boolean existCamera = true;
    private Boolean flashAvailable = false;
    private String cameraId;
    CameraManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate step");
        try {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String[] id = manager.getCameraIdList();
            Log.d(TAG, "Found camera(s) " + Arrays.toString(id));
            for (String camId: id){

                CameraCharacteristics characteristics = manager.getCameraCharacteristics(camId);
                Log.d(TAG, "Checking camera flashlight in camera " + camId);
                flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (flashAvailable){
                    Log.d(TAG, "Found camera flashlight in camera " + camId);
                    cameraId = camId;
                    break;

                } else {
                    Log.d(TAG, "Sorry, your camera " + camId + " doesn't have flashlight!");
                    Toast.makeText(this, "Flashlight not found",Toast.LENGTH_LONG).show();

                }


            }
        } catch (CameraAccessException e) {
            Log.d(TAG, "Failed to interact with camera.", e);

        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (existCamera && flashAvailable) {
                    Log.d(TAG, "Using cam " + cameraId + " on Click step");
                    //200...1000
                    int minimum = 200;
                    int maximum = 1000;
                    int numSignal = 10;
                    //int myRand = minimum + (int)(Math.random() * (maximum - minimum));
                  Toast.makeText(getApplicationContext(), "10 Cycles with random timings", Toast.LENGTH_LONG).show();
                    try {
                        for (int num = 0;  num < numSignal ; num++) {
                            int lenPauseInt = minimum + (int)(Math.random() * (maximum - minimum));
                            Log.d(TAG, "Starting in flashlight cycle. Sleeping " + lenPauseInt + " sec");
                            Thread.sleep(lenPauseInt);
                            int lenSignalInt = minimum + (int)(Math.random() * (maximum - minimum));
                            Log.d(TAG, "Turning on flashlight for "+ Integer.toString(lenSignalInt)+" sec ");
                            manager.setTorchMode(cameraId, true); //Only for 6.0 , API 23
                            Log.d(TAG, "Waiting with flashlight for " + Integer.toString(lenSignalInt) + "sec ");
                            Thread.sleep(lenSignalInt);
                            Log.d(TAG, "Turning off flashlight. Cycle finished");
                            manager.setTorchMode(cameraId, false); //Only for 6.0 , API 23
                            Log.d(TAG, "Cycle number " + Integer.toString(num));

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(TAG, "Camera management failed");
                    }

            }
        }
    });

    }
    public void onStart(){
        super.onStart();

    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause:");
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.d(TAG, "onRestart:");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy:");
    }


}

package local.doctor.blinker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
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
    public Camera camera;
    public CameraManager manager;
    public int minimum = 200;
    public int maximum = 1000;
    public int numSignal = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int apiVer = Build.VERSION.SDK_INT;
        Log.d(TAG, "onCreate step, version API " + Integer.toString(apiVer));
        if (apiVer > 22) {
            camInit22Plus();
        } else {
            camInit22();
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (existCamera && flashAvailable) {
                    if (apiVer > 22) {
                        Log.d(TAG, "Using cam " + cameraId + " on Click step");
                    }
                    Toast.makeText(getApplicationContext(), "10 Cycles with random timings", Toast.LENGTH_LONG).show();
                    try {
                        for (int num = 0;  num <= numSignal ; num++) {
                            int lenPauseInt = minimum + (int)(Math.random() * (maximum - minimum));
                            Log.d(TAG, "Starting in flashlight cycle. Sleeping " + lenPauseInt + " sec");
                            Thread.sleep(lenPauseInt);
                            int lenSignalInt = minimum + (int)(Math.random() * (maximum - minimum));
                            Log.d(TAG, "Turning on flashlight for "+ Integer.toString(lenSignalInt)+" sec ");

                            if (apiVer > 22) {
                                Log.d(TAG, "New API detected");
                                manager.setTorchMode(cameraId, true);
                            } else {
                                try {
                                    Log.d(TAG, "Old API detected. Try to use flashlight");
                                    camera = Camera.open();
                                    final Parameters p = camera.getParameters();
                                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                                    camera.setParameters(p);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d(TAG, "Flashlight goes wrong");
                                }
                            }
                            Log.d(TAG, "Waiting with flashlight for " + Integer.toString(lenSignalInt) + "sec ");
                            Thread.sleep(lenSignalInt);
                            Log.d(TAG, "Turning off flashlight. Cycle finished");

                            if (apiVer > 22) {
                                Log.d(TAG, "New API detected");
                                manager.setTorchMode(cameraId, false);
                            } else {
                                Log.d(TAG, "Old API detected.");
                                try {
                                    final Parameters p = camera.getParameters();
                                    p.setFlashMode(Parameters.FLASH_MODE_OFF);
                                    camera.setParameters(p);
                                    camera.release();
                                    Log.d(TAG, "Flashlight turning off");
                                } catch (Exception ex){
                                    ex.printStackTrace();
                                    Log.d(TAG, "Flashlight turning off failed");
                                }
                            }
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

    private void camInit22() {
        try {
            Log.d(TAG, "API 22 way camera initializing");
            final Context context = this;
            PackageManager pm = context.getPackageManager();
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Log.d(TAG, "onStart:Device has no camera");
                existCamera = false;
            } else {
                flashAvailable = true;
            }

        } catch (Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "Failed to interact with camera.", ex);
        }
    }

    private void camInit22Plus() {
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

package local.doctor.blinker;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.hardware.camera2.CameraManager;

public class MainActivity extends AppCompatActivity {

    private EditText lenPause;
    private EditText lenSignal;
    private EditText numSignal;
    private String TAG = "Blinker";
    private Boolean existCamera = true;
    private CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lenPause = (EditText)findViewById(R.id.lengthPause);
                int lenPauseInt = Integer.parseInt(lenPause.getText().toString());
                lenSignal = (EditText)findViewById(R.id.lenghtSignal);
                int lenSignalInt = Integer.parseInt(lenSignal.getText().toString());
                numSignal = (EditText)findViewById(R.id.numSignal);
                int numSignalInt = Integer.parseInt(numSignal.getText().toString());
                Log.d(TAG, "Timing detected:" + lenPause.getText().toString() + " " + lenSignal.getText().toString() + " " + numSignal.getText().toString());

                if (existCamera) {
                    try {
                        for (int num = 0;  num < numSignalInt ; num++) {
                            Log.d(TAG, "Starting in flashlight cycle. Sleeping " + lenPause.getText().toString() + " sec");
                            Thread.sleep(lenPauseInt * 1000);
                            Log.d(TAG, "Turning on flashlight for " + lenPause.getText().toString() + " sec ");
                            manager.setTorchMode("0", true);
                            Log.d(TAG, "Waiting with flashlight for " + lenSignal.getText().toString() + " sec ");
                            Thread.sleep(lenSignalInt * 1000);
                            Log.d(TAG, "Turning off flashlight. Cycle finished");
                            manager.setTorchMode("0", false);
                        }
                        Log.d(TAG, "onStart:Camera switched ON by switch");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(TAG, "onStart:Switch ON failed");
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

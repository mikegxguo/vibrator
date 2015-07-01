package com.mitac.vibrator;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemVibrator;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class VibratorActivity extends Activity {
    /** Called when the activity is first created. */
    private boolean mHasVibrator = false;
    private boolean bValHigh = false;
    private int valHigh = 127;
    private int valMid = 64;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("feong", "onCreate");
        mHasVibrator = new SystemVibrator().hasVibrator();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                if (mHasVibrator) {
                    if (!bValHigh) {
                        bValHigh = true;
                        new SystemVibrator().change(valHigh);
                        Log.d("feong", "High value");
                    } else {
                        bValHigh = false;
                        new SystemVibrator().change(valMid);
                        Log.d("feong", "Middle value");
                    }
                }
            }
        }, 0, 10);
   }
}

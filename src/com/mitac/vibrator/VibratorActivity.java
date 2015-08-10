package com.mitac.vibrator;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
//import android.os.SystemVibrator;
import android.text.format.Time;
import android.util.Log;
import com.mitac.util.TimeStamp;

import com.mitac.util.Recorder;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VibratorActivity extends Activity {
    /** Called when the activity is first created. */
//    private boolean mHasVibrator = false;
//    private boolean bValHigh = false;
//    private int valHigh = 127;
//    private int valMid = 64;
//    private Timer timer;
    private Context mContext;
//    private static RTC mRTC;
    private Recorder recorder;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("feong", "onCreate");
        
//        mHasVibrator = new SystemVibrator().hasVibrator();
//        timer = new Timer();
//        mContext = this;
//        if(mRTC == null) {
//            mRTC = new RTC();
//        }
        
//        timer.schedule(new TimerTask() {           
//            @Override
//            public void run() {
//                if (mHasVibrator) {
//                    if (!bValHigh) {
//                        bValHigh = true;
//                        new SystemVibrator().change(valHigh);
//                        Log.d("feong", "High value");
//                        //mRTC.getTime();
//                    } else {
//                        bValHigh = false;
//                        new SystemVibrator().change(valMid);
//                        Log.d("feong", "Middle value");
//                        //setTime(0, 100);
//                    }
//                }
//            }
//        }, 0, 10);
        

        recorder = Recorder.read();
        if (recorder == null) {
            recorder = Recorder.getInstance();
        } else if (!recorder.isTesting) {
            // First run
            recorder.delete();
            recorder.init();
            recorder.strTestFolder = CameraActivity.BASE_PATH + File.separator
                    + TimeStamp.getTimeStamp(TimeStamp.TimeType.FULL_S_TYPE);
        } else {
            // Continual run
            recorder.resetTimes++;
            recorder.write();
        }
        
   }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d("feong", "onResume");
        
        //Launch Camera Test
        Intent intent = new Intent();
        intent.setClass(this, CameraActivity.class);
        intent.putExtra(CameraActivity.CAMERA_FACING, 0);
        intent.putExtra(CameraActivity.CAMERA_SHOT_TIMES, 1);
        intent.putExtra(CameraActivity.CAMERA_DELETE_FILE, true);
        startActivity(intent);
    }

    public void getTime() { 
        //DateFormat df = new SimpleDateFormat("HH:mm:ss");
        //Log.d("feong", "Read time: "+ df.format(new Date()));
        
        //Time t=new Time("GMT+8"); 
        //t.setToNow();
        //Log.d("feong", "Read time: "+t.hour+":"+t.minute+":"+t.second);
        
        long time=System.currentTimeMillis();
        //long time = SystemClock.currentThreadTimeMillis();
        final Calendar mCalendar=Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR);
        int min = mCalendar.get(Calendar.MINUTE);
        int sec = mCalendar.get(Calendar.SECOND);
        Log.d("feong", "Read time: "+hour+":"+min+":"+sec);        
    }
    
    public void setTime(int hourOfDay, int minute) {
        Context context =  mContext;
        
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        SystemClock.setCurrentTimeMillis(when);
        // if (when / 1000 < Integer.MAX_VALUE) {
        //     ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        //}
    }
    


}

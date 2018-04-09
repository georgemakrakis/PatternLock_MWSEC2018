package com.example.georgemakrakis.patternlock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.ArrayList;
import java.util.List;

import static android.hardware.Sensor.TYPE_GYROSCOPE;

public class MainActivity extends AppCompatActivity
{
    private PatternLockView mPatternLockView;
    private EditText username;
    private AppCompatButton start_stop_button;
    private TextView pattern_counter_text;
    private ConstraintLayout activity_main_layout;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private SensorEventListener accelListener;
    private Sensor mGyroscope;
    private SensorEventListener gyroListener;
    private Sensor mLinearAccel;
    private SensorEventListener linearAccelListener;

    private List<String> patternsList1 = new ArrayList<>();
    private List<String> patternsList2 = new ArrayList<>();
    private List<Tuple<Float>> coordinatesList = new ArrayList<>();
    private List<Float> pressureList = new ArrayList<>();
    private List<RawPattern> rawPatterns = new ArrayList<>();
    private List<TripleData> accelList = new ArrayList<>();
    private List<TripleData> gyroList = new ArrayList<>();
    private List<TripleData> laccelList = new ArrayList<>();
    private List<SensorData> sensorlList = new ArrayList<>();

    private boolean flagAtThree = false;
    private int renterCounter;
    private int twoIdentical;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        username = (EditText) findViewById(R.id.username);
        start_stop_button = (AppCompatButton) findViewById(R.id.start_stop_button);
        pattern_counter_text = (TextView) findViewById(R.id.pattern_counter_text);

        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        mPatternLockView.setInputEnabled(false);
        pattern_counter_text.setText("Patterns entered until now: ");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLinearAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);


        //Enabling capturing process only if username in not empty
        username.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().trim().length() == 0)
                {
                    start_stop_button.setEnabled(false);
                }
                else
                {
                    start_stop_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
            }
        });

        start_stop_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (start_stop_button.getText().equals("Start"))
                {
                    start_stop_button.setText("Stop");
                    mPatternLockView.setInputEnabled(true);
                    addTouchListener();
                }
                else
                {
                    start_stop_button.setText("Start");
                    mPatternLockView.setInputEnabled(false);
                    patternsList1.clear();
                    patternsList2.clear();
                    pattern_counter_text.setText("Patterns entered until now: ");
                }
            }
        });
    }

    private void addSensorListener(boolean stop)
    {
        if (stop)
        {
            sensorManager.unregisterListener(accelListener);
            sensorManager.unregisterListener(gyroListener);
            sensorManager.unregisterListener(linearAccelListener);
        }
        else
        {
            accelListener = new SensorEventListener()
            {
                @Override
                public void onSensorChanged(SensorEvent event)
                {
                    TripleData accelData = new TripleData(event.values[0], event.values[1], event.values[2]);
                    accelList.add(accelData);
                    //double total = Math.sqrt(x * x + y * y + z * z);
                    Log.i("Info Accel", " X: " + accelData.x + " Y: " + accelData.y + " Z: " + accelData.z);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy)
                {

                }

            };

            gyroListener = new SensorEventListener()
            {
                @Override
                public void onSensorChanged(SensorEvent event)
                {
                    TripleData gyroData = new TripleData(event.values[0], event.values[1], event.values[2]);
                    gyroList.add(gyroData);
                    //double total = Math.sqrt(x * x + y * y + z * z);
                    Log.i("Info Gyro", "X: " + gyroData.x + " Y: " + gyroData.y + " Z: " + gyroData.z);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy)
                {

                }

            };

            linearAccelListener = new SensorEventListener()
            {
                @Override
                public void onSensorChanged(SensorEvent event)
                {
                    TripleData linearAccelData = new TripleData(event.values[0], event.values[1], event.values[2]);
                    laccelList.add(linearAccelData);
                    //double total = Math.sqrt(x * x + y * y + z * z);
                    Log.i("Info Linear Accel", " X: " + linearAccelData.x + " Y: " + linearAccelData.y + " Z: " + linearAccelData.z);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy)
                {

                }

            };

            sensorManager.registerListener(accelListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(gyroListener, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(linearAccelListener, mLinearAccel, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener()
    {
        //activity_main_layout = (ConstraintLayout) findViewById(R.id.activity_main_layout);
        mPatternLockView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                Tuple<Float> coordinate = new Tuple<>(motionEvent.getRawX(), motionEvent.getRawY());
                coordinatesList.add(coordinate);
                //Log.d("TAG", "Coordinates: " + coordinates.x + ", " + coordinates.y);

                pressureList.add(motionEvent.getPressure());
                //Log.d("TAG", "Pressure: "+pressure);

                return false;
            }
        });
    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener()
    {
        @Override
        public void onStarted()
        {
            // TODO: maybe add the check of the 10 patterns here too
            Log.d(getClass().getName(), "Pattern drawing started");
            coordinatesList.clear();
            pressureList.clear();
            accelList.clear();
            gyroList.clear();
            laccelList.clear();

            addSensorListener(false);
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern)
        {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));

            //2.2.1
            RawPatterns(progressPattern);
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern)
        {
            //2.2.2
            SensorPatterns();

            //Removing the listener on ACTION_UP event
            addSensorListener(true);

            //Separating the two sets of patterns
            if (patternsList1.size() < 13)
            {
                RecordPatterns(pattern, patternsList1);
            }
            else if (patternsList2.size() < 13)
            {
                //Maximum 2 identical patterns can appear between the two 10 - sampled sets
                if (twoIdentical != 2)
                {
                    if (patternsList1.contains(pattern))
                    {
                        twoIdentical++;
                        RecordPatterns(pattern, patternsList2);
                    }
                    else
                    {
                        RecordPatterns(pattern, patternsList2);
                    }
                }
                else
                {
                    ShowDialog("You exceeded the maximum 2 identical patterns in both sets. Please enter a different pattern");
                }
            }
            pattern_counter_text.setText("Patterns entered until now: " + (patternsList1.size() + patternsList2.size()));
            Log.i("Info", "Raw Patterns list count: " + rawPatterns.size());
            Log.i("Info", "Sensors list count: " + sensorlList.size());

            //Calling onCleared here to immediate response for the patterns count
            onCleared();
        }

        @Override
        public void onCleared()
        {
            Log.d(getClass().getName(), "Pattern has been cleared");
            //When user reaches the 10 patterns input check if he remembers 3 of them
            if (patternsList1.size() == 10 || patternsList2.size() == 10)
            {
                ShowDialog("Now you must enter 3 of your previous patterns");
                flagAtThree = true;
                renterCounter = 0;
            }
        }
    };

    public void ShowDialog(String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // do nothing
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void RecordPatterns(List<PatternLockView.Dot> pattern, List<String> patternsList)
    {
        if (pattern.size() >= 4)
        {
            String patternNow = PatternLockUtils.patternToString(mPatternLockView, pattern);
            Log.d(getClass().getName(), "Pattern complete: " +
                    patternNow);

            if (flagAtThree && renterCounter < 3)
            {
                if (patternsList.contains(patternNow))
                {
                    patternsList.add(patternNow);
                    renterCounter++;
                }
                else
                {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("The pattern you entered is not one of the previous patterns. Now you have to re-enter another 10 patterns");
                    flagAtThree = false;
                    patternsList.clear();
                    coordinatesList.clear();
                    pressureList.clear();
                    accelList.clear();
                    gyroList.clear();
                    laccelList.clear();
                }
            }
            else
            {
                if (!patternsList.contains(patternNow))
                {
                    patternsList.add(patternNow);
                }
                else
                {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("You have entered the same pattern twice, please enter a different pattern");
                }
            }
            Log.d(getClass().getName(), "Patterns to list 1: " + patternsList1.toString());
            Log.d(getClass().getName(), "Patterns to list 2: " + patternsList2.toString());
        }
        else
        {
            ShowDialog("Pattern must be greater than 4 dots");
        }
    }

    public void RawPatterns(List<PatternLockView.Dot> progressPattern)
    {
        //Getting the last activated point
        String activatedPoint = PatternLockUtils.patternToString(mPatternLockView, progressPattern).substring
                (PatternLockUtils.patternToString(mPatternLockView, progressPattern).length() - 1);
        long timeStamp = SystemClock.elapsedRealtimeNanos();

        rawPatterns.add(new RawPattern(activatedPoint, timeStamp, coordinatesList, pressureList));
    }

    public void SensorPatterns()
    {
        long timeStamp = SystemClock.elapsedRealtimeNanos();

        sensorlList.add(new SensorData(timeStamp,accelList,gyroList,laccelList));
    }
}

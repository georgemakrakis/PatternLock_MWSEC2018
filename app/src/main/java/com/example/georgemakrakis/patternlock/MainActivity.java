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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private Spinner spinnerFinger;
    private Spinner spinnerHand;

    private List<String> patternsList1 = new ArrayList<>();
    private List<String> patternsList2 = new ArrayList<>();
    private List<Tuple<Float>> coordinatesList = new ArrayList<>();
    private List<Float> pressureList = new ArrayList<>();
    private List<RawPattern> rawPatternsList = new ArrayList<>();
    private List<TripleData> accelList = new ArrayList<>();
    private List<TripleData> gyroList = new ArrayList<>();
    private List<TripleData> laccelList = new ArrayList<>();
    private List<SensorData> sensorlList = new ArrayList<>();
    private List<PatternMetadata> patternMetadataList = new ArrayList<>();
    private List<PairMetadata> pairMetadataList = new ArrayList<>();

    private boolean flagAtThree = false;
    private int renterCounter;
    private int twoIdentical;

    private long timeStampDown;
    private long timeStampUp;

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

        String[] itemsHand = new String[]{"Right Hand", "Left Hand"};
        spinnerHand = (Spinner) findViewById(R.id.spinnerHand);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, itemsHand);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHand.setAdapter(adapter);

        String[] itemsFinger = new String[]{"1st Finger", "2nd Finger", "3rd Finger", "4th Finger", "5th Finger"};
        spinnerFinger = (Spinner) findViewById(R.id.spinnerFinger);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, itemsFinger);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFinger.setAdapter(adapter2);

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
                    spinnerFinger.setEnabled(false);
                    spinnerHand.setEnabled(false);
                    addTouchListener();
                }
                else
                {
                    start_stop_button.setText("Start");
                    mPatternLockView.setInputEnabled(false);
                    spinnerFinger.setEnabled(true);
                    spinnerHand.setEnabled(true);
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
                    //Log.d("Info Accel", " X: " + accelData.x + " Y: " + accelData.y + " Z: " + accelData.z);
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
                    //Log.d("Info Gyro", "X: " + gyroData.x + " Y: " + gyroData.y + " Z: " + gyroData.z);
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
                    //Log.d("Info Linear Accel", " X: " + linearAccelData.x + " Y: " + linearAccelData.y + " Z: " + linearAccelData.z);
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
                Log.d("TAG", "Coordinates: " + coordinate.x + ", " + coordinate.y);

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

            timeStampDown = SystemClock.elapsedRealtimeNanos();

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
            timeStampUp = SystemClock.elapsedRealtimeNanos();

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
            Log.i("Info", "Raw Patterns list count: " + rawPatternsList.size());
            Log.i("Info", "Sensors list count: " + sensorlList.size());

            //2.2.3
            Metadata(PatternLockUtils.patternToString(mPatternLockView, pattern),
                    PatternLockUtils.patternToString(mPatternLockView, pattern).length());

            Log.i("Info", "Pattern metadata count: " + patternMetadataList.size());

            PairMetadata(PatternLockUtils.patternToString(mPatternLockView, pattern));

            Log.i("Info", "Pair metadata count: " + pairMetadataList.size());

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

        rawPatternsList.add(new RawPattern(activatedPoint, timeStamp, coordinatesList, pressureList));
    }

    public void SensorPatterns()
    {
        long timeStamp = SystemClock.elapsedRealtimeNanos();

        sensorlList.add(new SensorData(timeStamp, accelList, gyroList, laccelList));
    }

    public void Metadata(String sequense, int sequenceLength)
    {
        List<Tuple<Float>> pointsList = new ArrayList<>();
        int numOfPoints = (int) (coordinatesList.size() * 0.6);

        //Coping the list so we can remove objects later
        List<Tuple<Float>> coordinatesListCopy = coordinatesList;

        //Adding the first and the last point of the pattern just to be sure
        pointsList.add(coordinatesListCopy.get(0));
        pointsList.add(coordinatesListCopy.get(coordinatesListCopy.size() - 1));

        //Then iterating through the other points
        for (int i = 0; i < numOfPoints; i++)
        {
            Random r = new Random();
            int index = r.nextInt(((coordinatesListCopy.size() - 1) - 1) + 1) + 1;

            pointsList.add(coordinatesListCopy.get(index));
            coordinatesListCopy.remove(index);
        }

        double distance = 0.0;
        for (int i = 1; i < pointsList.size(); i++)
        {
            float tempFirst = pointsList.get(i - 1).x;
            float tempSecond = pointsList.get((i)).x;
            float tempYFirst = pointsList.get(i - 1).y;
            float tempYSecond = pointsList.get((i)).y;

            float xValue = tempFirst - tempSecond;
            float yValue = tempYFirst - tempYSecond;

            double tempX2 = Math.pow(xValue, 2);
            double tempY2 = Math.pow(yValue, 2);

            distance += Math.sqrt((tempX2 + tempY2));
        }
        Log.i("Info", "Pattern Distance: " + distance);

        double pressureSum = 0;
        for (float p : pressureList)
        {
            pressureSum += p;
        }

        int handNum = 0;
        if (spinnerHand.getSelectedItem().equals("Right Hand"))
        {
            handNum = 2;
        }
        else if (spinnerHand.getSelectedItem().equals("Left Hand"))
        {
            handNum = 1;
        }

        int fingerNum = 0;
        if (spinnerFinger.getSelectedItem().equals("1st Finger"))
        {
            fingerNum = 1;
        }
        else if (spinnerFinger.getSelectedItem().equals("2nd Finger"))
        {
            fingerNum = 2;
        }
        else if (spinnerFinger.getSelectedItem().equals("3rd Finger"))
        {
            fingerNum = 3;
        }
        else if (spinnerFinger.getSelectedItem().equals("4th Finger"))
        {
            fingerNum = 4;
        }
        else if (spinnerFinger.getSelectedItem().equals("5th Finger"))
        {
            fingerNum = 5;
        }

        //Patterns Lists size here represents the sequence number of the pattern the user enters
        PatternMetadata patternMetadata = new PatternMetadata(
                username.getText().toString(), patternsList1.size() + patternsList2.size(), sequense, sequenceLength, timeStampUp - timeStampDown,
                distance, distance / (timeStampUp - timeStampDown), (float) pressureSum / pressureList.size(), Collections.max(pressureList),
                Collections.min(pressureList), handNum, fingerNum);

        patternMetadataList.add(patternMetadata);

    }

    public void PairMetadata(String pattern)
    {
        //Getting the screen resolution
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String resolution = metrics.heightPixels + ", " + metrics.widthPixels;

        for (int i = 0; i < pattern.length()-1; i++)
        {
            int A = Character.getNumericValue(pattern.charAt(i));
            int B = Character.getNumericValue(pattern.charAt(i + 1));

            int columnA = PatternLockView.Dot.of(A).getColumn();
            int rowA = PatternLockView.Dot.of(A).getRow();

            int columnB = PatternLockView.Dot.of(B).getColumn();
            int rowB = PatternLockView.Dot.of(B).getRow();

            Tuple<Float> centralCoordsA = new Tuple<>(getCenterXForColumn(columnA), getCenterYForRow(rowA));
            Tuple<Float> centralCoordsB = new Tuple<>(getCenterXForColumn(columnB), getCenterYForRow(rowB));

            Tuple<Float> firstCoordsA = null;
            Tuple<Float> lastCoordsB = null;

            for (RawPattern rP : rawPatternsList)
            {
                if (rP.getNumberOfActivatedPoint().equals(Integer.toString(A)))
                {
                    firstCoordsA = rP.getCoordinates().get(0);
                    break;
                }
            }

            for (RawPattern rP : rawPatternsList)
            {
                if (rP.getNumberOfActivatedPoint().equals(Integer.toString(B)))
                {
                    lastCoordsB = rP.getCoordinates().get(rP.getCoordinates().size()-1);
                    break;
                }
            }

            double distanceAB = Math.hypot(firstCoordsA.x - lastCoordsB.x, firstCoordsA.y - lastCoordsB.y);

            Float pressureSum = 0.0f;
            for (RawPattern rP : rawPatternsList)
            {
                if (rP.getNumberOfActivatedPoint().equals(Integer.toString(A)))
                {
                    for (Float pressure : rP.getPressure())
                    {
                        pressureSum += pressure;
                    }
                }
                else if (rP.getNumberOfActivatedPoint().equals(Integer.toString(B)))
                {
                    for (Float pressure : rP.getPressure())
                    {
                        pressureSum += pressure;
                    }
                }
            }

            PairMetadata pairMetadata = new PairMetadata(
                    username.getText().toString(), patternsList1.size() + patternsList2.size(),
                    resolution, A, B, centralCoordsA, centralCoordsB, firstCoordsA, lastCoordsB,
                    distanceAB, timeStampUp - timeStampDown,
                    distanceAB / timeStampUp - timeStampDown, pressureSum / pressureList.size());

            pairMetadataList.add(pairMetadata);
        }
    }

    //These functions come from the source code of the initial PatternLockView project (https://github.com/aritraroy/PatternLockView)
    private float getCenterXForColumn(int column)
    {
        return mPatternLockView.getPaddingLeft() + column * mPatternLockView.getWidth() + mPatternLockView.getWidth() / 2f;
    }

    private float getCenterYForRow(int row)
    {
        return mPatternLockView.getPaddingTop() + row * mPatternLockView.getHeight() + mPatternLockView.getHeight() / 2f;
    }
}

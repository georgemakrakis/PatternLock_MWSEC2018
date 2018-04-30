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
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private List<RawPattern> rawPatternsTempList = new ArrayList<>();
    private List<TripleData> accelList = new ArrayList<>();
    private List<TripleData> gyroList = new ArrayList<>();
    private List<TripleData> laccelList = new ArrayList<>();
    private List<SensorData> sensorlList = new ArrayList<>();
    private List<PatternMetadata> patternMetadataList = new ArrayList<>();
    private List<PairMetadata> pairMetadataList = new ArrayList<>();
    private ArrayList<String> rawPatternsList = new ArrayList<>();
    private List<List<String>> finalRawPatternFile = new ArrayList<>();

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
                    coordinatesList.clear();
                    pressureList.clear();
                    rawPatternsTempList.clear();
                    accelList.clear();
                    gyroList.clear();
                    laccelList.clear();
                    sensorlList.clear();
                    patternMetadataList.clear();
                    pairMetadataList.clear();
                    rawPatternsList.clear();
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
                if (RecordPatterns(pattern, patternsList1))
                {
                    //2.2.3
                    Metadata(PatternLockUtils.patternToString(mPatternLockView, pattern),
                            PatternLockUtils.patternToString(mPatternLockView, pattern).length());

                    Log.i("Info", "Pattern metadata count: " + patternMetadataList.size());

                    PairMetadata(PatternLockUtils.patternToString(mPatternLockView, pattern));

                    Log.i("Info", "Pair metadata count: " + pairMetadataList.size());
                }
            }
            else if (patternsList2.size() < 13)
            {
                //Maximum 2 identical patterns can appear between the two 10 - sampled sets
                if (twoIdentical != 2)
                {
                    if (patternsList1.contains(pattern))
                    {
                        twoIdentical++;
                        if (RecordPatterns(pattern, patternsList2))
                        {
                            //2.2.3
                            Metadata(PatternLockUtils.patternToString(mPatternLockView, pattern),
                                    PatternLockUtils.patternToString(mPatternLockView, pattern).length());

                            Log.i("Info", "Pattern metadata count: " + patternMetadataList.size());

                            PairMetadata(PatternLockUtils.patternToString(mPatternLockView, pattern));

                            Log.i("Info", "Pair metadata count: " + pairMetadataList.size());
                        }

                    }
                    else
                    {
                        if (RecordPatterns(pattern, patternsList2))
                        {
                            //2.2.3
                            Metadata(PatternLockUtils.patternToString(mPatternLockView, pattern),
                                    PatternLockUtils.patternToString(mPatternLockView, pattern).length());

                            Log.i("Info", "Pattern metadata count: " + patternMetadataList.size());

                            PairMetadata(PatternLockUtils.patternToString(mPatternLockView, pattern));

                            Log.i("Info", "Pair metadata count: " + pairMetadataList.size());
                        }
                    }
                }
                else
                {
                    ShowDialog("You exceeded the maximum 2 identical patterns in both sets. Please enter a different pattern");
                }
            }
            pattern_counter_text.setText("Patterns entered until now: " + (patternsList1.size() + patternsList2.size()));
            Log.i("Info", "Raw Patterns list count: " + rawPatternsTempList.size());
            Log.i("Info", "Sensors list count: " + sensorlList.size());


            //Calling onCleared here to immediate response for the patterns count
            onCleared();
        }

        @Override
        public void onCleared()
        {
            Log.d(getClass().getName(), "Pattern has been cleared");
            if (patternsList1.size() == 13 && patternsList2.size() == 0)
            {
                ShowDialog("Continue with 10 more patterns");
            }
            //When user reaches the 10 patterns input check if he remembers 3 of them
            if (patternsList1.size() == 10 || patternsList2.size() == 10)
            {
                ShowDialog("Now you must enter 3 of your previous patterns");
                flagAtThree = true;
                renterCounter = 0;
            }

            if (patternsList1.size() + patternsList2.size() == 26)
            {
                ShowDialog("You made it! Check your statistics!");
                //TODO save data and move to the next page
                finalRawPatternFile.add(rawPatternsList);
                //writeRawPatternFile();
                //writeSensorDataFiles();
                writeMetadataFile();
            }

            finalRawPatternFile.add((List<String>) rawPatternsList.clone());

            rawPatternsList.clear();
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

    public boolean RecordPatterns(List<PatternLockView.Dot> pattern, List<String> patternsList)
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
                    Log.d(getClass().getName(), "Patterns to list 1: " + patternsList1.toString());
                    Log.d(getClass().getName(), "Patterns to list 2: " + patternsList2.toString());

                    return true;
                }
                else
                {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("The pattern you entered is not one of the previous patterns. Now you have to re-enter another 10 patterns");
                    flagAtThree = false;
                    patternsList.clear();
                    coordinatesList.clear();
                    pressureList.clear();
                    rawPatternsTempList.clear();
                    accelList.clear();
                    gyroList.clear();
                    laccelList.clear();
                    sensorlList.clear();
                    patternMetadataList.clear();
                    pairMetadataList.clear();

                    rawPatternsList.clear();

                    return false;
                }
            }
            else
            {
                if (!patternsList.contains(patternNow))
                {
                    patternsList.add(patternNow);

                    Log.d(getClass().getName(), "Patterns to list 1: " + patternsList1.toString());
                    Log.d(getClass().getName(), "Patterns to list 2: " + patternsList2.toString());

                    return true;
                }
                else
                {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("You have entered the same pattern twice, please enter a different pattern");

                    return false;
                }
            }

        }
        else
        {
            ShowDialog("Pattern must be greater than 4 dots");

            return false;
        }
    }

    public void RawPatterns(List<PatternLockView.Dot> progressPattern)
    {
        //Getting the last activated point
        String activatedPoint = PatternLockUtils.patternToString(mPatternLockView, progressPattern).substring
                (PatternLockUtils.patternToString(mPatternLockView, progressPattern).length() - 1);
        long timeStamp = SystemClock.elapsedRealtimeNanos();

        rawPatternsTempList.add(new RawPattern(activatedPoint, timeStamp, coordinatesList, pressureList));

        rawPatternsList.add(rawPatternsTempList.get(rawPatternsTempList.size() - 1).toString());

        coordinatesList.clear();
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

        for (int i = 0; i < pattern.length() - 1; i++)
        {
            int A = Character.getNumericValue(pattern.charAt(i));
            int B = Character.getNumericValue(pattern.charAt(i + 1));

            int columnA = PatternLockView.Dot.of(A).getColumn();
            int rowA = PatternLockView.Dot.of(A).getRow();

            int columnB = PatternLockView.Dot.of(B).getColumn();
            int rowB = PatternLockView.Dot.of(B).getRow();

            Tuple<Float> centralCoordsA = new Tuple<>(getCenterXForColumn(columnA), getCenterYForRow(rowA));
            Tuple<Float> centralCoordsB = new Tuple<>(getCenterXForColumn(columnB), getCenterYForRow(rowB));

//            Log.d("Coord dot",""+centralCoordsA.x+","+centralCoordsA.y);
//            Log.d("Coord dot",""+centralCoordsB.x+","+centralCoordsB.y);

            Tuple<Float> firstCoordsA = null;
            Tuple<Float> lastCoordsB = null;

            for (RawPattern rP : rawPatternsTempList)
            {
                if (rP.getNumberOfActivatedPoint().equals(Integer.toString(A)))
                {
                    firstCoordsA = rP.getCoordinates().get(0);
                    break;
                }
            }

            for (RawPattern rP : rawPatternsTempList)
            {
                if (rP.getNumberOfActivatedPoint().equals(Integer.toString(B)))
                {
                    lastCoordsB = rP.getCoordinates().get(rP.getCoordinates().size() - 1);
                    break;
                }
            }

            double distanceAB = Math.hypot(firstCoordsA.x - lastCoordsB.x, firstCoordsA.y - lastCoordsB.y);

            Float pressureSum = 0.0f;
            for (RawPattern rP : rawPatternsTempList)
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

    public void writeRawPatternFile()
    {
        String rootPath = getFilesDir() + "/" + username.getText() + "/";
        File root = new File(rootPath);
        if (!root.exists())
        {
            root.mkdirs();
        }
        try
        {
            //For the Raw Patterns File
            for (int i = 0; i < finalRawPatternFile.size(); i++)
            {
                FileWriter writer = null;

                File csv = new File(rootPath + username.getText() + "_" + i + "_raw.csv");
                writer = new FileWriter(csv);

                String header = "number_of_activated_point;timestamp;xpoint;ypoint;pressure\n";
                writer.write(header);
                for (int j = 0; j < finalRawPatternFile.get(i).size(); j++)
                {
                    writer.write(finalRawPatternFile.get(i).get(j));
                }
                writer.close();
            }
//            for (List<String> r: finalRawPatternFile)
//            {
//                for(int i=0;i<r.size();i++)
//                {
//                    Log.i("Infoooooo", r.get(i));
//                }
//
//            }

        }
        catch (IOException e)
        {
            Log.e("Error", e.toString());
        }
    }

    public void writeSensorDataFiles()
    {

        String rootPath = getFilesDir() + "/" + username.getText() + "/";
        File root = new File(rootPath);
        if (!root.exists())
        {
            root.mkdirs();
        }

        try
        {
            for (int i = 0; i < sensorlList.size(); i++)
            {
                FileWriter writer = null;

                File csv = new File(rootPath + username.getText() + "_" + i + "_sensors.csv");
                writer = new FileWriter(csv);

                String header = "timestamp;accel_x;accel_y;accel_z;gyro_x;gyro_y;gyro_z;laccel_x;laccel_y;laccel_z\n";
                writer.write(header);

                writer.write(sensorlList.get(i).toString());

                writer.close();

//                Log.d("Timestamp", sensorlList.get(i).getTimeStamp() + "");
//                for (TripleData a : sensorlList.get(i).getAccelData())
//                {
//                    Log.d("Accel X", a.x + "");
//                    Log.d("Accel Y", a.y + "");
//                    Log.d("Accel Z", a.z + "");
//                }
//
//                for (TripleData a : sensorlList.get(i).getGyroData())
//                {
//                    Log.d("Gyro X", a.x + "");
//                    Log.d("Gyro Y", a.y + "");
//                    Log.d("Gyro Z", a.z + "");
//                }
//
//                for (TripleData a : sensorlList.get(i).getLaccelData())
//                {
//                    Log.d("Laccel X", a.x + "");
//                    Log.d("Laccel Y", a.y + "");
//                    Log.d("Laccel Z", a.z + "");
//                }
                //Log.d("Debuuuug",sensorlList.get(i).toString());
            }

        }
        catch (IOException e)
        {
            Log.e("Error", e.toString());
        }


    }

    public void writeMetadataFile()
    {
        String rootPath = getFilesDir() + "/" + username.getText() + "/";
        File root = new File(rootPath);
        if (!root.exists())
        {
            root.mkdirs();
        }

        try
        {

            FileWriter writer = null;

            File csv = new File(rootPath + username.getText() + "_metadata.csv");
            writer = new FileWriter(csv);

            String header = "Username;Attempt_number;Sequence;Seq_length;Time_to_complete;" +
                    "PatternLength;Avg_speed;Avg_pressure;Highest_pressure;Lowest_pressure;HandNum;" +
                    "FingerNum\n";
            writer.write(header);

            for(int i=0;i<patternMetadataList.size();i++)
            {
                writer.write(patternMetadataList.get(i).toString());
            }

            writer.close();

//            for(int i=0;i<patternMetadataList.size();i++)
//            {
//                Log.d("Debugggg",patternMetadataList.get(i).toString());
//            }
        }
        catch (IOException e)
        {
            Log.e("Error", e.toString());
        }
    }
}

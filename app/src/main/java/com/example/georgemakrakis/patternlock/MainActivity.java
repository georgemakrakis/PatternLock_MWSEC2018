package com.example.georgemakrakis.patternlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
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

public class MainActivity extends AppCompatActivity
{
    private PatternLockView mPatternLockView;
    private EditText username;
    private AppCompatButton start_stop_button;
    private TextView pattern_counter_text;
    private ConstraintLayout activity_main_layout;

    private List<String> patternsList1 = new ArrayList<>();
    private List<String> patternsList2 = new ArrayList<>();
    private boolean flagAtThree = false;
    private int renterCounter;
    private int twoIdentical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        username = (EditText) findViewById(R.id.username);
        start_stop_button = (AppCompatButton) findViewById(R.id.start_stop_button);
        pattern_counter_text = (TextView) findViewById(R.id.pattern_counter_text);

        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        mPatternLockView.setInputEnabled(false);
        pattern_counter_text.setText("Patterns entered until now: ");

        //Enabling capturing process only if username in not empty
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    start_stop_button.setEnabled(false);
                } else {
                    start_stop_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        start_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_stop_button.getText().equals("Start")) {
                    start_stop_button.setText("Stop");
                    mPatternLockView.setInputEnabled(true);
                    addTouchListener();
                } else {
                    start_stop_button.setText("Start");
                    mPatternLockView.setInputEnabled(false);
                    patternsList1 = new ArrayList<>();
                    patternsList2 = new ArrayList<>();
                    pattern_counter_text.setText("Patterns entered until now: ");
                }
            }
        });
    }

    private void addTouchListener()
    {
        //activity_main_layout = (ConstraintLayout) findViewById(R.id.activity_main_layout);
        mPatternLockView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                Log.d("TAG","Coordinates: "+x+","+y);

                return false;
            }
        });
    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener()
    {
        @Override
        public void onStarted() {
            // TODO: maybe add the check of the 10 patterns here too
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));

            //Getting the last activated point
            String activatedPoint = PatternLockUtils.patternToString(mPatternLockView, progressPattern).substring
                    (PatternLockUtils.patternToString(mPatternLockView, progressPattern).length() - 1);
            long timeStamp = SystemClock.elapsedRealtimeNanos();

        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            //Separating the two sets of patterns
            if (patternsList1.size() < 13) {
                RecordPatterns(pattern, patternsList1);
            } else if (patternsList2.size() < 13) {
                //Maximum 2 identical patterns can appear between the two 10 - sampled sets
                if (twoIdentical != 2) {
                    if (patternsList1.contains(pattern)) {
                        twoIdentical++;
                        RecordPatterns(pattern, patternsList2);
                    } else {
                        RecordPatterns(pattern, patternsList2);
                    }
                } else {
                    ShowDialog("You exceeded the maximum 2 identical patterns in both sets. Please enter a different pattern");
                }
            }
            pattern_counter_text.setText("Patterns entered until now: " + (patternsList1.size() + patternsList2.size()));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
            //When user reaches the 10 patterns input check if he remembers 3 of them
            if (patternsList1.size() == 10 || patternsList2.size() == 10) {
                ShowDialog("Now you must enter 3 of your previous patterns");
                flagAtThree = true;
                renterCounter = 0;
            }
        }
    };

    public void ShowDialog(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void RecordPatterns(List<PatternLockView.Dot> pattern, List<String> patternsList) {
        if (pattern.size() >= 4) {
            String patternNow = PatternLockUtils.patternToString(mPatternLockView, pattern);
            Log.d(getClass().getName(), "Pattern complete: " +
                    patternNow);

            if (flagAtThree && renterCounter < 3) {
                if (patternsList.contains(patternNow)) {
                    patternsList.add(patternNow);
                    renterCounter++;
                } else {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("The pattern you entered is not one of the previous patterns. Now you have to re-enter another 10 patterns");
                    flagAtThree = false;
                    patternsList.clear();
                }
            } else {
                if (!patternsList.contains(patternNow)) {
                    patternsList.add(patternNow);
                } else {
                    //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                    ShowDialog("You have entered the same pattern twice, please enter a different pattern");
                }
            }
            Log.d(getClass().getName(), "Patterns to list 1: " + patternsList1.toString());
            Log.d(getClass().getName(), "Patterns to list 2: " + patternsList2.toString());
        } else {
            ShowDialog("Pattern must be greater than 4 dots");
        }
    }
}

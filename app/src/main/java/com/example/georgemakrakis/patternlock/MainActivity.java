package com.example.georgemakrakis.patternlock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private PatternLockView mPatternLockView;
    private EditText username;
    private AppCompatButton start_stop_button;
    private List<String> patternsList = new ArrayList<>();
    private List<String> patternsList2 = new ArrayList<>();
    private boolean flagAtThree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        username = (EditText) findViewById(R.id.username);
        start_stop_button = (AppCompatButton) findViewById(R.id.start_stop_button);

        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

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

        start_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(start_stop_button.getText().equals("Start"))
                {
                    start_stop_button.setText("Stop");
                }
                else
                {
                    start_stop_button.setText("Start");
                }
            }
        });
    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener()
    {
        @Override
        public void onStarted()
        {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern)
        {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern)
        {
            if(pattern.size()>=4)
            {
                String patternNow = PatternLockUtils.patternToString(mPatternLockView, pattern);
                Log.d(getClass().getName(), "Pattern complete: " +
                    patternNow);

                if(flagAtThree)
                {
                    if(patternsList.contains(patternNow))
                    {
                        patternsList.add(patternNow);
                    }
                    else
                    {
                        //Log.d(getClass().getName(), "You have entered the same pattern twice, please enter a different pattern");
                        ShowDialog("The pattern you entered is not one of the previous patterns");
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

                    Log.d(getClass().getName(), "Patterns to list: " + patternsList.toString());
                }
            }
        }

        @Override
        public void onCleared()
        {
            Log.d(getClass().getName(), "Pattern has been cleared");
            //When user reaches the 10 patterns input check if he remembers 3 of them
            if(patternsList.size()==10)
            {
                ShowDialog("Now you must enter 3 of your previous patterns");
                flagAtThree = true;
            }
        }
    };
    public void ShowDialog(String Message)
    {
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
}

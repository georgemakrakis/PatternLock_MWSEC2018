package com.example.georgemakrakis.patternlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        List<String> patternsList1 = getIntent().getStringArrayListExtra("patternsList1");

        List<String> patternsList2 = getIntent().getStringArrayListExtra("patternsList2");

        Log.d("Debug",getLongRuns(patternsList1, patternsList2)+"");
    }

    public int getLongRuns(List<String> patternsList1, List<String> patternsList2)
    {
        int longRunsCounter = 0;
        String[] allLongRuns = {
                "036", "630", "147", "741", "258", "852",
                "012", "210", "345", "543", "678", "876",
                "048", "840", "246", "642"
        };

        for (String pattern : patternsList1)
        {
            for (String longRun : allLongRuns)
            {
                if (pattern.contains(longRun))
                {
                    longRunsCounter++;
                }
            }
        }

        for (String pattern : patternsList2)
        {
            for (String longRun : allLongRuns)
            {
                if (pattern.contains(longRun))
                {
                    longRunsCounter++;
                }
            }
        }

        return longRunsCounter;

    }
}

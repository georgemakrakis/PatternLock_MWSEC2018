package com.example.georgemakrakis.patternlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity
{
    private TextView longRuns;
    private TextView closedCurves;
    private TextView longCurves;
    private TextView longEdges;
    private TextView shortEdges;
    private TextView longOrthEdges;
    private TextView shortOrthEdges;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        longRuns = (TextView) findViewById(R.id.pattern_counter_text);
        closedCurves = (TextView) findViewById(R.id.pattern_counter_text);
        longCurves = (TextView) findViewById(R.id.pattern_counter_text);
        longEdges = (TextView) findViewById(R.id.pattern_counter_text);
        shortEdges = (TextView) findViewById(R.id.pattern_counter_text);
        longOrthEdges = (TextView) findViewById(R.id.pattern_counter_text);
        shortOrthEdges = (TextView) findViewById(R.id.pattern_counter_text);

        List<String> patternsList1 = getIntent().getStringArrayListExtra("patternsList1");
        List<String> patternsList2 = getIntent().getStringArrayListExtra("patternsList2");

        //Log.d("Debug",getLongRuns(patternsList1, patternsList2)+"");

        longRuns.setText("Long runs in your patterns: ");
        closedCurves.setText("Closed curves in your patterns: ");
        longCurves.setText("Long curves in your patterns: ");
        longEdges.setText("Longs edges in your patterns: ");
        shortEdges.setText("Short edges in your patterns: ");
        longOrthEdges.setText("Long orthogonal edges in your patterns: ");
        shortOrthEdges.setText("Long orthogonal edges in your patterns: ");

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

    public int getClosedRuns(List<String> patternsList1, List<String> patternsList2)
    {
        int closedCurvesCounter = 0;
        String[] allClosedCurves = {
                "0341", "1430", "3674", "4763", "1452", "2541",
                "4785", "5874", "1034", "4301", "4367", "7634",
                "1254", "4521", "4785", "5874"
        };

        for (String pattern : patternsList1)
        {
            for (String longRun : allClosedCurves)
            {
                if (pattern.contains(longRun))
                {
                    closedCurvesCounter++;
                }
            }
        }

        for (String pattern : patternsList2)
        {
            for (String longRun : allClosedCurves)
            {
                if (pattern.contains(longRun))
                {
                    closedCurvesCounter++;
                }
            }
        }

        return closedCurvesCounter;
    }

    public int[] getNumberFreq(List<String> patternsList1, List<String> patternsList2)
    {
        int[] numberFreq = new int[10];
        for (int i = 0; i < numberFreq.length; i++)
        {
            numberFreq[i] = 0;
        }

        for (String pattern : patternsList1)
        {
            for(int i=0;i<pattern.length();i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        for (String pattern : patternsList2)
        {
            for(int i=0;i<pattern.length();i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        return  numberFreq;
    }
}

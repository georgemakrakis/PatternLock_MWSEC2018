package com.example.georgemakrakis.patternlock;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity
{

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView longRuns = (TextView) findViewById(R.id.longRuns);
        TextView closedCurves = (TextView) findViewById(R.id.closedCurves);
        TextView longCurves = (TextView) findViewById(R.id.longCurves);
        TextView longEdges = (TextView) findViewById(R.id.longEdges);
        TextView shortEdges = (TextView) findViewById(R.id.shortEdges);
        TextView longOrthEdges = (TextView) findViewById(R.id.longOrthEdges);
        TextView shortOrthEdges = (TextView) findViewById(R.id.shortOrthEdges);
        LinearLayout NumFreqList = (LinearLayout) findViewById(R.id.NumFreqList);
        TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        LinearLayout patternScoreList = (LinearLayout) findViewById(R.id.patternScoreList);

        List<String> patternsList1 = getIntent().getStringArrayListExtra("patternsList1");
        List<String> patternsList2 = getIntent().getStringArrayListExtra("patternsList2");

        //Log.d("Debug",getLongRuns(patternsList1, patternsList2)+"");


        //Our probable values of every desired pattern "schema" we want
        String[] allLongRuns = {
                "036", "630", "147", "741", "258", "852",
                "012", "210", "345", "543", "678", "876",
                "048", "840", "246", "642"
        };
        String[] allClosedCurves = {
                "0341", "1452", "3674", "4785", "1034", "4367", "2145", "5478", "3014", "4125",
                "0347", "7458", "3410", "4521", "6743", "7854", "4587", "3476", "1254", "0143",
                "8547", "7430", "5214", "4103", "8745", "5412", "7634", "4301", "5874", "4763",
                "2541", "1430"
        };

        String[] allLongCurves = {
                "0341", "1452", "3674", "4785", "1034", "4367", "2145", "5478", "3014", "4125",
                "0347", "7458", "3410", "4521", "6743", "7854", "4587", "3476", "1254", "0143",
                "8547", "7430", "5214", "4103", "8745", "5412", "7634", "4301", "5874", "4763",
                "2541", "1430"
        };

        String[] allLongEdges = {
                "63048", "64258", "64210", "04876", "67840", "01246", "85246", "84036"
        };

        String[] allShortEdges = {
                "314", "425", "647", "758", "403", "514", "847", "736", "043", "154", "376", "487",
                "310", "421", "643", "754", "457", "346", "124", "013", "784", "673", "451", "340",
                "637", "748", "415", "304", "857", "746", "524", "413", "072", "270", "618", "816",
                "615", "516", "813", "318", "273", "372", "075", "570"
        };

        String[] allLongOrthEdges = {
                "03678", "01258", "25878", "21036", "63012", "87852", "85210", "87630"
        };

        String[] allShortOrthEdges = {
                "034", "145", "367", "478", "143", "254", "476", "587", "301", "412", "634", "754",
                "410", "521", "743", "854", "458", "347", "125", "014", "457", "436", "214", "103",
                "785", "674", "452", "341", "874", "763", "541", "430"
        };


        int longRunsNum = getStatData(patternsList1, patternsList2, allLongRuns);
        int closedCurvesNum = getStatData(patternsList1, patternsList2, allClosedCurves);
        int longCurvesNum = getStatData(patternsList1, patternsList2, allLongCurves);
        int longEdgesNum = getStatData(patternsList1, patternsList2, allLongEdges);
        int shortEdgesNum = getStatData(patternsList1, patternsList2, allShortEdges);
        int shortOrthEdgesNum = getStatData(patternsList1, patternsList2, allShortOrthEdges);
        int longOrthEdgesNum = getStatData(patternsList1, patternsList2, allLongOrthEdges);

        longRuns.setText("Long runs in your patterns: " + longRunsNum);
        closedCurves.setText("Closed curves in your patterns: " + closedCurvesNum);
        longCurves.setText("Long curves in your patterns: " + longCurvesNum);
        longEdges.setText("Longs edges in your patterns: " + longEdgesNum);
        shortEdges.setText("Short edges in your patterns: " + shortEdgesNum);
        longOrthEdges.setText("Long orthogonal edges in your patterns: " + longOrthEdgesNum);
        shortOrthEdges.setText("Short orthogonal edges in your patterns: " + shortOrthEdgesNum);

        //Populating the number frequency listview
        int[] numberFreq = getNumberFreq(patternsList1, patternsList2);
        for (int i = 0; i < numberFreq.length - 1; i++)
        {
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText(i + " --> " + Integer.toString(numberFreq[i]) + " times");
            text.setTextColor(Color.parseColor("#ffffff"));
            NumFreqList.addView(text);
        }

        int[] scores1 = getPatternScore(patternsList1,allLongRuns, allClosedCurves,allLongCurves,
                allLongEdges, allShortEdges, allLongOrthEdges, allShortOrthEdges);
        int[] scores2 = getPatternScore(patternsList2,allLongRuns, allClosedCurves,allLongCurves,
                allLongEdges, allShortEdges, allLongOrthEdges, allShortOrthEdges);

        for (int i = 0; i < scores1.length - 1; i++)
        {
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText(patternsList1.get(i)+"-->"+scores1[i]);
            text.setTextColor(Color.parseColor("#ffffff"));
            patternScoreList.addView(text);
        }
        for (int i = 0; i < scores2.length - 1; i++)
        {
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText(patternsList2.get(i)+"-->"+scores2[i]);
            text.setTextColor(Color.parseColor("#ffffff"));
            patternScoreList.addView(text);
        }

        scoreTextView.setText("Your overall score is: "+getOverallScore(patternsList1,patternsList2,
                longRunsNum,closedCurvesNum,longCurvesNum,longEdgesNum,shortEdgesNum,
                shortOrthEdgesNum,longOrthEdgesNum));
    }

    public int getStatData(List<String> patternsList1, List<String> patternsList2, String[] data)
    {
        int counter = 0;

        for (String pattern : patternsList1)
        {
            for (String longRun : data)
            {
                if (pattern.contains(longRun))
                {
                    counter++;
                }
            }
        }

        for (String pattern : patternsList2)
        {
            for (String longRun : data)
            {
                if (pattern.contains(longRun))
                {
                    counter++;
                }
            }
        }

        return counter;

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
            for (int i = 0; i < pattern.length(); i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        for (String pattern : patternsList2)
        {
            for (int i = 0; i < pattern.length(); i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        return numberFreq;
    }

    public int getOverallScore(List<String> patternsList1, List<String> patternsList2, int longRunsNum,
                        int closedCurvesNum, int longCurvesNum, int longEdgesNum, int shortEdgesNum,
                        int shortOrthEdgesNum, int longOrthEdgesNum)
    {
        //For each extra number the user gets +1 point
        int points = 0;
        for (String pattern : patternsList1)
        {
            points += pattern.length() - 4;
        }
        for (String pattern : patternsList2)
        {
            points += pattern.length() - 4;
        }

        //For each orthogonal edges get +1, as so the closed curves and short edges
        points += shortOrthEdgesNum + shortEdgesNum + closedCurvesNum;
        //And all others get +2 points
        points += (longRunsNum+longCurvesNum+longEdgesNum+longOrthEdgesNum)*2;

        return points;
    }

    public int [] getPatternScore(List<String> patternsList,  String[] allLongRuns,
                                  String [] allClosedCurves,String[] allLongCurves,
                                  String[] allLongEdges,  String[] allShortEdges,
                                  String[] allLongOrthEdges,String[] allShortOrthEdges)
    {
        int points[] = new int[13];
        for (int i = 0; i < points.length; i++)
        {
            points[i] = 0;
        }

        for (int i=0;i<patternsList.size();i++)
        {
            points[i]+=patternsList.get(i).length()-4;
            for (String el : allLongRuns)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=2;
                }
            }
            for (String el : allClosedCurves)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=2;
                }
            }
            for (String el : allLongCurves)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=3;
                }
            }
            for (String el : allLongEdges)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=3;
                }
            }
            for (String el : allShortEdges)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=2;
                }
            }
            for (String el : allLongOrthEdges)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=3;
                }
            }
            for (String el : allShortOrthEdges)
            {
                if (patternsList.get(i).contains(el))
                {
                    points[i]+=2;
                }
            }

        }

        return points;
    }
}
